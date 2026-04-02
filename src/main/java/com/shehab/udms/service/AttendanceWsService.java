package com.shehab.udms.service;

import com.shehab.udms.DTO.AttendanceRequestDTO;
import com.shehab.udms.model.Attendance;
import com.shehab.udms.model.Course;
import com.shehab.udms.model.Semester;
import com.shehab.udms.model.Student;
import com.shehab.udms.repo.AttendanceRepo;
import com.shehab.udms.repo.CourseRepo;
import com.shehab.udms.repo.SemesterRepo;
import com.shehab.udms.repo.StudentRepo;
import com.shehab.udms.types.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AttendanceWsService {

    @Autowired private AttendanceRepo attendanceRepo;
    @Autowired private StudentRepo studentRepo;
    @Autowired private CourseRepo courseRepo;
    @Autowired private SemesterRepo semesterRepo;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    public String markAttendanceByQR(AttendanceRequestDTO request, String username) {

        // ১. স্টুডেন্ট খুঁজে বের করা (Security Context থেকে আসা ইউজারনেম দিয়ে)
        Student student = studentRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found for user: " + username));

        // ২. কোর্স এবং সেমিস্টার খুঁজে বের করা (DTO থেকে ID নিয়ে)
        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        Semester semester = semesterRepo.findById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found with id: " + request.getSemesterId()));

        LocalDate today = LocalDate.now();

        // ৩. অলরেডি এটেনডেন্স আছে কি না চেক করা (UPSERT লজিক)
        Attendance attendance = attendanceRepo
                .findByStudentAndCourseAndSemesterAndDate(student, course, semester, today)
                .orElse(new Attendance());

        // যদি অলরেডি প্রেজেন্ট (Status.P) থাকে
        if (attendance.getId() != null && attendance.getStatus() == Status.P) {
            return "Attendance already marked for today!";
        }

        // ৪. ডাটা সেট করা
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setSemester(semester);
        attendance.setDate(today);
        attendance.setStatus(Status.P); // P = Present
        attendance.setMarkedAt(LocalDateTime.now());
        attendance.setUpdatedBy(username);

        // ৫. ডাটাবেসে সেভ করা
        attendanceRepo.save(attendance);

        // ৬. WebSocket-এর মাধ্যমে টিচারকে জানানো
        String topic = "/topic/attendance/" + course.getId();

        Map<String, Object> payload = new HashMap<>();
        payload.put("studentId", student.getStudentID());
        payload.put("studentName", student.getName());
        payload.put("department", student.getDepartment());
        payload.put("status", Status.P.name());
        payload.put("time", LocalDateTime.now().toString());

        // মেসেজ ব্রডকাস্ট করা
        messagingTemplate.convertAndSend(topic, Optional.of(payload));

        return "Attendance successfully marked for: " + student.getName();
    }
}