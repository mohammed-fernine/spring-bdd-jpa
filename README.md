# HotelBay - Hotel Management RESTful API

A Spring Boot RESTful API for hotel management, built with Spring Data JPA, PostgreSQL, and tested using BDD with Cucumber.

## Prerequisites

- **Docker** (v20.10+)
- **Docker Compose** (v2.0+)

## Running the Service with Docker Compose

Start all services with a single command:

```bash
docker-compose up
```

To run in detached (background) mode:

```bash
docker-compose up -d
```

To stop the services:

```bash
docker-compose down
```

To stop the services and remove the database volume:

```bash
docker-compose down -v
```

The API will be available at: **http://localhost:8080**

## Docker Compose Structure

The `docker-compose.yml` file defines the following services:

### Services

| Service | Image | Description |
|---------|-------|-------------|
| `db` | `postgres:16-alpine` | PostgreSQL 16 database server |
| `app` | Built from `Dockerfile` | Spring Boot application |

### Service: `db` (PostgreSQL Database)

- **Image:** `postgres:16-alpine`
- **Container name:** `hotelbay-db`
- **Port mapping:** `5432:5432`
- **Environment variables:**
  - `POSTGRES_DB=hotelbay` — database name
  - `POSTGRES_USER=postgres` — database user
  - `POSTGRES_PASSWORD=password` — database password
- **Volume:** `pgdata` — persists database data across container restarts
- **Health check:** Uses `pg_isready` to verify the database is accepting connections

### Service: `app` (Spring Boot Application)

- **Build:** Uses the multi-stage `Dockerfile` in the project root
- **Container name:** `hotelbay-app`
- **Port mapping:** `8080:8080`
- **Environment variables:**
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/hotelbay` — JDBC connection to the `db` service
  - `SPRING_DATASOURCE_USERNAME=postgres` — database user
  - `SPRING_DATASOURCE_PASSWORD=password` — database password
- **Depends on:** `db` (waits until the database health check passes)

### Volumes

| Volume | Purpose |
|--------|---------|
| `pgdata` | Persistent storage for PostgreSQL data |

## Dockerfile Structure

The `Dockerfile` uses a multi-stage build:

1. **Build stage** (`maven:3.9-eclipse-temurin-17`): Compiles the application and packages it into a JAR file.
2. **Runtime stage** (`eclipse-temurin:17-jre`): Runs the application with a minimal JRE image.

## CI/CD Pipeline

The project includes a fully automated CI/CD pipeline implemented with **GitHub Actions**. The pipeline is triggered on every push to the `master` branch.

### Pipeline Stages

The pipeline consists of three sequential jobs:

```
Push to master → Build & Test → Build & Publish Docker Image → Deploy to Server
```

#### 1. Build & Test

- Checks out the source code
- Sets up JDK 17 (Temurin) with Maven caching
- Starts a PostgreSQL 16 service container for integration tests
- **Builds** the project (`mvn clean compile`)
- **Runs all tests** (`mvn test`) — including unit tests and Cucumber BDD tests
- **Packages** the application into a JAR (`mvn package`)
- Uploads the JAR artifact for reference

#### 2. Build & Publish Docker Image

- Builds the Docker image using the multi-stage `Dockerfile`
- Pushes the image to **Docker Hub** with two tags:
  - `latest` — always points to the most recent build
  - `<commit-sha>` — for traceability and rollback

#### 3. Deploy to Server

- Connects via **SSH** to `deves.xdi.uevora.pt`
- Pulls the latest Docker image from Docker Hub
- Stops and removes any existing containers
- Creates a Docker network (`hotelbay-net`)
- Starts PostgreSQL and the application containers
- The application is accessible on the configured port

### Pipeline Configuration

The pipeline is parameterized using **GitHub Secrets** and **Repository Variables**.

#### Required Secrets (Settings → Secrets and variables → Actions → Secrets)

| Secret | Description |
|--------|-------------|
| `DOCKERHUB_USERNAME` | Docker Hub username |
| `DOCKERHUB_TOKEN` | Docker Hub access token (not password) |
| `DEPLOY_USER` | SSH username for the deployment server |
| `DEPLOY_SSH_KEY` | Private SSH key for authentication to the server |
| `DB_PASSWORD` | Database password used in production |

#### Optional Variables (Settings → Secrets and variables → Actions → Variables)

| Variable | Default | Description |
|----------|---------|-------------|
| `DEPLOY_PORT` | `22` | SSH port of the deployment server |
| `APP_PORT` | `8080` | Port to expose the application on the server |
| `DB_NAME` | `hotelbay` | PostgreSQL database name |
| `DB_USER` | `postgres` | PostgreSQL username |

### How to Configure the Pipeline

1. **Create a Docker Hub account** at [hub.docker.com](https://hub.docker.com) (if you don't have one)
2. **Create a public repository** on Docker Hub named `hotelbay`
3. **Generate a Docker Hub access token**: Account Settings → Security → New Access Token
4. **Generate an SSH key pair** for deployment:
   ```bash
   ssh-keygen -t ed25519 -C "github-actions-deploy"
   ```
5. **Add the public key** to the deployment server's `~/.ssh/authorized_keys`
6. **Add all secrets** to the GitHub repository under Settings → Secrets and variables → Actions
7. **Push to master** — the pipeline will run automatically

### Design Decisions

- **Multi-stage Docker build**: Keeps the final image small (~300MB vs ~800MB with full JDK)
- **GitHub Actions cache**: Maven dependencies are cached between runs for faster builds
- **Service containers**: PostgreSQL runs as a GitHub Actions service for realistic integration testing
- **Image tagging with SHA**: Allows rollback to any previous version
- **SSH deployment with `appleboy/ssh-action`**: Industry-standard approach for server deployment
- **Parameterized configuration**: All sensitive data and environment-specific settings are externalized as secrets/variables
- **Health checks**: PostgreSQL must pass health check before tests and deployment proceed
- **`unless-stopped` restart policy**: Containers restart automatically after server reboot

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA**
- **PostgreSQL 16**
- **Maven**
- **Cucumber** (BDD testing)
- **Docker & Docker Compose**
- **GitHub Actions** (CI/CD)
