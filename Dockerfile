# Etapa de construcción
FROM gradle:latest AS build
WORKDIR /home/gradle/project

# Copiar todo el proyecto, no solo src/
COPY --chown=gradle:gradle . .

# Dar permisos de ejecución a gradlew
RUN chmod +x gradlew

# Ejecutar construcción con el wrapper de Gradle
RUN ./gradlew clean bootJar

# Etapa de empaquetado
FROM openjdk:17
WORKDIR /app

# Copiar el archivo JAR generado
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Exponer el puerto (Render establece el puerto automáticamente)
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]