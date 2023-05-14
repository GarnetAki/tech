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

    public void setBreed(String breed) throws IllegalArgumentException {
        new Validator().validateString(breed);

        this.breed = breed;
    }

    public String toString(){
        return getBreed();
    }
}
