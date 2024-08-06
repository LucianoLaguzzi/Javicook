# Utiliza la imagen oficial de WildFly desde Quay.io
FROM quay.io/wildfly/wildfly:25.0.1.Final

# Establece el directorio de trabajo
WORKDIR /home/jboss/javicook

# Copia el archivo .war al contenedor
COPY target/JaviCook-1.0-SNAPSHOT.war /home/jboss/javicook/

# Expone el puerto que usará WildFly
EXPOSE 8080

# Comando para iniciar WildFly
CMD ["standalone.sh", "-b", "0.0.0.0"]