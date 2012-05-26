package com.neoware.europlanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;
import com.steo.europlanner.R;

public class PlannerHomeActivity extends Activity implements OnClickListener {

    //private static final String fontName = "fonts/GoodDog.otf";
    private static final String fontName = "fonts/RockSalt.ttf";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        IMAdView adView = (IMAdView) findViewById(R.id.adView);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(true);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();

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
}
