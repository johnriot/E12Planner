package com.neoware.europlanner;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;
import com.neoware.europlanner.E12DataService.DataLoadedCallback;

public class GamesActivity extends E12ServiceActivity {

    TabsAdapter mTabAdapter;

    //TODO: Check out this indicator: http://www.zylinc.com/blog-reader/items/viewpaager-page-indicator.html
    ViewPager mPager;
    TournamentDefinition mTournamentDefn;

    public static final String GROUP_KNOCKOUT = "groupKnockout";

    private MenuItem mRefreshItem;
    private ProgressDialog mProgressDialog;

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

        mTournamentDefn = TournamentDefinition.getTournamentDefnInstance(this);

        mPager = (ViewPager)findViewById(R.id.groupPager);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mTabAdapter = new TabsAdapter(this, mPager);
        mPager.setAdapter(mTabAdapter);

        addGroupsAndKnockout();

        Bundle extras = getIntent().getExtras();
        int groupIndx = 0;
        if(extras != null) {
            groupIndx = extras.getInt(GROUP_KNOCKOUT);
        }

        mPager.setCurrentItem(groupIndx);
    }

    public void addGroupsAndKnockout() {
        String groupNames[] = getResources().getStringArray(R.array.groups);

        ArrayList<Group> groups = mTournamentDefn.getGroups();
        for(Group group : groups) {

            mTabAdapter.addTab(getSupportActionBar().newTab().setText(groupNames[group.getId()]),
                    new StageFragment(group.getId()));
        }

        int numGroups = groups.size();
        String knockoutNames[] = getResources().getStringArray(R.array.knockout);
        ArrayList<Knockout> knockoutStages = mTournamentDefn.getKnockoutStages();
        for(Knockout knockout : knockoutStages) {
            mTabAdapter.addTab(getSupportActionBar().newTab().setText(knockoutNames[knockout.getId() - numGroups]),
                    new StageFragment(knockout.getId()));
        }
    }

    public void clearTabsAdapter() {
        int cnt = mTabAdapter.getCount();

        for(int i = 0; i < cnt; i++) {
            mTabAdapter.destroyItem(null, i, mTabAdapter.getItem(i));
        }
    }

    public static class TabsAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<StageFragment> mFragments = new ArrayList<StageFragment>();

        public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());

            mActionBar = activity.getSupportActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, StageFragment fragment) {

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

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if(item.getItemId() == android.R.id.home) {

            Intent homeIntent = new Intent(this, PlannerHomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            return true;
        }
        else if(item == mRefreshItem) {
            readGamesFromServer();
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

    @Override
    protected void onServiceConnected() {
        readGamesFromServer();
    }

    @Override
    protected void onServiceDisconnected() { }

    private void readGamesFromServer() {

        String loading = getResources().getString(R.string.loadingResults);
        mProgressDialog = ProgressDialog.show(this, "", loading, true);

        mBinder.loadTournamentDefnFromServer(new DataLoadedCallback() {

            @Override
            public void errorLoadingData(String error) {

                mProgressDialog.hide();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GamesActivity.this);
                alertDialogBuilder.setTitle(R.string.errorHeader);
                alertDialogBuilder
                    .setMessage(error)
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                      });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

            @Override
            public void dataReady() {
                mProgressDialog.hide();
                mTabAdapter.notifyDataSetChanged();
            }
        });
    }
}
