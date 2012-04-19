package com.steo.europlanner;

import java.util.Comparator;

public class Team {

    private final int mTeamId;
	private int mComparisonValue = 0; // can be points, goal diff, goals scored etc
	private boolean mSorted = false;

    public Team(int teamId) {
        mTeamId = teamId;
    }

    public int getTeamId() {
        return mTeamId;
    }
    
    public void setComparsonValue(int value) {
    	mComparisonValue = value;
    }
    
	public int getComparisonValue() {
		return mComparisonValue;
	}
	
	public void addComparisonValue(int value) {
		mComparisonValue += value;
	}
	
	public void flagUnsorted() {
		mSorted = false;
	}
	
	public void flagSorted() {
		mSorted = true;
	}
	
	public boolean isSorted() {
		return mSorted;
	}
}

class TeamsComparator implements Comparator<Team> {
	public int compare(Team team1, Team team2) {
		return team2.getComparisonValue() - team1.getComparisonValue();
	}
}
