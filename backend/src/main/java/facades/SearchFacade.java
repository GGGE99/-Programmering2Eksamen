/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entities.Breed;
import entities.Searches;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.Env;

/**
 *
 * @author marcg
 */
public class SearchFacade {

    private static EntityManagerFactory emf;
    private static SearchFacade instance;
    private static Env env = Env.GetEnv();
    private static ExecutorService es = Executors.newCachedThreadPool();
    private static Gson GSON = new Gson();
    private static APIFacade api = APIFacade.getUserFacade(es);

    public static SearchFacade getSearchFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SearchFacade();
        }
        return instance;
    }

    public void addSearch(String breed) {
        EntityManager em = emf.createEntityManager();
        System.out.println(breed);
        Breed b = em.find(Breed.class, breed);
        System.out.println(b);
        if (b != null) {
            em.getTransaction().begin();
            Searches search = new Searches(new Date());
            search.addBreed(b);
            em.persist(search);
            em.getTransaction().commit();
        } else {
//            em.getTransaction().begin();
//            em.persist(b);
//            em.getTransaction().commit();
        }
    }

    public String populateBreeds() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        int count = 0;
        EntityManager em = emf.createEntityManager();
        JsonArray json = GSON.fromJson(api.getRawDataSingleURL("https://dog-info.cooljavascript.dk/api/breed/"), JsonObject.class).getAsJsonArray("dogs");
        HashMap<String, ArrayList<String>> map = new HashMap();
        for (JsonElement jsonElement : json) {
            JsonObject obj = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                String val = entry.getValue().toString();
                val = val.substring(1, val.length() - 1);
                if (em.find(Breed.class, val) == null) {
                    map.put(val, arrayMakker("info", "jokes", "https://dog-info.cooljavascript.dk/api/breed/" + val));
                    count++;
                }
            }
        }

        Map<String, String> res = api.getProcessedData(map);
        em.getTransaction().begin();
        for (Map.Entry<String, String> entry : res.entrySet()) {
            em.persist(new Breed(entry.getKey(), entry.getValue()));
        }
        em.getTransaction().commit();

        if (count > 0) {
            return count + " breeds has been added to the database";
        } else {
            return "Could not find any new breeds";
        }

    }

    private ArrayList<String> arrayMakker(String identifier, String methode, String urls) {
        ArrayList<String> arr = new ArrayList();
        arr.add(identifier);
        arr.add(methode);
        arr.add(urls);
        return arr;
    }
}
