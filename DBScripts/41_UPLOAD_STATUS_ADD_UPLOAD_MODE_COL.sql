ALTER TABLE `ss_user`.`UPLOAD_STATUS` 
ADD COLUMN `UPLOAD_MODE` CHAR(1) NOT NULL DEFAULT 'D' COMMENT '' AFTER `STATUS`;
