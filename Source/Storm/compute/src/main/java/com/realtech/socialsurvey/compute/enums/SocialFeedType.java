package com.realtech.socialsurvey.compute.enums;

public enum SocialFeedType
{
    FACEBOOK(0), LINKEDIN(1), TWITTER(2);
    
    private int value;
    
    SocialFeedType(int value){
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }

}
