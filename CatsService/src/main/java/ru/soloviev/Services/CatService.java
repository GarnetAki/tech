package ru.soloviev.Services;

import ru.soloviev.Dao.CatDao;
import ru.soloviev.Entities.Cat;
import ru.soloviev.Dto.CatDto;
import ru.soloviev.Dto.CatIdDto;
import ru.soloviev.Mappers.CatMapper;

import java.util.List;

public class CatService {
    private final CatDao catDao;

    public CatService(CatDao catDao) {
        this.catDao = catDao;
    }

    public CatDto save(CatDto cat) {
        var newCat = CatMapper.mapToEntity(cat);
        catDao.save(newCat);

        return CatMapper.mapToDto(newCat);
    }

    public CatDto find(Integer id) {
        return CatMapper.mapToDto(catDao.find(id));
    }

    public List<Cat> findAll() {
        return catDao.findAll();
    }

    public CatDto update(CatIdDto catId) {
        Cat cat = catDao.update(CatMapper.mapToEntity(catId));
        return CatMapper.mapToDto(cat);
    }

    public CatDto delete(CatIdDto catId) {
        var cat = CatMapper.mapToEntity(catId);
        catDao.delete(cat);

        return CatMapper.mapToDto(cat);
    }

    public CatDto addFriend(CatIdDto cat, CatIdDto friend) {
        catDao.addFriend(cat.getId(), friend.getId());
        catDao.addFriend(friend.getId(), cat.getId());

        return CatMapper.mapToDto(CatMapper.mapToEntity(cat));
    }

    public void removeFriend(CatIdDto cat, CatIdDto friend) {
        catDao.removeFriend(cat.getId(), friend.getId());
        catDao.removeFriend(friend.getId(), cat.getId());
    }
}
