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

import com.steo.europlanner.Group.GroupState;

public class GamesActivity extends FragmentActivity {

    GroupFragmentAdapter mFragmentAdapter;

    //TODO: Check out this indicator: http://www.zylinc.com/blog-reader/items/viewpaager-page-indicator.html
    ViewPager mPager;

    private static final int NUM_GROUPS = 4;

    private Group mGroupA;
    private Group mGroupB;
    private Group mGroupC;
    private Group mGroupD;

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

        mGroupA = new Group(GroupState.GROUP_A);
        mGroupB = new Group(GroupState.GROUP_B);
        mGroupC = new Group(GroupState.GROUP_C);
        mGroupD = new Group(GroupState.GROUP_D);
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
                case 0: return mGroupA.getView(GamesActivity.this);
                case 1: return mGroupB.getView(GamesActivity.this);
                case 2: return mGroupC.getView(GamesActivity.this);
                case 3: return mGroupD.getView(GamesActivity.this);
                default:
                    return null;
            }
        }
    }
}
