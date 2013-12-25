package com.neoware.europlanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import com.actionbarsherlock.view.Window;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.neoware.europlanner.fragments.SquadFragment;
import com.neoware.europlanner.fragments.SquadWriteupFragment;
import com.neoware.europlanner.fragments.StarPlayerFragment;

public class SquadActivity extends SherlockFragmentActivity {

    private SquadsTabAdapter mTabAdapter;
    private ViewPager mPager;

    public static final String TEAM_INDEX = "team_indx";
    SquadsDefinition mSquadDefn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setSupportProgressBarIndeterminateVisibility(false);

        setContentView(R.layout.squad_layout);

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.toolbar_bg);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

        final String teams[] = this.getResources().getStringArray(R.array.team_names);
        Bundle extras = getIntent().getExtras();
        int teamIndx = 0;
        if(extras != null) {
            teamIndx = extras.getInt(TEAM_INDEX);
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(bg);
        bar.setTitle(teams[teamIndx]);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayUseLogoEnabled(true);

        mSquadDefn = SquadsDefinition.getSquadsDefnInstance(this);

        mPager = (ViewPager)findViewById(R.id.squadPager);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabAdapter = new SquadsTabAdapter(this, mPager);
        mPager.setAdapter(mTabAdapter);

        mTabAdapter.addTab(getSupportActionBar()
                .newTab()
                .setText(R.string.squadProspects),
                new SquadWriteupFragment(teamIndx));

        mTabAdapter.addTab(getSupportActionBar()
                .newTab()
                .setText(R.string.squadStarPlayer),
                new StarPlayerFragment(teamIndx));
        
        /* TODO: PERHAPS REINSTATE LATER
        mTabAdapter.addTab(getSupportActionBar()
                .newTab()
                .setText(R.string.squadHeader),
                new SquadFragment(teamIndx));
                */
    }

    @Override
    public void onResume() {

        super.onResume();

        if(Settings.USE_LIVE_ADS) {
            AdView adview = (AdView)findViewById(R.id.adViewSquad);

            AdRequest req = new AdRequest();

            String [] keywords = getResources().getStringArray(R.array.adKeywords);
            Set<String> keywordsSet = new HashSet<String>(Arrays.asList(keywords));
            req.setKeywords(keywordsSet);

            adview.loadAd(req);
        }
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

    //TODO: This is exactly the same as the games one - unify
    private static class SquadsTabAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<SherlockFragment> mFragments = new ArrayList<SherlockFragment>();

        public SquadsTabAdapter(SherlockFragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());

            mActionBar = activity.getSupportActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, SherlockFragment fragment) {

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

        //Without this, notifyDataSetChanged does FUCK ALL
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
        @Override public void onTabReselected(Tab tab, FragmentTransaction ft) {}
    }
}
