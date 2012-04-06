package com.steo.europlanner;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GroupFragment extends Fragment {

    private final Group mGroup;

    public GroupFragment(Group group) {
        mGroup = group;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Resources res = this.getResources();

        View fragView = inflater.inflate(R.layout.group_fragment, container, false);

        TextView groupNameView = (TextView)fragView.findViewById(R.id.groupName);
        String groupName = res.getStringArray(R.array.groups)[mGroup.getGroupId()];
        groupNameView.setText(groupName);

        String packageName = getClass().getPackage().getName();

        String teamNames[] = res.getStringArray(R.array.team_names);
        String crestIds[] = res.getStringArray(R.array.team_icon_resource_names);

        ArrayList<Team> teams = mGroup.getGroupTeams();
        //This view supports 4 teams
        for(int i = 0; i < 4; i++) {

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
        }

        TableLayout fixturesTable = (TableLayout)fragView.findViewById(
                R.id.fixturesTable);

        ArrayList<Fixture> fixtures = mGroup.getFixtures();
        for(Fixture fixture : fixtures) {

            TableRow fixtureRow = (TableRow) inflater.inflate(
                    R.layout.fixture_row, fixturesTable, false);

            TextView homeTeam = (TextView)fixtureRow.findViewById(R.id.homeTeam);
            homeTeam.setText(teamNames[fixture.getHomeTeamId()]);

            TextView awayTeam = (TextView)fixtureRow.findViewById(R.id.awayTeam);
            awayTeam.setText(teamNames[fixture.getAwayTeamId()]);

            fixturesTable.addView(fixtureRow);
        }

        return fragView;
    }
}
