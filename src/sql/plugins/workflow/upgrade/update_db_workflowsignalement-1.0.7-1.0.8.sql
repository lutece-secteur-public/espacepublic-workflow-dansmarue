DROP TABLE IF EXISTS signalement_workflow_notification_user_config;

--
-- Table structure for table signalement_workflow_notification_user_config
--
CREATE TABLE signalement_workflow_notification_user_config
(
  id_task integer NOT NULL,
  subject character varying(255),
  message text,
  sender character varying(255),
  CONSTRAINT signalement_workflow_notification_user_config_pkey PRIMARY KEY (id_task )
);

INSERT INTO signalement_workflow_notification_user_config (id_task, subject, message, sender) VALUES (76, 'Signalement d''un incident', '<p>Bonjour,</p><p>Vous avez déclaré un signalement dont voici le récapitulatif :</p><p>Numéro du signalement : ${numero}  <br /></p><p>Type de signalement : ${type}</p><p> Adresse du signalement : ${adresse}</p><p>Précision de la localisation : ${precision}</p><p>Priorité : ${priorite}</p><p>Précisions complémentaires : ${commentaire}  <br /></p>', 'Mairie de Paris');
INSERT INTO signalement_workflow_notification_user_config (id_task, subject, message, sender) VALUES (77, 'Signalement d''un incident', '<p>Bonjour,</p><p>Vous avez déclaré un signalement dont voici le récapitulatif :</p><p>Numéro du signalement : ${numero}  <br /></p><p>Type de signalement : ${type}</p><p> Adresse du signalement : ${adresse}</p><p>Précision de la localisation : ${precision}</p><p>Priorité : ${priorite}</p><p>Précisions complémentaires : ${commentaire}  <br /></p>', 'Mairie de Paris');


INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (76, 'taskSignalementUserNotification', 27);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (77, 'taskSignalementUserNotification', 28);
