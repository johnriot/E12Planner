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

            //TODO: These groups need to live through the application and be
            //      persisted between application runs so we can update the
            //      scores etc
            Group group = null;

            switch(position){
            case 0:
                group = new Group(GroupState.GROUP_A);
                break;
            case 1:
                group = new Group(GroupState.GROUP_B);
                break;
            case 2:
                group = new Group(GroupState.GROUP_C);
                break;
            case 3:
                group = new Group(GroupState.GROUP_D);
                break;
            default:
                return null;
            }

            return group.getView(GamesActivity.this);
        }
    }

}
