package com.steo.europlanner;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;

public class VenuesActivity extends SherlockFragmentActivity {

    TabsAdapter mTabAdapter;
    ViewPager mPager;
    TournamentDefinition mTournamentDefn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.venues_layout);

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.toolbar_bg);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(bg);
        bar.setTitle(R.string.venues_title);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayUseLogoEnabled(true);

        IMAdView adView = (IMAdView) findViewById(R.id.adViewVenues);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(true);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();

        mTournamentDefn = new TournamentDefinition(this);

        mPager = (ViewPager)findViewById(R.id.venuePager);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabAdapter = new TabsAdapter(this, mPager);
        String venueNames[] = getResources().getStringArray(R.array.venues_short);

        ArrayList<Venue> venues = mTournamentDefn.getVenues();
        for(Venue venue : venues) {

            mTabAdapter.addTab(bar.newTab().setText(venueNames[venue.getVenueId()]),
                    new VenueFragment(venue));
        }

        mPager.setAdapter(mTabAdapter);
        mPager.setCurrentItem(0);
    }


    public static class TabsAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<VenueFragment> mFragments = new ArrayList<VenueFragment>();

        public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());

            mActionBar = activity.getSupportActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, VenueFragment fragment) {

            mFragments.add(fragment);
            tab.setTabListener(this);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public SherlockFragment getItem(int position) {
            return mFragments.get(position);
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }


        @Override
        public void onPageSelected(int position) {
            mActionBar.setSelectedNavigationItem(position);
        }


        @Override
        public void onPageScrollStateChanged(int state) {
        }


        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
        @Override public void onTabReselected(Tab tab, FragmentTransaction ft) {}
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if(item.getItemId() == android.R.id.home) {

            Intent homeIntent = new Intent(this, PlannerHomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
}

