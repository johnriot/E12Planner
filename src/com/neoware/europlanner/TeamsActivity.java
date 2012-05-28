package com.neoware.europlanner;

import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class TeamsActivity extends SherlockFragmentActivity {

    //Link for quick action menu: http://www.londatiga.net/it/how-to-create-quickaction-dialog-in-android/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.teams_layout);

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.toolbar_bg);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(bg);
        bar.setTitle(R.string.teams_title);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayUseLogoEnabled(true);

        final String teams[] = this.getResources().getStringArray(R.array.team_names);
        //Arrays.sort(teams); // Removing sort as unsorted list preserves the position = id equivalence

        ListView teamsListView = (ListView)findViewById(R.id.teamsListView);

        //temporary simple string adapter / custom to come
        ArrayAdapter<String> teamsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, teams);
        teamsListView.setAdapter(teamsAdapter);

        teamsListView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                Intent squadIntent = new Intent(TeamsActivity.this, SquadActivity.class);
                squadIntent.putExtra(SquadActivity.TEAM_INDEX, position);
                startActivity(squadIntent);
            }
        });
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
}
