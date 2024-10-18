package com.student.management.system.service;


import com.student.management.system.model.dto.LoginRequestDto;
import com.student.management.system.model.dto.RegisterRequestDto;
import com.student.management.system.model.entity.Student;


public interface AuthenticationService {

    void register(RegisterRequestDto input);

    String authenticate(LoginRequestDto input);

}
