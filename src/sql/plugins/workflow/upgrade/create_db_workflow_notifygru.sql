/*DROP TABLE IF EXISTS task_notify_gru_ef; */
DROP TABLE IF EXISTS workflow_task_notify_gru_cf;
DROP TABLE IF EXISTS workflow_task_notify_gru_history;


/*==============================================================*/
/* Table structure for table workflow_task_notify_gru_cf					*/
/*==============================================================*/


  
  
CREATE TABLE workflow_task_notify_gru_cf(
  id_task INT NOT NULL,
 id_spring_provider VARCHAR(255) DEFAULT NULL,
 marker_provider_ids VARCHAR(1000) NULL,
 demand_status INT DEFAULT  NULL, 
 crm_status_id INT DEFAULT  1, 
 set_onglet SMALLINT  NULL DEFAULT NULL,
  message_guichet TEXT DEFAULT NULL,  
  status_text_guichet VARCHAR(255) DEFAULT  NULL,  
  sender_name_guichet VARCHAR(255) DEFAULT  NULL,  
  subject_guichet VARCHAR(255) DEFAULT  NULL,  
  demand_max_step_guichet SMALLINT NULL DEFAULT 1,
  demand_user_current_step_guichet SMALLINT NULL DEFAULT 1,
  is_active_onglet_guichet SMALLINT NULL DEFAULT 0,
  status_text_agent  VARCHAR(255) DEFAULT  NULL,  
 message_agent text,  
  is_active_onglet_agent SMALLINT NOT NULL DEFAULT 0,
 subject_email VARCHAR(255) DEFAULT   NULL,
 message_email text,  
  sender_name_email VARCHAR(255) DEFAULT   NULL,
  recipients_cc_email VARCHAR(255) DEFAULT   NULL,
  recipients_cci_email VARCHAR(255) DEFAULT   NULL,
  is_active_onglet_email SMALLINT  NULL DEFAULT 0,
  message_sms text,  
  is_active_onglet_sms SMALLINT NOT NULL DEFAULT 0,
 id_mailing_list_broadcast INT DEFAULT NULL,
 email_broadcast VARCHAR(255) DEFAULT NULL,
   sender_name_broadcast VARCHAR(255) DEFAULT   NULL,
subject_broadcast VARCHAR(255) DEFAULT   NULL,
 message_broadcast text,  
  recipients_cc_broadcast VARCHAR(255) DEFAULT   NULL,
  recipients_cci_broadcast VARCHAR(255) DEFAULT   NULL,
  is_active_onglet_broadcast SMALLINT  NULL DEFAULT 0,
  PRIMARY KEY  (id_task)
  );


CREATE TABLE workflow_task_notify_gru_history(
  id_history INT NOT NULL,
  id_task INT DEFAULT NULL,
   crm_status_id INT DEFAULT  1, 
  message_guichet TEXT DEFAULT NULL,  
  status_text_guichet VARCHAR(255) DEFAULT  NULL,  
  sender_name_guichet VARCHAR(255) DEFAULT  NULL,  
  subject_guichet VARCHAR(255) DEFAULT  NULL,  
  demand_max_step_guichet SMALLINT NULL DEFAULT 1,
  demand_user_current_step_guichet SMALLINT NULL DEFAULT 1,
  is_active_onglet_guichet SMALLINT NULL DEFAULT 0,
  status_text_agent  VARCHAR(255) DEFAULT  NULL,  
 message_agent text,  
  is_active_onglet_agent SMALLINT NOT NULL DEFAULT 0,
 subject_email VARCHAR(255) DEFAULT   NULL,
 message_email text,  
  sender_name_email VARCHAR(255) DEFAULT   NULL,
  recipients_cc_email VARCHAR(255) DEFAULT   NULL,
  recipients_cci_email VARCHAR(255) DEFAULT   NULL,
  is_active_onglet_email SMALLINT  NULL DEFAULT 0,
  message_sms text,  
  is_active_onglet_sms SMALLINT NOT NULL DEFAULT 0,
 id_mailing_list_broadcast INT DEFAULT NULL,
 email_broadcast VARCHAR(255) DEFAULT NULL,
   sender_name_broadcast VARCHAR(255) DEFAULT   NULL,
subject_broadcast VARCHAR(255) DEFAULT   NULL,
 message_broadcast text,  
  recipients_cc_broadcast VARCHAR(255) DEFAULT   NULL,
  recipients_cci_broadcast VARCHAR(255) DEFAULT   NULL,
  is_active_onglet_broadcast SMALLINT  NULL DEFAULT 0,
  PRIMARY KEY  (id_history, id_task)
  );


