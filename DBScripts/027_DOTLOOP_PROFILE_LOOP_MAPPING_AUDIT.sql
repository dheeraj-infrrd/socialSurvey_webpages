ALTER TABLE ss_user.`DOTLOOP_PROFILE_LOOP_MAPPING` ADD COLUMN `CREATED_ON` TIMESTAMP NOT NULL;

UPDATE ss_user.`DOTLOOP_PROFILE_LOOP_MAPPING` SET CREATED_ON = NOW();