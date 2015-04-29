CREATE TABLE IF NOT EXISTS `ss_user`.`USERCOUNT_MODIFICATION_NOTIFICATION` (
	`USERCOUNT_MODIFICATION_NOTIFICATION_ID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`COMPANY_ID` INT UNSIGNED NOT NULL,
	`STATUS` INT(1) NOT NULL,
	`CREATED_ON` TIMESTAMP NOT NULL,
	`MODIFIED_ON` TIMESTAMP NOT NULL,
	PRIMARY KEY (`USERCOUNT_MODIFICATION_NOTIFICATION_ID`),
	INDEX `fk_USERCOUNT_MODIFICATION_NOTIFICATION_COMPANY1_idx` (`COMPANY_ID` ASC),
	CONSTRAINT `fk_USERCOUNT_MODIFICATION_NOTIFICATION_COMPANY1`
		FOREIGN KEY (`COMPANY_ID`)
		REFERENCES `ss_user`.`COMPANY` (`COMPANY_ID`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Holds records if any active user has been added or deleted from a company';