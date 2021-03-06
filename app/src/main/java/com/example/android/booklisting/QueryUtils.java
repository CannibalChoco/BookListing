package com.example.android.booklisting;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;


/**
 * Helper methods to request and receive book data form Google Books
 */

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();
    public static final int URL_READ_TIMEOUT = 10000; /* milliseconds */
    public static final int URL_CONNECT_TIMEOUT = 15000; /* milliseconds */
    public static final int URL_RESPONSE_OK = 200;
    public static final int MORE_THAN_ONE_AUTHOR = 1;


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the GoogleBooks dataset and return a list of {@link List<Book>} objects
     */
    public static List<Book> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create list of {@link Book} objects
        List<Book> books = extractBooksFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(URL_READ_TIMEOUT);
            urlConnection.setConnectTimeout(URL_CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == URL_RESPONSE_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Book> extractBooksFromJson(String bookJsonResponse) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root = new JSONObject(bookJsonResponse);
            JSONArray bookArray = root.getJSONArray("items");

            for (int i = 0, n = bookArray.length(); i < n; i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = "";
                String description = "";
                String previewLink = "";
                String author = "";

                if(volumeInfo.has("title")){
                    title = volumeInfo.getString("title");
                }

                if(volumeInfo.has("description")){
                    description = volumeInfo.getString("description");
                }

                if(volumeInfo.has("previewLink")){
                    previewLink = volumeInfo.getString("previewLink");
                }

                if(volumeInfo.has("authors")){
                    JSONArray authorArray = volumeInfo.getJSONArray("authors");


                    for (int j = 0, authorCount = authorArray.length(); j < authorCount; j++) {
                        author = author + authorArray.getString(j);

                        if (authorCount > MORE_THAN_ONE_AUTHOR && j !=
                                (authorCount - MORE_THAN_ONE_AUTHOR)) {
                            author += ", ";
                        }
                    }
                }

                books.add(new Book(author, title, description, previewLink));
                String data = books.toString();
                Log.v("extractBooks", data);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }
}
