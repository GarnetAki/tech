package ru.soloviev;

public class Validator {
    public void validateName(String string) throws IllegalArgumentException{
        if (string == null) throw new NullPointerException();

        String[] subs = string.split(" ");

        for (String sub : subs) validateString(sub);
    }

    public void validateString(String string) throws IllegalArgumentException{
        if (string == null) throw new NullPointerException();

        if (!(string.matches("^[A-Z][a-z]*$")))
            throw new IllegalArgumentException();
    }
}