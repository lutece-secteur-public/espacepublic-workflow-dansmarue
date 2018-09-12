DROP TABLE IF EXISTS signalement_workflow_notification_config;
DROP TABLE IF EXISTS signalement_workflow_notification_config_unit;

CREATE TABLE signalement_workflow_notification_config
(
  id_task integer NOT NULL,
  subject character varying(255),
  message text,
  sender character varying(255),
  CONSTRAINT signalement_workflow_notification_config_pkey PRIMARY KEY (id_task )
);

CREATE TABLE signalement_workflow_notification_config_unit
(
  id_task integer NOT NULL,
  destinataires character varying(255),
  id_unit bigint NOT NULL,
  CONSTRAINT signalement_workflow_notification_config_unit_pkey PRIMARY KEY (id_task , id_unit )
);

INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (35, 'taskSignalementNotification', 27);