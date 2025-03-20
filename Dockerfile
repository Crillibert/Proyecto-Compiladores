# Usa una imagen base de OpenJDK 24
FROM openjdk:24-jdk-slim

# Establecer el directorio de trabajo dentro del contenedor en /app
WORKDIR /app

# Copiar el JAR generado al contenedor desde el directorio target
COPY api/target/api-1.0-SNAPSHOT.jar /app/api-1.0-SNAPSHOT.jar

# Exponer el puerto en el que la aplicación Spring Boot estará corriendo
EXPOSE 8080

# Ejecutar la aplicación Spring Boot (usando el archivo JAR generado)
CMD ["java", "-jar", "api-1.0-SNAPSHOT.jar"]
