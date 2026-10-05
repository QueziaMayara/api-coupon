# Coupon Manager

Projeto desenvolvido para gerenciamento de cupons, com uma API REST utilizando Java e Spring Boot.

A API permite criar e excluir cupons, possui validações das regras de negócio e utiliza exclusão lógica para manter os registros no banco de dados.

## Como rodar o projeto

### Pré-requisitos

Para executar o projeto, é necessário ter instalado:

* Java
* Maven
* Docker
* Docker Compose

### Subindo o projeto

Primeiro, clone o repositório:

git clone <URL_DO_REPOSITORIO>
cd coupon-manager

Depois, suba os serviços necessários com o Docker:

docker compose up -d

Com os containers em execução, rode a aplicação:

./mvnw spring-boot:run

No Windows:

bash
mvnw.cmd spring-boot:run

A aplicação ficará disponível em:

http://localhost:8080

### Testes

Para executar os testes:

./mvnw test

No Windows:

bash
mvnw.cmd test

### Endpoints principais

**Criar um cupom**

http
POST /v1/coupons

Exemplo de requisição:

{
  "code": "ABC123",
  "description": "Cupom de desconto",
  "discountValue": 10.00,
  "expirationDate": "2026-12-31T23:59:59",
  "published": true
}

**Excluir um cupom**

http
DELETE /v1/coupons/{id}

A exclusão é lógica, então o registro não é removido fisicamente do banco.

### Parando os serviços

Quando terminar, os containers podem ser parados com:

docker compose down
