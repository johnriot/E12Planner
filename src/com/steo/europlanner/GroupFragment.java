package com.steo.europlanner;

import java.util.ArrayList;

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

    private final String mGroupName;
    private final Team[] mTeams;
    private final ArrayList<Fixture> mFixtures;

    private static final int EXPECTED_GROUP_SIZE = 4;

    public GroupFragment(String groupName, Team[] teams, ArrayList<Fixture> fixtures) {

        if(teams.length != EXPECTED_GROUP_SIZE) {
            throw new IllegalArgumentException("The group fragment currently " +
                    "only supports 4 teams. No more, no less.");
        }

        mGroupName = groupName;
        mTeams = teams;
        mFixtures = fixtures;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.group_fragment, container, false);

        TextView groupName = (TextView)fragView.findViewById(R.id.groupName);
        groupName.setText(mGroupName);

        //Set the icons
        ImageView team1Icon = (ImageView)fragView.findViewById(R.id.team1Icon);
        team1Icon.setImageResource(mTeams[0].teamIconResId);

        ImageView team2Icon = (ImageView)fragView.findViewById(R.id.team2Icon);
        team2Icon.setImageResource(mTeams[1].teamIconResId);

        ImageView team3Icon = (ImageView)fragView.findViewById(R.id.team3Icon);
        team3Icon.setImageResource(mTeams[2].teamIconResId);

        ImageView team4Icon = (ImageView)fragView.findViewById(R.id.team4Icon);
        team4Icon.setImageResource(mTeams[3].teamIconResId);

        //Set team Names...
        TextView team1name = (TextView)fragView.findViewById(R.id.team1Team);
        team1name.setText(mTeams[0].teamName);

        TextView team2name = (TextView)fragView.findViewById(R.id.team2Team);
        team2name.setText(mTeams[1].teamName);

        TextView team3name = (TextView)fragView.findViewById(R.id.team3Team);
        team3name.setText(mTeams[2].teamName);

        TextView team4name = (TextView)fragView.findViewById(R.id.team4Team);
        team4name.setText(mTeams[3].teamName);

        //Add the fixtures
        TableLayout fixturesTable = (TableLayout)fragView.findViewById(R.id.fixturesTable);

        for(Fixture fixture : mFixtures) {
            TableRow fixtureRow = (TableRow) inflater.inflate(
                    R.layout.fixture_row, fixturesTable, false);

            TextView homeTeam = (TextView)fixtureRow.findViewById(R.id.homeTeam);
            homeTeam.setText(fixture.getHomeTeam().getTeam(getActivity()).teamName);

            TextView awayTeam = (TextView)fixtureRow.findViewById(R.id.awayTeam);
            awayTeam.setText(fixture.getAwayTeam().getTeam(getActivity()).teamName);

            fixturesTable.addView(fixtureRow);
        }

        return fragView;
    }
}
