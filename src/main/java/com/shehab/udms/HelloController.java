package com.shehab.udms;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String sayHello(HttpServletRequest httpServletRequest){
        return "Hello world " + httpServletRequest.getSession().getId();
    }
}
