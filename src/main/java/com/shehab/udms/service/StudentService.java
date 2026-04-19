package com.shehab.udms.service;

import com.shehab.udms.DTO.AttendanceTinyDto;
import com.shehab.udms.DTO.SemesterSimpleDTO;
import com.shehab.udms.DTO.StudentDTO;
import com.shehab.udms.DTO.StudentTinyDto;
import com.shehab.udms.model.Attendance;
import com.shehab.udms.model.Semester;
import com.shehab.udms.model.Student;
import com.shehab.udms.repo.AttendanceRepo;
import com.shehab.udms.repo.SemesterRepo;
import com.shehab.udms.repo.StudentRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private SemesterRepo semesterRepo;

    @Autowired
    private AttendanceRepo attendanceRepo;

    // dto to return
    private static @NonNull StudentDTO getDto(Student student) {

        SemesterSimpleDTO semester = student.getCurrSemester() == null?
                null:
                new SemesterSimpleDTO(
                        student.getCurrSemester().getId(),
                        student.getCurrSemester().getSemesterNo(),
                        student.getCurrSemester().getBatch(),
                        student.getCurrSemester().getSession()
                );

        return new StudentDTO(
                student.getId(),
                student.getUsername(),
                student.getStudentID(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getAddress(),
                student.getDepartment(),
                semester,
                student.getGender(),
                student.getUser().getId(),
                student.getUser().getRole()
        );
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepo.findAll().stream()
                .map(student -> getDto(student))
                .toList();
    }

    public StudentDTO getStudent(String username){
        Student student = studentRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return getStudentDTO(student);
    }

    private static @NonNull StudentDTO getStudentDTO(Student student) {
        return getDto(student);
    }

    // update
    @Transactional
    public StudentDTO updateStudentDTO(String username, Student updatedStudent){
        Student student = studentRepo.findByUserUsername(username).orElseThrow(() -> new RuntimeException("Student not found"));

        // get the logged-in username from JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        System.out.println(loggedUsername + "##########");

        if (!loggedUsername.equals(username)) {
            throw new RuntimeException("You cannot update another student's profile");
        }

        student.setStudentID(updatedStudent.getStudentID());
        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        student.setPhone(updatedStudent.getPhone());
        student.setAddress(updatedStudent.getAddress());
        student.setDepartment(updatedStudent.getDepartment());
        student.setGender(updatedStudent.getGender());

        studentRepo.save(student);

        return getDto(student);


    }

    @Transactional
    public StudentDTO updateStudentsCurrSem(String username, Student updatedStudent) {
        // find student
        Student student = studentRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // update semester only if provided
        if (updatedStudent.getCurrSemester() != null) {
            Semester semester = semesterRepo.findById(updatedStudent.getCurrSemester().getId())
                    .orElseThrow(() -> new RuntimeException("Semester not found - student service"));
            student.setCurrSemester(semester);
        }

        studentRepo.save(student);
        return getDto(student);
    }

    public List<StudentDTO> getStudentsBySemester(Long semesterId) {
        List<Student> students = studentRepo.findByCurrSemesterId(semesterId);

        return students.stream()
                .map(StudentService::getDto) // বিদ্যমান স্ট্যাটিক মেথড ব্যবহার করা হয়েছে
                .toList();
    }

    public List<StudentTinyDto> getStudentByCourseId(Long courseId) {

        List<Attendance> attendances = attendanceRepo.findByCourseId(courseId);

        // group by student
        Map<Student, List<Attendance>> grouped = attendances.stream()
                .collect(Collectors.groupingBy(Attendance::getStudent));

        // convert to DTO
        return grouped.entrySet().stream()
                .map(entry -> {

                    Student student = entry.getKey();
                    List<Attendance> studentAttendances = entry.getValue();

                    /**
                     * hope everything will be fine
                     */
                    //
                    SemesterSimpleDTO currSemester = student.getCurrSemester() == null ? null :
                            new SemesterSimpleDTO(
                                    student.getCurrSemester().getId(),
                                    student.getCurrSemester().getSemesterNo(),
                                    student.getCurrSemester().getBatch(),
                                    student.getCurrSemester().getSession()
                            );
                    //

                    List<AttendanceTinyDto> attendanceList = studentAttendances.stream()
                            .map(att -> new AttendanceTinyDto(
                                    att.getCourse().getCourseName(),
                                    att.getCourse().getCourseCode(),
                                    att.getStatus(),
                                    currSemester

                            ))
                            .toList();

                    return new StudentTinyDto(
                            student.getName(),
                            student.getStudentID(),
                            attendanceList,
                            (long) attendanceList.size()
                    );
                })
                .toList();
    }
}
