package com.steo.europlanner;


import java.util.ArrayList;
import java.util.Collections;

public class GroupOrdering {
	private int mTieBreaker = 0;
	private static final int NUM_TIEBREAKERS = 4;
	public void order(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
		// Flag all teams unsorted
		flagTeamsUnsorted(teams);
		// First do a straight order by points
		orderByPoints(teams, fixtures);
		
		while(mTieBreaker <= NUM_TIEBREAKERS + 1 && !areAllTeamsSorted(teams)) {
			orderTies(teams, fixtures);
			Collections.sort(teams, new TeamsComparator());
			flagSortedTeams(teams);
			}
	}
	
	/**
	 * 
	 * 
	 */
	private void orderTies(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
		// Find ties then try to separate
		for(int t = 0; t < 4; ++t) {
			ArrayList<Team> ties = new ArrayList<Team>(teams.size());
			ties = findTies(t, teams);
			if(ties == null)
				continue;

			clearComparisonValues(ties);
			switch (mTieBreaker++) {
			case 0:
				orderTiesByPoints(ties, fixtures);
				break;
			case 1:
				orderTiesByGoalDifference(ties, fixtures);
				break;
			case 2:
				orderTiesByGoalsScored(ties, fixtures);
				break;
			case 3:
				orderTiesByGoalDifferenceAll(ties, fixtures);
				break;
			case 4:
				orderTiesByGoalsScoredAll(ties, fixtures);
				break;
			default:
				System.out.println("Can't break the tie.");
				break;
			}
			// Replace tied teams with (possibly) sorted teams 
			for(int ii = 0; ii < ties.size(); ++ii) {
				teams.set(ii + t, ties.get(ii));
			}
			t += ties.size() - 1;
		}
	}

	/**
	Orders teams in the group by points
	*/
	private void orderByPoints(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
		// If we have 4 teams then we order between all teams on points
		if(teams.size() == 4) {
			// Use all fixtures
			for(Fixture fixture: fixtures) {
				// Count all the points for each team based on the score
				updatePoints(fixture);
			}
		}
	}
	
	/**
	Orders tying teams based on points in games played against each other
	a) higher number of points obtained in the matches among the teams in question;
	*/
	private void orderTiesByPoints(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
		for(Fixture fixture: fixtures) {
			// Only consider games where the tying teams are present
			if(fixture.involvesTeams(teams)) {
				updatePoints(fixture);
			}
		}
	}
	
	/**
	 * Separate tying teams by goal difference in fixtures among those teams
	 * b) superior goal difference in the matches among the teams in question
	 * (if more than two teams finish equal on points);
	 */
	private void orderTiesByGoalDifference(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
		if(teams.size() > 2) {
			for(Fixture fixture: fixtures) {
				// Only consider games where the tying teams are present
				if(fixture.involvesTeams(teams)) {
					updateGoalDifference(fixture);
				}
			}
		}
	}
	
	/**
	 * Separate tying teams by goals scored in fixtures among those teams
	 * c) higher number of goals scored in the matches among the teams in
	 * question (if more than two teams finish equal on points);
	 */
	private void orderTiesByGoalsScored(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
		if(teams.size() > 2) {
			for(Fixture fixture : fixtures) {
				// Only consider games where the tying teams are present
				if(fixture.involvesTeams(teams)) {
					updateGoalsFor(fixture);
				}
			}
		}
	}
	
	/**
	 * Separate tying teams by goal difference in fixtures among those teams
	 * d) superior goal difference in all the group matches;
	 * 
	 */
	private void orderTiesByGoalDifferenceAll(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
		for(Fixture fixture : fixtures) {
			updateGoalDifference(fixture);
		}
	}
	
	/**
	 * Separate tying teams by goals scored in fixtures among those teams
	 * c) higher number of goals scored in the matches among the teams in
	 * question (if more than two teams finish equal on points);
	 */
	private void orderTiesByGoalsScoredAll(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
		for(Fixture fixture: fixtures) {
			updateGoalsFor(fixture);
		}
	}
	
	private void updatePoints(Fixture fixture) {
		if(!fixture.getScore().hasGameBeenPlayed()) {
			return;
		}
		
		Team homeTeam = fixture.getHomeTeam();
		Team awayTeam = fixture.getAwayTeam();
		int homeScore = fixture.getScore().getHomeScore();
		int awayScore = fixture.getScore().getAwayScore();
		if(homeScore > awayScore)
			updatePointsWin(homeTeam);
		else if(homeScore < awayScore)
			updatePointsWin(awayTeam);
		else {
			updatePointsTie(homeTeam);
			updatePointsTie(awayTeam);
		}
	}
	
	
	
	private void updatePointsWin(Team winTeam) {
		winTeam.addComparisonValue(3);
	}
	
	private void updatePointsTie(Team tieTeam) {
		tieTeam.addComparisonValue(1);
	}
	
	// Count goals for as the comparison value
	private void updateGoalsFor(Fixture fixture) {
		if(!fixture.getScore().hasGameBeenPlayed()) {
			return;
		}
		
		Team homeTeam = fixture.getHomeTeam();
		Team awayTeam = fixture.getAwayTeam();
		int homeScore = fixture.getScore().getHomeScore();
		int awayScore = fixture.getScore().getAwayScore();
		
		homeTeam.addComparisonValue(homeScore);
		awayTeam.addComparisonValue(awayScore);
	}
	
	// Count goals for as the comparison value
	private void updateGoalDifference(Fixture fixture) {
		if(!fixture.getScore().hasGameBeenPlayed()) {
			return;
		}
		
		Team homeTeam = fixture.getHomeTeam();
		Team awayTeam = fixture.getAwayTeam();
		int homeScore = fixture.getScore().getHomeScore();
		int awayScore = fixture.getScore().getAwayScore();
		
		homeTeam.addComparisonValue(homeScore - awayScore);
		awayTeam.addComparisonValue(awayScore - homeScore);
	}
	
	// Finds tying teams from a given index (returns null if none)
	private ArrayList<Team> findTies(int startIndx, ArrayList<Team> teams) {
		if(teams.get(startIndx).isSorted())
			return null;
		
		ArrayList<Team> teamArray = new ArrayList<Team>();
		teamArray.add(teams.get(startIndx));
		for(int ii = startIndx + 1; ii < 4; ++ii) {
			if(!teams.get(ii).isSorted() && teams.get(ii).getComparisonValue() == teams.get(ii).getComparisonValue())
				teamArray.add(teams.get(ii));
		}
		if(teamArray.size() == 1)
			return null;
		
		return teamArray;
	}
	
	/**
	 * After we've done a sorting step need to check if teams are indeed sorted
	 */
	private void flagSortedTeams(ArrayList<Team> teams) {
		for(int t = 0; t < teams.size(); ++t) {
			// If the team is not level with teams either side of it, then it's sorted
			Team tBefore = t > 0 ? teams.get(t - 1) : null;
			Team team = teams.get(t);
			Team tAfter = t + 1 < teams.size() ? teams.get(t + 1) : null;
			
			if( (tBefore == null || tBefore.getComparisonValue() > team.getComparisonValue())
				&& (tAfter == null || tAfter.getComparisonValue() < team.getComparisonValue())) {
				team.flagSorted();
			}
		}
	}
	
	/**
	 * Flags all teams as unsorted
	 */
	private void flagTeamsUnsorted(ArrayList<Team> teams) {
		for(Team t : teams) {
			t.flagUnsorted();	
		}
	}
	
	/**
	 * Returns true if all the teams are sorted
	 */
	private boolean areAllTeamsSorted(ArrayList<Team> teams) {
		for(Team t: teams) {
			if(!t.isSorted())
				return false;
		}
		return true;
	}
	
	// Sets comparison values to zero for the teams
	private void clearComparisonValues(ArrayList<Team> teams) {
		for(Team t : teams)
			t.setComparsonValue(0);
	}
}

