package com.jt.jwt_token_validation.service;

import com.jt.jwt_token_validation.model.Student;

public interface StudentService {

    Student addStudent(Student student);

    String login(Student student);
}
