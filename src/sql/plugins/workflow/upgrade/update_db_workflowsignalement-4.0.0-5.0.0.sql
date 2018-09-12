ALTER TABLE signalement_workflow_notifuser_3contents_config DROP COLUMN IF EXISTS title1;
ALTER TABLE signalement_workflow_notifuser_3contents_config ADD title1 varchar(255);
ALTER TABLE signalement_workflow_notifuser_3contents_config DROP COLUMN IF EXISTS title2;
ALTER TABLE signalement_workflow_notifuser_3contents_config ADD title2 varchar(255);
ALTER TABLE signalement_workflow_notifuser_3contents_config DROP COLUMN IF EXISTS title3;
ALTER TABLE signalement_workflow_notifuser_3contents_config ADD title3 varchar(255);
