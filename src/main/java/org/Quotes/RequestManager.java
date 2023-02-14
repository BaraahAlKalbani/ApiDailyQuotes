package org.Quotes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class RequestManager {
    /**
     * Fetches the quote of the day from the API and returns it as a Quote object
     * @return the Quote object obtained from the API call
     * @throws RuntimeException if there is an error while executing the API call
     */
    public static Quote fetchQuoteOfTheDay() {
        // Fetch the quote of the day from the API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://quotes.rest/qod.json")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(json, Quote.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
