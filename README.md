# virtual-threads-poc

I have been working with Project Reactor for a few years. I love the functional style and granular controls, however, the subscription model can get confusing and mistakes are easily made resulting in significant performance costs.

This project uses Virtual Threads (Java 21+) via `spring.threads.virtual.enabled=true`. All request handling uses virtual threads, making blocking JDBC calls scale efficiently without manual thread pool configuration.

## What You Get

- Bare bones React 19 + React Router UI
- build.gradle setup for Java 25 and Spring Boot 4+
- docker-compose file for Postgres with seed data (some classic free jazz albums)

## What You Need

- Java 25
- Node.js 22+
- Docker

## Getting Started

I run Linux, so you might need to tweak these commands slightly for Mac/Windows.

**1. Start Docker (if not already running)**

```bash
sudo systemctl start docker
```

**2. Spin up the Postgres container**

```bash
sudo docker-compose up -d
```

**3. Run the backend** (from project root)

```bash
./gradlew bootRun
```

**4. Run the frontend** (in a new terminal)

```bash
cd frontend
npm install
npm run dev
```

**5. Open the app**

Head to [http://localhost:5173](http://localhost:5173) and expand your mind!

## Cleanup

When you're done, tear it all down:

```bash
sudo docker-compose down -v
```
