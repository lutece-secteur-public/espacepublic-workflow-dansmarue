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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.utils.ISignalementUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologie;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.IMessageTypologieService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.TaskUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.BaliseFreemarkerDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationUserMultiContentsValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.service.NotificationUserMultiContentsValueService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * The notification user 3 contents component.
 */
public class NotificationUserMultiContentsComponent extends AbstractTaskComponent
{

    /** The Constant DTO_NAME_HEURE_DE_TRAITEMENT. */
    // CONSTANTS
    private static final String DTO_NAME_HEURE_DE_TRAITEMENT = "Heure de passage";

    /** The Constant DTO_NAME_DATE_DE_TRAITEMENT. */
    private static final String DTO_NAME_DATE_DE_TRAITEMENT = "Date de passage";

    /** The Constant LINE_SEPARATOR. */
    private static final String LINE_SEPARATOR = "line.separator";

    /** The Constant BALISE_BR. */
    private static final String BALISE_BR = "<br/>|<br>|<br />|<p>";

    /** The Constant BALISE. */
    private static final String BALISE = "<[^>]*>";

    /** The Constant MARK_CONFIG. */
    // MARKERS
    private static final String MARK_CONFIG = "config";

    /** The Constant MARK_MESSAGES_TYPOLOGIE. */
    private static final String MARK_MESSAGES_TYPOLOGIE = "message_typologie";

    /** The Constant MARK_WEBAPP_URL. */
    private static final String MARK_WEBAPP_URL = "webapp_url";

    /** The Constant MARK_LOCALE. */
    private static final String MARK_LOCALE = "locale";

    /** The Constant MARK_BALISES. */
    private static final String MARK_BALISES = "balises";

    /** The Constant MARK_NUMERO. */
    private static final String MARK_NUMERO = "numero";

    /** The Constant MARK_ID_TYPE. */
    private static final String MARK_ID_TYPE = "id_type";

    /** The Constant MARK_TYPE. */
    private static final String MARK_TYPE = "type";

    /** The Constant MARK_ADRESSE. */
    private static final String MARK_ADRESSE = "adresse";

    /** The Constant MARK_PRIORITE. */
    private static final String MARK_PRIORITE = "priorite";

    /** The Constant MARK_COMMENTAIRE. */
    private static final String MARK_COMMENTAIRE = "commentaire";

    /** The Constant MARK_PRECISION. */
    private static final String MARK_PRECISION = "precision";

    /** The Constant MARK_LIEN_CONSULTATION. */
    private static final String MARK_LIEN_CONSULTATION = "lien_consultation";

    /** The Constant MARK_DATE_PROGRAMMATION. */
    private static final String MARK_DATE_PROGRAMMATION = "date_programmation";

    /** The Constant MARK_NOTIFICATION_USER_VALUE. */
    private static final String MARK_NOTIFICATION_USER_VALUE = "notification_user_value";

    /** The Constant MARK_DATE_DE_TRAITEMENT. */
    private static final String MARK_DATE_DE_TRAITEMENT = "datetraitement";

    /** The Constant MARK_HEURE_DE_TRAITEMENT. */
    private static final String MARK_HEURE_DE_TRAITEMENT = "heuretraitement";

    /** The Constant MARK_ISROADMAP. */
    private static final String MARK_ISROADMAP = "isRoadMap";

    /** The Constant MARK_HAS_EMAIL_SIGNALEUR. */
    private static final String MARK_HAS_EMAIL_SIGNALEUR = "has_email_signaleur";

    /** The Constant MARK_FLAG_IS_SERVICE_FAIT. */
    private static final String MARK_FLAG_IS_SERVICE_FAIT = "fIsServiceFait";
    private static final String MARK_CP = "code_postal";
    private static final String MARK_ID_TYPO_LVL_1 = "id_typologie_lvl_1";

    /** The Constant MARK_ALIAS_ANOMALIE. */
    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    /** The Constant MARK_ID_ANOMALIE. */
    private static final String MARK_ID_ANOMALIE = "id_anomalie";

    private static final String MARK_URL_SONDAGE_DEMANDE = "urlSondageDemande";

    private static final String MARK_URL_SONDAGE_SERVICE = "urlSondageService";

    /** The Constant PARAM_ISROADMAP. */
    // PARAMETERS
    private static final String PARAM_ISROADMAP = "isRoadMap";

    /** The Constant PARAMETER_CHOSEN_MESSAGE. */
    private static final String PARAMETER_CHOSEN_MESSAGE = "chosenMessage";

    /** The Constant MESSAGE_MANDATORY_FIELD. */
    // MESSAGES
    private static final String MESSAGE_MANDATORY_FIELD = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";

    /** The Constant ERROR_SENDER. */
    private static final String ERROR_SENDER = "module.workflow.dansmarue.task_notification_config.error.sender";

    /** The Constant ERROR_SUBJECT. */
    private static final String ERROR_SUBJECT = "module.workflow.dansmarue.task_notification_config.error.subject";

    /** The Constant ERROR_TITLE. */
    private static final String ERROR_TITLE = "module.workflow.dansmarue.task_notification_config.error.title";

    /** The Constant ERROR_MESSAGE. */
    private static final String ERROR_MESSAGE = "module.workflow.dansmarue.task_notification_config.error.message";

    /** The Constant MESSAGE_CHOSE_MESSAGE. */
    private static final String MESSAGE_CHOSE_MESSAGE = "module.workflow.dansmarue.task_notification_config.3contents.servicefait.error.chosemessage";

    /** The Constant TEMPLATE_TASK_NOTIFICATION_CONFIG. */
    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFICATION_CONFIG = "admin/plugins/workflow/modules/signalement/task_notification_signalement_user_3contents_config.html";

    /** The Constant TEMPLATE_TASK_NOTIFICATION_USER_FORM. */
    private static final String TEMPLATE_TASK_NOTIFICATION_USER_FORM = "admin/plugins/workflow/modules/signalement/task_notification_signalement_user_3contents_form.html";

    /** The Constant TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION. */
    private static final String TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION = "admin/plugins/workflow/modules/signalement/task_notification_user_3contents_information.html";

    private static final String ENTETE_HEADER = "sitelabels.site_property.message.typologie.entete.htmlblock";

    private static final String ENTETE_FOOTER = "sitelabels.site_property.message.typologie.pieddepage.htmlblock";

    private static final String URL_SONDAGE_DEMANDE = "sitelabels.site_property.message.url.sondage.demande";

    private static final String URL_SONDAGE_SERVICE = "sitelabels.site_property.message.url.sondage.sevice";

    private static final String DTO_ARRONDISSEMENT = "arrondissement";
    private static final String MARK_ARRONDISSEMENT = "arrondissement";

    /** The notification user multi contents value service. */
    // SERVICES
    @Inject
    @Named( "signalement.notificationUserMultiContentsValueService" )
    private NotificationUserMultiContentsValueService _notificationUserMultiContentsValueService;

    /** The notification signalement user multi contents task config DAO. */
    @Inject
    @Named( "signalement.notificationSignalementUserMultiContentsTaskConfigDAO" )
    private NotificationSignalementUserMultiContentsTaskConfigDAO _notificationSignalementUserMultiContentsTaskConfigDAO;

    /** The signalement service. */
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /** The message typologie service. */
    @Inject
    @Named( "messageTypologieService" )
    private IMessageTypologieService _messageTypologieService;

    /** The signalement utils */
    //UTILS
    @Inject
    @Named( "signalement.signalementUtils" )
    private ISignalementUtils _signalementUtils;

    /**
     * Gets the display task form.
     *
     * @param nIdResource
     *            the n id resource
     * @param strResourceType
     *            the str resource type
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the display task form
     */
    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<>( );

        boolean fIsServiceFait = false;

        // Récupère les messages de cette tâche
        List<NotificationSignalementUserMultiContentsTaskConfig> config = getNotificationUserMultiContentsMessages( task );

        // on recupere l'identifiant de l'action en cours
        String idAction = request.getParameter( "action_id" );
        String strRoadMap = request.getParameter( PARAM_ISROADMAP );

        // on defini si l'action est une declaration de service fait
        fIsServiceFait = StringUtils.isNotBlank( idAction ) && ( idAction.equals( "22" ) || idAction.equals( "18" ) || idAction.equals( "41" )
                || idAction.equals( "49" ) || idAction.equals( "53" ) || idAction.equals( "62" ) );

        Signalement signalement = _signalementService.getSignalement( nIdResource );

        for ( NotificationSignalementUserMultiContentsTaskConfig messageSf : config )
        {
            String message = prepareMessage( request, locale, signalement, messageSf.getMessage( ) );
            messageSf.setMessage( message.replaceAll( BALISE_BR, System.getProperty( LINE_SEPARATOR ) ).replaceAll( BALISE, "" ) );
        }

        boolean hasEmailSignaleur = ( null != signalement ) && CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) )
                && !StringUtils.isBlank( signalement.getSignaleurs( ).get( 0 ).getMail( ) );

        model.put( MARK_HAS_EMAIL_SIGNALEUR, hasEmailSignaleur );

        if ( ( signalement != null ) && ( signalement.getTypeSignalement( ) != null ) )
        {
            List<MessageTypologie> listMessageTypologie = _messageTypologieService.loadAllMessageActifByIdType( signalement.getTypeSignalement( ).getId( ) );
            for ( MessageTypologie messageTypologie : listMessageTypologie )
            {
                messageTypologie.setContenuMessage( getFullMessage( messageTypologie ) );
                String message = prepareMessage( request, locale, signalement, messageTypologie.getContenuMessage( ) );
                messageTypologie.setContenuMessage( message.replaceAll( BALISE_BR, System.getProperty( LINE_SEPARATOR ) ).replaceAll( BALISE, "" ) );
            }
            model.put( MARK_MESSAGES_TYPOLOGIE, listMessageTypologie );
        }
        model.put( MARK_CONFIG, config );
        model.put( MARK_FLAG_IS_SERVICE_FAIT, fIsServiceFait );
        model.put( MARK_ISROADMAP, Boolean.valueOf( strRoadMap ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_USER_FORM, locale, model );

        return template.getHtml( );
    }

    private String getFullMessage( MessageTypologie messageTypologie )
    {
        String headerMessage = DatastoreService.getDataValue( ENTETE_HEADER, "" );
        String contentMessage = messageTypologie.getContenuMessage( );
        String footerMessage = DatastoreService.getDataValue( ENTETE_FOOTER, "" );

        String fullMessage = headerMessage + contentMessage + footerMessage;

        fullMessage = fullMessage.replaceAll( BALISE_BR, System.getProperty( LINE_SEPARATOR ) );

        return fullMessage;
    }

    /**
     * Return the different messages linked to the selected task.
     *
     * @param task
     *            the task
     * @return list of messages
     */
    private List<NotificationSignalementUserMultiContentsTaskConfig> getNotificationUserMultiContentsMessages( ITask task )
    {
        List<Long> listIdMessageTask = _notificationSignalementUserMultiContentsTaskConfigDAO.selectAllMessageTask( task.getId( ),
                _signalementUtils.getPlugin( ) );
        List<NotificationSignalementUserMultiContentsTaskConfig> config = new ArrayList<>( );

        for ( Long idMessage : listIdMessageTask )
        {
            config.add( _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( idMessage, _signalementUtils.getPlugin( ) ) );
        }
        return config;
    }

    /**
     * Gets the display config form.
     *
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the display config form
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        List<NotificationSignalementUserMultiContentsTaskConfig> config = getNotificationUserMultiContentsMessages( task );

        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_CONFIG, config );
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
        dto.setNom( "Id du Type de l'anomalie" );
        dto.setValeur( MARK_ID_TYPE );
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
        dto.setNom( "Url de sondage de demande" );
        dto.setValeur( MARK_URL_SONDAGE_DEMANDE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Url de sondage de service" );
        dto.setValeur( MARK_URL_SONDAGE_SERVICE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "code postal" );
        dto.setValeur( MARK_CP );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "ID de la catégorie de niveau 1" );
        dto.setValeur( MARK_ID_TYPO_LVL_1 );

        dto = new BaliseFreemarkerDTO( );
        dto.setNom( DTO_NAME_HEURE_DE_TRAITEMENT );
        dto.setValeur( MARK_HEURE_DE_TRAITEMENT );
        balises.add( dto );

        dto = new BaliseFreemarkerDTO( );
        dto.setNom( DTO_ARRONDISSEMENT );
        dto.setValeur( MARK_ARRONDISSEMENT );
        balises.add( dto );

        model.put( MARK_BALISES, balises );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_CONFIG, locale, model );
        return template.getHtml( );
    }

    /**
     * Do save config.
     *
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the string
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        List<NotificationSignalementUserMultiContentsTaskConfig> conf = new ArrayList<>( );
        Integer idTask = task.getId( );
        String sender = request.getParameter( "sender" );
        String subject = request.getParameter( "subject" );
        String [ ] listIdMessage = request.getParameterValues( "idMessage" );
        String [ ] listTitle = request.getParameterValues( "title" );
        String [ ] listMessage = request.getParameterValues( "message" );

        int nIndex = listIdMessage.length;

        // Crée les messages à ajouter ou update
        for ( int i = 0; i < nIndex; i++ )
        {
            NotificationSignalementUserMultiContentsTaskConfig config = new NotificationSignalementUserMultiContentsTaskConfig( );
            config.setIdTask( idTask );
            config.setIdMessage( Long.parseLong( listIdMessage [i] ) );
            config.setSender( sender );
            config.setSubject( subject );
            config.setTitle( listTitle [i] );
            config.setMessage( listMessage [i] );

            conf.add( config );
        }

        for ( NotificationSignalementUserMultiContentsTaskConfig config : conf )
        {
            // Gestion des erreurs
            if ( StringUtils.EMPTY.equals( config.getSender( ) ) )
            {
                strError = ERROR_SENDER;
            }
            else
                if ( StringUtils.EMPTY.equals( config.getSubject( ) ) )
                {
                    strError = ERROR_SUBJECT;
                }
                else
                    if ( StringUtils.EMPTY.equals( config.getTitle( ) ) )
                    {
                        strError = ERROR_TITLE;
                    }
                    else
                        if ( StringUtils.EMPTY.equals( config.getMessage( ) ) )
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

            if ( _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( config.getIdMessage( ), _signalementUtils.getPlugin( ) ) == null )
            {
                _notificationSignalementUserMultiContentsTaskConfigDAO.insert( config, task.getId( ), _signalementUtils.getPlugin( ) );
            }
            else
            {
                _notificationSignalementUserMultiContentsTaskConfigDAO.update( config, _signalementUtils.getPlugin( ) );
            }
        }

        return null;
    }

    /**
     * Gets the display task information.
     *
     * @param nIdHistory
     *            the n id history
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the display task information
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        NotificationUserMultiContentsValue notificationUserMultiContentsValue = _notificationUserMultiContentsValueService.findByPrimaryKey( nIdHistory,
                task.getId( ), null );

        Map<String, Object> model = new HashMap<>( );
        List<NotificationSignalementUserMultiContentsTaskConfig> config = getNotificationUserMultiContentsMessages( task );
        model.put( MARK_CONFIG, config );
        model.put( MARK_NOTIFICATION_USER_VALUE, notificationUserMultiContentsValue );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION, locale, model );

        return template.getHtml( );
    }

    /**
     * Do validate task.
     *
     * @param nIdResource
     *            the n id resource
     * @param strResourceType
     *            the str resource type
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the string
     */
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
            hasEmailSignaleur = CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) )
                    && StringUtils.isNotBlank( signalement.getSignaleurs( ).get( 0 ).getMail( ) );
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
     * Fills the message with variable values.
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
        Map<String, Object> emailModel = new HashMap<>( );
        emailModel.put( MARK_ID_ANOMALIE, signalement.getId( ) );
        emailModel.put( MARK_NUMERO, signalement.getNumeroSignalement( ) );
        emailModel.put( MARK_ID_TYPE, signalement.getTypeSignalement( ).getId( ) );
        emailModel.put( MARK_TYPE, signalement.getType( ) );

        // Alias de l'anomalie
        emailModel.put( MARK_ALIAS_ANOMALIE, checkEmptyNull( signalement.getTypeSignalement( ).getAlias( ) ) );


        emailModel.put( MARK_ADRESSE, signalement.getAdresses( ).get( 0 ).getAdresse( ) );

        emailModel.put( MARK_PRECISION, checkEmptyNull(signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) ));

        emailModel.put( MARK_PRIORITE, signalement.getPrioriteName( ) );
        emailModel.put( MARK_COMMENTAIRE, signalement.getCommentaire( ) );
        emailModel.put( MARK_LIEN_CONSULTATION, _signalementService.getLienConsultation( signalement, request ) );

        emailModel.put( MARK_DATE_PROGRAMMATION, checkEmptyNull(signalement.getDatePrevueTraitement( ) ));
        emailModel.put( MARK_DATE_DE_TRAITEMENT, checkEmptyNull( signalement.getDateServiceFaitTraitement( ) ) );

        String heureDeTraitement = signalement.getHeureServiceFaitTraitement( );
        if ( StringUtils.isNotBlank( heureDeTraitement ) )
        {
            heureDeTraitement = heureDeTraitement.substring( 0, 2 ) + ":" + heureDeTraitement.substring( 2 );
        }
        else
        {
            heureDeTraitement = StringUtils.EMPTY;
        }
        emailModel.put( MARK_HEURE_DE_TRAITEMENT, heureDeTraitement );

        emailModel.put( MARK_URL_SONDAGE_DEMANDE, DatastoreService.getDataValue( URL_SONDAGE_DEMANDE, "" ) );
        emailModel.put( MARK_URL_SONDAGE_SERVICE, DatastoreService.getDataValue( URL_SONDAGE_SERVICE, "" ) );

        if ( ( signalement.getAdresses( ) != null ) && ( signalement.getAdresses( ).get( 0 ) != null )
                && ( signalement.getAdresses( ).get( 0 ).getAdresse( ) != null ) )
        {
            emailModel.put( MARK_CP, TaskUtils.getCPFromAdresse( signalement.getAdresses( ).get( 0 ).getAdresse( ) ) );
        }
        else
        {
            emailModel.put( MARK_CP, StringUtils.EMPTY );
        }

        int idTypeAnoLvl1 = TaskUtils.getIdTypeAnoLvl1( signalement.getTypeSignalement( ) );
        if ( idTypeAnoLvl1 > -1 )
        {
            emailModel.put( MARK_ID_TYPO_LVL_1, idTypeAnoLvl1 );
        }
        else
        {
            emailModel.put( MARK_ID_TYPO_LVL_1, StringUtils.EMPTY );
        }

        String urlSondageDemande = DatastoreService.getDataValue( URL_SONDAGE_DEMANDE, "" );
        String urlSondageService = DatastoreService.getDataValue( URL_SONDAGE_SERVICE, "" );
        emailModel.put( MARK_URL_SONDAGE_DEMANDE, urlSondageDemande );
        emailModel.put( MARK_URL_SONDAGE_SERVICE, urlSondageService );

        if ( ( signalement.getArrondissement( ) != null ) && ( signalement.getArrondissement( ).getId( ) != null ) )
        {
            emailModel.put( MARK_ARRONDISSEMENT, signalement.getArrondissement( ).getId( ) );
        }
        else
        {
            emailModel.put( MARK_ARRONDISSEMENT, "" );
        }

        String messageHtml = "";
        messageHtml = AppTemplateService.getTemplateFromStringFtl( message, locale, emailModel ).getHtml( );
        return messageHtml;
    }


    /**
     * checkEmptyNull.
     * @param element
     * @return
     */
    private String checkEmptyNull(String element) {
        if (StringUtils.isBlank( element )) {
            return StringUtils.EMPTY;
        } else {
            return element;
        }

    }

}
