package ru.soloviev.UI;

import ru.soloviev.Controllers.UserController;
import java.util.Scanner;

public class CUI implements UI {

    private String inputString;

    private final UserController userController = new UserController();

    private final Scanner in = new Scanner(System.in);

    public CUI(){
        inputString = "";
    }

    @Override
    public void Run()
    {
        while (!inputString.equals("Stop"))
        {
            inputString = in.nextLine();
            Parse();
        }
    }

    private void Parse()
    {
        switch (inputString) {
            case "Create cat" -> createCat();
            case "Delete cat" -> deleteCat();
            case "Add friendship" -> addFriendship();
            case "Delete friendship" -> deleteFriendship();

            case "Get all cats" -> getAllCats();
            case "Get cat friends" -> getCatFriends();
            case "Get cat owner" -> getCatOwner();

            case "Find cats with breed" -> findCatsWithBreed();
            case "Find cats with birthday" -> findCatsWithBirthday();
            case "Find cats with name" -> findCatsWithName();

            case "Change cat name" -> changeCatName();
            case "Change cat breed" -> changeCatBreed();
            case "Change cat color" -> changeCatColor();
            case "Change cat birthday" -> changeCatBirthday();

            case "Create user" -> createUser();
            case "Delete user" -> deleteUser();
            case "Get all users" -> getAllUsers();
            case "Get user cats" -> getUserCats();
            case "Change user name" -> changeUserName();
            case "Change user birthday" -> changeUserBirthday();
            case "Change cat owner" -> changeCatOwner();
        }
    }

    private void createCat() {
        System.out.println("Enter cat name:");
        String name = in.nextLine();
        System.out.println("Enter cat color (White/Black/Grey/Red):");
        String color = in.nextLine();
        System.out.println("Enter cat birthday:");
        String birthday = in.nextLine();
        System.out.println("Enter cat breed:");
        String breed = in.nextLine();
        System.out.println("Enter cat's owner id:");
        String ownerId = in.nextLine();
        if (name == null || color == null ||
                birthday == null || breed == null || ownerId == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.createCat(name, color, birthday, breed, Integer.parseInt(ownerId));
            System.out.println("Cat created");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void deleteCat() {
        System.out.println("Enter cat id:");
        String id = in.nextLine();

        if (id == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.catController.deleteCat(Integer.parseInt(id));
            System.out.println("Cat deleted");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void addFriendship(){
        System.out.println("Enter first cat id:");
        String id1 = in.nextLine();
        System.out.println("Enter second cat id:");
        String id2 = in.nextLine();

        if (id1 == null || id2 == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.catController.addFriendship(Integer.parseInt(id1), Integer.parseInt(id2));
            System.out.println("Friendship added");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void deleteFriendship(){
        System.out.println("Enter first cat id:");
        String id1 = in.nextLine();
        System.out.println("Enter second cat id:");
        String id2 = in.nextLine();

        if (id1 == null || id2 == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.catController.deleteFriendship(Integer.parseInt(id1), Integer.parseInt(id2));
            System.out.println("Friendship deleted");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void getAllCats(){
        try
        {
            var list = userController.catController.getAllCatIds();
            for (var id : list){
                System.out.println(id + " | " +
                        userController.catController.getCatName(id) + " | " +
                        userController.catController.getCatColor(id) + " | " +
                        userController.catController.getCatBirthday(id) + " | " +
                        userController.catController.getCatBreed(id) + " | " +
                        userController.catController.getCatOwnerId(id) + " | " +
                        userController.getUserName(userController.catController.getCatOwnerId(id)));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void getCatFriends(){
        System.out.println("Enter cat id:");
        String id = in.nextLine();

        if (id == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var list = userController.catController.getCatFriendIds(Integer.parseInt(id));
            for (var cat : list){
                System.out.println(cat + " | " +
                        userController.catController.getCatName(cat) + " | " +
                        userController.catController.getCatColor(cat) + " | " +
                        userController.catController.getCatBirthday(cat) + " | " +
                        userController.catController.getCatBreed(cat) + " | " +
                        userController.catController.getCatOwnerId(cat) + " | " +
                        userController.getUserName(userController.catController.getCatOwnerId(cat)));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void getCatOwner(){
        System.out.println("Enter cat id:");
        String id = in.nextLine();

        if (id == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var user = userController.catController.getCatOwnerId(Integer.parseInt(id));
            System.out.println(user + " | " +
                    userController.getUserName(user) + " | " +
                    userController.getUserBirthday(user));

        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void findCatsWithBreed(){
        System.out.println("Enter searching breed:");
        String breed = in.nextLine();

        if (breed == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var list = userController.catController.findCatIdsByBreed(breed);
            for (var cat : list){
                System.out.println(cat + " | " +
                        userController.catController.getCatName(cat) + " | " +
                        userController.catController.getCatColor(cat) + " | " +
                        userController.catController.getCatBirthday(cat) + " | " +
                        userController.catController.getCatBreed(cat));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void findCatsWithBirthday(){
        System.out.println("Enter searching birthday:");
        String birthday = in.nextLine();

        if (birthday == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var list = userController.catController.findCatIdsByBirthday(birthday);
            for (var cat : list){
                System.out.println(cat + " | " +
                        userController.catController.getCatName(cat) + " | " +
                        userController.catController.getCatColor(cat) + " | " +
                        userController.catController.getCatBirthday(cat) + " | " +
                        userController.catController.getCatBreed(cat));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void findCatsWithName(){
        System.out.println("Enter searching name:");
        String name = in.nextLine();

        if (name == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var list = userController.catController.findCatIdsByName(name);
            for (var cat : list){
                System.out.println(cat + " | " +
                        userController.catController.getCatName(cat) + " | " +
                        userController.catController.getCatColor(cat) + " | " +
                        userController.catController.getCatBirthday(cat) + " | " +
                        userController.catController.getCatBreed(cat));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void changeCatName(){
        System.out.println("Enter cat id:");
        String id = in.nextLine();
        System.out.println("Enter new name:");
        String name = in.nextLine();

        if (id == null || name == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.catController.changeCatName(Integer.parseInt(id), name);
            System.out.println("Name changed");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void changeCatBreed(){
        System.out.println("Enter cat id:");
        String id = in.nextLine();
        System.out.println("Enter new breed:");
        String breed = in.nextLine();

        if (id == null || breed == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.catController.changeCatBreed(Integer.parseInt(id), breed);
            System.out.println("Breed changed");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void changeCatColor(){
        System.out.println("Enter cat id:");
        String id = in.nextLine();
        System.out.println("Enter new color:");
        String color = in.nextLine();

        if (id == null || color == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.catController.changeCatColor(Integer.parseInt(id), color);
            System.out.println("Color changed");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void changeCatBirthday(){
        System.out.println("Enter cat id:");
        String id = in.nextLine();
        System.out.println("Enter new birthday:");
        String birthday = in.nextLine();

        if (id == null || birthday == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.catController.changeCatBirthday(Integer.parseInt(id), birthday);
            System.out.println("Birthday changed");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void createUser(){
        System.out.println("Enter user name:");
        String name = in.nextLine();
        System.out.println("Enter user birthday:");
        String birthday = in.nextLine();
        if (name == null || birthday == null ) {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.createUser(name, birthday);
            System.out.println("User created");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void deleteUser(){
        System.out.println("Enter user id:");
        String id = in.nextLine();

        if (id == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.deleteUser(Integer.parseInt(id));
            System.out.println("User deleted");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void getAllUsers(){
        try
        {
            var list = userController.getAllUserIds();
            for (var id : list){
                System.out.println(id + " | " +
                        userController.getUserName(id) + " | " +
                        userController.getUserBirthday(id));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void getUserCats(){
        System.out.println("Enter user id:");
        String id = in.nextLine();

        if (id == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var list = userController.getUserCatIds(Integer.parseInt(id));
            for (var cat : list){
                System.out.println(cat + " | " +
                        userController.catController.getCatName(cat) + " | " +
                        userController.catController.getCatColor(cat) + " | " +
                        userController.catController.getCatBirthday(cat) + " | " +
                        userController.catController.getCatBreed(cat));
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void changeUserName(){
        System.out.println("Enter user id:");
        String id = in.nextLine();
        System.out.println("Enter new name:");
        String name = in.nextLine();

        if (id == null || name == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.changeUserName(Integer.parseInt(id), name);
            System.out.println("Name changed");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void changeUserBirthday(){
        System.out.println("Enter user id:");
        String id = in.nextLine();
        System.out.println("Enter new birthday:");
        String birthday = in.nextLine();

        if (id == null || birthday == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.changeUserBirthday(Integer.parseInt(id), birthday);
            System.out.println("Birthday changed");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void changeCatOwner(){
        System.out.println("Enter cat id:");
        String catId = in.nextLine();
        System.out.println("Enter user id:");
        String id = in.nextLine();

        if (id == null || catId == null){
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            userController.changeCatOwner(Integer.parseInt(catId), Integer.parseInt(id));
            System.out.println("Owner changed");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}