package com.steo.europlanner;

import android.content.Context;
import android.graphics.drawable.Drawable;

public enum Group {

    GROUP_A {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_a_name);
        }

        @Override
        public String[] getTeams(Context context) {
            return context.getResources().getStringArray(R.array.group_a_teams);
        }

        @Override
        public Drawable[] getTeamIcons(Context context) {
            Drawable[] icons = new Drawable[GROUP_SIZE];
            icons[0] = context.getResources().getDrawable(R.drawable.poland);
            icons[1] = context.getResources().getDrawable(R.drawable.greece);
            icons[2] = context.getResources().getDrawable(R.drawable.russia);
            icons[3] = context.getResources().getDrawable(R.drawable.czechrep);
            return icons;
        }
    },
    GROUP_B {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_b_name);
        }

        @Override
        public String[] getTeams(Context context) {
            return context.getResources().getStringArray(R.array.group_b_teams);
        }

        @Override
        public Drawable[] getTeamIcons(Context context) {
            Drawable[] icons = new Drawable[GROUP_SIZE];
            icons[0] = context.getResources().getDrawable(R.drawable.netherlands);
            icons[1] = context.getResources().getDrawable(R.drawable.denmark);
            icons[2] = context.getResources().getDrawable(R.drawable.germany);
            icons[3] = context.getResources().getDrawable(R.drawable.portugal);
            return icons;
        }
    },
    GROUP_C {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_c_name);
        }

        @Override
        public String[] getTeams(Context context) {
            return context.getResources().getStringArray(R.array.group_c_teams);
        }

        @Override
        public Drawable[] getTeamIcons(Context context) {
            Drawable[] icons = new Drawable[GROUP_SIZE];
            icons[0] = context.getResources().getDrawable(R.drawable.spain);
            icons[1] = context.getResources().getDrawable(R.drawable.italy);
            icons[2] = context.getResources().getDrawable(R.drawable.ireland);
            icons[3] = context.getResources().getDrawable(R.drawable.croatia);
            return icons;
        }
    },
    GROUP_D {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_d_name);
        }

        @Override
        public String[] getTeams(Context context) {
            return context.getResources().getStringArray(R.array.group_d_teams);
        }

        @Override
        public Drawable[] getTeamIcons(Context context) {
            Drawable[] icons = new Drawable[GROUP_SIZE];
            icons[0] = context.getResources().getDrawable(R.drawable.ukraine);
            icons[1] = context.getResources().getDrawable(R.drawable.sweden);
            icons[2] = context.getResources().getDrawable(R.drawable.france);
            icons[3] = context.getResources().getDrawable(R.drawable.england);
            return icons;
        }
    };

    private static final int GROUP_SIZE = 4;

    public abstract String getName(Context context);
    public abstract String[]getTeams(Context context);
    public abstract Drawable[] getTeamIcons(Context context);
}
