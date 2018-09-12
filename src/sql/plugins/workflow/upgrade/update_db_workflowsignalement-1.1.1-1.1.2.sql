-- The action "Accepter" becomes "A traiter" 
UPDATE workflow_action SET name='A traiter' WHERE id_action=13;
UPDATE workflow_icon SET name='A traiter (Signalement)' WHERE id_icon=8;

-- The labels "signalement" become "message"
UPDATE signalement_workflow_notification_user_config SET message='<p>Bonjour,</p><p>Vous avez déclaré un incident dont voici le récapitulatif :</p><p>Numéro du message : ${numero}  <br /></p><p>Type d''incident : ${type}</p><p> Adresse du message : ${adresse}</p><p>Précision de la localisation : ${precision}</p><p>Priorité : ${priorite}</p><p>Précisions complémentaires : ${commentaire}  <br /></p>' WHERE id_task=76;
UPDATE signalement_workflow_notification_user_config SET message='<p>Bonjour,</p><p>Vous avez déclaré un incident dont voici le récapitulatif :</p><p>Numéro du message : ${numero}  <br /></p><p>Type d''incident : ${type}</p><p> Adresse du message : ${adresse}</p><p>Précision de la localisation : ${precision}</p><p>Priorité : ${priorite}</p><p>Précisions complémentaires : ${commentaire}  <br /></p>' WHERE id_task=77;
