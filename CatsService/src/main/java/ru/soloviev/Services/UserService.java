package ru.soloviev.Services;

import org.springframework.data.domain.Pageable;
import ru.soloviev.Dao.UserDao;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Dto.UserIdDto;
import ru.soloviev.Mappers.UserMapper;

import java.util.List;

public class UserService {

    private UserDao userDao;

    private final UserMapper userMapper;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
        userMapper = new UserMapper(this.userDao);
    }

    public UserDto save(UserDto user) {
        var newUser = userMapper.mapToEntity(user);
        userDao.saveAndFlush(newUser);

        return userMapper.mapToDto(newUser);
    }

    public UserDto find(Integer id) {
        return userMapper.mapToDto(userDao.getReferenceById(id));
    }

    public List<UserDto> findAll() {
        return userDao.findAll().stream().map(UserMapper::mapToDto).toList();
    }

    public List<UserDto> findAll(Pageable pageable){
        return userDao.findAll(pageable).stream().map(UserMapper::mapToDto).toList();
    }

    public List<UserDto> findAll(Object object){

        if (object.getClass().getSimpleName().equals("Name"))
            return userDao.findAllByName(object.toString()).stream().map(UserMapper::mapToDto).toList();

        if (object.getClass().getSimpleName().equals("LocalDate"))
            return userDao.findAllByDateOfBirth(object.toString()).stream().map(UserMapper::mapToDto).toList();

        throw new NullPointerException();
    }

    public UserDto delete(UserIdDto userId) {
        var user = userMapper.mapToEntity(userId);
        userDao.delete(user);

        return UserMapper.mapToDto(user);
    }
}
