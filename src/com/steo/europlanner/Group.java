package com.steo.europlanner;

import android.content.Context;
import android.support.v4.app.Fragment;

public class Group {

    private final GroupState mGroupState;
    private GroupFragment mViewFragment;

    public Group(GroupState state) {
        mGroupState = state;
    }

    public Fragment getView(Context ctx) {

        if(mViewFragment == null) {
            mViewFragment = new GroupFragment(mGroupState.getName(ctx),
                    mGroupState.getTeams(ctx));
        }

        return mViewFragment;
    }

    public enum GroupState {

        GROUP_A {

            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_a_name);
            }

            @Override
            public Team[] getTeams(Context context) {

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = Team.TeamID.POLAND.getTeam(context);
                teams[1] = Team.TeamID.GREECE.getTeam(context);
                teams[2] = Team.TeamID.RUSSIA.getTeam(context);
                teams[3] = Team.TeamID.CZECH_REPUBLIC.getTeam(context);

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

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = Team.TeamID.NETHERLANDS.getTeam(context);
                teams[1] = Team.TeamID.DENMARK.getTeam(context);
                teams[2] = Team.TeamID.GERMANY.getTeam(context);
                teams[3] = Team.TeamID.PORTUGAL.getTeam(context);

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

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = Team.TeamID.SPAIN.getTeam(context);
                teams[1] = Team.TeamID.ITALY.getTeam(context);
                teams[2] = Team.TeamID.REPUBLIC_OF_IRELAND.getTeam(context);
                teams[3] = Team.TeamID.CROATIA.getTeam(context);

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
                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = Team.TeamID.UKRAINE.getTeam(context);
                teams[1] = Team.TeamID.SWEDEN.getTeam(context);
                teams[2] = Team.TeamID.FRANCE.getTeam(context);
                teams[3] = Team.TeamID.ENGLAND.getTeam(context);

                return teams;
            }
        };

        private static final int GROUP_SIZE = 4;

        public abstract Team[] getTeams(Context context);
        public abstract String getName(Context context);
    }
}

