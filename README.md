# 🎓 EAD Platform — Arquitetura de Microsserviços

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen?style=flat-square&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.13-orange?style=flat-square&logo=rabbitmq)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)
![CI](https://img.shields.io/github/actions/workflow/status/wilsonbraga/ead-platform-microservices/ci.yml?label=CI&style=flat-square&logo=githubactions)

Plataforma de ensino a distância (EAD) desenvolvida com arquitetura de microsserviços utilizando Java Spring Boot. O projeto tem como objetivo demonstrar boas práticas de desenvolvimento, containerização com Docker, testes automatizados e pipeline de CI/CD com GitHub Actions.

---

## 📐 Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                     EAD Platform                        │
│                                                         │
│  ┌───────────┐   ┌───────────┐   ┌──────────────────┐  │
│  │ authuser  │   │  course   │   │  notification    │  │
│  │ :8080     │   │  :8081    │   │  :8082           │  │
│  └─────┬─────┘   └─────┬─────┘   └────────┬─────────┘  │
│        │               │                  │             │
│        └───────────────┴──────────────────┘             │
│                        │                                │
│              ┌─────────▼──────────┐                     │
│              │     RabbitMQ       │                     │
│              │  (Mensageria)      │                     │
│              └─────────┬──────────┘                     │
│                        │                                │
│              ┌─────────▼──────────┐                     │
│              │    PostgreSQL      │                     │
│              │  (Banco de Dados)  │                     │
│              └────────────────────┘                     │
└─────────────────────────────────────────────────────────┘
```

---

## 🧩 Microsserviços

### 👤 authuser (porta 8080)
Responsável pelo gerenciamento de usuários e autenticação da plataforma.
- Cadastro e gerenciamento de usuários
- Autenticação e autorização
- Banco: `authuser_db`

### 📚 course (porta 8081)
Responsável pelo gerenciamento de cursos e conteúdos da plataforma.
- CRUD de cursos
- Gerenciamento de módulos e aulas
- Banco: `course_db`

### 🔔 notification (porta 8082)
Responsável pelo envio de notificações entre os serviços via mensageria.
- Consumidor de eventos do RabbitMQ
- Envio de e-mails e notificações

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 2.7.18 | Framework principal |
| Spring Data JPA | - | Persistência de dados |
| Spring AMQP | - | Mensageria com RabbitMQ |
| PostgreSQL | 15 | Banco de dados relacional |
| RabbitMQ | 3.13 | Mensageria entre microsserviços |
| Docker | - | Containerização |
| Docker Compose | - | Orquestração local |
| GitHub Actions | - | Pipeline de CI/CD |
| JUnit 5 | - | Framework de testes |
| Mockito | - | Mock para testes unitários |
| H2 | - | Banco em memória para testes |
| Maven | - | Gerenciamento de dependências |

---

## 📁 Estrutura do Projeto

```
ead-platform-microservices/
├── .github/
│   └── workflows/
│       └── ci.yml                    # Pipeline GitHub Actions
├── authuser/
│   ├── src/
│   │   ├── main/java/                # Código fonte
│   │   └── test/
│   │       ├── java/                 # Testes automatizados
│   │       └── resources/
│   │           └── application.properties  # Config de testes (H2)
│   ├── Dockerfile
│   └── pom.xml
├── course/
│   ├── src/
│   │   ├── main/java/
│   │   └── test/
│   │       ├── java/
│   │       └── resources/
│   │           └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── notification/
│   ├── src/
│   │   ├── main/java/
│   │   └── test/
│   │       ├── java/
│   │       └── resources/
│   │           └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml                # Orquestração local completa
└── README.md
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado
- [Git](https://git-scm.com/) instalado

### 1. Clone o repositório

```bash
git clone https://github.com/wilsonbraga/ead-platform-microservices.git
cd ead-platform-microservices
```

### 2. Compile cada microsserviço

```bash
# authuser
cd authuser && ./mvnw clean package -DskipTests && cd ..

# course
cd course && ./mvnw clean package -DskipTests && cd ..

# notification
cd notification && ./mvnw clean package -DskipTests && cd ..
```

### 3. Suba todos os serviços com Docker Compose

```bash
docker compose up --build
```

### 4. Verifique se está tudo rodando

```bash
docker compose ps
```

---

## 🧪 Testes

O projeto utiliza testes automatizados com **JUnit 5** e **Mockito**. Os testes rodam sem precisar do Docker ou banco de dados — utilizam H2 em memória.

### Estratégia de testes

| Camada | Ferramenta | O que testa |
|---|---|---|
| Service | JUnit 5 + Mockito | Lógica de negócio isolada |
| Controller | MockMvc | Endpoints HTTP e validações |
| Validation | JUnit 5 | Regras de validação customizadas |

### Cobertura atual — authuser

| Classe | Testes |
|---|---|
| `UserServiceImpl` | 13 testes unitários |
| `AuthenticationController` | 7 testes de integração |
| `UsernameConstraintImpl` | 10 testes unitários |

### Como rodar os testes

```bash
# Rodar testes de um serviço específico
cd authuser
./mvnw test

# Rodar build completo com testes
./mvnw clean verify -B
```

### Rodar pelo STS

Clique com botão direito na classe de teste → `Run As` → `JUnit Test`

---

## 🌐 Endpoints de Health Check

Após subir os serviços, acesse:

| Serviço | URL |
|---|---|
| authuser | http://localhost:8080/actuator/health |
| course | http://localhost:8081/actuator/health |
| notification | http://localhost:8082/actuator/health |
| RabbitMQ UI | http://localhost:15672 (guest/guest) |

---

## ⚙️ CI/CD — GitHub Actions

O pipeline é executado automaticamente em todo `push` ou `pull request` nas branches `main` e `develop`.

### Etapas do Pipeline

```
Push/PR
   │
   ▼
┌─────────────────────────────┐
│  Matrix Build + Test        │
│  (paralelo)                 │
│  ├── Build & Test authuser  │
│  ├── Build & Test course    │
│  └── Build & Test notif.    │
└──────────────┬──────────────┘
               │
               ▼
┌──────────────────────────────┐
│  Validar Docker Compose      │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│  Resumo do Pipeline          │
└──────────────────────────────┘
```

---

## 🔧 Comandos Úteis

```bash
# Subir em background
docker compose up --build -d

# Ver logs de um serviço específico
docker compose logs -f authuser
docker compose logs -f course
docker compose logs -f notification

# Parar os containers
docker compose stop

# Remover os containers (mantém volumes)
docker compose down

# Remover containers e volumes (banco zerado)
docker compose down -v
```

---

## 📋 Padrão de Commits

Este projeto segue o padrão [Conventional Commits](https://www.conventionalcommits.org/).

| Tipo | Descrição |
|---|---|
| `feat` | Nova funcionalidade |
| `fix` | Correção de bug |
| `chore` | Configuração, build, CI/CD |
| `docs` | Documentação |
| `refactor` | Refatoração sem mudança de comportamento |
| `test` | Adição ou correção de testes |
| `perf` | Melhoria de performance |
| `style` | Formatação, sem mudança de lógica |

**Exemplos:**
```bash
feat(authuser): adiciona endpoint de registro de usuário
fix(notification): corrige envio de e-mail assíncrono
chore(docker): atualiza configuração do docker-compose
docs: atualiza README com instrucoes de execucao
test(authuser): adiciona testes unitarios para UserServiceImpl
```

---

## 👨‍💻 Autor

**Wilson Rodrigues**

[![GitHub](https://img.shields.io/badge/GitHub-wilsonbraga-181717?style=flat-square&logo=github)](https://github.com/wilsonbraga)

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.