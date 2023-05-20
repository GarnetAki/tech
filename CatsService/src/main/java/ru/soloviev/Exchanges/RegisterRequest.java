package ru.soloviev.Exchanges;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.soloviev.Models.Name;

import java.time.LocalDate;

public @Data class RegisterRequest {

    @NotNull(message = "Username can not be null")
    private String username;

    @NotNull(message = "Password can not be null")
    private String password;

    @NotNull(message = "Name can not be null")
    private Name name;

    @NotNull(message = "Date of Birth can not be null")
    private LocalDate dateOfBirth;

    private String adminAccess;

    public RegisterRequest(){}

    public RegisterRequest(String username, String password, Name name, LocalDate dateOfBirth, String adminAccess) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.adminAccess = adminAccess;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAdminAccess() {
        return adminAccess;
    }

    public void setAdminAccess(String adminAccess) {
        this.adminAccess = adminAccess;
    }
}
