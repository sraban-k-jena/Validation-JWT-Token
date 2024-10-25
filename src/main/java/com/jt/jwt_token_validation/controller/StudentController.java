package com.jt.jwt_token_validation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.jt.jwt_token_validation.model.Student;
import com.jt.jwt_token_validation.service.JwtService;
import com.jt.jwt_token_validation.service.StudentService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StudentController {
    @Autowired
    public JwtService jwtService;

    @Autowired
    public StudentService service;

    @GetMapping("/")
    public ResponseEntity<?> getAllDetails(HttpServletRequest request) {
        String id = request.getSession().getId();
        return new ResponseEntity<>("Successfully :" + id, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Student> addStduent(@RequestBody Student student) {
        Student student2 = service.addStudent(student);
        return new ResponseEntity<>(student2, HttpStatus.CREATED);
    }

    @PostMapping("/log")
    public ResponseEntity<?> login(@RequestBody Student student) {
        String token = service.login(student);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/claims")
    public ResponseEntity<Claims> getClaims(HttpServletRequest request) {
        String authHead = request.getHeader("Authorization");
        if (authHead != null && authHead.startsWith("Bearer ")) {
            String token = authHead.substring(7);
            Claims claims = jwtService.extractAllClaims(token);
            return new ResponseEntity<>(claims, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
