-- DMR-975 Clôture des anomalies trop anciennes --
-- Passer au statut service fait anomalie d'avant 2018--

-- Insert History
WITH insHistory AS (
	insert into workflow_resource_history (id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
	select id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 70, localtimestamp, 'admin' 
	from signalement_signalement, workflow_resource_workflow 
	where signalement_signalement.id_signalement = workflow_resource_workflow.id_resource
	and date_creation <= '2017-12-31'
	and id_state in (13, 7, 8, 15, 17, 16, 9, 19, 18, 21)
	RETURNING id_history
)
-- Insert Comment
insert into workflow_task_comment_value (id_history, id_task, comment_value)
select id_history, 150, 'Clôture via script' from insHistory;

-- Update state
update workflow_resource_workflow set id_state = 10 where id_resource in (
	select id_signalement from signalement_signalement, workflow_resource_workflow 
	where signalement_signalement.id_signalement = workflow_resource_workflow.id_resource
	and date_creation <= '2017-12-31'
	and id_state in (13, 7, 8, 15, 17, 16, 9, 19, 18, 21)
);

-- DMR-976 Requalification d'anomalie vers la DPSP
-- Requalifier anomalies de type  Objets abandonnés > Épave de vélo vers la DPSP
update signalement_signalement set fk_id_sector = 99037 where id_signalement in (
	select ss.id_signalement from signalement_signalement ss, unittree_unit_sector uus, workflow_resource_workflow wkf
	where ss.fk_id_sector = uus.id_sector
	and uus.id_unit in ( select id_unit from unittree_unit uu 
	                     where uu.id_parent = 0
	                     and uu.label = 'DPE')
	and ss.fk_id_type_signalement = 1006
	and ss.id_signalement = wkf.id_resource
	and wkf.id_state in (13, 7, 8, 15, 17, 16, 9, 19, 18, 21)
);	