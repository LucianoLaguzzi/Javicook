#!/bin/bash

# Directorio persistente para las imágenes
PERSISTENT_DIR="/opt/jboss/src/main/webapp/img/fotos"

# Directorio base donde están las carpetas temporales de WildFly
BASE_DIR="/opt/jboss/wildfly/standalone/tmp/vfs/temp"

# Iniciar WildFly en segundo plano
/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 &

# Obtener el PID del proceso de WildFly
WILDFLY_PID=$!

# Esperar a que WildFly esté completamente iniciado
sleep 30  # Puedes ajustar este tiempo según tus necesidades

# Buscar la carpeta correcta dentro de /opt/jboss/wildfly/standalone/tmp/vfs/temp/
TARGET_DIR=$(find "$BASE_DIR" -type d -path "*/content-*/img/fotos" 2>/dev/null)

# Verificar si se encontró el directorio temporal de imágenes
if [ -z "$TARGET_DIR" ]; then
    echo "No se encontró el directorio temporal de imágenes en WildFly"
    exit 1
else
    # Copiar las imágenes desde el directorio persistente al directorio temporal
    cp -r "$PERSISTENT_DIR"/* "$TARGET_DIR"
    echo "Imágenes copiadas a $TARGET_DIR"
fi

# Esperar a que WildFly termine (debería estar ejecutándose en segundo plano)
wait $WILDFLY_PID
