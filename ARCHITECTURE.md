# UDMS - System Architecture

Comprehensive architectural documentation for the University Department Management System.

## 📐 Architecture Overview

UDMS follows a **layered architecture** pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                   Client Layer                          │
│              (Web/Mobile Application)                   │
└────────────────────────┬────────────────────────────────┘
                         │ REST Requests
                         ▼
┌─────────────────────────────────────────────────────────┐
│                   API Gateway                           │
│  (CORS, Authentication, Request Routing)                │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│            Presentation Layer (Controllers)             │
│  - AdminController                                      │
│  - StudentController                                    │
│  - CourseController                                     │
│  - TeacherController                                    │
│  - AttendanceController                                 │
│  - NoticeController                                     │
│  - SemesterController                                   │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│            Business Logic Layer (Services)              │
│  - AdminService      - StudentService                  │
│  - CourseService     - TeacherService                  │
│  - AttendanceService - NoticeService                   │
│  - SemesterService                                      │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│            Data Access Layer (Repositories)             │
│  - AdminRepo      - StudentRepo                         │
│  - CourseRepo     - TeacherRepo                         │
│  - AttendanceRepo - NoticeRepo                          │
│  - SemesterRepo                                         │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│                  Database Layer                         │
│             (MySQL with Hibernate ORM)                  │
└─────────────────────────────────────────────────────────┘
```

## 🏗 Component Architecture

### 1. Controllers (Presentation Layer)

Controllers handle incoming HTTP requests and format responses.

**Location**: `src/main/java/com/shehab/udms/controller/`

| Controller                  | Purpose                                          |
| --------------------------- | ------------------------------------------------ |
| `UserController`            | User registration, login, and profile management |
| `StudentController`         | Student information and enrollment               |
| `TeacherController`         | Teacher profiles and assignments                 |
| `CourseController`          | Course management and details                    |
| `AttendanceController`      | Attendance tracking and records                  |
| `SemesterController`        | Semester management                              |
| `NoticeController`          | Notice board operations                          |
| `AdminController`           | Administrative functions                         |
| `HelloController`           | Health check/test endpoint                       |
| `StudentEnrolledController` | Enrollment tracking                              |

**Typical Flow**:

```java
@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getCourses() {
        // Delegate to service
        List<CourseDTO> courses = courseService.getAllCourses();
        // Format response
        return ApiResponse.success(courses);
    }
}
```

### 2. Services (Business Logic Layer)

Services contain core business logic and coordinate between controllers and repositories.

**Location**: `src/main/java/com/shehab/udms/service/`

| Service                | Responsibilities                                       |
| ---------------------- | ------------------------------------------------------ |
| `CourseService`        | Course CRUD, enrollment capacity, course validation    |
| `StudentService`       | Student enrollment, GPA calculation, status management |
| `TeacherService`       | Teacher profile, course assignments                    |
| `AttendanceService`    | Attendance recording, statistics, reports              |
| `SemesterService`      | Semester CRUD, course scheduling                       |
| `NoticeService`        | Notice management, filtering                           |
| `AdminService`         | System administration, analytics                       |
| `UserService`          | User profile, password changes                         |
| `JWTService`           | Token generation and validation                        |
| `MyUserDetailsService` | User authentication and authorization                  |

**Key Methods**:

```java
@Service
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;

    public CourseDTO createCourse(CourseDTO dto) {
        // Business logic: validate, transform, save
        Course course = new Course();
        course.setName(dto.getName());
        // Validation logic
        return courseRepo.save(course);
    }

    public List<CourseDTO> getByDepartment(String dept) {
        // Complex business logic
        return courseRepo.findByDepartment(dept)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
}
```

### 3. Repositories (Data Access Layer)

Spring Data JPA repositories provide database access without boilerplate.

**Location**: `src/main/java/com/shehab/udms/repo/`

| Repository            | Entity          |
| --------------------- | --------------- |
| `CourseRepo`          | Course          |
| `StudentRepo`         | Student         |
| `TeacherRepo`         | Teacher         |
| `AdminRepo`           | Admin           |
| `AttendanceRepo`      | Attendance      |
| `SemesterRepo`        | Semester        |
| `NoticeRepo`          | Notice          |
| `UserRepo`            | Users           |
| `StudentEnrolledRepo` | StudentEnrolled |

**Interface Design**:

```java
@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
    // Auto-implemented methods
    List<Course> findByDepartment(String dept);
    Course findByCode(String courseCode);
    @Query("SELECT c FROM Course c WHERE c.semester.id = ?1")
    List<Course> findBySemester(Long semesterId);
}
```

### 4. Models (Entity Layer)

JPA entities represent database tables with ORM mapping.

**Location**: `src/main/java/com/shehab/udms/model/`

**Core Entities**:

```
Users (Base user entity)
├── Student
├── Teacher
└── Admin

Course
├── Semester (Many-to-One relationship)
└── Teacher (Many-to-One relationship)

StudentEnrolled (Junction table)
├── Student (Many-to-One)
└── Course (Many-to-One)

Attendance
├── Student (Many-to-One)
└── Course (Many-to-One)

Notice

Semester
```

### 5. DTOs (Data Transfer Objects)

DTOs define API request/response payloads, separating API contracts from entities.

**Location**: `src/main/java/com/shehab/udms/DTO/`

**Example DTO**:

```java
@Data
@NoArgsConstructor
public class CourseDTO {
    private Long id;
    private String code;
    private String name;
    private Integer credits;
    private Long teacherId;
    private Long semesterId;
    // Only includes fields needed for API
}

@Data
@NoArgsConstructor
public class CourseSimpleDTO {
    private Long id;
    private String code;
    private String name;
    // Simplified view for lists
}
```

### 6. Security & Filtering

JWT-based authentication with Spring Security.

**Location**: `src/main/java/com/shehab/udms/filter/` and `config/`

```
Request
  ↓
JWTFilter (Validate token)
  ↓
SecurityContext (Set authentication)
  ↓
Authorization Checks (@PreAuthorize)
  ↓
Controller
```

**JWT Flow**:

1. **Registration/Login**:
    - User credentials → UserDetailsService
    - Validate credentials
    - Generate JWT token (JWTService)

2. **Protected Request**:
    - Client sends Authorization header with JWT
    - JWTFilter intercepts request
    - JWTFilter validates token signature and expiry
    - JWTFilter extracts user details from token
    - SecurityContext updated with authenticated user
    - Request proceeds to controller

### 7. Utility Classes

**Location**: `src/main/java/com/shehab/udms/utility/`

**ApiResponse**: Standardized API response wrapper

```java
public class ApiResponse {
    private String status;      // SUCCESS, ERROR
    private String message;
    private Object data;

    public static ResponseEntity<?> success(Object data) {
        return ResponseEntity.ok(
            new ApiResponse("SUCCESS", "Operation successful", data)
        );
    }
}
```

### 8. Custom Types

**Location**: `src/main/java/com/shehab/udms/types/`

**Role Enum**:

```java
public enum Role {
    STUDENT,
    TEACHER,
    ADMIN
}
```

## 🔐 Security Architecture

### Authentication

- **Method**: JWT (JSON Web Tokens)
- **Flow**:
    1. User logs in with credentials
    2. Server validates and returns JWT
    3. Client stores JWT
    4. Client includes JWT in Authorization header for all requests

### Authorization

- **Method**: Role-Based Access Control (RBAC)
- **Roles**: STUDENT, TEACHER, ADMIN
- **Implementation**: Spring Security with @PreAuthorize

### Password Encoding

- **Algorithm**: BCrypt
- **Configuration**: SecurityConfig bean provides PasswordEncoder

### CORS

- **Status**: Enabled
- **Configuration**: CORSConfigurationSource in SecurityConfig

## 📊 Data Model Relationships

### Entity Relationships

```
Users (Base Table)
├── id (PK)
├── username (UNIQUE)
├── password (Encrypted)
├── email
├── role (ENUM: STUDENT, TEACHER, ADMIN)
└── created_at

Student
├── id (PK)
├── user_id (FK → Users)
├── student_id (Unique identifier)
├── first_name
├── last_name
├── date_of_birth
├── phone
├── current_semester_id (FK → Semester)
└── gpa

Teacher
├── id (PK)
├── user_id (FK → Users)
├── teacher_id (Unique identifier)
├── first_name
├── last_name
├── department
├── specialization
└── phone

Course
├── id (PK)
├── course_code (UNIQUE)
├── course_name
├── credits
├── description
├── semester_id (FK → Semester)
├── teacher_id (FK → Teacher)
└── max_capacity

Semester
├── id (PK)
├── semester_name
├── start_date
├── end_date
├── status (ACTIVE, INACTIVE, ARCHIVED)
└── description

StudentEnrolled (Junction Table)
├── id (PK)
├── student_id (FK → Student)
├── course_id (FK → Course)
├── enrolled_date
├── status (ENROLLED, DROPPED, COMPLETED)
└── grade

Attendance
├── id (PK)
├── student_id (FK → Student)
├── course_id (FK → Course)
├── attendance_date
├── status (PRESENT, ABSENT, LATE)
└── recorded_at

Notice
├── id (PK)
├── title
├── content
├── type (GENERAL, ACADEMIC, EVENT)
├── posted_by_id (FK → Users)
├── created_at
└── updated_at

Admin
├── id (PK)
├── user_id (FK → Users)
├── admin_id (Unique identifier)
└── permissions
```

## 🔄 Request-Response Flow Example

### Example: Create Course Flow

```
1. Client sends POST /courses
   {
     "courseCode": "CS101",
     "courseName": "Intro to CS",
     "credits": 3,
     "semesterId": 1,
     "teacherId": 5
   }

2. JWTFilter intercepts
   - Validates JWT token
   - Extracts user information
   - Sets SecurityContext

3. CourseController.createCourse()
   - Receives CourseDTO
   - Calls courseService.createCourse()

4. CourseService.createCourse()
   - Validates input (code uniqueness, semester exists, teacher exists)
   - Transforms DTO to Course entity
   - Calls courseRepo.save()

5. CourseRepo.save()
   - Executes SQL INSERT
   - Returns persisted entity

6. Service transforms to DTO

7. Controller returns ResponseEntity<CourseDTO>

8. Client receives JSON response
   {
     "status": "SUCCESS",
     "data": {
       "id": 10,
       "courseCode": "CS101",
       ...
     }
   }
```

## 🗄 Database Schema

### Schema Overview

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    role ENUM('STUDENT', 'TEACHER', 'ADMIN'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,
    student_id VARCHAR(20) UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    date_of_birth DATE,
    phone VARCHAR(20),
    current_semester_id BIGINT,
    gpa DECIMAL(3,2),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (current_semester_id) REFERENCES semester(id)
);

CREATE TABLE course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    credits INT,
    description TEXT,
    semester_id BIGINT,
    teacher_id BIGINT,
    max_capacity INT,
    FOREIGN KEY (semester_id) REFERENCES semester(id),
    FOREIGN KEY (teacher_id) REFERENCES teacher(id)
);

-- Additional tables follow similar pattern
```

## 🚀 Deployment Architecture

```
Development:
    Maven Build → Spring Boot Embedded Tomcat → MySQL (Local)

Production:
    Code Repository
    ↓
    Build Pipeline (Maven)
    ↓
    Docker Container
    ↓
    Application Server (Tomcat)
    ↓
    Database (MySQL)
    ↓
    Reverse Proxy (Nginx)
    ↓
    Client Applications
```

## 📈 Performance Considerations

1. **Database Indexing**: Primary keys and foreign keys indexed
2. **Query Optimization**: Use specific SELECT columns, pagination
3. **Caching**: Semester and course data candidates for caching
4. **Connection Pooling**: Handled by Spring
5. **Lazy Loading**: GTOs prevent N+1 query problems

## 🔄 Future Architecture Enhancements

1. **Microservices**: Split into separate services (User, Course, Attendance)
2. **API Gateway**: Dedicated gateway for routing and authentication
3. **Message Queue**: For async operations (notifications, reporting)
4. **Caching Layer**: Redis for performance optimization
5. **Search Engine**: Elasticsearch for advanced search
6. **Logging**: ELK stack for centralized logging
7. **Monitoring**: Prometheus + Grafana for metrics

---

**Architecture Version**: 1.0  
**Last Updated**: March 2026
