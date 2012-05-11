package com.steo.europlanner;

import java.util.ArrayList;

public abstract class TournamentStage {
	
	private final int mId;
	
	private final ArrayList<Team> mTeams = new ArrayList<Team>();
    private final ArrayList<Fixture> mFixtures = new ArrayList<Fixture>();
    private final GroupOrdering mOrderer = new GroupOrdering();
	
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
		mOrderer.order(mTeams, mFixtures);
	}
    
	public Team getTeamById(int id) {
		for(Team team : mTeams) {
			if(team.getTeamId() == id)
				return team;
		}
		return null;
	}
}
