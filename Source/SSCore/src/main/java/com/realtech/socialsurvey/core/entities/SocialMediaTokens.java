package com.realtech.socialsurvey.core.entities;

public class SocialMediaTokens {

	private FacebookToken facebookToken;
	private GoogleToken googleToken;
	private LinkedInToken linkedInToken;
	private SocialProfileToken rssToken;
	private TwitterToken twitterToken;
	private YelpToken yelpToken;
	private ZillowToken zillowToken;

	public FacebookToken getFacebookToken() {
		return facebookToken;
	}

	public void setFacebookToken(FacebookToken facebookToken) {
		this.facebookToken = facebookToken;
	}

	public TwitterToken getTwitterToken() {
		return twitterToken;
	}

	public void setTwitterToken(TwitterToken twitterToken) {
		this.twitterToken = twitterToken;
	}

	public LinkedInToken getLinkedInToken() {
		return linkedInToken;
	}

	public void setLinkedInToken(LinkedInToken linkedInToken) {
		this.linkedInToken = linkedInToken;
	}

	public YelpToken getYelpToken() {
		return yelpToken;
	}

	public void setYelpToken(YelpToken yelpToken) {
		this.yelpToken = yelpToken;
	}

	public GoogleToken getGoogleToken() {
		return googleToken;
	}

	public void setGoogleToken(GoogleToken googleToken) {
		this.googleToken = googleToken;
	}

	public SocialProfileToken getRssToken() {
		return rssToken;
	}

	public void setRssToken(SocialProfileToken rssToken) {
		this.rssToken = rssToken;
	}

	public ZillowToken getZillowToken() {
		return zillowToken;
	}

	public void setZillowToken(ZillowToken zillowToken) {
		this.zillowToken = zillowToken;
	}

	@Override
	public String toString() {
		return "SocialMediaTokens [facebookToken=" + facebookToken + ", twitterToken=" + twitterToken + ", linkdenInToken=" + linkedInToken
				+ ", yelpToken=" + yelpToken + ", googleToken=" + googleToken + ", rssToken=" + rssToken + ", zillowToken=" + zillowToken + "]";
	}
}