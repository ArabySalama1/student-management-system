package com.student.management.system.service.mapper;

import com.student.management.system.model.dto.CourseDto;
import com.student.management.system.model.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseDto courseToCourseDto(Course course);

    List<CourseDto> coursesToCourseDos(List<Course> courses);
}
