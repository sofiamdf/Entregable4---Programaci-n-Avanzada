#!/bin/bash

echo "Iniciando deploy en Mac..."

# Carpeta de deployment
DEPLOY_DIR="/tmp/deploy_playlist"

# Ruta al jar dentro del proyecto
JAR_SOURCE="../target/playlist-0.0.1-SNAPSHOT.jar"

# Destino final
JAR_DEST="$DEPLOY_DIR/playlist.jar"

# Crear carpeta si no existe
mkdir -p "$DEPLOY_DIR"

# Verificar que el jar existe
if [ ! -f "$JAR_SOURCE" ]; then
  echo "ERROR: No se encontró el archivo $JAR_SOURCE"
  exit 1
fi

# Copiar el jar
cp -f "$JAR_SOURCE" "$JAR_DEST"

echo "Deploy completado correctamente."
echo "El jar fue copiado a: $JAR_DEST"
