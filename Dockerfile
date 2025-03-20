# Usa una imagen base de OpenJDK 24
FROM openjdk:24-jdk-slim AS build

# Establecer el directorio de trabajo dentro del contenedor en la carpeta `api`
WORKDIR /app/api

# Copiar los archivos del backend (incluyendo pom.xml y código fuente)
COPY api/ /app/api/

# Copiar las librerías si es necesario (ajustar si se requieren archivos desde lib)
COPY lib/ /app/lib/

# Ejecutar Maven para compilar el proyecto (usando Maven Wrapper si lo tienes)
RUN ./mvnw clean install

# Exponer el puerto en el que la aplicación Spring Boot estará corriendo (puerto predeterminado: 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot (usando el archivo JAR generado)
CMD ["java", "-jar", "target/*.jar"]
