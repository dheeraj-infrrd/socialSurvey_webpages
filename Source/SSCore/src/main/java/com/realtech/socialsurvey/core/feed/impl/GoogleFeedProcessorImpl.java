package com.realtech.socialsurvey.core.feed.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.dao.GenericDao;
import com.realtech.socialsurvey.core.dao.impl.MongoOrganizationUnitSettingDaoImpl;
import com.realtech.socialsurvey.core.entities.FeedStatus;
import com.realtech.socialsurvey.core.entities.GooglePlusSocialPost;
import com.realtech.socialsurvey.core.entities.SocialProfileToken;
import com.realtech.socialsurvey.core.exception.NonFatalException;
import com.realtech.socialsurvey.core.feed.SocialNetworkDataProcessor;

@Component("googleFeed")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GoogleFeedProcessorImpl implements SocialNetworkDataProcessor<GooglePlusPost, SocialProfileToken> {

	private static final Logger LOG = LoggerFactory.getLogger(GoogleFeedProcessorImpl.class);
	private static final String FEED_SOURCE = "google";
	private static final int PAGE_SIZE = 10;

	@Autowired
	private GenericDao<FeedStatus, Long> feedStatusDao;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${GOOGLE_API_KEY}")
	private String googleApiKey;
	
	@Value("${GOOGLE_API_SECRET}")
	private String googleApiSecretKey;
	
	@Value("${GOOGLE_REDIRECT_URI}")
	private String googleApiRedirectURL;
	
	@Value("${GOOGLE_API_SCOPE}")
	private String googleApiScope;
	
	@Value("${GOOGLE_SHARE_URI}")
	private String googleShareURI;

	private FeedStatus status;
	private long profileId;
	private Timestamp lastFetchedTill;
	private String lastFetchedPostId = "";
	
	
	@Override
	@Transactional
	public void preProcess(long iden, String organizationUnit, SocialProfileToken token) {
		List<FeedStatus> statuses = null;
		Map<String, Object> queries = new HashMap<>();
		queries.put(CommonConstants.FEED_SOURCE_COLUMN, FEED_SOURCE);

		switch (organizationUnit) {
			case MongoOrganizationUnitSettingDaoImpl.COMPANY_SETTINGS_COLLECTION:
				queries.put(CommonConstants.COMPANY_ID_COLUMN, iden);

				statuses = feedStatusDao.findByKeyValue(FeedStatus.class, queries);
				if (statuses != null && statuses.size() > 0) {
					status = statuses.get(CommonConstants.INITIAL_INDEX);
				}

				if (status == null) {
					status = new FeedStatus();
					status.setFeedSource(FEED_SOURCE);
					status.setCompanyId(iden);
				}
				else {
					lastFetchedPostId = status.getLastFetchedPostId();
					lastFetchedTill = status.getLastFetchedTill();
				}
				break;

			case MongoOrganizationUnitSettingDaoImpl.REGION_SETTINGS_COLLECTION:
				queries.put(CommonConstants.REGION_ID_COLUMN, iden);

				statuses = feedStatusDao.findByKeyValue(FeedStatus.class, queries);
				if (statuses != null && statuses.size() > 0) {
					status = statuses.get(CommonConstants.INITIAL_INDEX);
				}

				if (status == null) {
					status = new FeedStatus();
					status.setFeedSource(FEED_SOURCE);
					status.setRegionId(iden);
				}
				else {
					lastFetchedPostId = status.getLastFetchedPostId();
					lastFetchedTill = status.getLastFetchedTill();
				}
				break;

			case MongoOrganizationUnitSettingDaoImpl.BRANCH_SETTINGS_COLLECTION:
				queries.put(CommonConstants.BRANCH_ID_COLUMN, iden);

				statuses = feedStatusDao.findByKeyValue(FeedStatus.class, queries);
				if (statuses != null && statuses.size() > 0) {
					status = statuses.get(CommonConstants.INITIAL_INDEX);
				}

				if (status == null) {
					status = new FeedStatus();
					status.setFeedSource(FEED_SOURCE);
					status.setBranchId(iden);
				}
				else {
					lastFetchedPostId = status.getLastFetchedPostId();
					lastFetchedTill = status.getLastFetchedTill();
				}
				break;

			case MongoOrganizationUnitSettingDaoImpl.AGENT_SETTINGS_COLLECTION:
				queries.put(CommonConstants.AGENT_ID_COLUMN, iden);

				statuses = feedStatusDao.findByKeyValue(FeedStatus.class, queries);
				if (statuses != null && statuses.size() > 0) {
					status = statuses.get(CommonConstants.INITIAL_INDEX);
				}

				if (status == null) {
					status = new FeedStatus();
					status.setFeedSource(FEED_SOURCE);
					status.setAgentId(iden);
				}
				else {
					lastFetchedPostId = status.getLastFetchedPostId();
					lastFetchedTill = status.getLastFetchedTill();
				}
				break;
		}
		profileId = iden;

		
	}

	@Override
	public List<GooglePlusPost> fetchFeed(long iden, String organizationUnit, SocialProfileToken token) throws NonFatalException {
		LOG.info("Getting tweets for " + organizationUnit + " with id: " + iden);
		List<GooglePlusPost> posts = new ArrayList<GooglePlusPost>();
		
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			
			String url = createGooglePlusFeedURL(token.getAccessToken());
			HttpGet getRequest = new HttpGet(url);
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new NonFatalException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			InputStreamReader jsonReader = new InputStreamReader(response.getEntity().getContent());	

			fetchPosts(posts,jsonReader);
			
		}
		catch (ClientProtocolException e) {
			LOG.error("Exception in Google feed extration. Reason: " + e.getMessage());
			// setting no.of retries
			status.setRetries(status.getRetries() + 1);
			feedStatusDao.saveOrUpdate(status);
		}
		catch (IOException e) {
			LOG.error("Exception in Google feed extration. Reason: " + e.getMessage());
			// setting no.of retries
			status.setRetries(status.getRetries() + 1);
			feedStatusDao.saveOrUpdate(status);
		}
		return posts;
	}

	private String createGooglePlusFeedURL(String accessToken) {
		StringBuffer url = new StringBuffer("https://www.googleapis.com/plus/v1/people/me/activities/public?access_token=" + accessToken);
		// Add parameters which are required in response fetch results.
		
		url.append("&fields=nextPageToken,updated,items(id,title,published,actor)");
		
		// add maximum results per page
		url.append("&maxResults=").append(PAGE_SIZE);
		
		if(!lastFetchedPostId.isEmpty()){
			url.append("&pageToken=").append(lastFetchedPostId);
		}
		
		return url.toString();
	}

	private Timestamp convertStrigToDate(String str) throws NonFatalException{
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {
			date = format.parse(str);
		}
		catch (ParseException e) {
			throw new NonFatalException("Unable to parse date : ", e.getMessage());
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MILLISECOND, 0);
		return new Timestamp(c.getTimeInMillis());
	}
	
	private void fetchPosts(List<GooglePlusPost> posts, InputStreamReader jsonReader) throws NonFatalException {
        
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject parentObj = jsonParser.parse(jsonReader).getAsJsonObject();
			String nextPageToken = "";
			if(null != parentObj.get("nextPageToken")){
				nextPageToken = parentObj.get("nextPageToken").getAsString();
			}
			 
			Timestamp profileUpdatedOn = convertStrigToDate(parentObj.get("updated").getAsString());
			if(!nextPageToken.isEmpty()){
				lastFetchedPostId = nextPageToken;
			}
			
			JsonArray array = (JsonArray) parentObj.get("items");
			for (JsonElement jsonElement : array) {
				if (jsonElement == null) {
					continue;
				}
				
				JsonObject items = (JsonObject) jsonElement;
				GooglePlusPost post = new GooglePlusPost();
				
				
				Timestamp postCreatedOn = convertStrigToDate(items.get("published").getAsString());
				
				JsonObject actor = (JsonObject) items.get("actor");
				
				post.setId(items.get("id").getAsString());
				post.setCreatedOn(postCreatedOn);
				post.setPost(items.get("title").getAsString());
				post.setPostedBy(actor.get("displayName").getAsString());
				post.setLastUpdatedOn(profileUpdatedOn);
				posts.add(post);
			}
		}
		catch (NonFatalException e) {
			LOG.error("Exception in Google feed extration. Reason: " + e.getMessage());
			throw new NonFatalException(e.getMessage());
		}
	}

	@Override
	public void processFeed(List<GooglePlusPost> posts, String organizationUnit) throws NonFatalException {
		LOG.info("Process tweets for organizationUnit " + organizationUnit);
		Date lastFetchedOn = null;
		
		GooglePlusSocialPost socialPost= null;
		for (GooglePlusPost post : posts) {
			if(lastFetchedTill == null){
				socialPost = new GooglePlusSocialPost();
				socialPost.setPost(post);
				lastFetchedOn = post.getCreatedOn(); 
				socialPost.setPostText(post.getPost());
				socialPost.setPostedBy(post.getPostedBy());
				socialPost.setSource(FEED_SOURCE);
				socialPost.setPostId(post.getId());
				//socialPost.setPostedBy(post.);
				socialPost.setTimeInMillis(post.getCreatedOn().getTime());
			}
			
			if (lastFetchedTill != null && lastFetchedTill.after(post.getCreatedOn())) {
				socialPost = new GooglePlusSocialPost();
				socialPost.setPost(post);
				lastFetchedOn = post.getCreatedOn(); 
				socialPost.setPostText(post.getPost());
				socialPost.setPostedBy(post.getPostedBy());
				socialPost.setSource(FEED_SOURCE);
				socialPost.setPostId(post.getId());
				socialPost.setTimeInMillis(post.getCreatedOn().getTime());
			}
			
			if(socialPost == null)
				break;
			
			switch (organizationUnit) {
				case MongoOrganizationUnitSettingDaoImpl.COMPANY_SETTINGS_COLLECTION:
					socialPost.setCompanyId(profileId);
					break;

				case MongoOrganizationUnitSettingDaoImpl.REGION_SETTINGS_COLLECTION:
					socialPost.setRegionId(profileId);
					break;

				case MongoOrganizationUnitSettingDaoImpl.BRANCH_SETTINGS_COLLECTION:
					socialPost.setBranchId(profileId);
					break;

				case MongoOrganizationUnitSettingDaoImpl.AGENT_SETTINGS_COLLECTION:
					socialPost.setAgentId(profileId);
					break;
			}
			// pushing to mongo
			mongoTemplate.insert(socialPost, CommonConstants.SOCIAL_POST_COLLECTION);
		}
		// updating last fetched details
		if(lastFetchedOn != null){
			lastFetchedTill = new Timestamp(lastFetchedOn.getTime());
		}
	}

	@Override
	@Transactional
	public void postProcess(long iden, String organizationUnit) throws NonFatalException {
		status.setLastFetchedTill(lastFetchedTill);
		status.setLastFetchedPostId(lastFetchedPostId);
		feedStatusDao.saveOrUpdate(status);
	}
}
