package com.shehab.udms.service;

import com.shehab.udms.DTO.AttendanceRequestDTO;
import com.shehab.udms.DTO.TokenData;
import com.shehab.udms.model.*;
import com.shehab.udms.repo.*;
import com.shehab.udms.types.Status;
import com.shehab.udms.utility.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AttendanceWsService {

    @Autowired private QRCodeGenerator qrGenerator;
    @Autowired private AttendanceRepo attendanceRepo;
    @Autowired private StudentRepo studentRepo;
    @Autowired private CourseRepo courseRepo;
    @Autowired private SemesterRepo semesterRepo;

    // Token storage (Consider using Redis for production/cluster environments)
    private final Map<String, TokenData> tokenStorage = new ConcurrentHashMap<>();

    /**
     * Generates a QR code.
     * Note: We use JSON format for the QR content to make it easy for the Frontend
     * to parse and send back to the @RequestBody DTO.
     */
    public String generateTeacherQR(Long courseId, Long semesterId) throws Exception {
        // Using UUID ensures uniqueness and security against guessing
        String token = UUID.randomUUID().toString().substring(0, 8);

        // Store token data with current timestamp
        tokenStorage.put(token, new TokenData(courseId, semesterId, System.currentTimeMillis()));

        // We send JSON string in QR so Frontend's JSON.parse() works perfectly
        String qrContent = String.format(
                "{\"courseId\":\"%d\",\"semesterId\":\"%d\",\"qrToken\":\"%s\"}",
                courseId, semesterId, token
        );

        return qrGenerator.generateBase64QR(qrContent);
    }

    @Transactional
    public String markAttendanceByQR(AttendanceRequestDTO request, String username) {
        // --- START DEBUGGING LOGS ---
//        System.out.println("--- QR SCAN DEBUG ---");
//        System.out.println("Received Token from React: '" + request.getQrToken() + "'");
//        System.out.println("Current Tokens in Server Memory: " + tokenStorage.keySet());
        // --- END DEBUGGING LOGS ---

        // 0. Clean the token string just in case
        String cleanToken = request.getQrToken() != null ? request.getQrToken().trim() : "";

        // 1. Token existence check
        TokenData cachedData = tokenStorage.get(cleanToken);
        if (cachedData == null) {
            return "Invalid or used QR code!";
        }

        // 2. Expiry check (60 seconds)
        long elapsed = System.currentTimeMillis() - cachedData.getTimestamp();
        if (elapsed > 60000) {
            tokenStorage.remove(cleanToken);
            return "QR Code Expired!";
        }

        // 3. Security: Data matching
        if (!cachedData.getCourseId().equals(request.getCourseId()) ||
                !cachedData.getSemesterId().equals(request.getSemesterId())) {
            return "Data tampering detected!";
        }

        // 4. Fetch Entities safely
        Student student = studentRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found for username: " + username));

        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Semester semester = semesterRepo.findById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        LocalDate today = LocalDate.now();

        // 5. Check if already marked for today
        Attendance attendance = attendanceRepo
                .findByStudentAndCourseAndSemesterAndDate(student, course, semester, today)
                .orElse(new Attendance());

        // 6. Update/Save Attendance
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setSemester(semester);
        attendance.setDate(today);
        attendance.setStatus(Status.P); // Present
        attendance.setMarkedAt(LocalDateTime.now());
        attendance.setUpdatedBy(username);

        attendanceRepo.save(attendance);

        // 7. Optional: Remove token after single use
        // tokenStorage.remove(cleanToken);

        return "Attendance marked successfully!";
    }
}