package ru.soloviev.Controllers;

import ru.soloviev.Dao.CatDao;
import ru.soloviev.Entities.Cat;
import ru.soloviev.Models.Breed;
import ru.soloviev.Models.Color;
import ru.soloviev.Models.Name;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CatController {
    private CatDao catDao;

    public CatController(){
        catDao = new CatDao();
    }

    public void createCat(String name, String color, String birthday, String breed, Integer ownerId){
        Cat cat = new Cat();
        cat.setCatName(new Name(name));
        cat.setDateOfBirth(LocalDate.parse(birthday));
        cat.setColor(Color.valueOf(color));
        cat.setBreed(new Breed(breed));
        cat.setOwner(ownerId);
        catDao.save(cat);
    }

    public void deleteCat(Integer id){
        for (Integer catFriendId : getCatFriendIds(id))
            deleteFriendship(id, catFriendId);

        catDao.delete(catDao.find(id));
    }

    public List<Integer> getAllCatIds(){
        return catDao.findAll().stream().map(Cat::getId).toList();
    }

    public void addFriendship(Integer id1, Integer id2){
        catDao.addFriend(id1,id2);
    }

    public void deleteFriendship(Integer id1, Integer id2){
        catDao.removeFriend(id1, id2);
    }

    public String getCatName(Integer id){
        return catDao.find(id).getCatName().getName();
    }

    public String getCatColor(Integer id){
        return catDao.find(id).getColor().toString();
    }

    public String getCatBirthday(Integer id){
        return catDao.find(id).getDateOfBirth().toString();
    }

    public String getCatBreed(Integer id){
        return catDao.find(id).getBreed().getBreed();
    }

    public List<Integer> getCatFriendIds(Integer id){
        return catDao.find(id).getFriends().stream().map(Cat::getId).toList();
    }

    public Integer getCatOwnerId(Integer id){
        return catDao.find(id).getOwner();
    }

    public List<Integer> findCatIdsByBreed(String breed){
        List<Integer> list = new ArrayList<>();
        for (Cat cat : catDao.findAll()){
            if (cat.getBreed().getBreed().equals(breed))
                list.add(cat.getId());
        }
        return list;
    }

    public List<Integer> findCatIdsByBirthday(String birthday){
        List<Integer> list = new ArrayList<>();
        for (Cat cat : catDao.findAll()){
            if (cat.getDateOfBirth().toString().equals(birthday))
                list.add(cat.getId());
        }
        return list;
    }

    public List<Integer> findCatIdsByName(String name){
        List<Integer> list = new ArrayList<>();
        for (Cat cat : catDao.findAll()){
            if (cat.getCatName().getName().equals(name))
                list.add(cat.getId());
        }
        return list;
    }

    public void changeCatName(Integer id, String name){
        Cat cat = catDao.find(id);
        cat.setCatName(new Name(name));
        catDao.update(cat);
    }

    public void changeCatBreed(Integer id, String breed){
        Cat cat = catDao.find(id);
        cat.setBreed(new Breed(breed));
        catDao.update(cat);
    }

    public void changeCatColor(Integer id, String color){
        Cat cat = catDao.find(id);
        cat.setColor(Color.valueOf(color));
        catDao.update(cat);
    }

    public void changeCatBirthday(Integer id, String birthday){
        Cat cat = catDao.find(id);
        cat.setDateOfBirth(LocalDate.parse(birthday));
        catDao.update(cat);
    }

    public void changeCatOwner(Integer catId, Integer ownerId){
        Cat cat = catDao.find(catId);
        cat.setOwner(ownerId);
        catDao.update(cat);
    }
}
