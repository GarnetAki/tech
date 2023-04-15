package ru.soloviev.Controllers;

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
    @ResponseBody
    public List<String> getAllCats(){
        var listCats = catService.findAll();
        var answer = new ArrayList<String>();
        for (var cat : listCats){
            answer.add(CatMapper.mapToString(cat));
        }
        return answer;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getCatById(@PathVariable(required = false) Integer id){
        var cat = catService.find(id);
        return CatMapper.mapToString(cat);
    }

    @GetMapping("/delete")
    public String catDeleteForm(Model model) {
        model.addAttribute("cat", new CatIdDto());
        return "cat-delete";
    }

    @PostMapping("/delete")
    public String catDeleteSubmit(@ModelAttribute CatIdDto catIdDto, Model model) {
        var deletedCat = catService.delete(catIdDto);
        model.addAttribute("cat", deletedCat);
        return "cat-delete-return";
    }

    @GetMapping("/add_friendship")
    public String catAddFriendshipForm(Model model) {
        model.addAttribute("cat", new TwoIdsDto());
        return "cat-add-friendship";
    }

    @PostMapping("/add_friendship")
    public String catAddFriendshipSubmit(@ModelAttribute TwoIdsDto catIds) {
        catService.addFriend(catIds.getCat1(), catIds.getCat2());
        return "cat-add-friendship-return";
    }

    @GetMapping("/delete_friendship")
    public String catDeleteFriendshipForm(Model model) {
        model.addAttribute("cat", new TwoIdsDto());
        return "cat-delete-friendship";
    }

    @PostMapping("/delete_friendship")
    public String catDeleteFriendshipSubmit(@ModelAttribute TwoIdsDto catIds) {
        catService.removeFriend(catIds.getCat1(), catIds.getCat2());
        return "cat-delete-friendship-return";
    }

    @GetMapping("/find_cat_friends/{id}")
    @ResponseBody
    public List<String> getCatFriends(@PathVariable(required = false) Integer id){
        return catService.find(id).getFriends().stream().map(catIdDto -> CatMapper.mapToString(catService.find(catIdDto.getId()))).toList();
    }

    @GetMapping("/find_by_breed/{breed}")
    @ResponseBody
    public List<String> findCatIdsByBreed(@PathVariable(required = false) String breed){
        List<CatDto> list = catService.findAll(new Breed(breed));
        return list.stream().map(CatMapper::mapToString).toList();
    }

    @GetMapping("/find_by_color/{color}")
    @ResponseBody
    public List<String> findCatIdsByColor(@PathVariable(required = false) String color){
        List<CatDto> list = catService.findAll(Color.valueOf(color));
        return list.stream().map(CatMapper::mapToString).toList();
    }

    @GetMapping("/find_by_name/{name}")
    @ResponseBody
    public List<String> findCatIdsByName(@PathVariable(required = false) String name){
        List<CatDto> list = catService.findAll(new Name(name));
        return list.stream().map(CatMapper::mapToString).toList();
    }

    @GetMapping("/find_by_dob/{date}")
    @ResponseBody
    public List<String> findCatIdsByDateOfBirth(@PathVariable(required = false) String date){
        List<CatDto> list = catService.findAll(LocalDate.parse(date));
        return list.stream().map(CatMapper::mapToString).toList();
    }

    @GetMapping("/change_cat")
    public String catChangeForm(Model model) {
        model.addAttribute("cat", new CatDto());
        return "cat-change";
    }

    @PostMapping("/change_cat")
    public String catChangeSubmit(@ModelAttribute CatDto cat, Model model) {
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
    }

    public CatService getCatService(){
        return catService;
    }
}
