/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTOs.DogDTO;
import DTOs.DogsDTO;
import com.nimbusds.jose.shaded.json.JSONObject;
import entities.Dog;
import entities.Role;
import entities.User;
import facades.DogFacade;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author marcg
 */
public class DogResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static DogFacade facade;
    

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = DogFacade.getDogFacade(emf);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from Dog").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("user", "test");
            user.addRole(userRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);
            User both = new User("user_admin", "test");
            both.addRole(userRole);
            both.addRole(adminRole);
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

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;
    private static String count;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private static void getCount(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        count = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("count");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void testAddDog() {
        JSONObject req = new JSONObject();
        req.put("name", "Hansi");
        req.put("dateOfBirth", "11-12-2021 00:00:00");
        req.put("info", "hej");
        req.put("breed", "bulldog");

        login("user", "test");
        System.out.println(securityToken);
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(req.toJSONString())
                .when()
                .post("/dog").then()
                .statusCode(200)
                .body("name", equalTo("Hansi"))
                .body("DateOfBirth", equalTo("Dec 11, 2021, 12:00:00 AM"))
                .body("info", equalTo("hej"))
                .body("breed", equalTo("bulldog"));
    }

    @Test
    public void testGetAllDogsFromAUser() {
        EntityManager em = emf.createEntityManager();
        User u = em.find(User.class, "user");
        Dog dog = new Dog("Hans", new Date(), "Det ved jeg ikke helt", "bulldog");
        Dog jens = new Dog("Jens", new Date(), "Det ved jeg ikke helt", "bulldog");
        facade.addDog(u, new DogDTO(dog));
        facade.addDog(u, new DogDTO(jens));
        

        login("user", "test");
        System.out.println(securityToken);
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/dog").then()
                .statusCode(200)
                .body("dogsDTO[0].name", equalTo("Hans"))
                .body("dogsDTO[1].name", equalTo("Jens"));
    }
}
