package com.steo.europlanner;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

public class Group {

    private final GroupState mGroupState;

    public Group(GroupState state) {
        mGroupState = state;
    }

    public Fragment getView(Context ctx) {

        return new GroupFragment(mGroupState.getName(ctx),
                mGroupState.getTeams(ctx));
    }

    public enum GroupState {

        GROUP_A {

            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_a_name);
            }

            @Override
            public Team[] getTeams(Context context) {

                Resources res = context.getResources();

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = new Team(res.getString(R.string.polandName), R.drawable.poland);
                teams[1] = new Team(res.getString(R.string.greeceName), R.drawable.greece);
                teams[2] = new Team(res.getString(R.string.russiaName), R.drawable.russia);
                teams[3] = new Team(res.getString(R.string.czechRepName), R.drawable.czechrep);

                return teams;
            }
        },
        GROUP_B {
            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_b_name);
            }

            @Override
            public Team[] getTeams(Context context) {

                Resources res = context.getResources();

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = new Team(res.getString(R.string.netherlandsName), R.drawable.netherlands);
                teams[1] = new Team(res.getString(R.string.denmarkName), R.drawable.denmark);
                teams[2] = new Team(res.getString(R.string.germanyName), R.drawable.germany);
                teams[3] = new Team(res.getString(R.string.portugalName), R.drawable.portugal);

                return teams;
            }
        },
        GROUP_C {
            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_c_name);
            }

            @Override
            public Team[] getTeams(Context context) {

                Resources res = context.getResources();

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = new Team(res.getString(R.string.spainName), R.drawable.spain);
                teams[1] = new Team(res.getString(R.string.italyName), R.drawable.italy);
                teams[2] = new Team(res.getString(R.string.irelandName), R.drawable.ireland);
                teams[3] = new Team(res.getString(R.string.croatiaName), R.drawable.croatia);

                return teams;
            }
        },
        GROUP_D {
            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_d_name);
            }

            @Override
            public Team[] getTeams(Context context) {

                Resources res = context.getResources();

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = new Team(res.getString(R.string.ukraineName), R.drawable.ukraine);
                teams[1] = new Team(res.getString(R.string.swedenName), R.drawable.sweden);
                teams[2] = new Team(res.getString(R.string.franceName), R.drawable.france);
                teams[3] = new Team(res.getString(R.string.englandName), R.drawable.england);

                return teams;
            }
        };

        private static final int GROUP_SIZE = 4;

        public abstract Team[] getTeams(Context context);
        public abstract String getName(Context context);
    }
}

