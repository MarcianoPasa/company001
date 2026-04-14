# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build 
# Nota: Como o Java 25 é muito novo, usamos a imagem do 21 ou 
# uma imagem personalizada com o JDK 25.
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio de Execução
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
