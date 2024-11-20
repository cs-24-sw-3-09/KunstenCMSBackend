package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import org.modelmapper.internal.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.StudentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TeacherEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.StudentRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.TeacherRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    public TeacherEntity saveTeacher(TeacherEntity teacher) {
        return teacherRepository.save(teacher);
    }

    public TeacherEntity addStudentsToTeacher(Long teacherId, Set<Long> studentIds) {
        TeacherEntity teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Set<StudentEntity> students = new HashSet<>(studentRepository.findAllById(studentIds));
        teacher.getStudents().addAll(students);

        return teacherRepository.save(teacher);
    }

    public TeacherEntity getTeacherWithStudents(Long teacherId) {
        java.util.Optional<TeacherEntity> teacher = teacherRepository.findById(teacherId);
        return teacher.orElseThrow(() -> new RuntimeException("Teacher not found"));
    }
}
