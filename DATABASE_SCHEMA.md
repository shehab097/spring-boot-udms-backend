# UDMS - Database Schema Documentation

Complete database schema, entity relationships, and data model for the University Department Management System.

## 📊 Database Overview

- **Database Name**: `udms`
- **DBMS**: MySQL 8.0+
- **Total Tables**: 9
- **Character Set**: utf8mb4
- **Collation**: utf8mb4_unicode_ci

```sql
CREATE DATABASE udms;
USE udms;
```

## 🗂 Entity Relationship Diagram (ERD)

```
┌──────────────┐
│    Users     │ (Base Entity)
├──────────────┤
│ id (PK)      │
│ username     │
│ password     │
│ email        │
│ role         │
└──┬───┬───┬───┘
   │   │   │
   ▼   ▼   ▼
┌────────┐ ┌────────┐ ┌───────┐
│Student │ │Teacher │ │ Admin │
└────┬───┘ └───┬────┘ └───────┘
     │         │
     ▼         ▼
┌──────────────────────────┐
│      StudentEnrolled     │ ◄──────────────┐
│   (Many-to-Many)         │                │
└────────┬─────────────────┘                │
         │                                  │
         ├──────────────┐                   │
         ▼              ▼                   │
    ┌────────────┐  ┌──────────────┐      │
    │   Course   │  │  Attendance  │      │
    ├────────────┤  ├──────────────┤      │
    │ id (PK)    │  │ id (PK)      │      │
    │ code       │  │ date         │      │
    │ name       │  │ status       │      │
    │ semester_id├──┤ student_id ──┼──────┘
    │ teacher_id │  │ course_id    │
    └────────────┘  └──────────────┘
         │
         ▼
    ┌──────────────┐
    │  Semester    │
    ├──────────────┤
    │ id (PK)      │
    │ name         │
    │ start_date   │
    │ end_date     │
    │ status       │
    └──────────────┘

    ┌──────────────┐
    │   Notice     │
    ├──────────────┤
    │ id (PK)      │
    │ title        │
    │ content      │
    │ posted_by_id │ (FK → Users)
    └──────────────┘
```

## 📋 Tables

### 1. Users Table

Base user entity for all system users.

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT 'Unique username for login',
    password VARCHAR(255) NOT NULL COMMENT 'BCrypt encrypted password',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT 'User email address',
    role ENUM('STUDENT', 'TEACHER', 'ADMIN') NOT NULL DEFAULT 'STUDENT' COMMENT 'User role',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'Account status: ACTIVE, INACTIVE, DISABLED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
) COMMENT='Base user table for authentication'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Columns**:

| Column     | Type         | Constraint         | Description                 |
| ---------- | ------------ | ------------------ | --------------------------- |
| id         | BIGINT       | PK, AUTO_INCREMENT | Unique identifier           |
| username   | VARCHAR(50)  | UNIQUE, NOT NULL   | Login username              |
| password   | VARCHAR(255) | NOT NULL           | Encrypted password (BCrypt) |
| email      | VARCHAR(100) | UNIQUE, NOT NULL   | Email address               |
| role       | ENUM         | NOT NULL           | STUDENT, TEACHER, or ADMIN  |
| status     | VARCHAR(20)  | DEFAULT 'ACTIVE'   | Account status              |
| created_at | TIMESTAMP    | DEFAULT NOW()      | Record creation time        |
| updated_at | TIMESTAMP    | AUTO UPDATE        | Last modification time      |

---

### 2. Student Table

Student-specific information linked to Users.

```sql
CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL COMMENT 'Link to Users table',
    student_id VARCHAR(20) UNIQUE NOT NULL COMMENT 'Student ID (e.g., STU001)',
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    phone VARCHAR(20),
    current_semester_id BIGINT,
    gpa DECIMAL(3,2) UNSIGNED DEFAULT 0.00 COMMENT 'Current GPA',
    total_attendance_percentage DECIMAL(5,2) UNSIGNED DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, SUSPENDED, INACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (current_semester_id) REFERENCES semester(id),
    INDEX idx_student_id (student_id),
    INDEX idx_user_id (user_id),
    INDEX idx_semester (current_semester_id)
) COMMENT='Student profile and academic information'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Columns**:

| Column                      | Type         | Constraint         | Description                 |
| --------------------------- | ------------ | ------------------ | --------------------------- |
| id                          | BIGINT       | PK, AUTO_INCREMENT | Primary key                 |
| user_id                     | BIGINT       | FK, UNIQUE         | Reference to Users table    |
| student_id                  | VARCHAR(20)  | UNIQUE             | Student identifier          |
| first_name                  | VARCHAR(50)  | NOT NULL           | Student's first name        |
| last_name                   | VARCHAR(50)  | NOT NULL           | Student's last name         |
| date_of_birth               | DATE         |                    | Date of birth               |
| phone                       | VARCHAR(20)  |                    | Contact phone number        |
| current_semester_id         | BIGINT       | FK                 | Current enrolled semester   |
| gpa                         | DECIMAL(3,2) |                    | Grade Point Average         |
| total_attendance_percentage | DECIMAL(5,2) |                    | Overall attendance %        |
| status                      | VARCHAR(20)  |                    | ACTIVE, SUSPENDED, INACTIVE |

---

### 3. Teacher Table

Teacher-specific information linked to Users.

```sql
CREATE TABLE teacher (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    teacher_id VARCHAR(20) UNIQUE NOT NULL COMMENT 'Teacher ID (e.g., TECH001)',
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    department VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20),
    office_location VARCHAR(100),
    qualifications VARCHAR(255),
    experience_years INT UNSIGNED,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_department (department)
) COMMENT='Teacher profile and qualifications'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 4. Admin Table

Administrative user information.

```sql
CREATE TABLE admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    admin_id VARCHAR(20) UNIQUE NOT NULL COMMENT 'Admin identifier',
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    department VARCHAR(100),
    phone VARCHAR(20),
    permissions JSON COMMENT 'Admin permissions stored as JSON',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_admin_id (admin_id)
) COMMENT='Administrator profiles'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 5. Semester Table

Academic semester information.

```sql
CREATE TABLE semester (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    semester_name VARCHAR(100) NOT NULL COMMENT 'e.g., Spring 2026',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'INACTIVE' COMMENT 'PLANNING, ACTIVE, COMPLETED, ARCHIVED',
    total_days INT GENERATED ALWAYS AS (DATEDIFF(end_date, start_date) + 1) STORED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_semester (semester_name),
    INDEX idx_status (status),
    INDEX idx_start_date (start_date)
) COMMENT='Academic semester definitions'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Columns**:

| Column        | Type         | Constraint | Description                           |
| ------------- | ------------ | ---------- | ------------------------------------- |
| id            | BIGINT       | PK         | Primary key                           |
| semester_name | VARCHAR(100) | UNIQUE     | Semester name (e.g., Spring 2026)     |
| start_date    | DATE         | NOT NULL   | Semester start date                   |
| end_date      | DATE         | NOT NULL   | Semester end date                     |
| description   | TEXT         |            | Semester description                  |
| status        | VARCHAR(20)  |            | PLANNING, ACTIVE, COMPLETED, ARCHIVED |
| total_days    | INT          | GENERATED  | Calculated total days                 |

---

### 6. Course Table

Course offerings with enrollment and teacher assignment.

```sql
CREATE TABLE course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) UNIQUE NOT NULL COMMENT 'e.g., CS101',
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL COMMENT 'Course credit hours',
    description TEXT,
    semester_id BIGINT NOT NULL COMMENT 'Associated semester',
    teacher_id BIGINT NOT NULL COMMENT 'Assigned teacher',
    max_capacity INT NOT NULL DEFAULT 50,
    current_enrollment INT DEFAULT 0,
    schedule VARCHAR(100) COMMENT 'e.g., MWF 10:00-11:00',
    room_number VARCHAR(20) COMMENT 'Classroom location',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (semester_id) REFERENCES semester(id) ON DELETE RESTRICT,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE SET NULL,
    INDEX idx_course_code (course_code),
    INDEX idx_semester (semester_id),
    INDEX idx_teacher (teacher_id),
    CONSTRAINT chk_capacity CHECK (max_capacity > 0)
) COMMENT='Course offerings'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Columns**:

| Column             | Type         | Constraint   | Description                 |
| ------------------ | ------------ | ------------ | --------------------------- |
| id                 | BIGINT       | PK           | Primary key                 |
| course_code        | VARCHAR(20)  | UNIQUE       | Unique course code          |
| course_name        | VARCHAR(100) | NOT NULL     | Full course name            |
| credits            | INT          | NOT NULL     | Credit hours                |
| description        | TEXT         |              | Course description          |
| semester_id        | BIGINT       | FK, NOT NULL | Reference to semester       |
| teacher_id         | BIGINT       | FK, NOT NULL | Assigned teacher            |
| max_capacity       | INT          | NOT NULL     | Max students allowed        |
| current_enrollment | INT          |              | Current enrollment count    |
| schedule           | VARCHAR(100) |              | Class schedule pattern      |
| room_number        | VARCHAR(20)  |              | Classroom location          |
| status             | VARCHAR(20)  |              | ACTIVE, INACTIVE, CANCELLED |

---

### 7. StudentEnrolled Table

Many-to-Many relationship between Students and Courses.

```sql
CREATE TABLE student_enrolled (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL COMMENT 'Student ID',
    course_id BIGINT NOT NULL COMMENT 'Course ID',
    enrolled_date DATE NOT NULL DEFAULT CURDATE(),
    grade VARCHAR(2) COMMENT 'Letter grade: A, B, C, D, F, or NULL if incomplete',
    final_score DECIMAL(5,2) COMMENT 'Numerical score (0-100)',
    status VARCHAR(20) DEFAULT 'ENROLLED' COMMENT 'ENROLLED, COMPLETED, DROPPED, FAILED',
    completion_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id),
    INDEX idx_student (student_id),
    INDEX idx_course (course_id),
    INDEX idx_status (status)
) COMMENT='Student course enrollments'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Columns**:

| Column          | Type         | Constraint | Description                  |
| --------------- | ------------ | ---------- | ---------------------------- |
| id              | BIGINT       | PK         | Primary key                  |
| student_id      | BIGINT       | FK         | Reference to student         |
| course_id       | BIGINT       | FK         | Reference to course          |
| enrolled_date   | DATE         |            | Enrollment date              |
| grade           | VARCHAR(2)   |            | Final letter grade           |
| final_score     | DECIMAL(5,2) |            | Numerical score              |
| status          | VARCHAR(20)  |            | ENROLLED, COMPLETED, DROPPED |
| completion_date | DATE         |            | Course completion date       |

---

### 8. Attendance Table

Student attendance records.

```sql
CREATE TABLE attendance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL COMMENT 'PRESENT, ABSENT, LATE',
    remarks VARCHAR(255) COMMENT 'Notes about absence reason',
    recorded_by BIGINT COMMENT 'Teacher ID who recorded',
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    FOREIGN KEY (recorded_by) REFERENCES teacher(id) ON DELETE SET NULL,
    UNIQUE KEY unique_attendance (student_id, course_id, attendance_date),
    INDEX idx_student (student_id),
    INDEX idx_course (course_id),
    INDEX idx_date (attendance_date),
    INDEX idx_status (status)
) COMMENT='Student attendance tracking'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Columns**:

| Column          | Type         | Constraint | Description                    |
| --------------- | ------------ | ---------- | ------------------------------ |
| id              | BIGINT       | PK         | Primary key                    |
| student_id      | BIGINT       | FK         | Reference to student           |
| course_id       | BIGINT       | FK         | Reference to course            |
| attendance_date | DATE         | NOT NULL   | Date of attendance             |
| status          | VARCHAR(20)  | NOT NULL   | PRESENT, ABSENT, LATE, EXCUSED |
| remarks         | VARCHAR(255) |            | Reason for absence             |
| recorded_by     | BIGINT       | FK         | Teacher who recorded           |
| recorded_at     | TIMESTAMP    |            | Recording timestamp            |

---

### 9. Notice Table

Notice board messages.

```sql
CREATE TABLE notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content LONGTEXT NOT NULL,
    type VARCHAR(50) NOT NULL COMMENT 'GENERAL, ACADEMIC, EVENT, URGENT',
    posted_by_id BIGINT NOT NULL COMMENT 'User ID of poster (usually admin)',
    target_audience VARCHAR(50) DEFAULT 'ALL' COMMENT 'ALL, STUDENTS, TEACHERS, SPECIFIC_SEMESTER',
    view_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    expiry_date DATE COMMENT 'Notice expiry date',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (posted_by_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_type (type),
    INDEX idx_created_date (created_at),
    INDEX idx_is_active (is_active)
) COMMENT='Notice board messages'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Columns**:

| Column          | Type         | Constraint | Description                      |
| --------------- | ------------ | ---------- | -------------------------------- |
| id              | BIGINT       | PK         | Primary key                      |
| title           | VARCHAR(200) | NOT NULL   | Notice title                     |
| content         | LONGTEXT     | NOT NULL   | Full notice content              |
| type            | VARCHAR(50)  |            | GENERAL, ACADEMIC, EVENT, URGENT |
| posted_by_id    | BIGINT       | FK         | User who posted                  |
| target_audience | VARCHAR(50)  |            | Intended audience                |
| view_count      | INT          |            | Number of views                  |
| is_active       | BOOLEAN      |            | Active status                    |
| expiry_date     | DATE         |            | When notice expires              |

---

## 🔗 Relationships Summary

| Relationship              | Type | From        | To                          | Constraint |
| ------------------------- | ---- | ----------- | --------------------------- | ---------- |
| User → Student            | 1:1  | users.id    | student.user_id             | CASCADE    |
| User → Teacher            | 1:1  | users.id    | teacher.user_id             | CASCADE    |
| User → Admin              | 1:1  | users.id    | admin.user_id               | CASCADE    |
| Teacher → Course          | 1:N  | teacher.id  | course.teacher_id           | SET NULL   |
| Semester → Course         | 1:N  | semester.id | course.semester_id          | RESTRICT   |
| Student → StudentEnrolled | 1:N  | student.id  | student_enrolled.student_id | CASCADE    |
| Course → StudentEnrolled  | 1:N  | course.id   | student_enrolled.course_id  | CASCADE    |
| Student → Attendance      | 1:N  | student.id  | attendance.student_id       | CASCADE    |
| Course → Attendance       | 1:N  | course.id   | attendance.course_id        | CASCADE    |
| User → Notice             | 1:N  | users.id    | notice.posted_by_id         | SET NULL   |

---

## 🔐 Constraints and Rules

### Data Integrity Constraints

1. **Unique Constraints**:
    - Username must be unique
    - Email must be unique
    - Student ID must be unique
    - Teacher ID must be unique
    - Course Code must be unique
    - Each student can enroll in course only once

2. **Foreign Key Constraints**:
    - CASCADE DELETE: Users → Student/Teacher/Admin (when user deleted, cascade to role)
    - CASCADE DELETE: Student → StudentEnrolled, Attendance
    - CASCADE DELETE: Course → StudentEnrolled, Attendance
    - SET NULL: Course → Teacher (if teacher deleted, course assignment becomes NULL)
    - RESTRICT: Semester → Course (cannot delete semester with active courses)

3. **Check Constraints**:
    - Max capacity must be > 0
    - Final score must be 0-100
    - Course credits must be > 0

---

## 📈 Indexing Strategy

### Indexed Columns (Performance Optimization)

```sql
-- User lookups
INDEX idx_username (username)
INDEX idx_email (email)
INDEX idx_role (role)

-- Student queries
INDEX idx_student_id (student_id)
INDEX idx_semester (current_semester_id)

-- Course filtering
INDEX idx_course_code (course_code)
INDEX idx_semester (semester_id)
INDEX idx_teacher (teacher_id)
INDEX idx_status (status)

-- Enrollment queries
INDEX idx_student (student_id)
INDEX idx_course (course_id)
INDEX idx_status (status)

-- Attendance reports
INDEX idx_date (attendance_date)
INDEX idx_status (status)

-- Notice queries
INDEX idx_type (type)
INDEX idx_created_date (created_at)
INDEX idx_is_active (is_active)
```

---

## 🗄 SQL Views (Useful Queries)

```sql
-- Student Enrollment Summary
CREATE VIEW student_enrollment_summary AS
SELECT
    s.id,
    s.student_id,
    s.first_name,
    s.last_name,
    COUNT(se.id) as courses_enrolled,
    AVG(se.final_score) as average_score,
    SUM(c.credits) as total_credits
FROM student s
LEFT JOIN student_enrolled se ON s.id = se.student_id
LEFT JOIN course c ON se.course_id = c.id
GROUP BY s.id;

-- Course Enrollment Status
CREATE VIEW course_enrollment_status AS
SELECT
    c.id,
    c.course_code,
    c.course_name,
    COUNT(se.id) as current_enrollment,
    c.max_capacity,
    ROUND((COUNT(se.id) / c.max_capacity * 100), 2) as capacity_percentage
FROM course c
LEFT JOIN student_enrolled se ON c.id = se.course_id
GROUP BY c.id;

-- Student Attendance Report
CREATE VIEW student_attendance_report AS
SELECT
    s.id,
    s.student_id,
    s.first_name,
    c.course_code,
    COUNT(a.id) as total_classes,
    SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) as classes_present,
    ROUND((SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) / COUNT(a.id) * 100), 2) as attendance_percentage
FROM student s
JOIN attendance a ON s.id = a.student_id
JOIN course c ON a.course_id = c.id
GROUP BY s.id, c.id;
```

---

## 🔄 Data Flow

### User Registration → Course Enrollment → Attendance Tracking

```
1. User Registration
   INSERT INTO users (username, password, email, role)

2. Create Student Record
   INSERT INTO student (user_id, student_id, first_name, ...)

3. Enroll in Semester
   UPDATE student SET current_semester_id = ?

4. Enroll in Course
   INSERT INTO student_enrolled (student_id, course_id, enrolled_date)

5. Record Attendance
   INSERT INTO attendance (student_id, course_id, attendance_date, status)

6. Generate Report
   SELECT FROM student_attendance_report
```

---

## 📊 Database Statistics

```sql
-- Get table sizes
SELECT
    table_name,
    ROUND(((data_length + index_length) / 1024 / 1024), 2) as size_mb
FROM information_schema.tables
WHERE table_schema = 'udms'
ORDER BY data_length + index_length DESC;

-- Count records in each table
SELECT table_name, TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'udms'
ORDER BY TABLE_ROWS DESC;
```

---

## 🔧 Maintenance Queries

```sql
-- Backup database
mysqldump -u root -p udms > udms_backup.sql

-- Restore database
mysql -u root -p udms < udms_backup.sql

-- Optimize tables
OPTIMIZE TABLE users, student, teacher, course, semester, student_enrolled, attendance, notice, admin;

-- Check table integrity
CHECK TABLE users, student, teacher, course;

-- Repair table (if needed)
REPAIR TABLE users;
```

---

**Database Schema Version**: 1.0  
**Last Updated**: March 2026
