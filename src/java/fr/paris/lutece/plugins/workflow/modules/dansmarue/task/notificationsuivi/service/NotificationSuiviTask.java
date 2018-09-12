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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.service;

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

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
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
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviValue;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;

/**
 *
 * NotificationSuiviTask
 *
 */
public class NotificationSuiviTask extends AbstractSignalementTask
{

    private static final Logger            LOGGER                     = Logger.getLogger( NotificationSuiviTask.class );

    // TITLE
    private static final String            TASK_TITLE                 = "#i18n{module.workflow.dansmarue.workflow.notification.suivi}";

    // MARKERS
    private static final String            MARK_NUMERO                = "numero";
    private static final String            MARK_TYPE                  = "type";
    private static final String            MARK_ADRESSE               = "adresse";
    private static final String            MARK_PRIORITE              = "priorite";
    private static final String            MARK_COMMENTAIRE           = "commentaire";
    private static final String            MARK_PRECISION             = "precision";
    private static final String            MARK_LIEN_CONSULTATION     = "lien_consultation";

    private static final String            MARK_DATE_PROGRAMMATION    = "date_programmation";
    private static final String            MARK_DATE_DE_TRAITEMENT    = "datetraitement";
    private static final String            MARK_HEURE_DE_TRAITEMENT   = "heuretraitement";

    private static final String            MARK_DATE_ENVOI            = "dateEnvoi";
    private static final String            MARK_HEURE_ENVOI           = "heureEnvoi";
    private static final String            MARK_EMAIL_USAGER          = "emailUsager";

    private static final String            MARK_ALIAS_ANOMALIE        = "alias_anomalie";
    private static final String            MARK_ALIAS_MOBILE_ANOMALIE = "alias_mobile_anomalie";

    private static final String            MARK_ID_ANOMALIE           = "id_anomalie";

    private static final String            PROPERTY_CERT_PWD          = "signalement.cert.pwd";
    private static final String            PROPERTY_CERT_PATH         = "signalement.cert.p12.path";
    private static final String            PROPERTY_APNS_PROD         = "signalement.anps.prod";

    private static final String            MARK_ANOMALY_ID            = "anomalyId";
    private static final String            MARK_TYPE_PUSH             = "type";

    @Inject
    @Named( "signalementService" )
    private ISignalementService            _signalementService;

    @Inject
    @Named( "signalement.notificationSuiviValueService" )
    private NotificationSuiviValueService  _notificationSuiviValueService;
    @Inject
    @Named( "signalement.notificationSignalementSuiviTaskConfigDAO" )
    private NotificationSuiviTaskConfigDAO _notificationSuiviTaskConfigDAO;

    @Inject
    @Named( "signalementSuiviService" )
    private ISignalementSuiviService       _signalementSuiviService;

    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        Integer idRessource = resourceHistory.getIdResource( );
        Signalement signalement = _signalementService.getSignalementWithFullPhoto( idRessource );

        // get the config
        NotificationSuiviTaskConfig config = _notificationSuiviTaskConfigDAO.load( getId( ), SignalementUtils.getPlugin( ) );

        List<SiraUser> followers = _signalementSuiviService.findUsersMobilesByIdSignalement( new Long( idRessource ) );
        List<String> followersMails = _signalementSuiviService.findUsersMailByIdSignalement( new Long( idRessource ) );
        if ( CollectionUtils.isEmpty( followers ) && CollectionUtils.isEmpty( followersMails ) )
        {
            return;
        }

        // Obtention des informations de l'email par remplacement des eventuelles balises freemarkers
        // ==> ajout des données pouvant être demandées ( correspondant à la map "balises" dans getDisplayConfigForm(...) )
        Map<String, Object> notifModel = new HashMap<String, Object>( );
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
        } else
        {
            notifModel.put( MARK_PRECISION, "" );
        }
        notifModel.put( MARK_PRIORITE, signalement.getPrioriteName( ) );

        notifModel.put( MARK_COMMENTAIRE, signalement.getCommentaire( ) );

        notifModel.put( MARK_LIEN_CONSULTATION, _signalementService.getLienConsultation( signalement, request ) );

        if ( StringUtils.isNotBlank( signalement.getDatePrevueTraitement( ) ) )
        {
            notifModel.put( MARK_DATE_PROGRAMMATION, signalement.getDatePrevueTraitement( ) );
        } else
        {
            notifModel.put( MARK_DATE_PROGRAMMATION, StringUtils.EMPTY );
        }

        String dateDeTraitement = signalement.getDateServiceFaitTraitement( );

        if ( StringUtils.isNotBlank( dateDeTraitement ) )
        {
            notifModel.put( MARK_DATE_DE_TRAITEMENT, dateDeTraitement );
        } else
        {
            notifModel.put( MARK_DATE_DE_TRAITEMENT, StringUtils.EMPTY );
        }
        String heureDeTraitement = signalement.getHeureServiceFaitTraitement( );
        if ( StringUtils.isNotBlank( heureDeTraitement ) )
        {
            heureDeTraitement = heureDeTraitement.substring( 0, 2 ) + ":" + heureDeTraitement.substring( 2 );
        } else
        {
            heureDeTraitement = StringUtils.EMPTY;
        }
        notifModel.put( MARK_HEURE_DE_TRAITEMENT, heureDeTraitement );

        // Date d'envoi
        String dateEnvoi = signalement.getDateCreation( );
        if ( StringUtils.isNotBlank( dateEnvoi ) )
        {
            notifModel.put( MARK_DATE_ENVOI, dateEnvoi );
        } else
        {
            notifModel.put( MARK_DATE_ENVOI, StringUtils.EMPTY );
        }

        // Heure d'envoi
        Date heureEnvoiTmstp = signalement.getHeureCreation( );
        if ( null != heureEnvoiTmstp )
        {
            notifModel.put( MARK_HEURE_ENVOI, DateUtils.getHourWithSecondsFr( heureEnvoiTmstp ) );
        } else
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

        // save the email (notification) in the workflow history
        NotificationSuiviValue notificationSuiviValue = new NotificationSuiviValue( );
        notificationSuiviValue.setIdResourceHistory( nIdResourceHistory );
        notificationSuiviValue.setIdTask( getId( ) );

        if ( CollectionUtils.isNotEmpty( followersMails ) )
        {
            String message = AppTemplateService.getTemplateFromStringFtl( config.getMailMessage( ), locale, notifModel ).getHtml( );
            String subject = AppTemplateService.getTemplateFromStringFtl( config.getSubject( ), locale, notifModel ).getHtml( );
            List<PhotoDMR> photos = signalement.getPhotos( );
            List<FileAttachment> files = new ArrayList<FileAttachment>( );

            for ( PhotoDMR photo : photos )
            {

                String[] mime = photo.getImage( ).getMimeType( ).split( "/" );

                if ( photo.getVue( ) == 1 )
                {

                    files.add( new FileAttachment( "VueEnsemble." + mime[1], photo.getImage( ).getImage( ), photo.getImage( ).getMimeType( ) ) );

                } else
                {
                    files.add( new FileAttachment( "VueDetaillee." + mime[1], photo.getImage( ).getImage( ), photo.getImage( ).getMimeType( ) ) );
                }
            }

            for ( String followerMail : followersMails )
            {
                MailService.sendMailMultipartHtml( followerMail, null, null, config.getSender( ), AppPropertiesService.getProperty( "mail.noreply.email", "noreply-dansmarue@paris.fr" ), subject,
                        message, null, files );
            }

            notificationSuiviValue.setMailNotificationValue( message );
        }

        if ( CollectionUtils.isNotEmpty( followers ) )
        {

            String subject = AppTemplateService.getTemplateFromStringFtl( config.getMobileSubject( ), locale, notifModel ).getHtml( );
            String message = AppTemplateService.getTemplateFromStringFtl( config.getMobileMessage( ), locale, notifModel ).getHtml( );

            List<String> iOsTokens = new ArrayList<>( );
            List<String> androidTokens = new ArrayList<>( );

            // Constitution des listes d'usagers a notifier
            for ( SiraUser siraUser : followers )
            {
                if ( StringUtils.isEmpty( siraUser.getToken( ) ) )
                {
                    continue;
                }
                // iOS
                if ( StringUtils.equals( SignalementConstants.SIGNALEMENT_PREFIX_IOS, siraUser.getDevice( ) ) )
                {
                    iOsTokens.add( siraUser.getToken( ) );
                }

                // Android
                if ( StringUtils.equals( SignalementConstants.SIGNALEMENT_PREFIX_ANDROID, siraUser.getDevice( ) ) )
                {
                    androidTokens.add( siraUser.getToken( ) );
                }

            }

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

            // Push iOS
            if ( CollectionUtils.isNotEmpty( iOsTokens ) )
            {

                try
                {
                    String certPwd = AppPropertiesService.getProperty( PROPERTY_CERT_PWD );
                    String certPath = AppPropertiesService.getProperty( PROPERTY_CERT_PATH );
                    boolean isApnsProd = AppPropertiesService.getPropertyBoolean( PROPERTY_APNS_PROD, false );

                    ApnsService service = APNS.newService( ).withCert( certPath, certPwd ).withAppleDestination( isApnsProd ).build( );
                    for ( String iosToken : iOsTokens )
                    {
                        PayloadBuilder payload = APNS.newPayload( );
                        payload.alertTitle( subject );
                        payload.alertBody( message );
                        payload.customField( MARK_ANOMALY_ID, idRessource );
                        payload.customField( MARK_TYPE_PUSH, "OUTDOOR" );
                        try
                        {
                            service.push( iosToken, payload.build( ) );
                        } catch ( Exception e )
                        {
                            LOGGER.error( "Erreur lors de l'envoi du push iOS vers " + iosToken, e );
                        }
                    }
                } catch ( Exception ex )
                {
                    LOGGER.error( "Erreur lors l'initialisation du service de push ios", ex );
                }
            }

            notificationSuiviValue.setMobileNotificationValue( message );
            _notificationSuiviValueService.create( notificationSuiviValue );
        }

    }

    @Override
    public String getTitle( Locale locale )
    {
        return TASK_TITLE;
    }

    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )
    {
        return null;
    }

    @Override
    public void doRemoveConfig( )
    {
        _notificationSuiviTaskConfigDAO.delete( getId( ), SignalementUtils.getPlugin( ) );
        _notificationSuiviValueService.removeByTask( getId( ), SignalementUtils.getPlugin( ) );
    }

    @Override
    public void doRemoveTaskInformation( int nIdHistory )
    {
        _notificationSuiviValueService.removeByHistory( nIdHistory, getId( ), null );
    }

}
