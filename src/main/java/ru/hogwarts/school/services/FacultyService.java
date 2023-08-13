package ru.hogwarts.school.services;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class FacultyService {

    private final FacultyRepository repository;

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty createFacul(Faculty faculty) {
        return repository.save(faculty);
    }

    public Faculty findFacul(Long id) {
        return repository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Faculty editFacul(Faculty faculty) {
        return repository.save(faculty);
    }

    public void deleteFacul(Long id) {
        repository.deleteById(id);
    }

    public Collection<Faculty> getAllFacul() {
        return repository.findAll();
    }

    public Collection<Faculty> getFaculByColor(String color) {
        return repository.getFacultiesByColor(color);
    }

    public Collection<Faculty> getFacultyNameOrColor(String searchString) {
        return repository.getFacultyByNameIgnoreCaseOrColorIgnoreCase(searchString, searchString);
    }

    public Collection<Student> getFacultyInStudent(Long id) {
        Faculty faculty = repository.findFacultiesById(id);
        if (faculty==null) {
            throw new NoSuchElementException("Факультета с данный ID не сущестует");
        }
        return faculty.getStudents();
    }
}

