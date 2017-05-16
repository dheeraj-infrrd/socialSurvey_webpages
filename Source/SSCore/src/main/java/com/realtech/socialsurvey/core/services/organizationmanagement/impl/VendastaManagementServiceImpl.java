package com.realtech.socialsurvey.core.services.organizationmanagement.impl;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.dao.GenericDao;
import com.realtech.socialsurvey.core.dao.OrganizationUnitSettingsDao;
import com.realtech.socialsurvey.core.dao.impl.MongoOrganizationUnitSettingDaoImpl;
import com.realtech.socialsurvey.core.entities.OrganizationUnitSettings;
import com.realtech.socialsurvey.core.entities.User;
import com.realtech.socialsurvey.core.entities.VendastaProductSettings;
import com.realtech.socialsurvey.core.entities.VendastaRmAccount;
import com.realtech.socialsurvey.core.entities.VendastaSingleSignOnTicket;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.exception.NoRecordsFetchedException;
import com.realtech.socialsurvey.core.integration.pos.errorhandlers.VendastaAccessException;
import com.realtech.socialsurvey.core.integration.vendasta.VendastaApiIntegrationBuilder;
import com.realtech.socialsurvey.core.services.batchtracker.BatchTrackerService;
import com.realtech.socialsurvey.core.services.mail.UndeliveredEmailException;
import com.realtech.socialsurvey.core.services.organizationmanagement.OrganizationManagementService;
import com.realtech.socialsurvey.core.services.organizationmanagement.VendastaManagementService;
import com.realtech.socialsurvey.core.utils.EncryptionHelper;
import com.realtech.socialsurvey.core.utils.UrlValidationHelper;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


@DependsOn ( "generic")
@Component
public class VendastaManagementServiceImpl implements VendastaManagementService
{
    private static final Logger LOG = LoggerFactory.getLogger( VendastaManagementServiceImpl.class );

    private static final String SSO_TICKET_SECRET = "";
    private static final int SSO_TICKET_EXPIRY_TIME_IN_MILLISECONDS = 60000;
    private static final int SSO_TICKET_MINIMUM_LENGTH = 16;
    private static final int SSO_TICKET_SURPLUS = 16;


    @Autowired
    private OrganizationUnitSettingsDao organizationUnitSettingsDao;

    @Autowired
    private OrganizationManagementService organizationManagementService;

    @Autowired
    private BatchTrackerService batchTrackerService;

    @Autowired
    private GenericDao<VendastaSingleSignOnTicket, Long> vendastaSingleSignOnTicketDao;

    @Autowired
    private UrlValidationHelper urlValidationHelper;

    @Autowired
    private EncryptionHelper encryptionHelper;

    @Autowired
    private VendastaApiIntegrationBuilder vendastaApiIntegrationBuilder;

    @Value ( "${API_USER}")
    private String apiUser;

    @Value ( "${API_KEY}")
    private String apiKey;


    //the method that actually interacts with data layer objects to update the boolean VendastaAccessible
    //in mongo for every hierarchy entity 
    @Override
    public boolean updateVendastaAccess( String collectionName, OrganizationUnitSettings unitSettings )
        throws InvalidInputException
    {
        if ( unitSettings == null ) {
            throw new InvalidInputException( "Unit settings cannot be null." );
        }

        LOG.debug( "Updating unitSettings: " + unitSettings + " with vendasta Access: " + unitSettings.isVendastaAccessible() );
        organizationUnitSettingsDao.updateParticularKeyOrganizationUnitSettings( CommonConstants.VENDASTA_ACCESS,
            unitSettings.isVendastaAccessible(), unitSettings, collectionName );
        LOG.debug( "Updated the record successfully" );

        return true;
    }


    //the method which inreracts with the data objects to store the accountId for vendasta reputation mannagement account
    @Override
    public boolean updateVendastaRMSettings( String collectionName, OrganizationUnitSettings unitSettings,
        VendastaProductSettings vendastaReputationManagementSettings ) throws InvalidInputException
    {
        if ( unitSettings == null ) {
            throw new InvalidInputException( "OrganizationUnitSettings cannot be null." );
        }

        LOG.debug( "Updating collectionName: " + unitSettings + " with vendastaReputationManagementSettings: "
            + vendastaReputationManagementSettings );
        organizationUnitSettingsDao.updateParticularKeyOrganizationUnitSettings(
            MongoOrganizationUnitSettingDaoImpl.KEY_VENDASTA_RM_SETTINGS, vendastaReputationManagementSettings, unitSettings,
            collectionName );
        LOG.debug( "Updated the record successfully" );

        return true;
    }


    //generates the validate url to be sent to the vendasta server
    @Override
    public String validateUrlGenerator( User user, String nextUrl, String productId, String ssoToken )
        throws IOException, InvalidInputException
    {
        LOG.info( "VendastaManagementService.validateUrlGenerator started" );
        String validateUrl = "";
        if ( nextUrl == null || nextUrl.isEmpty() || productId == null || ssoToken == null ) {
            LOG.error( "null parameter/s" );
            throw new InvalidInputException();
        }

        validateUrl = URLDecoder.decode( nextUrl, "UTF-8" );
        validateUrl += "&sso_token=" + ssoToken + "&sso_ticket=" + generateSSOTicket( user, productId, ssoToken );
        urlValidationHelper.validateUrl( validateUrl );
        LOG.info( "VendastaManagementService.validateUrlGenerator successfully finished" );
        return validateUrl;
    }


    //method that genrates the ticket that is needed for every sso process
    private String generateSSOTicket( User user, String productId, String ssoToken ) throws InvalidInputException
    {
        LOG.info( "generateSSOTicket started" );
        String ssoTicket = "";
        if ( productId == null || ssoToken == null || user == null || user.getUserId() <= 0 ) {
            LOG.error( "null parameter/s" );
            throw new InvalidInputException();
        }
        try {
            ssoTicket = encryptionHelper.encryptAES( String.valueOf( System.currentTimeMillis() ), SSO_TICKET_SECRET );
        } catch ( InvalidInputException error ) {
            LOG.error( "generateSSOTicket: primary strategy failed, applying alternate strategy" );
            ssoTicket = RandomStringUtils
                .randomAlphanumeric( SSO_TICKET_MINIMUM_LENGTH + RandomUtils.nextInt( SSO_TICKET_SURPLUS + 1 ) );
        }
        storeSSOTicket( user, productId, ssoToken, ssoTicket, true );
        LOG.info( "generateSSOTicket finished" );
        return ssoTicket;
    }


    //method that interacts with the data objects to check if the ticket is stored or not
    @Override
    public boolean validateSSOTuple( String productId, String ssoToken, String ssoTicket )
    {
        if ( productId == null || ssoToken == null || ssoTicket == null ) {
            LOG.error( "null parameter/s" );
            return false;
        }

        Criterion id = Restrictions.eq( CommonConstants.VENDASTA_PRODUCT_ID_COLUMN, productId );
        Criterion token = Restrictions.eq( CommonConstants.VENDASTA_SSO_TOKEN_COLUMN, ssoToken );
        Criterion ticket = Restrictions.eq( CommonConstants.VENDASTA_SSO_TICKET_COLUMN, ssoTicket );
        Criterion status = Restrictions.eq( CommonConstants.VENDASTA_STATUS_COLUMN, true );

        List<VendastaSingleSignOnTicket> tickets = vendastaSingleSignOnTicketDao
            .findByCriteria( VendastaSingleSignOnTicket.class, id, token, ticket, status );
        if ( tickets.size() == 0 ) {
            return false;
        } else {
            VendastaSingleSignOnTicket usedTicket = tickets.get( CommonConstants.INITIAL_INDEX );
            usedTicket.setStatus( false );
            usedTicket.setModifiedOn( new Timestamp( System.currentTimeMillis() ) );
            usedTicket.setModifiedBy( CommonConstants.VENDASTA );
            vendastaSingleSignOnTicketDao.merge( usedTicket );
            return true;
        }
    }


    //method that fetches the accountId from mongo for a hierarchy 
    @Override
    public String fetchSSOTokenForReputationManagementAccount( String entityType, long entityId, String productId )
        throws InvalidInputException, NoRecordsFetchedException
    {
        LOG.info( "VendastaManagementService.fetchSSOToken started" );
        if ( productId != null && productId.equals( CommonConstants.VENDASTA_REPUTATION_MANAGEMENT_ID ) && entityType != null
            && entityId > 0 ) {

            Map<String, Object> details = getUnitSettingsForAHierarchy( entityType, entityId );
            OrganizationUnitSettings unitSettings = (OrganizationUnitSettings) details.get( "unitSettings" );
            VendastaProductSettings vendastaSettings = unitSettings.getVendasta_rm_settings();

            if ( vendastaSettings != null && vendastaSettings.getAccountId() != null ) {
                String accountId = unitSettings.getVendasta_rm_settings().getAccountId();
                LOG.info( "VendastaManagementService.fetchSSOToken finished" );
                return accountId;
            } else {
                LOG.error( "VendastaManagementService.fetchSSOToken could not fetch the ssoToken for unitSettings: "
                    + unitSettings.getId() );
                throw new NoRecordsFetchedException();
            }
        } else {
            throw new NoRecordsFetchedException();
        }
    }


    //a generic method that returns a map of OrganizationUnitSettings object and its type for any hierarchy entity
    @Override
    public Map<String, Object> getUnitSettingsForAHierarchy( String entityType, long entityId )
        throws InvalidInputException, NoRecordsFetchedException
    {

        LOG.info( "VendastaManagementService.getUnitSettingsForAHierarchy started" );
        String collectionName = "";
        OrganizationUnitSettings unitSettings = null;
        Map<String, Object> details = new HashMap<String, Object>();
        if ( entityId > 0 ) {
            if ( entityType.equalsIgnoreCase( CommonConstants.COMPANY_ID ) ) {
                collectionName = MongoOrganizationUnitSettingDaoImpl.COMPANY_SETTINGS_COLLECTION;
                unitSettings = organizationManagementService.getCompanySettings( entityId );

            } else if ( entityType.equalsIgnoreCase( CommonConstants.REGION_ID ) ) {
                collectionName = MongoOrganizationUnitSettingDaoImpl.REGION_SETTINGS_COLLECTION;
                unitSettings = organizationManagementService.getRegionSettings( entityId );

            } else if ( entityType.equalsIgnoreCase( CommonConstants.BRANCH_ID ) ) {
                collectionName = MongoOrganizationUnitSettingDaoImpl.BRANCH_SETTINGS_COLLECTION;
                unitSettings = organizationManagementService.getBranchSettingsDefault( entityId );

            } else if ( entityType.equalsIgnoreCase( CommonConstants.AGENT_ID ) ) {
                collectionName = MongoOrganizationUnitSettingDaoImpl.AGENT_SETTINGS_COLLECTION;
                unitSettings = organizationManagementService.getAgentSettings( entityId );
            } else {
                throw new InvalidInputException( "Invalid EntityType: " + entityType
                    + ". EntityType should be one of the following: [companyId, regionId, branchId, agentId]." );
            }
            details.put( "collectionName", collectionName );
            details.put( "unitSettings", unitSettings );
        } else {
            throw new InvalidInputException( "Invalid EntityId: " + entityId + ". EntityId should be greater than 0." );

        }
        LOG.info( "VendastaManagementService.getUnitSettingsForAHierarchy finished" );
        return details;
    }


    // method that stores the ticket generated for every sso process in VENDASTA_SSO_TICKET table
    private void storeSSOTicket( User user, String productId, String ssoToken, String ssoTicket, boolean status )
        throws InvalidInputException
    {
        LOG.info( "storeSSOTicket started" );
        if ( user != null && productId != null && ssoTicket != null && ssoToken != null && user.getUserId() != 0 ) {
            VendastaSingleSignOnTicket ticket = new VendastaSingleSignOnTicket();
            ticket.setVendastaSingleSignOnTicket( ssoTicket );
            ticket.setProductId( productId );
            ticket.setVendastaSingleSignOnToken( ssoToken );
            ticket.setStatus( status );
            Timestamp currentTimestamp = new Timestamp( System.currentTimeMillis() );
            ticket.setCreatedOn( currentTimestamp );
            ticket.setModifiedOn( currentTimestamp );
            ticket.setCreatedBy( String.valueOf( user.getUserId() ) );
            ticket.setModifiedBy( String.valueOf( user.getUserId() ) );
            VendastaSingleSignOnTicket savedticket = vendastaSingleSignOnTicketDao.save( ticket );
            ticketTimeoutTrigger( savedticket.getVendastaSingleSignOnTicketId() );
            LOG.info( "storeSSOTicket finished" );
        } else {
            LOG.error( "storeSSOTicket: Insufficient data to create the ticket " );
            throw new InvalidInputException();
        }
    }


    //a method that spawns a new thread for each generated ticket to delete the entry after the ticket has expired
    private void ticketTimeoutTrigger( Long ticketId ) throws InvalidInputException
    {
        if ( ticketId > 0 ) {
            LOG.info( "ticketTimeoutTrigger started" );
            Thread trigger = new Thread( new Runnable() {
                private long ticketId;


                private Runnable init( long ticketId )
                {
                    this.ticketId = ticketId;
                    Thread.currentThread().setName( "usedTicketDeleterThread-" + Thread.currentThread().getId() );
                    return this;
                }


                public void run()
                {
                    try {
                        LOG.info( "ticketTimeoutTrigger thread started for ticket:" + ticketId );
                        Thread.sleep( SSO_TICKET_EXPIRY_TIME_IN_MILLISECONDS );

                        VendastaSingleSignOnTicket ticket = getSSOTicketById( ticketId );
                        if ( ticket.getStatus() ) {
                            ticket.setStatus( false );
                            ticket.setModifiedOn( new Timestamp( System.currentTimeMillis() ) );
                            ticket.setModifiedBy( "Thread: " + Thread.currentThread().getName() );
                            vendastaSingleSignOnTicketDao.merge( ticket );
                        }
                    } catch ( InterruptedException error ) {
                        LOG.error( "ticketTimeoutTrigger: count down was interrupted, initiating trigger prematurely" );
                    } catch ( InvalidInputException error ) {
                        LOG.error( "ticketTimeoutTrigger: ticketId is null" );
                    }
                    LOG.info( "ticketTimeoutTrigger thread finished for ticket:" + ticketId );
                }
            }.init( ticketId ) );
            trigger.start();
        } else {
            LOG.error( "ticketTimeoutTrigger: ticketId is null" );
            throw new InvalidInputException();
        }
        LOG.info( "ticketTimeoutTrigger finished" );
    }


    @Override
    public VendastaSingleSignOnTicket getSSOTicketById( long ticketId ) throws InvalidInputException
    {
        if ( ticketId > 0 ) {
            return vendastaSingleSignOnTicketDao.findById( VendastaSingleSignOnTicket.class, ticketId );
        } else {
            throw new InvalidInputException();
        }
    }


    @Override
    public List<VendastaSingleSignOnTicket> getAllInactiveTickets()
    {
        return vendastaSingleSignOnTicketDao.findByColumn( VendastaSingleSignOnTicket.class,
            CommonConstants.VENDASTA_STATUS_COLUMN, false );

    }


    // a method to fetch all the used tckets and delete them 
    @Override
    public void usedVendastaTicketRemover()
    {
        try {
            // update last start time
            batchTrackerService.getLastRunEndTimeAndUpdateLastStartTimeByBatchType(
                CommonConstants.BATCH_TYPE_PROCESSED_SSO_TICKET_REMOVER,
                CommonConstants.BATCH_NAME_PROCESSED_SSO_TICKET_REMOVER );

            List<VendastaSingleSignOnTicket> usedTickets = getAllInactiveTickets();
            for ( VendastaSingleSignOnTicket ticket : usedTickets ) {
                try {
                    Long createdOn = ticket.getCreatedOn().getTime();
                    Long currentTime = System.currentTimeMillis();
                    if ( ( (Integer) currentTime.compareTo( createdOn + SSO_TICKET_EXPIRY_TIME_IN_MILLISECONDS ) )
                        .equals( CommonConstants.VENDASTA_TICKET_EXPIRED ) ) {
                        vendastaSingleSignOnTicketDao.delete( ticket );
                    }
                } catch ( Exception error ) {
                    LOG.error( "usedVendastaTicketRemover: seems like the ticket has already been removed ", error );
                }
            }
            //updating last run time for batch in database
            batchTrackerService.updateLastRunEndTimeByBatchType( CommonConstants.BATCH_TYPE_PROCESSED_SSO_TICKET_REMOVER );
            LOG.debug( "Completed usedVendastaTicketRemover" );
        } catch ( Exception error ) {
            LOG.error( "Error in usedVendastaTicketRemover", error );
            try {
                //update batch tracker with error message
                batchTrackerService.updateErrorForBatchTrackerByBatchType(
                    CommonConstants.BATCH_TYPE_PROCESSED_SSO_TICKET_REMOVER, error.getMessage() );
                //send report bug mail to admin
                batchTrackerService.sendMailToAdminRegardingBatchError( CommonConstants.BATCH_NAME_PROCESSED_SSO_TICKET_REMOVER,
                    System.currentTimeMillis(), error );
            } catch ( NoRecordsFetchedException | InvalidInputException nestedError ) {
                LOG.error( "Error while updating error message in usedVendastaTicketRemover " );
            } catch ( UndeliveredEmailException nestedError ) {
                LOG.error( "Error while sending report excption mail to admin " );
            }
        }
    }


    @SuppressWarnings ( "unchecked")
    @Override
    public boolean isAccountExistInVendasta( String accountId )
    {
        boolean status = false;
        try {
            Response response = vendastaApiIntegrationBuilder.getIntegrationApi().getAccountById( apiUser, apiKey, accountId );
            if ( response != null && response.getStatus() == HttpStatus.SC_OK ) {
                String responseString = new String( ( (TypedByteArray) response.getBody() ).getBytes() );

                Map<String, Object> ResponseMap = new ObjectMapper().readValue( responseString,
                    new TypeReference<HashMap<String, Object>>() {} );
                HashMap<String, Object> data = (HashMap<String, Object>) ResponseMap.get( "data" );
                if ( data != null && data.size() != 0 ) {
                    status = true;
                }
            }
        } catch ( VendastaAccessException | IOException ex ) {
            LOG.error( "Error connecting to vendasta. " + ex.getMessage() );
        }
        return status;
    }


    @Override
    public Map<String, Object> validateAndCreateRmAccount( VendastaRmAccount vendastaRmAccount, boolean isForced )
        throws InvalidInputException
    {
        String customerIdentifier = null;
        boolean isAlreadyExistingAccount = false;
        Map<String, Object> dataMap = new HashMap<>();
        try {
            Map<String, Object> hierarchyDetails = this.getUnitSettingsForAHierarchy( vendastaRmAccount.getEntityType(),
                vendastaRmAccount.getEntityId() );
            OrganizationUnitSettings unitSettings = (OrganizationUnitSettings) hierarchyDetails.get( "unitSettings" );
            if ( unitSettings == null ) {
                throw new NoRecordsFetchedException( "Invalid EntityId: " + vendastaRmAccount.getEntityId()
                    + " for EntityType: " + vendastaRmAccount.getEntityType() );
            }

            // fill the default entries for creating the account
            populateRmAccountDetails( vendastaRmAccount );

            if ( !isForced ) {
                if ( unitSettings != null && unitSettings.getVendasta_rm_settings() != null
                    && !StringUtils.isEmpty( unitSettings.getVendasta_rm_settings().getAccountId() ) ) {
                    if ( this.isAccountExistInVendasta( unitSettings.getVendasta_rm_settings().getAccountId() ) ) {
                        customerIdentifier = unitSettings.getVendasta_rm_settings().getAccountId();
                        isAlreadyExistingAccount = true;
                    }
                }
                if ( !isAlreadyExistingAccount ) {
                    customerIdentifier = createRmAccount( vendastaRmAccount, hierarchyDetails );
                }
            } else {
                customerIdentifier = createRmAccount( vendastaRmAccount, hierarchyDetails );
            }
        } catch ( VendastaAccessException e ) {
            LOG.error( "Error connecting to vendasta." );
            try {
                if ( e.getCause() instanceof RetrofitError ) {
                    RetrofitError error = (RetrofitError) e.getCause();
                    String responseString = new String( ( (TypedByteArray) error.getResponse().getBody() ).getBytes() );
                    Map<String, Object> responseMap = new ObjectMapper().readValue( responseString,
                        new TypeReference<HashMap<String, Object>>() {} );
                    String message = (String) responseMap.get( "message" );
                    throw new InvalidInputException( message );
                }
            } catch ( IOException ex ) {
                LOG.error( "Error connecting to vendasta. " + ex );
                throw new InvalidInputException( "Invalid json response received from Vendasta." + e.getMessage() );
            }
        } catch ( InvalidInputException | NoRecordsFetchedException e ) {
            LOG.error( "Error connecting to vendasta. " + e );
            throw new InvalidInputException( e.getMessage() );
        } catch ( IOException e ) {
            LOG.error( "Error connecting to vendasta. " + e );
            throw new InvalidInputException( "Invalid json response received from Vendasta." + e.getMessage() );
        } catch ( Exception e ) {
            throw new InvalidInputException( "Error in creating Vendasta RM account. " + e );
        }
        dataMap.put( "customerIdentifier", customerIdentifier );
        dataMap.put( "isAlreadyExistingAccount", isAlreadyExistingAccount );
        return dataMap;
    }


    private void populateRmAccountDetails( VendastaRmAccount vendastaRmAccount )
        throws InvalidInputException, NoRecordsFetchedException
    {
        Map<String, Object> hierarchyDetails = getUnitSettingsForAHierarchy( vendastaRmAccount.getEntityType(),
            vendastaRmAccount.getEntityId() );
        
        OrganizationUnitSettings unitSettings = (OrganizationUnitSettings)hierarchyDetails.get( "unitSettings" );
        
        if( unitSettings.getContact_details().getCountry() != null && vendastaRmAccount.getCountry() == null ){
            vendastaRmAccount.setCountry( unitSettings.getContact_details().getCountry() );
        }
        
        if( unitSettings.getContact_details().getState() != null && vendastaRmAccount.getState() == null ){
            vendastaRmAccount.setState( unitSettings.getContact_details().getState() );
        }
        
        if( unitSettings.getContact_details().getCity() != null && vendastaRmAccount.getCity() == null ){
            vendastaRmAccount.setCity( unitSettings.getContact_details().getCity() );
        }
        
        if( unitSettings.getContact_details().getAddress() != null && vendastaRmAccount.getAddress() == null ){
            vendastaRmAccount.setAddress( unitSettings.getContact_details().getAddress() );
        }
        
        if( unitSettings.getContact_details().getZipcode() != null && vendastaRmAccount.getZip() == null ){
            vendastaRmAccount.setZip( unitSettings.getContact_details().getZipcode() );
        }
    }


    @SuppressWarnings ( "unchecked")
    private String createRmAccount( VendastaRmAccount vendastaRmAccount, Map<String, Object> hierarchyDetails )
        throws IOException, InvalidInputException
    {
        String customerIdentifier = null;
        Map<String, String> data = getVendastaRmAccountDataMap( vendastaRmAccount );
        Response response = vendastaApiIntegrationBuilder.getIntegrationApi().createRmAccount( data,
            new HashMap<Object, Object>() );
        if ( response != null && response.getStatus() == HttpStatus.SC_CREATED ) {
            String responseString = new String( ( (TypedByteArray) response.getBody() ).getBytes() );
            Map<String, Object> responseMap = new ObjectMapper().readValue( responseString,
                new TypeReference<HashMap<String, Object>>() {} );
            Map<String, Object> responseData = (HashMap<String, Object>) responseMap.get( "data" );
            if ( responseData != null && responseData.size() > 0 ) {
                if ( storeCustomerIdentifierInMongoForEntity( hierarchyDetails,
                    (String) responseData.get( "customerIdentifier" ) ) ) {
                    customerIdentifier = (String) responseData.get( "customerIdentifier" );
                }
            }
        }
        return customerIdentifier;
    }


    private boolean storeCustomerIdentifierInMongoForEntity( Map<String, Object> hierarchyDetails, String customerIdentifier )
        throws InvalidInputException
    {
        OrganizationUnitSettings unitSettings = (OrganizationUnitSettings) hierarchyDetails.get( "unitSettings" );
        String collectionName = (String) hierarchyDetails.get( "collectionName" );
        VendastaProductSettings settings = new VendastaProductSettings();
        settings.setAccountId( customerIdentifier );
        boolean isStoredInMongo = this.updateVendastaRMSettings( collectionName, unitSettings, settings );
        return isStoredInMongo;
    }


    private Map<String, String> getVendastaRmAccountDataMap( VendastaRmAccount account )
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put( "apiKey", apiKey );
        data.put( "apiUser", apiUser );
        data.put( "address", account.getAddress() );
        data.put( "city", account.getCity() );
        data.put( "companyName", account.getCompanyName() );
        data.put( "country", account.getCountry() );
        data.put( "zip", account.getZip() );
        data.put( "state", account.getState() );
        data.put( "accountGroupId", account.getAccountGroupId() );
        data.put( "adminNotes", account.getAdminNotes() );
        data.put( "alternateEmail", account.getAlternateEmail() );
        data.put( "billingCode", account.getBillingCode() );
        data.put( "businessCategory", account.getBusinessCategory() );
        data.put( "callTrackingNumber", account.getCallTrackingNumber() );
        data.put( "cellNumber", account.getCellNumber() );
        data.put( "commonCompanyName", account.getCommonCompanyName() );
        data.put( "competitor", account.getCompetitor() );
        data.put( "customerIdentifier", account.getCustomerIdentifier() );
        data.put( "email", account.getEmail() );
        data.put( "employee", account.getEmployee() );
        data.put( "faxNumber", account.getFaxNumber() );
        data.put( "firstName", account.getFirstName() );
        data.put( "lastName", account.getLastName() );
        data.put( "marketId", account.getMarketId() );
        data.put( "salesPersonEmail", account.getSalesPersonEmail() );
        data.put( "service", account.getService() );
        data.put( "ssoToken", account.getSsoToken() );
        data.put( "taxId", account.getTaxId() );
        data.put( "twitterSearches", account.getTwitterSearches() );
        data.put( "website", account.getWebsite() );
        data.put( "welcomeMessage", account.getWelcomeMessage() );
        data.put( "workNumber", account.getWorkNumber() );
        data.put( "demoAccountFlag", account.getDemoAccountFlag() );
        data.put( "sendAlertsFlag", account.getSendAlertsFlag() );
        data.put( "sendReportsFlag", account.getSendReportsFlag() );
        data.put( "sendTutorialsFlag", account.getSendTutorialsFlag() );
        data.put( "latitude", account.getLatitude() );
        data.put( "longitude", account.getLongitude() );
        return data;
    }
}
