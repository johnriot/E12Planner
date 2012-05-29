package com.neoware.europlanner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class TournamentStage {

    protected final int mId;
    protected final ArrayList<Team> mTeams = new ArrayList<Team>();
    protected final ArrayList<Fixture> mFixtures = new ArrayList<Fixture>();
    protected static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("E, dd MMM, HH:mm");

    public TournamentStage(int id) {
        mId = id;
    }

    public abstract View drawView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState, Resources res);

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
