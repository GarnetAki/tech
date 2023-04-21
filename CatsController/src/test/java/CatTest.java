import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.util.Assert;
import ru.soloviev.Application;

import java.util.Objects;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class CatTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void findUserByIdOk() throws Exception{
        var result = Objects.requireNonNull(mockMvc
                        .perform(get("/users/2"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getModelAndView())
                        .getModel().get("lists");

        Assert.isTrue(result.toString().contains("[UserDto(id=2, name=Petya, dateOfBirth=2001-02-20)]"), "");
    }

    @Test
    void findUserByIdNotOk() throws Exception{
        var result = Objects.requireNonNull(mockMvc
                        .perform(get("/users/1"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getModelAndView());

        Assert.isTrue(result.toString().contains("404"), "");
    }

    @Test
    void FindAllCats() throws Exception{
        var result = Objects.requireNonNull(mockMvc
                        .perform(get("/cats"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getModelAndView())
                .getModel().get("lists");

        Assert.isTrue(result.toString().contains("CatDto(id=237, name=Pozyomka, breed=Russian, color=White, dateOfBirth=2017-12-05, ownerId=2, friends=[CatIdDto(id=240)])"), "");
        Assert.isTrue(result.toString().contains("CatDto(id=243, name=Persik, breed=Fruit, color=Red, dateOfBirth=2021-12-12, ownerId=3, friends=[])"), "");
        Assert.isTrue(result.toString().contains("CatDto(id=240, name=Abrikos, breed=Fruit, color=Red, dateOfBirth=2002-02-02, ownerId=2, friends=[CatIdDto(id=237)])"), "");
    }

    @Test
    void wrongPath() throws Exception{
        Objects.requireNonNull(mockMvc
                .perform(get("/use"))
                .andExpect(status().is4xxClientError()));
    }
}
