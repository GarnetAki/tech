package ru.soloviev.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import ru.soloviev.Dao.CatDao;
import ru.soloviev.Dao.UserDao;
import ru.soloviev.Dto.*;
import ru.soloviev.Exchanges.StringResponse;
import ru.soloviev.Mappers.CatMapper;
import ru.soloviev.Models.Name;
import ru.soloviev.Models.Role;
import ru.soloviev.Security.JWT.JwtUtils;
import ru.soloviev.Services.AuthService;
import ru.soloviev.Services.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    private final CatController catController;

    private final AuthService authService;

    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserDao userDao, CatDao catDao, AuthService authService, JwtUtils jwtUtils, PasswordEncoder encoder){
        this.userService = new UserService(userDao);
        catController = new CatController(catDao, authService, jwtUtils);
        this.authService = authService;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/users/get_user/{id}")
    public UserDto getUserById(HttpServletRequest req, @Valid @PathVariable() Integer id){
        catController.accessCheckById(req, id);

        return userService.find(id);
    }

    @PostMapping("/users/create")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto userCreateSubmit(@Valid @RequestBody UserDto userDto) {
        userDto.setId(null);
        return userService.save(userDto);
    }

    @PostMapping("/users/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto userDeleteSubmit(@Valid @RequestBody UserIdDto userIdDto) {
        for (var cat : catController.getCatService().findAll()){
            if (cat.getOwnerId().equals(userIdDto.getId()))
                catController.getCatService().delete(CatMapper.mapToIdDto(CatMapper.mapToEntity(cat)));
        }

        userService.find(userIdDto.getId());

        return userService.delete(userIdDto);
    }

    @GetMapping("/users/find_by_name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> findUserIdsByName(@Valid @PathVariable() String name) throws IllegalArgumentException{
        return userService.findAll(new Name(name));
    }

    @GetMapping("/users/find_by_dob/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> findUserIdsByDateOfBirth(@Valid @PathVariable() LocalDate date){
        return userService.findAll(date);
    }

    @PostMapping("/users/change")
    public UserDto userChangeSubmit(HttpServletRequest req, @RequestBody UserDto user) {
        catController.accessCheckById(req, user.getId());

        var userOld = userService.find(user.getId());

        if (user.getName() != null)
            userOld.setName(user.getName());

        if (user.getDateOfBirth() != null)
            userOld.setDateOfBirth(user.getDateOfBirth());

        if (user.getUsername() != null){
            if (authService.existsByLogin(user.getUsername())) {
                throw new RequestRejectedException("Error: Username is already taken!");
            }
            userOld.setUsername(user.getUsername());
        }

        if (user.getPassword() != null)
            userOld.setPassword(encoder.encode(user.getPassword()));

        return userService.save(userOld);
    }

    @GetMapping("/users/get_cats/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CatDto> getUserCats(@Valid @PathVariable() Integer id){
        userService.find(id);
        UserIdDto userIdDto = new UserIdDto(id);

        return catController.getCatService().findAllByOwner(userIdDto);
    }

    @PostMapping("/cats/change_owner")
    @PreAuthorize("hasRole('ADMIN')")
    public CatDto changeCatOwnerSubmit(@Valid @RequestBody TwoIdsDto idsDto) {
        userService.find(idsDto.getCat2().getId());
        var cat = catController.getCatService().find(idsDto.getCat1().getId());
        cat.setOwnerId(idsDto.getCat2().getId());

        return catController.getCatService().update(cat);
    }

    @PostMapping("/cats/create")
    public CatDto catCreateSubmit(HttpServletRequest req, @Valid @RequestBody CatDto catDto) {
        catController.accessCheckById(req, catDto.getOwnerId());

        catDto.setId(null);
        return catController.getCatService().save(catDto);
    }

    public UserService getUserService(){
        return userService;
    }

}