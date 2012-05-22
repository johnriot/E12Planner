package com.steo.europlanner;

import java.util.ArrayList;
import java.util.Date;


public class Fixture {

    private final Team mHomeTeam;
    private final Team mAwayTeam;
    private final int mVenueId;
    private final Date mTime;
    private final Score mScore;

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

    public Score getScore() {
        return mScore;
    }

    // Returns true if the fixture involves only teams from the teams ArrayList argument
    public boolean involvesTeams(ArrayList<Team> teams) {
        return teams.contains(mHomeTeam) && teams.contains(mAwayTeam);
    }

    public boolean involvesTeam(Team team) {
        return team.equals(mHomeTeam) || team.equals(mAwayTeam);
    }

    public boolean hasGameBeenPlayed() {
       return mScore.hasGameBeenPlayed();
    }

    public int getGoalsFor(Team team) {
        if(hasGameBeenPlayed()) {
            if(team.equals(mHomeTeam)) {
                return mScore.getHomeScore();
            }
            else { // awayTeam
                return mScore.getAwayScore();
            }
        }
        return 0;
    }

    public int getGoalsAgainst(Team team) {
        if(hasGameBeenPlayed()) {
            if(team.equals(mHomeTeam)) {
                return mScore.getAwayScore();
            }
            else { // awayTeam
                return mScore.getHomeScore();
            }
        }
        return 0;
    }

    public int getPoints(Team team) {
        if(hasGameBeenPlayed()) {
            if(team.equals(mHomeTeam)) {
                return mScore.getHomePoints();
            }
            else {
                return mScore.getAwayPoints();
            }
        }
        return 0;
    }

}
