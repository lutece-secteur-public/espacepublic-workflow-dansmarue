-- DMR-944 Passage au statut nouveau  --
-- Des anomalies affectées DEVE, DEVE-SAB au statut Transféré à un prestataire --

-- DEVE --
update workflow_resource_workflow set id_state = 7 where id_resource in (
  select ss.id_signalement from signalement_signalement ss, unittree_unit_sector uus, workflow_resource_workflow wkf
	where ss.fk_id_sector = uus.id_sector
	and ss.id_signalement = wkf.id_resource
	and uus.id_unit in ( select id_unit from unittree_unit uu 
	                     where uu.id_parent = 0
	                    and uu.label = 'DEVE')
	and wkf.id_state in (18)
);

-- DEVE-SAB --
update workflow_resource_workflow set id_state = 7 where id_resource in (
  select ss.id_signalement from signalement_signalement ss, unittree_unit_sector uus, workflow_resource_workflow wkf
	where ss.fk_id_sector = uus.id_sector
	and ss.id_signalement = wkf.id_resource
	and uus.id_unit in ( select id_unit from unittree_unit uu 
	                     where uu.id_parent = 0
	                    and uu.label = 'DEVE-SAB')
	and wkf.id_state in (18)
);
	