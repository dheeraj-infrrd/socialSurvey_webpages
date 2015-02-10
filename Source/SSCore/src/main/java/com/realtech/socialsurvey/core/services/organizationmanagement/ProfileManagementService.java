package com.realtech.socialsurvey.core.services.organizationmanagement;

import java.util.List;
import com.realtech.socialsurvey.core.entities.Achievement;
import com.realtech.socialsurvey.core.entities.Association;
import com.realtech.socialsurvey.core.entities.ContactDetailsSettings;
import com.realtech.socialsurvey.core.entities.Licenses;
import com.realtech.socialsurvey.core.entities.OrganizationUnitSettings;
import com.realtech.socialsurvey.core.entities.SocialMediaTokens;
import com.realtech.socialsurvey.core.entities.User;
import com.realtech.socialsurvey.core.entities.UserSettings;
import com.realtech.socialsurvey.core.enums.AccountType;
import com.realtech.socialsurvey.core.exception.InvalidInputException;

public interface ProfileManagementService {

	/**
	 * Finalize profile settings
	 * 
	 * @param user
	 * @param accountType
	 * @param settings
	 * @throws InvalidInputException
	 */
	public OrganizationUnitSettings finalizeProfile(User user, AccountType accountType, UserSettings settings, long agentId, long branchId,
			long regionId) throws InvalidInputException;

	// JIRA SS-97 by RM-06 : BOC
	/**
	 * Method to update logo of a company
	 * 
	 * @param collection
	 * @param companySettings
	 * @param logo
	 * @throws InvalidInputException
	 */
	public void updateLogo(String collection, OrganizationUnitSettings companySettings, String logo) throws InvalidInputException;

	/**
	 * Method to update company contact information
	 * 
	 * @param collection
	 * @param unitSettings
	 * @param contactDetailsSettings
	 * @return
	 * @throws InvalidInputException
	 */
	public ContactDetailsSettings updateContactDetails(String collection, OrganizationUnitSettings unitSettings,
			ContactDetailsSettings contactDetailsSettings) throws InvalidInputException;

	/**
	 * Method to add an association
	 * 
	 * @param collection
	 * @param unitSettings
	 * @param associationList
	 * @return
	 * @throws InvalidInputException
	 */
	public List<Association> addAssociations(String collection, OrganizationUnitSettings unitSettings, List<Association> associations)
			throws InvalidInputException;

	/**
	 * Method to add an achievement
	 * 
	 * @param collection
	 * @param unitSettings
	 * @param achievements
	 * @return
	 * @throws InvalidInputException
	 */
	public List<Achievement> addAchievements(String collection, OrganizationUnitSettings unitSettings, List<Achievement> achievements)
			throws InvalidInputException;

	/**
	 * Method to add licence details
	 * 
	 * @param collection
	 * @param unitSettings
	 * @param authorisedIn
	 * @param licenseDisclaimer
	 * @return
	 * @throws InvalidInputException
	 */
	public Licenses addLicences(String collection, OrganizationUnitSettings unitSettings, List<String> authorisedIn) throws InvalidInputException;

	/**
	 * Method to update social media tokens in profile
	 * 
	 * @param collection
	 * @param unitSettings
	 * @param mediaTokens
	 * @throws InvalidInputException
	 */
	public void updateSocialMediaTokens(String collection, OrganizationUnitSettings unitSettings, SocialMediaTokens mediaTokens)
			throws InvalidInputException;
}