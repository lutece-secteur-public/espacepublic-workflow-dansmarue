-- DMR 968 --
-- Purge des tables du workflow faisant référence à des id_signalement --
-- non présenent en base de donées. --

-- signalement_workflow_notification_user_value
delete from signalement_workflow_notification_user_value where id_history in (
	select rh.id_history from workflow_resource_history rh, signalement_workflow_notification_user_value nuv 
	where  rh.id_history = nuv.id_history
	and rh.id_resource > (select max(id_signalement) from signalement_signalement)
);

--signalement_workflow_notification_suivi_value
delete from signalement_workflow_notification_suivi_value where id_history in (
	select rh.id_history from workflow_resource_history rh, signalement_workflow_notification_suivi_value nsv
	where  rh.id_history = nsv.id_history
	and rh.id_resource > (select max(id_signalement) from signalement_signalement)
);

-- signalement_workflow_notifuser_3contents_value
delete from signalement_workflow_notifuser_3contents_value where id_history in (
	select rh.id_history from workflow_resource_history rh, signalement_workflow_notifuser_3contents_value ncv
	where  rh.id_history = ncv.id_history
	and rh.id_resource > (select max(id_signalement) from signalement_signalement)
);


-- signalement_workflow_webservice_value
delete from signalement_workflow_webservice_value where id_history in (
	select rh.id_history from workflow_resource_history rh, signalement_workflow_webservice_value wsv
	where  rh.id_history = wsv.id_history
	and rh.id_resource > (select max(id_signalement) from signalement_signalement)
);

-- workflow_task_comment_value
delete from workflow_task_comment_value where id_history in (
	select rh.id_history from workflow_resource_history rh, workflow_task_comment_value tcv
	where  rh.id_history = tcv.id_history
	and rh.id_resource > (select max(id_signalement) from signalement_signalement)
);

-- workflow_task_notify_gru_history
delete from workflow_task_notify_gru_history where id_history in (
	select rh.id_history from workflow_resource_history rh, workflow_task_notify_gru_history tgru
	where  rh.id_history = tgru.id_history
	and rh.id_resource > (select max(id_signalement) from signalement_signalement)
);

-- workflow_resource_history
delete from workflow_resource_history  where id_history in (
	select id_history from workflow_resource_history where id_resource > (select max(id_signalement) from signalement_signalement)
);

-- workflow_resource_workflow
delete from workflow_resource_workflow  where id_resource in (
  select id_resource from workflow_resource_workflow where id_resource > (select max(id_signalement) from signalement_signalement)
);