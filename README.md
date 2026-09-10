# Kaamgar Connect — Java Spring Boot Version

A **Worker-Employer Job Marketplace** built with **Java Spring Boot + React.js + MongoDB Atlas**.

> 📌 This is the **Java/Spring Boot portfolio version** of the project.  
> MERN version: [github.com/Prateek8178/Kaamgar-Connect](https://github.com/Prateek8178/Kaamgar-Connect)

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2 |
| Auth | Spring Security + JWT |
| Database | MongoDB Atlas |
| Email | JavaMailSender (Gmail SMTP) |
| Frontend | React.js, Bootstrap 5 |

## Features
- ✅ Register/Login with OTP email verification
- ✅ Forgot Password + Reset via OTP
- ✅ Worker can browse & apply to jobs (with proposed rate)
- ✅ Employer can post jobs & review applicants
- ✅ Worker ↔ Employer direct messaging (REST polling)
- ✅ Application status updates & notifications
- ✅ Worker profile with skills, rate, availability

## Project Structure
```
Kaamgar-Connect-Java/
├── springboot-backend/    ← Java Spring Boot API (port 8080)
│   ├── pom.xml
│   └── src/main/java/com/kaamgar/
│       ├── controller/   (Auth, Job, Application, Chat, Worker, Notification)
│       ├── service/      (Business Logic)
│       ├── model/        (MongoDB Documents)
│       ├── repository/   (MongoRepository interfaces)
│       ├── security/     (JwtFilter, JwtUtil)
│       └── config/       (SecurityConfig - CORS + JWT)
└── frontend/             ← React.js (port 3000)
    └── src/
        ├── pages/        (Dashboard, Jobs, Workers, Chat, etc.)
        └── services/api.js (axios → Spring Boot backend)
```

## Run Locally

### 1. Spring Boot Backend
```bash
cd springboot-backend
# Set env vars or edit application.properties
mvn spring-boot:run
# API runs at http://localhost:8080
```

### 2. React Frontend
```bash
cd frontend
# Create .env from .env.example
cp .env.example .env
npm install
npm start
# App runs at http://localhost:3000
```

## API Endpoints
See [springboot-backend/README.md](./springboot-backend/README.md) for full API docs.

## Environment Variables

### Spring Boot (`application.properties`)
```properties
MONGO_URI=mongodb+srv://...
JWT_SECRET=your-secret
EMAIL_USER=your@gmail.com
EMAIL_PASS=your-app-password
CLIENT_URL=http://localhost:3000
```

### React (`frontend/.env`)
```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_SOCKET_URL=http://localhost:8080
```
