package com.shehab.udms.service;


import com.shehab.udms.DTO.CourseDTO;
import com.shehab.udms.DTO.StudentEnrolledDTO;
import com.shehab.udms.model.Course;
import com.shehab.udms.model.Semester;
import com.shehab.udms.model.Student;
import com.shehab.udms.model.StudentEnrolled;
import com.shehab.udms.repo.CourseRepo;
import com.shehab.udms.repo.SemesterRepo;
import com.shehab.udms.repo.StudentEnrolledRepo;
import com.shehab.udms.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentEnrolledService {

    @Autowired
    private StudentEnrolledRepo studentEnrolledRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private SemesterRepo semesterRepo;

    // DTO function
    private StudentEnrolledDTO convertToDTO(StudentEnrolled enroll){
        List<String> codes = enroll.getEnrolledCourses().stream()
                .map(Course::getCourseCode)
                .toList();

        List<String> names = enroll.getEnrolledCourses().stream()
                .map(Course::getCourseName)
                .toList();

        return new StudentEnrolledDTO(
                enroll.getStudent().getStudentID(),
                enroll.getStudent().getName(),
                enroll.getSemester().getId(), // sem_id
                enroll.getSemester().getSemesterNo(),
                enroll.getSemester().getBatch(),
                enroll.getSemester().getSession(),
                codes,
                names
        );
    }


    // get all
    public List<StudentEnrolledDTO> getAllStudentEnrolls() {
        return studentEnrolledRepo.findAll().stream()
                .map(this::convertToDTO
                ).toList();
    }



    // get studentEnrolled
    public StudentEnrolledDTO getStudentEnrolled(int id){

        StudentEnrolled studentEnrolled = studentEnrolledRepo.findById(id).orElseThrow(() -> new RuntimeException("studentEnrolled not found"));
        studentEnrolledRepo.save(studentEnrolled);

        return this.convertToDTO(studentEnrolled);
    }



    // post
    public StudentEnrolledDTO postStudentEnrolled(StudentEnrolledDTO enrolledDTO){

        StudentEnrolled enroll = new StudentEnrolled();

        Student student = studentRepo.findByStudentID(enrolledDTO.studentId()).orElseThrow(() -> new RuntimeException("Student not found"));
        Semester semester = semesterRepo.findById(enrolledDTO.sem_id()).orElseThrow(() -> new RuntimeException("Semester not found"));

        List<Course> courses = enrolledDTO.courseCodes().stream()
                .map(code -> courseRepo.findByCourseCode(code))
                .toList();



        enroll.setStudent(student);
        enroll.setSemester(semester);
        enroll.setEnrolledCourses(courses);

        StudentEnrolled updated = studentEnrolledRepo.save(enroll);

        return this.convertToDTO(updated);
    }



    // update
    public StudentEnrolledDTO updateStudentEnrolled(int id, StudentEnrolledDTO enrolledDTO){

        StudentEnrolled enroll = studentEnrolledRepo.findById(id).orElseThrow(() -> new RuntimeException("studentEnrolled not found"));

        Student student = studentRepo.findByStudentID(enrolledDTO.studentId()).orElseThrow(() -> new RuntimeException("Student not found"));
        Semester semester = semesterRepo.findById(enrolledDTO.sem_id()).orElseThrow(() -> new RuntimeException("Semester not found"));

        List<Course> courses = enrolledDTO.courseCodes().stream()
                .map(code -> courseRepo.findByCourseCode(code))
                .toList();


        enroll.setStudent(student);
        enroll.setStudent(student);
        enroll.setEnrolledCourses(courses);

        StudentEnrolled updated = studentEnrolledRepo.save(enroll);

        return this.convertToDTO(updated);
    }



    //delete
    public void deleteStudentEnrolled(int id) {
        studentEnrolledRepo.deleteById(id);
    }

}

/*

{
    "studentId": "M123",
    "sem_id": 1,
    "courseCodes": ["CS101", "PHY102"],
    "name": "",
    "semesterNo": 0,
    "batch": 0,
    "session": "",
    "courseName": []
}


{
    "studentId": "M123",
    "name": null,
    "sem_id": 1,
    "semesterNo": 2,
    "batch": 13,
    "session": "2022-23",
    "courseCodes": [
        "CS101",
        "PHY102"
    ],
    "courseName": [
        "Introduction to Computer Science",
        "Physics II: Electromagnetism"
    ]
}
 */