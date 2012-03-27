package com.steo.europlanner;

import java.util.Date;

import com.steo.europlanner.Team.TeamID;

public class Fixture {

    private final TeamID mHome;
    private final TeamID mAway;
    private final String mLocation;
    private final Date mTime;

    public Fixture(TeamID home, TeamID away, String location, Date time) {
        mHome = home;
        mAway = away;
        mLocation = location;
        mTime = time;
    }

    public TeamID getHomeTeam() {
        return mHome;
    }

    public TeamID getAwayTeam() {
        return mAway;
    }

    public String getLocation() {
        return mLocation;
    }

    public Date getTime() {
        return mTime;
    }
}
