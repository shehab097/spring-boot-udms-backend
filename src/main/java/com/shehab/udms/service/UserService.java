package com.shehab.udms.service;

import com.shehab.udms.DTO.UserDTO;
import com.shehab.udms.model.Admin;
import com.shehab.udms.model.Student;
import com.shehab.udms.model.Teacher;
import com.shehab.udms.model.Users;
import com.shehab.udms.repo.AdminRepo;
import com.shehab.udms.repo.StudentRepo;
import com.shehab.udms.repo.TeacherRepo;
import com.shehab.udms.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtService;



    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public Users register(Users user){

        user.setPassword(encoder.encode(user.getPassword()));

        Users savedUser = userRepo.save(user);

        // create empty profile depending on role
        switch(savedUser.getRole()){

            case STUDENT:
                Student student = new Student();
                student.setUsername(user.getUsername());
                student.setUser(savedUser);
                studentRepo.save(student);
                break;

            case TEACHER:
                Teacher teacher = new Teacher();
                teacher.setUsername(user.getUsername());
                teacher.setUser(savedUser);
                teacherRepo.save(teacher);
                break;

            case ADMIN:
                Admin admin = new Admin();
                admin.setUsername(user.getUsername());
                admin.setUser(savedUser);
                adminRepo.save(admin);
                break;
        }

        return savedUser;
    }

    public UserDTO verify(Users user) throws Exception {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        if(authentication.isAuthenticated()){
            String token = jwtService.generateToken(user.getUsername());
            Users dbUser = userRepo.findUserByUsername(user.getUsername()); // from db

            return new UserDTO(
                    user.getUsername(),
                    dbUser.getRole(),
                    token
            );
        }
        throw new Exception("Invalid username or password");
    }
}

/*
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10); // we can pass straight

    public Users register(Users user){
        // before sending data to the database, we can encrypt password
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(Users user) {
        // verify
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());

        return "failed";
    }
}

*/
