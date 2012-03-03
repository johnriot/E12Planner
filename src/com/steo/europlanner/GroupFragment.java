package com.steo.europlanner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupFragment extends Fragment {

    private final Group mGroup;
    Context mContext;

    public GroupFragment(Group group, Context context) {
        mGroup = group;
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.group_fragment, container, false);

        TextView groupName = (TextView)fragView.findViewById(R.id.groupName);
        groupName.setText(mGroup.getName(mContext));

        //Set All icons...
        Drawable [] icons = mGroup.getTeamIcons(mContext);

        ImageView team1Icon = (ImageView)fragView.findViewById(R.id.team1Icon);
        team1Icon.setImageDrawable(icons[0]);

        ImageView team2Icon = (ImageView)fragView.findViewById(R.id.team2Icon);
        team2Icon.setImageDrawable(icons[1]);

        ImageView team3Icon = (ImageView)fragView.findViewById(R.id.team3Icon);
        team3Icon.setImageDrawable(icons[2]);

        ImageView team4Icon = (ImageView)fragView.findViewById(R.id.team4Icon);
        team4Icon.setImageDrawable(icons[3]);

        //Set team Names...
        String [] names = mGroup.getTeams(mContext);
        TextView team1name = (TextView)fragView.findViewById(R.id.team1Team);
        team1name.setText(names[0]);

        TextView team2name = (TextView)fragView.findViewById(R.id.team2Team);
        team2name.setText(names[1]);

        TextView team3name = (TextView)fragView.findViewById(R.id.team3Team);
        team3name.setText(names[2]);

        TextView team4name = (TextView)fragView.findViewById(R.id.team4Team);
        team4name.setText(names[3]);



        return fragView;
    }
}
