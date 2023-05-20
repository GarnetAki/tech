package ru.soloviev.Services;

import org.springframework.data.domain.Pageable;
import ru.soloviev.Dao.CatDao;
import ru.soloviev.Dto.CatDto;
import ru.soloviev.Dto.CatIdDto;
import ru.soloviev.Dto.UserIdDto;
import ru.soloviev.Mappers.CatMapper;
import ru.soloviev.Models.Color;

import java.util.HashSet;
import java.util.List;

public class CatService {
    private final CatDao catDao;

    private final CatMapper catMapper;

    public CatService(CatDao catDao) {
        this.catDao = catDao;
        catMapper = new CatMapper(this.catDao);
    }

    public CatDto update(CatDto cat) {
        var newCat = catMapper.mapToEntityUp(cat);
        newCat.setCatName(cat.getName());
        newCat.setBreed(cat.getBreed());
        newCat.setColor(cat.getColor());
        newCat.setDateOfBirth(cat.getDateOfBirth());
        newCat.setOwner(cat.getOwnerId());
        catDao.saveAndFlush(newCat);

        return catMapper.mapToDto(newCat);
    }

    public CatDto save(CatDto cat) {
        var newCat = catMapper.mapToEntity(cat);
        catDao.saveAndFlush(newCat);

        return catMapper.mapToDto(newCat);
    }

    public CatDto find(Integer id) {
        return catMapper.mapToDto(catDao.getReferenceById(id));
    }

    public List<CatDto> findAll() {
        return catDao.findAll().stream().map(CatMapper::mapToDto).toList();
    }

    public List<CatDto> findAll(Pageable pageable){
        return catDao.findAll(pageable).stream().map(CatMapper::mapToDto).toList();
    }

    public List<CatDto> findAll(Object object){
        if (object.getClass().getSimpleName().equals("Breed"))
            return catDao.findAllByBreed(object.toString()).stream().map(CatMapper::mapToDto).toList();

        if (object.getClass().getSimpleName().equals("Color"))
            return catDao.findAllByColor((Color) object).stream().map(CatMapper::mapToDto).toList();

        if (object.getClass().getSimpleName().equals("Name"))
            return catDao.findAllByCatName(object.toString()).stream().map(CatMapper::mapToDto).toList();

        if (object.getClass().getSimpleName().equals("LocalDate"))
            return catDao.findAllByDateOfBirth(object.toString()).stream().map(CatMapper::mapToDto).toList();

        throw new NullPointerException();
    }

    public List<CatDto> findAllByOwner(UserIdDto user){
        return catDao.findAllByOwnerId(user.getId()).stream().map(CatMapper::mapToDto).toList();
    }

    public CatDto delete(CatIdDto catId) {
        var cat = catMapper.mapToEntity(catId);
        var friends = cat.getFriends();
        cat.setFriends(new HashSet<>());
        catDao.saveAndFlush(cat);
        for (var cats : friends){
            var fr = cats.getFriends();
            fr.remove(cat);
            cats.setFriends(fr);
        }

        catDao.delete(cat);
        catDao.flush();

        return CatMapper.mapToDto(cat);
    }

    public void addFriend(CatIdDto cat, CatIdDto friend) {
        if (catDao.findById(cat.getId()).isEmpty() || catDao.findById(friend.getId()).isEmpty())
            throw new NullPointerException();

        var cat1 = catDao.findById(cat.getId()).get();
        var cat2 = catDao.findById(friend.getId()).get();
        var fr1 = cat1.getFriends();
        var fr2 = cat2.getFriends();
        fr2.add(cat1);
        fr1.add(cat2);
        cat1.setFriends(fr1);
        cat2.setFriends(fr2);
        catDao.saveAndFlush(cat1);
        catDao.saveAndFlush(cat2);
    }

    public void removeFriend(CatIdDto cat, CatIdDto friend) {
        if (catDao.findById(cat.getId()).isEmpty() || catDao.findById(friend.getId()).isEmpty())
            throw new NullPointerException();

        var cat1 = catDao.findById(cat.getId()).get();
        var cat2 = catDao.findById(friend.getId()).get();
        var fr1 = cat1.getFriends();
        var fr2 = cat2.getFriends();
        fr2.remove(cat1);
        fr1.remove(cat2);
        cat1.setFriends(fr1);
        cat2.setFriends(fr2);
        catDao.saveAndFlush(cat1);
        catDao.saveAndFlush(cat2);
    }
}
