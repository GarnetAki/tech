package ru.soloviev.Entities;

import jakarta.persistence.*;
import ru.soloviev.Models.Breed;
import ru.soloviev.Models.Color;
import ru.soloviev.Models.Name;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cat")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String catName;

    private String dateOfBirth;

    private String breed;

    @Enumerated(value = EnumType.STRING)
    private Color color;

    private Integer ownerId;

    @Column(name = "friends", columnDefinition = "integer[]")
    @ManyToMany(targetEntity = Cat.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "cat_cat",
    joinColumns = @JoinColumn(name = "cat1_id"),
    inverseJoinColumns = @JoinColumn(name = "cat2_id"))
    private Set<Cat> friends = new HashSet<>();

    public Cat(){}

    public Cat(Integer id, Name name, Breed breed, Color color, LocalDate dateOfBirth, Integer ownerId){
        this.id = id;
        catName = name.getName();
        this.breed = breed.getBreed();
        this.color = color;
        this.dateOfBirth = dateOfBirth.toString();
        this.ownerId = ownerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
         this.id = id;
    }

    public Name getCatName() {
        return new Name(catName);
    }

    public void setCatName(Name catName) {
        this.catName = catName.getName();
    }

    public LocalDate getDateOfBirth() {
        return LocalDate.parse(dateOfBirth);
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth.toString();
    }

    public Breed getBreed() {
        return new Breed(breed);
    }

    public void setBreed(Breed breed) {
        this.breed = breed.getBreed();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getOwner() {
        return ownerId;
    }

    public void setOwner(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Cat> getFriends() {
        return friends;
    }

    public void setFriends(Set<Cat> friends) {
        this.friends = friends;
    }
}
