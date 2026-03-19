package com.shehab.udms.service;

import com.shehab.udms.DTO.StudentDTO;
import com.shehab.udms.model.Student;
import com.shehab.udms.repo.StudentRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    // dto to return
    private static @NonNull StudentDTO getDto(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getUsername(),
                student.getStudentID(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getAddress(),
                student.getDepartment(),
                student.getCurrSemester(),
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

    // update current semester (admin only)
    public StudentDTO updateStudentsCurrSem(String username, Student updatedStudent) {

        Student student = studentRepo.findByUserUsername(username).orElseThrow(() -> new RuntimeException("Student not found"));

        student.setCurrSemester(updatedStudent.getCurrSemester());
        studentRepo.save(student);

        return getDto(student);
    }
}
