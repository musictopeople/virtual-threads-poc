# virtual-threads-poc

## Free The Jazz!!!

When building Java apps I have been working with Project Reactor for a few years now, but I have been wanting to switch over to virtual threads since Java 21. The subscription model can get confusing and mistakes are easily made in Project Reactor that have significant performance costs. Don't get me wrong, I love the functional style and granular controls, but being able to write straightforward Java code and pass off the heavy lifting to the JVM is awful nice.

This project uses Java 25 virtual threads via `spring.threads.virtual.enabled=true`. All request handling uses virtual threads, making blocking JDBC calls scale efficiently without manual thread pool configuration. 

The update and delete endpoints still need to be implemented, but there is a React frontend for you to go ahead and wire them up. Have a blast!

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
