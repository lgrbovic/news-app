package com.example.luka.allnews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Luka on 21-Mar-18.
 */

public class NewsLoader extends AsyncTaskLoader <List<NewsItem>>{

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.e("\n\nNewsLoader.java"," OnStartLoading Has Fired\n\n");
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        Log.e("\n\nNewsLoader.java"," LoadInBackground Has Fired\n\n");
        if (mUrl == null) {
            return null;
        }

        Log.e("anything really","Yah");

        List<NewsItem> news = QueryUtils.returnNewsList(mUrl);
        return news;
    }
}
