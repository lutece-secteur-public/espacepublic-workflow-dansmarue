DROP TABLE IF EXISTS signalement_workflow_rac_unit;
CREATE TABLE signalement_workflow_rac_unit
(
  id_config_unit integer NOT NULL,
  id_task integer NOT NULL,
  id_unit_source integer NOT NULL,
  id_type_signalement integer NOT NULL,
  id_unit_target integer NOT NULL,
  id_state_after integer NOT NULL,
  CONSTRAINT signalement_workflow_rac_unit_pkey PRIMARY KEY (id_config_unit),
  CONSTRAINT fk_signalement_rac_id_task FOREIGN KEY (id_task)
      REFERENCES workflow_task (id_task) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
);