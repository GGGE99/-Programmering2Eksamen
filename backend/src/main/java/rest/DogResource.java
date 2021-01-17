/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTOs.DogDTO;
import DTOs.DogsDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.User;
import errorhandling.DatabaseException;
import errorhandling.DateException;
import errorhandling.InvalidInputException;
import facades.APIFacade;
import facades.DateFacade;
import facades.DogFacade;
import facades.UserFacade;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import utils.EMF_Creator;
import utils.Env;

/**
 *
 * @author marcg
 */
@Path("dog")
public class DogResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static ExecutorService es = Executors.newCachedThreadPool();
    private static Gson GSON = new Gson();
    private static UserFacade userFacade = UserFacade.getUserFacade(EMF);
    private static DogFacade dogFacade = DogFacade.getDogFacade(EMF);
    private static DateFacade dateFacade = DateFacade.getDateFacade("dd-MM-yyyy HH:mm:ss");

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getInfoForAll() {
//        return "{\"msg\":\"Hello anonymous\"}";
//    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String addDog(String req) throws IOException, InterruptedException, ExecutionException, TimeoutException, DatabaseException, ParseException, InvalidInputException, DateException {
        String thisuser = securityContext.getUserPrincipal().getName();
        User user = userFacade.findUser(thisuser);
        String name = GSON.fromJson(req, JsonObject.class).get("name").getAsString();
        String breed = GSON.fromJson(req, JsonObject.class).get("breed").getAsString();
        String date = JsonParser.parseString(req).getAsJsonObject().get("DateOfBirth").toString();
        Date newDate = dateFacade.getDate(date.substring(1, date.length() - 1));
        
        
        DogDTO dog = dogFacade.addDog(user, name, breed, newDate);

        if (dog.testValidValues()) {
            return GSON.toJson(dog);
        }
        return GSON.toJson(dog);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String getAllDogsFromUSer(String req) throws IOException, InterruptedException, ExecutionException, TimeoutException, DatabaseException, ParseException {
        String thisuser = securityContext.getUserPrincipal().getName();
        User user = userFacade.findUser(thisuser);

        DogsDTO dogsDTO = dogFacade.getAllDogsFromAUser(user);
        return GSON.toJson(dogsDTO);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String updateDog(String req) throws IOException, InterruptedException, ExecutionException, TimeoutException, DatabaseException, ParseException, InvalidInputException, DateException {
        String thisuser = securityContext.getUserPrincipal().getName();
        User user = userFacade.findUser(thisuser);
        DogDTO dog = null;
        Date newDate = null;
        try {
            dog = GSON.fromJson(req, DogDTO.class);
            String date = JsonParser.parseString(req).getAsJsonObject().get("dateOfBirth").toString();
            newDate = dateFacade.getDate(date.substring(1, date.length() - 1));
            dog.setDateOfBirth(newDate);
        } catch (Exception ex) {

        }
        if (dog.testValidValues()) {
            return GSON.toJson(dogFacade.updateDog(user, dog));
        }
        return GSON.toJson(dog);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Path("{id}")
    public String deleteDog(@PathParam("id") String id) throws IOException, InterruptedException, ExecutionException, TimeoutException, DatabaseException, ParseException, InvalidInputException, DateException {
        String thisuser = securityContext.getUserPrincipal().getName();
        User user = userFacade.findUser(thisuser);
        DogDTO dog = dogFacade.deleteDog(user, id);
       

        return GSON.toJson(dog);
    }
}
