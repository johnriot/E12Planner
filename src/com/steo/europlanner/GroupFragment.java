package com.steo.europlanner;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class GroupFragment extends SherlockFragment {

    private final Group mGroup;

    public GroupFragment(Group group) {
        mGroup = group;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Resources res = this.getResources();

        View fragView = inflater.inflate(R.layout.group_fragment, container, false);

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

            String gamesPlayedIdStr = "team" + i + "played";
            int gamesPlayedViewId = res.getIdentifier(gamesPlayedIdStr, "id", packageName);
            TextView gamesPlayedView = (TextView)fragView.findViewById(gamesPlayedViewId);

            gamesPlayedView.setText("3");

            String forIdStr = "team" + i + "for";
            int forViewId = res.getIdentifier(forIdStr, "id", packageName);
            TextView forView = (TextView)fragView.findViewById(forViewId);

            forView.setText("3");

            String againstIdStr = "team" + i + "against";
            int againstViewId = res.getIdentifier(againstIdStr, "id", packageName);
            TextView againstView = (TextView)fragView.findViewById(againstViewId);

            againstView.setText("3");

            String pointsIdStr = "team" + i + "pts";
            int pointsViewId = res.getIdentifier(pointsIdStr, "id", packageName);
            TextView pointsView = (TextView)fragView.findViewById(pointsViewId);

            pointsView.setText("3");
        }

        return fragView;
    }
}
