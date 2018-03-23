package com.example.luka.allnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    public static final String REQUEST_URL_BASE = "https://content.guardianapis.com/";
    public static final String SPORTS_APPEND = "sport?api-key=test";
    public static final String TECH_APPEND = "technology?api-key=test";
    public static final String ENTERTAINMENT_APPEND = "culture?api-key=test";
    public static final String EVERYTHING_APPEND = "news?api-key=test";
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar pb;
    private int rbSelection;
    LoaderManager.LoaderCallbacks<List<NewsItem>> loaderCallbacksObject = null;
    int started = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        RadioGroup group = findViewById(R.id.radiogroup);

        final ImageView interestImage = findViewById(R.id.interest_image);

        final ListView newsListView = findViewById(R.id.news_list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        pb = findViewById(R.id.progress_bar);

        pb.setVisibility(View.GONE);

        newsListView.setEmptyView(mEmptyStateTextView);

        final LoaderManager loaderManager = getLoaderManager();

        loaderCallbacksObject = this;

        if (isConnected) {

            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    Log.e("\n\nMainActivity.java"," OnCheckedChanged Has Fired\n\n");
                    switch (i){
                        case 1:
                            rbSelection = 1;
                            interestImage.setImageResource(R.drawable.sports);
                            pb.setVisibility(View.VISIBLE);
                            loaderManager.initLoader(1, null,loaderCallbacksObject);
                            System.out.println("sports");
                            break;

                        case 2:
                            rbSelection = 2;
                            interestImage.setImageResource(R.drawable.entertainment);
                            pb.setVisibility(View.VISIBLE);
                            loaderManager.initLoader(2, null,loaderCallbacksObject);
                            System.out.println("entertainment");
                            break;

                        case 3:
                            rbSelection = 3;
                            interestImage.setImageResource(R.drawable.tech);
                            pb.setVisibility(View.VISIBLE);
                            loaderManager.initLoader(3, null,loaderCallbacksObject);
                            System.out.println("tech");
                            break;

                        case 4:
                            rbSelection = 4;
                            interestImage.setImageResource(R.drawable.news);
                            pb.setVisibility(View.VISIBLE);
                            loaderManager.initLoader(4, null,loaderCallbacksObject);
                            System.out.println("news");
                            break;
                    }
                }
            });

        } else {
            //handles not having intenet access
            mEmptyStateTextView.setText("No internet");
            pb.setVisibility(View.GONE);
        }

        mAdapter = new NewsAdapter(this,new ArrayList<NewsItem>());
        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsItem current = (NewsItem) newsListView.getItemAtPosition(i);
                String url = current.getLinkUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //RADIOBUTTONS DO NOT LIKE BEING FLIPPED
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RadioGroup group = findViewById(R.id.radiogroup);
            rbSelection = group.getCheckedRadioButtonId();
            group.check(rbSelection);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            RadioGroup group = findViewById(R.id.radiogroup);
            rbSelection = group.getCheckedRadioButtonId();
            group.check(rbSelection);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {

        Log.e("\n\nMainActivity.java"," OnCreateLoader Has Fired\n\n");
        switch(i){
            case 1:
                return new NewsLoader(this,REQUEST_URL_BASE+SPORTS_APPEND);

            case 2:
                return new NewsLoader(this,REQUEST_URL_BASE+ENTERTAINMENT_APPEND);

            case 3:
                return new NewsLoader(this,REQUEST_URL_BASE+TECH_APPEND);

            case 4:
                return new NewsLoader(this,REQUEST_URL_BASE+EVERYTHING_APPEND);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {
        mAdapter.clear();

        pb.setVisibility(View.GONE);

        if (newsItems != null && !newsItems.isEmpty()) {
            mAdapter.addAll(newsItems);
        }

        mEmptyStateTextView.setText("No News");

        Log.e("\n\nMainActivity.java"," OnLoadFinished Has Fired\n\n");
        Log.e("\n\nMainActivity.java"," rbSleection: "+Integer.toString(rbSelection));
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        mAdapter.clear();
        Log.e("\n\nMainActivity.java"," OnLoaderReset Has Fired\n\n");
    }
}
