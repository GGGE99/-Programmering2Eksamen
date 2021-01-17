/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcesses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author marcg
 */
public class Breed implements Processes {

    private static Gson GSON = new Gson();

    @Override
    public String process(String joke, String identifier) {
//        System.out.println(joke);
        if (identifier == null) {
            System.out.println(joke + " : " + "dasdadasd");
            return joke;
        } 
        System.out.println(joke);
        JsonElement ele = GSON.fromJson(joke, JsonObject.class).getAsJsonArray(identifier).get(0);
        JsonObject res = new JsonObject();
        res.add(identifier, ele);
        System.out.println(res + " : " + identifier);
        return res.toString();
    }
}
