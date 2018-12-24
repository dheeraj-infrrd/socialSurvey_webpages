package com.realtech.socialsurvey.core.entities;

/**
 * 
 * @author rohitpatidar
 *
 */
public class SurveyStats {

	private double avgScore;
	private long surveyCount;
	private long recentSurveyCount;
	private long incompleteSurveyCount;
	private double spsScore;
	private double searchRankingScore;
	private String latestReview;
	private long gatewayResponseGreat;
	private long gatewayResponseUnpleasant;
	
	public double getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}
	public long getSurveyCount() {
		return surveyCount;
	}
	public void setSurveyCount(long surveyCount) {
		this.surveyCount = surveyCount;
	}
	public long getRecentSurveyCount() {
		return recentSurveyCount;
	}
	public void setRecentSurveyCount(long recentSurveyCount) {
		this.recentSurveyCount = recentSurveyCount;
	}
	public long getIncompleteSurveyCount() {
		return incompleteSurveyCount;
	}
	public void setIncompleteSurveyCount(long incompleteSurveyCount) {
		this.incompleteSurveyCount = incompleteSurveyCount;
	}
	public double getSpsScore() {
		return spsScore;
	}
	public void setSpsScore(double spsScore) {
		this.spsScore = spsScore;
	}
	public double getSearchRankingScore() {
		return searchRankingScore;
	}
	public void setSearchRankingScore(double searchRankingScore) {
		this.searchRankingScore = searchRankingScore;
	}
	public String getLatestReview() {
		return latestReview;
	}
	public void setLatestReview(String latestReview) {
		this.latestReview = latestReview;
	}
	public long getGatewayResponseGreat() {
		return gatewayResponseGreat;
	}
	public void setGatewayResponseGreat(long gatewayResponseGreat) {
		this.gatewayResponseGreat = gatewayResponseGreat;
	}
	public long getGatewayResponseUnpleasant() {
		return gatewayResponseUnpleasant;
	}
	public void setGatewayResponseUnpleasant(long gatewayResponseUnpleasant) {
		this.gatewayResponseUnpleasant = gatewayResponseUnpleasant;
	}
	
}