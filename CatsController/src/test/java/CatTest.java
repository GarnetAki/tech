import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.util.Assert;
import ru.soloviev.Application;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Models.Name;

import java.time.LocalDate;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class CatTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void findAllCats_0() throws Exception{
        var result = mockMvc.perform(get("/cats")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andReturn()
                .getModelAndView();

        assert result != null;
        Assert.isTrue(result.toString().contains("lists=[]"), "");
    }

    @Test
    void createUser() throws Exception{
        UserDto userDto = new UserDto();
        userDto.setName(new Name("Vasiliy"));
        userDto.setDateOfBirth(LocalDate.parse("2000-01-01"));
        mockMvc.perform(get("/users/create")
                        .contentType(MediaType.TEXT_HTML)
                        .flashAttr("userDto", userDto))
                    .andExpect(status().isOk());
    }

    @Test
    void wrongPath() throws Exception{
        mockMvc.perform(get("/use"))
                .andExpect(status().is4xxClientError());
    }
}
