package ru.soloviev.Mappers;

import ru.soloviev.Dao.UserDao;
import ru.soloviev.Entities.User;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Dto.UserIdDto;

public class UserMapper {
    private static final UserDao userDao = new UserDao();

    private UserMapper() {}

    public static User mapToEntity(UserIdDto dto) {
        return userDao.find(dto.getId());
    }

    public static User mapToEntity(UserDto dto) {
        return new User(dto.getId(), dto.getName(), dto.getDateOfBirth());
    }

    public static UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setDateOfBirth(user.getDateOfBirth());
        return userDto;
    }
}
