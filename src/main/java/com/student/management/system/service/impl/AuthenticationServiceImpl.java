package com.student.management.system.service.impl;


import com.student.management.system.model.dto.LoginRequestDto;
import com.student.management.system.model.dto.RegisterRequestDto;
import com.student.management.system.model.dto.UserDetailDto;
import com.student.management.system.model.entity.Student;
import com.student.management.system.model.entity.User;
import com.student.management.system.repository.StudentRepository;
import com.student.management.system.repository.UserRepository;
import com.student.management.system.service.AuthenticationService;
import com.student.management.system.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    @Override
    public void register(RegisterRequestDto registerRequestDto) {
        User user = User.builder()
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .fullName(registerRequestDto.getFullName())
                .build();
        userRepository.save(user);

        // Create and link the Student entity to the User
        Student student = Student.builder()
                .user(user)
                .build();

        // Save the Student entity (will also save the User due to cascade)
        studentRepository.save(student);

    }

    @Override
    public String authenticate(LoginRequestDto loginRequestDto) {
        Authentication authenticate =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDto.getEmail(), loginRequestDto.getPassword()));

        UserDetailDto user = (UserDetailDto) authenticate.getPrincipal();

        return jwtService.generateToken(user);

    }


}
