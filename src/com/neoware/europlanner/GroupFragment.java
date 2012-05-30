package com.neoware.europlanner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import com.actionbarsherlock.app.SherlockFragment;

public class GroupFragment extends SherlockFragment {

    private final TournamentStage mStage;
    public final int NUMBER_GROUPS = 4;
    public final int NUMBER_TEAMS_PER_GROUP = 4;

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("E, dd MMM, HH:mm");

    public GroupFragment(TournamentStage stage) {
        mStage = stage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Resources res = this.getResources();

        View fragView = inflater.inflate(R.layout.group_fragment, container, false);

        String packageName = getClass().getPackage().getName();

        String teamNames[] = res.getStringArray(R.array.team_names);
        String crestIds[] = res.getStringArray(R.array.team_icon_resource_names);

        ArrayList<Team> teams = mStage.getTeams();
        if(mStage.getId() < NUMBER_GROUPS) {
            //This view supports 4 teams
            for(int i = 0; i < NUMBER_TEAMS_PER_GROUP; i++) {

                Team team = teams.get(i);

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

                gamesPlayedView.setText("" + mStage.getFixturesPlayed(team));

                String forIdStr = "team" + i + "for";
                int forViewId = res.getIdentifier(forIdStr, "id", packageName);
                TextView forView = (TextView)fragView.findViewById(forViewId);

                forView.setText("" + mStage.getGoalsFor(team));

                String againstIdStr = "team" + i + "against";
                int againstViewId = res.getIdentifier(againstIdStr, "id", packageName);
                TextView againstView = (TextView)fragView.findViewById(againstViewId);

                againstView.setText("" + mStage.getGoalsAgainst(team));

                String pointsIdStr = "team" + i + "pts";
                int pointsViewId = res.getIdentifier(pointsIdStr, "id", packageName);
                TextView pointsView = (TextView)fragView.findViewById(pointsViewId);

                pointsView.setText("" + mStage.getPoints(team));
            }
        }

        TableLayout fixturesTable = (TableLayout)fragView.findViewById(R.id.fixturesTable);

        String venues[] = res.getStringArray(R.array.venues);
        String scoreSep = res.getString(R.string.scoreSeperator);
        String scoreSepUnplayed = res.getString(R.string.scoreSeperatorUnplayed);

        ArrayList<Fixture> fixtures = mStage.getFixtures();
        Date currentDate = null;
        for(final Fixture fixture : fixtures) {

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

            TextView fixtureTv = (TextView)fixtureRow.findViewById(R.id.fixtureVenue);

            SpannableString venue = new SpannableString(venues[fixture.getLocationId()]);
            venue.setSpan(new UnderlineSpan(), 0, venue.length(), 0);
            fixtureTv.setText(venue);

            fixtureTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent venueIntent = new Intent(GroupFragment.this.getActivity(), VenuesActivity.class);
                    venueIntent.putExtra(VenuesActivity.VENUE_ID, fixture.getLocationId());
                    startActivity(venueIntent);
                }
            });

            fixturesTable.addView(fixtureRow);
        }

        return fragView;
    }
}
