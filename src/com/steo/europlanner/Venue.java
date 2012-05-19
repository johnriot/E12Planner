package com.steo.europlanner;

import java.util.Set;
import java.util.TreeSet;

public class Venue {

    private final int mVenueId;
    private final Set<Integer> mGroupKnockoutHosting = new TreeSet<Integer>();

    public Venue(int venueId) {
        mVenueId = venueId;
    }

    public int getVenueId() {
        return mVenueId;
    }

    public void addGroupKnockoutId(int groupKnockoutId) {
        mGroupKnockoutHosting.add(groupKnockoutId);
    }

    public Set<Integer> getGroupKnockoutIds() {
        return mGroupKnockoutHosting;
    }
}
