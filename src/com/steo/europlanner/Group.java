package com.steo.europlanner;

import java.util.ArrayList;

public class Group {

    private final int mGroupId;

    private final ArrayList<Team> mTeams = new ArrayList<Team>();
    private final ArrayList<Fixture> mFixtures = new ArrayList<Fixture>();

    public Group(int groupId) {
        mGroupId = groupId;
    }

    public int getGroupId() {
        return mGroupId;
    }

    public ArrayList<Team> getGroupTeams() {
        return mTeams;
    }

    public ArrayList<Fixture> getFixtures() {
        return mFixtures;
    }

    public void addTeam(Team team) {
        mTeams.add(team);
    }

    public void addFixture(Fixture fixture) {
        mFixtures.add(fixture);
    }

    public void orderTeams() {
        GroupOrdering.order(mTeams, mFixtures);
    }

    public Team getTeamById(int id) {
        for(Team team : mTeams) {
            if(team.getTeamId() == id)
                return team;
        }
        return null;
    }
}
