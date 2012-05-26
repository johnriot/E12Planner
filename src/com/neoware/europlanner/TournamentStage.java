package com.neoware.europlanner;

import java.util.ArrayList;

public abstract class TournamentStage {

    private final int mId;

    private final ArrayList<Team> mTeams = new ArrayList<Team>();
    private final ArrayList<Fixture> mFixtures = new ArrayList<Fixture>();

    public TournamentStage(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    //@Override
    public ArrayList<Team> getTeams() {
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

    public int getFixturesPlayed(Team team) {
        int played = 0;
        for(Fixture fixture : mFixtures) {
            if(fixture.involvesTeam(team) && fixture.hasGameBeenPlayed()) {
                played++;
            }
        }
        return played;
    }

    public int getGoalsFor(Team team) {
        int goals = 0;
        for(Fixture fixture : mFixtures) {
            if(fixture.involvesTeam(team) && fixture.hasGameBeenPlayed()) {
                goals += fixture.getGoalsFor(team);
            }
        }
        return goals;
    }

    public int getGoalsAgainst(Team team) {
        int goals = 0;
        for(Fixture fixture : mFixtures) {
            if(fixture.involvesTeam(team) && fixture.hasGameBeenPlayed()) {
                goals += fixture.getGoalsAgainst(team);
            }
        }
        return goals;
    }

    public int getPoints(Team team) {
        int points = 0;
        for(Fixture fixture : mFixtures) {
            if(fixture.involvesTeam(team) && fixture.hasGameBeenPlayed()) {
                points += fixture.getPoints(team);
            }
        }
        return points;
    }

}
