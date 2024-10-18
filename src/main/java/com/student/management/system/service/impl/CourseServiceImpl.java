package com.student.management.system.service.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.student.management.system.model.dto.CourseDto;
import com.student.management.system.model.entity.Course;
import com.student.management.system.repository.CourseRepository;
import com.student.management.system.service.CourseService;
import com.student.management.system.service.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Cacheable(value = "coursesCache")
    public List<CourseDto> getCourses() {

        List<Course> courses = courseRepository.findAll();
        return courseMapper.coursesToCourseDos(courses);
    }

    @Override
    public byte[] generateCourseSchedulePdf() {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Title
            document.add(new Paragraph("Course Schedule")
                    .setFontSize(20));

            // Table of courses
            Table table = new Table(3); // 3 columns: ID, Course Name, Description
            table.addHeaderCell("Course ID");
            table.addHeaderCell("Course Name");
            table.addHeaderCell("Description");

            // Fetch courses from the repository
            List<Course> courses = courseRepository.findAll();
            for (Course course : courses) {
                table.addCell(String.valueOf(course.getId()));
                table.addCell(course.getCourseName());
                table.addCell(course.getDescription());
            }
            document.add(table);
            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
