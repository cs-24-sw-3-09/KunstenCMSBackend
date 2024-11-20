package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.StudentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TeacherEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public StudentEntity saveStudent(StudentEntity student) {
        return studentRepository.save(student);
    }

    public StudentEntity getTeacherWithStudents(Long studentId) {
        java.util.Optional<StudentEntity> student = studentRepository.findById(studentId);
        return student.orElseThrow(() -> new RuntimeException("Teacher not found"));
    }
}
