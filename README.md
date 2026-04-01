# 🎓 EAD Platform — Arquitetura de Microsserviços

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen?style=flat-square&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.13-orange?style=flat-square&logo=rabbitmq)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)
![CI](https://img.shields.io/github/actions/workflow/status/wilsonbraga/ead-platform-microservices/ci.yml?label=CI&style=flat-square&logo=githubactions)

Plataforma de ensino a distância (EAD) desenvolvida com arquitetura de microsserviços utilizando Java Spring Boot. O projeto tem como objetivo demonstrar boas práticas de desenvolvimento, containerização com Docker e pipeline de CI/CD com GitHub Actions.

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
- Banco: `notification_db`

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
| Maven | - | Gerenciamento de dependências |

---

## 📁 Estrutura do Projeto

```
ead-platform-microservices/
├── .github/
│   └── workflows/
│       └── ci.yml              # Pipeline GitHub Actions
├── authuser/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── course/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── notification/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml          # Orquestração local completa
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
│  Matrix Build (paralelo)    │
│  ├── Build authuser         │
│  ├── Build course           │
│  └── Build notification     │
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
docs: atualiza README com instruções de execução
```

---

## 👨‍💻 Autor

**Wilson Rodrigues**

[![GitHub](https://img.shields.io/badge/GitHub-wilsonbraga-181717?style=flat-square&logo=github)](https://github.com/wilsonbraga)

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
