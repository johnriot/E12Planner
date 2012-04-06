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

import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;

public class GamesActivity extends FragmentActivity {

    GroupFragmentAdapter mFragmentAdapter;

    //TODO: Check out this indicator: http://www.zylinc.com/blog-reader/items/viewpaager-page-indicator.html
    ViewPager mPager;
    TournamentDefinition mTournamentDefn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.games_layout);

        IMAdView adView = (IMAdView) findViewById(R.id.adViewGames);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(true);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();

        mTournamentDefn = new TournamentDefinition(this);

        mFragmentAdapter = new GroupFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.groupPager);
        mPager.setAdapter(mFragmentAdapter);

        mPager.setCurrentItem(0);

        ImageButton homeButton = (ImageButton)findViewById(R.id.gamesHomeButton);
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
            return mTournamentDefn.getGroups().size();
        }

        @Override
        public Fragment getItem(int position) {
            return new GroupFragment(mTournamentDefn.getGroups().get(position));
        }
    }
}
