package com.student.management.system.service;

import com.student.management.system.model.dto.CourseDto;

import java.util.List;

public interface CourseService {
    List<CourseDto> getCourses();
    byte[] generateCourseSchedulePdf();
}
