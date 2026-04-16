# Estágio 1: Build
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Copia os arquivos do Maven (pom.xml) e baixa as dependências (cache)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

# Copia o código fonte e gera o JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Estágio 2: Runtime (Imagem final mais leve)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copia apenas o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta configurada no seu yaml
EXPOSE 8081

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
