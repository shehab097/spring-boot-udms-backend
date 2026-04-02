package com.shehab.udms.service;

import com.shehab.udms.DTO.AttendanceDTO;
import com.shehab.udms.DTO.CourseSimpleDTO;
import com.shehab.udms.DTO.SemesterSimpleDTO;
import com.shehab.udms.DTO.StudentSimpleDTO;
import com.shehab.udms.model.Attendance;
import com.shehab.udms.model.Course;
import com.shehab.udms.model.Semester;
import com.shehab.udms.model.Student;
import com.shehab.udms.repo.AttendanceRepo;
import com.shehab.udms.repo.CourseRepo;
import com.shehab.udms.repo.SemesterRepo;
import com.shehab.udms.repo.StudentRepo;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private SemesterRepo semesterRepo;

    // method for dto
    private static @NonNull AttendanceDTO getDto(Attendance attendance) {

        StudentSimpleDTO student = attendance.getStudent() == null ?
                null :
                new StudentSimpleDTO(
                        attendance.getStudent().getId(),
                        attendance.getStudent().getUsername(),
                        attendance.getStudent().getStudentID(),
                        attendance.getStudent().getName(),
                        attendance.getStudent().getDepartment()
                );

        CourseSimpleDTO course = attendance.getCourse() == null ?
                null :
                new CourseSimpleDTO(
                        attendance.getCourse().getId(),
                        attendance.getCourse().getCourseCode(),
                        attendance.getCourse().getCourseName(),
                        attendance.getCourse().getCourseSemester(),
                        attendance.getCourse().getCourseCredit(),
                        attendance.getCourse().getCourseDepartment()
                );

        SemesterSimpleDTO semester = attendance.getSemester() == null ?
                null :
                new SemesterSimpleDTO(
                        attendance.getSemester().getId(),
                        attendance.getSemester().getSemesterNo(),
                        attendance.getSemester().getBatch(),
                        attendance.getSemester().getSession()
                );


        return new AttendanceDTO(
                attendance.getId(),
                student,
                course,
                semester,
                attendance.getDate(),
                attendance.getStatus(),
                attendance.getMarkedAt(),
                attendance.getUpdatedBy()
        );
    }

    // get all
    public List<AttendanceDTO> getAttendances() {
        return attendanceRepo.findAll().stream().map(
                AttendanceService::getDto
        ).toList();
    }

    // get attendance
    public AttendanceDTO getAttendance(Long id) {
        Attendance attendance = attendanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        attendanceRepo.save(attendance);
        return getDto(attendance);
    }

    // delete
    public void deleteAttendance(Long id) {
        Attendance attendance = attendanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));
        attendanceRepo.deleteById(id);
    }

    // post
    /**
    public AttendanceDTO postAttendance(AttendanceDTO attendanceDto) {
        System.out.println("POST");

        Student student = studentRepo.findById(attendanceDto.student().id())
                .orElseThrow(() -> new RuntimeException("Student not found by id"));

        Course course = courseRepo.findById(attendanceDto.course().id())
                .orElseThrow(() -> new RuntimeException("course not found by id"));

        Semester semester = semesterRepo.findById(attendanceDto.semester().id())
                .orElseThrow(() -> new RuntimeException("semester not found by id"));


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        if (loggedUsername == null) {
            System.out.println("You cannot update another student's profile");
        }

        System.out.println(course);
        System.out.println(student);
        System.out.println(semester);

        //
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setSemester(semester);

        attendance.setDate(attendanceDto.date()); // date received from frontend
        attendance.setStatus(attendanceDto.status());
        attendance.setMarkedAt(LocalDateTime.now());
        attendance.setUpdatedBy(loggedUsername);

        Attendance saved = attendanceRepo.save(attendance);

        return getDto(saved);
    }
     **/

    public AttendanceDTO postAttendance(AttendanceDTO attendanceDto) {
        // 1. Get logged in user for audit trailing
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = (auth != null) ? auth.getName() : "no data";

        // 2. Fetch required entities (using Record accessor syntax)
        Student student = studentRepo.findById(attendanceDto.student().id())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + attendanceDto.student().id()));

        Course course = courseRepo.findById(attendanceDto.course().id())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + attendanceDto.course().id()));

        Semester semester = semesterRepo.findById(attendanceDto.semester().id())
                .orElseThrow(() -> new RuntimeException("Semester not found with id: " + attendanceDto.semester().id()));

        // 3. UPSERT LOGIC: Check if this record already exists in the database
        // This uses the unique combination: Student + Course + Semester + Date
        Attendance attendance = attendanceRepo
                .findByStudentAndCourseAndSemesterAndDate(
                        student,
                        course,
                        semester,
                        attendanceDto.date()
                )
                .orElse(new Attendance()); // Create new if not found, otherwise update existing

        // 4. Set/Update values
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setSemester(semester);
        attendance.setDate(attendanceDto.date());
        attendance.setStatus(attendanceDto.status());

        // Metadata
        attendance.setMarkedAt(LocalDateTime.now());
        attendance.setUpdatedBy(loggedUsername);

        // 5. Save (JPA automatically knows whether to INSERT or UPDATE based on ID presence)
        Attendance saved = attendanceRepo.save(attendance);

        System.out.println(attendance.getId() == null ? "Inserted new attendance" : "Updated existing attendance");

        return getDto(saved);
    }


    //update
    public AttendanceDTO updateAttendance(AttendanceDTO attendanceDto, Long id) {
        System.out.println("UPDATE");

        //  find existing attendance
        Attendance attendance = attendanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        //  get logged user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        if (loggedUsername == null) {
            System.out.println("Unauthorized user");
        }

        // OPTIONAL: update student (if provided)
        if (attendanceDto.student() != null) {
            Student student = studentRepo.findById(attendanceDto.student().id())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            attendance.setStudent(student);
            System.out.println(student);

        }

        // OPTIONAL: update course
        if (attendanceDto.course() != null) {
            Course course = courseRepo.findById(attendanceDto.course().id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            attendance.setCourse(course);
            System.out.println(course);
        }

        // OPTIONAL: update semester
        if (attendanceDto.semester() != null) {
            Semester semester = semesterRepo.findById(attendanceDto.semester().id())
                    .orElseThrow(() -> new RuntimeException("Semester not found"));
            attendance.setSemester(semester);
            System.out.println(semester);
        }

        // update main fields
        if (attendanceDto.status() != null) {
            attendance.setStatus(attendanceDto.status());
        }

        // usually date shouldn't change, but if you want:
        if (attendanceDto.date() != null) {
            attendance.setDate(attendanceDto.date());
        }



        // 🔄 update metadata
        attendance.setMarkedAt(LocalDateTime.now());
        attendance.setUpdatedBy(loggedUsername);

        // 💾 save
        Attendance updated = attendanceRepo.save(attendance);

        return getDto(updated);
    }

    //
    public List<AttendanceDTO> postAllAttendances(List<AttendanceDTO> attendanceDtos) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = (auth != null) ? auth.getName() : "System";

        List<Attendance> entities = attendanceDtos.stream().map(dto -> {
            // 1. Fetch dependencies
            Student student = studentRepo.findById(dto.student().id())
                    .orElseThrow(() -> new RuntimeException("Student not found: " + dto.student().id()));
            Course course = courseRepo.findById(dto.course().id())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + dto.course().id()));
            Semester semester = semesterRepo.findById(dto.semester().id())
                    .orElseThrow(() -> new RuntimeException("Semester not found: " + dto.semester().id()));

            // 2. Check if attendance already exists for this unique combination
            // This prevents DataIntegrityViolationException
            Attendance attendance = attendanceRepo
                    .findByStudentAndCourseAndSemesterAndDate(student, course, semester, dto.date())
                    .orElse(new Attendance());

            // 3. Set/Update values
            attendance.setStudent(student);
            attendance.setCourse(course);
            attendance.setSemester(semester);
            attendance.setDate(dto.date()); // Use date from DTO
            attendance.setStatus(dto.status());
            attendance.setMarkedAt(LocalDateTime.now());
            attendance.setUpdatedBy(loggedUsername);

            return attendance;
        }).toList();

        // 4. Batch Save
        List<Attendance> savedEntities = attendanceRepo.saveAll(entities);

        return savedEntities.stream()
                .map(AttendanceService::getDto)
                .toList();
    }

    public List<AttendanceDTO> getTodaysAttendanceByCourse(Long courseId, LocalDate today) {
        // ১. ডাটাবেস থেকে আজকের ওই কোর্সের সব রেকর্ড নিয়ে আসা
        List<Attendance> attendances = attendanceRepo.findByCourseIdAndDate(courseId, today);

        // ২. রেকর্ডগুলোকে DTO তে কনভার্ট করে রিটার্ন করা
        return attendances.stream()
                .map(AttendanceService::getDto) // আপনার অলরেডি তৈরি করা static getDto মেথড
                .toList();
    }
}