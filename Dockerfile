# Estágio 1: Build da Aplicação com Maven
# Usamos uma imagem oficial do Maven com Java 21 para compilar nosso projeto.
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar o cache de dependências do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte
COPY src ./src

# Executa o build do Maven, gerando o arquivo .war
RUN mvn package -DskipTests

# ---

# Estágio 2: Execução da Aplicação com Tomcat
# Usamos uma imagem oficial do Tomcat que já vem com o Java 21.
FROM tomcat:10.1-jdk21-temurin

# Remove a aplicação de exemplo que vem com o Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o arquivo .war que foi gerado no estágio de build para a pasta de deploy do Tomcat.
# O Tomcat irá automaticamente "descompactar" e executar nossa aplicação.
COPY --from=builder /app/target/pi2biblios.war /usr/local/tomcat/webapps/ROOT.war

# Expõe a porta 8080, que é a porta padrão do Tomcat.
EXPOSE 8080

# O comando para iniciar o servidor já está embutido na imagem base do Tomcat.