package ru.hogwarts.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.HomeWork33Application;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = HomeWork33Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplate {
    public static final Student STUD1 = new Student(null, "Roma", 35);
    public static final Student STUD2 = new Student(null, "Anna", 21);

    @Autowired
    TestRestTemplate template;
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        template.postForEntity("/student", STUD1, Student.class);
        template.postForEntity("/student", STUD2, Student.class);
    }

    @AfterEach
    void clearDB() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();

    }

    private ResponseEntity<Student> createStudent(String name, int age) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        ResponseEntity<Student> response = template.postForEntity("/student", student, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;
    }


    @Test
    void createStudentTest() {
        ResponseEntity<Student> response = createStudent("Stas", 25);
        Student createdStudent = response.getBody();
        assertThat(createdStudent.getName()).isEqualTo("Stas");
        assertThat(createdStudent.getAge()).isEqualTo(25);
    }

    @Test
    void findStudentTest() {
        ResponseEntity<Student> student = createStudent("Stas", 25);
        Long id = student.getBody().getId();

        ResponseEntity<Student> response = template.getForEntity("/student/" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((response.getBody())).isNotNull();
        assertThat((response.getBody()).getId()).isEqualTo(id);
        assertThat((response.getBody()).getName()).isEqualTo("Stas");
        assertThat((response.getBody()).getAge()).isEqualTo(25);
    }

    @Test
    void editStudentTest() {
        ResponseEntity<Student> response = createStudent("Stas", 25);
        Student student = response.getBody();
        student.setName("Roman");
        template.put("/student", student, Student.class);
        response = template.getForEntity("/student/" + student.getId(), Student.class);
        assertThat(response.getBody().getName()).isEqualTo("Roman");

    }

    @Test
    void deleteStudentTest() {
        ResponseEntity<Student> student = createStudent("Stas", 25);
        Long id = student.getBody().getId();
        template.delete("/student/" + id);
        student = template.getForEntity("/student/" + id, Student.class);
        assertThat(student.getBody().getName()).isNull();
        assertThat(student.getBody().getAge()).isZero();
        assertThat(student.getBody()).isNotNull();
        Optional<Student> deletedStudent = studentRepository.findById(id);
        assertThat(deletedStudent).isEmpty();
    }

    @Test
    void getAllStudentTest() {
        ResponseEntity<Collection> response = template.getForEntity("/student", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Collection<Student> body = response.getBody();
        assertThat(body.isEmpty()).isFalse();
        assertThat(body.size()).isEqualTo(2);
    }

    @Test
    void getStudentByAge() {
        ResponseEntity<Collection> response = template.getForEntity("/student/age/35", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void getStudentsByAgeBetween() {
        ResponseEntity<Student> response = createStudent("Elena", 15);
        Long id = response.getBody().getId();
        ResponseEntity<Collection> ageBetweenResponse = template.getForEntity("/student/age-between?minAge=10&maxAge=20", Collection.class);
        assertThat(ageBetweenResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ageBetweenResponse.getBody()).isNotNull();
        assertThat(ageBetweenResponse.getBody().size()).isEqualTo(1);
        template.delete("/student/" + id);
        ageBetweenResponse = template.getForEntity("/student/age-between?minAge=10&maxAge=20", Collection.class);
        assertThat(ageBetweenResponse.getBody()).isEmpty();

    }
    @Test
    void byFaculty() {
        Faculty expectedFaculty = new Faculty(null, "Gryffindor", "Red");
        facultyRepository.save(expectedFaculty);
        Student student = new Student();
        student.setFaculty(expectedFaculty);
        ResponseEntity<Student> studentResponse = template.postForEntity("/student", student, Student.class);
        Long studId = studentResponse.getBody().getId();
        ResponseEntity<Faculty> response = template.getForEntity("/faculty/by-student?id=" + studId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(expectedFaculty);
    }
}
