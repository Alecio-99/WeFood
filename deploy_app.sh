#!/usr/bin/env bash
set -euo pipefail

# ===== Config (ajuste se precisar) =====
SERVICE_APP="${SERVICE_APP:-app}"            # nome do serviço da app no docker-compose.yml
COMPOSE_FILE="${COMPOSE_FILE:-docker-compose.yml}"
COMPOSE_PROJECT_NAME="${COMPOSE_PROJECT_NAME:-}"

IMAGE_NAME="${IMAGE_NAME:-wefood-app}"       # para modo sem compose
CONTAINER_NAME="${CONTAINER_NAME:-wefood_app}"
PORT_MAP="${PORT_MAP:-8080:8080}"
DOCKERFILE_PATH="${DOCKERFILE_PATH:-Dockerfile}"
BUILD_CONTEXT="${BUILD_CONTEXT:-.}"
EXTRA_RUN_ARGS="${EXTRA_RUN_ARGS:-}"
RESTART_POLICY="${RESTART_POLICY:-unless-stopped}"
USE_NO_CACHE="${USE_NO_CACHE:-false}"
PUBLISH_LATEST="${PUBLISH_LATEST:-true}"

# ===== Tag =====
GIT_SHA="$(git rev-parse --short HEAD 2>/dev/null || true)"
if [[ -n "$GIT_SHA" ]]; then TAG="${TAG:-$GIT_SHA}"; else TAG="${TAG:-$(date +%Y%m%d-%H%M%S)}"; fi

log() { printf "\n[deploy-app] %s\n" "$*"; }
has_compose_file(){ [[ -f "$COMPOSE_FILE" || -f docker-compose.yaml || -f compose.yml || -f compose.yaml ]]; }
which_compose_cmd(){
  if docker compose version >/dev/null 2>&1; then echo "docker compose";
  elif command -v docker-compose >/dev/null 2>&1; then echo "docker-compose";
  else echo ""; fi
}

# ===== Fluxo Compose: só a app (sem deps) =====
if has_compose_file; then
  CMD="$(which_compose_cmd)"
  [[ -z "$CMD" ]] && { echo "Compose file encontrado, mas nenhum 'docker compose'/'docker-compose' disponível."; exit 1; }

  ARGS=()
  [[ -f "$COMPOSE_FILE" ]] && ARGS+=(-f "$COMPOSE_FILE")
  [[ -n "$COMPOSE_PROJECT_NAME" ]] && ARGS+=(-p "$COMPOSE_PROJECT_NAME")

  log "Recriando apenas o serviço '$SERVICE_APP' (sem deps)…"
  $CMD "${ARGS[@]}" up -d --build --force-recreate --no-deps "$SERVICE_APP"

  log "Status:"
  $CMD "${ARGS[@]}" ps
  log "OK (Compose app)."
  exit 0
fi

# ===== Fluxo sem Compose: build/run só da app =====
log "Sem Compose: build/run apenas da app."
BUILD_ARGS=(); [[ "$USE_NO_CACHE" == "true" ]] && BUILD_ARGS+=(--no-cache)

log "Build imagem: $IMAGE_NAME:$TAG"
docker build -f "$DOCKERFILE_PATH" -t "$IMAGE_NAME:$TAG" "${BUILD_ARGS[@]}" "$BUILD_CONTEXT"

if [[ "$PUBLISH_LATEST" == "true" ]]; then
  log "Tag extra: $IMAGE_NAME:latest"
  docker tag "$IMAGE_NAME:$TAG" "$IMAGE_NAME:latest"
fi

# parar/remover somente o container da app, mantendo banco
if docker ps -a --format '{{.Names}}' | grep -qx "$CONTAINER_NAME"; then
  log "Parando/removendo container antigo da app: $CONTAINER_NAME"
  docker stop "$CONTAINER_NAME" >/dev/null || true
  docker rm "$CONTAINER_NAME" >/dev/null || true
fi

log "Subindo container da app (sem tocar no MySQL)…"
docker run -d --name "$CONTAINER_NAME" -p "$PORT_MAP" --restart "$RESTART_POLICY" $EXTRA_RUN_ARGS "$IMAGE_NAME:$TAG"

log "Containers da app:"
docker ps --filter "name=$CONTAINER_NAME"

log "Limpeza de images dangling (se houver)…"
docker image prune -f >/dev/null || true

log "OK (app) -> $IMAGE_NAME:$TAG"
