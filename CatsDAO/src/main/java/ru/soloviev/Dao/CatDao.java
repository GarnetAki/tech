package ru.soloviev.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.soloviev.Entities.Cat;
import ru.soloviev.Models.Color;

import java.util.List;

@Repository
public interface CatDao extends JpaRepository<Cat, Integer> {

    List<Cat> findAllByOwnerId(Integer ownerId);

    List<Cat> findAllByBreed(String breed);

    List<Cat> findAllByCatName(String name);

    List<Cat> findAllByColor(Color color);

    List<Cat> findAllByDateOfBirth(String date);
}