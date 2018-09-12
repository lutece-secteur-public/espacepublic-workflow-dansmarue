--
-- Data for Name: workflow_state; Type: TABLE DATA; Schema: public; Owner: ramen_dev
--
INSERT INTO workflow_state (id_state, name, description, id_workflow, is_initial_state, is_required_workgroup_assigned, id_icon) VALUES (19, 'Vérification prestataire', 'Vérification de la présence d''un prestataire lié à cette tâche', 2, 0, 0, NULL);
INSERT INTO workflow_state (id_state, name, description, id_workflow, is_initial_state, is_required_workgroup_assigned, id_icon) VALUES (20, 'Echec d''envoi par WS', 'Echec de l''evoi du signalement au prestataire par WS', 2, 0, 0, NULL);
INSERT INTO workflow_state (id_state, name, description, id_workflow, is_initial_state, is_required_workgroup_assigned, id_icon) VALUES (21, 'Service programmé prestataire', 'Le service est programmé par le prestataire', 2, 0, 0, NULL);



--
-- Data for Name: workflow_action; Type: TABLE DATA; Schema: public; Owner: ramen_dev
--

-- Change of action "Etat initial" -> "Nouveau" to "Etat initial" --> "Vérification prestataire"
UPDATE workflow_action
SET id_state_after=19, id_workflow=2, id_icon=12
WHERE id_action=27;



--Action Transféré à un prestataire --> Service fait
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action) VALUES (66, 'Service fait', 'Réception du service fait par le prestataire', 2, 18, 10, 12, 0, 0);
--Action Transféré à un prestataire --> Rejeté
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action) VALUES (67, 'Service rejeté', 'Réception du rejet par le prestataire', 2, 18, 11, 12, 0, 0);
--Action Transféré à un prestataire --> Service programmé prestataire
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action) VALUES (68, 'Service programmé prestataire', 'Réception du service programmé par le prestataire', 2, 18, 21, 12, 0, 0);
--Action Transféré à un prestataire --> A requalifier
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action) VALUES (69, 'A requalifier', 'Réception de la requalification par le prestataire', 2, 18, 15, 12, 0, 0);
--Action Service programmé prestataire --> Service fait
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action) VALUES (70, 'Service fait', 'Service fait par le prestataire après programmation', 2, 21, 10, 12, 0, 0);
--Action Service programmé prestataire --> Service fait
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action) VALUES (71, 'Service rejeté', 'Service rejeté par le prestataire après programmation', 2, 21, 11, 12, 0, 0);
--Action Service programmé prestataire --> Service fait
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action) VALUES (72, 'Service programmé prestataire', 'Service reprogrammé par le prestataire après programmation', 2, 21, 21, 12, 0, 0);
--Action Service programmé prestataire --> Service fait
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action) VALUES (73, 'A requalifier', 'Service à requalifier par le prestataire après programmation', 2, 21, 15, 12, 0, 0);



--
-- Data for Name: workflow_task; Type: TABLE DATA; Schema: public; Owner: ramen_dev
--


INSERT INTO workflow_task (id_task, task_type_key, id_action ) VALUES (101, 'taskSignalementNotification', 27);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (102, 'taskSignalementWebService', 27);
INSERT INTO workflow_task (id_task, task_type_key, id_action ) VALUES (103, 'taskSignalementUserNotification3Contents', 67);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (104, 'taskSignalementUserNotification3Contents', 68);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (105, 'taskWebServiceComment', 66);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (106, 'taskWebServiceComment', 67);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (107, 'taskWebServiceComment', 68);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (108, 'taskWebServiceComment', 69);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (109, 'taskWebServiceComment', 70);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (110, 'taskWebServiceComment', 71);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (111, 'taskWebServiceComment', 72);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (112, 'taskWebServiceComment', 73);
INSERT INTO workflow_task (id_task, task_type_key, id_action ) VALUES (113, 'taskSignalementUserNotification3Contents', 71);
INSERT INTO workflow_task (id_task, task_type_key, id_action) VALUES (114, 'taskSignalementUserNotification3Contents', 72);


--
-- Table structure for table signalement_workflow_webservice_config
--

CREATE TABLE signalement_workflow_webservice_config
(
    id_task integer NOT NULL,
    id_state_withWS_success integer,
	id_state_withWS_failure integer,
    id_state_withoutWS integer,
	PRIMARY KEY (id_task)
);

--
-- Data for Name: signalement_workflow_webservice_config; Type: TABLE DATA; Schema: public; Owner: ramen_dev
--

INSERT INTO signalement_workflow_webservice_config (id_task, id_state_withWS_success, id_state_withWS_failure, id_state_withoutWS) VALUES (102, 18, 20, 7);


CREATE TABLE signalement_workflow_webservice_config_unit
(
id_task integer not null,
id_unit integer not null,
prestatairesansws character varying(8),
urlprestataire character varying(255)
);


CREATE TABLE signalement_workflow_webservice_value
(
id_history integer not null,
id_task integer not null,
webservice_value text,
CONSTRAINT signalement_workflow_webservice_value_pkey PRIMARY KEY (id_history , id_task )
);







INSERT INTO signalement_workflow_notifuser_3contents_config (id_task, subject, sender, message1, message2, message3) VALUES(103,'Signalement d''un incident', 'Mairie de Paris', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>');
INSERT INTO signalement_workflow_notifuser_3contents_config (id_task, subject, sender, message1, message2, message3) VALUES(104,'Signalement d''un incident', 'Mairie de Paris', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>');
INSERT INTO signalement_workflow_notifuser_3contents_config (id_task, subject, sender, message1, message2, message3) VALUES(113,'Signalement d''un incident', 'Mairie de Paris', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>');
INSERT INTO signalement_workflow_notifuser_3contents_config (id_task, subject, sender, message1, message2, message3) VALUES(114,'Signalement d''un incident', 'Mairie de Paris', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>', '<p>Nous avons bien reçu votre message mais les informations transmises sont insuffisantes ou inadaptées<br />et ne permettent pas à nos services d''y donner suite.<br />Avant de ne nous transmettre un nouveau message, nous vous invitons à vérifier si :<br /><br />- La localisation transmise est bien précise, en particulier dans un grand parc.<br />- L''incident se situe bien sur l''espace public municipal et non dans un espace privé.<br />- La description de la situation est précise.<br />- La qualité de la photo est correcte et adaptée.<br />- Une signalétique explicative n''est pas déjà mise en place autour de l''incident.<br />Ceci afin de nous permettre d''améliorer notre réactivité et de vous apporter toute satisfaction dans notre réponse.<br /><br />Nous vous remercions de votre participation.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public<br /></p>');