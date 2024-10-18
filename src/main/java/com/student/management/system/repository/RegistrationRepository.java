package com.student.management.system.repository;

import com.student.management.system.model.entity.Course;
import com.student.management.system.model.entity.Student;
import com.student.management.system.model.entity.StudentCourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<StudentCourseRegistration, Long> {
    Optional<StudentCourseRegistration> findByStudentAndCourse(Student student, Course course);
}