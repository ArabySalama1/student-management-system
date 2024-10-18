package com.student.management.system.repository;

import com.student.management.system.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("Select s from Student s inner join User u on s.user.id=u.id where u.email=:email")
    Optional<Student> findByEmail(@Param("email") String email);
}
