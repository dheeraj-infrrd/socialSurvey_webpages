package com.realtech.socialsurvey.core.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the organization_level_settings database table.
 * 
 */
@Entity
@Table(name="ORGANIZATION_LEVEL_SETTINGS")
@NamedQuery(name="OrganizationLevelSetting.findAll", query="SELECT o FROM OrganizationLevelSetting o")
public class OrganizationLevelSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ORGANIZATION_LEVEL_SETTINGS_ID")
	private long organizationLevelSettingsId;

	@Column(name="AGENT_ID")
	private long agentId;

	@Column(name="BRANCH_ID")
	private long branchId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_ON")
	private Timestamp createdOn;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_ON")
	private Timestamp modifiedOn;

	@Column(name="REGION_ID")
	private long regionId;

	@Column(name="SETTING_KEY")
	private String settingKey;

	@Column(name="SETTING_VALUE")
	private String settingValue;

	private int status;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COMPANY_ID")
	private Company company;

	public OrganizationLevelSetting() {
	}

	public long getOrganizationLevelSettingsId() {
		return this.organizationLevelSettingsId;
	}

	public void setOrganizationLevelSettingsId(long organizationLevelSettingsId) {
		this.organizationLevelSettingsId = organizationLevelSettingsId;
	}

	public long getAgentId() {
		return this.agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public long getBranchId() {
		return this.branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public long getRegionId() {
		return this.regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public String getSettingKey() {
		return this.settingKey;
	}

	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}

	public String getSettingValue() {
		return this.settingValue;
	}

	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}