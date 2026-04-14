# Usamos uma imagem que permite rodar o Maven com qualquer JDK instalado
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .

# AJUSTE NO POM: Se você puder, mude no seu pom.xml <java.version> para 21.
# Se PRECISA ser 25, use a estratégia abaixo:

# Estágio de Build (Compilação)
FROM openjdk:25-slim AS build
WORKDIR /app
COPY . .
# Usando o wrapper do maven (mvnw) que costuma vir no projeto Spring
RUN ./mvnw clean package -DskipTests

# Estágio de Execução
FROM openjdk:25-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
