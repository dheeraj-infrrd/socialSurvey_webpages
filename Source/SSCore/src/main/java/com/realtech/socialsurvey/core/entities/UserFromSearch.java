package com.realtech.socialsurvey.core.entities;

import java.util.List;

/**
 * User entity from the search
 */
public class UserFromSearch {

	private long userId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String loginName;
	private int isOwner;
	private String displayName;
	private long companyId;
	private int status;
	private boolean isRegionAdmin;
	private boolean isBranchAdmin;
	private boolean isAgent;
	private List<Long> regions;
	private List<Long> branches;
	private boolean canEdit;
	private String title;
	private String aboutMe;
	private String profileUrl;
	private String profileName;
	private String profileImageUrl;
	private String profileImageThumbnail;
	private List<Long> agentIds;
	private long reviewCount;
	private boolean isProfileImageSet;

	public List<Long> getRegions() {
		return regions;
	}

	public void setRegions(List<Long> regions) {
		this.regions = regions;
	}

	public List<Long> getBranches() {
		return branches;
	}

	public void setBranches(List<Long> branches) {
		this.branches = branches;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public int getIsOwner() {
		return isOwner;
	}

	public void setIsOwner(int isOwner) {
		this.isOwner = isOwner;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean getIsRegionAdmin() {
		return isRegionAdmin;
	}

	public void setRegionAdmin(boolean isRegionAdmin) {
		this.isRegionAdmin = isRegionAdmin;
	}

	public boolean getIsBranchAdmin() {
		return isBranchAdmin;
	}

	public void setBranchAdmin(boolean isBranchAdmin) {
		this.isBranchAdmin = isBranchAdmin;
	}

	public boolean getIsAgent() {
		return isAgent;
	}

	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}

	public boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public List<Long> getAgentIds() {
		return agentIds;
	}

	public void setAgentIds(List<Long> agentIds) {
		this.agentIds = agentIds;
	}

	public long getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(long reviewCount) {
		this.reviewCount = reviewCount;
	}

	/**
     * @return the profileImageThumbnail
     */
    public String getProfileImageThumbnail()
    {
        return profileImageThumbnail;
    }

    /**
     * @param profileImageThumbnail the profileImageThumbnail to set
     */
    public void setProfileImageThumbnail( String profileImageThumbnail )
    {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    /**
     * @return the isProfileImageSet
     */
    public boolean isProfileImageSet()
    {
        return isProfileImageSet;
    }

    /**
     * @param isProfileImageSet the isProfileImageSet to set
     */
    public void setProfileImageSet( boolean isProfileImageSet )
    {
        this.isProfileImageSet = isProfileImageSet;
    }

    @Override
	public String toString() {
		return "user id: " + userId + "\temailId: " + emailId + "\tdisplayName: " + displayName + "\tstatus: " + status + "\tisRegionAdmin: "
				+ isRegionAdmin + "\tisBranchAdmin: " + isBranchAdmin + "\tisAgent: " + isAgent + "\tcanEdit: " + canEdit;
	}
}