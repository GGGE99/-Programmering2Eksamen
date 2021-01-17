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
import facades.DateFacade;
import facades.DogFacade;
import facades.SearchFacade;
import facades.UserFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
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
@Path("dog-breed")
public class BreedResource {

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
    @Produces(MediaType.APPLICATION_JSON)
    public String getBreeds() throws InterruptedException, ExecutionException, TimeoutException {
        HashMap<String, String> map = new HashMap();
        map.put("breeds", "https://dog-info.cooljavascript.dk/api/breed");
        Map<String, String> res = api.getRawData(map);
        return res.get("breeds");
    }

    @GET
    @Path("{breed}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBreed(@PathParam("breed") String breed) throws InterruptedException, ExecutionException, TimeoutException {
        HashMap<String, ArrayList<String>> map = new HashMap();
        map.put("breed", arrayMakker(null, "breed", "https://dog-info.cooljavascript.dk/api/breed/" + breed));
        map.put("fact", arrayMakker("facts", "breed", "https://dog-api.kinduff.com/api/facts"));
        map.put("image", arrayMakker(null, "breed", "https://dog-image.cooljavascript.dk/api/breed/random-image/" + breed));

        Map<String, String> res = api.getProcessedData(map);
        JsonObject json = new JsonObject();
        for (Map.Entry<String, String> entry : res.entrySet()) {
            JsonObject js = GSON.fromJson(entry.getValue(), JsonObject.class);
            for (Map.Entry<String, JsonElement> entry1 : js.entrySet()) {
                json.add(entry1.getKey(), entry1.getValue());
            }
        }
        
        searchFacade.addSearch(breed);
        return json.toString();
    }

    private ArrayList<String> arrayMakker(String identifier, String methode, String urls) {
        ArrayList<String> arr = new ArrayList();
        arr.add(identifier);
        arr.add(methode);
        arr.add(urls);
        return arr;
    }
}
