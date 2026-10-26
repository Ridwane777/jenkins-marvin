# Jenkins Marvin

Infrastructure-as-code project that deploys a fully configured **Jenkins** instance with zero manual setup, using **Docker**, **JCasC (Jenkins Configuration as Code)** and **Job DSL**.

## What it does

1. Builds a ready-to-use Jenkins server image with the required plugins.
2. Manages users and permissions through a role-based strategy (Admin, Gorilla, Ape, Assist).
3. Automatically creates a folder structure and maintenance jobs.
4. Provides a **SEED** job that dynamically generates CI jobs (build and test) for C projects using Make.

## Project structure

| File | Purpose |
|---|---|
| `Dockerfile` | Builds the Jenkins image with the required plugins |
| `my_marvin.yml` | JCasC configuration: users, roles, security |
| `job_dsl.groovy` | Job DSL script creating the `Tools` folder and the `SEED` and `clone-repository` jobs |
| `.env` | Secrets and environment variables (not versioned, see `.env.example`) |

## Getting started

### 1. Prerequisites
Docker installed on a Linux host.

### 2. Secrets
Copy the example file and set the user passwords:
```bash
cp .env.example .env
```

### 3. Build the image
```bash
docker build --network=host -t my-marvin .
```

### 4. Run the instance
```bash
docker run -d \
  --name jenkins \
  --network host \
  --env-file .env \
  -v jenkins_home:/var/jenkins_home \
  -v $(pwd)/my_marvin.yml:/var/jenkins_home/my_marvin.yml \
  -e CASC_JENKINS_CONFIG=/var/jenkins_home/my_marvin.yml \
  my-marvin
```
Jenkins is then available on `http://localhost:8080`, already configured: no setup wizard.

## Author

[Ridwane777](https://github.com/Ridwane777)
