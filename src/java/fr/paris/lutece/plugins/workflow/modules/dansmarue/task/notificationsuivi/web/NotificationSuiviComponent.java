/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.BaliseFreemarkerDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.service.NotificationSuiviValueService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 *
 * NotificationSuiviComponent
 *
 */
public class NotificationSuiviComponent extends AbstractTaskComponent
{

    // MARKERS
    private static final String            MARK_CONFIG                                  = "config";
    private static final String            MARK_WEBAPP_URL                              = "webapp_url";
    private static final String            MARK_LOCALE                                  = "locale";
    private static final String            MARK_BALISES                                 = "balises";

    private static final String            MARK_NUMERO                                  = "numero";
    private static final String            MARK_TYPE                                    = "type";
    private static final String            MARK_ADRESSE                                 = "adresse";
    private static final String            MARK_PRIORITE                                = "priorite";
    private static final String            MARK_COMMENTAIRE                             = "commentaire";
    private static final String            MARK_PRECISION                               = "precision";
    private static final String            MARK_LIEN_CONSULTATION                       = "lien_consultation";

    private static final String            MARK_DATE_PROGRAMMATION                      = "date_programmation";
    private static final String            MARK_DATE_DE_TRAITEMENT                      = "datetraitement";
    private static final String            MARK_HEURE_DE_TRAITEMENT                     = "heuretraitement";

    private static final String            MARK_DATE_ENVOI                              = "dateEnvoi";
    private static final String            MARK_HEURE_ENVOI                             = "heureEnvoi";
    private static final String            MARK_EMAIL_USAGER                            = "emailUsager";

    private static final String            MARK_ALIAS_ANOMALIE                          = "alias_anomalie";
    private static final String            MARK_ALIAS_MOBILE_ANOMALIE                   = "alias_mobile_anomalie";

    private static final String            MARK_ID_ANOMALIE                             = "id_anomalie";

    private static final String            MARK_NOTIFICATION_SUIVI                      = "notification_suivi";

    // MESSAGES
    private static final String            MESSAGE_EXCEPTION_OCCURED                    = "module.workflow.dansmarue.task_notification_config.message.exception";
    private static final String            MESSAGE_MANDATORY_FIELD                      = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";
    private static final String            ERROR_SENDER                                 = "module.workflow.dansmarue.task_notification_config.error.sender";
    private static final String            ERROR_SUBJECT                                = "module.workflow.dansmarue.task_notification_config.error.subject";
    private static final String            ERROR_MOBILE_SUBJECT                         = "module.workflow.dansmarue.task_notification_config.error.mobile.subject";
    private static final String            ERROR_MAIL_MESSAGE                           = "module.workflow.dansmarue.task_notification_config.suivi.mail.message.title";
    private static final String            ERROR_MOBILE_MESSAGE                         = "module.workflow.dansmarue.task_notification_config.suivi.mobile.message.title";

    // TEMPLATES
    private static final String            TEMPLATE_TASK_NOTIFICATION_SUIVI_CONFIG      = "admin/plugins/workflow/modules/signalement/task_notification_suivi_config.html";
    private static final String            TEMPLATE_TASK_NOTIFICATION_SUIVI_INFORMATION = "admin/plugins/workflow/modules/signalement/task_notification_suivi_information.html";

    // LABELS
    private static final String            LABEL_DATE_ENVOI                             = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.dateenvoi.label}";
    private static final String            LABEL_HEURE_ENVOI                            = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.heureenvoi.label}";
    private static final String            LABEL_EMAIL_USAGER                           = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.emailusager.label}";
    private static final String            LABEL_DATE_PROGRAMMATION                     = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.dateprogrammation.label}";
    private static final String            LABEL_DATE_DE_TRAITEMENT                     = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.datetraitement.label}";
    private static final String            LABEL_HEURE_DE_TRAITEMENT                    = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.heuretraitement.label}";

    // SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService            _signalementService;

    @Inject
    @Named( "signalement.notificationSuiviValueService" )
    private NotificationSuiviValueService  _notificationSuiviValueService;
    @Inject
    @Named( "signalement.notificationSignalementSuiviTaskConfigDAO" )
    private NotificationSuiviTaskConfigDAO _notificationSuiviTaskConfigDAO;

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        NotificationSuiviTaskConfig config = _notificationSuiviTaskConfigDAO.load( task.getId( ), SignalementUtils.getPlugin( ) );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_CONFIG, config );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, request.getLocale( ) );

        // Liste des balises freemaker pouvant être utilisées (à ajouter dans emailModel dans processAction))
        List<BaliseFreemarkerDTO> balises = new ArrayList<BaliseFreemarkerDTO>( );
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
        dto.setNom( "Alias mobile de l'anomalie" );
        dto.setValeur( MARK_ALIAS_MOBILE_ANOMALIE );
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
        dto.setNom( "Commentaire" );
        dto.setValeur( MARK_COMMENTAIRE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Lien de consultation du message" );
        dto.setValeur( MARK_LIEN_CONSULTATION );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_DATE_PROGRAMMATION );
        dto.setValeur( MARK_DATE_PROGRAMMATION );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_DATE_DE_TRAITEMENT );
        dto.setValeur( MARK_DATE_DE_TRAITEMENT );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_HEURE_DE_TRAITEMENT );
        dto.setValeur( MARK_HEURE_DE_TRAITEMENT );
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

        model.put( MARK_BALISES, balises );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_SUIVI_CONFIG, locale, model );
        return template.getHtml( );
    }

    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        NotificationSuiviTaskConfig config = new NotificationSuiviTaskConfig( );

        try
        {
            BeanUtils.populate( config, request.getParameterMap( ) );
            config.setIdTask( task.getId( ) );
        }

        catch ( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );

            Object[] tabError = { e.getMessage( ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_EXCEPTION_OCCURED, tabError, AdminMessage.TYPE_STOP );
        }

        // Gestion des erreurs
        if ( config.getSender( ).equals( StringUtils.EMPTY ) )
        {
            strError = ERROR_SENDER;
        } else if ( config.getSubject( ).equals( StringUtils.EMPTY ) )
        {
            strError = ERROR_SUBJECT;
        } else if ( config.getMobileSubject( ).equals( StringUtils.EMPTY ) )
        {
            strError = ERROR_MOBILE_SUBJECT;
        } else if ( config.getMailMessage( ).equals( StringUtils.EMPTY ) )
        {
            strError = ERROR_MAIL_MESSAGE;
        } else if ( config.getMobileMessage( ).equals( StringUtils.EMPTY ) )
        {
            strError = ERROR_MOBILE_MESSAGE;
        }
        if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        if ( _notificationSuiviTaskConfigDAO.load( task.getId( ), SignalementUtils.getPlugin( ) ).getIdTask( ) == 0 )
        {
            _notificationSuiviTaskConfigDAO.insert( config, SignalementUtils.getPlugin( ) );
        } else
        {
            _notificationSuiviTaskConfigDAO.store( config, SignalementUtils.getPlugin( ) );
        }

        return null;
    }

    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        NotificationSuiviValue notificationSuiviValue = _notificationSuiviValueService.findByPrimaryKey( nIdHistory, task.getId( ), null );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_NOTIFICATION_SUIVI, notificationSuiviValue );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_SUIVI_INFORMATION, locale, model );

        return template.getHtml( );
    }

    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

}
