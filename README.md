# HotelBay - Hotel Management RESTful API

A Spring Boot RESTful API for hotel management, built with Spring Data JPA, PostgreSQL, and tested using BDD with Cucumber.

## Final Version Summary

- **CI/CD**: GitHub Actions builds, tests, builds Docker image, pushes to Docker Hub, and deploys via SSH to the university server.
- **Trigger**: On push to `main` or `master` branches.
- **Deploy target**: `deves.xdi.uevora.pt` (configurable), app exposed on port `8080` by default.
- **Secrets/Variables**: All credentials and ports are parameterized via GitHub Secrets and Variables (see tables below).
- **Tests**: Cucumber BDD on H2 in-memory DB with the `test` profile.
- **Docker**: Multi-stage Dockerfile; local development via Docker Compose.

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA** with PostgreSQL 16
- **Maven** for build and dependency management
- **Cucumber 7.14** for BDD testing (H2 in-memory database)
- **Docker & Docker Compose** for containerization
- **GitHub Actions** for CI/CD

## Project Structure

```
spring-bdd-jpa/
├── .github/workflows/ci-cd.yml   # CI/CD pipeline definition
├── src/
│   ├── main/
│   │   ├── java/com/hotelbay/
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── repository/       # Spring Data repositories
│   │   │   ├── service/          # Business logic
│   │   │   └── HotelBayApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/hotelbay/    # Cucumber step definitions
│       └── resources/
│           ├── application-test.properties
│           └── features/         # Cucumber .feature files
├── Dockerfile                    # Multi-stage Docker build
├── docker-compose.yml            # Local development setup
├── pom.xml                       # Maven configuration
└── README.md
```

## API Endpoints

| Endpoint | Description |
|----------|-------------|
| `/api/hotels` | Hotel management |
| `/api/rooms` | Room management |
| `/api/room-categories` | Room category management |
| `/api/rooms/search` | Room search |
| `/api/reservations` | Reservation management |
| `/api/payments` | Payment management |
| `/api/reviews` | Review management |
| `/api/users` | User management |

## Prerequisites

- **Docker** (v20.10+)
- **Docker Compose** (v2.0+)

## Running the Service with Docker Compose

Start all services:

```bash
docker-compose up
```

Run in detached (background) mode:

```bash
docker-compose up -d
```

Stop the services:

```bash
docker-compose down
```

Stop and remove the database volume:

```bash
docker-compose down -v
```

The API will be available at **http://localhost:8080**.

## Docker Compose Structure

The `docker-compose.yml` defines two services:

### Service: `db` (PostgreSQL Database)

- **Image:** `postgres:16-alpine`
- **Container name:** `hotelbay-db`
- **Port:** `5432:5432`
- **Environment:**
  - `POSTGRES_DB=hotelbay`
  - `POSTGRES_USER=postgres`
  - `POSTGRES_PASSWORD=password`
- **Volume:** `pgdata` — persistent database storage
- **Health check:** `pg_isready` verifies the database is accepting connections

### Service: `app` (Spring Boot Application)

- **Build:** Multi-stage `Dockerfile`
- **Container name:** `hotelbay-app`
- **Port:** `8080:8080`
- **Environment:**
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/hotelbay`
  - `SPRING_DATASOURCE_USERNAME=postgres`
  - `SPRING_DATASOURCE_PASSWORD=password`
- **Depends on:** `db` — waits until the database health check passes

### Volumes

| Volume | Purpose |
|--------|---------|
| `pgdata` | Persistent storage for PostgreSQL data |

## Dockerfile

The `Dockerfile` uses a **multi-stage build**:

1. **Build stage** (`maven:3.9-eclipse-temurin-17`) — compiles the source code and packages it into a JAR
2. **Runtime stage** (`eclipse-temurin:17-jre`) — runs the application with a minimal JRE image (~300MB vs ~800MB with full JDK)

## CI/CD Pipeline

The project includes a fully automated CI/CD pipeline implemented with **GitHub Actions**, defined in `.github/workflows/ci-cd.yml`. The pipeline is triggered on every push to the `main` or `master` branches.

### Pipeline Stages

```
Push to main → Build & Test → Build & Publish Docker Image → Deploy to Server
```

#### 1. Build & Test

- Sets up JDK 17 (Temurin) with Maven dependency caching
- **Builds** the project (`mvn clean compile`)
- **Runs all tests** (`mvn test`) — Cucumber BDD tests use H2 in-memory database
- **Packages** the application into a JAR (`mvn package`)
- Uploads the JAR as a build artifact

#### 2. Build & Publish Docker Image

- Builds the Docker image using the multi-stage `Dockerfile`
- Pushes the image to **Docker Hub** with two tags:
  - `latest` — always points to the most recent build
  - `<commit-sha>` — for version traceability and rollback

#### 3. Deploy to Server

- Connects via **SSH** (password authentication) to the deployment server
- Pulls the latest Docker image from Docker Hub
- Stops and removes any existing containers
- Creates a Docker network (`hotelbay-net`)
- Starts PostgreSQL and the application containers with `--restart unless-stopped`

### Pipeline Configuration

The pipeline is fully parameterized using **GitHub Repository Secrets** and **Variables**.

#### Required Secrets (Settings → Secrets and variables → Actions)

| Secret | Description |
|--------|-------------|
| `DOCKERHUB_USERNAME` | Docker Hub username |
| `DOCKERHUB_TOKEN` | Docker Hub access token |
| `DEPLOY_USER` | SSH username for the deployment server |
| `DEPLOY_PASSWORD` | SSH password for the deployment server |
| `DB_PASSWORD` | Database password used in production |

#### Optional Variables (Settings → Secrets and variables → Actions → Variables)

| Variable | Default | Description |
|----------|---------|-------------|
| `DEPLOY_HOST` | `deves.xdi.uevora.pt` | Deployment server hostname |
| `DEPLOY_PORT` | `22` | SSH port of the deployment server |
| `APP_PORT` | `8080` | Port to expose the application on the server |
| `DB_NAME` | `hotelbay` | PostgreSQL database name |
| `DB_USER` | `postgres` | PostgreSQL username |

### How to Configure the Pipeline

1. **Create a Docker Hub account** at [hub.docker.com](https://hub.docker.com)
2. **Create a public repository** on Docker Hub named `hotelbay`
3. **Generate a Docker Hub access token:** Account Settings → Security → New Access Token
4. **Add all secrets** to the GitHub repository under Settings → Secrets and variables → Actions
5. **Push to main or master** — the pipeline runs automatically

Note: If your Docker Hub repository is private, ensure `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` are set; the pipeline performs a Docker login on the remote host before pulling the image.

### Design Decisions

- **Multi-stage Docker build** — keeps the final image lightweight
- **Maven dependency caching** — speeds up CI builds
- **H2 for testing** — tests run with an in-memory database, no external dependencies needed in CI
- **Image tagging with commit SHA** — enables rollback to any previous version
- **Password-based SSH deployment** — compatible with university server credentials
- **Parameterized configuration** — all sensitive data and environment-specific settings are externalized as GitHub secrets/variables
- **`unless-stopped` restart policy** — containers restart automatically after server reboot
