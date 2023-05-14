package ru.soloviev.Mappers;

import ru.soloviev.Dao.UserDao;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Dto.UserIdDto;
import ru.soloviev.Entities.User;

public class UserMapper {

    private static UserDao userDao;

    public UserMapper(UserDao userDao) {
        UserMapper.userDao = userDao;
    }

    public static User mapToEntity(UserIdDto dto) {
        return userDao.getReferenceById(dto.getId());
    }

    public static User mapToEntity(UserDto dto) {
        return new User(dto.getId(), dto.getName(), dto.getDateOfBirth(),
                dto.getUsername(), dto.getPassword(), dto.getRole());
    }

    public static UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setRole(user.getRole());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(userDto.getPassword());
        return userDto;
    }

    public static String mapToString(UserDto user) {
        return user.getId() + " | " +
                user.getUsername() + " | " +
                user.getName().getName() + " | " +
                user.getDateOfBirth();
    }
}
