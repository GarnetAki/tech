package ru.soloviev.Controllers;

import jakarta.validation.Valid;
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
    public String getAllUsers(Model model){
        var list = userService.findAll();
        model.addAttribute("lists", list);
        return "user-list";
    }

    @GetMapping("/users/{id}")
    public String getUserById(@Valid @PathVariable() Integer id, Model model){
        try{
            var user = userService.find(id);
            var list = new ArrayList<>();
            list.add(user);
            model.addAttribute("lists", list);
            return "user-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/users/create")
    public String userCreateForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user-create";
    }

    @PostMapping("/users/create")
    public String userCreateSubmit(@Valid @ModelAttribute UserDto userDto, Model model) {
        try{
            var savedUser = userService.save(userDto);
            model.addAttribute("user", savedUser);
            return "user-create-return";
        }catch (Exception e){
            return "400";
        }
    }

    @GetMapping("/users/delete")
    public String userDeleteForm(Model model) {
        model.addAttribute("user", new UserIdDto());
        return "user-delete";
    }

    @PostMapping("/users/delete")
    public String userDeleteSubmit(@Valid @ModelAttribute UserIdDto userIdDto, Model model) {
        try{
            for (var cat : catController.getCatService().findAll()){
                if (cat.getOwnerId().equals(userIdDto.getId()))
                    catController.getCatService().delete(CatMapper.mapToIdDto(CatMapper.mapToEntity(cat)));
            }
        }catch (Exception e){
            return "500";
        }
        try{
            userService.find(userIdDto.getId());
        }catch (Exception e){
            return "400";
        }
        try{
            var deletedUser = userService.delete(userIdDto);
            model.addAttribute("user", deletedUser);
            return "user-delete-return";
        }catch (Exception e){
            return "500";
        }
    }

    @GetMapping("/users/find_by_name/{name}")
    public String findUserIdsByName(@PathVariable() String name, Model model){
        try{
            List<UserDto> list = userService.findAll(new Name(name));
            model.addAttribute("lists", list);
            return "user-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/users/find_by_dob/{date}")
    public String findUserIdsByDateOfBirth(@PathVariable() String date,  Model model){
        try{
            List<UserDto> list = userService.findAll(LocalDate.parse(date));
            model.addAttribute("lists", list);
            return "user-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/users/change_user")
    public String userChangeForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user-change";
    }

    @PostMapping("/users/change_user")
    public String userChangeSubmit(@Valid @ModelAttribute UserDto user, Model model) {
        try{
            var userOld = userService.find(user.getId());

            if (user.getName() != null)
                userOld.setName(user.getName());

            if (user.getDateOfBirth() != null)
                userOld.setDateOfBirth(user.getDateOfBirth());

            model.addAttribute("user", userService.save(userOld));
            return "user-change-return";
        }catch (Exception e){
            return "400";
        }
    }

    @GetMapping("/users/get_cats/{id}")
    public String getUserCats(@Valid @PathVariable() Integer id, Model model){
        try {
            userService.find(id);
        }catch (Exception e){
            return "404";
        }
        try{
            UserIdDto userIdDto = new UserIdDto(id);
            var list = catController.getCatService().findAllByOwner(userIdDto);
            model.addAttribute("lists", list);
            return "cat-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/cats/change_owner")
    public String changeCatOwnerForm(Model model) {
        model.addAttribute("cat", new TwoIdsDto());
        return "cat-change-owner";
    }

    @PostMapping("/cats/change_owner")
    public String changeCatOwnerSubmit(@Valid @ModelAttribute TwoIdsDto idsDto, Model model) {
        try{
            userService.find(idsDto.getCat2().getId());
            var cat = catController.getCatService().find(idsDto.getCat1().getId());
            cat.setOwnerId(idsDto.getCat2().getId());
            model.addAttribute("cat", catController.getCatService().save(cat));
            return "cat-change-owner-return";
        }catch (Exception e){
            return "400";
        }
    }

    @GetMapping("/cats/create")
    public String catCreateForm(Model model) {
        model.addAttribute("cat", new CatDto());
        return "cat-create";
    }

    @PostMapping("/cats/create")
    public String catCreateSubmit(@Valid @ModelAttribute CatDto catDto, Model model) {
        try{
            var savedCat = catController.getCatService().save(catDto);
            model.addAttribute("cat", savedCat);
            return "cat-create-return";
        }catch (Exception e){
            return "400";
        }
    }
}