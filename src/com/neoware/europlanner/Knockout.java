package com.neoware.europlanner;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Knockout extends TournamentStage {

    private static final int KNOCKOUT_FRAGMENT_RESOURCE = R.layout.knockout_fragment;
    public static final int KNOCKOUT_ID_OFFSET = 100;

    public Knockout(int id) {
        super(id);
    }

    @Override
    public View drawView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState, Resources res, final Activity activity) {

        View fragView = inflater.inflate(KNOCKOUT_FRAGMENT_RESOURCE, container, false);

        TableLayout fixturesTable = (TableLayout)fragView.findViewById(R.id.knockoutTable);

        String venues[] = res.getStringArray(R.array.venues);
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
        for(final Fixture fixture : mFixtures) {

            if(!fixture.getTime().equals(currentDate)) {

                TableRow titleRow = (TableRow) inflater.inflate(
                        R.layout.knockout_table_day_row, null);

                TextView titleView = (TextView)titleRow.findViewById(
                        R.id.knockoutTableTitle);
                titleView.setText(gameTitles[i++]);

                TextView dateView = (TextView)titleRow.findViewById(
                        R.id.knockoutTableDate);
                dateView.setText(DATE_FORMAT.format(fixture.getTime()));

                fixturesTable.addView(titleRow);
                currentDate = fixture.getTime();
            }

            TableRow fixtureRow = null;
            // Decide which Teams are Real and which are Placeholders; draw view accordingly
            if(fixture.getHomeTeam().isRealTeam() && fixture.getAwayTeam().isRealTeam()) {
                fixtureRow = (TableRow) inflater.inflate(
                        R.layout.fixtures_table_fixture_row, null);
            }
            else if(fixture.getHomeTeam().isRealTeam()) {
                fixtureRow = (TableRow) inflater.inflate(
                        R.layout.knockout_t_f_left_real, null);
            }
            else if(fixture.getAwayTeam().isRealTeam()) {
                fixtureRow = (TableRow) inflater.inflate(
                        R.layout.knockout_t_f_right_real, null);
            }
            else { // both real teams (not placeholders)
                fixtureRow = (TableRow) inflater.inflate(
                        R.layout.knockout_t_f_neither_real, null);
            }

            String crestIds[] = res.getStringArray(R.array.team_icon_resource_names);
            String teamNames[] = res.getStringArray(R.array.team_names);
            String packageName = getClass().getPackage().getName();

            TextView homeTeamTV = (TextView)fixtureRow.findViewById(
                    R.id.fixturesTableHomeTeam);


            if(fixture.getHomeTeam().isRealTeam()) {
                homeTeamTV.setText(teamNames[fixture.getHomeTeamId()]);
                ImageView homeTeamIV = (ImageView)fixtureRow.findViewById(
                        R.id.fixturesTableHomeTeamIcon);
                homeTeamIV.setImageResource(res.getIdentifier(crestIds[fixture.getHomeTeamId()],
                        "drawable", packageName));
            }
            else {
                homeTeamTV.setText(teamPlaceholders[fixture.getHomeTeamId() - KNOCKOUT_ID_OFFSET]);
            }


            TextView awayTeamTV = (TextView)fixtureRow.findViewById(
                    R.id.fixturesTableAwayTeam);


            if(fixture.getAwayTeam().isRealTeam()) {
                awayTeamTV.setText(teamNames[fixture.getAwayTeamId()]);
                ImageView awayTeamIV = (ImageView)fixtureRow.findViewById(
                        R.id.fixturesTableAwayTeamIcon);
                awayTeamIV.setImageResource(res.getIdentifier(crestIds[fixture.getAwayTeamId()],
                        "drawable", packageName));
            }
            else {
                awayTeamTV.setText(teamPlaceholders[fixture.getAwayTeamId() - KNOCKOUT_ID_OFFSET]);
            }

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

            TableRow venueRow = (TableRow) inflater.inflate(
                    R.layout.fixtures_table_venue_row, null);
            TextView fixtureTv = (TextView)venueRow.findViewById(R.id.fixtureVenue);

            SpannableString venue = new SpannableString(venues[fixture.getLocationId()]);
            venue.setSpan(new UnderlineSpan(), 0, venue.length(), 0);
            fixtureTv.setText(venue);

            fixtureTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent venueIntent = new Intent(activity, VenuesActivity.class);
                    venueIntent.putExtra(VenuesActivity.VENUE_ID, fixture.getLocationId());
                    activity.startActivity(venueIntent);
                }
            });

            fixturesTable.addView(fixtureRow);
            fixturesTable.addView(venueRow);
        }
        return fragView;
    }
}
