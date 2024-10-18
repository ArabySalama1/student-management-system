package com.student.management.system.model.entity;

import com.student.management.system.model.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "student_course_registration")
public class StudentCourseRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Date registrationDate = new Date();

    @Enumerated(EnumType.STRING)  // Store enum as string in the database
    @Column(name = "status", nullable = false)
    private CourseStatus status;
}
