# RELATÓRIO DE ANÁLISE - PROJETO WEFOOD

**Data:** $(date)  
**Versão:** 1.0  
**Analista:** Assistente IA

---

## ÍNDICE

1. [Descrição Detalhada da Arquitetura da Aplicação](#1-descrição-detalhada-da-arquitetura-da-aplicação)
2. [Modelagem das Entidades e Relacionamentos](#2-modelagem-das-entidades-e-relacionamentos)
3. [Descrição dos Endpoints Disponíveis](#3-descrição-dos-endpoints-disponíveis)
4. [Descrição da Documentação Swagger](#4-descrição-da-documentação-swagger)
5. [Considerações Adicionais](#5-considerações-adicionais)

---

## 1. DESCRIÇÃO DETALHADA DA ARQUITETURA DA APLICAÇÃO

### 1.1 Visão Geral

O WeFood é uma aplicação Spring Boot desenvolvida em Java 17 que implementa um sistema de cadastro e gerenciamento de usuários para um sistema de delivery de comida. A aplicação segue os padrões de arquitetura em camadas (Layered Architecture) e utiliza o padrão MVC (Model-View-Controller).

### 1.2 Stack Tecnológica

- **Framework:** Spring Boot 3.5.5
- **Linguagem:** Java 17
- **Banco de Dados:** MySQL 8.4 (produção) / H2 (testes)
- **ORM:** Spring Data JPA com Hibernate
- **Documentação API:** SpringDoc OpenAPI 3 (Swagger)
- **Build Tool:** Maven
- **Containerização:** Docker + Docker Compose
- **Validação:** Jakarta Validation (Bean Validation)
- **Utilitários:** Lombok

### 1.3 Arquitetura em Camadas

#### 1.3.1 Camada de Apresentação (Presentation Layer)
- **Controller:** `UsuarioController` - Gerencia as requisições HTTP
- **Exception Handler:** `GlobalExceptionHandler` - Tratamento global de exceções
- **DTOs:** Objetos de transferência de dados para comunicação com a API

#### 1.3.2 Camada de Serviço (Service Layer)
- **Service:** `ServiceFood` - Lógica de negócio principal
- **Validadores:** `ValidaLogin` e `ValidaLoginImpl` - Validações específicas

#### 1.3.3 Camada de Persistência (Persistence Layer)
- **Repository:** `UsuarioRepository` - Interface para acesso aos dados
- **Entity:** `Usuario` e `Endereco` - Modelos de domínio

#### 1.3.4 Camada de Configuração
- **OpenAPI Config:** Configuração do Swagger/OpenAPI
- **Application Properties:** Configurações da aplicação

### 1.4 Padrões Arquiteturais Implementados

#### 1.4.1 Dependency Injection
Utiliza injeção de dependência do Spring através de `@Autowired` e `@Component`.

#### 1.4.2 Repository Pattern
Implementa o padrão Repository através do `UsuarioRepository` que estende `JpaRepository`.

#### 1.4.3 DTO Pattern
Utiliza DTOs (Data Transfer Objects) para transferência de dados entre camadas:
- `UsuarioDTO` - Para criação de usuários
- `AtualizaUsuarioDTO` - Para atualização de usuários
- `DetalheUsuarioDTO` - Para retorno de dados do usuário
- `ValidaLoginDTO` - Para validação de login

#### 1.4.4 Exception Handling
Implementa tratamento global de exceções com `@RestControllerAdvice`.

---

## 2. MODELAGEM DAS ENTIDADES E RELACIONAMENTOS

### 2.1 Entidade Principal: Usuario

```java
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "E-mail")
    private String email;
    
    @Column(name = "Nome")
    private String name;
    
    private String password;
    
    @CreationTimestamp
    private LocalDateTime dataCadastro;
    
    @UpdateTimestamp
    private LocalDateTime ultimaAtualizacao;
    
    @Embedded
    private Endereco endereco;
}
```

### 2.2 Entidade Embeddable: Endereco

```java
@Embeddable
public class Endereco {
    private String rua;
    private String numero;
    private String cidade;
    private String cep;
}
```

### 2.3 Relacionamentos

#### 2.3.1 Usuario ↔ Endereco
- **Tipo:** Composição (Embedded)
- **Cardinalidade:** 1:1
- **Descrição:** Cada usuário possui um endereço embutido na mesma tabela
- **Implementação:** `@Embedded` na entidade Usuario

### 2.4 Estrutura do Banco de Dados

#### 2.4.1 Tabela: usuario

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária (auto increment) |
| E-mail | VARCHAR | Email do usuário (único) |
| Nome | VARCHAR | Nome completo do usuário |
| password | VARCHAR | Senha do usuário |
| dataCadastro | TIMESTAMP | Data de criação (automática) |
| ultimaAtualizacao | TIMESTAMP | Data da última atualização (automática) |
| rua | VARCHAR | Rua do endereço |
| numero | VARCHAR | Número do endereço |
| cidade | VARCHAR | Cidade do endereço |
| cep | VARCHAR | CEP do endereço |

---

## 3. DESCRIÇÃO DOS ENDPOINTS DISPONÍVEIS

### 3.1 Base URL
```
http://localhost:8080/usuario
```

### 3.2 Endpoints Implementados

#### 3.2.1 POST /usuario
**Descrição:** Cadastra um novo usuário no sistema

**Request Body:**
```json
{
    "name": "João Silva",
    "email": "joao@email.com",
    "password": "senha123",
    "endereco": {
        "rua": "Rua das Flores",
        "numero": "123",
        "cidade": "São Paulo",
        "cep": "01234567"
    }
}
```

**Response (201 Created):**
```json
{
    "name": "João Silva",
    "email": "joao@email.com"
}
```

**Validações:**
- Nome não pode ser nulo ou vazio
- Email deve ser válido e não pode ser nulo ou vazio
- Senha não pode ser nula ou vazia
- Endereço é obrigatório
- CEP deve ter exatamente 8 dígitos
- Email deve ser único no sistema

#### 3.2.2 GET /usuario/{name}
**Descrição:** Busca usuários por nome (busca parcial, case-insensitive)

**Path Parameter:**
- `name`: Nome ou parte do nome do usuário

**Response (200 OK):**
```json
[
    {
        "name": "João Silva",
        "email": "joao@email.com"
    }
]
```

**Exemplo de Uso:**
```
GET /usuario/João
GET /usuario/silva
```

#### 3.2.3 POST /usuario/login
**Descrição:** Valida credenciais de login do usuário

**Request Body:**
```json
{
    "email": "joao@email.com",
    "password": "senha123"
}
```

**Response (200 OK):**
```
"Login efetuado com sucesso!"
```

**Validações:**
- Email deve existir no sistema
- Senha deve corresponder ao email
- Ambos os campos são obrigatórios

#### 3.2.4 PUT /usuario/senha/{id}
**Descrição:** Atualiza a senha de um usuário específico

**Path Parameter:**
- `id`: ID do usuário

**Request Body:**
```json
{
    "email": "joao@email.com",
    "password": "novaSenha123"
}
```

**Response (200 OK):**
```json
{
    "name": "João Silva",
    "email": "joao@email.com"
}
```

#### 3.2.5 PATCH /usuario/{id}
**Descrição:** Atualiza dados de um usuário (parcial)

**Path Parameter:**
- `id`: ID do usuário

**Request Body:**
```json
{
    "name": "João Santos",
    "email": "joao.santos@email.com",
    "endereco": {
        "rua": "Rua Nova",
        "numero": "456",
        "cidade": "Rio de Janeiro",
        "cep": "12345678"
    }
}
```

**Response (200 OK):**
```json
{
    "name": "João Santos",
    "email": "joao.santos@email.com"
}
```

#### 3.2.6 DELETE /usuario/{id}
**Descrição:** Remove um usuário do sistema

**Path Parameter:**
- `id`: ID do usuário

**Response (204 No Content):**
```
(Sem conteúdo)
```

### 3.3 Códigos de Status HTTP Utilizados

- **200 OK:** Operação realizada com sucesso
- **201 Created:** Usuário criado com sucesso
- **204 No Content:** Usuário removido com sucesso
- **400 Bad Request:** Dados inválidos ou erro de validação
- **404 Not Found:** Usuário não encontrado
- **500 Internal Server Error:** Erro interno do servidor

### 3.4 Tratamento de Erros

#### 3.4.1 ResourceNotFoundException (404)
```json
{
    "message": "Usuario não encontrado com o id: 123",
    "status": 404
}
```

#### 3.4.2 ValidationErrorDTO (400)
```json
{
    "errors": [
        "name: O nome não pode ser nulo ou vazio",
        "email: O email não pode ser nulo ou vazio"
    ],
    "status": 400
}
```

---

## 4. DESCRIÇÃO DA DOCUMENTAÇÃO SWAGGER

### 4.1 Configuração do OpenAPI/Swagger

A documentação Swagger é configurada através da classe `OpenApiConfig`:

```java
@OpenAPIDefinition
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI weFood(){
        return new OpenAPI()
                .info(
                        new Info().title("We food Api")
                                .description("Esse sistema permitirá que os clientes escolham restaurantes com base na comida oferecida.")
                                .version("v0.0.1")
                                .license(new License().name("Apache 2.0").url("https://github.com/Alecio-99/WeFood"))
                );
    }
}
```

### 4.2 Informações da API

- **Título:** "We food Api"
- **Descrição:** "Esse sistema permitirá que os clientes escolhem restaurantes com base na comida oferecida."
- **Versão:** "v0.0.1"
- **Licença:** Apache 2.0
- **URL da Licença:** https://github.com/Alecio-99/WeFood

### 4.3 Acesso à Documentação

A documentação Swagger estará disponível em:
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

### 4.4 Tags e Organização

O controller utiliza a tag "WeFood" para organizar os endpoints:

```java
@Tag(name = "WeFood", description = "Controller para o crud de cadastro para o usuarios")
public class UsuarioController {
    // endpoints...
}
```

### 4.5 Dependências do Swagger

No `pom.xml`:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.13</version>
</dependency>
```

### 4.6 Funcionalidades da Documentação

A documentação Swagger incluirá:
- **Lista de todos os endpoints** disponíveis
- **Métodos HTTP** suportados (GET, POST, PUT, PATCH, DELETE)
- **Parâmetros de entrada** (path parameters, request body)
- **Exemplos de request/response** para cada endpoint
- **Códigos de status** possíveis para cada operação
- **Modelos de dados** (DTOs) utilizados
- **Validações** aplicadas aos campos
- **Tratamento de erros** e respostas de erro

### 4.7 Exemplo de Documentação Swagger

A documentação gerada automaticamente incluirá informações como:

**Endpoint:** POST /usuario
- **Summary:** Cadastra um novo usuário
- **Request Body:** UsuarioDTO
- **Response:** DetalheUsuarioDTO (201 Created)
- **Validation:** Campos obrigatórios e formatos

**Endpoint:** GET /usuario/{name}
- **Summary:** Busca usuários por nome
- **Parameters:** name (path parameter)
- **Response:** List<DetalheUsuarioDTO> (200 OK)

---

## 5. CONSIDERAÇÕES ADICIONAIS

### 5.1 Segurança
- **Senhas:** Armazenadas em texto plano (não recomendado para produção)
- **Validação:** Implementada através de Jakarta Validation
- **Autenticação:** Sistema básico de login sem tokens JWT

### 5.2 Testes
- **Testes Unitários:** Implementados para controller, service e repository
- **Banco de Teste:** H2 em memória para testes
- **Mockito:** Utilizado para mock de dependências

### 5.3 Containerização
- **Docker:** Aplicação containerizada
- **Docker Compose:** Orquestração com MySQL
- **Health Check:** Implementado para o banco de dados

### 5.4 Melhorias Sugeridas
1. **Segurança:** Implementar hash de senhas (BCrypt)
2. **Autenticação:** Adicionar JWT para autenticação stateless
3. **Validação:** Melhorar validações de CEP e email
4. **Logs:** Implementar logging estruturado
5. **Monitoramento:** Adicionar métricas e health checks
6. **Documentação:** Expandir documentação da API com mais exemplos

---

## CONCLUSÃO

Este relatório apresenta uma análise completa da aplicação WeFood, cobrindo todos os aspectos solicitados: arquitetura, modelagem de dados, endpoints e documentação Swagger. A aplicação demonstra uma estrutura bem organizada seguindo padrões de desenvolvimento Java/Spring Boot, com separação clara de responsabilidades e implementação adequada de validações e tratamento de erros.

A documentação Swagger está configurada e funcionando, proporcionando uma interface interativa para testar e entender os endpoints da API. O sistema está preparado para expansão futura com a adição de novas funcionalidades relacionadas ao domínio de delivery de comida.

---

**Documento gerado automaticamente em:** $(date)  
**Versão do relatório:** 1.0
