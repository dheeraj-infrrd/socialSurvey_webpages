package com.realtech.socialsurvey.core.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the crm_master database table.
 */
@Entity
@Table(name = "DOTLOOP_PROFILE_LOOP_MAPPING")
@NamedQuery(name = "LoopProfileMapping.findAll", query = "SELECT lpm FROM LoopProfileMapping lpm")
public class LoopProfileMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	public LoopProfileMapping() {}

	@Id
	@Column(name = "PROFILE_LOOP_MAPPING_ID")
	private long id;

	@Column(name = "PROFILE_ID")
	private String profileId;

	@Column(name = "PROFILE_LOOP_ID")
	private String loopId;

	@Column(name = "PROFILE_LOOP_VIEW_ID")
	private String loopViewId;

	@Column(name = "LOOP_CLOSED_TIME")
	private Timestamp loopClosedTime;

	@Column(name = "COMPANY_ID")
	private long companyId;

	@Column(name = "REGION_ID")
	private long regionId;

	@Column(name = "BRANCH_ID")
	private long branchId;

	@Column(name = "AGENT_ID")
	private long agentId;

	@Column(name = "COLLECTION_NAME")
	private String collectionName;

	@Column(name = "CREATED_ON")
	private Timestamp createdOn;

	@Transient
	private String loopName;
	@Transient
	private String loopStatus;
	@Transient
	private long createdBy;
	@Transient
	private String lastUpdated;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getLoopId() {
		return loopId;
	}

	public void setLoopId(String loopId) {
		this.loopId = loopId;
	}

	public String getLoopViewId() {
		return loopViewId;
	}

	public void setLoopViewId(String loopViewId) {
		this.loopViewId = loopViewId;
	}

	public Timestamp getLoopClosedTime() {
		return loopClosedTime;
	}

	public void setLoopClosedTime(Timestamp loopClosedTime) {
		this.loopClosedTime = loopClosedTime;
	}

	public String getLoopName() {
		return loopName;
	}

	public void setLoopName(String loopName) {
		this.loopName = loopName;
	}

	public String getLoopStatus() {
		return loopStatus;
	}

	public void setLoopStatus(String loopStatus) {
		this.loopStatus = loopStatus;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

}