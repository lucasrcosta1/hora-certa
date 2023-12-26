# hora-certa API

**hora-certa-api** é uma aplicação REST que visa facilitar o gerenciamento de dados de horas trabalhadas. Isto é, se você controla seu próprio horário de serviço, essa é a plataforma ideal para gerenciamento dos seus horários.

A função dessa aplicação é o armazenamento de horas trabalhadas, tal como a geração de estatísticas personalizadas de folgas, férias e horários trabalhados.

## Padões de projeto:

A arquitetura utilizada segue os padrões [*Chain of Responsibility*](https://refactoring.guru/design-patterns/chain-of-responsibility) e [*Command*](https://refactoring.guru/design-patterns/command) para execução das funcionalidades
Sendo assim, temos as seguintes "camadas" de código:

- **Auxiliary:** lida com toda a lógica secundária do serviço, conversão de horário, hash de senhas, etc;
- **Controllers:** recebe as chamadas externas da api, onde estão armazenados os endpoints;
- **Services:** encapsula funcionalidades (ou validações) genéricas que podem ser utilizadas por vários commands;
- **Entities** são as classes manipuladas dentro dos fluxos de execução;
- 
Antes de desenvolver, fique atento à organização dos pacotes.

## Regras básicas para desenvolvimento:

- Todas as PRs com precisam de ao menos 2 aprovações;
- A cobertura de testes do projeto nao pode ser inferior a 80% para o geral e **100%  para novos códigos**
- Novas integrações devem ter testes integrados e mapeados no wiremock
- Atente-se aos conceitos de **SOLID**. Principalmente ao de responsabilidade única.

## Execução da aplicação:

```
$ ./mvn clean install && docker-compose -f db/docker-compose.yml up
```

Este comando constroe a aplicação e inicia o container de banco de dados.


### Exemplos de uso

Para acessar a aplicação após subir o docker, vá em:

http://localhost:8999/swagger-ui/index.html


## Tech stack

Tecnologias utlizadas:


- **Java 17**+
- **JDK 17**+
- **Spring Boot 3.2.0**
- **Spring JPA, Open API 3(swagger)**
- **Maven 4.0.0**
- **Docker** e **Docker Compose 2**
- **Postgres 14.1**
