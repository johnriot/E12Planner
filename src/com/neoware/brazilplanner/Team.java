package com.neoware.brazilplanner;

import java.util.Comparator;

public class Team {

    private final int mTeamId;
    private int mPointsValue = 0; // points for a team
    private int mGoalsValue = 0; // Can be goal difference, goals for etc.
    private boolean mSorted = false;

    public Team(int teamId) {
        mTeamId = teamId;
    }

    public Team(int teamId, boolean realTeam) {
        mTeamId = teamId;
    }

    public boolean isRealTeam() {
        return mTeamId < Knockout.KNOCKOUT_ID_OFFSET;
    }

    public int getTeamId() {
        return mTeamId;
    }

    public void setPointsValue(int value) {
        mPointsValue = value;
    }

    public int getPointsValue() {
        return mPointsValue;
    }

    public void addPointsValue(int value) {
        mPointsValue += value;
    }

    public void setGoalsValue(int value) {
        mGoalsValue = value;
    }

    public int getGoalsValue() {
        return mGoalsValue;
    }

    public void addGoalsValue(int value) {
        mGoalsValue += value;
    }

    public void setUnsorted() {
        mSorted = false;
    }

    public void setSorted() {
        mSorted = true;
    }

    public boolean isSorted() {
        return mSorted;
    }

    @Override
    public boolean equals(Object obj) {
        return mTeamId == ((Team) obj).mTeamId;
    }
}

class TeamsComparator implements Comparator<Team> {
    @Override
    public int compare(Team team1, Team team2) {
        int pointsDiff = team2.getPointsValue() - team1.getPointsValue();
        if (pointsDiff != 0)
            return pointsDiff;

        return team2.getGoalsValue() - team1.getGoalsValue();
    }
}
