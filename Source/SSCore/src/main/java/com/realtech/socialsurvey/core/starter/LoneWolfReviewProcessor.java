package com.realtech.socialsurvey.core.starter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.commons.Utils;
import com.realtech.socialsurvey.core.dao.impl.MongoOrganizationUnitSettingDaoImpl;
import com.realtech.socialsurvey.core.entities.Company;
import com.realtech.socialsurvey.core.entities.CrmBatchTracker;
import com.realtech.socialsurvey.core.entities.LoneWolfAgentCommission;
import com.realtech.socialsurvey.core.entities.LoneWolfClientContact;
import com.realtech.socialsurvey.core.entities.LoneWolfCrmInfo;
import com.realtech.socialsurvey.core.entities.LoneWolfMember;
import com.realtech.socialsurvey.core.entities.LoneWolfTier;
import com.realtech.socialsurvey.core.entities.LoneWolfTransaction;
import com.realtech.socialsurvey.core.entities.OrganizationUnitSettings;
import com.realtech.socialsurvey.core.entities.Region;
import com.realtech.socialsurvey.core.entities.SurveyPreInitiation;
import com.realtech.socialsurvey.core.entities.User;
import com.realtech.socialsurvey.core.enums.LoanWolfContactType;
import com.realtech.socialsurvey.core.enums.LoanWolfTransactionClassificationMode;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.exception.NoRecordsFetchedException;
import com.realtech.socialsurvey.core.services.batchtracker.BatchTrackerService;
import com.realtech.socialsurvey.core.services.crmbatchtracker.CRMBatchTrackerService;
import com.realtech.socialsurvey.core.services.crmbatchtrackerhistory.CRMBatchTrackerHistoryService;
import com.realtech.socialsurvey.core.services.lonewolf.LoneWolfIntegrationService;
import com.realtech.socialsurvey.core.services.mail.EmailServices;
import com.realtech.socialsurvey.core.services.mail.UndeliveredEmailException;
import com.realtech.socialsurvey.core.services.organizationmanagement.OrganizationManagementService;
import com.realtech.socialsurvey.core.services.organizationmanagement.UserManagementService;
import com.realtech.socialsurvey.core.services.surveybuilder.SurveyHandler;
import com.realtech.socialsurvey.core.utils.LoneWolfRestUtils;


@Component ( "lonewolfreviewprocessor")
public class LoneWolfReviewProcessor extends QuartzJobBean
{
    private static final Logger LOG = LoggerFactory.getLogger( LoneWolfReviewProcessor.class );

    private BatchTrackerService batchTrackerService;
    private CRMBatchTrackerService crmBatchTrackerService;
    private CRMBatchTrackerHistoryService crmBatchTrackerHistoryService;
    private EmailServices emailServices;
    private OrganizationManagementService organizationManagementService;
    private UserManagementService userManagementService;
    private SurveyHandler surveyHandler;
    private Utils utils;
    private int newRecordFoundCount = 0;
    private String applicationAdminEmail;
    private String applicationAdminName;
    private String maskEmail;
    private String apiToken;
    private String secretKey;
    private LoneWolfIntegrationService loneWolfIntegrationService;
    private LoneWolfRestUtils loneWolfRestUtils;


    @Override
    protected void executeInternal( JobExecutionContext jobExecutionContext ) throws JobExecutionException
    {
        try {
            LOG.info( "Executing lonewolf review processor" );

            initializeDependencies( jobExecutionContext.getMergedJobDataMap() );

            // update last start time
            batchTrackerService.getLastRunEndTimeAndUpdateLastStartTimeByBatchType(
                CommonConstants.BATCH_TYPE_LONE_WOLF_REVIEW_PROCESSOR, CommonConstants.BATCH_NAME_LONE_WOLF_REVIEW_PROCESSOR );

            startLoneWolfFeedProcessing( MongoOrganizationUnitSettingDaoImpl.COMPANY_SETTINGS_COLLECTION );
            startLoneWolfFeedProcessing( MongoOrganizationUnitSettingDaoImpl.REGION_SETTINGS_COLLECTION );
            startLoneWolfFeedProcessing( MongoOrganizationUnitSettingDaoImpl.BRANCH_SETTINGS_COLLECTION );
            startLoneWolfFeedProcessing( MongoOrganizationUnitSettingDaoImpl.AGENT_SETTINGS_COLLECTION );

            //updating last run time for batch in database
            batchTrackerService.updateLastRunEndTimeByBatchType( CommonConstants.BATCH_TYPE_LONE_WOLF_REVIEW_PROCESSOR );
        } catch ( Exception e ) {
            LOG.error( "Error in lonewolf review processor", e );
            try {
                //update batch tracker with error message
                batchTrackerService.updateErrorForBatchTrackerByBatchType(
                    CommonConstants.BATCH_TYPE_LONE_WOLF_REVIEW_PROCESSOR, e.getMessage() );

                //send report bug mail to admin
                batchTrackerService.sendMailToAdminRegardingBatchError( CommonConstants.BATCH_NAME_LONE_WOLF_REVIEW_PROCESSOR,
                    System.currentTimeMillis(), e );
            } catch ( NoRecordsFetchedException | InvalidInputException e1 ) {
                LOG.error( "Error while updating error message in lonewolf review processor " );
            } catch ( UndeliveredEmailException e1 ) {
                LOG.error( "Error while sending report execption mail to admin " );
            }
        }
    }


    private void startLoneWolfFeedProcessing( String collectionName )
    {
        LOG.debug( "Inside method startLoneWolfFeedProcessing for collection : " + collectionName );

        try {
            CrmBatchTracker crmBatchTracker = null;

            // to maintain entry in crm batch tracker, get entity type and id
            String entityType = null;
            long entityId;
            if ( collectionName.equalsIgnoreCase( MongoOrganizationUnitSettingDaoImpl.COMPANY_SETTINGS_COLLECTION ) ) {
                entityType = CommonConstants.COMPANY_ID_COLUMN;
            } else if ( collectionName.equalsIgnoreCase( MongoOrganizationUnitSettingDaoImpl.REGION_SETTINGS_COLLECTION ) ) {
                entityType = CommonConstants.REGION_ID_COLUMN;
            } else if ( collectionName.equalsIgnoreCase( MongoOrganizationUnitSettingDaoImpl.BRANCH_SETTINGS_COLLECTION ) ) {
                entityType = CommonConstants.BRANCH_ID_COLUMN;
            } else if ( collectionName.equalsIgnoreCase( MongoOrganizationUnitSettingDaoImpl.AGENT_SETTINGS_COLLECTION ) ) {
                entityType = CommonConstants.AGENT_ID_COLUMN;
            }

            List<OrganizationUnitSettings> organizationUnitSettingsList = organizationManagementService
                .getOrganizationUnitSettingsForCRMSource( CommonConstants.CRM_SOURCE_LONEWOLF, collectionName );
            if ( organizationUnitSettingsList != null && !organizationUnitSettingsList.isEmpty() ) {

                LOG.info( "Looping through crm list of size: " + organizationUnitSettingsList.size() );
                for ( OrganizationUnitSettings organizationUnitSettings : organizationUnitSettingsList ) {

                    LOG.info( "Getting lonewolf records for company id: " + organizationUnitSettings.getIden() );
                    LoneWolfCrmInfo loneWolfCrmInfo = (LoneWolfCrmInfo) organizationUnitSettings.getCrm_info();
                    if ( StringUtils.isNotEmpty( loneWolfCrmInfo.getClientCode() ) ) {

                        entityId = organizationUnitSettings.getIden();

                        //make an entry in crm batch tracker and update last run start time
                        crmBatchTrackerService.getLastRunEndTimeAndUpdateLastStartTimeByEntityTypeAndSourceType( entityType,
                            entityId, CommonConstants.CRM_SOURCE_LONEWOLF );
                        try {

                            //Fetch transactions data from lone wolf.
                            int skip = 0;
                            String filter = loneWolfRestUtils.generateFilterQueryParamFor();
                            List<LoneWolfTransaction> loneWolfTransactionsBatch = null;
                            List<LoneWolfTransaction> loneWolfTransactions = new ArrayList<LoneWolfTransaction>();
                            do {
                                LOG.debug( "Fetching record in batch of " + CommonConstants.LONEWOLF_TRANSACTION_API_BATCH_SIZE );
                                LOG.debug( "Fetching records start from " + skip );
                                // use linked hash map to maintain the order
                                Map<String, String> queryParam = new LinkedHashMap<String, String>();
                                queryParam.put( CommonConstants.LONEWOLF_QUERY_PARAM_$TOP, String.valueOf( 1000 ) );
                                queryParam.put( CommonConstants.LONEWOLF_QUERY_PARAM_$FILTER, filter );
                                queryParam.put( CommonConstants.LONEWOLF_QUERY_PARAM_$ORDERBY,
                                    CommonConstants.LONEWOLF_QUERY_PARAM_ORDERBY_VALUE );
                                queryParam.put( CommonConstants.LONEWOLF_QUERY_PARAM_$SKIP, String.valueOf( skip ) );
                                
                                loneWolfTransactionsBatch = loneWolfIntegrationService.fetchLoneWolfTransactionsData(
                                    secretKey, apiToken, loneWolfCrmInfo.getClientCode(), queryParam );
                                loneWolfTransactions.addAll( loneWolfTransactionsBatch );

                                skip += CommonConstants.LONEWOLF_TRANSACTION_API_BATCH_SIZE;

                            } while ( loneWolfTransactionsBatch != null && loneWolfTransactionsBatch.size() == CommonConstants.LONEWOLF_TRANSACTION_API_BATCH_SIZE );

                            //Fetch members data from lonewolf.
                            Map<String, LoneWolfMember> membersById = fetchLoneWolfMembersDataMap( secretKey, apiToken,
                                loneWolfCrmInfo.getClientCode() );

                            //Process lone wolf transactions and put it in survey pre initiation table to send surveys
                            processLoneWolfTransactions( loneWolfTransactions, membersById, collectionName, entityId );

                            //insert crmbatchTrackerHistory with count of Records Fetched
                            crmBatchTracker = crmBatchTrackerService.getCrmBatchTracker( entityType, entityId,
                                CommonConstants.CRM_SOURCE_LONEWOLF );

                            if ( crmBatchTracker != null ) {
                                crmBatchTrackerHistoryService.insertCrmBatchTrackerHistory( newRecordFoundCount,
                                    crmBatchTracker.getCrmBatchTrackerId(), CommonConstants.CRM_SOURCE_LONEWOLF );
                            }

                            // update  last run end time and count of new records found in crm batch tracker
                            crmBatchTrackerService.updateLastRunEndTimeByEntityTypeAndSourceType( entityType, entityId,
                                CommonConstants.CRM_SOURCE_LONEWOLF, newRecordFoundCount );

                        } catch ( Exception e ) {
                            LOG.error( "Exception caught for collection " + collectionName + "having iden as "
                                + organizationUnitSettings.getIden(), e );

                            // update  error message in crm batch tracker
                            crmBatchTrackerService.updateErrorForBatchTrackerByEntityTypeAndSourceType( entityType, entityId,
                                CommonConstants.CRM_SOURCE_LONEWOLF, e.getMessage() );
                            try {
                                LOG.info( "Building error message for the auto post failure" );
                                String errorMsg = "Error while processing lonewolf feed for collection " + collectionName
                                    + ", profile name " + organizationUnitSettings.getProfileName() + " with iden "
                                    + organizationUnitSettings.getIden() + "at time " + new Date( System.currentTimeMillis() )
                                    + " <br>";
                                errorMsg += "<br>" + e.getMessage() + "<br><br>";
                                errorMsg += "<br>StackTrace : <br>"
                                    + ExceptionUtils.getStackTrace( e ).replaceAll( "\n", "<br>" ) + "<br>";
                                LOG.info( "Sending bug mail to admin" );
                                emailServices.sendReportBugMailToAdmin( applicationAdminName, errorMsg, applicationAdminEmail );
                                LOG.info( "Sent bug mail to admin for the auto post failure" );
                            } catch ( UndeliveredEmailException ude ) {
                                LOG.error( "error while sending report bug mail to admin ", ude );
                            } catch ( InvalidInputException iie ) {
                                LOG.error( "error while sending report bug mail to admin ", iie );
                            }
                        }
                    }
                }
            }
        } catch ( InvalidInputException | NoRecordsFetchedException e1 ) {
            LOG.info( "Could not get list of lone wolf records" );
        }
    }


    //TODO refactor this code and add all business scenarios
    private void processLoneWolfTransactions( List<LoneWolfTransaction> loneWolfTransactions,
        Map<String, LoneWolfMember> membersByName, String collectionName, long organizationUnitId )
    {
        LOG.info( "method processLoneWolfTransactions started" );
        if ( !loneWolfTransactions.isEmpty() ) {
            for ( LoneWolfTransaction transaction : loneWolfTransactions ) {
                try {
                    if ( !isTransactionValid( transaction ) )
                        continue;

                    //get seller agent and client detail
                    Map<String, LoneWolfClientContact> clientContactsForTransaction = getClientContactForTransaction( transaction );
                    LoneWolfClientContact sellerClientContact = clientContactsForTransaction.get( LoanWolfContactType.SELLER
                        .getCode() );
                    LoneWolfClientContact buyerClientContact = clientContactsForTransaction.get( LoanWolfContactType.BUYER
                        .getCode() );

                    //get buyer seller member detail
                    Map<String, LoneWolfMember> membersForTransaction = getMembersForTransaction( transaction, membersByName );
                    LoneWolfMember sellerMember = membersForTransaction.get( LoanWolfTransactionClassificationMode.SELLING
                        .getMode() );
                    LoneWolfMember buyerMember = membersForTransaction.get( LoanWolfTransactionClassificationMode.LISTING
                        .getMode() );

                    //get classification code
                    String classificationCode = transaction.getClassification().getCode();

                    //generate survey pre initiation entry based on classification code
                    if ( classificationCode.equals( LoanWolfTransactionClassificationMode.SELLING.getMode() ) ) {
                        generateSurveyPreinitiaionAndSave( sellerClientContact, sellerMember, collectionName,
                            organizationUnitId, transaction.getNumber() );
                    }
                    if ( classificationCode.equals( LoanWolfTransactionClassificationMode.LISTING.getMode() ) ) {
                        generateSurveyPreinitiaionAndSave( buyerClientContact, buyerMember, collectionName, organizationUnitId,
                            transaction.getNumber() );
                    }
                    if ( classificationCode.equals( LoanWolfTransactionClassificationMode.OFFICEAGENTS.getMode() ) ) {
                        generateSurveyPreinitiaionAndSave( sellerClientContact, sellerMember, collectionName,
                            organizationUnitId, transaction.getNumber() );
                        generateSurveyPreinitiaionAndSave( buyerClientContact, buyerMember, collectionName, organizationUnitId,
                            transaction.getNumber() );
                    }
                    if ( classificationCode.equals( LoanWolfTransactionClassificationMode.DOUBLEAGENT.getMode() ) ) {
                        generateSurveyPreinitiaionAndSave( sellerClientContact, sellerMember, collectionName,
                            organizationUnitId, transaction.getNumber() );
                        generateSurveyPreinitiaionAndSave( buyerClientContact, buyerMember, collectionName, organizationUnitId,
                            transaction.getNumber() );
                    }

                } catch ( Exception e ) {
                    LOG.error( "Error while processing transaction " + transaction.getId() );
                }
            }
        }
    }


    /**
     * 
     * @param transaction
     * @return
     */
    private boolean isTransactionValid( LoneWolfTransaction transaction )
    {

        LOG.debug( "Metohd isTransactionValid started for transaction " + transaction.getId() );
        //check if minimum 1 client is there in the transaction
        if ( transaction.getClientContacts() == null || transaction.getClientContacts().size() == 0 )
            return false;

        //check if minimum 1 tire is there
        if ( transaction.getTiers() == null || transaction.getTiers().size() == 0 )
            return false;

        LoneWolfTier tier = transaction.getTiers().get( 0 );
        //check if minimum 1 agent is there
        if ( tier == null || tier.getAgentCommissions() == null || tier.getAgentCommissions().size() == 0 )
            return false;

        if ( transaction.getClassification() == null || transaction.getClassification().getCode() == null )
            return false;

        LOG.debug( "Transaction is valid with id" + transaction.getId() );
        return true;
    }


    /**
     * 
     * @param transaction
     * @return
     */
    private Map<String, LoneWolfClientContact> getClientContactForTransaction( LoneWolfTransaction transaction )
    {

        LOG.debug( "Inside method getClientContactForTransaction " );
        Map<String, LoneWolfClientContact> clientContactsForTransaction = new HashMap<String, LoneWolfClientContact>();
        //get buyer seller contact details
        for ( LoneWolfClientContact clientContact : transaction.getClientContacts() ) {
            if ( clientContact.getContactType().getCode().equals( LoanWolfContactType.SELLER.getCode() ) ) {
                clientContactsForTransaction.put( LoanWolfContactType.SELLER.getCode(), clientContact );
            } else if ( clientContact.getContactType().getCode().equals( LoanWolfContactType.BUYER.getCode() ) ) {
                clientContactsForTransaction.put( LoanWolfContactType.BUYER.getCode(), clientContact );
            }
        }

        return clientContactsForTransaction;
    }


    /**
     * 
     * @param transaction
     * @param membersByName
     * @return
     */
    private Map<String, LoneWolfMember> getMembersForTransaction( LoneWolfTransaction transaction,
        Map<String, LoneWolfMember> membersByName )
    {

        LOG.debug( "methd getMembersForTransaction started" );
        LoneWolfMember member = null;
        Map<String, LoneWolfMember> membersForTransaction = new HashMap<String, LoneWolfMember>();

        for ( LoneWolfAgentCommission agentCommission : transaction.getTiers().get( 0 ).getAgentCommissions() ) {
            if ( agentCommission.getEndCode().equals( LoanWolfTransactionClassificationMode.SELLING.getMode() ) ) {
                LOG.info( "Found a seller for transaction with id : " + transaction.getId() );
                member = membersByName.get( getKeyForMembersDataMap( agentCommission.getAgent().getFirstName(), agentCommission
                    .getAgent().getLastName() ) );
                membersForTransaction.put( LoanWolfTransactionClassificationMode.SELLING.getMode(), member );
            } else if ( agentCommission.getEndCode().equals( LoanWolfTransactionClassificationMode.LISTING.getMode() ) ) {
                LOG.info( "Found a buyer for transaction with id : " + transaction.getId() );
                member = membersByName.get( getKeyForMembersDataMap( agentCommission.getAgent().getFirstName(), agentCommission
                    .getAgent().getLastName() ) );
                membersForTransaction.put( LoanWolfTransactionClassificationMode.LISTING.getMode(), member );
            }
        }
        return membersForTransaction;
    }


    /**
     * 
     * @param client
     * @param member
     * @param collectionName
     * @param organizationUnitId
     * @throws InvalidInputException 
     */
    private void generateSurveyPreinitiaionAndSave( LoneWolfClientContact client, LoneWolfMember member, String collectionName,
        long organizationUnitId, String transactionNumber )
    {

        LOG.debug( "Inside method generateSurveyPreinitiaionAndSave for transaction number : " + transactionNumber);
        try {
            if ( client == null ) {
                LOG.error( "client is null for the transaction" );
                throw new InvalidInputException( "Passed parameter client is null" );
            }

            if ( member == null ) {
                LOG.error( "member is null for the transaction" );
                throw new InvalidInputException( "Passed parameter member is null" );
            }

            SurveyPreInitiation surveyPreInitiation = new SurveyPreInitiation();
            surveyPreInitiation = setCollectionDetails( surveyPreInitiation, collectionName, organizationUnitId );
            surveyPreInitiation.setCreatedOn( new Timestamp( System.currentTimeMillis() ) );
            surveyPreInitiation.setModifiedOn( new Timestamp( System.currentTimeMillis() ) );
            String customerEmailId = null;
            if ( client.getEmailAddresses() != null && !client.getEmailAddresses().isEmpty() ) {
                customerEmailId = client.getEmailAddresses().get( 0 ).getAddress();
                if ( maskEmail.equals( CommonConstants.YES_STRING ) ) {
                    customerEmailId = utils.maskEmailAddress( customerEmailId );
                }
            }
            surveyPreInitiation.setCustomerEmailId( customerEmailId );
            surveyPreInitiation.setCustomerFirstName( client.getFirstName() );
            surveyPreInitiation.setCustomerLastName( client.getLastName() );
            surveyPreInitiation.setLastReminderTime( utils.convertEpochDateToTimestamp() );
            String agentEmailId = null;

            if ( member.getEmailAddresses() != null && !member.getEmailAddresses().isEmpty() ) {
                agentEmailId = member.getEmailAddresses().get( 0 ).getAddress();
                if ( maskEmail.equals( CommonConstants.YES_STRING ) ) {
                    agentEmailId = utils.maskEmailAddress( agentEmailId );
                }
            }
            surveyPreInitiation.setAgentEmailId( agentEmailId );
            surveyPreInitiation.setAgentName( member.getFirstName() + " " + member.getLastName() );
            surveyPreInitiation.setEngagementClosedTime( new Timestamp( System.currentTimeMillis() ) );
            surveyPreInitiation.setStatus( CommonConstants.STATUS_SURVEYPREINITIATION_NOT_PROCESSED );
            surveyPreInitiation.setSurveySource( CommonConstants.CRM_SOURCE_LONEWOLF );
            surveyPreInitiation.setSurveySourceId( transactionNumber );
            surveyHandler.saveSurveyPreInitiationObject( surveyPreInitiation );
            newRecordFoundCount++;
        } catch ( InvalidInputException e ) {
            LOG.error( "Error while inserting survey preinitiation " , e  );
        }

    }


    /**
     * 
     * @param secretKey
     * @param apiToken
     * @param clientCode
     * @return
     */
    private Map<String, LoneWolfMember> fetchLoneWolfMembersDataMap( String secretKey, String apiToken, String clientCode )
    {
        LOG.info( "method fetchLoneWolfMembersDataMap started "  );
        List<LoneWolfMember> members = loneWolfIntegrationService.fetchLoneWolfMembersData( secretKey, apiToken, clientCode );
        Map<String, LoneWolfMember> membersByName = new HashMap<String, LoneWolfMember>();
        if ( members != null && !members.isEmpty() ) {
            for ( LoneWolfMember member : members ) {
                membersByName.put( getKeyForMembersDataMap( member.getFirstName(), member.getLastName() ), member );
            }
        }
        
        LOG.info( "method fetchLoneWolfMembersDataMap ended "  );
        return membersByName;
    }


    private String getKeyForMembersDataMap( String firstName, String lastName )
    {
        String key = "";
        if ( !StringUtils.isEmpty( firstName ) ) {
            key = key + firstName.trim();
        }

        if ( !StringUtils.isEmpty( lastName ) ) {
            key = key + " " + lastName.trim();
        }
        return key;
    }


    private SurveyPreInitiation setCollectionDetails( SurveyPreInitiation surveyPreInitiation, String collectionName,
        long organizationUnitId )
    {
        LOG.debug( "Inside method setCollectionDetails " );
        if ( collectionName.equalsIgnoreCase( MongoOrganizationUnitSettingDaoImpl.COMPANY_SETTINGS_COLLECTION ) ) {
            surveyPreInitiation.setCompanyId( organizationUnitId );
            surveyPreInitiation.setAgentId( 0 );
        } else if ( collectionName.equalsIgnoreCase( MongoOrganizationUnitSettingDaoImpl.REGION_SETTINGS_COLLECTION ) ) {
            Company company = organizationManagementService.getPrimaryCompanyByRegion( organizationUnitId );
            if ( company != null ) {
                surveyPreInitiation.setCompanyId( company.getCompanyId() );
            }
            surveyPreInitiation.setRegionCollectionId( organizationUnitId );
            surveyPreInitiation.setAgentId( 0 );
        } else if ( collectionName.equalsIgnoreCase( MongoOrganizationUnitSettingDaoImpl.BRANCH_SETTINGS_COLLECTION ) ) {
            Region region = organizationManagementService.getPrimaryRegionByBranch( organizationUnitId );
            if ( region != null ) {
                Company company = organizationManagementService.getPrimaryCompanyByRegion( region.getRegionId() );
                if ( company != null ) {
                    surveyPreInitiation.setCompanyId( company.getCompanyId() );
                }
                surveyPreInitiation.setRegionCollectionId( region.getRegionId() );
            }
            surveyPreInitiation.setBranchCollectionId( organizationUnitId );
            surveyPreInitiation.setAgentId( 0 );
        } else if ( collectionName.equals( MongoOrganizationUnitSettingDaoImpl.AGENT_SETTINGS_COLLECTION ) ) {
            User user = null;
            try {
                user = userManagementService.getUserObjByUserId( organizationUnitId );
            } catch ( InvalidInputException e ) {
                LOG.error( "Exception caught ", e );
            }
            if ( user != null ) {
                Company company = user.getCompany();
                if ( company != null ) {
                    surveyPreInitiation.setCompanyId( company.getCompanyId() );
                }
                surveyPreInitiation.setAgentId( organizationUnitId );
            }

        }
        surveyPreInitiation.setCollectionName( collectionName );
        return surveyPreInitiation;
    }


    private void initializeDependencies( JobDataMap jobMap )
    {
        batchTrackerService = (BatchTrackerService) jobMap.get( "batchTrackerService" );
        crmBatchTrackerService = (CRMBatchTrackerService) jobMap.get( "crmBatchTrackerService" );
        crmBatchTrackerHistoryService = (CRMBatchTrackerHistoryService) jobMap.get( "crmBatchTrackerHistoryService" );
        emailServices = (EmailServices) jobMap.get( "emailServices" );
        organizationManagementService = (OrganizationManagementService) jobMap.get( "organizationManagementService" );
        userManagementService = (UserManagementService) jobMap.get( "userManagementService" );
        surveyHandler = (SurveyHandler) jobMap.get( "surveyHandler" );
        utils = (Utils) jobMap.get( "utils" );
        maskEmail = (String) jobMap.get( "maskEmail" );
        apiToken = (String) jobMap.get( "apiToken" );
        secretKey = (String) jobMap.get( "secretKey" );
        applicationAdminEmail = (String) jobMap.get( "applicationAdminEmail" );
        applicationAdminName = (String) jobMap.get( "applicationAdminName" );
        loneWolfIntegrationService = (LoneWolfIntegrationService) jobMap.get( "loneWolfIntegrationService" );
        loneWolfRestUtils = (LoneWolfRestUtils) jobMap.get( "loneWolfRestUtils" );
    }
}
