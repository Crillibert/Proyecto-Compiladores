# Usa una imagen base de OpenJDK 21 (cambia si usas otra versión)
FROM openjdk:21-jdk-slim AS build

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Copiar solo el archivo pom.xml para descargar dependencias primero
COPY api/pom.xml /app/api/

# Descargar dependencias (esto evita que Render las tenga que bajar cada vez)
RUN mvn dependency:go-offline -f /app/api/pom.xml

# Copiar el código fuente del proyecto
COPY api/ /app/api/

# Construir la aplicación con Maven
RUN mvn clean package -f /app/api/pom.xml

# Segunda fase: Crear una imagen limpia para ejecutar el JAR
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copiar el JAR compilado desde la fase anterior
COPY --from=build /app/api/target/api-1.0-SNAPSHOT.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]


