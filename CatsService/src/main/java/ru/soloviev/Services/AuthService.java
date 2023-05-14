package ru.soloviev.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.soloviev.Dao.UserDao;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Entities.User;
import ru.soloviev.Exchanges.LoginRequest;
import ru.soloviev.Mappers.UserMapper;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserMapper.mapToDto(user);
    }

    public Boolean existsByLogin(String username){
        return userDao.existsByUsername(username);
    }


    public UserDto login(LoginRequest dto) {

        var user = userDao.findByUsername(dto.getUsername())
                .orElseThrow(() -> new NullPointerException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return UserMapper.mapToDto(user);
    }
}
