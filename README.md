# WeFood - Sistema de Gerenciamento de Usuários

## 📌 Sobre o Projeto
Este repositório contém o projeto desenvolvido como parte do **Tech Challenge** da FIAP, na fase de **Arquitetura e Desenvolvimento Java**.

O objetivo é aplicar os conceitos estudados, construindo um **backend completo** utilizando **Spring Boot** e **Docker Compose**. O sistema é responsável por gerenciar usuários de uma aplicação web, implementando operações fundamentais como:

- Cadastro de usuários
- Atualização de dados
- Exclusão de registros
- Validação de login

Além disso, todo o ambiente é configurado para execução via **Docker**, garantindo consistência e escalabilidade.

---

## 🛠 Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Validation**
- **MySQL**
- **Docker & Docker Compose**
- **Maven**

---

## 🚀 Funcionalidades
- **Cadastro de usuários**: Criação de um novo usuário com nome, e-mail, login e senha.
- **Atualização de usuários**: Alteração dos dados de um usuário existente, com atualização automática da data da última modificação.
- **Exclusão de usuários**: Remoção de registros a partir do ID.
- **Validação de login**: Verificação de credenciais, retornando mensagens claras de sucesso ou erro.

---

## 📂 Estrutura do Projeto

    WeFood/
    ├── src/main/java/com/restaurant/wefood
    │ ├── controller
    │ ├── dto
    │ ├── entity
    │ ├── repository
    │ ├── service
    │ └── WeFoodApplication.java
    ├── src/main/resources/
    │ ├── application.properties
    ├── Dockerfile
    ├── docker-compose.yml
    ├── deploy_app.sh
    ├── deploy_full.sh
    ├── pom.xml
    └── README.md

___

## 📦 Como Executar

### Pré-requisitos
- **Docker Desktop**
- **Docker Compose**
- **WSL** e **Ubuntu 24**
- **Java 17** e **Maven**
- **Postman**

### Rodando com Docker Compose
Na raiz do projeto, execute:

    ./deploy_full.sh

ou

    ./deploy_app.sh

para subir a aplicação inteira (banco de dados e backend), ou somente a aplicação (backend).

___

