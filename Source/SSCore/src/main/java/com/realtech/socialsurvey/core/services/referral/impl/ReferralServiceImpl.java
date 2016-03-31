package com.realtech.socialsurvey.core.services.referral.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.dao.GenericDao;
import com.realtech.socialsurvey.core.entities.ReferralInvitiation;
import com.realtech.socialsurvey.core.entities.User;
import com.realtech.socialsurvey.core.entities.UserReferralMapping;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.exception.NonFatalException;
import com.realtech.socialsurvey.core.services.referral.ReferralService;


@Component
public class ReferralServiceImpl implements ReferralService
{

    private static final Logger LOG = LoggerFactory.getLogger( ReferralServiceImpl.class );

    @Autowired
    private GenericDao<ReferralInvitiation, Long> referralDao;

    @Autowired
    private GenericDao<UserReferralMapping, Long> userReferralMappingDao;


    @Override
    public boolean validateReferralCode( String referralCode ) throws InvalidInputException
    {
        LOG.info( "Validating referral code: " + referralCode );
        // checking for active profile
        ReferralInvitiation referralInvitation = getReferralInvitation( referralCode );
        if ( referralInvitation != null ) {
            LOG.info( "Fond referral code " + referralCode );
            return true;
        } else {
            LOG.info( "Could not find referral code " + referralCode );
            return false;
        }
    }


    @Override
    public ReferralInvitiation getReferralInvitation( String referralCode ) throws InvalidInputException
    {
        LOG.info( "Fetching referral object for " + referralCode );
        // checking for active profile
        if ( referralCode == null || referralCode.isEmpty() ) {
            LOG.warn( "Referral code is not present." );
            throw new InvalidInputException( "Referral code is not present." );
        }
        ReferralInvitiation referralInvitation = null;
        Map<String, Object> queries = new HashMap<>();
        queries.put( ReferralInvitiation.REFERRAL_ID_COLUMN, referralCode );
        queries.put( CommonConstants.STATUS_COLUMN, CommonConstants.STATUS_ACTIVE );
        List<ReferralInvitiation> referralCodes = referralDao.findByKeyValue( ReferralInvitiation.class, queries );
        if ( referralCodes != null && !referralCodes.isEmpty() ) {
            referralInvitation = referralCodes.get( CommonConstants.INITIAL_INDEX );
        }
        return referralInvitation;
    }

    @Transactional
    @Override
    public void addRefferalMapping( User user, String referralCode ) throws NonFatalException
    {
        LOG.info( "Mapping user " + user.getUserId() + " with referral code " + referralCode );
        ReferralInvitiation referral = getReferralInvitation( referralCode );
        if ( referral != null ) {
            UserReferralMapping userReferralMapping = new UserReferralMapping();
            userReferralMapping.setReferralInvitation( referral );
            userReferralMapping.setUser( user );
            userReferralMapping.setStatus( CommonConstants.STATUS_ACTIVE );
            userReferralMapping.setCreatedOn( new Timestamp( System.currentTimeMillis() ) );
            userReferralMapping.setModifiedOn( new Timestamp( System.currentTimeMillis() ) );
            userReferralMapping.setCreatedBy( String.valueOf( user.getUserId() ) );
            userReferralMapping.setModifiedBy( String.valueOf( user.getUserId() ) );
            userReferralMappingDao.save( userReferralMapping );
        } else {
            LOG.warn( "Could not find referral code " + referralCode );
            throw new NonFatalException( "Could not find referral code " + referralCode );
        }
    }
}
