package ru.soloviev.Models;

import ru.soloviev.Validator;

public class Breed {
    private String breed;

    public Breed(String breed){
        this.setBreed(breed);
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        new Validator().validateString(breed);

        this.breed = breed;
    }
}