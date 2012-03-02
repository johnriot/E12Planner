package com.steo.europlanner;

import java.util.ArrayList;

import android.content.Context;

public enum Groups {

    GROUP_A {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_a_name);
        }

        @Override
        public ArrayList<String> getTeams(Context context) {
            // TODO Auto-generated method stub
            return null;
        }
    },
    GROUP_B {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_b_name);
        }

        @Override
        public ArrayList<String> getTeams(Context context) {
            // TODO Auto-generated method stub
            return null;
        }
    },
    GROUP_C {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_c_name);
        }

        @Override
        public ArrayList<String> getTeams(Context context) {
            // TODO Auto-generated method stub
            return null;
        }
    },
    GROUP_D {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_d_name);
        }

        @Override
        public ArrayList<String> getTeams(Context context) {
            // TODO Auto-generated method stub
            return null;
        }
    },
    GROUP_E {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_a_name);
        }

        @Override
        public ArrayList<String> getTeams(Context context) {
            // TODO Auto-generated method stub
            return null;
        }
    },
    GROUP_F {
        @Override
        public String getName(Context context) {
            return context.getResources().getString(R.string.group_a_name);
        }

        @Override
        public ArrayList<String> getTeams(Context context) {
            // TODO Auto-generated method stub
            return null;
        }
    };

    public abstract String getName(Context context);
    public abstract ArrayList<String> getTeams(Context context);
}
