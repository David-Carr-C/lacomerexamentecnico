FROM openjdk:21-jdk-slim

# Instalar dependencias
RUN apt-get update && apt-get install -y maven

# Copiar el archivo pom.xml y el código fuente
WORKDIR /app
COPY pom.xml /app/
COPY src /app/src

# Construir el proyecto
RUN mvn clean package -DskipTests
EXPOSE 8080

# Copiar el archivo jar generado al contenedor
COPY target/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

#FROM openjdk:21-jdk-slim
#COPY target/*.jar /app/app.jar
#WORKDIR /app
#EXPOSE 8080
#CMD ["java", "-jar", "app.jar"]
