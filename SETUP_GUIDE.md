# UDMS - Setup and Installation Guide

Complete step-by-step guide to set up and run the University Department Management System locally.

## 📋 Prerequisites

Before starting, ensure you have the following installed:

### System Requirements

| Requirement | Version | Minimum    |
| ----------- | ------- | ---------- |
| Java        | 21      | 17+        |
| MySQL       | 8.0+    | 5.7+       |
| Maven       | 3.8+    | 3.6+       |
| Git         | Latest  | Any recent |
| RAM         | 4GB     | 2GB        |
| Disk Space  | 5GB     | 2GB        |

### Installation Verification

Check if tools are installed:

```bash
# Check Java
java -version
# Expected: Java 21 or higher

# Check Maven
mvn -version
# Expected: Maven 3.8+

# Check MySQL
mysql --version
# Expected: MySQL 8.0+
```

## 🖥 Windows Installation

### Step 1: Install Java 21

1. Download from [Oracle Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
2. Run the installer
3. Follow setup wizard (accept default locations)
4. Verify installation:
    ```bash
    java -version
    ```

### Step 2: Install Maven

1. Download from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\apache-maven-3.8.x`
3. Add to PATH:
    - Open System Properties → Environment Variables
    - Add `C:\Program Files\apache-maven-3.8.x\bin` to PATH
4. Verify:
    ```bash
    mvn -version
    ```

### Step 3: Install MySQL

1. Download from [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
2. Choose Windows installer (MSI installer)
3. Run installer and follow setup wizard
4. Configure as:
    - **Config Type**: Development Machine
    - **Server Port**: 3306 (default)
    - **Service Name**: MySQL80 (or later version)

5. Create database user:

    ```bash
    mysql -u root -p
    CREATE USER 'root'@'localhost' IDENTIFIED BY '1234';
    GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';
    FLUSH PRIVILEGES;
    ```

6. Verify MySQL is running:
    - Open Services (services.msc)
    - Look for "MySQL80" service

## 🔧 Database Setup

### Create UDMS Database

```bash
# Connect to MySQL
mysql -u root -p
# Enter password: 1234

# Create database
CREATE DATABASE IF NOT EXISTS udms;

# Select database
USE udms;

# Verify creation
SHOW DATABASES;
```

### Initial Database Tables

Tables will be auto-created by Hibernate on application startup. The `spring.jpa.hibernate.ddl-auto=update` property in `application.properties` handles this.

## 📁 Project Setup

### Step 1: Clone Repository

```bash
# Navigate to desired location
cd C:\Users\YourUsername\Projects

# Clone the repository (adjust URL as needed)
git clone https://github.com/yourusername/udms.git
cd udms
```

Or extract if you have a ZIP file:

```bash
# Extract ZIP to desired location
cd C:\Users\YourUsername\Projects\udms
```

### Step 2: Verify Project Structure

```
udms/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── pom.xml
├── mvnw
└── mvnw.cmd
```

### Step 3: Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=udms

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/udms
spring.datasource.username=root
spring.datasource.password=1234

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Logging
logging.level.com.shehab.udms=INFO
```

**Configuration Details:**

| Property              | Value                            | Purpose                                        |
| --------------------- | -------------------------------- | ---------------------------------------------- |
| `datasource.url`      | jdbc:mysql://localhost:3306/udms | MySQL connection string                        |
| `datasource.username` | root                             | Database user                                  |
| `datasource.password` | 1234                             | Database password                              |
| `ddl-auto`            | update                           | Auto-create/update tables                      |
| `show-sql`            | false                            | Don't log SQL queries (set true for debugging) |

## 🏗 Build and Run

### Step 1: Build Project

Open Command Prompt and navigate to project directory:

```bash
cd C:\Users\YourUsername\Projects\udms

# Clean and build
mvn clean install

# Output should show BUILD SUCCESS
```

### Step 2: Run Application

```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Using JAR (after build)
java -jar target/udms-0.0.1-SNAPSHOT.jar

# Option 3: Using IDE (VS Code, IntelliJ, Eclipse)
# Just run the main class UdmsApplication
```

### Step 3: Verify Application Started

Check for output like:

```
Tomcat started on port(s): 8080 (http)
Started UdmsApplication in X.XX seconds
```

Application is ready at: `http://localhost:8080`

## 🧪 Testing Setup

### Step 1: Verify API Health

```bash
# Test if server is running
curl http://localhost:8080/hello
# Should return some response

# Or open in browser
http://localhost:8080/hello
```

### Step 2: Test Registration and Login

Using a tool like **Postman** or **curl**:

```bash
# Register a new user
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test@123",
    "email": "test@example.com",
    "role": "STUDENT"
  }'

# Response should contain user ID and confirmation
```

```bash
# Login to get JWT token
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test@123"
  }'

# Response includes JWT token
# Sample: {
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "type": "Bearer",
#   "expiresIn": 3600
# }
```

## 🚀 IDE Setup

### Visual Studio Code

1. **Extensions to Install:**
    - Extension Pack for Java
    - Spring Boot Extension Pack
    - REST Client

2. **Run Configuration:**
    - VS Code → Run and Debug
    - Select "Java" launch configuration
    - Or use: `Ctrl+Shift+D`

3. **Debug Mode:**
    - Press `F5` to start debugging
    - Set breakpoints with `F9`

### IntelliJ IDEA

1. **Open Project:**
    - File → Open → Select udms folder
    - Wait for indexing

2. **Configure JDK:**
    - File → Project Structure → Project
    - Select Java 21 as SDK

3. **Run Configuration:**
    - Click Run → Edit Configurations
    - Create new "Spring Boot" configuration
    - Main class: `com.shehab.udms.UdmsApplication`
    - Click Run (Shift+F10)

## 🗄 Database Tools

### MySQL Workbench (GUI)

1. Download from [MySQL Community Downloads](https://dev.mysql.com/downloads/workbench/)
2. Connect with:
    - **Hostname**: 127.0.0.1
    - **Port**: 3306
    - **Username**: root
    - **Password**: 1234

3. Browse `udms` database to view auto-created tables

### Command Line MySQL

```bash
# Connect to database
mysql -u root -p -h localhost udms

# Show tables (after app starts)
SHOW TABLES;

# Describe a table
DESCRIBE users;
DESCRIBE course;
DESCRIBE student;
```

## 📊 Sample Data

### Insert Test Data

```sql
-- Insert test student
INSERT INTO users (username, password, email, role)
VALUES ('student1', '$2a$10$...', 'student1@example.com', 'STUDENT');

-- Insert test teacher
INSERT INTO users (username, password, email, role)
VALUES ('teacher1', '$2a$10$...', 'teacher1@example.com', 'TEACHER');

-- Insert test semester
INSERT INTO semester (semester_name, start_date, end_date, status)
VALUES ('Spring 2026', '2026-01-15', '2026-05-30', 'ACTIVE');

-- Insert test course
INSERT INTO course (course_code, course_name, credits, semester_id, teacher_id, max_capacity)
VALUES ('CS101', 'Intro to CS', 3, 1, 2, 50);
```

## 🔧 Troubleshooting

### Problem: Maven Build Fails

**Solution:**

```bash
# Clear Maven cache
mvn clean

# Rebuild
mvn install -U
```

### Problem: MySQL Connection Error

**Check:**

```bash
# Verify MySQL is running
# Windows: Services → MySQL80 should be running

# Test connection
mysql -u root -p -h localhost
# Enter password: 1234
```

**Fix database connection in application.properties:**

```properties
# If using different port
spring.datasource.url=jdbc:mysql://localhost:3307/udms

# If using different password
spring.datasource.password=yourpassword
```

### Problem: Port 8080 Already in Use

**Solution:**

```bash
# Run on different port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"

# Or modify application.properties
server.port=8081
```

### Problem: Tables Not Created Automatically

**Check:**

```properties
# Ensure ddl-auto is set correctly
spring.jpa.hibernate.ddl-auto=update

# Restart application
```

**Manual Fix:**

```bash
# Connect to MySQL and create tables manually
mysql -u root -p udms < database_schema.sql
```

### Problem: JWT Token Expired

**Solution:**

- Get new token by logging in again
- Token expires in 3600 seconds (1 hour)

### Problem: 403 Forbidden Error

**Cause:** Missing or invalid JWT token

**Solution:**

```bash
# Ensure Authorization header is included
Authorization: Bearer <valid_jwt_token>

# Get valid token from /login endpoint
```

## 📈 Performance Tuning

### Enable SQL Logging for Debugging

```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
spring.jpa.show-sql=true
```

### Connection Pool Configuration

```properties
# Edit application.properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

## 🔒 Security Setup

### Generate JWT Secret Key

Currently set in `application.properties`. For production:

```bash
# Generate strong random key
# Using OpenSSL
openssl rand -base64 32

# Add to application.properties
jwt.secret=your_generated_key_here
```

### Change Database Password

```bash
# Connect to MySQL as root
mysql -u root -p

# Change root password
ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword';
FLUSH PRIVILEGES;

# Update application.properties
spring.datasource.password=newpassword
```

## ✅ Verification Checklist

- [ ] Java 21 installed and verified
- [ ] Maven installed and verified
- [ ] MySQL 8.0+ installed and running
- [ ] UDMS database created
- [ ] Project cloned/extracted
- [ ] application.properties configured
- [ ] Project built successfully (`mvn clean install`)
- [ ] Application runs without errors
- [ ] Can register new user (POST /register)
- [ ] Can login and receive JWT token (POST /login)
- [ ] Database tables created automatically
- [ ] Can execute API calls with valid JWT token

## 📞 Common Commands

```bash
# Clean project
mvn clean

# Build project
mvn install

# Run tests
mvn test

# Run application
mvn spring-boot:run

# Build JAR
mvn package

# Show dependency tree
mvn dependency:tree

# Skip tests during build
mvn install -DskipTests
```

## 📝 Next Steps

1. Review [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for API endpoints
2. Review [ARCHITECTURE.md](ARCHITECTURE.md) for system design
3. Check [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) for data model
4. Test API endpoints using Postman or cURL

---

**Setup Guide Version**: 1.0  
**Last Updated**: March 2026
