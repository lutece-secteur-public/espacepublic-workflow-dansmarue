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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.FileMessageCreationService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.BaliseFreemarkerDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationSignalementUserTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationSignalementUserTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationUserValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.service.NotificationUserValueService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * The notification user component
 */
public class NotificationUserComponent extends AbstractTaskComponent
{
    // PROPERTIES
    private static final String                      PROPERTY_BASE_TS_URL                        = "lutece.ts.prod.url";

    // JSP
    private static final String                      JSP_PORTAL                                  = "jsp/site/Portal.jsp?instance=signalement";

    // MARKERS
    private static final String                      MARK_CONFIG                                 = "config";
    private static final String                      MARK_CONFIG_NOTIFICATION_USER               = "config_notification_user";
    private static final String                      MARK_WEBAPP_URL                             = "webapp_url";
    private static final String                      MARK_LOCALE                                 = "locale";
    private static final String                      MARK_BALISES                                = "balises";

    private static final String                      MARK_NUMERO                                 = "numero";
    private static final String                      MARK_TYPE                                   = "type";
    private static final String                      MARK_ADRESSE                                = "adresse";
    private static final String                      MARK_PRIORITE                               = "priorite";
    private static final String                      MARK_COMMENTAIRE                            = "commentaire";
    private static final String                      MARK_PRECISION                              = "precision";
    private static final String                      MARK_NOTIFICATION_USER_VALUE                = "notification_user_value";
    private static final String                      MARK_LIEN_CONSULTATION                      = "lien_consultation";

    private static final String                      MARK_DATE_PROGRAMMATION                     = "date_programmation";
    private static final String                      MARK_DATE_DE_TRAITEMENT                     = "datetraitement";
    private static final String                      MARK_HEURE_DE_TRAITEMENT                    = "heuretraitement";
    private static final String                      MARK_HAS_EMAIL_SIGNALEUR                    = "has_email_signaleur";

    private static final String                      MARK_DATE_ENVOI                             = "dateEnvoi";
    private static final String                      MARK_HEURE_ENVOI                            = "heureEnvoi";

    private static final String                      MARK_ALIAS_ANOMALIE                         = "alias_anomalie";

    private static final String                      MARK_ID_ANOMALIE                            = "id_anomalie";

    // MESSAGES
    private static final String                      MESSAGE_EXCEPTION_OCCURED                   = "module.workflow.dansmarue.task_notification_config.message.exception";
    private static final String                      MESSAGE_MANDATORY_FIELD                     = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";
    private static final String                      ERROR_SENDER                                = "module.workflow.dansmarue.task_notification_config.error.sender";
    private static final String                      ERROR_SUBJECT                               = "module.workflow.dansmarue.task_notification_config.error.subject";
    private static final String                      ERROR_MESSAGE                               = "module.workflow.dansmarue.task_notification_config.error.message";

    // TEMPLATES
    private static final String                      TEMPLATE_TASK_NOTIFICATION_CONFIG           = "admin/plugins/workflow/modules/signalement/task_notification_signalement_user_config.html";
    private static final String                      TEMPLATE_TASK_NOTIFICATION_FORM             = "admin/plugins/workflow/modules/signalement/task_notification_signalement_user_form.html";
    private static final String                      TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION = "admin/plugins/workflow/modules/signalement/task_notification_user_information.html";

    // PARAMETERS
    private static final String                      PARAMETER_PAGE                              = "page";
    private static final String                      PARAMETER_SUIVI                             = "suivi";
    private static final String                      PARAMETER_TOKEN                             = "token";

    // SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService                      _signalementService;

    @Inject
    @Named( "signalement.notificationUserValueService" )
    private NotificationUserValueService             _notificationUserValueService;

    @Inject
    @Named( "fileMessageCreationService" )
    private FileMessageCreationService               _fileMessageCreationService;

    @Inject
    @Named( "signalement.notificationSignalementUserTaskConfigDAO" )
    private NotificationSignalementUserTaskConfigDAO _notificationSignalementUserTaskConfigDAO;

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        NotificationSignalementUserTaskConfig config = _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( task.getId( ), SignalementUtils.getPlugin( ) );

        Signalement signalement = _signalementService.getSignalement( nIdResource );
        // Récupération du message
        String message = config.getMessage( );

        // Vérification si un des signaleurs a son mail de renseigné
        boolean hasEmailSignaleur = false;
        if ( null != signalement )
        {
            // Récupération des mails signaleurs - selon la règle métier, il ne peut y avoir qu'un seul signaleur dans la liste getSignaleur
            if ( CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) ) )
            {
                if ( !StringUtils.isBlank( signalement.getSignaleurs( ).get( 0 ).getMail( ) ) )
                {
                    hasEmailSignaleur = true;
                }
            }

            // Obtention des informations de l'email par remplacement des eventuelles balises freemarkers
            // ==> ajout des données pouvant être demandées ( correspondant à la map "balises" dans getDisplayConfigForm(...) )
            Map<String, Object> emailModel = new HashMap<String, Object>( );
            emailModel.put( MARK_ID_ANOMALIE, nIdResource );
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
            emailModel.put( MARK_LIEN_CONSULTATION, getLienConsultation( signalement) );

            emailModel.put( MARK_DATE_PROGRAMMATION, signalement.getDatePrevueTraitement( ) );
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

            // Date d'envoi
            String dateEnvoi = signalement.getDateCreation( );
            if ( StringUtils.isNotBlank( dateEnvoi ) )
            {
                emailModel.put( MARK_DATE_ENVOI, dateEnvoi );
            } else
            {
                emailModel.put( MARK_DATE_ENVOI, StringUtils.EMPTY );
            }

            // Heure d'envoi
            Date heureEnvoiTmstp = signalement.getHeureCreation( );
            if ( null != heureEnvoiTmstp )
            {
                emailModel.put( MARK_HEURE_ENVOI, DateUtils.getHourWithSecondsFr( heureEnvoiTmstp ) );
            } else
            {
                emailModel.put( MARK_HEURE_ENVOI, StringUtils.EMPTY );
            }

            // Application pré filtre sur les variables
            message = AppTemplateService.getTemplateFromStringFtl( message, locale, emailModel ).getHtml( );
        }

        model.put( MARK_HAS_EMAIL_SIGNALEUR, hasEmailSignaleur );

        config.setMessage( message.replaceAll( "<br/>|<br>|<br />|<p>", System.getProperty( "line.separator" ) ).replaceAll( "<[^>]*>", "" ) );
        model.put( MARK_CONFIG_NOTIFICATION_USER, config );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_FORM, locale, model );

        return template.getHtml( );
    }

    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        NotificationSignalementUserTaskConfig config = _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( task.getId( ), SignalementUtils.getPlugin( ) );

        String strMessage = _signalementService.loadMessageCreationSignalement( );
        if ( ( ( config != null ) && ( config.getMessage( ) == null ) ) && !strMessage.equals( StringUtils.EMPTY ) )
        {
            config.setMessage( strMessage );
        }

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
        dto.setNom( "Heure d'envoi" );
        dto.setValeur( MARK_HEURE_ENVOI );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Date d'envoi" );
        dto.setValeur( MARK_DATE_ENVOI );
        balises.add( dto );

        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Lien de consultation du message" );
        dto.setValeur( MARK_LIEN_CONSULTATION );
        balises.add( dto );
        model.put( MARK_BALISES, balises );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_CONFIG, locale, model );
        return template.getHtml( );
    }

    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        NotificationSignalementUserTaskConfig config = new NotificationSignalementUserTaskConfig( );

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
        } else if ( config.getMessage( ).equals( StringUtils.EMPTY ) )
        {
            strError = ERROR_MESSAGE;
        }
        if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        if ( _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( task.getId( ), SignalementUtils.getPlugin( ) ).getIdTask( ) == 0 )
        {
            _notificationSignalementUserTaskConfigDAO.insert( config, SignalementUtils.getPlugin( ) );
        } else
        {
            _notificationSignalementUserTaskConfigDAO.update( config, SignalementUtils.getPlugin( ) );
        }

        return null;
    }

    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        NotificationUserValue notificationUserValue = _notificationUserValueService.findByPrimaryKey( nIdHistory, task.getId( ), null );

        Map<String, Object> model = new HashMap<String, Object>( );
        NotificationSignalementUserTaskConfig config = _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( task.getId( ), null );
        model.put( MARK_CONFIG, config );
        model.put( MARK_NOTIFICATION_USER_VALUE, notificationUserValue );

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
        return null;
    }

    /**
     * Get the link of the "consultation page" (front office signalement)
     *
     * @param signalement
     *            the report
     *
     * @return the url
     */
    private String getLienConsultation( Signalement signalement )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( AppPropertiesService.getProperty( PROPERTY_BASE_TS_URL ) + JSP_PORTAL );

        urlItem.addParameter( PARAMETER_PAGE, PARAMETER_SUIVI );
        urlItem.addParameter( PARAMETER_TOKEN, signalement.getToken( ) );

        return urlItem.getUrl( );
    }

}
