# Usa una imagen base de OpenJDK 24
FROM openjdk:24-jdk-slim AS build

# Establecer el directorio de trabajo dentro del contenedor en /app
WORKDIR /app

# Copiar el archivo mvnw y darle permisos de ejecución
COPY api/mvnw /app/api/mvnw
RUN chmod +x /app/api/mvnw

# Copiar los archivos de la carpeta `api` al directorio de trabajo del contenedor
COPY api/ /app/api/

# Copiar las librerías si es necesario (ajustar si se requieren archivos desde lib)
COPY lib/ /app/lib/

# Ejecutar Maven usando el wrapper
RUN ./api/mvnw clean install

# Exponer el puerto en el que la aplicación Spring Boot estará corriendo (por defecto: 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot (usando el archivo JAR generado)
CMD ["java", "-jar", "api/target/*.jar"]

