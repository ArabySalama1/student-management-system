package com.student.management.system.service.impl;

import com.student.management.system.exception.RegistrationAlreadyExistsException;
import com.student.management.system.exception.ResourceNotFoundException;
import com.student.management.system.model.dto.CourseRegistrationRequestDto;
import com.student.management.system.model.entity.Course;
import com.student.management.system.model.entity.Student;
import com.student.management.system.model.entity.StudentCourseRegistration;
import com.student.management.system.model.enums.CourseStatus;
import com.student.management.system.repository.CourseRepository;
import com.student.management.system.repository.RegistrationRepository;
import com.student.management.system.repository.StudentRepository;
import com.student.management.system.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;


    @Override
    public void registerToCourse(String email, CourseRegistrationRequestDto courseRegistrationRequestDto) {
        Student student = getStudentByEmail(email);
        Course course = fetchCourseById(courseRegistrationRequestDto.getCourseId());

        // Check if the student is already registered for the course
        if (isStudentAlreadyRegistered(student, course)) {
            log.warn("Student {} is already registered for course {}", email, course.getId());
            throw new RegistrationAlreadyExistsException("Student is already registered for this course");
        }

        // Create a new registration and save it
        saveStudentCourseRegistration(student, course);
    }


    @Override
    public void cancelCourse(String email, CourseRegistrationRequestDto courseRegistrationRequestDto) {

        Student student = getStudentByEmail(email);
        Course course = fetchCourseById(courseRegistrationRequestDto.getCourseId());

        StudentCourseRegistration registration = registrationRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> {
                    log.warn("No registration found for student {} in course {}", email, course.getId());
                    return new RegistrationAlreadyExistsException("No registration found for this course");
                });

        registration.setStatus(CourseStatus.CANCELLED);
        registrationRepository.save(registration);
        log.info("Student {} successfully cancelled registration for course {}", email, course.getId());
    }


    private Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Student with email {} not found", email);
                    return new ResourceNotFoundException("Student not found");
                });
    }


    private Course fetchCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course with ID {} not found", courseId);
                    return new ResourceNotFoundException("Course not found");
                });
    }


    private boolean isStudentAlreadyRegistered(Student student, Course course) {
        return registrationRepository.findByStudentAndCourse(student, course).isPresent();
    }


    private void saveStudentCourseRegistration(Student student, Course course) {
        StudentCourseRegistration studentCourseRegistration = new StudentCourseRegistration();
        studentCourseRegistration.setStudent(student);
        studentCourseRegistration.setCourse(course);
        studentCourseRegistration.setStatus(CourseStatus.REGISTERED);
        registrationRepository.save(studentCourseRegistration);
    }
}
