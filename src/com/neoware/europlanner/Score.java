package com.neoware.europlanner;

public class Score {
    private Integer mHomeScore;
    private Integer mAwayScore;

    private static final int WIN_POINTS = 3;
    private static final int TIE_POINTS = 1;

    public Integer getHomeScore() { return mHomeScore; }
    public Integer getAwayScore() { return mAwayScore; }

    public boolean hasGameBeenPlayed() {
          return mHomeScore != null && mAwayScore != null;
    }

    public Score(String score) {
        try {
            String[] scores = score.split(":");
            mHomeScore = Integer.parseInt(scores[0]);
            mAwayScore = Integer.parseInt(scores[1]);
            }
        catch(NumberFormatException e) {
            mHomeScore = null;
            mAwayScore = null;
            }
    }

    @Override
    public String toString() {
        if(hasGameBeenPlayed())
            return ("" + mHomeScore + ":" + mAwayScore);
        else
            return "x:x";
    }

    public int getHomePoints() {
        if(mHomeScore > mAwayScore) {
            return WIN_POINTS;
        }
        else if(mHomeScore == mAwayScore) {
            return TIE_POINTS;
        }
        return 0;
    }

    public int getAwayPoints() {
        if(mAwayScore > mHomeScore) {
            return WIN_POINTS;
        }
        else if(mAwayScore == mHomeScore) {
            return TIE_POINTS;
        }
        return 0;
    }
}
