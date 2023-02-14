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
 * The Main Application:
 * This application accesses the Programming Quotes API to fetch the quote of the day and stores it in a JSON file.
 * It first checks if the file exists and has the quote for the current day.
 * If it does, it reads the quote from the file and displays it.
 * If not, it fetches the quote from the API, stores it in the file and displays it.
 * The application also handles errors while reading,
 * writing and parsing the JSON response from the API.
 */
public class Main {
     // get the File Path from the FileManager class
    private static final String FILE_PATH = FileManager.getFilePath();
    /**
     * Main class for accessing the Programming Quotes API for quote of the day
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            FileManager fileManager = new FileManager();
            Quote quote = fileManager.readQuoteFromFile();
            // Check if the file exists and is not empty
            File file = new File(FILE_PATH);
            if (quote != null) {
                String fileDate = quote.contents.quotes[0].date;
                String todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                if (fileDate.equals(todayDate)) {
                    System.out.println("From The .json File:");
                    System.out.println("Today's (" + quote.contents.quotes[0].date + ") Quote:");
                    System.out.println('"' + quote.contents.quotes[0].quote + '"');
                    System.out.println("By : " + quote.contents.quotes[0].author);
                }
                else {
                    System.out.println("Found: \n(" + quote.contents.quotes[0].date + ") Quote: ");
                    System.out.print('"'+quote.contents.quotes[0].quote+'"');
                    System.out.println("Updating the quote !!");
                    // Fetch the quote of the day from the API and rewrite the File
                    RequestManager requestManager = new RequestManager();
                    quote = requestManager.fetchQuoteOfTheDay();
                    fileManager.saveQuoteToFile(quote);
                    System.out.println("Today's (" + quote.contents.quotes[0].date + ") Quote:");
                    System.out.println('"' + quote.contents.quotes[0].quote + '"');
                    System.out.println("By : " + quote.contents.quotes[0].author);
                }
            }  else {
                // Fetch the quote of the day from the API
                RequestManager requestManager = new RequestManager();
                quote = requestManager.fetchQuoteOfTheDay();
                fileManager.saveQuoteToFile(quote);
                System.out.println("Today's (" + quote.contents.quotes[0].date + ") Quote:");
                System.out.println('"' + quote.contents.quotes[0].quote + '"');
                System.out.println("By : " + quote.contents.quotes[0].author);
            }
        } catch (JsonSyntaxException e) {
            System.out.println("The API response is not in the expected format. Please try again later.");
        } catch (Exception e) {
            System.out.println("An error occurred. Error message: " + e.getMessage());
        }
    }
}