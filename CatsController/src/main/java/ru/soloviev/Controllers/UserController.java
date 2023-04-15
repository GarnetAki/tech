package ru.soloviev.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.soloviev.Dao.CatDao;
import ru.soloviev.Dao.UserDao;
import ru.soloviev.Dto.CatDto;
import ru.soloviev.Dto.TwoIdsDto;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Dto.UserIdDto;
import ru.soloviev.Mappers.CatMapper;
import ru.soloviev.Mappers.UserMapper;
import ru.soloviev.Models.Name;
import ru.soloviev.Services.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    private final CatController catController;

    @Autowired
    public UserController(UserDao userDao, CatDao catDao){
        this.userService = new UserService(userDao);
        catController = new CatController(catDao);
    }

    @GetMapping("/users")
    @ResponseBody
    public List<String> getAllUsers(){
        var listUsers = userService.findAll();
        var answer = new ArrayList<String>();
        for (var user : listUsers){
            answer.add(UserMapper.mapToString(user));
        }
        return answer;
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public String getUserById(@PathVariable(required = false) Integer id){
        var user = userService.find(id);
        return UserMapper.mapToString(user);
    }

    @GetMapping("/users/create")
    public String userCreateForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user-create";
    }

    @PostMapping("/users/create")
    public String userCreateSubmit(@ModelAttribute UserDto userDto, Model model) {
        var savedUser = userService.save(userDto);
        model.addAttribute("user", savedUser);
        return "user-create-return";
    }

    @GetMapping("/users/delete")
    public String userDeleteForm(Model model) {
        model.addAttribute("user", new UserIdDto());
        return "user-delete";
    }

    @PostMapping("/users/delete")
    public String userDeleteSubmit(@ModelAttribute UserIdDto userIdDto, Model model) {
        for (var cat : catController.getCatService().findAll()){
            if (cat.getOwnerId().equals(userIdDto.getId()))
                catController.getCatService().delete(CatMapper.mapToIdDto(CatMapper.mapToEntity(cat)));
        }

        var deletedUser = userService.delete(userIdDto);
        model.addAttribute("user", deletedUser);
        return "user-delete-return";
    }

    @GetMapping("/users/find_by_name/{name}")
    @ResponseBody
    public List<String> findUserIdsByName(@PathVariable(required = false) String name){
        List<UserDto> list = userService.findAll(new Name(name));
        return list.stream().map(UserMapper::mapToString).toList();
    }

    @GetMapping("/users/find_by_dob/{date}")
    @ResponseBody
    public List<String> findUserIdsByDateOfBirth(@PathVariable(required = false) String date){
        List<UserDto> list = userService.findAll(LocalDate.parse(date));
        return list.stream().map(UserMapper::mapToString).toList();
    }

    @GetMapping("/users/change_user")
    public String userChangeForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user-change";
    }

    @PostMapping("/users/change_user")
    public String userChangeSubmit(@ModelAttribute UserDto user, Model model) {
        var userOld = userService.find(user.getId());

        if (user.getName() != null)
            userOld.setName(user.getName());

        if (user.getDateOfBirth() != null)
            userOld.setDateOfBirth(user.getDateOfBirth());

        model.addAttribute("user", userService.save(userOld));
        return "user-change-return";
    }

    @GetMapping("/users/get_cats/{id}")
    @ResponseBody
    public List<String> getUserCats(@PathVariable(required = false) Integer id){
        UserIdDto userIdDto = new UserIdDto(id);
        return catController.getCatService().findAllByOwner(userIdDto).stream().map(CatMapper::mapToString).toList();
    }

    @GetMapping("/cats/change_owner")
    public String changeCatOwnerForm(Model model) {
        model.addAttribute("cat", new TwoIdsDto());
        return "cat-change-owner";
    }

    @PostMapping("/cats/change_owner")
    public String changeCatOwnerSubmit(@ModelAttribute TwoIdsDto idsDto, Model model) {
        userService.find(idsDto.getCat2().getId());
        var cat = catController.getCatService().find(idsDto.getCat1().getId());
        cat.setOwnerId(idsDto.getCat2().getId());
        model.addAttribute("cat", catController.getCatService().save(cat));
        return "cat-change-owner-return";
    }

    @GetMapping("/cats/create")
    public String catCreateForm(Model model) {
        model.addAttribute("cat", new CatDto());
        return "cat-create";
    }

    @PostMapping("/cats/create")
    public String catCreateSubmit(@ModelAttribute CatDto catDto, Model model) {
        var savedCat = catController.getCatService().save(catDto);
        model.addAttribute("cat", savedCat);
        return "cat-create-return";
    }
}