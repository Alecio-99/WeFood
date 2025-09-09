#!/usr/bin/env bash
set -euo pipefail

# Configurações (para uso sem Compose)
IMAGE_NAME="${IMAGE_NAME:-wefood-app}"
CONTAINER_NAME="${CONTAINER_NAME:-wefood_app}"
PORT_MAP="${PORT_MAP:-8080:8080}"
DOCKERFILE_PATH="${DOCKERFILE_PATH:-Dockerfile}"
BUILD_CONTEXT="${BUILD_CONTEXT:-.}"
EXTRA_RUN_ARGS="${EXTRA_RUN_ARGS:-}"
RESTART_POLICY="${RESTART_POLICY:-unless-stopped}"
USE_NO_CACHE="${USE_NO_CACHE:-false}"
PUBLISH_LATEST="${PUBLISH_LATEST:-true}"

# Configurações (para uso com Compose)
COMPOSE_FILE="${COMPOSE_FILE:-docker-compose.yml}"
COMPOSE_SERVICE="${COMPOSE_SERVICE:-}"
COMPOSE_PROJECT_NAME="${COMPOSE_PROJECT_NAME:-}"

# Tag automático: git SHA ou timestamp
GIT_SHA="$(git rev-parse --short HEAD 2>/dev/null || true)"
if [[ -n "${GIT_SHA}" ]]; then
  TAG="${TAG:-${GIT_SHA}}"
else
  TAG="${TAG:-$(date +%Y%m%d-%H%M%S)}"
fi

log() { printf "\n\033[1;34m[deploy]\033[0m %s\n" "$*"; }
container_exists() { docker ps -a --format '{{.Names}}' | grep -qx "$1"; }

has_compose_file() {
  [[ -f "${COMPOSE_FILE}" || -f "docker-compose.yaml" || -f "compose.yml" || -f "compose.yaml" ]]
}

which_compose_cmd() {
  if docker compose version >/dev/null 2>&1; then
    echo "docker compose"
  elif command -v docker-compose >/dev/null 2>&1; then
    echo "docker-compose"
  else
    echo ""
  fi
}

# Deploy via Docker Compose (se existir)
if has_compose_file; then
  COMPOSE_CMD="$(which_compose_cmd)"
  if [[ -z "${COMPOSE_CMD}" ]]; then
    echo "Erro: Arquivo de compose encontrado, mas nenhum comando 'docker compose' ou 'docker-compose' disponível."
    exit 1
  fi

  ARGS=(-f "${COMPOSE_FILE}")
  [[ -n "${COMPOSE_PROJECT_NAME}" ]] && ARGS+=(-p "${COMPOSE_PROJECT_NAME}")

  log "Arquivo de Compose detectado: usando '${COMPOSE_CMD}'."
  log "Recriando serviços com build…"
  if [[ -n "${COMPOSE_SERVICE}" ]]; then
    ${COMPOSE_CMD} "${ARGS[@]}" up -d --build --force-recreate "${COMPOSE_SERVICE}"
  else
    ${COMPOSE_CMD} "${ARGS[@]}" up -d --build --force-recreate
  fi

  log "Status dos serviços:"
  ${COMPOSE_CMD} "${ARGS[@]}" ps
  log "Deploy concluído via Compose!"
  exit 0
fi

# Fluxo tradicional (build/run)
log "Nenhum arquivo de Compose detectado. Seguindo com docker build/run."

BUILD_ARGS=()
[[ "${USE_NO_CACHE}" == "true" ]] && BUILD_ARGS+=(--no-cache)

log "Construindo imagem: ${IMAGE_NAME}:${TAG}"
docker build -f "${DOCKERFILE_PATH}" -t "${IMAGE_NAME}:${TAG}" "${BUILD_ARGS[@]}" "${BUILD_CONTEXT}"

if [[ "${PUBLISH_LATEST}" == "true" ]]; then
  log "Criando tag adicional: ${IMAGE_NAME}:latest"
  docker tag "${IMAGE_NAME}:${TAG}" "${IMAGE_NAME}:latest"
fi

if container_exists "${CONTAINER_NAME}"; then
  log "Parando e removendo container antigo: ${CONTAINER_NAME}"
  docker stop "${CONTAINER_NAME}" >/dev/null || true
  docker rm "${CONTAINER_NAME}" >/dev/null || true
fi

log "Subindo container: ${CONTAINER_NAME}"
docker run -d \
  --name "${CONTAINER_NAME}" \
  -p "${PORT_MAP}" \
  --restart "${RESTART_POLICY}" \
  ${EXTRA_RUN_ARGS} \
  "${IMAGE_NAME}:${TAG}"

log "Container em execução:"
docker ps --filter "name=${CONTAINER_NAME}"

log "Limpando imagens 'dangling' (se houver)…"
docker image prune -f >/dev/null || true

log "Deploy concluído! -> ${IMAGE_NAME}:${TAG}"
