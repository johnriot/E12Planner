package com.steo.europlanner;

import java.util.Date;


public class Fixture {

    private final int mHomeTeamId;
    private final int mAwayTeamId;
    private final int mVenueId;
    private final Date mTime;
    private String mScore;

    public Fixture(int homeTeamId, int awayTeamId, int venueId, Date time, String score) {
        mHomeTeamId = homeTeamId;
        mAwayTeamId = awayTeamId;
        mVenueId = venueId;
        mTime = time;
    }

    public int getHomeTeamId() {
        return mHomeTeamId;
    }

    public int getAwayTeamId() {
        return mAwayTeamId;
    }

    public int getLocationId() {
        return mVenueId;
    }

    public Date getTime() {
        return mTime;
    }

    public String getScore() {
        return mScore;
    }
}
