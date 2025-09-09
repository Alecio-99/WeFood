#!/usr/bin/env bash
set -euo pipefail

echo "[run_tests] Limpando target..."
./mvnw clean

echo "[run_tests] Rodando testes (profile test)…"
./mvnw -q -Dspring.profiles.active=test -DskipTests=false test

echo "[run_tests] Testes finalizados"
