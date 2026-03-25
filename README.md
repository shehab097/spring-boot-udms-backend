# UDMS - University Department Management System

A comprehensive Spring Boot-based backend application for managing university department operations including students, teachers, courses, attendance, and administrative functions.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Documentation](#documentation)
- [Configuration](#configuration)

## 🎯 Overview

UDMS is a REST API-based system designed to handle various operations in a university department. It provides secure authentication using JWT tokens, role-based access control, and comprehensive management features for academic entities.

### Key Capabilities

- **User Management**: Registration, authentication, and user role management
- **Course Management**: Create and manage courses offered by the department
- **Student Management**: Maintain student records and enrollment information
- **Teacher Management**: Manage faculty/teacher information
- **Attendance Tracking**: Record and track student attendance
- **Semester Management**: Organize courses and activities by semester
- **Notice Board**: Post and manage notices for students
- **Admin Functions**: Administrative operations and system management

## ✨ Features

- ✅ JWT-based authentication and stateless API sessions
- ✅ Role-based access control (RBAC) with different user roles
- ✅ RESTful API design with standardized responses
- ✅ MySQL database with JPA/Hibernate ORM
- ✅ CORS support for cross-origin requests
- ✅ Comprehensive error handling
- ✅ Data Transfer Objects (DTOs) for API payloads
- ✅ Spring Security integration
- ✅ Database auto-migration with Hibernate

## 🛠 Tech Stack

| Component           | Technology    | Version |
| ------------------- | ------------- | ------- |
| **Framework**       | Spring Boot   | 4.0.3   |
| **Language**        | Java          | 21      |
| **Database**        | MySQL         | Latest  |
| **Authentication**  | JWT           | 0.12.5  |
| **ORM**             | Hibernate/JPA | Latest  |
| **Build Tool**      | Maven         | Latest  |
| **Code Generation** | Lombok        | Latest  |

## 🚀 Quick Start

### Prerequisites

- Java 21 or higher
- MySQL Server running
- Maven installed

### Step 1: Database Setup

```sql
CREATE DATABASE udms;
```

### Step 2: Configure Application

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/udms
spring.datasource.username=root
spring.datasource.password=your_password
```

### Step 3: Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Step 4: Test Authentication

**Register a new user:**

```bash
POST http://localhost:8080/register
Content-Type: application/json

{
  "username": "user123",
  "password": "password123",
  "email": "user@example.com",
  "role": "STUDENT"
}
```

**Login:**

```bash
POST http://localhost:8080/login
Content-Type: application/json

{
  "username": "user123",
  "password": "password123"
}
```

Response includes JWT token for authenticated requests.

## 📁 Project Structure

```
udms/
├── src/main/java/com/shehab/udms/
│   ├── config/              # Spring configuration classes
│   │   └── SecurityConfig.java
│   ├── controller/          # REST API endpoints
│   ├── service/             # Business logic layer
│   ├── repo/                # Database repositories
│   ├── model/               # Entity models/JPA entities
│   ├── DTO/                 # Data Transfer Objects
│   ├── filter/              # Custom filters (JWT)
│   ├── utility/             # Utility classes
│   ├── types/               # Custom types/enums
│   └── UdmsApplication.java # Main entry point
├── src/main/resources/
│   └── application.properties
├── pom.xml                  # Maven dependencies
└── HELP.md

```

### Layer Architecture

```
Request → Controller → Service → Repository → Database
           ↓ Response ← DTO ← Entity ← Data

Authentication Filter (JWT) → All Requests
```

## 📚 Documentation

Comprehensive documentation for different aspects:

- **[API Documentation](API_DOCUMENTATION.md)** - Complete API endpoint reference
- **[Architecture Guide](ARCHITECTURE.md)** - System design and architecture
- **[Setup Guide](SETUP_GUIDE.md)** - Detailed installation and configuration
- **[Database Schema](DATABASE_SCHEMA.md)** - Entity relationships and schema

## ⚙️ Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Application
spring.application.name=udms

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/udms
spring.datasource.username=root
spring.datasource.password=1234

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update           # auto-create/update tables
spring.jpa.show-sql=true                       # log SQL queries
spring.jpa.properties.hibernate.dialect=...    # MySQL dialect

# JWT Security
jwt.secret=RDZTtgZcH1YGlnwqEDtS1MdhsSdoSRNzpXJUCSiSEVq
```

### Security Configuration

- **CORS Enabled**: Cross-Origin Resource Sharing is configured
- **CSRF Disabled**: Disabled for stateless API
- **Session Management**: STATELESS (JWT-based)
- **Public Endpoints**: `/register`, `/login`
- **Protected Endpoints**: All other endpoints require authentication

## 🔐 Authentication Flow

1. User registers with username, password, email
2. Credentials stored securely with password encoding
3. User logs in with credentials
4. Server returns JWT token
5. Client includes token in Authorization header for subsequent requests
6. JWT Filter validates token on each request

## 📝 API Endpoints Overview

### Public Endpoints

- `POST /register` - User registration
- `POST /login` - User authentication

### Protected Endpoints (Require Authentication)

**User Management**

- `GET /users` - List all users
- `GET /users/{id}` - Get user details
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

**Course Management**

- `GET /courses` - List courses
- `POST /courses` - Create course
- `PUT /courses/{id}` - Update course
- `DELETE /courses/{id}` - Delete course

**Student Management**

- `GET /students` - List students
- `POST /students` - Create student
- `PUT /students/{id}` - Update student
- `DELETE /students/{id}` - Delete student

**Teacher Management**

- `GET /teachers` - List teachers
- `POST /teachers` - Create teacher
- `PUT /teachers/{id}` - Update teacher
- `DELETE /teachers/{id}` - Delete teacher

**Attendance Tracking**

- `GET /attendance` - List attendance records
- `POST /attendance` - Create attendance record
- `PUT /attendance/{id}` - Update attendance

**Semester Management**

- `GET /semesters` - List semesters
- `POST /semesters` - Create semester
- `PUT /semesters/{id}` - Update semester

**Admin Operations**

- `GET /admin` - Admin dashboard
- `POST /admin/actions` - Perform admin tasks

**Notice Board**

- `GET /notices` - List notices
- `POST /notices` - Create notice
- `DELETE /notices/{id}` - Delete notice

See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for detailed endpoint specifications.

## 🔧 Building and Deployment

### Build

```bash
mvn clean install
```

### Run Tests

```bash
mvn test
```

### Create JAR

```bash
mvn package
```

### Run JAR

```bash
java -jar target/udms-0.0.1-SNAPSHOT.jar
```

## 📞 Support and Contributions

For issues, questions, or contributions, please refer to the documentation files or contact the development team.

## 📄 License

This project is part of the university department management suite.

---

**Version**: 0.0.1-SNAPSHOT  
**Last Updated**: March 2026
