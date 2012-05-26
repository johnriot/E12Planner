
package com.neoware.europlanner;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neoware.rss.RSSFeed;
import com.neoware.rss.RSSItem;
import com.steo.europlanner.R;

public class FeedsAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final ArrayList<FeedDefn> mFeeds = new ArrayList<FeedDefn>();

    public FeedsAdapter(Context context) {
        mContext = context;
    }

    public ArrayList<FeedDefn> getFeeds() {
        return mFeeds;
    }

    public void addFeed(FeedDefn feed) {
        mFeeds.add(feed);
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mFeeds.get(groupPosition).rssFeed.getItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Return a child view. You can load your custom layout here.
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {

        RSSItem item = mFeeds.get(groupPosition).rssFeed.getItems().get(childPosition);

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
        return mFeeds.get(groupPosition).rssFeed.getItems().size();
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

        FeedDefn feed = mFeeds.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.feed_group_layout, null);
        }

        ImageView iconView = (ImageView)convertView.findViewById(R.id.groupIcon);
        iconView.setImageResource(feed.iconId);

        TextView tv = (TextView) convertView.findViewById(R.id.feedGroupTV);
        tv.setText(feed.description);

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

    public static final class FeedDefn {

        public String url;
        public String description;
        public boolean loaded;
        public int iconId;
        public RSSFeed rssFeed;

        public FeedDefn(String url, String description, int iconId) {
            this.url = url;
            this.description = description;
            this.iconId = iconId;
        }
    }

    public void clearFeeds() {
        mFeeds.clear();
    }
}
