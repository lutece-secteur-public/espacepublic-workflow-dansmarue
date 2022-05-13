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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationpush.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementSuiviService;
import fr.paris.lutece.plugins.dansmarue.service.impl.AndroidPushService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationpush.business.NotificationPushTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationpush.business.NotificationPushTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationpush.business.NotificationPushValue;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * The Class NotificationPushTask.
 */
public class NotificationPushTask extends AbstractSignalementTask
{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = Logger.getLogger( NotificationPushTask.class );

    /** The Constant TASK_TITLE. */
    // TITLE
    private static final String TASK_TITLE = "#i18n{module.workflow.dansmarue.workflow.notification.push}";

    /** The Constant MARK_NUMERO. */
    // MARKERS
    private static final String MARK_NUMERO = "numero";

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

    /** The Constant MARK_DATE_DE_TRAITEMENT. */
    private static final String MARK_DATE_DE_TRAITEMENT = "datetraitement";

    /** The Constant MARK_HEURE_DE_TRAITEMENT. */
    private static final String MARK_HEURE_DE_TRAITEMENT = "heuretraitement";

    /** The Constant MARK_DATE_ENVOI. */
    private static final String MARK_DATE_ENVOI = "dateEnvoi";

    /** The Constant MARK_HEURE_ENVOI. */
    private static final String MARK_HEURE_ENVOI = "heureEnvoi";

    /** The Constant MARK_EMAIL_USAGER. */
    private static final String MARK_EMAIL_USAGER = "emailUsager";

    /** The Constant MARK_ALIAS_ANOMALIE. */
    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    /** The Constant MARK_ALIAS_MOBILE_ANOMALIE. */
    private static final String MARK_ALIAS_MOBILE_ANOMALIE = "alias_mobile_anomalie";

    /** The Constant MARK_ID_ANOMALIE. */
    private static final String MARK_ID_ANOMALIE = "id_anomalie";

    /** The Constant PROPERTY_CERT_CRED. */
    private static final String PROPERTY_CERT_CRED = "signalement.cert.pwd";

    /** The Constant PROPERTY_CERT_PATH. */
    private static final String PROPERTY_CERT_PATH = "signalement.cert.p12.path";

    /** The Constant PROPERTY_APNS_PROD. */
    private static final String PROPERTY_APNS_PROD = "signalement.anps.prod";

    /** The Constant MARK_ANOMALY_ID. */
    private static final String MARK_ANOMALY_ID = "anomalyId";

    /** The Constant MARK_TYPE_PUSH. */
    private static final String MARK_TYPE_PUSH = "type";

    /** The signalement service. */
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /** The notification push value service. */
    @Inject
    @Named( "signalement.notificationPushValueService" )
    private NotificationPushValueService _notificationPushValueService;

    /** The notification push task config DAO. */
    @Inject
    @Named( "signalement.notificationSignalementPushTaskConfigDAO" )
    private NotificationPushTaskConfigDAO _notificationPushTaskConfigDAO;

    /** The signalement suivi service. */
    @Inject
    @Named( "signalementSuiviService" )
    private ISignalementSuiviService _signalementSuiviService;

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

        // get the config
        NotificationPushTaskConfig config = _notificationPushTaskConfigDAO.load( getId( ), SignalementUtils.getPlugin( ) );

        List<SiraUser> receiver = new ArrayList<>( );
        if ( config.getIsDiffusionSuiveur( ) )
        {
            // Récupération des suiveurs non déclarant
            receiver.addAll( _signalementSuiviService.findFollowersMobilesByIdSignalement( idRessource ) );
        }

        if ( config.getIsDiffusionDeclarant( ) )
        {
            // Récupération des devices du déclarant
            receiver.addAll( _signalementSuiviService.findCreatorMobilesByIdSignalement( idRessource ) );
        }

        if ( CollectionUtils.isEmpty( receiver ) )
        {
            return;
        }

        // Obtention des informations de l'email par remplacement des eventuelles balises freemarkers
        // ==> ajout des données pouvant être demandées ( correspondant à la map "balises" dans getDisplayConfigForm(...) )
        Map<String, Object> notifModel = new HashMap<>( );
        notifModel.put( MARK_ID_ANOMALIE, idRessource );
        notifModel.put( MARK_NUMERO, signalement.getNumeroSignalement( ) );
        notifModel.put( MARK_TYPE, signalement.getType( ) );

        // Alias de l'anomalie
        String aliasType = signalement.getTypeSignalement( ).getAlias( );
        if ( null == aliasType )
        {
            aliasType = StringUtils.EMPTY;
        }
        notifModel.put( MARK_ALIAS_ANOMALIE, aliasType );

        // Alias mobile de l'anomalie
        String aliasMobileType = signalement.getTypeSignalement( ).getAliasMobile( );
        if ( null == aliasMobileType )
        {
            aliasMobileType = StringUtils.EMPTY;
        }
        notifModel.put( MARK_ALIAS_MOBILE_ANOMALIE, aliasMobileType );

        notifModel.put( MARK_ADRESSE, signalement.getAdresses( ).get( 0 ).getAdresse( ) );
        if ( signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) != null )
        {
            notifModel.put( MARK_PRECISION, signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) );
        }
        else
        {
            notifModel.put( MARK_PRECISION, "" );
        }
        notifModel.put( MARK_PRIORITE, signalement.getPrioriteName( ) );

        notifModel.put( MARK_COMMENTAIRE, signalement.getCommentaire( ) );

        notifModel.put( MARK_LIEN_CONSULTATION, _signalementService.getLienConsultation( signalement, request ) );

        if ( StringUtils.isNotBlank( signalement.getDatePrevueTraitement( ) ) )
        {
            notifModel.put( MARK_DATE_PROGRAMMATION, signalement.getDatePrevueTraitement( ) );
        }
        else
        {
            notifModel.put( MARK_DATE_PROGRAMMATION, StringUtils.EMPTY );
        }

        String dateDeTraitement = signalement.getDateServiceFaitTraitement( );

        if ( StringUtils.isNotBlank( dateDeTraitement ) )
        {
            notifModel.put( MARK_DATE_DE_TRAITEMENT, dateDeTraitement );
        }
        else
        {
            notifModel.put( MARK_DATE_DE_TRAITEMENT, StringUtils.EMPTY );
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
        notifModel.put( MARK_HEURE_DE_TRAITEMENT, heureDeTraitement );

        // Date d'envoi
        String dateEnvoi = signalement.getDateCreation( );
        if ( StringUtils.isNotBlank( dateEnvoi ) )
        {
            notifModel.put( MARK_DATE_ENVOI, dateEnvoi );
        }
        else
        {
            notifModel.put( MARK_DATE_ENVOI, StringUtils.EMPTY );
        }

        // Heure d'envoi
        Date heureEnvoiTmstp = signalement.getHeureCreation( );
        if ( null != heureEnvoiTmstp )
        {
            notifModel.put( MARK_HEURE_ENVOI, DateUtils.getHourWithSecondsFr( heureEnvoiTmstp ) );
        }
        else
        {
            notifModel.put( MARK_HEURE_ENVOI, StringUtils.EMPTY );
        }

        // Email de l'usager
        List<Signaleur> signaleurs = signalement.getSignaleurs( );
        String emailUsager = StringUtils.EMPTY;
        if ( CollectionUtils.isNotEmpty( signaleurs ) )
        {
            for ( Signaleur signaleur : signaleurs )
            {
                if ( !signaleur.getMail( ).isEmpty( ) )
                {
                    emailUsager = signaleur.getMail( );
                }
            }
        }
        notifModel.put( MARK_EMAIL_USAGER, emailUsager );

        // save the notification in the workflow history
        NotificationPushValue notificationPushValue = new NotificationPushValue( );
        notificationPushValue.setIdResourceHistory( nIdResourceHistory );
        notificationPushValue.setIdTask( getId( ) );

        if ( CollectionUtils.isNotEmpty( receiver ) )
        {

            String subject = AppTemplateService.getTemplateFromStringFtl( config.getMobileSubject( ), locale, notifModel ).getHtml( );
            String message = AppTemplateService.getTemplateFromStringFtl( config.getMobileMessage( ), locale, notifModel ).getHtml( );

            List<String> iOsTokens = new ArrayList<>( );
            List<String> androidTokens = new ArrayList<>( );

            // Constitution des listes d'usagers a notifier
            for ( SiraUser siraUser : receiver )
            {
                if ( StringUtils.isEmpty( siraUser.getToken( ) ) )
                {
                    continue;
                }
                // iOS
                if ( StringUtils.equals( SignalementConstants.SIGNALEMENT_PREFIX_IOS, siraUser.getDevice( ) ) )
                {
                    LOGGER.info( "Ajout d'un recever IOS" );
                    iOsTokens.add( siraUser.getToken( ) );
                }

                // Android
                if ( StringUtils.equals( SignalementConstants.SIGNALEMENT_PREFIX_ANDROID, siraUser.getDevice( ) ) )
                {
                    LOGGER.info( "Ajout d'un recever Android" );
                    androidTokens.add( siraUser.getToken( ) );
                }

            }

            LOGGER.info( "Début de l'envoi de notifs push Android" );
            // Push android
            if ( CollectionUtils.isNotEmpty( androidTokens ) )
            {
                Map<String, String> payload = new HashMap<>( );
                payload.put( MARK_ANOMALY_ID, idRessource.toString( ) );
                payload.put( MARK_TYPE_PUSH, "OUTDOOR" );

                for ( String androidToken : androidTokens )
                {
                    AndroidPushService.sendPush( androidToken, subject, message, payload );
                }
            }

            LOGGER.info( "Fin de l'envoi de notifs push Android" );

            LOGGER.info( "Début de l'envoi de notifs push IOS" );
            // Push iOS
            if ( CollectionUtils.isNotEmpty( iOsTokens ) )
            {
                LOGGER.info( "Préparation de l'envoi de notifs push IOS" );
                try
                {
                    String certPwd = AppPropertiesService.getProperty( PROPERTY_CERT_CRED );
                    String certPath = AppPropertiesService.getProperty( PROPERTY_CERT_PATH );
                    boolean isApnsProd = AppPropertiesService.getPropertyBoolean( PROPERTY_APNS_PROD, false );

                    ApnsService service = APNS.newService( ).withCert( certPath, certPwd ).withAppleDestination( isApnsProd ).build( );
                    LOGGER.info( "Initialisation du service de notif push IOS OK" );
                    for ( String iosToken : iOsTokens )
                    {
                        PayloadBuilder payload = APNS.newPayload( );
                        payload.alertTitle( subject );
                        payload.alertBody( message );
                        payload.customField( MARK_ANOMALY_ID, idRessource );
                        payload.customField( MARK_TYPE_PUSH, "OUTDOOR" );
                        pushNotification( service, iosToken, payload );
                    }
                }
                catch( Exception ex )
                {
                    LOGGER.error( "Erreur lors l'initialisation du service de push ios", ex );
                }
            }
            LOGGER.info( "Fin de l'envoi de notifs push IOS" );

            notificationPushValue.setMobileNotificationValue( message );
            _notificationPushValueService.create( notificationPushValue );
        }

    }

    /**
     * Push notification.
     *
     * @param service
     *            the service
     * @param iosToken
     *            the ios token
     * @param payload
     *            the payload
     */
    private void pushNotification( ApnsService service, String iosToken, PayloadBuilder payload )
    {
        try
        {
            LOGGER.info( "Envoi de la notif push IOS" );
            service.push( iosToken, payload.build( ) );
        }
        catch( Exception e )
        {
            LOGGER.error( "Erreur lors de l'envoi du push iOS vers " + iosToken, e );
        }
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
        return TASK_TITLE;
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

    /**
     * Do remove config.
     */
    @Override
    public void doRemoveConfig( )
    {
        _notificationPushTaskConfigDAO.delete( getId( ), SignalementUtils.getPlugin( ) );
        _notificationPushValueService.removeByTask( getId( ), SignalementUtils.getPlugin( ) );
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
        _notificationPushValueService.removeByHistory( nIdHistory, getId( ), null );
    }

}
