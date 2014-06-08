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


public class Group extends TournamentStage {

    private static final int GROUP_FRAGMENT_RESOURCE = R.layout.group_fragment;
    public final int NUMBER_GROUPS = 8;
    public final int NUMBER_TEAMS_PER_GROUP = 4;

    public Group(int groupId) {
        super(groupId);
    }

    @Override
    public View drawView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState, Resources res, final Activity activity) {


        View fragView = inflater.inflate(GROUP_FRAGMENT_RESOURCE, container, false);

        String packageName = getClass().getPackage().getName();

        String teamNames[] = res.getStringArray(R.array.team_names);
        String crestIds[] = res.getStringArray(R.array.team_icon_resource_names);

        if(mId < NUMBER_GROUPS) {
            //This view supports 4 teams
            for(int i = 0; i < NUMBER_TEAMS_PER_GROUP; i++) {

                Team team = mTeams.get(i);

                String iconViewIdStr = "team" + i + "Icon";
                int iconViewId = res.getIdentifier(iconViewIdStr, "id", packageName);
                ImageView teamIconView = (ImageView)fragView.findViewById(iconViewId);

                teamIconView.setImageResource(res.getIdentifier(crestIds[team.getTeamId()],
                        "drawable", packageName));

                String teamNameViewIdStr = "team" + i + "Team";
                int teamNameViewId = res.getIdentifier(teamNameViewIdStr, "id", packageName);
                TextView teamNameView = (TextView)fragView.findViewById(teamNameViewId);

                teamNameView.setText(teamNames[team.getTeamId()]);

                String gamesPlayedIdStr = "team" + i + "played";
                int gamesPlayedViewId = res.getIdentifier(gamesPlayedIdStr, "id", packageName);
                TextView gamesPlayedView = (TextView)fragView.findViewById(gamesPlayedViewId);

                gamesPlayedView.setText("" + getFixturesPlayed(team));

                String forIdStr = "team" + i + "for";
                int forViewId = res.getIdentifier(forIdStr, "id", packageName);
                TextView forView = (TextView)fragView.findViewById(forViewId);

                forView.setText("" + getGoalsFor(team));

                String againstIdStr = "team" + i + "against";
                int againstViewId = res.getIdentifier(againstIdStr, "id", packageName);
                TextView againstView = (TextView)fragView.findViewById(againstViewId);

                againstView.setText("" + getGoalsAgainst(team));

                String pointsIdStr = "team" + i + "pts";
                int pointsViewId = res.getIdentifier(pointsIdStr, "id", packageName);
                TextView pointsView = (TextView)fragView.findViewById(pointsViewId);

                pointsView.setText("" + getPoints(team));
            }

            TableLayout fixturesTable = (TableLayout)fragView.findViewById(R.id.fixturesTable);

            String venues[] = res.getStringArray(R.array.venues);
            String scoreSep = res.getString(R.string.scoreSeperator);
            String scoreSepUnplayed = res.getString(R.string.scoreSeperatorUnplayed);

            Date currentDate = null;
            for(final Fixture fixture : mFixtures) {

                if(!fixture.getTime().equals(currentDate)) {

                    TableRow dateRow = (TableRow) inflater.inflate(
                            R.layout.fixtures_table_day_row, null);

                    TextView dateView = (TextView)dateRow.findViewById(
                            R.id.fixturesTableDate);
                    dateView.setText(DATE_FORMAT.format(fixture.getTime()));

                    fixturesTable.addView(dateRow);
                    currentDate = fixture.getTime();
                }

                TableRow fixtureRow = (TableRow) inflater.inflate(
                        R.layout.fixtures_table_fixture_row, null);

                TextView homeTeamTV = (TextView)fixtureRow.findViewById(
                        R.id.fixturesTableHomeTeam);
                homeTeamTV.setText(teamNames[fixture.getHomeTeamId()]);

                ImageView homeTeamIV = (ImageView)fixtureRow.findViewById(
                        R.id.fixturesTableHomeTeamIcon);
                homeTeamIV.setImageResource(res.getIdentifier(crestIds[fixture.getHomeTeamId()],
                        "drawable", packageName));

                TextView awayTeamTV = (TextView)fixtureRow.findViewById(
                        R.id.fixturesTableAwayTeam);
                awayTeamTV.setText(teamNames[fixture.getAwayTeamId()]);

                ImageView awayTeamIV = (ImageView)fixtureRow.findViewById(
                        R.id.fixturesTableAwayTeamIcon);
                awayTeamIV.setImageResource(res.getIdentifier(crestIds[fixture.getAwayTeamId()],
                        "drawable", packageName));

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
        }
        return fragView;
    }

}
