package ru.soloviev.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import ru.soloviev.Dao.CatDao;
import ru.soloviev.Dto.CatDto;
import ru.soloviev.Dto.CatIdDto;
import ru.soloviev.Dto.TwoIdsDto;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Mappers.CatMapper;
import ru.soloviev.Models.Breed;
import ru.soloviev.Models.Color;
import ru.soloviev.Models.Name;
import ru.soloviev.Models.Role;
import ru.soloviev.Security.JWT.JwtUtils;
import ru.soloviev.Services.AuthService;
import ru.soloviev.Services.CatService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/cats")
public class CatController {

    private final CatService catService;

    private final AuthService authService;

    private final JwtUtils jwtUtils;

    @Autowired
    public CatController(CatDao catDao, AuthService authService, JwtUtils jwtUtils){
        this.catService = new CatService(catDao);
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping()
    public List<CatDto> getAllCats(HttpServletRequest req){
        return filterById(req, getId(req), catService.findAll());
    }

    @GetMapping("/{id}")
    public CatDto getCatById(HttpServletRequest req, @Valid @PathVariable() Integer id){
        var tmp = catService.find(id);
        accessCheckById(req, tmp.getOwnerId());
        return tmp;
    }

    @PostMapping("/delete")
    public CatDto catDeleteSubmit(HttpServletRequest req, @Valid @RequestBody CatIdDto catIdDto) {
        var tmp = catService.find(catIdDto.getId());
        accessCheckById(req, tmp.getOwnerId());
        return catService.delete(catIdDto);
    }

    @PostMapping("/add_friendship")
    public List<CatDto> catAddFriendshipSubmit(HttpServletRequest req, @Valid @RequestBody TwoIdsDto catIds) {
        var tmp = catService.find(catIds.getCat1().getId());
        accessCheckById(req, tmp.getOwnerId());
        catService.find(catIds.getCat2().getId());
        catService.addFriend(catIds.getCat1(), catIds.getCat2());
        ArrayList<CatDto> list = new ArrayList<>();
        list.add(catService.find(catIds.getCat1().getId()));
        list.add(catService.find(catIds.getCat2().getId()));
        return list;
    }

    @PostMapping("/delete_friendship")
    public List<CatDto> catDeleteFriendshipSubmit(HttpServletRequest req, @Valid @RequestBody TwoIdsDto catIds) {
        var tmp = catService.find(catIds.getCat1().getId());
        accessCheckById(req, tmp.getOwnerId());
        catService.find(catIds.getCat2().getId());
        catService.removeFriend(catIds.getCat1(), catIds.getCat2());
        ArrayList<CatDto> list = new ArrayList<>();
        list.add(catService.find(catIds.getCat1().getId()));
        list.add(catService.find(catIds.getCat2().getId()));
        return list;
    }

    @GetMapping("/find_cat_friends/{id}")
    public List<CatDto> getCatFriends(HttpServletRequest req, @Valid @PathVariable() Integer id){
        return filterById(req, getId(req), catService.find(id).getFriends().stream().map(catIdDto -> catService.find(catIdDto.getId())).toList());
    }

    @GetMapping("/find_by_breed/{breed}")
    public List<CatDto> findCatIdsByBreed(HttpServletRequest req, @Valid @PathVariable() String breed){
        return filterById(req, getId(req), catService.findAll(new Breed(breed)));
    }

    @GetMapping("/find_by_color/{color}")
    public List<CatDto> findCatIdsByColor(HttpServletRequest req, @Valid @PathVariable() Color color){
        return filterById(req, getId(req), catService.findAll(color));
    }

    @GetMapping("/find_by_name/{name}")
    public List<CatDto> findCatIdsByName(HttpServletRequest req, @Valid @PathVariable() String name){
        return filterById(req, getId(req), catService.findAll(new Name(name)));
    }

    @GetMapping("/find_by_dob/{date}")
    public List<CatDto> findCatIdsByDateOfBirth(HttpServletRequest req, @Valid @PathVariable() LocalDate date){
        return filterById(req, getId(req), catService.findAll(date));
    }

    @PostMapping("/change")
    public CatDto catChangeSubmit(HttpServletRequest req, @RequestBody CatDto cat) {
            accessCheckById(req, cat.getOwnerId());
            var catOld = catService.find(cat.getId());

            if (cat.getName() != null)
                catOld.setName(cat.getName());

            if (cat.getColor() != null)
                catOld.setColor(cat.getColor());

            if (cat.getBreed() != null)
                catOld.setBreed(cat.getBreed());

            if (cat.getDateOfBirth() != null)
                catOld.setDateOfBirth(cat.getDateOfBirth());

            return catService.update(catOld);
    }

    public CatService getCatService(){
        return catService;
    }

    public Role getRole(HttpServletRequest request){
        if (jwtUtils.getJwtFromCookies(request) != null){
            String name = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            UserDto details = authService.loadUserByUsername(name);
            return details.getRole();
        }else {
            return Role.ADMIN;
        }
    }

    public Integer getId(HttpServletRequest request){
        if(jwtUtils.getJwtFromCookies(request) != null){
            String name = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));
            UserDto details = authService.loadUserByUsername(name);
            return details.getId();
        }else {
            return 1;
        }
    }

    public void accessCheckById(HttpServletRequest req, Integer id){
        if (getRole(req) != Role.ADMIN && !Objects.equals(id, getId(req)))
            throw new AccessDeniedException("Access was denied");
    }

    public List<CatDto> filterById(HttpServletRequest req, Integer id, List<CatDto> catDtoList){
        if (getRole(req) != Role.ADMIN)
            return catDtoList.stream().filter(catDto -> catDto.getOwnerId().equals(id)).toList();
        else
            return catDtoList;
    }
}
