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
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.TaskUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.BaliseFreemarkerDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationSignalementUserTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationSignalementUserTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationUserValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.service.NotificationUserValueService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.WorkflowSignalementConstants;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
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
 * The notification user component.
 */
public class NotificationUserComponent extends AbstractTaskComponent
{

    /** The Constant PROPERTY_BASE_TS_URL. */
    // PROPERTIES
    private static final String PROPERTY_BASE_TS_URL = "lutece.ts.prod.url";

    /** The Constant JSP_PORTAL. */
    // JSP
    private static final String JSP_PORTAL = "jsp/site/Portal.jsp?instance=signalement";

    /** The Constant MARK_CONFIG. */
    // MARKERS
    private static final String MARK_CONFIG = "config";

    /** The Constant MARK_CONFIG_NOTIFICATION_USER. */
    private static final String MARK_CONFIG_NOTIFICATION_USER = "config_notification_user";

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

    /** The Constant MARK_NOTIFICATION_USER_VALUE. */
    private static final String MARK_NOTIFICATION_USER_VALUE = "notification_user_value";

    /** The Constant MARK_LIEN_CONSULTATION. */
    private static final String MARK_LIEN_CONSULTATION = "lien_consultation";

    /** The Constant MARK_DATE_PROGRAMMATION. */
    private static final String MARK_DATE_PROGRAMMATION = "date_programmation";

    /** The Constant MARK_DATE_DE_TRAITEMENT. */
    private static final String MARK_DATE_DE_TRAITEMENT = "datetraitement";

    /** The Constant MARK_HEURE_DE_TRAITEMENT. */
    private static final String MARK_HEURE_DE_TRAITEMENT = "heuretraitement";

    /** The Constant MARK_HAS_EMAIL_SIGNALEUR. */
    private static final String MARK_HAS_EMAIL_SIGNALEUR = "has_email_signaleur";

    /** The Constant MARK_DATE_ENVOI. */
    private static final String MARK_DATE_ENVOI = "dateEnvoi";

    /** The Constant MARK_HEURE_ENVOI. */
    private static final String MARK_HEURE_ENVOI = "heureEnvoi";
    private static final String MARK_URL_SONDAGE_DEMANDE = "urlSondageDemande";
    private static final String MARK_URL_SONDAGE_SERVICE = "urlSondageService";
    private static final String URL_SONDAGE_DEMANDE = "sitelabels.site_property.message.url.sondage.demande";
    private static final String URL_SONDAGE_SERVICE = "sitelabels.site_property.message.url.sondage.sevice";
    private static final String MARK_CP = "code_postal";
    private static final String MARK_ID_TYPO_LVL_1 = "id_typologie_lvl_1";
    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    /** The Constant MARK_ID_ANOMALIE. */
    private static final String MARK_ID_ANOMALIE = "id_anomalie";

    /** The Constant MESSAGE_EXCEPTION_OCCURED. */
    // MESSAGES
    private static final String MESSAGE_EXCEPTION_OCCURED = "module.workflow.dansmarue.task_notification_config.message.exception";

    /** The Constant MESSAGE_MANDATORY_FIELD. */
    private static final String MESSAGE_MANDATORY_FIELD = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";

    /** The Constant ERROR_SENDER. */
    private static final String ERROR_SENDER = "module.workflow.dansmarue.task_notification_config.error.sender";

    /** The Constant ERROR_SUBJECT. */
    private static final String ERROR_SUBJECT = "module.workflow.dansmarue.task_notification_config.error.subject";

    /** The Constant ERROR_MESSAGE. */
    private static final String ERROR_MESSAGE = "module.workflow.dansmarue.task_notification_config.error.message";

    /** The Constant TEMPLATE_TASK_NOTIFICATION_CONFIG. */
    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFICATION_CONFIG = "admin/plugins/workflow/modules/signalement/task_notification_signalement_user_config.html";

    /** The Constant TEMPLATE_TASK_NOTIFICATION_FORM. */
    private static final String TEMPLATE_TASK_NOTIFICATION_FORM = "admin/plugins/workflow/modules/signalement/task_notification_signalement_user_form.html";

    /** The Constant TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION. */
    private static final String TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION = "admin/plugins/workflow/modules/signalement/task_notification_user_information.html";

    /** The Constant PARAMETER_PAGE. */
    // PARAMETERS
    private static final String PARAMETER_PAGE = "page";

    /** The Constant PARAMETER_SUIVI. */
    private static final String PARAMETER_SUIVI = "suivi";

    /** The Constant PARAMETER_TOKEN. */
    private static final String PARAMETER_TOKEN = "token";

    private static final String DTO_ARRONDISSEMENT = "arrondissement";
    private static final String MARK_ARRONDISSEMENT = "arrondissement";
    /** The signalement service. */
    // SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /** The notification user value service. */
    @Inject
    @Named( "signalement.notificationUserValueService" )
    private NotificationUserValueService _notificationUserValueService;

    /** The file message creation service. */
    @Inject
    @Named( "fileMessageCreationService" )
    private FileMessageCreationService _fileMessageCreationService;

    /** The notification signalement user task config DAO. */
    @Inject
    @Named( "signalement.notificationSignalementUserTaskConfigDAO" )
    private NotificationSignalementUserTaskConfigDAO _notificationSignalementUserTaskConfigDAO;

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
        NotificationSignalementUserTaskConfig config = _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( task.getId( ),
                SignalementUtils.getPlugin( ) );

        Signalement signalement = _signalementService.getSignalement( nIdResource );
        // Récupération du message
        String message = config.getMessage( );

        // Vérification si un des signaleurs a son mail de renseigné
        boolean hasEmailSignaleur = false;
        if ( null != signalement )
        {
            // Récupération des mails signaleurs - selon la règle métier, il ne peut y avoir qu'un seul signaleur dans la liste getSignaleur
            hasEmailSignaleur = CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) )
                    && !StringUtils.isBlank( signalement.getSignaleurs( ).get( 0 ).getMail( ) );

            // Obtention des informations de l'email par remplacement des eventuelles balises freemarkers
            // ==> ajout des données pouvant être demandées ( correspondant à la map "balises" dans getDisplayConfigForm(...) )
            Map<String, Object> emailModel = new HashMap<>( );
            emailModel.put( MARK_ID_ANOMALIE, nIdResource );
            emailModel.put( MARK_NUMERO, signalement.getNumeroSignalement( ) );
            emailModel.put( MARK_ID_TYPE, signalement.getTypeSignalement( ).getId( ) );
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
            }
            else
            {
                emailModel.put( MARK_PRECISION, "" );
            }
            emailModel.put( MARK_PRIORITE, signalement.getPrioriteName( ) );
            emailModel.put( MARK_COMMENTAIRE, signalement.getCommentaire( ) );
            emailModel.put( MARK_LIEN_CONSULTATION, getLienConsultation( signalement ) );

            emailModel.put( MARK_DATE_PROGRAMMATION, signalement.getDatePrevueTraitement( ) );
            String dateDeTraitement = signalement.getDateServiceFaitTraitement( );

            if ( StringUtils.isNotBlank( dateDeTraitement ) )
            {
                emailModel.put( MARK_DATE_DE_TRAITEMENT, dateDeTraitement );
            }
            else
            {
                emailModel.put( MARK_DATE_DE_TRAITEMENT, StringUtils.EMPTY );
            }
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

            // Date d'envoi
            String dateEnvoi = signalement.getDateCreation( );
            if ( StringUtils.isNotBlank( dateEnvoi ) )
            {
                emailModel.put( MARK_DATE_ENVOI, dateEnvoi );
            }
            else
            {
                emailModel.put( MARK_DATE_ENVOI, StringUtils.EMPTY );
            }

            // Heure d'envoi
            Date heureEnvoiTmstp = signalement.getHeureCreation( );
            if ( null != heureEnvoiTmstp )
            {
                emailModel.put( MARK_HEURE_ENVOI, DateUtils.getHourWithSecondsFr( heureEnvoiTmstp ) );
            }
            else
            {
                emailModel.put( MARK_HEURE_ENVOI, StringUtils.EMPTY );
            }

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

            if ( signalement.getArrondissement( ) != null && signalement.getArrondissement( ).getId( ) != null )
            {
                emailModel.put( MARK_ARRONDISSEMENT, signalement.getArrondissement( ).getId( ) );
            }
            else
            {
                emailModel.put( MARK_ARRONDISSEMENT, "" );
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
        NotificationSignalementUserTaskConfig config = _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( task.getId( ),
                SignalementUtils.getPlugin( ) );

        String strMessage = _signalementService.loadMessageCreationSignalement( );
        if ( ( ( config != null ) && ( config.getMessage( ) == null ) ) && !strMessage.equals( StringUtils.EMPTY ) )
        {
            config.setMessage( strMessage );
        }

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
        dto.setNom( "Heure d'envoi" );
        dto.setValeur( MARK_HEURE_ENVOI );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Date d'envoi" );
        dto.setValeur( MARK_DATE_ENVOI );
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
        balises.add( dto );

        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Lien de consultation du message" );
        dto.setValeur( MARK_LIEN_CONSULTATION );
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

        NotificationSignalementUserTaskConfig config = new NotificationSignalementUserTaskConfig( );

        try
        {
            BeanUtils.populate( config, request.getParameterMap( ) );
            config.setIdTask( task.getId( ) );
        }

        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );

            Object [ ] tabError = {
                    e.getMessage( )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_EXCEPTION_OCCURED, tabError, AdminMessage.TYPE_STOP );
        }

        // Gestion des erreurs
        if ( config.getSender( ).equals( StringUtils.EMPTY ) )
        {
            strError = ERROR_SENDER;
        }
        else
            if ( config.getSubject( ).equals( StringUtils.EMPTY ) )
            {
                strError = ERROR_SUBJECT;
            }
            else
                if ( config.getMessage( ).equals( StringUtils.EMPTY ) )
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

        if ( _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( task.getId( ), SignalementUtils.getPlugin( ) ).getIdTask( ) == 0 )
        {
            _notificationSignalementUserTaskConfigDAO.insert( config, SignalementUtils.getPlugin( ) );
        }
        else
        {
            _notificationSignalementUserTaskConfigDAO.update( config, SignalementUtils.getPlugin( ) );
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
        NotificationUserValue notificationUserValue = _notificationUserValueService.findByPrimaryKey( nIdHistory, task.getId( ), null );

        Map<String, Object> model = new HashMap<>( );
        NotificationSignalementUserTaskConfig config = _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( task.getId( ), null );
        model.put( MARK_CONFIG, config );
        model.put( MARK_NOTIFICATION_USER_VALUE, notificationUserValue );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_USER_INFORMATION, locale, model );

        return template.getHtml( );
    }

    /**
     * Gets the task information xml.
     *
     * @param nIdHistory
     *            the n id history
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the task information xml
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
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
        return null;
    }

    /**
     * Get the link of the "consultation page" (front office signalement).
     *
     * @param signalement
     *            the report
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
