package com.realtech.socialsurvey.core.dao.impl;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.dao.UserInviteDao;
import com.realtech.socialsurvey.core.entities.UserInvite;
import com.realtech.socialsurvey.core.exception.DatabaseException;

// JIRA: SS-8: By RM05: BOC
/*
 * This class contains methods specific to UserInvite entity.
 */
@Component("userInvite")
public class UserInviteDaoImpl extends GenericDaoImpl<UserInvite, Integer> implements UserInviteDao {

	/**
	 * JIRA SS-33 BY RM02 Method selects the valid user invites for the specified url parameters
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserInvite> findByUrlParameter(String encryptedUrlParameter) {
		Criteria criteria = getSession().createCriteria(UserInvite.class);
		try {
			criteria.add(Restrictions.eq(CommonConstants.USER_INVITE_INVITATION_PARAMETERS_COLUMN, encryptedUrlParameter));
			criteria.add(Restrictions.eq(CommonConstants.STATUS_COLUMN, CommonConstants.STATUS_ACTIVE));

			// Valid_till date should either be equal to 01-JAN-1970 or less than today.
			Criterion criterion = Restrictions.or(
					Restrictions.eq(CommonConstants.USER_INVITE_INVITATION_VALID_UNTIL_COLUMN, new Timestamp(CommonConstants.EPOCH_TIME_IN_MILLIS)),
					Restrictions.lt(CommonConstants.USER_INVITE_INVITATION_VALID_UNTIL_COLUMN, new Timestamp(System.currentTimeMillis())));
			criteria.add(criterion);
		}
		catch (HibernateException hibernateException) {
			throw new DatabaseException("Exception caught in findByKeyValue() ", hibernateException);
		}
		return (List<UserInvite>) criteria.list();
	}
}

// JIRA: SS-8: By RM05: EOC