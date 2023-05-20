package ru.soloviev.Entities;

import jakarta.persistence.*;
import ru.soloviev.Models.Name;
import ru.soloviev.Models.Role;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String dateOfBirth;

    public User(){}

    public User(Integer id, Name name, LocalDate dateOfBirth, String username, String password, Role role){
        this.id = id;
        this.name = name.getName();
        this.dateOfBirth = dateOfBirth.toString();
        this.username = username;
        this.password = password;
        this.role = role;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setColor(Role role) {
        this.role = role;
    }
}
