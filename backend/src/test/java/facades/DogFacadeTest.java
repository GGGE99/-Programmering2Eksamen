/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOs.DogDTO;
import DTOs.DogsDTO;
import DTOs.UserDTO;
import DTOs.UsersDTO;
import entities.Breed;
import entities.Dog;
import entities.Role;
import entities.User;
import errorhandling.DatabaseException;
import errorhandling.InvalidInputException;
import java.text.ParseException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 *
 * @author marcg
 */
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class DogFacadeTest {

    private static EntityManagerFactory emf;
    private static DogFacade facade;
    public static final DateFacade dateFacade = DateFacade.getDateFacade("dd-MM-yyyy HH:mm:ss");

//    private static String pass = "1234";
//    private static User user = new User("Test", pass);
//    private static User both = new User("user_admin", pass);
    private static Breed breed = new Breed("bulldog", "Det ved jeg ikke helt");

//    private static Role userRole = new Role("user");
//    private static Role adminRole = new Role("admin");
//    public DogFacadeTest() {
//    }
    @BeforeAll
    public static void setUpClass() throws ParseException {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = DogFacade.getDogFacade(emf);
        EntityManager em = emf.createEntityManager();

//        facade.addDog(em.find(User.class, "Test"), new DogDTO(dog));
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            User admin = new User("admin", "test");
            User both = new User("user_admin", "test");
            User user = new User("user", "test");
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from Dog").executeUpdate();
            em.createQuery("delete from Breed").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            System.out.println(user.getUserName());
            
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");

            Breed breed = new Breed("bulldog", "Det ved jeg ikke helt");

            Dog dog = new Dog("Hans", new Date());
            Dog jens = new Dog("Jens", new Date());

            dog.addBreed(breed);
            jens.addBreed(breed);

            dog.addUser(user);
            jens.addUser(user);

            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);
            
            em.persist(breed);
            
            em.persist(dog);
            em.persist(jens);

            em.persist(userRole);
            em.persist(adminRole);
            
            em.persist(user);
            em.persist(admin);
            em.persist(both);

            //System.out.println("Saved test data to database");
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @Test
    public void testAddDog() throws AuthenticationException, ParseException {
        EntityManager em = emf.createEntityManager();
        User u = em.find(User.class, "user");
        Dog dog = new Dog("Jens", dateFacade.getDate("10-04-2015 00:00:00"));
        dog.addBreed(breed);
        DogDTO actual = new DogDTO(dog);
        DogDTO expected = facade.addDog(u, actual);
        System.out.println(expected.getName());
        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    public void testGetAllUsers() throws AuthenticationException, ParseException {
        EntityManager em = emf.createEntityManager();
        User u = em.find(User.class, "user");

//        facade.addDog(u, new DogDTO(dog));
//        facade.addDog(u, new DogDTO(jens));
        DogsDTO list = facade.getAllDogsFromAUser(u);
        int actual = list.getUsersDTO().size();
        int expected = 2;
        assertEquals(actual, expected);
    }

}
