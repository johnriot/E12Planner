package com.steo.europlanner;

public class Score {
	private Integer mHomeScore;
	private Integer mAwayScore;
	
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
	
	public String toString() {
		if(hasGameBeenPlayed())
			return ("" + mHomeScore + ":" + mAwayScore);
		else
			return "x:x";
	}
}
