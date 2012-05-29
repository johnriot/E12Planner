package com.neoware.europlanner;

import java.util.Date;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Knockout extends TournamentStage {

    private static final int KNOCKOUT_FRAGMENT_RESOURCE = R.layout.knockout_fragment;

    public Knockout(int id) {
        super(id);
    }

    @Override
    public View drawView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState, Resources res) {

        View fragView = inflater.inflate(KNOCKOUT_FRAGMENT_RESOURCE, container, false);

        TableLayout fixturesTable = (TableLayout)fragView.findViewById(R.id.knockoutTable);

        String scoreSep = res.getString(R.string.scoreSeperator);
        String scoreSepUnplayed = res.getString(R.string.scoreSeperatorUnplayed);

        String teamPlaceholders[] = null;
        String gameTitles[] = null;
        if(mId == 4) { // Quarter-Final
            gameTitles = res.getStringArray(R.array.qf_game_titles);
            teamPlaceholders = res.getStringArray(R.array.qf_placeholder_names);
        }
        else if(mId == 5) { // Semi-Final
            gameTitles = res.getStringArray(R.array.sf_game_titles);
            teamPlaceholders = res.getStringArray(R.array.sf_placeholder_names);
        }
        else { // Final
            gameTitles = res.getStringArray(R.array.f_game_titles);
            teamPlaceholders = res.getStringArray(R.array.f_placeholder_names);
        }

        Date currentDate = null;
        int i = 0;
        for(Fixture fixture : mFixtures) {

            if(!fixture.getTime().equals(currentDate)) {

                TableRow titleRow = (TableRow) inflater.inflate(
                        R.layout.knockout_table_row, null);

                TextView titleView = (TextView)titleRow.findViewById(
                        R.id.knockoutTableTitle);
                titleView.setText(gameTitles[i++]);

                TextView dateView = (TextView)titleRow.findViewById(
                        R.id.knockoutTableDate);
                dateView.setText(DATE_FORMAT.format(fixture.getTime()));

                fixturesTable.addView(titleRow);
                currentDate = fixture.getTime();
            }

            TableRow fixtureRow = (TableRow) inflater.inflate(
                    R.layout.placeholder_table_row, null);

            TextView homeTeamTV = (TextView)fixtureRow.findViewById(
                    R.id.fixturesTableHomeTeam);
            homeTeamTV.setText(teamPlaceholders[fixture.getHomeTeamId()]);

            //ImageView homeTeamIV = (ImageView)fixtureRow.findViewById(
            //        R.id.fixturesTableHomeTeamIcon);
            //homeTeamIV.setImageResource(res.getIdentifier(crestIds[fixture.getHomeTeamId()],
            //        "drawable", packageName));

            TextView awayTeamTV = (TextView)fixtureRow.findViewById(
                    R.id.fixturesTableAwayTeam);
            awayTeamTV.setText(teamPlaceholders[fixture.getAwayTeamId()]);

            //ImageView awayTeamIV = (ImageView)fixtureRow.findViewById(
            //        R.id.fixturesTableAwayTeamIcon);
            //awayTeamIV.setImageResource(res.getIdentifier(crestIds[fixture.getAwayTeamId()],
            //        "drawable", packageName));

            TextView scoreTv = (TextView)fixtureRow.findViewById(
                    R.id.fixturesTableScore);
            Score score = fixture.getScore();
            if(score.hasGameBeenPlayed()) {
                String scoreStr = score.getHomeScore() + scoreSep + score.getAwayScore();
                scoreTv.setText(scoreStr);
            }
            else {
                scoreTv.setText(scoreSepUnplayed);
            }

            fixturesTable.addView(fixtureRow);
        }
        return fragView;
    }
}
