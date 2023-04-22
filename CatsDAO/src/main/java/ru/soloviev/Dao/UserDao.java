package ru.soloviev.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.soloviev.Entities.User;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    List<User> findAllByName(String name);

    List<User> findAllByDateOfBirth(String date);
}