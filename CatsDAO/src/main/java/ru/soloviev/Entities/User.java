package ru.soloviev.Entities;

import ru.soloviev.Models.Name;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String dateOfBirth;

    public User(){}

    public User(Integer id, Name name, LocalDate dateOfBirth){
        this.id = id;
        this.name = name.getName();
        this.dateOfBirth = dateOfBirth.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Name getName() {
        return new Name(name);
    }

    public void setName(Name name) {
        this.name = name.getName();
    }

    public LocalDate getDateOfBirth() {
        return LocalDate.parse(dateOfBirth);
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth.toString();
    }
}
