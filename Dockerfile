# Estágio 1: Compilação (Build)
# Usamos a imagem do JDK 25 para suportar a versão do seu pom.xml
FROM openjdk:25-slim AS build
WORKDIR /app

# Copia todos os arquivos do projeto para dentro do container
COPY . .

# Dá permissão de execução para o wrapper do Maven
RUN chmod +x mvnw

# Executa o build usando o wrapper (ele vai baixar o Maven sozinho)
RUN ./mvnw clean package -DskipTests

# Estágio 2: Execução (Runtime)
FROM openjdk:25-slim
WORKDIR /app

# Copia apenas o arquivo .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
