package com.realtech.socialsurvey.core.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.mongodb.BasicDBObject;
import com.realtech.socialsurvey.core.dao.OrganizationUnitSettingsDao;
import com.realtech.socialsurvey.core.entities.AgentSettings;
import com.realtech.socialsurvey.core.entities.OrganizationUnitSettings;

/**
 * Mongo implementation of settings
 *
 */
@Repository
public class MongoOrganizationUnitSettingDaoImpl implements OrganizationUnitSettingsDao, InitializingBean {
	
	public static final String COMPANY_SETTINGS_COLLECTION = "COMPANY_SETTINGS";
	public static final String REGION_SETTINGS_COLLECTION = "REGION_SETTINGS";
	public static final String BRANCH_SETTINGS_COLLECTION = "BRANCH_SETTINGS";
	public static final String AGENT_SETTINGS_COLLECTION = "AGENT_SETTINGS";
	public static final String KEY_CRM_INFO = "crm_info";
	public static final String KEY_MAIL_CONTENT = "mail_content";
	
	private static final String KEY_IDENTIFIER = "iden";
	
	
	private static final Logger LOG = LoggerFactory.getLogger(MongoOrganizationUnitSettingDaoImpl.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insertOrganizationUnitSettings(OrganizationUnitSettings organizationUnitSettings, String collectionName) {
		LOG.info("Creating "+collectionName+" document. Organiztion Unit id: "+organizationUnitSettings.getIden());
		LOG.debug("Inserting into "+collectionName+". Object: "+organizationUnitSettings.toString());
		mongoTemplate.insert(organizationUnitSettings, collectionName);
		LOG.info("Inserted into "+collectionName);
	}

	@Override
	public void insertAgentSettings(AgentSettings agentSettings) {
		LOG.info("Inseting agent settings. Agent id: "+agentSettings.getIden());
		LOG.debug("Inserting agent settings: "+agentSettings.toString());
		mongoTemplate.insert(agentSettings, AGENT_SETTINGS_COLLECTION);
		LOG.info("Inserted into agent settings");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("Checking if collections are created in mongodb");
		if(!mongoTemplate.collectionExists(COMPANY_SETTINGS_COLLECTION)){
			LOG.info("Creating "+COMPANY_SETTINGS_COLLECTION);
			mongoTemplate.createCollection(COMPANY_SETTINGS_COLLECTION);
		}
		if(!mongoTemplate.collectionExists(REGION_SETTINGS_COLLECTION)){
			LOG.info("Creating "+REGION_SETTINGS_COLLECTION);
			mongoTemplate.createCollection(REGION_SETTINGS_COLLECTION);
		}
		if(!mongoTemplate.collectionExists(BRANCH_SETTINGS_COLLECTION)){
			LOG.info("Creating "+BRANCH_SETTINGS_COLLECTION);
			mongoTemplate.createCollection(BRANCH_SETTINGS_COLLECTION);
		}
		if(!mongoTemplate.collectionExists(AGENT_SETTINGS_COLLECTION)){
			LOG.info("Creating "+AGENT_SETTINGS_COLLECTION);
			mongoTemplate.createCollection(AGENT_SETTINGS_COLLECTION);
		}
	}

	@Override
	public OrganizationUnitSettings fetchOrganizationUnitSettingsById(long identifier, String collectionName) {
		LOG.info("Fetch organization unit settings from "+collectionName+" for id: "+identifier);
		OrganizationUnitSettings settings = mongoTemplate.findOne(new BasicQuery(new BasicDBObject(KEY_IDENTIFIER,identifier)), OrganizationUnitSettings.class, collectionName);
		return settings;
	}

	@Override
	public AgentSettings fetchAgentSettingsById(long identifier) {
		LOG.info("Fetch agent settings from for id: "+identifier);
		AgentSettings settings = mongoTemplate.findOne(new BasicQuery(new BasicDBObject(KEY_IDENTIFIER,identifier)), AgentSettings.class, AGENT_SETTINGS_COLLECTION);
		return settings;
	}
	
	@Override
	public void updateParticularKeyOrganizationUnitSettings(String keyToUpdate, Object updatedRecord, OrganizationUnitSettings unitSettings, String collectionName){
		LOG.info("Updating unit setting in "+collectionName+" with "+unitSettings+" for key: "+keyToUpdate+" wtih value: "+updatedRecord);
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(unitSettings.getId()));
		Update update =  new Update().set(keyToUpdate, updatedRecord);
		LOG.debug("Updating the unit settings");
		mongoTemplate.updateFirst(query, update, OrganizationUnitSettings.class, collectionName);
		LOG.info("Updated the unit setting");
	}

}
