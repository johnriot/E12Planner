package com.neoware.europlanner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.neoware.europlanner.E12DataService.DataLoadedCallback;

public class PlannerHomeActivity extends E12ServiceActivity implements OnClickListener {

    //private static final String fontName = "fonts/GoodDog.otf";
    private static final String fontName = "fonts/RockSalt.ttf";
    private static final int FIFA_FEED = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        LinearLayout newsLayout =
                (LinearLayout)findViewById(R.id.news_button);
        newsLayout.setOnClickListener(this);

        LinearLayout teamsLayout =
                (LinearLayout)findViewById(R.id.teams_button);
        teamsLayout.setOnClickListener(this);

        LinearLayout venuesLayout =
                (LinearLayout)findViewById(R.id.venues_button);
        venuesLayout.setOnClickListener(this);

        LinearLayout gamesLayout =
                (LinearLayout)findViewById(R.id.games_button);
        gamesLayout.setOnClickListener(this);

        Typeface font = Typeface.createFromAsset(getAssets(), fontName);

        TextView newsTv = (TextView)findViewById(R.id.newsMenuTv);
        newsTv.setTypeface(font);

        TextView gamesTv = (TextView)findViewById(R.id.gamesMenuTv);
        gamesTv.setTypeface(font);

        TextView venuesTv = (TextView)findViewById(R.id.venuesMenuTv);
        venuesTv.setTypeface(font);

        TextView teamsTv = (TextView)findViewById(R.id.teamsMenuTv);
        teamsTv.setTypeface(font);
    }

    @Override
    public void onResume() {

        super.onResume();

        if(Settings.USE_LIVE_ADS) {
            AdView adview = (AdView)findViewById(R.id.adViewPlannerActivityHome);
            Assert.assertNotNull(adview);

            AdRequest req = new AdRequest();

            String [] keywords = getResources().getStringArray(R.array.adKeywords);
            Set<String> keywordsSet = new HashSet<String>(Arrays.asList(keywords));
            req.setKeywords(keywordsSet);

            adview.loadAd(req);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
        case R.id.games_button:

            Intent gamesIntent = new Intent(this, GamesActivity.class);
            startActivity(gamesIntent);

            break;
        case R.id.teams_button:

            Intent teamIntent = new Intent(this, TeamsActivity.class);
            startActivity(teamIntent);

            break;

        case R.id.news_button:

            Intent newsIntent = new Intent(this, NewsActivity.class);
            startActivity(newsIntent);

            break;
        case R.id.venues_button:
            Intent venuesIntent = new Intent(this, VenuesActivity.class);
            startActivity(venuesIntent);

            break;
        default:
            Toast.makeText(this, "Not implemented yet.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getSupportMenuInflater();
        menuInflater.inflate(R.layout.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.menu_about:

            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(R.string.aboutMenu);
                alertDialog.setMessage("v" + getPackageManager().getPackageInfo(
                        getPackageName(), 0).versionName + "\n\n"
                        +getResources().getString(R.string.aboutText));
                alertDialog.setButton(getResources().getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                });
                alertDialog.show();
            }
            catch(NameNotFoundException ex) { }

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Gets feeds either synchronously (in the case that we saved them from before)
     * or asynchronously (in the case that we are reading them now) and displays
     * them in a News Ticker across the screen.
     */
    private void getAndDisplayFeed() {

        if(TournamentDefinition.getTournamentDefnInstance(this).areFeedRssItemsSaved(FIFA_FEED)) {
            String newsTicker = makeTickerText();
            displayTickerText(newsTicker);
        }

        else {
            mBinder.loadNewsFeeds(new DataLoadedCallback() {

                @Override
                public void errorLoadingData(String error) {

                    displayTickerText(error);
                }


                @Override
                public void dataReady() {

                    if(TournamentDefinition.getTournamentDefnInstance(PlannerHomeActivity.this).areFeedRssItemsSaved(FIFA_FEED)) {
                        String newsTicker = makeTickerText();
                        displayTickerText(newsTicker);
                    }
                }
            });
        }
    }

    private String makeTickerText() {

        ArrayList<String> titles = FeedsReader.getAllTitles(FIFA_FEED, this);
        int titlesSize = titles.size();
        int randomPosition = (int) Math.floor((Math.random()*titles.size()));
        String newsTicker = getResources().getString(R.string.fifa_feed_lead) + "    ";

        // Write the string from a random point so that users will see a different
        // headline first each time.
        for(int i = randomPosition; i < titlesSize; i++) {
            newsTicker += titles.get(i) + "    ";
        }
        for(int i = 0; i < randomPosition; i++) {
            newsTicker += titles.get(i) + "    ";
        }

        newsTicker += getResources().getString(R.string.fifa_feed_tail);
        return newsTicker;
    }

    private void displayTickerText(String ticker) {
        TextView tickerTv = (TextView)findViewById(R.id.news_ticker);
        tickerTv.setText(ticker);
        tickerTv.setSelected(true);
        tickerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newsIntent = new Intent(PlannerHomeActivity.this, NewsActivity.class);
                startActivity(newsIntent);
            }
        });
    }

    @Override
    protected void onServiceConnected() {

            if(!TournamentDefinition.getTournamentDefnInstance(this).areFeedRssItemsSaved(FIFA_FEED)) {
                displayTickerText(getResources().getString(R.string.ticker_text));
                getAndDisplayFeed();
            }
            else {
                String tickerText = makeTickerText();
                displayTickerText(tickerText);
            }
    }

    @Override
    protected void onServiceDisconnected() {
        // TODO Auto-generated method stub
    }
}
