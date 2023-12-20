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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.IDateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.ISignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.TaskUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationSignalementUserTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationSignalementUserTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationUserValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.IWorkflowSignalementUtil;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.WorkflowSignalementConstants;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.url.UrlItem;

/**
 * NotificationSignalementUserTask class.
 */
public class NotificationSignalementUserTask extends AbstractSignalementTask
{

    /** The Constant PROPERTY_TS_BASE_URL. */
    // PROPERTIES
    private static final String PROPERTY_TS_BASE_URL = "lutece.ts.prod.url";

    /** The Constant JSP_PORTAL_USER. */
    // JSP
    private static final String JSP_PORTAL_USER = "jsp/site/Portal.jsp?instance=signalement";

    /** The Constant MARK_NUMERO. */
    // MARKERS
    private static final String MARK_NUMERO = "numero";

    /** The Constant MARK_TYPE. */
    private static final String MARK_TYPE = "type";

    /** The Constant MARK_ID_TYPE. */
    private static final String MARK_ID_TYPE = "id_type";

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

    /** The Constant MARK_DATE_DE_TRAITEMENT. */
    private static final String MARK_DATE_DE_TRAITEMENT = "datetraitement";

    /** The Constant MARK_HEURE_DE_TRAITEMENT. */
    private static final String MARK_HEURE_DE_TRAITEMENT = "heuretraitement";
    private static final String MARK_URL_SONDAGE_DEMANDE = "urlSondageDemande";
    private static final String MARK_URL_SONDAGE_SERVICE = "urlSondageService";
    private static final String URL_SONDAGE_DEMANDE = "sitelabels.site_property.message.url.sondage.demande";
    private static final String URL_SONDAGE_SERVICE = "sitelabels.site_property.message.url.sondage.sevice";
    private static final String MARK_CP = "code_postal";
    private static final String MARK_ID_TYPO_LVL_1 = "id_typologie_lvl_1";

    /** The Constant MARK_DATE_ENVOI. */
    private static final String MARK_DATE_ENVOI = "dateEnvoi";

    /** The Constant MARK_HEURE_ENVOI. */
    private static final String MARK_HEURE_ENVOI = "heureEnvoi";

    /** The Constant MARK_RAISONS_REJET. */
    private static final String MARK_RAISONS_REJET = "raisons_rejet";

    /** The Constant MARK_ALIAS_ANOMALIE. */
    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    /** The Constant MARK_ID_ANOMALIE. */
    private static final String MARK_ID_ANOMALIE = "id_anomalie";

    /** The Constant PARAMETER_PAGE. */
    // PARAMETERS
    private static final String PARAMETER_PAGE = "page";

    /** The Constant PARAMETER_SUIVI. */
    private static final String PARAMETER_SUIVI = "suivi";

    /** The Constant PARAMETER_TOKEN. */
    private static final String PARAMETER_TOKEN = "token";

    /** The Constant PARAMETER_MESSAGE_FOR_USER. */
    private static final String PARAMETER_MESSAGE_FOR_USER = "messageForUser";

    /** The signalement service. */
    // SERVICES
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The notification user value service. */
    private NotificationUserValueService _notificationUserValueService = SpringContextService.getBean( "signalement.notificationUserValueService" );

    /** The observation rejet service. */
    private IObservationRejetService _observationRejetService = SpringContextService.getBean( "observationRejetService" );

    /** The notification signalement user task config DAO. */
    // DAO
    private NotificationSignalementUserTaskConfigDAO _notificationSignalementUserTaskConfigDAO = SpringContextService
            .getBean( "signalement.notificationSignalementUserTaskConfigDAO" );

    /** The signalement utils */
    // UTILS
    private ISignalementUtils _signalementUtils = SpringContextService.getBean( "signalement.signalementUtils" );

    /** The date utils */
    private IDateUtils _dateUtils = SpringContextService.getBean( "signalement.dateUtils" );

    /** The workflow signalement utils */
    private IWorkflowSignalementUtil _workflowSignalementUtils = SpringContextService.getBean( "signalement.workflowSignalementUtil" );

    private static final String MARK_ARRONDISSEMENT = "arrondissement";

    /**
     * Process task.
     *
     * @param nIdResourceHistory
     *            the n id resource history
     * @param request
     *            the request
     * @param locale
     *            the locale
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {

        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        Integer idRessource = resourceHistory.getIdResource( );
        Signalement signalement = _signalementService.getSignalementWithFullPhoto( idRessource );

        if ( signalement == null )
        {
            AppLogService.error( "Aucun signalement trouve pour resource id " + idRessource );
            return;
        }

        // get the config
        NotificationSignalementUserTaskConfig config = _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( getId( ), _signalementUtils.getPlugin( ) );

        String messageForUser = StringUtils.EMPTY;

        // 1 - Si via une tâche (Form), modification du contenu par le user
        if ( null != request )
        {
            messageForUser = request.getParameter( PARAMETER_MESSAGE_FOR_USER );
        }

        // 2 - Sinon récupération du message configuré sur la tache
        if ( StringUtils.isBlank( messageForUser ) && StringUtils.isNotBlank( config.getMessage( ) ) )
        {
            messageForUser = config.getMessage( );

        }

        if ( !StringUtils.isBlank( messageForUser ) )
        {
            messageForUser = messageForUser.replaceAll( "\r\n", "<br />" ).replaceAll( "\r|\n", "<br />" );
            config.setMessage( messageForUser );
        }

        boolean hasMail = false;
        String email = "";
        List<Signaleur> signaleurs = signalement.getSignaleurs( );
        for ( Signaleur signaleur : signaleurs )
        {
            if ( StringUtils.isNotEmpty( signaleur.getMail( ) ) )
            {
                hasMail = true;
                email = signaleur.getMail( );
            }
        }

        // Obtention des informations de l'email par remplacement des eventuelles balises freemarkers
        // ==> ajout des données pouvant être demandées ( correspondant à la map "balises" dans getDisplayConfigForm(...) )
        Map<String, Object> emailModel = new HashMap<>( );

        emailModel.put( MARK_ID_ANOMALIE, idRessource );
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

        if ( !signalement.getAdresses( ).isEmpty( ) )
        {
            emailModel.put( MARK_ADRESSE, signalement.getAdresses( ).get( 0 ).getAdresse( ) );
            if ( signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) != null )
            {
                emailModel.put( MARK_PRECISION, signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) );
            }
            else
            {
                emailModel.put( MARK_PRECISION, "" );
            }
        }
        else
        {
            emailModel.put( MARK_ADRESSE, "" );
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
            emailModel.put( MARK_HEURE_ENVOI, _dateUtils.getHourWithSecondsFr( heureEnvoiTmstp ) );
        }
        else
        {
            emailModel.put( MARK_HEURE_ENVOI, StringUtils.EMPTY );
        }

        emailModel.put( MARK_URL_SONDAGE_DEMANDE, DatastoreService.getDataValue( URL_SONDAGE_DEMANDE, "" ) );
        emailModel.put( MARK_URL_SONDAGE_SERVICE, DatastoreService.getDataValue( URL_SONDAGE_SERVICE, "" ) );

        if ( ( signalement.getAdresses( ) != null ) && ( CollectionUtils.isNotEmpty( signalement.getAdresses( ) ) )
                && ( signalement.getAdresses( ).get( 0 ) != null ) && ( signalement.getAdresses( ).get( 0 ).getAdresse( ) != null ) )
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

        String rejectReason = _workflowSignalementUtils.buildValueMotifRejetForEmailNotification( request,
                _observationRejetService.getAllObservationRejetActif( ) );
        if ( StringUtils.isNotBlank( rejectReason ) )
        {
            emailModel.put( MARK_RAISONS_REJET, rejectReason );
        }
        else
            if ( ( request != null ) && ( request.getSession( ).getAttribute( WorkflowSignalementConstants.PARAMETER_WEBSERVICE_RAISON_REJET ) != null ) )
            {
                // Récupération de la raison de rejet envoyé par le prestataire via WS
                emailModel.put( MARK_RAISONS_REJET, request.getSession( ).getAttribute( WorkflowSignalementConstants.PARAMETER_WEBSERVICE_RAISON_REJET ) );
            }
            else
            {
                // Sinon afin de ne pas avoir d'erreur, on set la raison de rejet à vide
                emailModel.put( MARK_RAISONS_REJET, "" );
            }

        if ( ( signalement.getArrondissement( ) != null ) && ( signalement.getArrondissement( ).getId( ) != null ) )
        {
            emailModel.put( MARK_ARRONDISSEMENT, signalement.getArrondissement( ).getId( ) );
        }
        else
        {
            emailModel.put( MARK_ARRONDISSEMENT, "" );
        }

        String message = AppTemplateService.getTemplateFromStringFtl( "[#ftl]" + config.getMessage( ), locale, emailModel ).getHtml( );
        String subject = AppTemplateService.getTemplateFromStringFtl( config.getSubject( ), locale, emailModel ).getHtml( );

        if ( emailModel.get( MARK_RAISONS_REJET ) != null )
        {
            String message2;
            Pattern p = Pattern.compile( MARK_RAISONS_REJET );
            Matcher m = p.matcher( message );
            while ( m.find( ) )
            {
                message2 = AppTemplateService.getTemplateFromStringFtl( message, locale, emailModel ).getHtml( );
                message = message2;
            }
        }

        if ( hasMail )
        {
            List<PhotoDMR> photos = signalement.getPhotos( );
            List<FileAttachment> files = new ArrayList<>( );

            for ( PhotoDMR photo : photos )
            {

                if ( ( photo.getImage( ) != null ) && ( photo.getImage( ).getImage( ) != null ) )
                {

                    String mime = photo.getImage( ).getMimeType( ).contains( "/" ) ? photo.getImage( ).getMimeType( ).split( "/" )[1] : photo.getImage( ).getMimeType( );
                    String fileAttachmentMime = SignalementConstants.MIME_TYPE_START + mime;

                    if ( photo.getVue( ) == 1 )
                    {

                        files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_ENSEMBLE_PJ + mime, photo.getImage( ).getImage( ),
                                fileAttachmentMime ) );

                    }
                    else
                    {
                        files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_PRES_PJ + mime, photo.getImage( ).getImage( ),
                                fileAttachmentMime ) );
                    }
                }
            }

            MailService.sendMailMultipartHtml( email, null, null, config.getSender( ),
                    AppPropertiesService.getProperty( "mail.noreply.email", "noreply-dansmarue@paris.fr" ), subject, message, null, files );

            // save the email (notification) in the workflow history
            NotificationUserValue notificationUserValue = new NotificationUserValue( );
            notificationUserValue.setIdResourceHistory( nIdResourceHistory );
            notificationUserValue.setIdTask( getId( ) );
            notificationUserValue.setValue( message );
            _notificationUserValueService.create( notificationUserValue );

        }

    }

    /**
     * Do remove config.
     */
    @Override
    public void doRemoveConfig( )
    {
        _notificationSignalementUserTaskConfigDAO.delete( getId( ), _signalementUtils.getPlugin( ) );
        _notificationUserValueService.removeByTask( getId( ), _signalementUtils.getPlugin( ) );
    }

    /**
     * Gets the title.
     *
     * @param locale
     *            the locale
     * @return the title
     */
    @Override
    public String getTitle( Locale locale )
    {
        return "Notification par email pour l'usager";
    }

    /**
     * Do remove task information.
     *
     * @param nIdHistory
     *            the n id history
     */
    @Override
    public void doRemoveTaskInformation( int nIdHistory )
    {
        _notificationUserValueService.removeByHistory( nIdHistory, getId( ), null );
    }

    /**
     * Get the link of the "consultation page" (front office signalement).
     *
     * @param signalement
     *            the report
     * @return the url to consult
     */
    private String getLienConsultation( Signalement signalement )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( AppPropertiesService.getProperty( PROPERTY_TS_BASE_URL ) + JSP_PORTAL_USER );

        urlItem.addParameter( PARAMETER_PAGE, PARAMETER_SUIVI );
        urlItem.addParameter( PARAMETER_TOKEN, signalement.getToken( ) );

        return urlItem.getUrl( );
    }

    /**
     * Gets the task form entries.
     *
     * @param locale
     *            the locale
     * @return the task form entries
     */
    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )
    {
        return null;
    }

}
