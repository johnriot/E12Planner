package com.steo.europlanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;

public class PlannerHomeActivity extends Activity implements OnClickListener {

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
        default:
            Toast.makeText(this, "Not implemented yet.", Toast.LENGTH_LONG).show();
        }
    }
}