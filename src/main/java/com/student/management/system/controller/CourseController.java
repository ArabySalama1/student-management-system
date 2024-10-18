package com.student.management.system.controller;

import com.student.management.system.model.dto.CourseDto;
import com.student.management.system.model.dto.CourseRegistrationRequestDto;
import com.student.management.system.service.CourseService;
import com.student.management.system.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> viewAllCourses() {
        List<CourseDto> courses = courseService.getCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerToCourse( Authentication authentication, @RequestBody CourseRegistrationRequestDto courseRegistrationRequestDto) {

        try {
            studentService.registerToCourse(authentication.getName(), courseRegistrationRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");

        } catch (RuntimeException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelCourse(Authentication authentication, @RequestBody CourseRegistrationRequestDto courseRegistrationRequestDto) {

        try {
            studentService.cancelCourse(authentication.getName(), courseRegistrationRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED).body("Cancelled successful");

        } catch (RuntimeException e) {
            log.error("Cancelling failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/schedule/pdf")
    public ResponseEntity<byte[]> getCourseScheduleAsPdf() {

        byte[] pdfData = courseService.generateCourseSchedulePdf();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"schedule.pdf\"")
                .body(pdfData);
    }
}
