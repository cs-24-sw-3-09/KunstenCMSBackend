package com.github.cs_24_sw_3_09.CMS.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.model.entities.StudentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TeacherEntity;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.StudentService;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.TeacherService;

@RestController
@RequestMapping("/api")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    // POST for adding a new teacher
    @PostMapping("/teachers")
    public TeacherEntity createTeacher(@RequestBody TeacherEntity teacher) {
        return teacherService.saveTeacher(teacher);
    }

    // POST for adding a new student
    @PostMapping("/students")
    public StudentEntity createStudent(@RequestBody StudentEntity student) {
        return studentService.saveStudent(student);
    }

    // POST to associate students with a teacher
    @PostMapping("/teachers/{teacherId}/students")
    public TeacherEntity addStudentsToTeacher(
            @PathVariable Long teacherId,
            @RequestBody Set<Long> studentIds) {
        return teacherService.addStudentsToTeacher(teacherId, studentIds);
    }

    @GetMapping("/teachers/{id}")
    public TeacherEntity getTeacher(@PathVariable Long id) {
        return teacherService.getTeacherWithStudents(id);
    }

    @GetMapping("/students/{id}")
    public StudentEntity getStudent(@PathVariable Long id) {
        return studentService.getTeacherWithStudents(id);
    }
}
