package com.steo.europlanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PlannerHomeActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        //LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        //mainLayout.getBackground().setAlpha(100);

        LinearLayout factfileLayout =
                (LinearLayout)findViewById(R.id.factfile_button);
        factfileLayout.setOnClickListener(this);

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
        case R.id.venues_button:
        case R.id.factfile_button:
        default:
            Toast.makeText(this, "Not implemented yet.", Toast.LENGTH_LONG).show();
        }
    }
}