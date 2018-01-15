package com.realtech.socialsurvey.compute.common;

/**
 * Constants for the application
 * @author nishit
 *
 */
public final class ComputeConstants
{
    private ComputeConstants()
    {}

    public static final String SEND_EMAIL_THROUGH_SOCIALSURVEY_ME = "socialsurvey.me";
    public static final String SEND_EMAIL_THROUGH_SOCIALSURVEY_US = "socialsurvey.us";

    // sendgrid keys
    public static final String SENDGRID_ME_API_KEY = "sendgrid.me.api.key";
    public static final String SENDGRID_US_API_KEY = "sendgrid.us.api.key";

    //amazon keys
    public static final String AMAZON_ACCESS_KEY = "amazon.access.key";
    public static final String AMAZON_SECRET_KEY = "amazon.secret.key";

    // property files
    public static final String APPLICATION_PROPERTY_FILE = "application";

    // Runtime params map
    public static final String RUNTIME_PARAMS = "RUNTIME_PARAMS";
    public static final String PROFILE = "profile";

    // database properties
    public static final String MONGO_DB_URI = "MONGO_DB_URI";
    public static final String STREAM_DATABASE = "stream_db";

    // API end point properties
    public static final String SOLR_API_ENDPOINT = "SOLR_API_ENDPOINT";
    
    // Zookeper end point
    public static final String ZOOKEEPER_BROKERS_ENDPOINT = "ZOOKEEPER_BROKERS_ENDPOINT";
    
    // SS api end point
    public static final String SS_API_ENDPOINT = "SS_API_ENDPOINT";

    // Misc properties
    public static final String SALES_LEAD_EMAIL_ADDRESS = "SALES_LEAD_EMAIL_ADDRESS";
    public static final String ADMIN_EMAIL_ADDRESS = "ADMIN_EMAIL_ADDRESS";
    public static final String ADMIN_EMAIL_ADDRESS_NAME = "ADMIN_EMAIL_ADDRESS_NAME";
    public static final String SS_DATABASE = "ss_db";

    // Amazon S3 constants
    public static final String AMAZON_BUCKET = "AMAZON_BUCKET";
    public static final String AMAZON_REPORTS_BUCKET = "AMAZON_REPORTS_BUCKET";
    public static final String AMAZON_ENDPOINT = "AMAZON_ENDPOINT";
    
    public static final String FILEUPLOAD_DIRECTORY_LOCATION = "FILEUPLOAD_DIRECTORY_LOCATION";
    
}