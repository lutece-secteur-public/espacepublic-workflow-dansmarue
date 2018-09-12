------------------------------------------------------------
-------- DMR 722 fix surveillance - Requalification --------
------------------------------------------------------------

--Sous surveillance --> Etat initial* (Action Requalifier)
UPDATE workflow_action SET id_state_after = 13 WHERE id_action = 84;

------------------------------------------------------------
--------   DMR 545 Objet unique pour les échanges   --------
------------------------------------------------------------

update
	public.signalement_workflow_notifuser_3contents_config
set	
	subject_ramen = 'DansMaRue : Suivi de l’anomalie n°${numero}'
where
	id_task = 133;

update
	public.signalement_workflow_notifuser_3contents_config
set
	subject_ramen = 'DansMaRue : Suivi de l’anomalie n°${numero}'
where
	id_task = 85;
