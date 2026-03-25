# UDMS API Documentation

Complete REST API endpoint reference for the University Department Management System.

## 📌 Base URL

```
http://localhost:8080
```

## 🔐 Authentication

All protected endpoints require JWT token in the Authorization header:

```
Authorization: Bearer <JWT_TOKEN>
```

Obtain token via `/login` endpoint.

## 📋 Response Format

All API responses follow a standardized format:

### Success Response

```json
{
    "status": "SUCCESS",
    "message": "Operation completed successfully",
    "data": {
        /* response data */
    }
}
```

### Error Response

```json
{
    "status": "ERROR",
    "message": "Error description",
    "error": "Error type"
}
```

---

## 🔓 Public Endpoints (No Authentication Required)

### 1. User Registration

**POST** `/register`

Create a new user account.

**Request Body:**

```json
{
    "username": "john_doe",
    "password": "SecurePassword123",
    "email": "john@example.com",
    "role": "STUDENT"
}
```

**Parameters:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| username | string | Yes | Unique username |
| password | string | Yes | User password (encrypted) |
| email | string | Yes | User email address |
| role | enum | Yes | User role: STUDENT, TEACHER, ADMIN |

**Response (200 OK):**

```json
{
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "STUDENT",
    "createdAt": "2026-03-26T10:30:00Z"
}
```

**Error Responses:**

- `400 Bad Request` - Invalid input data
- `409 Conflict` - Username already exists

---

### 2. User Login

**POST** `/login`

Authenticate user and obtain JWT token.

**Request Body:**

```json
{
    "username": "john_doe",
    "password": "SecurePassword123"
}
```

**Response (200 OK):**

```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "expiresIn": 3600,
    "user": {
        "id": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "role": "STUDENT"
    }
}
```

**Error Responses:**

- `401 Unauthorized` - Invalid credentials
- `404 Not Found` - User not found

---

## 🔒 Protected Endpoints (Authentication Required)

### User Management

#### 3. Get All Users

**GET** `/users`

Retrieve list of all users.

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| page | integer | Page number (default: 0) |
| size | integer | Records per page (default: 10) |
| role | string | Filter by role (STUDENT, TEACHER, ADMIN) |

**Response (200 OK):**

```json
[
    {
        "id": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "role": "STUDENT",
        "status": "ACTIVE"
    },
    {
        "id": 2,
        "username": "jane_smith",
        "email": "jane@example.com",
        "role": "TEACHER",
        "status": "ACTIVE"
    }
]
```

---

#### 4. Get User by ID

**GET** `/users/{id}`

Retrieve specific user information.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | integer | User ID |

**Response (200 OK):**

```json
{
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "STUDENT",
    "phone": "+1234567890",
    "status": "ACTIVE",
    "lastLogin": "2026-03-26T10:30:00Z"
}
```

**Error Responses:**

- `404 Not Found` - User not found

---

#### 5. Update User

**PUT** `/users/{id}`

Update user information.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | integer | User ID |

**Request Body:**

```json
{
    "email": "newemail@example.com",
    "phone": "+1987654321",
    "role": "TEACHER"
}
```

**Response (200 OK):**

```json
{
    "id": 1,
    "username": "john_doe",
    "email": "newemail@example.com",
    "phone": "+1987654321",
    "role": "TEACHER"
}
```

---

#### 6. Delete User

**DELETE** `/users/{id}`

Delete a user account.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | integer | User ID |

**Response (204 No Content):** Empty

**Error Responses:**

- `404 Not Found` - User not found
- `403 Forbidden` - Insufficient permissions

---

### Course Management

#### 7. Get All Courses

**GET** `/courses`

Retrieve list of all courses.

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| semester | integer | Filter by semester ID |
| department | string | Filter by department |
| page | integer | Page number |

**Response (200 OK):**

```json
[
    {
        "id": 1,
        "courseCode": "CS101",
        "courseName": "Introduction to Computer Science",
        "credits": 3,
        "semester": 1,
        "teacher": "Dr. Smith",
        "students": 45,
        "maxCapacity": 50
    }
]
```

---

#### 8. Create Course

**POST** `/courses`

Create a new course.

**Request Body:**

```json
{
    "courseCode": "CS102",
    "courseName": "Data Structures",
    "credits": 4,
    "description": "Study of fundamental data structures",
    "semester": 1,
    "teacherId": 5,
    "maxCapacity": 50
}
```

**Response (201 Created):**

```json
{
    "id": 2,
    "courseCode": "CS102",
    "courseName": "Data Structures",
    "credits": 4,
    "semester": 1,
    "teacher": "Dr. Johnson",
    "createdAt": "2026-03-26T10:30:00Z"
}
```

---

#### 9. Update Course

**PUT** `/courses/{id}`

Update course information.

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | integer | Course ID |

**Request Body:**

```json
{
    "maxCapacity": 55,
    "description": "Updated course description"
}
```

**Response (200 OK):** Updated course object

---

#### 10. Delete Course

**DELETE** `/courses/{id}`

Delete a course.

**Response (204 No Content):** Empty

---

### Student Management

#### 11. Get All Students

**GET** `/students`

Retrieve list of all students.

**Response (200 OK):**

```json
[
    {
        "id": 1,
        "studentId": "STU001",
        "firstName": "John",
        "lastName": "Doe",
        "email": "john@example.com",
        "enrolledCourses": 4,
        "gpa": 3.8,
        "semester": 2,
        "status": "ACTIVE"
    }
]
```

---

#### 12. Create Student

**POST** `/students`

Register a new student.

**Request Body:**

```json
{
    "firstName": "Alice",
    "lastName": "Brown",
    "email": "alice@example.com",
    "dateOfBirth": "2005-01-15",
    "phone": "+1234567890",
    "userId": 10,
    "semester": 1
}
```

**Response (201 Created):** Student object with generated studentId

---

#### 13. Get Student Details

**GET** `/students/{id}`

Retrieve detailed student information.

**Response (200 OK):**

```json
{
    "id": 1,
    "studentId": "STU001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "dateOfBirth": "2003-05-20",
    "phone": "+1234567890",
    "enrolled_courses": [
        {
            "courseId": 1,
            "courseCode": "CS101",
            "courseName": "Intro to CS",
            "grade": "A"
        }
    ],
    "gpa": 3.8,
    "attendance": 95.5,
    "semester": 2,
    "status": "ACTIVE"
}
```

---

#### 14. Update Student

**PUT** `/students/{id}`

Update student information.

**Response (200 OK):** Updated student object

---

#### 15. Delete Student

**DELETE** `/students/{id}`

Remove student from system.

**Response (204 No Content):** Empty

---

### Teacher Management

#### 16. Get All Teachers

**GET** `/teachers`

Retrieve list of all teachers.

**Response (200 OK):**

```json
[
    {
        "id": 1,
        "teacherId": "TECH001",
        "firstName": "Smith",
        "lastName": "Johnson",
        "email": "smith@example.com",
        "department": "Computer Science",
        "specialization": "AI/ML",
        "coursesAssigned": 3,
        "status": "ACTIVE"
    }
]
```

---

#### 17. Create Teacher

**POST** `/teachers`

Add a new teacher.

**Request Body:**

```json
{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane@example.com",
    "department": "Computer Science",
    "specialization": "Database Systems",
    "phone": "+1234567890",
    "userId": 15
}
```

**Response (201 Created):** Teacher object

---

#### 18. Get Teacher Details

**GET** `/teachers/{id}`

Retrieve teacher information.

**Response (200 OK):**

```json
{
    "id": 1,
    "teacherId": "TECH001",
    "firstName": "Smith",
    "lastName": "Johnson",
    "email": "smith@example.com",
    "department": "Computer Science",
    "specialization": "AI/ML",
    "assignedCourses": [
        {
            "courseId": 1,
            "courseName": "CS101"
        }
    ],
    "totalStudents": 120,
    "status": "ACTIVE"
}
```

---

#### 19. Update Teacher

**PUT** `/teachers/{id}`

Update teacher information.

**Response (200 OK):** Updated teacher object

---

#### 20. Delete Teacher

**DELETE** `/teachers/{id}`

Remove teacher from system.

**Response (204 No Content):** Empty

---

### Attendance Management

#### 21. Get Attendance Records

**GET** `/attendance`

Retrieve attendance records.

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| studentId | integer | Filter by student |
| courseId | integer | Filter by course |
| date | date | Filter by date (YYYY-MM-DD) |

**Response (200 OK):**

```json
[
    {
        "id": 1,
        "studentId": "STU001",
        "courseId": "CS101",
        "date": "2026-03-26",
        "status": "PRESENT",
        "recordedAt": "2026-03-26T10:30:00Z"
    }
]
```

---

#### 22. Record Attendance

**POST** `/attendance`

Mark student attendance for a class.

**Request Body:**

```json
{
    "studentId": 1,
    "courseId": 1,
    "date": "2026-03-26",
    "status": "PRESENT"
}
```

**Response (201 Created):** Attendance record

---

#### 23. Update Attendance

**PUT** `/attendance/{id}`

Update attendance record.

**Request Body:**

```json
{
    "status": "ABSENT"
}
```

**Response (200 OK):** Updated record

---

#### 24. Get Student Attendance Summary

**GET** `/attendance/student/{studentId}`

Get attendance statistics for a student.

**Response (200 OK):**

```json
{
    "studentId": 1,
    "totalClasses": 45,
    "attended": 43,
    "absent": 2,
    "attendancePercentage": 95.56
}
```

---

### Semester Management

#### 25. Get All Semesters

**GET** `/semesters`

Retrieve all semesters.

**Response (200 OK):**

```json
[
    {
        "id": 1,
        "semesterName": "Spring 2026",
        "startDate": "2026-01-15",
        "endDate": "2026-05-30",
        "status": "ACTIVE",
        "coursesOffered": 15
    }
]
```

---

#### 26. Create Semester

**POST** `/semesters`

Create a new semester.

**Request Body:**

```json
{
    "semesterName": "Fall 2026",
    "startDate": "2026-09-01",
    "endDate": "2026-12-20",
    "description": "Fall semester 2026"
}
```

**Response (201 Created):** Semester object

---

#### 27. Get Semester Details

**GET** `/semesters/{id}`

Get detailed semester information.

**Response (200 OK):** Semester with courses and statistics

---

#### 28. Update Semester

**PUT** `/semesters/{id}`

Update semester details.

**Response (200 OK):** Updated semester

---

### Notice Management

#### 29. Get All Notices

**GET** `/notices`

Retrieve all notices.

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| type | string | GENERAL, ACADEMIC, EVENT |
| date | date | Filter by date |

**Response (200 OK):**

```json
[
    {
        "id": 1,
        "title": "Semester Registration Open",
        "content": "Students can now register for Fall 2026...",
        "type": "ACADEMIC",
        "postedBy": "admin",
        "createdAt": "2026-03-26T10:30:00Z",
        "viewCount": 245
    }
]
```

---

#### 30. Create Notice

**POST** `/notices`

Post a new notice.

**Request Body:**

```json
{
    "title": "Library Closure",
    "content": "The library will be closed on 2026-03-27",
    "type": "GENERAL",
    "targetAudience": "ALL"
}
```

**Response (201 Created):** Notice object

---

#### 31. Get Notice Details

**GET** `/notices/{id}`

Retrieve specific notice.

**Response (200 OK):** Notice with full content

---

#### 32. Delete Notice

**DELETE** `/notices/{id}`

Delete a notice (admin only).

**Response (204 No Content):** Empty

---

### Student Enrollment

#### 33. Enroll Student in Course

**POST** `/students/{studentId}/enroll/{courseId}`

Add student to course.

**Response (200 OK):**

```json
{
    "studentId": 1,
    "courseId": 1,
    "enrolledAt": "2026-03-26T10:30:00Z",
    "status": "ENROLLED"
}
```

---

#### 34. Get Enrolled Students

**GET** `/courses/{courseId}/students`

List students enrolled in a course.

**Response (200 OK):** Array of enrolled students

---

#### 35. Drop Course

**DELETE** `/students/{studentId}/courses/{courseId}`

Remove student from course.

**Response (204 No Content):** Empty

---

### Admin Operations

#### 36. Get Admin Dashboard

**GET** `/admin/dashboard`

Administrative statistics and overview.

**Response (200 OK):**

```json
{
    "totalUsers": 500,
    "totalStudents": 350,
    "totalTeachers": 50,
    "totalCourses": 30,
    "activeSemester": 1,
    "recentActivities": []
}
```

---

#### 37. Get System Statistics

**GET** `/admin/statistics`

Detailed system statistics.

**Response (200 OK):** Comprehensive stats

---

#### 38. User Role Assignment

**POST** `/admin/assign-role`

Assign or change user role (admin only).

**Request Body:**

```json
{
    "userId": 5,
    "newRole": "TEACHER"
}
```

**Response (200 OK):** Updated user

---

## ⚠️ Error Codes

| Code | Status       | Description                       |
| ---- | ------------ | --------------------------------- |
| 200  | OK           | Request successful                |
| 201  | Created      | Resource created successfully     |
| 204  | No Content   | Successful deletion               |
| 400  | Bad Request  | Invalid request parameters        |
| 401  | Unauthorized | Missing or invalid authentication |
| 403  | Forbidden    | Insufficient permissions          |
| 404  | Not Found    | Resource not found                |
| 409  | Conflict     | Resource already exists           |
| 500  | Server Error | Internal server error             |

---

## 🔄 Common Workflows

### Workflow 1: Register and Login

```
1. POST /register - Create account
2. POST /login - Get JWT token
3. Use token in Authorization header for subsequent requests
```

### Workflow 2: Enroll Student in Course

```
1. POST /students - Create student profile
2. GET /courses - List available courses
3. POST /students/{id}/enroll/{courseId} - Enroll in course
```

### Workflow 3: Record Attendance

```
1. GET /courses - Get course ID
2. GET /students - Get student ID
3. POST /attendance - Record attendance
4. GET /attendance/student/{id} - View attendance record
```

---

## 📝 Rate Limiting

Currently, no rate limiting is implemented. Future versions will include rate limiting.

---

**API Version**: 1.0  
**Last Updated**: March 2026
