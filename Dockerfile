# Estágio 1: Build
FROM bellsoft/liberica-openjdk-debian:25 AS build
WORKDIR /app

# Instala o Maven para garantir a compilação
RUN apt-get update && apt-get install -y maven

# Copia o código fonte
COPY . .

# Gera o arquivo .jar ignorando os testes (para agilizar o deploy no Jenkins)
RUN mvn clean package -DskipTests

# Estágio 2: Runtime
FROM bellsoft/liberica-openjdk-debian:25
WORKDIR /app

# Copia o jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Define variáveis de ambiente para o Spring (ou você pode passar no comando de run)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
