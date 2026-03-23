package com.shehab.udms.service;

import com.shehab.udms.DTO.CourseDTO;
import com.shehab.udms.DTO.TeacherSimpleDTO;
import com.shehab.udms.model.Course;
import com.shehab.udms.model.Teacher;
import com.shehab.udms.repo.CourseRepo;
import com.shehab.udms.repo.TeacherRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    // dto
    private static @NonNull CourseDTO getDto(Course course) {

        TeacherSimpleDTO teacher = course.getTeacher() == null?
                null:
                new TeacherSimpleDTO(
                        course.getTeacher().getId(),
                        course.getTeacher().getUsername(),
                        course.getTeacher().getName(),
                        course.getTeacher().getEmail(),
                        course.getTeacher().getId(),
                        course.getTeacher().getUser().getRole()
                );

        return new CourseDTO(
                course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                course.getCourseSemester(),
                course.getCourseCredit(),
                course.getCourseDepartment(),
                teacher
        );
    }

    // get all
    public List<CourseDTO> getAllCourses(){
        return courseRepo.findAll().stream()
                .map(
                        course -> getDto(course)
                ).toList();
    }


    // get course
    public CourseDTO getCourse(int id){
        Course course = courseRepo.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));

        courseRepo.save(course);

        return getDto(course);
    }

    // post
    public CourseDTO postCourse(Course newCourse){
      /*
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();


        if (loggedUsername == null) {
            throw new RuntimeException("null update");
        }
        */
        // teacher may be null
        Teacher teacher = null;
        if (newCourse.getTeacher() != null && newCourse.getTeacher().getId() != null) {
            teacher = teacherRepo.findById(newCourse.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        }

        newCourse.setTeacher(teacher); // can be null

        Course course = courseRepo.save(newCourse);

        return getDto(course);
    }

    // update
    public CourseDTO updateCourse(int id, Course updatedCourse){

        // fetch course first
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // optional teacher
        Teacher teacher = null;
        if (updatedCourse.getTeacher() != null && updatedCourse.getTeacher().getId() != null) {
            teacher = teacherRepo.findById(updatedCourse.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        }

        // update fields
        course.setCourseCode(updatedCourse.getCourseCode());
        course.setCourseName(updatedCourse.getCourseName());
        course.setCourseSemester(updatedCourse.getCourseSemester());
        course.setCourseCredit(updatedCourse.getCourseCredit());
        course.setCourseDepartment(updatedCourse.getCourseDepartment());
        course.setTeacher(teacher);

        course = courseRepo.save(course); // persist changes

        return getDto(course);
    }

    //delete
    public void deleteCourse(int id){
        courseRepo.deleteById(id);
    }
}
