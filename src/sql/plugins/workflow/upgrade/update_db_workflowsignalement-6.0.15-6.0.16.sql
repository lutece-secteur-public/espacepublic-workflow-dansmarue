-- DMR-987 --
-- Workflow add action for daemon WebServiceDaemon
-- State start  id 20 (Echec d'envoi par WS)
INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(nextval('seq_workflow_action'), 'Transferer à un tiers', 'Demon appel WS prestataire', 2, 20, 19, null, 0, 0, 0, 0);

INSERT INTO workflow_action
(id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(nextval('seq_workflow_action'), 'Demon service fait', 'Demon WS prestataire pour service fait', 2, 20, 10, null, 0, 0, 0, 0);

INSERT INTO workflow_task (id_task, task_type_key, id_action, display_order)
VALUES(nextval('seq_workflow_task'), 'taskSignalementWebService', 85, null);

INSERT INTO workflow_task (id_task, task_type_key, id_action, display_order)
VALUES(nextval('seq_workflow_task'), 'taskSignalementWebService', 86, null);

-- DMR-896 --
-- Add task to notify partner for service done --
INSERT INTO workflow_task (id_task, task_type_key, id_action, display_order)
VALUES(nextval('seq_workflow_task'), 'taskSignalementWebService', 62, null);

INSERT INTO workflow_task (id_task, task_type_key, id_action, display_order)
VALUES(nextval('seq_workflow_task'), 'taskSignalementWebService', 70, null);

-- DMR-979 -- 
UPDATE workflow_state set name = 'Transféré à un tiers', description = 'Cette anomalie est en cours de traitement par un tiers externe' where id_state = 18;
UPDATE workflow_state set name = 'Service programmé tiers', description = 'Le service est programmé par le tiers' where id_state = 21;