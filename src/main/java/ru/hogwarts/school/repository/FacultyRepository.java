package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> getFacultiesByColor(String color);

    Collection<Faculty> getFacultyByNameIgnoreCaseOrColorIgnoreCase(String name, String color);


}
