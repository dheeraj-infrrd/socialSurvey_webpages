package com.realtech.socialsurvey.core.services.organizationmanagement.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import com.realtech.socialsurvey.core.dao.OrganizationUnitSettingsDao;
import com.realtech.socialsurvey.core.dao.impl.MongoOrganizationUnitSettingDaoImpl;
import com.realtech.socialsurvey.core.entities.Achievement;
import com.realtech.socialsurvey.core.entities.AgentSettings;
import com.realtech.socialsurvey.core.entities.Association;
import com.realtech.socialsurvey.core.entities.ContactDetailsSettings;
import com.realtech.socialsurvey.core.entities.Licenses;
import com.realtech.socialsurvey.core.entities.LockSettings;
import com.realtech.socialsurvey.core.entities.OrganizationUnitSettings;
import com.realtech.socialsurvey.core.entities.SocialMediaTokens;
import com.realtech.socialsurvey.core.entities.User;
import com.realtech.socialsurvey.core.entities.UserSettings;
import com.realtech.socialsurvey.core.enums.AccountType;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.services.organizationmanagement.ProfileManagementService;

@DependsOn("generic")
@Component
public class ProfileManagementServiceImpl implements ProfileManagementService, InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(ProfileManagementServiceImpl.class);

	@Autowired
	private OrganizationUnitSettingsDao organizationUnitSettingsDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("afterPropertiesSet called for profile management service");
	}

	public OrganizationUnitSettings finalizeProfile(User user, AccountType accountType, UserSettings settings, long agentId, long branchId,
			long regionId) throws InvalidInputException {
		LOG.info("Method finalizeProfileDetail() called from ProfileManagementService");
		if (user == null) {
			throw new InvalidInputException("User is not set.");
		}
		if (settings == null) {
			throw new InvalidInputException("Invalid user settings.");
		}
		if (accountType == null) {
			throw new InvalidInputException("Invalid account type.");
		}

		OrganizationUnitSettings finalSettings = null;
		switch (accountType) {
			case INDIVIDUAL:
			case TEAM:
				LOG.info("Individual/Team account type");
				// Company Admin
				if (user.isCompanyAdmin()) {
					finalSettings = settings.getCompanySettings();
				}

				// Individual
				else if (user.isAgent()) {
					finalSettings = generateAgentProfile(settings.getCompanySettings(), null, null, settings.getAgentSettings().get(agentId));
				}
				break;

			case COMPANY:
				LOG.info("Company account type");
				// Company Admin
				if (user.isCompanyAdmin()) {
					finalSettings = settings.getCompanySettings();
				}

				// Branch Admin
				else if (user.isBranchAdmin()) {
					finalSettings = generateBranchProfile(settings.getCompanySettings(), null, settings.getBranchSettings().get(branchId));
				}

				// Individual
				else if (user.isAgent()) {
					finalSettings = generateAgentProfile(settings.getCompanySettings(), null, settings.getBranchSettings().get(branchId), settings
							.getAgentSettings().get(agentId));
				}
				break;

			case ENTERPRISE:
				LOG.info("Company account type");
				// Company Admin
				if (user.isCompanyAdmin()) {
					finalSettings = settings.getCompanySettings();
				}

				// Region Admin
				else if (user.isRegionAdmin()) {
					finalSettings = generateRegionProfile(settings.getCompanySettings(), settings.getRegionSettings().get(regionId));
				}

				// Branch Admin
				else if (user.isBranchAdmin()) {
					finalSettings = generateBranchProfile(settings.getCompanySettings(), settings.getRegionSettings().get(regionId), settings
							.getBranchSettings().get(branchId));
				}

				// Individual
				else if (user.isAgent()) {
					finalSettings = generateAgentProfile(settings.getCompanySettings(), settings.getRegionSettings().get(regionId), settings
							.getBranchSettings().get(branchId), settings.getAgentSettings().get(agentId));
				}
				break;

			default:
				throw new InvalidInputException("Account type is invalid in finalizeProfileDetail");
		}

		LOG.info("Method finalizeProfileDetail() finished from ProfileManagementService");
		return finalSettings;
	}

	private OrganizationUnitSettings generateRegionProfile(OrganizationUnitSettings companySettings, OrganizationUnitSettings regionSettings)
			throws InvalidInputException {
		if (companySettings == null || regionSettings == null) {
			throw new InvalidInputException("No Settings found");
		}

		// Company Lock settings
		LockSettings regionLock = new LockSettings();
		updateSettings(companySettings, regionSettings, regionLock);

		regionSettings.setLockSettings(regionLock);
		return regionSettings;
	}

	private OrganizationUnitSettings generateBranchProfile(OrganizationUnitSettings companySettings, OrganizationUnitSettings regionSettings,
			OrganizationUnitSettings branchSettings) throws InvalidInputException {
		if (companySettings == null || branchSettings == null) {
			throw new InvalidInputException("No Settings found");
		}

		// Company Lock settings
		LockSettings branchLock = new LockSettings();
		updateSettings(companySettings, branchSettings, branchLock);

		// Region Lock settings
		if (regionSettings != null) {
			updateSettings(regionSettings, branchSettings, branchLock);
		}

		branchSettings.setLockSettings(branchLock);
		return branchSettings;
	}

	private AgentSettings generateAgentProfile(OrganizationUnitSettings companySettings, OrganizationUnitSettings regionSettings,
			OrganizationUnitSettings branchSettings, AgentSettings agentSettings) throws InvalidInputException {
		if (companySettings == null || agentSettings == null) {
			throw new InvalidInputException("No Settings found");
		}

		// Company Lock settings
		LockSettings agentLock = new LockSettings();
		updateSettings(companySettings, agentSettings, agentLock);

		// Region Lock settings
		if (regionSettings != null) {
			updateSettings(regionSettings, agentSettings, agentLock);
		}

		// Branch Lock settings
		if (branchSettings != null) {
			updateSettings(branchSettings, agentSettings, agentLock);
		}

		agentSettings.setLockSettings(agentLock);
		return agentSettings;
	}

	private void updateSettings(OrganizationUnitSettings higherSettings, OrganizationUnitSettings lowerSettings, LockSettings finalLock) {
		LockSettings lock = higherSettings.getLockSettings();
		if (lock != null) {
			if (lock.isLogoLocked() && !finalLock.isLogoLocked()) {
				lowerSettings.setLogo(higherSettings.getLogo());
				finalLock.setLogoLocked(true);
			}
			if (lock.isLocationLocked() && !finalLock.isLocationLocked()) {
				lowerSettings.setLocationEnabled(higherSettings.getIsLocationEnabled());
				finalLock.setLocationLocked(true);
			}
			if (lock.isVerticalLocked() && !finalLock.isVerticalLocked()) {
				lowerSettings.setVertical(higherSettings.getVertical());
				finalLock.setVerticalLocked(true);
			}
			if (lock.isCRMInfoLocked() && !finalLock.isCRMInfoLocked()) {
				lowerSettings.setCrm_info(higherSettings.getCrm_info());
				finalLock.setCRMInfoLocked(true);
			}
			if (lock.isMailContentLocked() && !finalLock.isMailContentLocked()) {
				lowerSettings.setMail_content(higherSettings.getMail_content());
				finalLock.setMailContentLocked(true);
			}
			if (lock.isLicensesLocked() && !finalLock.isLicensesLocked()) {
				lowerSettings.setLicenses(higherSettings.getLicenses());
				finalLock.setLicensesLocked(true);
			}
			if (lock.isAssociationsLocked() && !finalLock.isAssociationsLocked()) {
				lowerSettings.setAssociations(higherSettings.getAssociations());
				finalLock.setAssociationsLocked(true);
			}
			if (lock.isAcheivementsLocked() && !finalLock.isAcheivementsLocked()) {
				lowerSettings.setAchievements(higherSettings.getAchievements());
				finalLock.setAcheivementsLocked(true);
			}
			if (lock.isSocialTokensLocked() && !finalLock.isSocialTokensLocked()) {
				lowerSettings.setSocialMediaTokens(higherSettings.getSocialMediaTokens());
				finalLock.setSocialTokensLocked(true);
			}
			if (lock.isSurveySettingsLocked() && !finalLock.isSurveySettingsLocked()) {
				lowerSettings.setSurvey_settings(higherSettings.getSurvey_settings());
				finalLock.setSurveySettingsLocked(true);
			}
		}
	}
	
	@Override
	public void updateLogo(String collection, OrganizationUnitSettings companySettings, String logo) throws InvalidInputException {
		if (logo == null || logo.isEmpty()) {
			throw new InvalidInputException("Logo passed can not be null or empty");
		}
		LOG.info("Updating logo");
		organizationUnitSettingsDao.updateParticularKeyOrganizationUnitSettings(MongoOrganizationUnitSettingDaoImpl.KEY_LOGO, logo, companySettings,
				collection);
		LOG.info("Logo updated successfully");
	}

	@Override
	public ContactDetailsSettings updateContactDetails(String collection, OrganizationUnitSettings unitSettings,
			ContactDetailsSettings contactDetailsSettings) throws InvalidInputException {
		if (contactDetailsSettings == null) {
			throw new InvalidInputException("Contact details passed can not be null");
		}
		LOG.info("Updating contact detail information");
		organizationUnitSettingsDao.updateParticularKeyOrganizationUnitSettings(MongoOrganizationUnitSettingDaoImpl.KEY_CONTACT_DETAIL_SETTINGS,
				contactDetailsSettings, unitSettings, collection);
		LOG.info("Contact details updated successfully");
		return contactDetailsSettings;
	}

	@Override
	public List<Association> addAssociations(String collection, OrganizationUnitSettings unitSettings, List<Association> associations)
			throws InvalidInputException {
		if (associations == null || associations.isEmpty()) {
			throw new InvalidInputException("Association name passed can not be null");
		}
		for (Association association : associations) {
			if (association.getName() == null || association.getName().isEmpty()) {
				associations.remove(association);
			}
		}
		LOG.info("Adding associations");
		organizationUnitSettingsDao.updateParticularKeyOrganizationUnitSettings(MongoOrganizationUnitSettingDaoImpl.KEY_ASSOCIATION, associations,
				unitSettings, MongoOrganizationUnitSettingDaoImpl.COMPANY_SETTINGS_COLLECTION);
		LOG.info("Associations added successfully");
		return associations;
	}

	@Override
	public List<Achievement> addAchievements(String collection, OrganizationUnitSettings unitSettings, List<Achievement> achievements)
			throws InvalidInputException {
		if (achievements == null || achievements.isEmpty()) {
			throw new InvalidInputException("Achievements passed can not be null or empty");
		}
		LOG.info("Adding achievements");
		organizationUnitSettingsDao.updateParticularKeyOrganizationUnitSettings(MongoOrganizationUnitSettingDaoImpl.KEY_ACHIEVEMENTS, achievements,
				unitSettings, collection);
		LOG.info("Achievements added successfully");
		return achievements;
	}

	@Override
	public Licenses addLicences(String collection, OrganizationUnitSettings unitSettings, List<String> authorisedIn) throws InvalidInputException {
		if (authorisedIn == null) {
			throw new InvalidInputException("Contact details passed can not be null");
		}

		Licenses licenses = unitSettings.getLicenses();
		if (licenses == null) {
			LOG.debug("Licenses not present for current profile, create a new license object");
			licenses = new Licenses();
		}
		licenses.setAuthorized_in(authorisedIn);
		LOG.info("Adding Licences list");
		organizationUnitSettingsDao.updateParticularKeyOrganizationUnitSettings(MongoOrganizationUnitSettingDaoImpl.KEY_LICENCES, licenses,
				unitSettings, collection);
		LOG.info("Licence authorisations added successfully");
		return licenses;
	}

	@Override
	public void updateSocialMediaTokens(String collection, OrganizationUnitSettings unitSettings, SocialMediaTokens mediaTokens)
			throws InvalidInputException {
		if (mediaTokens == null) {
			throw new InvalidInputException("Media tokens passed was null");
		}
		LOG.info("Updating the social media tokens in profile.");
		organizationUnitSettingsDao.updateParticularKeyOrganizationUnitSettings(MongoOrganizationUnitSettingDaoImpl.KEY_SOCIAL_MEDIA_TOKENS,
				mediaTokens, unitSettings, collection);
		LOG.info("Successfully updated the social media tokens.");
	}

}