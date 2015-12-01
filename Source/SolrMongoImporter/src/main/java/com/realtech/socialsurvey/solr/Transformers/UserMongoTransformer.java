package com.realtech.socialsurvey.solr.Transformers;

import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;


public class UserMongoTransformer
{
    public Object transformRow( Map<String, Object> row )
    {
        //Set Review count
        if ( row.get( "reviewCount" ) != null ) {
            Long reviewCount = (Long) row.get( "reviewCount" );
            if ( reviewCount != null ) {
                row.put( "reviewCount", reviewCount );
            }
        }
        //profileUrl
        if ( row.get( "profileUrl" ) != null ) {
            String profileUrl = (String) row.get( "profileUrl" );
            row.put( "profileUrl", profileUrl );
        }
        //profileName
        if ( row.get( "profileName" ) != null ) {
            String profileName = (String) row.get( "profileName" );
            row.put( "profileName", profileName );
        }
        //profileImageUrl
        if ( row.get( "profileImageUrl" ) != null ) {
            String profileImageUrl = (String) row.get( "profileImageUrl" );
            row.put( "profileImageUrl", profileImageUrl );
            row.put( "isProfileImageSet", true );
        } else {
            row.put( "isProfileImageSet", false );
        }
        //profileImageUrlThumbnail
        if ( row.get( "profileImageUrlThumbnail" ) != null ) {
            String profileImageUrlThumbnail = (String) row.get( "profileImageUrlThumbnail" );
            row.put( "profileImageUrlThumbnail", profileImageUrlThumbnail );
        }
        //contact details
        BasicDBObject contactDetailsObject = (BasicDBObject) row.get( "contact_details" );
        System.out.println( "CONTACT DETAILS : " + contactDetailsObject );
        if ( contactDetailsObject != null ) {
            Document contactDetails = Document.parse( new Gson().toJson( contactDetailsObject ) );
            System.out.println( "contact_details : " + contactDetails );
            if ( contactDetails != null ) {
                //title
                System.out.println( "title : " + contactDetails.get( "title" ) );
                if ( contactDetails.get( "title" ) != null ) {
                    String title = (String) contactDetails.get( "title" );
                    row.put( "title", title );
                } else {
                    System.out.println( "Title is empty" );
                }
                //aboutMe
                System.out.println( "aboutMe : " + contactDetails.get( "about_me" ) );
                if ( contactDetails.get( "about_me" ) != null ) {
                    String aboutMe = (String) contactDetails.get( "about_me" );
                    row.put( "aboutMe", aboutMe );
                } else {
                    System.out.println( "aboutMe is empty" );
                }
            }
        }
        return row;
    }
}