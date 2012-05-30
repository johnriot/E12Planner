package com.neoware.europlanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;
import com.neoware.europlanner.Player.PlayerPositionException;

public class SquadActivity extends SherlockFragmentActivity {

    public static final String TEAM_INDEX = "team_indx";
    SquadDefinition mSquadDefn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.squad_layout);

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.toolbar_bg);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

        final String teams[] = this.getResources().getStringArray(R.array.team_names);
        Bundle extras = getIntent().getExtras();
        int teamIndx = 0;
        if(extras != null) {
            teamIndx = extras.getInt(TEAM_INDEX);
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(bg);
        bar.setTitle(teams[teamIndx]);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayUseLogoEnabled(true);

        IMAdView adView = (IMAdView) findViewById(R.id.adViewTeams);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(true);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();

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
                HandleException(ex);
            }
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if(item.getItemId() == android.R.id.home) {

            Intent homeIntent = new Intent(this, PlannerHomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void HandleException(PlayerPositionException ex) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.server_error_title);
        alertDialogBuilder
            .setMessage(R.string.server_error_body)
            .setCancelable(true)
            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,int id) {
                    dialog.cancel();
                }
              });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
