package com.realtech.socialsurvey.core.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.dao.MongoSurveyDetailsDao;
import com.realtech.socialsurvey.core.entities.SurveyDetails;
import com.realtech.socialsurvey.core.entities.SurveyResponse;

/*
 * Provides list of operations to be performed on SurveyDetails collection of mongo. SurveyDetails
 * collection contains list of surveys taken by customers. It also contains answers provided by
 * customers for questions specific to an agent.
 */
@Repository
public class MongoSurveyDetailsDaoImpl implements MongoSurveyDetailsDao {

	private static final Logger LOG = LoggerFactory.getLogger(MongoSurveyDetailsDaoImpl.class);

	public static final String SURVEY_DETAILS_COLLECTION = "SURVEY_DETAILS";

	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * Method to insert survey details into the SURVEY_DETAILS collection.
	 */
	@Override
	public void insertSurveyDetails(SurveyDetails surveyDetails) {
		LOG.info("Method insertSurveyDetails() to insert details of survey started.");
		mongoTemplate.insert(surveyDetails, SURVEY_DETAILS_COLLECTION);
		LOG.info("Method insertSurveyDetails() to insert details of survey finished.");
	}

	/*
	 * Method to update email id by appending timestamp in documents from SurveyDetails collection
	 * by agent id and customer's email-id.
	 */
	@Override
	public void updateEmailForExistingFeedback(long agentId, String customerEmail) {
		LOG.info("Method updateEmailForExistingFeedback() to insert details of survey started.");
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("customerEmail").is(customerEmail));
		Update update = new Update();
		update.set("customerEmail", customerEmail + "#" + new Timestamp(System.currentTimeMillis()));
		mongoTemplate.updateMulti(query, update, SURVEY_DETAILS_COLLECTION);
		LOG.info("Method updateEmailForExistingFeedback() to insert details of survey finished.");
	}

	/*
	 * Method to update questions for survey in SURVEY_DETAILS collection.
	 */
	@Override
	public void updateCustomerResponse(long agentId, String customerEmail, SurveyResponse surveyResponse, int stage) {
		LOG.info("Method updateCustomerResponse() to update response provided by customer started.");
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("customerEmail").is(customerEmail));
		Update update = new Update();
		update.set("stage", stage);
		update.push("surveyResponse", surveyResponse);
		mongoTemplate.updateMulti(query, update, SURVEY_DETAILS_COLLECTION);
		LOG.info("Method updateCustomerResponse() to update response provided by customer finished.");
	}

	/*
	 * Method to update answer and response for gateway question of survey in SURVEY_DETAILS
	 * collection.
	 */
	@Override
	public void updateGatewayAnswer(long agentId, String customerEmail, String mood, String review) {
		LOG.info("Method updateGatewayAnswer() to update review provided by customer started.");
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("customerEmail").is(customerEmail));
		Update update = new Update();
		update.set("stage", CommonConstants.SURVEY_STAGE_COMPLETE);
		update.set("mood", mood);
		update.set("review", review);
		mongoTemplate.updateMulti(query, update, SURVEY_DETAILS_COLLECTION);
		LOG.info("Method updateGatewayAnswer() to update review provided by customer finished.");
	}

	/*
	 * Method to calculate and update final score based upon rating questions.
	 */
	@Override
	public void updateFinalScore(long agentId, String customerEmail) {
		LOG.info("Method to calculate and update final score based upon rating questions started.");
		Query query = new Query();
		List<String> ratingType = new ArrayList<>();
		ratingType.add("sb-range-smiles");
		ratingType.add("sb-range-scale");
		ratingType.add("sb-range-star");
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("customerEmail").is(customerEmail));
		query.addCriteria(Criteria.where("surveyResponse.questionType").in(ratingType));
		List<SurveyResponse> surveyResponse = mongoTemplate.find(query, SurveyDetails.class, SURVEY_DETAILS_COLLECTION)
				.get(CommonConstants.INITIAL_INDEX).getSurveyResponse();
		int noOfResponse = 0;
		int answer = 0;
		for (SurveyResponse response : surveyResponse) {
			if (response.getQuestionType().equals(ratingType.get(CommonConstants.INITIAL_INDEX))
					|| response.getQuestionType().equals(ratingType.get(1)) || response.getQuestionType().equals(ratingType.get(2))) {
				if (response.getAnswer() != null && !response.getAnswer().isEmpty()) {
					answer += Integer.parseInt(response.getAnswer());
					noOfResponse++;
				}
			}
		}
		Update update = new Update();
		update.set("score", answer / noOfResponse);
		mongoTemplate.updateMulti(query, update, SURVEY_DETAILS_COLLECTION);
		LOG.info("Method to calculate and update final score based upon rating questions finished.");
	}
}