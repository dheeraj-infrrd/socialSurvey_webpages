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
 * The persistent class for the company_invitation_license_key database table.
 * 
 */
@Entity
@Table(name="COMPANY_INVITATION_LICENSE_KEY")
@NamedQuery(name="CompanyInvitationLicenseKey.findAll", query="SELECT c FROM CompanyInvitationLicenseKey c")
public class CompanyInvitationLicenseKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="COMPANY_INVITATION_LICENSE_KEY_ID")
	private long companyInvitationLicenseKeyId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_ON")
	private Timestamp createdOn;

	@Column(name="LICENSE_KEY")
	private String licenseKey;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_ON")
	private Timestamp modifiedOn;

	private int status;

	@Column(name="VALID_UNTIL")
	private Timestamp validUntil;

	//bi-directional many-to-one association to AccountsMaster
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ACCOUNTS_MASTER_ID")
	private AccountsMaster accountsMaster;

	public CompanyInvitationLicenseKey() {
	}

	public long getCompanyInvitationLicenseKeyId() {
		return this.companyInvitationLicenseKeyId;
	}

	public void setCompanyInvitationLicenseKeyId(long companyInvitationLicenseKeyId) {
		this.companyInvitationLicenseKeyId = companyInvitationLicenseKeyId;
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

	public String getLicenseKey() {
		return this.licenseKey;
	}

	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
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

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getValidUntil() {
		return this.validUntil;
	}

	public void setValidUntil(Timestamp validUntil) {
		this.validUntil = validUntil;
	}

	public AccountsMaster getAccountsMaster() {
		return this.accountsMaster;
	}

	public void setAccountsMaster(AccountsMaster accountsMaster) {
		this.accountsMaster = accountsMaster;
	}

}