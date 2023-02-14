package org.Quotes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The Main Application
 */
public class Main {
    /**
     * The path of the file to store the quote of the day
     */
    private static final String FILE_PATH = Paths.get("Data", "quote_of_the_day.json").toString();
    /**
     * Main class for accessing the Programming Quotes API for quote of the day
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            // Check if the file exists and is not empty
            File file = new File(FILE_PATH);
            if (file.exists() && file.length() > 0) {
                // Read the quote of the day from the file
                String json = new String(java.nio.file.Files.readAllBytes(Paths.get(FILE_PATH)));
                Gson gson = new Gson();
                Quote quote = gson.fromJson(json, Quote.class);
                String fileDate = quote.contents.quotes[0].date;
                String todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                if (fileDate.equals(todayDate)) {
                    System.out.println("From The .json File:");
                    System.out.println("Today's (" + quote.contents.quotes[0].date + ") Quote:");
                    System.out.println('"'+quote.contents.quotes[0].quote+'"');
                    System.out.println("By : " + quote.contents.quotes[0].author);
                }
                else {
                    System.out.println("Found: \n(" + quote.contents.quotes[0].date + ") Quote: ");
                    System.out.print('"'+quote.contents.quotes[0].quote+'"');

                    System.out.println("Updating the quote !!");
                    // Delete the file
                    file.delete();
                    // Fetch the quote of the day from the API
                    getResponseFromApi();
                }
            } else {
                // Fetch the quote of the day from the API
                getResponseFromApi();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing the file. Error message: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.out.println("The API response is not in the expected format. Please try again later.");
        } catch (Exception e) {
            System.out.println("An error occurred. Error message: " + e.getMessage());
        }
    }
    /**
     * Method for saving the response of the API call to a JSON file
     * @param request the request object for the API call
     * @param client the OkHttpClient object for executing the API call
     * @return the Quote object obtained from the API call
     */
    public static Quote saveResponse(Request request, OkHttpClient client) {
        try (Response response = client.newCall(request).execute()) {
            // Get the JSON response from the API
            String json = response.body().string();
            // Create a Gson object to parse the JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            // Convert the JSON to a `Quote` object
            Quote quote = gson.fromJson(json, Quote.class);

            // Write the `Quote` object to a file
            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                gson.toJson(quote, writer);
            }
            // Return the `Quote` object
            return quote;
        } catch (IOException e) {
            // Throw a runtime exception in case of an Error
            throw new RuntimeException(e);
        }
    }

    /**
     *  Fetch the quote of the day from the API and save it into a json file
     */
    public static void getResponseFromApi()
    {
        // Fetch the quote of the day from the API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://quotes.rest/qod.json")
                .get()
                .build();
        Quote quote = saveResponse(request, client);
        System.out.println("From The API:");
        // Print the quote of the day
        System.out.println("Today's ("+quote.contents.quotes[0].date +") Quote:");
        System.out.println(quote.contents.quotes[0].quote);
        System.out.println("By : "+quote.contents.quotes[0].author);
    }
}
