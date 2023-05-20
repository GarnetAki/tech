package ru.soloviev.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.soloviev.Models.Name;
import ru.soloviev.Models.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public @Data class UserDto implements UserDetails {
    private Integer id;

    private String username;

    private String password;

    private Role role;

    @NotNull(message = "Name can not be null")
    private Name name;

    @NotNull(message = "Date of Birth can not be null")
    private LocalDate dateOfBirth;

    public UserDto(Integer id, String username, String password, Role role, Name name, LocalDate dateOfBirth) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public UserDto() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(role.name()));
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
