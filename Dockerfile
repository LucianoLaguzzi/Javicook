FROM quay.io/wildfly/wildfly:25.0.1.Final

# Establecer variables de entorno
ENV DEPLOYMENT_DIR=/opt/jboss/wildfly/standalone/deployments/
ENV CONFIGURATION_DIR=/opt/jboss/wildfly/standalone/configuration/

# Crear la estructura de directorios necesaria
RUN mkdir -p /opt/jboss/src/main/webapp/img/fotos/

# Copiar el WAR generado por Maven a la carpeta de despliegue de WildFly
COPY target/JaviCook-1.0-SNAPSHOT.war $DEPLOYMENT_DIR

# Copiar el archivo JAR del controlador JDBC
COPY wildfly-25.0.1.Final/modules/system/layers/base/com/mysql/main/mysql-connector-java-5.1.48.jar /opt/jboss/wildfly/modules/com/mysql/jdbc/main/

# Copiar el archivo module.xml
COPY wildfly-25.0.1.Final/modules/system/layers/base/com/mysql/main/module.xml /opt/jboss/wildfly/modules/com/mysql/jdbc/main/

# Copiar el standalone.xml personalizado
COPY wildfly-25.0.1.Final/standalone/configuration/standalone.xml $CONFIGURATION_DIR

# Copiar el script de inicialización
COPY sync-images.sh /opt/jboss/

# Exponer el puerto 8080 para acceder a la app
EXPOSE 8080

# Cambiar el comando para ejecutar WildFly usando el script de inicialización
CMD ["/opt/jboss/sync-images.sh"]