package com.shehab.udms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

//        http.csrf(csrf -> csrf.disable()); // csrf disabled but still not secure
//        http.authorizeHttpRequests(request -> request.anyRequest().authenticated()); // access to localhost denies even if we sent username pass
//        http.formLogin(Customizer.withDefaults()); // form login implemented and now works but mot workin on postman
//        http.httpBasic(Customizer.withDefaults());  // for postman
//        logout will not work
//        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // browser login will not work, every time i needs to pass credential
//        every time i send a request, i will get new session id
//        if i wanna use on browser i have to disable form login, after login refresh everytime get new session id

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
