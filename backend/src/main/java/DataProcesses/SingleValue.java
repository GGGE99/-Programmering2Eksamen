/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcesses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;

/**
 *
 * @author marcg
 */
public class SingleValue implements Processes {

    private static Gson GSON = new Gson();

    @Override
    public String process(String facts, String identifier) {
//        System.out.println(joke);
        if (identifier == null) {
            return facts;
        } else {
            JsonElement res = GSON.fromJson(facts, JsonObject.class).get(identifier);
            System.out.println(res + " : " + identifier);
            return res.toString();
        }
    }

}
