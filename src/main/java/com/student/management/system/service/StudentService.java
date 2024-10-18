package com.student.management.system.service;

import com.student.management.system.model.dto.CourseRegistrationRequestDto;

public interface StudentService {

    void registerToCourse(String token, CourseRegistrationRequestDto courseRegistrationRequestDto);

    void cancelCourse(String token, CourseRegistrationRequestDto courseRegistrationRequestDto);
}
