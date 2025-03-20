# Usa una imagen base de OpenJDK 24
FROM openjdk:24-jdk-slim AS build

# Establecer el directorio de trabajo dentro del contenedor en /app
WORKDIR /app

# Instalar Maven globalmente
RUN apt-get update && apt-get install -y maven

# Copiar los archivos del proyecto al directorio de trabajo
COPY api/ /app/api/
COPY lib/ /app/lib/

# Ejecutar Maven para construir el proyecto (aunque ahora ya está hecho, solo por si hay que repetirlo en el futuro)
RUN mvn clean install -f /app/api/pom.xml

# Exponer el puerto en el que la aplicación Spring Boot estará corriendo (por defecto: 8080)
EXPOSE 8080

# Ejecutar la aplicación Spring Boot (usando el archivo JAR generado)
CMD ["java", "-jar", "api/target/api-1.0-SNAPSHOT.jar"]
