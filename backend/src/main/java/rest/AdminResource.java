/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import facades.APIFacade;
import facades.SearchFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import utils.EMF_Creator;

/**
 *
 * @author marcg
 */
@Path("admin")
public class AdminResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static ExecutorService es = Executors.newCachedThreadPool();
    private static Gson GSON = new Gson();
    private static APIFacade api = APIFacade.getUserFacade(es);
    private static SearchFacade searchFacade = SearchFacade.getSearchFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/populate")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String populateDB(@PathParam("breed") String breed) throws InterruptedException, ExecutionException, TimeoutException, IOException {

        return searchFacade.populateBreeds();
    }

    @GET
    @Path("/count")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCount() throws InterruptedException, ExecutionException, TimeoutException, IOException {

        return "" + searchFacade.countSearches();
    }

    @GET
    @Path("/count/{breed}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountForBreed(@PathParam("breed") String breed) throws InterruptedException, ExecutionException, TimeoutException, IOException {

        return "" + searchFacade.countSearchesForBreed(breed);
    }
}
