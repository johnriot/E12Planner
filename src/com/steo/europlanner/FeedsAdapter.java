
package com.steo.europlanner;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.steo.rss.RSSFeed;
import com.steo.rss.RSSItem;

public class FeedsAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final ArrayList<FeedWrapper> mFeeds =
            new ArrayList<FeedsAdapter.FeedWrapper>();

    public FeedsAdapter(Context context) {
        mContext = context;
    }

    public ArrayList<FeedWrapper> getFeeds() {
        return mFeeds;
    }

    public void addFeed(FeedWrapper feed) {
        mFeeds.add(feed);
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mFeeds.get(groupPosition).feed.getItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Return a child view. You can load your custom layout here.
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {

        RSSItem item = mFeeds.get(groupPosition).feed.getItems().get(childPosition);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.feed_child_layout, null);
        }

        TextView headerView = (TextView) convertView.findViewById(R.id.feedChildTV);
        headerView.setText(item.getTitle());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mFeeds.get(groupPosition).feed.getItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mFeeds.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mFeeds.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Return a group view. You can load your custom layout here.
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {

        FeedWrapper feed = mFeeds.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.feed_group_layout, null);
        }

        ImageView iconView = (ImageView)convertView.findViewById(R.id.groupIcon);
        iconView.setImageResource(feed.iconId);

        TextView tv = (TextView) convertView.findViewById(R.id.feedGroupTV);
        tv.setText(feed.name);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    public static class FeedWrapper {
        public String name;
        public RSSFeed feed;
        public int iconId;

        public FeedWrapper(String name, RSSFeed feed, int iconResId) {
            this.name = name;
            this.feed = feed;
            this.iconId = iconResId;
        }
    }

}
