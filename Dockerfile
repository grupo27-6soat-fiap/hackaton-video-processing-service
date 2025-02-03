# FROM openjdk:17
# ADD target/api-0.0.1-SNAPSHOT.jar api-0.0.1-SNAPSHOT.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "api-0.0.1-SNAPSHOT.jar"]

# Usando uma imagem base do Maven para compilar a aplicação
FROM maven:3.8.4-openjdk-17 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo pom.xml e as dependências
COPY pom.xml .

# Faz o download das dependências
RUN mvn dependency:go-offline

# Copia o código-fonte do projeto
COPY src ./src

# Compila o projeto
RUN mvn package -DskipTests

# Usando uma imagem base do JDK para rodar a aplicação
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o JAR gerado da fase de build
COPY --from=build /app/target/*.jar app.jar

# Define o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]