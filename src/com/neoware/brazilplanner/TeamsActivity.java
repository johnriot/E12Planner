package com.neoware.brazilplanner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.neoware.brazilplanner.E12DataService.DataLoadedCallback;

public class TeamsActivity extends E12ServiceActivity {

    private static final int TEAMS_PER_GROUP = 4;

    private ProgressDialog mProgressDialog;

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

        final String teams[] = getResources().getStringArray(R.array.team_names);
        final String groups[] = getResources().getStringArray(R.array.groups);

        ListView teamsListView = (ListView) findViewById(R.id.teamsListView);
        teamsListView.setAdapter(new TeamsAdapter(teams, groups));

        teamsListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                RowTag tag = (RowTag) view.getTag();
                if(tag.isTeam) {
                    Intent squadIntent = new Intent(TeamsActivity.this, SquadActivity.class);
                    squadIntent.putExtra(SquadActivity.TEAM_INDEX, tag.teamID);
                    startActivity(squadIntent);
                }
                else if(tag.isHeader) {
                    Intent gamesIntent = new Intent(TeamsActivity.this, GamesActivity.class);
                    gamesIntent.putExtra(GamesActivity.GROUP_KNOCKOUT, tag.groupID);
                    startActivity(gamesIntent);
                }
            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();

        if(Settings.USE_LIVE_ADS) {
            AdView adview = (AdView)findViewById(R.id.adViewTeams);
            Assert.assertNotNull(adview);

            AdRequest req = new AdRequest();

            String [] keywords = getResources().getStringArray(R.array.adKeywords);
            Set<String> keywordsSet = new HashSet<String>(Arrays.asList(keywords));
            req.setKeywords(keywordsSet);

            adview.loadAd(req);
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

    private class TeamsAdapter extends BaseAdapter{

        private final String [] mTeams;
        private final String [] mGroups;
        private final String [] mCrestIds;

        public TeamsAdapter(String[] teams, String[] groups) {
            mTeams = teams;
            mGroups = groups;
            mCrestIds = getResources().getStringArray(R.array.team_icon_resource_names);
        }

        @Override
        public View getView(int position, View recycledRow, ViewGroup parent) {
            //TODO: Not using recycled row as it isn't always the right type
            View row = null;
            LayoutInflater inflater = (TeamsActivity.this).getLayoutInflater();

            String packageName = getClass().getPackage().getName();

            RowTag tag = new RowTag();

            if(isHeader(position)) {
                row = inflater.inflate(R.layout.teams_group_header, null);
                TextView teamTv = (TextView) row.findViewById(R.id.teamsGroup);
                teamTv.setText(mGroups[position / (TEAMS_PER_GROUP + 1)]);

                tag.isHeader = true;
                tag.groupID = position/ (TEAMS_PER_GROUP + 1);
            }
            else {
                row = inflater.inflate(R.layout.teams_list_item, null);
                TextView teamTv = (TextView) row.findViewById(R.id.teamsTableItem);
                teamTv.setText(getTeam(position));

                tag.isTeam = true;
                tag.teamID = getTeamId(position);

                ImageView teamIcon = (ImageView) row.findViewById(R.id.teamIcon);
                teamIcon.setImageResource(getResources().getIdentifier(mCrestIds[getTeamId(position)],
                        "drawable", packageName));
            }

            row.setTag(tag);
            row.setFocusable(false);
            row.setFocusableInTouchMode(false);
            //row.setClickable(true);

            return row;
        }

        private boolean isHeader(int position) {
            return position % (TEAMS_PER_GROUP + 1) == 0;
        }

        private String getTeam(int position) {
            return mTeams[getTeamId(position)];
        }

        private int getTeamId(int position) {
            int group = position /  (TEAMS_PER_GROUP + 1);
            return position - group - 1;
        }

        @Override
        public boolean areAllItemsEnabled() {
            //TODO: This should be toggleable from blue box
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public int getCount() {
            return mTeams.length + mGroups.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    private class RowTag {
        public boolean isHeader = false;
        public boolean isTeam = false;
        public int teamID = 0;
        public int groupID = 0;
    }

    @Override
    protected void onServiceConnected() {

        String loading = getResources().getString(R.string.loadingSquads);
        mProgressDialog = ProgressDialog.show(this, "", loading, true);

        mBinder.loadSquadsDefnFromServer(new DataLoadedCallback() {

            @Override
            public void errorLoadingData(String error) {

                mProgressDialog.dismiss();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TeamsActivity.this);
                alertDialogBuilder.setTitle(R.string.errorHeader);
                alertDialogBuilder
                    .setMessage(error)
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                      });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

            @Override
            public void dataReady() {
                mProgressDialog.dismiss();
            }
        } , false);
    }

    @Override
    protected void onServiceDisconnected() {
        // TODO Auto-generated method stub

    }
}
