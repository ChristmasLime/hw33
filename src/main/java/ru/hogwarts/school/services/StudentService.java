package ru.hogwarts.school.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStud(Student student) {
        return studentRepository.save(student);
    }

    public Student findStud(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Student editStud(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStud(Long id) {
        studentRepository.deleteById(id);

    }

    public Collection<Student> getAllStud() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudByAge(int age) {
        return studentRepository.getStudByAge(age);
    }

    public Collection<Student> getStudentsAgeBetween(int minAge, int maxAge) {
        return studentRepository.findStudByAgeBetween(minAge, maxAge);
    }

    public Faculty getFacultyByStudId(Long id) {
        Student student = studentRepository.findStudentById(id);
        if (student==null) {
            throw new NoSuchElementException("Студента в факультете не существует");
        }
        return student.getFaculty();
    }



}

