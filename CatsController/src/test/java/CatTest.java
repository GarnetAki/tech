import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import ru.soloviev.Application;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Models.Name;

import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CatTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    @Order(1)
    void registerAndLoginByAdmin() throws Exception{
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Admin\", \"password\": \"ww\", \"name\": \"Vasiliy\", \"dateOfBirth\": \"2003-01-01\", \"adminAccess\": \"YaAdminOtvechayu\"}"))
        ;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Admin\", \"password\": \"ww\"}")).andReturn();

        mockMvc.perform(post("/auth/logout")).andReturn();
    }

    @Test
    @Order(2)
    void createUser() throws Exception{

        mockMvc.perform(post("/users/create")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Vas\", \"password\": \"ww\", \"name\": \"Petr\", \"dateOfBirth\": \"2000-01-01\"}"))
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.name.name").value("Petr"));
    }

    @Test
    @Order(3)
    void findAllUsers() throws Exception{
        mockMvc.perform(get("/users")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name.name").value("Vasiliy"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name.name").value("Petr"));
    }

    @Test
    @Order(4)
    void findUsersByName() throws Exception{
        mockMvc.perform(get("/users/find_by_name/Petr")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name.name").value("Petr"));

        mockMvc.perform(get("/users/find_by_name/Abob")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(5)
    void findUsersByDateOfBirth() throws Exception{
        mockMvc.perform(get("/users/find_by_dob/2000-01-01")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name.name").value("Petr"));

        mockMvc.perform(get("/users/find_by_dob/2010-10-10")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "Admin", authorities = { "ADMIN" })
    void createCats() throws Exception{
        mockMvc.perform(post("/cats/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"0\", \"name\": \"Bob\", \"dateOfBirth\": \"2020-01-01\", \"breed\": \"Russkiy\", \"color\": \"Black\", \"ownerId\": \"1\"}"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name.name").value("Bob"));

        mockMvc.perform(post("/cats/create")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"0\", \"name\": \"Pyos\", \"dateOfBirth\": \"2021-11-19\", \"breed\": \"Sobaken\", \"color\": \"White\", \"ownerId\": \"1\"}"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name.name").value("Pyos"));

        mockMvc.perform(post("/cats/create")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"0\", \"name\": \"Kleopatr\", \"dateOfBirth\": \"2023-02-01\", \"breed\": \"Lisiy\", \"color\": \"Red\", \"ownerId\": \"2\"}"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name.name").value("Kleopatr"));
    }

    @Test
    @Order(7)
    void createFriendships() throws Exception{
        mockMvc.perform(post("/cats/add_friendship")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cat1\": {\"id\": 1}, \"cat2\": {\"id\": 2}}"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].friends[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].friends[0].id").value(1));

        mockMvc.perform(post("/cats/add_friendship")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cat1\": {\"id\": 1}, \"cat2\": {\"id\": 3}}"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].friends[1].id").isNumber())
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].friends[0].id").value(1));

        mockMvc.perform(post("/cats/add_friendship")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cat1\": {\"id\": 2}, \"cat2\": {\"id\": 3}}"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].friends[1].id").isNumber())
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].friends[1].id").isNumber());
    }

    @Test
    @Order(8)
    void deleteFriendships() throws Exception {
        mockMvc.perform(post("/cats/delete_friendship")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cat1\": {\"id\": 2}, \"cat2\": {\"id\": 3}}"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].friends", hasSize(1)))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].friends", hasSize(1)));
    }

    @Test
    @Order(9)
    void findCatById() throws Exception{
        mockMvc.perform(get("/cats/1")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name.name").value("Bob"));

        mockMvc.perform(get("/cats/4")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void findCatsByName() throws Exception{
        mockMvc.perform(get("/cats/find_by_name/Bob")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name.name").value("Bob"));

        mockMvc.perform(get("/cats/find_by_name/Abob")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(11)
    void findCatsByBreed() throws Exception{
        mockMvc.perform(get("/cats/find_by_breed/Sobaken")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name.name").value("Pyos"));

        mockMvc.perform(get("/cats/find_by_breed/Abob")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(12)
    void findCatsByDateOfBirth() throws Exception{
        mockMvc.perform(get("/cats/find_by_dob/2023-02-01")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name.name").value("Kleopatr"));

        mockMvc.perform(get("/cats/find_by_dob/2023-02-02")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(13)
    void findCatsByColor() throws Exception{
        mockMvc.perform(get("/cats/find_by_color/Black")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name.name").value("Bob"));
    }

    @Test
    @Order(14)
    void changeCatOwner() throws Exception{
        mockMvc.perform(post("/cats/change_owner")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cat1\": {\"id\": 2}, \"cat2\": {\"id\": 2}}"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name.name").value("Pyos"))
                .andExpect(jsonPath("$.ownerId").value(2));
    }

    @Test
    @Order(15)
    void getUserCats() throws Exception{
        mockMvc.perform(get("/users/get_cats/2")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(16)
    void getCatFriends() throws Exception{
        mockMvc.perform(get("/cats/find_cat_friends/2")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name.name").value("Bob"));
    }

    @Test
    @Order(17)
    void deleteUser() throws Exception{
        mockMvc.perform(post("/users/delete")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"2\"}"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name.name").value("Petr"));

        mockMvc.perform(get("/users")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name.name").value("Vasiliy"));
    }

    @Test
    @Order(18)
    void findAllCats_1() throws Exception{
        mockMvc.perform(get("/cats")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].friends", hasSize(0)));
    }

    @Test
    @Order(19)
    void deleteCat() throws Exception{
        mockMvc.perform(post("/cats/delete")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"1\"}"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name.name").value("Bob"));
    }

    @Test
    @Order(20)
    void findAllCats_2() throws Exception{
        mockMvc.perform(get("/cats")
                        .with(user("Admin").password("ww").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
