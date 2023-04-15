/*@SpringBootTest
@EntityScan(basePackages = {"ru.soloviev.Entities"} )
@EnableJpaRepositories(basePackages = {"ru.soloviev.Dao"})*/
public class CatTest {
    /*@Autowired
    private static UserDao userDao;

    @Autowired
    private static CatDao catDao;
    private static User user;
    private static Cat cat;
    private static Cat cat2;

    @BeforeAll
    public static void setUp() {
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
        when(userDao.findById(user.getId()).get()).thenReturn(user);
        when(catDao.findById(cat.getId()).get()).thenReturn(cat);

        assertEquals("Abob Petrovich", userDao.findById(user.getId()).get().getName().getName());
        assertEquals("Chel", catDao.findById(cat.getId()).get().getCatName().getName());
    }

    @Test
    public void friendTest(){
        when(catDao.findById(cat.getId()).get()).thenReturn(cat);
        when(catDao.findById(cat2.getId()).get()).thenReturn(cat2);

        cat.getFriends().add(cat2);
        cat2.getFriends().add(cat);

        assertEquals(1, catDao.findById(cat.getId()).get().getFriends().size());
        assertEquals(1, catDao.findById(cat2.getId()).get().getFriends().size());
    }

    @Test
    public void ownerTest(){
        var userDto = UserMapper.mapToDto(user);

        when(catDao.findById(cat.getId()).get()).thenReturn(cat);
        when(userDao.findById(user.getId()).get()).thenReturn(user);
        
        cat.setOwner(userDto.getId());

        assertEquals(userDto.getId(), catDao.findById(cat.getId()).get().getOwner());
    }*/
}
