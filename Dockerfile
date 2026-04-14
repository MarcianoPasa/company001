# Estágio 1: Compilação (Build)
FROM container-registry.oracle.com/java/jdk:25 AS build
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Garante permissão para o Maven Wrapper
# No Windows o comando chmod pode falhar no Docker, mas o RUN fará o trabalho no Linux do container
RUN chmod +x mvnw

# Executa o build (o ./mvnw vai baixar o Maven necessário)
RUN ./mvnw clean package -DskipTests

# Estágio 2: Execução (Runtime)
FROM container-registry.oracle.com/java/jdk:25
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
