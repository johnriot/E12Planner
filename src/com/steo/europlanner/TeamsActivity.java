package com.steo.europlanner;

import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class TeamsActivity extends FragmentActivity {

    //Link for quick action menu: http://www.londatiga.net/it/how-to-create-quickaction-dialog-in-android/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.teams_layout);

        ImageButton homeButton = (ImageButton)findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String teams[] = this.getResources().getStringArray(R.array.team_names);
        Arrays.sort(teams);

        ListView teamsListView = (ListView)findViewById(R.id.teamsListView);

        //temporary simple string adapter / custom to come
        ArrayAdapter<String> teamsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, teams);
        teamsListView.setAdapter(teamsAdapter);
    }
}
