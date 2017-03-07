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
 * The persistent class for the user_invite database table.
 * 
 */
@Entity
@Table(name="USER_INVITE")
@NamedQuery(name="UserInvite.findAll", query="SELECT u FROM UserInvite u")
public class UserInvite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="USER_INVITE_ID")
	private long userInviteId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_ON")
	private Timestamp createdOn;

	@Column(name="INVITATION_EMAIL_ID")
	private String invitationEmailId;

	@Column(name="INVITATION_KEY")
	private String invitationKey;

	@Column(name="INVITATION_SENT_BY")
	private int invitationSentBy;

	@Column(name="INVITATION_TIME")
	private Timestamp invitationTime;

	@Column(name="INVITATION_PARAMETERS")
	private String invitationParameters;
	
	@Column(name="INVITATION_VALID_UNTIL")
	private Timestamp invitationValidUntil;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_ON")
	private Timestamp modifiedOn;

	private int status;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COMPANY_ID")
	private Company company;

	//bi-directional many-to-one association to ProfilesMaster
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PROFILE_MASTERS_ID")
	private ProfilesMaster profilesMaster;

	public UserInvite() {
	}

	public long getUserInviteId() {
		return this.userInviteId;
	}

	public void setUserInviteId(long userInviteId) {
		this.userInviteId = userInviteId;
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

	public String getInvitationEmailId() {
		return this.invitationEmailId;
	}

	public void setInvitationEmailId(String invitationEmailId) {
		this.invitationEmailId = invitationEmailId;
	}

	public String getInvitationKey() {
		return this.invitationKey;
	}

	public void setInvitationKey(String invitationKey) {
		this.invitationKey = invitationKey;
	}

	public int getInvitationSentBy() {
		return this.invitationSentBy;
	}

	public void setInvitationSentBy(int invitationSentBy) {
		this.invitationSentBy = invitationSentBy;
	}

	public Timestamp getInvitationTime() {
		return this.invitationTime;
	}

	public void setInvitationTime(Timestamp invitationTime) {
		this.invitationTime = invitationTime;
	}

	public Timestamp getInvitationValidUntil() {
		return this.invitationValidUntil;
	}

	public void setInvitationValidUntil(Timestamp invitationValidUntil) {
		this.invitationValidUntil = invitationValidUntil;
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

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public ProfilesMaster getProfilesMaster() {
		return this.profilesMaster;
	}

	public void setProfilesMaster(ProfilesMaster profilesMaster) {
		this.profilesMaster = profilesMaster;
	}

	public String getInvitationParameters() {
		return invitationParameters;
	}

	public void setInvitationParameters(String invitationParameters) {
		this.invitationParameters = invitationParameters;
	}
}