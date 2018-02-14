package com.realtech.socialsurvey.compute.topology.bolts.monitor;

import java.util.List;

import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.realtech.socialsurvey.compute.entities.SocialMediaTokenResponse;
import com.realtech.socialsurvey.compute.entities.TwitterToken;
import com.realtech.socialsurvey.compute.entities.response.SocialResponseObject;
import com.realtech.socialsurvey.compute.entities.response.TwitterFeedData;
import com.realtech.socialsurvey.compute.enums.SocialFeedType;
import com.realtech.socialsurvey.compute.feeds.TwitterFeedProcessor;
import com.realtech.socialsurvey.compute.feeds.impl.TwitterFeedProcessorImpl;
import com.realtech.socialsurvey.compute.topology.bolts.BaseComputeBolt;


/**
 * @author manish
 *
 */
public class TwitterFeedExtractorBolt extends BaseComputeBolt
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger( TwitterFeedExtractorBolt.class );
    private TwitterFeedProcessor twitterFeedProcessor = new TwitterFeedProcessorImpl();

    private boolean isRateLimitExceeded()
    {
        // TODO ckech for ratelimiting for facebook api (based on user-id, page-id, )
        return false;
    }


    @Override
    public void execute( Tuple input )
    {
        try {
            SocialMediaTokenResponse mediaToken = (SocialMediaTokenResponse) input.getValueByField( "mediaToken" );

            Long companyId = mediaToken.getCompanyId();
            TwitterToken token = null;
            if ( mediaToken.getSocialMediaTokens() != null ) {
                token = mediaToken.getSocialMediaTokens().getTwitterToken();
            }

            // Check rate limiting for company
            if ( isRateLimitExceeded( /* pass media token*/ ) ) {
                LOG.warn( "Rate limit exceeded" );
            }

            //Call facebook api to get facebook page post.
            List<TwitterFeedData> response = twitterFeedProcessor.fetchFeed( companyId, token );
            LOG.debug( "Total tweet fetched : {}", response.size() );
            for ( TwitterFeedData twitterFeedData : response ) {
                SocialResponseObject<TwitterFeedData> responseWrapper = createSocialResponseObject( companyId,
                    twitterFeedData );

                String responseWrapperString = new Gson().toJson( responseWrapper );
                _collector.emit( new Values( companyId.toString(), responseWrapperString ) );
                LOG.trace( "Emitted successfully {}", responseWrapper );
            }
            // End loop for companies
        } catch ( Exception e ) {
            LOG.error( "Error while fetching post from Twitter.", e );
        }
    }


    /**
     *  Create SocialResponseObject with common fields
     * @param companyId
     * @param twitterFeedData
     * @return
     */
    private SocialResponseObject<TwitterFeedData> createSocialResponseObject( long companyId, TwitterFeedData twitterFeedData )
    {
        SocialResponseObject<TwitterFeedData> responseWrapper = new SocialResponseObject<>( companyId, SocialFeedType.TWITTER,
            twitterFeedData.getText(), twitterFeedData, 1 );

        if ( twitterFeedData.getText() != null && !twitterFeedData.getText().isEmpty() ) {
            responseWrapper.setHash( responseWrapper.getText().hashCode() );
        }
        responseWrapper.setPostId(String.valueOf(twitterFeedData.getId()));
        if ( twitterFeedData.getCreatedAt() != null ) {
            responseWrapper.setCreatedTime( twitterFeedData.getCreatedAt().getTime() );
            responseWrapper.setUpdatedTime( twitterFeedData.getCreatedAt().getTime() );
        }

        return responseWrapper;
    }


    @Override
    public void declareOutputFields( OutputFieldsDeclarer declarer )
    {
        declarer.declare( new Fields( "companyId", "post" ) );
    }


    public TwitterFeedProcessor getTwitterFeedProcessor()
    {
        return twitterFeedProcessor;
    }


    public void setTwitterFeedProcessor( TwitterFeedProcessor twitterFeedProcessor )
    {
        this.twitterFeedProcessor = twitterFeedProcessor;
    }
}
