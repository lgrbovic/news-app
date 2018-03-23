package com.example.luka.allnews;

/**
 * Created by Luka on 20-Mar-18.
 */

public class NewsItem {
    private String mTitle;
    private String mWriter;
    private String mTime;
    private String mLinkUrl;

    public NewsItem(String title,String writer,String time,String linkUrl){
        this.mTitle = title;
        this.mWriter = writer;
        this.mTime = time;
        this.mLinkUrl = linkUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getWriter() {
        return mWriter;
    }

    public String getTime() {
        return mTime;
    }

    public String getLinkUrl(){
        return  this.mLinkUrl;
    }
}
