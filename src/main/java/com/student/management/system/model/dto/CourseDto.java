package com.student.management.system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Long id;
    private String courseName;
    private String description;
    private Date startDate;
    private Date endDate;
}
