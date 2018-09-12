---PM2015- Update comment
UPDATE workflow_task_comment_config
SET title='Ce commentaire est destiné à un usage interne lié au suivi du dossier,<p> il ne sera pas vu par l''usager </p>'
WHERE id_task IN
(SELECT id_task 
FROM workflow_task 
WHERE id_action in (16,21,22,41,49,53)
AND task_type_key='taskTypeComment');

-- fix problems with Signalement that exist on workflow_resource_workflow but not in signalement_signalement
update workflow_resource_workflow set id_state=11
where id_resource in (
(select id_resource from workflow_resource_workflow
where resource_type='SIGNALEMENT_SIGNALEMENT' 
)
EXCEPT 
(select id_signalement from signalement_signalement)
)
AND resource_type='SIGNALEMENT_SIGNALEMENT' ;
