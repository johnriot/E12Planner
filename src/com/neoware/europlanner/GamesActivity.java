package com.neoware.europlanner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;

public class GamesActivity extends SherlockFragmentActivity {

    TabsAdapter mTabAdapter;

    //TODO: Check out this indicator: http://www.zylinc.com/blog-reader/items/viewpaager-page-indicator.html
    ViewPager mPager;
    TournamentDefinition mTournamentDefn;

    public static final String GROUP_KNOCKOUT = "groupKnockout";

    private MenuItem mRefreshItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.games_layout);

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.toolbar_bg);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(bg);
        bar.setTitle(R.string.games_title);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayUseLogoEnabled(true);

        IMAdView adView = (IMAdView) findViewById(R.id.adViewGames);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(true);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();

        mTournamentDefn = new TournamentDefinition(this);

        mPager = (ViewPager)findViewById(R.id.groupPager);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabAdapter = new TabsAdapter(this, mPager);
        String groupNames[] = getResources().getStringArray(R.array.groups);

        ArrayList<Group> groups = mTournamentDefn.getGroups();
        for(Group group : groups) {

            mTabAdapter.addTab(bar.newTab().setText(groupNames[group.getId()]),
                    new GroupFragment(group));
        }

        int numGroups = groups.size();
        String knockoutNames[] = getResources().getStringArray(R.array.knockout);
        ArrayList<Knockout> knockoutStages = mTournamentDefn.getKnockoutStages();
        for(Knockout knockout : knockoutStages) {

            mTabAdapter.addTab(bar.newTab().setText(knockoutNames[knockout.getId() - numGroups]),
                    new GroupFragment(knockout));
        }

        mPager.setAdapter(mTabAdapter);

        Bundle extras = getIntent().getExtras();
        int groupIndx = 0;
        if(extras != null) {
            groupIndx = extras.getInt(GROUP_KNOCKOUT);
        }
        mPager.setCurrentItem(groupIndx);
    }


    public static class TabsAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<GroupFragment> mFragments = new ArrayList<GroupFragment>();

        public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());

            mActionBar = activity.getSupportActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, GroupFragment fragment) {

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
            //TabInfo info = mTabs.get(position);
            //return (SherlockFragment)Fragment.instantiate(mContext, info.clss.getName(), info.args);
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
        else if(item == mRefreshItem) {

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(new Date("06/08/2012 19:00"));

            Calendar nowCal = Calendar.getInstance();
            nowCal.setTime(new Date());

            String text = null;
            long days = daysBetween(nowCal, startCal);
            if(days == 1) {
               text = getResources().getString(R.string.oneDayToGo);
            }
            else {
                text = getResources().getString(R.string.daysToGo, days);
            }

            if(days >= 1) {
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }
        }

        return super.onMenuItemSelected(featureId, item);
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
          date.add(Calendar.DAY_OF_MONTH, 1);
          daysBetween++;
        }
        return daysBetween;
      }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        mRefreshItem = menu.add(R.string.refresh_text)
            .setIcon(R.drawable.ic_refresh_dark);
        mRefreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }
}
