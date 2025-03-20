# Usar una imagen base con Java y Maven
FROM maven:3.8.4-openjdk-11-slim AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el código fuente del proyecto al contenedor
COPY . .

# Ejecutar el comando Maven para compilar el proyecto (usamos mvn clean install)
RUN --mount=type=cache,id=m2-repo,target=/root/.m2 mvn clean install -X

# Exponer el puerto que la aplicación usará (ajusta el puerto si es necesario)
EXPOSE 8080

# Comando para ejecutar la aplicación una vez que se haya construido
CMD ["java", "-jar", "api/target/api-1.0-SNAPSHOT.jar"]
