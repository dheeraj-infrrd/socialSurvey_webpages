package com.realtech.socialsurvey.core.services.mail.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.entities.EmailEntity;
import com.realtech.socialsurvey.core.entities.FileContentReplacements;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.services.mail.EmailSender;
import com.realtech.socialsurvey.core.services.mail.UndeliveredEmailException;
import com.realtech.socialsurvey.core.utils.FileOperations;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;
import com.sendgrid.SendGrid.Response;
import com.sendgrid.SendGridException;

/**
 * Uses sendgrid api to send mails
 * 
 * @author nishit
 */
@Component
public class SendGridEmailSenderImpl implements EmailSender, InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(SendGridEmailSenderImpl.class);

	@Value("${SENDGRID_SENDER_USERNAME}")
	private String sendGridUserName;

	@Value("${SENDGRID_SENDER_PASSWORD}")
	private String sendGridPassword;

	@Value("${SENDGRID_SENDER_NAME}")
	private String defaultSendName;

	@Value("${DEFAULT_EMAIL_FROM_ADDRESS}")
	private String defaultFromAddress;

	@Value("${SEND_MAIL}")
	private String sendMail;

	@Autowired
	private FileOperations fileOperations;

	private SendGrid sendGrid;

	private void sendMail(EmailEntity emailEntity) throws InvalidInputException, UndeliveredEmailException {
		LOG.debug("Sending mail: " + emailEntity.toString());
		if (emailEntity.getRecipients() == null || emailEntity.getRecipients().isEmpty()) {
			throw new InvalidInputException("No recipients to send mail");
		}
		if (emailEntity.getSenderEmailId() == null || emailEntity.getSenderEmailId().isEmpty()) {
			LOG.debug("Setting default from email id");
			emailEntity.setSenderEmailId(defaultFromAddress);
		}
		if (emailEntity.getSenderName() == null || emailEntity.getSenderName().isEmpty()) {
			LOG.debug("Setting default sender name");
			emailEntity.setSenderName(defaultSendName);
		}
		if (emailEntity.getBody() == null || emailEntity.getBody().isEmpty()) {
			throw new InvalidInputException("Email body is blank.");
		}
		if (emailEntity.getSubject() == null || emailEntity.getSubject().isEmpty()) {
			throw new InvalidInputException("Email subject is blank.");
		}

		// create and Email object and fill the details
		Email email = new Email();
		email.addTo(emailEntity.getRecipients().toArray(new String[emailEntity.getRecipients().size()]));
		email.setFrom(emailEntity.getSenderEmailId());
		email.setFromName(emailEntity.getSenderName());
		email.setSubject(emailEntity.getSubject());
		email.setHtml(emailEntity.getBody());

		Response response = null;
		try {
			LOG.debug("About to send mail. " + emailEntity.toString());
			response = sendGrid.send(email);
			LOG.debug("Sent the mail. " + emailEntity.toString());
		}
		catch (SendGridException e) {
			LOG.error("Exception while sending the mail. " + emailEntity.toString(), e);
			throw new UndeliveredEmailException("Could not send message. Reason: " + e.getMessage());
		}
		if (response.getStatus()) {
			LOG.debug("Mail sent successfully to " + emailEntity.toString());
		}
		else {
			LOG.error("Could not send mail to " + emailEntity.toString() + ". Reason: " + response.getMessage());
			throw new UndeliveredEmailException("Could not send message. Reason: " + response.getMessage());
		}
	}

	@Override
	public void sendEmailWithBodyReplacements(EmailEntity emailEntity, String subjectFileName, FileContentReplacements messageBodyReplacements)
			throws InvalidInputException, UndeliveredEmailException {
		LOG.info("Method sendEmailWithBodyReplacements called for emailEntity : " + emailEntity + " subjectFileName : " + subjectFileName
				+ " and messageBodyReplacements : " + messageBodyReplacements);
		// check if mail needs to be sent
		if (sendMail.equals(CommonConstants.YES_STRING)) {
			if (subjectFileName == null || subjectFileName.isEmpty()) {
				throw new InvalidInputException("Subject file name is null for sending mail");
			}
			if (messageBodyReplacements == null) {
				throw new InvalidInputException("Email body file name  and replacements are null for sending mail");
			}

			/**
			 * Read the subject template to get the subject and set in emailEntity
			 */
			LOG.debug("Reading template to set the mail subject");
			emailEntity.setSubject(fileOperations.getContentFromFile(subjectFileName));

			/**
			 * Read the mail body template, replace the required contents with arguments provided
			 * and set in emailEntity
			 */
			LOG.debug("Reading template to set the mail body");
			emailEntity.setBody(fileOperations.replaceFileContents(messageBodyReplacements));

			// Send the mail
			sendMail(emailEntity);
		}

		LOG.info("Method sendEmailWithBodyReplacements completed successfully");

	}

	@Override
	public void sendEmail(EmailEntity emailEntity, String subject, String mailBody) throws InvalidInputException, UndeliveredEmailException {
		LOG.info("Method sendEmailWithBodyReplacements called for subject : " + subject);
		if (sendMail.equals(CommonConstants.YES_STRING)) {
			if (subject == null || subject.isEmpty()) {
				throw new InvalidInputException("Subject is null for sending mail");
			}
			if (mailBody == null) {
				throw new InvalidInputException("Email body is null for sending mail");
			}

			LOG.debug("Setting the mail subject");
			emailEntity.setSubject(subject);

			LOG.debug("Setting the mail body");
			emailEntity.setBody(mailBody);

			// Send the mail
			sendMail(emailEntity);
		}
		LOG.info("Method sendEmailWithBodyReplacements completed successfully");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("Settings Up sendGrid gateway");

		if (sendGrid == null) {
			LOG.info("Initialising Sendgrid gateway with " + sendGridUserName + " and " + sendGridPassword);
			sendGrid = new SendGrid(sendGridUserName, sendGridPassword);
			LOG.info("Sendgrid gateway initialised!");
		}
	}

}