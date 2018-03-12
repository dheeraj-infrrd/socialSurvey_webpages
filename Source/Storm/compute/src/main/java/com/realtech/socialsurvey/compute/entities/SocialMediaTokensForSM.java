package com.realtech.socialsurvey.compute.entities;

import java.io.Serializable;


/**
 * @author manish
 *
 */
public class SocialMediaTokensForSM implements Serializable
{
    private static final long serialVersionUID = 1L;
    private FacebookTokenForSM facebookToken;
    private GoogleTokenForSM googleToken;
    private LinkedInTokenForSM linkedInToken;
    private TwitterTokenForSM twitterToken;


    public FacebookTokenForSM getFacebookToken()
    {
        return facebookToken;
    }


    public void setFacebookToken( FacebookTokenForSM facebookToken )
    {
        this.facebookToken = facebookToken;
    }


    public GoogleTokenForSM getGoogleToken()
    {
        return googleToken;
    }


    public void setGoogleToken( GoogleTokenForSM googleToken )
    {
        this.googleToken = googleToken;
    }


    public LinkedInTokenForSM getLinkedInToken()
    {
        return linkedInToken;
    }


    public void setLinkedInToken( LinkedInTokenForSM linkedInToken )
    {
        this.linkedInToken = linkedInToken;
    }


    public TwitterTokenForSM getTwitterToken()
    {
        return twitterToken;
    }


    public void setTwitterToken( TwitterTokenForSM twitterToken )
    {
        this.twitterToken = twitterToken;
    }


    @Override
    public String toString()
    {
        return "SocialMediaTokensVO [facebookToken=" + facebookToken + ", googleToken=" + googleToken + ", linkedInToken="
            + linkedInToken + ", twitterToken=" + twitterToken + "]";
    }
}