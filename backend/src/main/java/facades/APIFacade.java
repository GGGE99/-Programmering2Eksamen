/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DataProcesses.Breed;
import DataProcesses.Processes;
import DataProcesses.SingleValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.persistence.EntityManagerFactory;
import rest.JokeResource;
import utils.Env;
import utils.FetchFinder;
import utils.HttpUtils;

/**
 *
 * @author marcg
 */
public class APIFacade {

    private static ExecutorService es;
    private static APIFacade instance;
    private static Env env = Env.GetEnv();

    private static Map<String, Processes> processes = new HashMap();

    public static APIFacade getUserFacade(ExecutorService _es) {
        if (instance == null) {
            es = _es;
            instance = new APIFacade();
            processes.put("jokes", new SingleValue());
            processes.put("breed", new Breed());
        }
        return instance;
    }

    public Map<String, String> getRawData(HashMap<String, String> URLS) throws InterruptedException, ExecutionException, TimeoutException {
        Map<String, FetchFinder> urls = new HashMap();
        for (Map.Entry<String, String> entry : URLS.entrySet()) {
            urls.put(entry.getKey(), new FetchFinder(entry.getValue()));

        }
        Map<String, Future<String>> futures = new HashMap();
        for (Map.Entry<String, FetchFinder> entry : urls.entrySet()) {
            fetchHandler jh = new fetchHandler(entry.getValue());
            futures.put(entry.getKey(), es.submit(jh));
        }

        Map<String, String> results = new HashMap();
        for (Map.Entry<String, Future<String>> entry : futures.entrySet()) {
            results.put(entry.getKey(), entry.getValue().get(10, TimeUnit.SECONDS));
        }

        return results;
    }

    public String getRawDataSingleURL(String URL) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        String ressult = HttpUtils.fetchData(URL);

        return ressult;
    }

    public Map<String, String> getProcessedData(HashMap<String, ArrayList<String>> URLS) throws InterruptedException, ExecutionException, TimeoutException {
        Map<String, String> data = new HashMap();
        for (Map.Entry<String, ArrayList<String>> entry : URLS.entrySet()) {
            data.put(entry.getKey(), entry.getValue().get(2));
        }
        Map<String, String> newData = getRawData((HashMap<String, String>) data);
        Map<String, String> results = new HashMap();
        for (Map.Entry<String, String> entry : newData.entrySet()) {
            String identifier = URLS.get(entry.getKey()).get(0);
            String methode = URLS.get(entry.getKey()).get(1);

            results.put(entry.getKey(), processes.get(methode).process(entry.getValue(), identifier));

        }

        return results;
    }

}

class fetchHandler implements Callable<String> {

    FetchFinder tc;

    fetchHandler(FetchFinder tc) {
        this.tc = tc;
    }

    @Override
    public String call() throws Exception {
        tc.get();
        return new String(tc.getJson());
    }
}
