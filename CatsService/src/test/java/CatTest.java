import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.soloviev.Dao.CatDao;
import ru.soloviev.Dao.UserDao;
import ru.soloviev.Entities.Cat;
import ru.soloviev.Entities.User;
import ru.soloviev.Models.Breed;
import ru.soloviev.Models.Color;
import ru.soloviev.Models.Name;
import ru.soloviev.Mappers.UserMapper;


import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CatTest {
    private static UserDao userDao;
    private static CatDao catDao;
    private static User user;
    private static Cat cat;
    private static Cat cat2;

    @BeforeAll
    public static void setUp() {
        userDao = mock(UserDao.class);
        catDao = mock(CatDao.class);

        user = new User();
        user.setId(1);
        user.setDateOfBirth(LocalDate.parse("2000-11-11"));
        user.setName(new Name("Abob Petrovich"));

        cat = new Cat();
        cat.setCatName(new Name("Chel"));
        cat.setColor(Color.Black);
        cat.setBreed(new Breed("Englishman"));
        cat.setDateOfBirth(LocalDate.parse("2019-06-06"));
        cat.setFriends(new HashSet<>());

        cat2 = new Cat();
        cat2.setCatName(new Name("Chuvachok"));
        cat2.setColor(Color.White);
        cat2.setBreed(new Breed("Russkiy"));
        cat2.setDateOfBirth(LocalDate.parse("2019-03-06"));
        cat2.setFriends(new HashSet<>());
    }

    @Test
    public void searchTest() {
        when(userDao.find(user.getId())).thenReturn(user);
        when(catDao.find(cat.getId())).thenReturn(cat);

        assertEquals("Abob Petrovich", userDao.find(user.getId()).getName().getName());
        assertEquals("Chel", catDao.find(cat.getId()).getCatName().getName());
    }

    @Test
    public void friendTest(){
        when(catDao.find(cat.getId())).thenReturn(cat);
        when(catDao.find(cat2.getId())).thenReturn(cat2);

        cat.getFriends().add(cat2);
        cat2.getFriends().add(cat);

        assertEquals(1, catDao.find(cat.getId()).getFriends().size());
        assertEquals(1, catDao.find(cat2.getId()).getFriends().size());
    }

    @Test
    public void ownerTest(){
        var userDto = UserMapper.mapToDto(user);

        when(catDao.find(cat.getId())).thenReturn(cat);
        when(userDao.find(user.getId())).thenReturn(user);
        
        cat.setOwner(userDto.getId());

        assertEquals(userDto.getId(), catDao.find(cat.getId()).getOwner());
    }
}
