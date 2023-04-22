package ru.soloviev.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.soloviev.Dao.CatDao;
import ru.soloviev.Dto.CatDto;
import ru.soloviev.Dto.CatIdDto;
import ru.soloviev.Dto.TwoIdsDto;
import ru.soloviev.Mappers.CatMapper;
import ru.soloviev.Models.Breed;
import ru.soloviev.Models.Color;
import ru.soloviev.Models.Name;
import ru.soloviev.Services.CatService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cats")
public class CatController {

    private final CatService catService;

    @Autowired
    public CatController(CatDao catDao){
        this.catService = new CatService(catDao);
    }

    @GetMapping()
    public String getAllCats(Model model){
        var list = catService.findAll();
        model.addAttribute("lists", list);
        return "cat-list";
    }

    @GetMapping("/{id}")
    public String getCatById(@Valid @PathVariable() Integer id, Model model){
        try{
            var cat = catService.find(id);
            var list = new ArrayList<>();
            list.add(cat);
            model.addAttribute("lists", list);
            return "cat-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/delete")
    public String catDeleteForm(Model model) {
        model.addAttribute("cat", new CatIdDto());
        return "cat-delete";
    }

    @PostMapping("/delete")
    public String catDeleteSubmit(@Valid @ModelAttribute CatIdDto catIdDto, Model model) {
        try{
            catService.find(catIdDto.getId());
        }catch (Exception e){
            return "400";
        }
        try{
            var deletedCat = catService.delete(catIdDto);
            model.addAttribute("cat", deletedCat);
            return "cat-delete-return";
        }catch (Exception e){
            return "500";
        }
    }

    @GetMapping("/add_friendship")
    public String catAddFriendshipForm(Model model) {
        model.addAttribute("cat", new TwoIdsDto());
        return "cat-add-friendship";
    }

    @PostMapping("/add_friendship")
    public String catAddFriendshipSubmit(@Valid @ModelAttribute TwoIdsDto catIds) {
        try{
            catService.find(catIds.getCat1().getId());
            catService.find(catIds.getCat2().getId());
        }catch (Exception e){
            return "400";
        }
        try{
            catService.addFriend(catIds.getCat1(), catIds.getCat2());
            return "cat-add-friendship-return";
        }catch (Exception e){
            return "500";
        }
    }

    @GetMapping("/delete_friendship")
    public String catDeleteFriendshipForm(Model model) {
        model.addAttribute("cat", new TwoIdsDto());
        return "cat-delete-friendship";
    }

    @PostMapping("/delete_friendship")
    public String catDeleteFriendshipSubmit(@Valid @ModelAttribute TwoIdsDto catIds) {
        try{
            catService.find(catIds.getCat1().getId());
            catService.find(catIds.getCat2().getId());
        }catch (Exception e){
            return "400";
        }
        try{
            catService.removeFriend(catIds.getCat1(), catIds.getCat2());
            return "cat-delete-friendship-return";
        }catch (Exception e){
            return "500";
        }
    }

    @GetMapping("/find_cat_friends/{id}")
    public String getCatFriends(@Valid @PathVariable() Integer id, Model model){
        try{
            var list = catService.find(id).getFriends().stream().map(catIdDto -> CatMapper.mapToString(catService.find(catIdDto.getId()))).toList();
            model.addAttribute("lists", list);
            return "cat-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/find_by_breed/{breed}")
    public String findCatIdsByBreed(@PathVariable() String breed, Model model){
        try{
            List<CatDto> list = catService.findAll(new Breed(breed));
            model.addAttribute("lists", list);
            return "cat-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/find_by_color/{color}")
    public String findCatIdsByColor(@Valid @PathVariable() String color, Model model){
        try{
            List<CatDto> list = catService.findAll(Color.valueOf(color));
            model.addAttribute("lists", list);
            return "cat-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/find_by_name/{name}")
    public String findCatIdsByName(@PathVariable() String name, Model model){
        try{
            List<CatDto> list = catService.findAll(new Name(name));
            model.addAttribute("lists", list);
            return "cat-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/find_by_dob/{date}")
    public String findCatIdsByDateOfBirth(@PathVariable() String date, Model model){
        try{
            List<CatDto> list = catService.findAll(LocalDate.parse(date));
            model.addAttribute("lists", list);
            return "cat-list";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/change_cat")
    public String catChangeForm(Model model) {
        model.addAttribute("cat", new CatDto());
        return "cat-change";
    }

    @PostMapping("/change_cat")
    public String catChangeSubmit(@Valid @ModelAttribute CatDto cat, Model model) {
        try{
            var catOld = catService.find(cat.getId());
            if (cat.getName() != null)
                catOld.setName(cat.getName());

            if (cat.getColor() != null)
                catOld.setColor(cat.getColor());

            if (cat.getBreed() != null)
                catOld.setBreed(cat.getBreed());

            if (cat.getDateOfBirth() != null)
                catOld.setDateOfBirth(cat.getDateOfBirth());

            model.addAttribute("cat", catService.save(catOld));
            return "cat-change-return";
        }catch (Exception e){
            return "400";
        }
    }

    public CatService getCatService(){
        return catService;
    }
}
