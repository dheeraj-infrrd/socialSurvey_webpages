ALTER TABLE ss_user.EXTERNAL_SURVEY_TRACKER
ADD COLUMN POSTED_ON VARCHAR(100) NOT NULL DEFAULT 0 COMMENT '' AFTER REVIEW_DATE;