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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.BaliseFreemarkerDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationUserMultiContentsValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.service.NotificationUserMultiContentsValueService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * The notification user 3 contents component
 */
public class NotificationUserMultiContentsComponent extends AbstractTaskComponent
{
    // CONSTANTS
    private static final String                                   DTO_NAME_HEURE_DE_TRAITEMENT                = "Heure de passage";
    private static final String                                   DTO_NAME_DATE_DE_TRAITEMENT                 = "Date de passage";

    // MARKERS
    private static final String                                   MARK_CONFIG                                 = "config";
    private static final String                                   MARK_WEBAPP_URL                             = "webapp_url";
    private static final String                                   MARK_LOCALE                                 = "locale";
    private static final String                                   MARK_BALISES                                = "balises";
    private static final String                                   MARK_NUMERO                                 = "numero";
    private static final String                                   MARK_TYPE                                   = "type";
    private static final String                                   MARK_ADRESSE                                = "adresse";
    private static final String                                   MARK_PRIORITE                               = "priorite";
    private static final String                                   MARK_COMMENTAIRE                            = "commentaire";
    private static final String                                   MARK_PRECISION                              = "precision";
    private static final String                                   MARK_LIEN_CONSULTATION                      = "lien_consultation";
    private static final String                                   MARK_DATE_PROGRAMMATION                     = "date_programmation";
    private static final String                                   MARK_NOTIFICATION_USER_VALUE                = "notification_user_value";
    private static final String                                   MARK_DATE_DE_TRAITEMENT                     = "datetraitement";
    private static final String                                   MARK_HEURE_DE_TRAITEMENT                    = "heuretraitement";
    private static final String                                   MARK_ISROADMAP                              = "isRoadMap";
    private static final String                                   MARK_HAS_EMAIL_SIGNALEUR                    = "has_email_signaleur";
    private static final String                                   MARK_FLAG_IS_SERVICE_FAIT                   = "fIsServiceFait";

    private static final String                                   MARK_ALIAS_ANOMALIE                         = "alias_anomalie";

    private static final String                                   MARK_ID_ANOMALIE                            = "id_anomalie";

    // PARAMETERS
    private static final String                                   PARAM_ISROADMAP                             = "isRoadMap";
    private static final String                                   PARAMETER_CHOSEN_MESSAGE                    = "chosenMessage";

    // MESSAGES
    private static final String                                   MESSAGE_MANDATORY_FIELD                     = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";
    private static final String                                   ERROR_SENDER                                = "module.workflow.dansmarue.task_notification_config.error.sender";
    private static final String                                   ERROR_SUBJECT                               = "module.workflow.dansmarue.task_notification_config.error.subject";
    private static final String                                   ERROR_TITLE                                 = "module.workflow.dansmarue.task_notification_config.error.title";
    private static final String                                   ERROR_MESSAGE                               = "module.workflow.dansmarue.task_notification_config.error.message";
    private static final String                                   MESSAGE_CHOSE_MESSAGE                       = "module.workflow.dansmarue.task_notification_config.3contents.servicefait.error.chosemessage";

    // TEMPLATES
    private static final String                                   TEMPLATE_TASK_NOTIFICATION_CONFIG           = "admin/plugins/workflow/modules/signalement/task_notification_signalement_user_3contents_config.html";
    private static final String                                   TEMPLATE_TASK_NOTIFICATION_USER_FORM        = "admin/plugins/workflow/modules/signalement/task_notification_signalement_user_3contents_form.html";
    private static final String                                   TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION = "admin/plugins/workflow/modules/signalement/task_notification_user_3contents_information.html";

    // SERVICES
    @Inject
    @Named( "signalement.notificationUserMultiContentsValueService" )
    private NotificationUserMultiContentsValueService             _notificationUserMultiContentsValueService;

    @Inject
    @Named( "signalement.notificationSignalementUserMultiContentsTaskConfigDAO" )
    private NotificationSignalementUserMultiContentsTaskConfigDAO _notificationSignalementUserMultiContentsTaskConfigDAO;

    @Inject
    @Named( "signalementService" )
    private ISignalementService                                   _signalementService;

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        boolean fIsServiceFait = false;

        // Récupère les messages de cette tâche
        List<NotificationSignalementUserMultiContentsTaskConfig> config = getNotificationUserMultiContentsMessages( task );

        // on recupere l'identifiant de l'action en cours
        String id_action = request.getParameter( "action_id" );
        String strRoadMap = request.getParameter( PARAM_ISROADMAP );

        // on defini si l'action est une declaration de service fait
        fIsServiceFait = StringUtils.isNotBlank( id_action )
                && ( id_action.equals( "22" ) || id_action.equals( "18" ) || id_action.equals( "41" ) || id_action.equals( "49" ) || id_action.equals( "53" ) || id_action.equals( "62" ) );

        Signalement signalement = _signalementService.getSignalement( nIdResource );

        for ( NotificationSignalementUserMultiContentsTaskConfig messageSf : config )
        {
            String message = prepareMessage( request, locale, signalement, messageSf.getMessage( ) );
            messageSf.setMessage( message.replaceAll( "<br/>|<br>|<br />|<p>", System.getProperty( "line.separator" ) ).replaceAll( "<[^>]*>", "" ) );
        }

        boolean hasEmailSignaleur = false;
        if ( null != signalement )
        {
            if ( CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) ) )
            {
                if ( !StringUtils.isBlank( signalement.getSignaleurs( ).get( 0 ).getMail( ) ) )
                {
                    hasEmailSignaleur = true;
                }
            }
        }
        model.put( MARK_HAS_EMAIL_SIGNALEUR, hasEmailSignaleur );

        model.put( MARK_CONFIG, config );
        model.put( MARK_FLAG_IS_SERVICE_FAIT, fIsServiceFait );
        model.put( MARK_ISROADMAP, Boolean.valueOf( strRoadMap ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_USER_FORM, locale, model );

        return template.getHtml( );
    }

    /**
     * Return the different messages linked to the selected task
     *
     * @param task
     *            the task
     * @return list of messages
     */
    private List<NotificationSignalementUserMultiContentsTaskConfig> getNotificationUserMultiContentsMessages( ITask task )
    {
        List<Long> listIdMessageTask = _notificationSignalementUserMultiContentsTaskConfigDAO.selectAllMessageTask( task.getId( ), SignalementUtils.getPlugin( ) );
        List<NotificationSignalementUserMultiContentsTaskConfig> config = new ArrayList<>( );

        for ( Long idMessage : listIdMessageTask )
        {
            config.add( _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( idMessage, SignalementUtils.getPlugin( ) ) );
        }
        return config;
    }

    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        List<NotificationSignalementUserMultiContentsTaskConfig> config = getNotificationUserMultiContentsMessages( task );

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
        dto.setNom( "Date de programmation de l'anomalie" );
        dto.setValeur( MARK_DATE_PROGRAMMATION );
        balises.add( dto );

        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Lien de consultation de l'anomalie" );
        dto.setValeur( MARK_LIEN_CONSULTATION );
        balises.add( dto );
        // on ajoute les batises pour la date et l'heure de traitement
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( DTO_NAME_DATE_DE_TRAITEMENT );
        dto.setValeur( MARK_DATE_DE_TRAITEMENT );
        balises.add( dto );

        dto = new BaliseFreemarkerDTO( );
        dto.setNom( DTO_NAME_HEURE_DE_TRAITEMENT );
        dto.setValeur( MARK_HEURE_DE_TRAITEMENT );
        balises.add( dto );
        model.put( MARK_BALISES, balises );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_CONFIG, locale, model );
        return template.getHtml( );
    }

    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        List<NotificationSignalementUserMultiContentsTaskConfig> conf = new ArrayList<>( );
        Integer idTask = task.getId( );
        String sender = request.getParameter( "sender" );
        String subject = request.getParameter( "subject" );
        String[] listIdMessage = request.getParameterValues( "idMessage" );
        String[] listTitle = request.getParameterValues( "title" );
        String[] listMessage = request.getParameterValues( "message" );

        int nIndex = listIdMessage.length;

        // Crée les messages à ajouter ou update
        for ( int i = 0; i < nIndex; i++ )
        {
            NotificationSignalementUserMultiContentsTaskConfig config = new NotificationSignalementUserMultiContentsTaskConfig( );
            config.setIdTask( idTask );
            config.setIdMessage( Long.parseLong( listIdMessage[i] ) );
            config.setSender( sender );
            config.setSubject( subject );
            config.setTitle( listTitle[i] );
            config.setMessage( listMessage[i] );

            conf.add( config );
        }

        for ( NotificationSignalementUserMultiContentsTaskConfig config : conf )
        {
            // Gestion des erreurs
            if ( StringUtils.EMPTY.equals( config.getSender( ) ) )
            {
                strError = ERROR_SENDER;
            } else if ( StringUtils.EMPTY.equals( config.getSubject( ) ) )
            {
                strError = ERROR_SUBJECT;
            } else if ( StringUtils.EMPTY.equals( config.getTitle( ) ) )
            {
                strError = ERROR_TITLE;
            } else if ( StringUtils.EMPTY.equals( config.getMessage( ) ) )
            {
                strError = ERROR_MESSAGE;
            }
            if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            if ( _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( config.getIdMessage( ), SignalementUtils.getPlugin( ) ) == null )
            {
                _notificationSignalementUserMultiContentsTaskConfigDAO.insert( config, task.getId( ), SignalementUtils.getPlugin( ) );
            } else
            {
                _notificationSignalementUserMultiContentsTaskConfigDAO.update( config, SignalementUtils.getPlugin( ) );
            }
        }

        return null;
    }

    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        NotificationUserMultiContentsValue notificationUserMultiContentsValue = _notificationUserMultiContentsValueService.findByPrimaryKey( nIdHistory, task.getId( ), null );

        Map<String, Object> model = new HashMap<String, Object>( );
        List<NotificationSignalementUserMultiContentsTaskConfig> config = getNotificationUserMultiContentsMessages( task );
        model.put( MARK_CONFIG, config );
        model.put( MARK_NOTIFICATION_USER_VALUE, notificationUserMultiContentsValue );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION, locale, model );

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
        String strRoadMap = request.getParameter( PARAM_ISROADMAP );
        if ( ( strRoadMap != null ) && Boolean.valueOf( strRoadMap ) )
        {
            return null;
        }

        // Si pas de mail, pas de contrôle
        Signalement signalement = _signalementService.getSignalement( nIdResource );
        boolean hasEmailSignaleur = false;
        if ( null != signalement )
        {
            hasEmailSignaleur = CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) ) && StringUtils.isNotBlank( signalement.getSignaleurs( ).get( 0 ).getMail( ) );
            if ( !hasEmailSignaleur )
            {
                return null;
            }
        }

        String chosenMessage = request.getParameter( PARAMETER_CHOSEN_MESSAGE );
        if ( StringUtils.isBlank( chosenMessage ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CHOSE_MESSAGE, AdminMessage.TYPE_STOP );
        }

        return null;
    }

    /**
     * Fills the message with variable values
     *
     * @param request
     *            the http request
     * @param locale
     *            the local
     * @param signalement
     *            the report
     * @param message
     *            the message
     * @return email message
     */
    private String prepareMessage( HttpServletRequest request, Locale locale, Signalement signalement, String message )
    {
        Map<String, Object> emailModel = new HashMap<String, Object>( );
        emailModel.put( MARK_ID_ANOMALIE, signalement.getId( ) );
        emailModel.put( MARK_NUMERO, signalement.getNumeroSignalement( ) );
        emailModel.put( MARK_TYPE, signalement.getType( ) );

        // Alias de l'anomalie
        String aliasType = signalement.getTypeSignalement( ).getAlias( );
        if ( null == aliasType )
        {
            aliasType = StringUtils.EMPTY;
        }
        emailModel.put( MARK_ALIAS_ANOMALIE, aliasType );

        emailModel.put( MARK_ADRESSE, signalement.getAdresses( ).get( 0 ).getAdresse( ) );
        if ( signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) != null )
        {
            emailModel.put( MARK_PRECISION, signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) );
        } else
        {
            emailModel.put( MARK_PRECISION, "" );
        }
        emailModel.put( MARK_PRIORITE, signalement.getPrioriteName( ) );
        emailModel.put( MARK_COMMENTAIRE, signalement.getCommentaire( ) );
        emailModel.put( MARK_LIEN_CONSULTATION, _signalementService.getLienConsultation( signalement, request ) );
        if ( StringUtils.isNotBlank( signalement.getDatePrevueTraitement( ) ) )
        {
            emailModel.put( MARK_DATE_PROGRAMMATION, signalement.getDatePrevueTraitement( ) );
        } else
        {
            emailModel.put( MARK_DATE_PROGRAMMATION, StringUtils.EMPTY );
        }
        String dateDeTraitement = signalement.getDateServiceFaitTraitement( );

        if ( StringUtils.isNotBlank( dateDeTraitement ) )
        {
            emailModel.put( MARK_DATE_DE_TRAITEMENT, dateDeTraitement );
        } else
        {
            emailModel.put( MARK_DATE_DE_TRAITEMENT, StringUtils.EMPTY );
        }
        String heureDeTraitement = signalement.getHeureServiceFaitTraitement( );
        if ( StringUtils.isNotBlank( heureDeTraitement ) )
        {
            heureDeTraitement = heureDeTraitement.substring( 0, 2 ) + ":" + heureDeTraitement.substring( 2 );
        } else
        {
            heureDeTraitement = StringUtils.EMPTY;
        }
        emailModel.put( MARK_HEURE_DE_TRAITEMENT, heureDeTraitement );
        String messageHtml = "";
        messageHtml = AppTemplateService.getTemplateFromStringFtl( message, locale, emailModel ).getHtml( );
        return messageHtml;
    }

}
