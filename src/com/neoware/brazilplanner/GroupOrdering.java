package com.neoware.brazilplanner;

import java.util.ArrayList;
import java.util.Collections;

public class GroupOrdering {

    private static int mTieBreaker;

    private enum TieBreakers {
        POINTS_DECIDE, GOAL_DIFF_ALL_DECIDES, GOALS_SCORED_ALL_DECIDES, GOAL_DIFF_TEAMS_CONCERNED_DECIDES, GOALS_SCORED_TEAMS_CONCERNED_DECIDES, NO_MORE_TIEBREAKERS
    }

    public static void order(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {

        // Order the group based on points only
        setTeamsUnsorted(teams);
        orderByPoints(teams, fixtures);
        Collections.sort(teams, new TeamsComparator());
        setTeamsSorted(teams);
        if (areAllTeamsSorted(teams)) {
            return;
        }

        try {
            orderTies(teams, fixtures);
        } catch (CantOrderTeamsException e) {
            // TODO: Should take some action here if teams cannot be sorted on
            // info
            // available - either sort group based on XML input or prompt user
            return;
        }

    }

    /**
     * Order ties based on: The ranking of each team in each group shall be
     * determined as follows:
     * 
     * a) greatest number of points obtained in all group matches;
     * 
     * b) goal difference in all group matches;
     * 
     * c) greatest number of goals scored in all group matches.
     * 
     * * If two or more teams are equal on the basis of the above three
     * criteria, their rankings shall be determined as follows:
     * 
     * d) greatest number of points obtained in the group matches between the
     * teams concerned;
     * 
     * e) goal difference resulting from the group matches between the teams
     * concerned;
     * 
     * f) greater number of goals scored in all group matches between the teams
     * concerned;
     * 
     * g) drawing of lots by the FIFA Organising Committee.
     */
    private static void orderTies(ArrayList<Team> teams, ArrayList<Fixture> fixtures) throws CantOrderTeamsException {

        ArrayList<Team> ties = new ArrayList<Team>(teams.size());
        for (int t = 0; t < teams.size() - 1;) {
            ties = findTiesFromSubset(t, teams);
            if (ties == null) {
                t++;
                continue;
            }

            // Start at the first tie-breaker
            mTieBreaker = TieBreakers.POINTS_DECIDE.ordinal();

            do {
                clearGoalsValues(ties);
                switch (TieBreakers.values()[mTieBreaker++]) {
                case POINTS_DECIDE:
                    clearPointsValues(ties);
                    orderTiesByPoints(ties, fixtures);
                    break;
                case GOAL_DIFF_ALL_DECIDES:
                    orderTiesByGoalDifferenceAll(ties, fixtures);
                    break;
                case GOALS_SCORED_ALL_DECIDES:
                    orderTiesByGoalsScoredAll(ties, fixtures);
                    break;
                case GOAL_DIFF_TEAMS_CONCERNED_DECIDES:
                    orderTiesByGoalDifference(ties, fixtures);
                    break;
                case GOALS_SCORED_TEAMS_CONCERNED_DECIDES:
                    orderTiesByGoalsScored(ties, fixtures);
                    break;
                case NO_MORE_TIEBREAKERS:
                    break;
                }

                Collections.sort(ties, new TeamsComparator());
                setTeamsSorted(ties);
                // Replace tied teams with (possibly) sorted teams
                replaceTiedWithSortedTeams(t, teams, ties);

            } while (!areAllTeamsSorted(ties) && mTieBreaker <= TieBreakers.NO_MORE_TIEBREAKERS.ordinal());

            // Set next loop for next bunch of tying teams
            t += ties.size();
        }

        // Throw exception if we've failed to sort by now
        if (!areAllTeamsSorted(teams)) {
            throw new CantOrderTeamsException();
        }
    }

    /**
     * Orders teams in the group by points
     */
    private static void orderByPoints(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
        for (Fixture fixture : fixtures) {
            updatePoints(fixture);
        }
    }

    /**
     * Orders tying teams based on points in games played against each other a)
     * higher number of points obtained in the matches among the teams in
     * question;
     */
    private static void orderTiesByPoints(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
        for (Fixture fixture : fixtures) {
            // Only consider games where the tying teams are present
            if (fixture.involvesTeams(teams)) {
                updatePoints(fixture);
            }
        }
    }

    /**
     * Separate tying teams by goal difference in fixtures among those teams b)
     * superior goal difference in the matches among the teams in question (if
     * more than two teams finish equal on points);
     */
    private static void orderTiesByGoalDifference(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
        // This is only used at the end of the group - when all fixtures are
        // played
        if (!areAllFixturesPlayed(fixtures)) {
            return;
        }
        int minTeams = 2; // Only order then more than two teams tied
        if (teams.size() > minTeams) {
            for (Fixture fixture : fixtures) {
                // Only consider games where the tying teams are present
                if (fixture.involvesTeams(teams)) {
                    updateGoalDifference(fixture);
                }
            }
        }
    }

    /**
     * Separate tying teams by goals scored in fixtures among those teams c)
     * higher number of goals scored in the matches among the teams in question
     * (if more than two teams finish equal on points);
     */
    private static void orderTiesByGoalsScored(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
        // This is only used at the end of the group - when all fixtures are
        // played
        if (!areAllFixturesPlayed(fixtures)) {
            return;
        }
        int minTeams = 2; // Only order then more than two teams tied
        if (teams.size() > minTeams) {
            for (Fixture fixture : fixtures) {
                // Only consider games where the tying teams are present
                if (fixture.involvesTeams(teams)) {
                    updateGoalsFor(fixture);
                }
            }
        }
    }

    /**
     * Separate tying teams by goal difference in fixtures among those teams d)
     * superior goal difference in all the group matches;
     */
    private static void orderTiesByGoalDifferenceAll(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
        for (Fixture fixture : fixtures) {
            updateGoalDifference(fixture);
        }
    }

    /**
     * Separate tying teams by goals scored in fixtures among those teams e)
     * higher number of goals scored in all matches
     */
    private static void orderTiesByGoalsScoredAll(ArrayList<Team> teams, ArrayList<Fixture> fixtures) {
        for (Fixture fixture : fixtures) {
            updateGoalsFor(fixture);
        }
    }

    private static void updatePoints(Fixture fixture) {
        if (!fixture.getScore().hasGameBeenPlayed()) {
            return;
        }

        Team homeTeam = fixture.getHomeTeam();
        Team awayTeam = fixture.getAwayTeam();
        int homeScore = fixture.getScore().getHomeScore();
        int awayScore = fixture.getScore().getAwayScore();
        if (homeScore > awayScore) {
            updatePointsWin(homeTeam);
        } else if (homeScore < awayScore) {
            updatePointsWin(awayTeam);
        } else {
            updatePointsTie(homeTeam);
            updatePointsTie(awayTeam);
        }
    }

    private static void updatePointsWin(Team winTeam) {
        int pointsForWin = 3;
        winTeam.addPointsValue(pointsForWin);
    }

    private static void updatePointsTie(Team tieTeam) {
        int pointsForTie = 1;
        tieTeam.addPointsValue(pointsForTie);
    }

    // Count goals for as the comparison value
    private static void updateGoalsFor(Fixture fixture) {
        if (!fixture.getScore().hasGameBeenPlayed()) {
            return;
        }

        Team homeTeam = fixture.getHomeTeam();
        Team awayTeam = fixture.getAwayTeam();
        int homeScore = fixture.getScore().getHomeScore();
        int awayScore = fixture.getScore().getAwayScore();

        homeTeam.addGoalsValue(homeScore);
        awayTeam.addGoalsValue(awayScore);
    }

    // Count goals for as the comparison value
    private static void updateGoalDifference(Fixture fixture) {
        if (!fixture.getScore().hasGameBeenPlayed()) {
            return;
        }

        Team homeTeam = fixture.getHomeTeam();
        Team awayTeam = fixture.getAwayTeam();
        int homeScore = fixture.getScore().getHomeScore();
        int awayScore = fixture.getScore().getAwayScore();

        homeTeam.addGoalsValue(homeScore - awayScore);
        awayTeam.addGoalsValue(awayScore - homeScore);
    }

    // Finds tying teams from a given index (returns null if none)
    private static ArrayList<Team> findTiesFromSubset(int startIndx, ArrayList<Team> teams) {
        if (teams.get(startIndx).isSorted()) {
            return null;
        }

        ArrayList<Team> tiedTeams = new ArrayList<Team>();
        tiedTeams.add(teams.get(startIndx));
        for (int ii = startIndx + 1; ii < teams.size(); ++ii) {
            Team thisTeam = teams.get(ii);
            if (!thisTeam.isSorted() && thisTeam.getPointsValue() == teams.get(startIndx).getPointsValue()) {
                tiedTeams.add(teams.get(ii));
            } else {
                break;
            }
        }
        if (tiedTeams.size() == 1) {
            tiedTeams.get(0).setSorted();
            return null;
        }

        return tiedTeams;
    }

    /*
     * @return The next index from which to try and sort the ArrayList teams
     * Function will replace teams with ties, whether or not we have managed to sort
     * ties on this sorting step - worst case everything remains the same.
     */
    static private int replaceTiedWithSortedTeams(int startIndx, ArrayList<Team> teams, ArrayList<Team> ties) {
        for (int ii = 0; ii < ties.size(); ++ii) {
            teams.set(ii + startIndx, ties.get(ii));
        }
        return startIndx + ties.size() - 1;
    }

    /**
     * After we've done a sorting step need to check if teams are indeed sorted
     */
    private static void setTeamsSorted(ArrayList<Team> teams) {
        for (int t = 0; t < teams.size(); ++t) {
            // If the team is not level with teams either side of it, then it's
            // sorted
            Team tBefore = t > 0 ? teams.get(t - 1) : null;
            Team team = teams.get(t);
            Team tAfter = t + 1 < teams.size() ? teams.get(t + 1) : null;

            // Sorting based on points:
            if ((tBefore == null || tBefore.getPointsValue() > team.getPointsValue())
                    && (tAfter == null || tAfter.getPointsValue() < team.getPointsValue())) {
                team.setSorted();
            }
            // Sorting based on goals:
            else if ((tBefore == null || tBefore.getGoalsValue() > team.getGoalsValue())
                    && (tAfter == null || tAfter.getGoalsValue() < team.getGoalsValue())) {
                team.setSorted();
            }
        }
    }

    /**
     * Flags all teams as unsorted
     */
    private static void setTeamsUnsorted(ArrayList<Team> teams) {
        for (Team t : teams) {
            t.setUnsorted();
        }
    }

    /**
     * Returns true if all the teams are sorted
     */
    private static boolean areAllTeamsSorted(ArrayList<Team> teams) {
        for (Team t : teams) {
            if (!t.isSorted())
                return false;
        }
        return true;
    }

    // Sets points values to zero for the teams
    private static void clearPointsValues(ArrayList<Team> teams) {
        for (Team t : teams) {
            t.setPointsValue(0);
        }
    }

    // Sets comparison values to zero for the teams
    private static void clearGoalsValues(ArrayList<Team> teams) {
        for (Team t : teams) {
            t.setGoalsValue(0);
        }
    }

    /**
     * Counts the number of fixtures for which a score is present in the xml
     * file
     */
    private static boolean areAllFixturesPlayed(ArrayList<Fixture> fixtures) {
        for (Fixture fix : fixtures) {
            if (!fix.hasGameBeenPlayed())
                return false;
        }
        return true;
    }
}

@SuppressWarnings("serial")
class CantOrderTeamsException extends Exception {
}
