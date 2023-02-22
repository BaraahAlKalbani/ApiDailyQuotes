package org.Quotes;

import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
public class FileManager {
    // The path of the file to store the quote of the day
    private static final String FILE_PATH = Paths.get("Data", "quote_of_the_day.json").toString();
    /**
     * Saves the Quote object to a JSON file
     * @param quote the Quote object to be saved
     */
    public static void saveQuoteToFile(Quote quote) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(quote, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Reads the Quote object from the JSON file
     * @return the Quote object read from the file
     */
    public static Quote readQuoteFromFile() {
        File file = new File(FILE_PATH);
        try {
            if (file.exists() && file.length() > 0) {
                String json = new String(java.nio.file.Files.readAllBytes(Paths.get(FILE_PATH)));
                Gson gson = new Gson();
                return gson.fromJson(json, Quote.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public static String getFilePath()
    {
        return FILE_PATH;
    }

}
