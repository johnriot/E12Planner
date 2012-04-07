package com.steo.europlanner;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.vladexologija.widget.GroupedTextView;

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
        }

        LinearLayout groupFragmentLayout = (LinearLayout)fragView.findViewById(
                R.id.groupFragmentLayout);

        GroupedTextView gtv = new GroupedTextView(fragView.getContext());
        gtv.setPadding(20, 20, 20, 0);

        ArrayList<Fixture> fixtures = mGroup.getFixtures();
        for(Fixture fixture : fixtures) {

            TextView tv = new TextView(fragView.getContext());
            tv.setText("SteoTime");

            gtv.addPlainTextView("Steo");
            //(tv);
        }

        groupFragmentLayout.addView(gtv);

        return fragView;
    }
}
