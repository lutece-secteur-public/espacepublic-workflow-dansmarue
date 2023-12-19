/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.programme.web;

import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.BaliseFreemarkerDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.programme.business.NotificationServiceProgrammeSignalementTaskConfigDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.programme.service.NotificationServiceProgrammeSignalementTaskConfigService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The notification component
 */
public class NotificationServiceProgrammeComponent extends AbstractTaskComponent
{
    // MARKERS
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_BALISES = "balises";
    private static final String MARK_CONFIG = "config";

    private static final String MARK_NUMERO = "numero";
    private static final String MARK_TYPE = "type";
    private static final String MARK_ADRESSE = "adresse";
    private static final String MARK_PRIORITE = "priorite";
    private static final String MARK_DESCRIPTION = "description";
    private static final String MARK_PRECISION = "precision";
    private static final String MARK_LIEN_CONSULT = "lien";
    private static final String MARK_LIEN_SIGNALEMENT_WS = "wsSignalement";

    private static final String MARK_DATE_ENVOI = "dateEnvoi";
    private static final String MARK_HEURE_ENVOI = "heureEnvoi";
    private static final String MARK_EMAIL_USAGER = "emailUsager";
    private static final String MARK_DATE_PROGRAMMATION = "date_programmation";

    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    private static final String MARK_ID_ANOMALIE = "id_anomalie";
    private static final String MARK_NOTIFICATION = "notification";

    // MESSAGES
    private static final String MESSAGE_MANDATORY_FIELD = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";
    private static final String ERROR_SENDER = "module.workflow.dansmarue.task_notification_config.error.sender";
    private static final String ERROR_SUBJECT = "module.workflow.dansmarue.task_notification_config.error.subject";
    private static final String ERROR_MESSAGE = "module.workflow.dansmarue.task_notification_config.error.message";

    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFICATION_SIGNALEMENT_CONFIG = "admin/plugins/workflow/modules/signalement/task_notification_service_programme_signalement_config.html";
    private static final String TEMPLATE_TASK_NOTIFICATION_SIGNALEMENT_INFORMATION = "admin/plugins/workflow/modules/signalement/task_notif_SP_information.html";

    // LABELS
    private static final String LABEL_DATE_ENVOI = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.dateenvoi.label}";
    private static final String LABEL_HEURE_ENVOI = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.heureenvoi.label}";
    private static final String LABEL_EMAIL_USAGER = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.emailusager.label}";
    private static final String LABEL_DATE_PROGRAMMATION = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.dateprogrammation.label}";

    // SERVICES
    @Inject
    @Named( "signalement.notificationServiceProgrammeSignalementTaskConfigService" )
    private NotificationServiceProgrammeSignalementTaskConfigService _notificationSignalementTaskConfigService;

    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;
    @Inject
    @Named( "workflow.resourceHistoryService" )
    private IResourceHistoryService _resourceHistoryService;

    @Inject
    @Named( "typeSignalementService" )
    private ITypeSignalementService _typeSignalementService;

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, request.getLocale( ) );

        // Liste des balises freemaker pouvant être utilisées (à ajouter dans emailModel dans processAction))
        List<BaliseFreemarkerDTO> balises = new ArrayList<>( );
        BaliseFreemarkerDTO dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Id de l'anomalie" );
        dto.setValeur( MARK_ID_ANOMALIE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Numéro de l'anomalie" );
        dto.setValeur( MARK_NUMERO );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Type d'anomalie" );
        dto.setValeur( MARK_TYPE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Alias de l'anomalie" );
        dto.setValeur( MARK_ALIAS_ANOMALIE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Adresse de l'anomalie" );
        dto.setValeur( MARK_ADRESSE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Précision de la localisation" );
        dto.setValeur( MARK_PRECISION );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Priorité" );
        dto.setValeur( MARK_PRIORITE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Description" );
        dto.setValeur( MARK_DESCRIPTION );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Lien de consultation du message" );
        dto.setValeur( MARK_LIEN_CONSULT );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Lien vers le formulaire de gestion d'une anomalie par un prestataire" );
        dto.setValeur( MARK_LIEN_SIGNALEMENT_WS );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_DATE_ENVOI );
        dto.setValeur( MARK_DATE_ENVOI );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_HEURE_ENVOI );
        dto.setValeur( MARK_HEURE_ENVOI );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_EMAIL_USAGER );
        dto.setValeur( MARK_EMAIL_USAGER );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_DATE_PROGRAMMATION );
        dto.setValeur( MARK_DATE_PROGRAMMATION );
        balises.add( dto );
        model.put( MARK_BALISES, balises );

        model.put( MARK_CONFIG, _notificationSignalementTaskConfigService.findByPrimaryKey( task.getId( ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_SIGNALEMENT_CONFIG, locale, model );
        return template.getHtml( );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        // Sauvegarde de la config
        NotificationServiceProgrammeSignalementTaskConfigDTO configDTO = new NotificationServiceProgrammeSignalementTaskConfigDTO( );

        try
        {
            BeanUtils.populate( configDTO, request.getParameterMap( ) );
            configDTO.setIdTask( task.getId( ) );
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            Object [ ] tabError = {
                    e.getMessage( )
            };
            return AdminMessageService.getMessageUrl( request, e.getMessage( ), tabError, AdminMessage.TYPE_STOP );
        }

        if ( configDTO.getSender( ).equals( StringUtils.EMPTY ) )
        {
            strError = ERROR_SENDER;
        }
        else
            if ( configDTO.getSubject( ).equals( StringUtils.EMPTY ) )
            {
                strError = ERROR_SUBJECT;
            }
            else
                if ( configDTO.getMessage( ).equals( StringUtils.EMPTY ) )
                {
                    strError = ERROR_MESSAGE;
                }
        if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
        {
            Object [ ] tabRequiredFields = {
                    I18nService.getLocalizedString( strError, locale )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        if ( _notificationSignalementTaskConfigService.findByPrimaryKey( task.getId( ) ).getIdTask( ) == 0 )
        {
            _notificationSignalementTaskConfigService.insert( configDTO );
        }
        else
        {
            _notificationSignalementTaskConfigService.update( configDTO );
        }

        return null;
    }

    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_NOTIFICATION, _notificationSignalementTaskConfigService.getNotification( nIdHistory, task.getId( ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_SIGNALEMENT_INFORMATION, locale, model );

        return template.getHtml( );
    }

    @Override
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

}
