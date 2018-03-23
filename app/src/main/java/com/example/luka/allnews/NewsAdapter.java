package com.example.luka.allnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Luka on 20-Mar-18.
 */

public class NewsAdapter extends ArrayAdapter<NewsItem> {
    public NewsAdapter(@NonNull Context context, ArrayList<NewsItem> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentView = convertView;

        if(currentView == null){
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);
        }

        TextView title = currentView.findViewById(R.id.title);
        TextView writer = currentView.findViewById(R.id.writer);
        TextView time = currentView.findViewById(R.id.time);

        NewsItem currentItem = getItem(position);

        title.setText(currentItem.getTitle());
        writer.setText(currentItem.getWriter());
        time.setText(currentItem.getTime());

        return currentView;
    }
}
