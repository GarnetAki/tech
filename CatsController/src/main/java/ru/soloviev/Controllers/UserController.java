package ru.soloviev.Controllers;

import ru.soloviev.Entities.User;
import ru.soloviev.Models.Name;
import ru.soloviev.Dao.UserDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserController {
    private UserDao userDao;

    public CatController catController;

    public UserController(){
        userDao = new UserDao();
        catController = new CatController();
    }

    public void createUser(String name, String birthday){
        User user = new User();
        user.setName(new Name(name));
        user.setDateOfBirth(LocalDate.parse(birthday));
        userDao.save(user);
    }

    public void deleteUser(Integer id){
        for (Integer catId : getUserCatIds(id)){
            for (Integer catFriendId : catController.getCatFriendIds(catId))
                catController.deleteFriendship(catId, catFriendId);

            catController.deleteCat(catId);
        }

        userDao.delete(userDao.find(id));
    }

    public List<Integer> getAllUserIds(){
        return userDao.findAll().stream().map(User::getId).toList();
    }

    public String getUserName(Integer id){
        return userDao.find(id).getName().getName();
    }

    public String getUserBirthday(Integer id){
        return userDao.find(id).getDateOfBirth().toString();
    }

    public List<Integer> getUserCatIds(Integer id){
        List<Integer> list = new ArrayList<>();
        for (Integer catId : catController.getAllCatIds()){
            if (Objects.equals(catController.getCatOwnerId(catId), id))
                list.add(catId);
        }
        return list;
    }

    public void changeUserName(Integer id, String name){
        User user = userDao.find(id);
        user.setName(new Name(name));
        userDao.update(user);
    }

    public void changeUserBirthday(Integer id, String birthday){
        User user = userDao.find(id);
        user.setDateOfBirth(LocalDate.parse(birthday));
        userDao.update(user);
    }

    public void changeCatOwner(Integer catId, Integer ownerId) {
        if (userDao.find(ownerId) == null)
            throw new NullPointerException();

        catController.changeCatOwner(catId, ownerId);
    }

    public void createCat(String name, String color, String birthday, String breed, Integer ownerId){
        if (userDao.find(ownerId) == null)
            throw new NullPointerException();

        catController.createCat(name, color, birthday, breed, ownerId);
    }
}