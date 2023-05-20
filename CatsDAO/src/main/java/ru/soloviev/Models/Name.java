package ru.soloviev.Models;

import ru.soloviev.Validator;

public class Name {
    private String name;

    public Name(String name){
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        new Validator().validateName(name);

        this.name = name;
    }

    public String toString(){
        return getName();
    }
}