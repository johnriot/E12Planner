package com.steo.europlanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class GamesActivity extends FragmentActivity {

    GroupFragmentAdapter mFragmentAdapter;
    ViewPager mPager;

    private static final int NUM_GROUPS = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.games_layout);

        mFragmentAdapter = new GroupFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.groupPager);
        mPager.setAdapter(mFragmentAdapter);

        mPager.setCurrentItem(0);

        ImageButton homeButton = (ImageButton)findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class GroupFragmentAdapter extends FragmentPagerAdapter {
        public GroupFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_GROUPS;
        }

        @Override
        public Fragment getItem(int position) {

            String groupName = null;
            Team[] teams = null;

            switch(position){
            case 0:
                groupName = GroupStates.GROUP_A.getName(GamesActivity.this);
                teams = GroupStates.GROUP_A.getTeams(GamesActivity.this);
                break;
            case 1:
                groupName = GroupStates.GROUP_B.getName(GamesActivity.this);
                teams = GroupStates.GROUP_B.getTeams(GamesActivity.this);
                break;
            case 2:
                groupName = GroupStates.GROUP_C.getName(GamesActivity.this);
                teams = GroupStates.GROUP_C.getTeams(GamesActivity.this);
                break;
            case 3:
                groupName = GroupStates.GROUP_D.getName(GamesActivity.this);
                teams = GroupStates.GROUP_D.getTeams(GamesActivity.this);
                break;
            default:
                return null;
            }

            return new GroupFragment(groupName, teams);
        }
    }

}
