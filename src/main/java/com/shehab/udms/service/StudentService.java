package com.shehab.udms.service;

import com.shehab.udms.DTO.SemesterDTO;
import com.shehab.udms.DTO.SemesterSimpleDTO;
import com.shehab.udms.DTO.StudentDTO;
import com.shehab.udms.model.Semester;
import com.shehab.udms.model.Student;
import com.shehab.udms.repo.SemesterRepo;
import com.shehab.udms.repo.StudentRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private SemesterRepo semesterRepo;

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

}
