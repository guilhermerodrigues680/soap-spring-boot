# Spring Web Service SOAP

## Projeto no repl.it

- [soap-spring-boot.guilhermerodri8.repl.co](https://soap-spring-boot.guilhermerodri8.repl.co)

## Gerando o pacote JAR e executando o projeto

````shell
# Comandos executados na raiz do projeto

# Gerando classes JAVA a partir do arquivo `src/main/resources/countries.xsd`
# output: target/generated-sources/jaxb
./mvnw compile 

# Gerando pacote jar
./mvnw clean package -DskipTests

# Executando pacote jar
java -jar target/producing-web-service-0.0.1-SNAPSHOT.jar

# Executando pacote jar em uma porta
java -jar target/producing-web-service-0.0.1-SNAPSHOT.jar --server.port=8010

# Executando projeto sem criar pacote jar
./mvnw spring-boot:run

# Executando projeto sem criar pacote jar em uma porta
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8010
````

---

## Erros conhecidos

Caso ocorra o erro: `package com.example.contrato has already been annotated` apague a pasta `src/main/java/com/example/contrato` e tente fazer a compilação novamente.

---
