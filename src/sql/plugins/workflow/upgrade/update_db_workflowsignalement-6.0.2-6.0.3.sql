
-- DMR 308 DEBUT --

-- Workflow Mise en surveillance vers Nouveau  --

UPDATE workflow_state set display_order = display_order+1 WHERE display_order >=7;

INSERT INTO workflow_state
(id_state, name, description, id_workflow, is_initial_state, is_required_workgroup_assigned, id_icon, display_order)
VALUES(22 , 'Mise en surveillance', 'Mise en surveillance d''une anomalie', 2, 0, 0, null, 7);

INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(76, 'Surveiller', 'Surveiller une anomalie', 2, 7, 22, 6, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(175, 'taskInformationsSignalement', 76, 1);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(176, 'taskMiseEnSurveillance', 76, 2);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(177, 'taskSignalementUserNotification', 76, 3);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(178, 'taskTypeComment', 76, 4);
-- Fin --

-- Workflow Mise en surveillance vers Mise en A traiter --
INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(77, 'A traiter', 'Le signalement devra être traité', 2, 22, 8, 8, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(179, 'taskTypeComment', 77, 1);
-- Fin --

INSERT INTO signalement_workflow_notification_user_config
(id_task, subject, sender, title, message)
VALUES(177, 'DansMaRue : Surveillance de l''anomalie  ${numero}', 'Mairie de Paris', NULL, '<p>Bonjour,<br/></p>');

INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(178, 'Ce commentaire est destiné à un usage interne lié au suivi du dossier, il ne sera pas vu par l''usager (facultatif)', 0, 0);


ALTER TABLE signalement_signalement ADD COLUMN date_mise_surveillance timestamp NULL;

-- DMR 308 FIN --

-- DMR 567 DEBUT --

DELETE FROM signalement_workflow_webservice_value WHERE id_history IN (
SELECT ws.id_history FROM signalement_workflow_webservice_value ws 
LEFT OUTER JOIN workflow_resource_history  wk ON (ws.id_history = wk.id_history)
WHERE wk.id_history is null);

ALTER TABLE signalement_workflow_webservice_value
ADD CONSTRAINT fk_id_history
FOREIGN KEY (id_history) REFERENCES workflow_resource_history(id_history) ON DELETE CASCADE;

-- DMR 567 FIN --

-- DMR 127 DEBUT Ajout action  Requalifier au Workflow--
INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(78, 'Requalifier', 'Requalifier un signalement', 2, 18, 7, 12, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(180, 'taskSignalementRequalification', 78, 1);
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(181, 'taskTypeComment', 78, 2);
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(182, 'taskSignalementCreation', 78, 3);
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(183, 'taskSignalementWebService', 78, 4);
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(184, 'taskSignalementNotification', 78, 5);

INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(79, 'Requalifier', 'Requalifier un signalement', 2, 21, 7, 12, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(185, 'taskSignalementRequalification', 79, 1);
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(186, 'taskTypeComment', 79, 2);
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(187, 'taskSignalementCreation', 79, 3);
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(188, 'taskSignalementWebService', 79, 4);
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(189, 'taskSignalementNotification', 79, 5);

INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(181, 'Commentaire interne non vu par l’usager (facultatif)', 0, 0);

INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(186, 'Commentaire interne non vu par l’usager (facultatif)', 0, 0);

--DMR 127 FIN --



-----------------------
--------DMR 661--------
-----------------------
update workflow_state set name='Sous surveillance' where id_state=22;
update signalement_workflow_notification_user_config set subject='Dansmarue : Suivi de l’anomalie ${numero}' where id_task=177;

--A traiter --> Sous surveillance (Action de mise en surveillance)
INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(80, 'Surveiller', 'Surveiller une anomalie', 2, 8, 22, 6, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(190, 'taskInformationsSignalement', 80, 1);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(191, 'taskMiseEnSurveillance', 80, 2);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(192, 'taskSignalementUserNotification', 80, 3);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(193, 'taskTypeComment', 80, 4);

--A requalifier --> Sous surveillance (Action de mise en surveillance)
INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(81, 'Surveiller', 'Surveiller une anomalie', 2, 15, 22, 6, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(194, 'taskInformationsSignalement', 81, 1);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(195, 'taskMiseEnSurveillance', 81, 2);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(196, 'taskSignalementUserNotification', 81, 3);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(197, 'taskTypeComment', 81, 4);

--Sous surveillance --> A requalifier (Action A requalifier)
INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(82, 'A requalifier', 'Signalement à requalifier', 2, 22, 15, 16, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(198, 'taskTypeComment', 82, 1);

--Sous surveillance --> Service programmé (Action Programmer)
INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(83, 'Programmer', 'Programme le traitement du signalement', 2, 22, 9, 9, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(199, 'taskInformationsSignalement', 83, 1);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(200, 'taskSignalementProgrammation', 83, 2);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(201, 'taskSignalementUserNotification', 83, 3);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(202, 'taskTypeComment', 83, 4);

--Sous surveillance --> Vérification prestataire* (Action Requalifier)
INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(84, 'Requalifier', 'Requalifie le signalement', 2, 22, 19, 12, 0, 0, 0, 0);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(203, 'taskSignalementRequalification', 84, 1);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(204, 'taskTypeComment', 84, 2);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(205, 'taskSignalementCreation', 84, 3);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(206, 'taskSignalementWebService', 84, 4);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(207, 'taskSignalementNotification', 84, 5);

