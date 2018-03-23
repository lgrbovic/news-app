package com.example.luka.allnews;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luka on 21-Mar-18.
 */

public final class QueryUtils {

    public static String LOG_TAG = "QueryUtils.java";

    private QueryUtils(){

    }

    public static URL createURL(String urlString){
        URL url = null;

        try {
            url=new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Failed to create URL object");
        }

        return url;
    }

    public static String makeHTTPRequest(URL url) throws IOException {
        String responseJSON = "";

        if(url == null){
            return null;
        }

        HttpURLConnection connection = null;
        InputStream inStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == 200){
                inStream = connection.getInputStream();
                responseJSON = readFromInStream(inStream);
            }
            else{
                Log.e(LOG_TAG,"Network request failed with response code: "+responseCode);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Failed to connect");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inStream != null) {
                inStream.close();
            }
        }

        return responseJSON;
    }

    private static String readFromInStream(InputStream inStream) throws IOException {
        StringBuilder returnString = new StringBuilder();

        if(inStream !=null){
            InputStreamReader isr = new InputStreamReader(inStream);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();

            while (line!=null){
                returnString.append(line);
                line = br.readLine();
            }
        }

        return returnString.toString();
    }

    private static List<NewsItem> readJSON(String inputJSON){
        if(TextUtils.isEmpty(inputJSON)){
            return null;
        }

        List<NewsItem> news = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(inputJSON);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for(int i=0;i<results.length();i++){
                JSONObject current = results.getJSONObject(i);
                String webPublicationDate = current.getString("webPublicationDate");
                String webTitle = current.getString("webTitle");
                String webUrl = current.getString("webUrl");

                String[] dateAndTimeDirect = webPublicationDate.split("T");
                String date = dateAndTimeDirect[0];
                String time = dateAndTimeDirect[1];
                time = time.substring(0,time.length()-1);
                String dateAndTime = date+" "+time;

                String title;
                String writer = "";
                if(webTitle.contains("|")){
                    String[] titleAndWriter = webTitle.split(" \\| ");
                    title = titleAndWriter[0].trim();
                    writer = titleAndWriter[1].trim();

                    Log.e("title = ",title);
                    Log.e("writer = ",writer);
                }
                else{
                    title = webTitle;
                }

                Log.e(LOG_TAG,webUrl);
                news.add(new NewsItem(title,writer,dateAndTime,webUrl));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG,"Failed to extract from JSON");
        }
        return news;
    }

    public static List<NewsItem> returnNewsList(String urlString){
        URL url = createURL(urlString);

        String responseJSON = null;
        try {
            responseJSON = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP Request Failed");
        }

        List<NewsItem> news = readJSON(responseJSON);

        return news;
    }

}
