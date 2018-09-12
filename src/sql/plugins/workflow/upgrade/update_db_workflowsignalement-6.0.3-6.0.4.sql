----------------------------------------
--------DMR 688 fix surveillance--------
----------------------------------------

--Ajout signalement_workflow_notification_user_config
INSERT INTO signalement_workflow_notification_user_config
(id_task, subject, sender, title, message)
VALUES(192, 'Dansmarue : Suivi de l’anomalie ${numero}', 'Mairie de Paris', NULL, '<p>Bonjour,<br/></p>');

INSERT INTO signalement_workflow_notification_user_config
(id_task, subject, sender, title, message)
VALUES(196, 'Dansmarue : Suivi de l’anomalie ${numero}', 'Mairie de Paris', NULL, '<p>Bonjour,<br/></p>');

INSERT INTO signalement_workflow_notification_user_config
(id_task, subject, sender, title, message)
VALUES(201, 'Dansmarue : Suivi de l’anomalie ${numero}', 'Mairie de Paris', NULL, '<p>Bonjour,<br/></p>');

--Ajout workflow_task_comment_config
INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(193, 'Commentaire interne non vu par l’usager (facultatif)', 0, 0);

INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(197, 'Commentaire interne non vu par l’usager (facultatif)', 0, 0);

INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(198, 'Commentaire interne non vu par l’usager (facultatif)', 0, 0);

INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(202, 'Commentaire interne non vu par l’usager (facultatif)', 0, 0);

INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(204, 'Commentaire interne non vu par l’usager (facultatif)', 0, 0);

INSERT INTO workflow_task_comment_config
(id_task, title, is_mandatory, is_richtext)
VALUES(179, 'Ajoutez un commentaire si besoin', 0, 0);


--surveillance -> etat X

-- DMR 348 WS Prestetaires Debut -- 
INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(211, 'taskWebServiceComment', 70, NULL);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(212, 'taskWebServiceComment', 62, NULL);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(213, 'taskWebServiceComment', 64, NULL);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(214, 'taskWebServiceComment', 71, NULL);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(215, 'taskWebServiceComment', 68, NULL);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(216, 'taskWebServiceComment', 63 , NULL);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(217, 'taskWebServiceComment', 73 , NULL);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(218, 'taskWebServiceComment', 78 , NULL);

INSERT INTO workflow_task
(id_task, task_type_key, id_action, display_order)
VALUES(219, 'taskWebServiceComment', 79 , NULL);

-- DMR 348 WS Prestetaires Fin -- 
