# Usa una imagen base de OpenJDK 21 (JDK 24 no es estable en muchas plataformas)
FROM openjdk:21-jdk-slim AS build

# Establecer el directorio de trabajo dentro del contenedor en /app
WORKDIR /app

# Instalar Maven globalmente (si es necesario)
RUN apt-get update && apt-get install -y maven

# Copiar el código fuente del backend y las dependencias
COPY api/ /app/api/

# Construir la aplicación con Maven
RUN mvn clean package -f /app/api/pom.xml

# Usar una imagen limpia para ejecutar el JAR
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copiar el JAR generado desde la fase de construcción
COPY --from=build /app/api/target/api-1.0-SNAPSHOT.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

