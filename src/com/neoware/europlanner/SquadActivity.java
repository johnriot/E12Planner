package com.neoware.europlanner;

import junit.framework.Assert;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.neoware.europlanner.Player.PlayerPositionException;
import com.steo.europlanner.R;

public class SquadActivity extends FragmentActivity {

    public static final String TEAM_INDEX = "team_indx";
    SquadDefinition mSquadDefn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.squad_layout);

        ImageButton homeButton = (ImageButton)findViewById(R.id.squadHomeButton);
        homeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        int teamIndx = 0;
        if(extras != null) {
            teamIndx = extras.getInt(TEAM_INDEX);
        }
        mSquadDefn = new SquadDefinition(this, teamIndx);

        Resources res = this.getResources();
        String packageName = getClass().getPackage().getName();

        // for every player in the squad
        for(int i = 0; i < mSquadDefn.getSquad().getSize(); ++i) {
            String iconViewIdStr = "player" + i + "Icon";
            int iconViewId = res.getIdentifier(iconViewIdStr, "id", packageName);
            ImageView playerIconView = (ImageView)findViewById(iconViewId);

            try {
                Player player = mSquadDefn.getSquad().getPlayer(i);
                String playerIcon = player.getPositionFilename();
                playerIconView.setImageResource((res.getIdentifier(playerIcon,
                        "drawable", packageName)));

                String playerNameViewIdStr = "player" + i + "Name";
                int playerNameViewId = res.getIdentifier(playerNameViewIdStr, "id", packageName);
                TextView playerNameView = (TextView)findViewById(playerNameViewId);

                playerNameView.setText(player.getName());

                String playerNumberViewIdStr = "player" + i + "Number";
                int playerNumberViewId = res.getIdentifier(playerNumberViewIdStr, "id", packageName);
                TextView playerNumberView = (TextView)findViewById(playerNumberViewId);

                playerNumberView.setText(player.getNumber());

                String playerAgeViewIdStr = "player" + i + "Age";
                int playerAgeViewId = res.getIdentifier(playerAgeViewIdStr, "id", packageName);
                TextView playerAgeView = (TextView)findViewById(playerAgeViewId);

                Integer playerAge = player.getAge();
                playerAgeView.setText(playerAge.toString());

            } catch (PlayerPositionException ex) {
                Assert.fail("Squad XML data corrupt: " + ex.getMessage());
            }
        }
    }
}
