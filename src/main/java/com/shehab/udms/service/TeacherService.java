package com.shehab.udms.service;


import com.shehab.udms.DTO.TeacherDTO;
import com.shehab.udms.model.Teacher;
import com.shehab.udms.repo.TeacherRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepo teacherRepo;

    // dto
    private static @NonNull TeacherDTO getDto(Teacher teacher) {
        return new TeacherDTO(
                teacher.getId(),
                teacher.getUsername(),
                teacher.getName(),
                teacher.getEmail(),
                teacher.getPhone(),
                teacher.getAddress(),
                teacher.getGender(),
                teacher.getUser().getId(),
                teacher.getUser().getRole()
        );
    }

    // get teachers
    public List<TeacherDTO> getAllTeacherDTOs() {
        return teacherRepo.findAll().stream()
                .map(teacher -> getDto(teacher))
                .toList();
    }

    // get teacher
    public TeacherDTO getTeacher(String username){

        Teacher teacher = teacherRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return getDto(teacher);
    }


    // put teacher (update)
    public TeacherDTO updateTeacher(String username, Teacher updatedTeacher){
        Teacher teacher = teacherRepo.findByUserUsername(username).orElseThrow(() -> new RuntimeException("Teacher not found"));

        // get the logged-in username from JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        System.out.println(loggedUsername + "##########");

        if (!loggedUsername.equals(username)) {
            throw new RuntimeException("You cannot update another teacher's profile");
        }

        teacher.setName(updatedTeacher.getName());
        teacher.setEmail(updatedTeacher.getEmail());
        teacher.setPhone(updatedTeacher.getPhone());
        teacher.setAddress(updatedTeacher.getAddress());
        teacher.setGender(updatedTeacher.getGender());

        teacherRepo.save(teacher);

        return getDto(teacher);
    }
}
