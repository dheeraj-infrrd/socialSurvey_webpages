ALTER TABLE `ss_user`.`crm_batch_tracker`
ADD COLUMN `LAST_RUN_RECORD_FETCHED_COUNT` INT DEFAULT 0 AFTER `RECENT_RECORD_FETCHED_DATE`;
