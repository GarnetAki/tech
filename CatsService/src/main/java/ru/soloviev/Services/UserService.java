package ru.soloviev.Services;

import ru.soloviev.Dao.UserDao;
import ru.soloviev.Entities.User;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Dto.UserIdDto;
import ru.soloviev.Mappers.UserMapper;

import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDto save(UserIdDto userId) {
        var user = UserMapper.mapToEntity(userId);
        userDao.save(user);

        return UserMapper.mapToDto(user);
    }

    public UserDto find(Integer id) {
        return UserMapper.mapToDto(userDao.find(id));
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public UserDto update(UserIdDto userId) {
        User user = userDao.update(UserMapper.mapToEntity(userId));
        return UserMapper.mapToDto(user);
    }

    public UserDto delete(UserIdDto userId) {
        var user = UserMapper.mapToEntity(userId);
        userDao.delete(user);

        return UserMapper.mapToDto(user);
    }
}
