ALTER TABLE `survey_details` 
ADD COLUMN `PARTICIPANT_TYPE` INT(11) NULL DEFAULT 0 AFTER `CUSTOMER_LAST_NAME`,
ADD INDEX `participant_type_idx` USING BTREE (`PARTICIPANT_TYPE` ASC);