package com.steo.europlanner;

import java.util.ArrayList;
import java.util.Date;


public class Fixture {

	private final Team mHomeTeam;
	private final Team mAwayTeam;
    private final int mVenueId;
    private final Date mTime;
    private Score mScore;

    public Fixture(Team homeTeam, Team awayTeam, int venueId, Date time, String score) {
    	mHomeTeam = homeTeam;
    	mAwayTeam = awayTeam;
        mVenueId = venueId;
        mTime = time;
        mScore = new Score(score);
    }

    public Team getHomeTeam() {
        return mHomeTeam;
    }

    public Team getAwayTeam() {
        return mAwayTeam;
    }
    
    public int getHomeTeamId() {
        return mHomeTeam.getTeamId();
    }

    public int getAwayTeamId() {
        return mAwayTeam.getTeamId();
    }

    public int getLocationId() {
        return mVenueId;
    }

    public Date getTime() {
        return mTime;
    }

    // TODO: Change name or return
    public Score getScore() {
        return mScore;
    }
    
    // Returns true if the fixture involves only teams from the teams ArrayList argument
 	public boolean involvesTeams(ArrayList<Team> teams) {
		boolean homeTeamFound = false;
		boolean awayTeamFound = false;
		for(Team team : teams) {
			if(!homeTeamFound && mHomeTeam.getTeamId() == team.getTeamId()) {
				homeTeamFound = true;
				continue;
			}
			if(!awayTeamFound && mAwayTeam.getTeamId() == team.getTeamId()) {
				awayTeamFound = true;
				continue;
			}	
		}
 		return homeTeamFound && awayTeamFound;
 	}
}
