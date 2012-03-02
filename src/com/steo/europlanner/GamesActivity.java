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

    public static class GroupFragmentAdapter extends FragmentPagerAdapter {
        public GroupFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {

            switch(position){
            case 0:
                return new GroupFragment(Groups.GROUP_A);
            case 1:
                return new GroupFragment(Groups.GROUP_B);
            case 2:
                return new GroupFragment(Groups.GROUP_C);
            case 3:
                return new GroupFragment(Groups.GROUP_D);
            }

            return null;

        }
    }

}
