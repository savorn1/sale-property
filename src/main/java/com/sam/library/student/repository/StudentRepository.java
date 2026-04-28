package com.sam.library.student.repository;

import com.sam.library.student.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository  extends JpaRepository<Student, Long> {

    @Query(value = "SELECT s.* FROM student s WHERE LOWER(s.student_id) = LOWER(:studentId)", nativeQuery = true)
    Student getStudentByStudentId(@Param("studentId") String studentId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE student SET password = :password WHERE id = :id", nativeQuery = true)
    int createStudentUser(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Transactional
    @Query(value = "UPDATE student SET " +
            "name = :name, " +
            "email = :email, " +
            "photo = CASE WHEN :photo IS NOT NULL THEN :photo ELSE NULL END, " +
            "phone_number = :phoneNumber " +
            "WHERE id = :id", nativeQuery = true)
    int updateStudent(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("email") String email,
            @Param("photo") String photo,
            @Param("phoneNumber") String phoneNumber);

}
