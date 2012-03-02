package com.steo.europlanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

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

            switch(position){
            case 0:
                return new GroupFragment(Group.GROUP_A, GamesActivity.this);
            case 1:
                return new GroupFragment(Group.GROUP_B, GamesActivity.this);
            case 2:
                return new GroupFragment(Group.GROUP_C, GamesActivity.this);
            case 3:
                return new GroupFragment(Group.GROUP_D, GamesActivity.this);
            }

            return null;

        }
    }

}
