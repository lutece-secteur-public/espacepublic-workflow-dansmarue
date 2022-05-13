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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementSuiviService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business.NotificationSuiviValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.WorkflowSignalementConstants;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.WorkflowSignalementUtil;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;

/**
 * NotificationSuiviTask.
 */
public class NotificationSuiviTask extends AbstractSignalementTask
{

    /** The Constant TASK_TITLE. */
    // TITLE
    private static final String TASK_TITLE = "#i18n{module.workflow.dansmarue.workflow.notification.suivi}";

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

    /** The Constant MARK_RAISONS_REJET. */
    private static final String MARK_RAISONS_REJET = "raisons_rejet";

    private static final String MARK_ARRONDISSEMENT = "arrondissement";

    private static final String MARK_ID_TYPO = "id_type";

    /** The signalement service. */
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /** The notification suivi value service. */
    @Inject
    @Named( "signalement.notificationSuiviValueService" )
    private NotificationSuiviValueService _notificationSuiviValueService;

    /** The notification suivi task config DAO. */
    @Inject
    @Named( "signalement.notificationSignalementSuiviTaskConfigDAO" )
    private NotificationSuiviTaskConfigDAO _notificationSuiviTaskConfigDAO;

    /** The signalement suivi service. */
    @Inject
    @Named( "signalementSuiviService" )
    private ISignalementSuiviService _signalementSuiviService;

    /** The observation rejet service. */
    @Inject
    @Named( "observationRejetService" )
    private IObservationRejetService _observationRejetService;

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
        NotificationSuiviTaskConfig config = _notificationSuiviTaskConfigDAO.load( getId( ), SignalementUtils.getPlugin( ) );

        List<String> followersMails = _signalementSuiviService.findUsersMailByIdSignalement( idRessource );
        if ( CollectionUtils.isEmpty( followersMails ) )
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

        if ( followersMails.contains( emailUsager ) )
        {
            followersMails.remove( emailUsager );
        }

        String rejectReason = WorkflowSignalementUtil.buildValueMotifRejetForEmailNotification( request,
                _observationRejetService.getAllObservationRejetActif( ) );
        if ( StringUtils.isNotBlank( rejectReason ) )
        {
            notifModel.put( MARK_RAISONS_REJET, rejectReason );
        }
        else
            if ( ( request != null ) && ( request.getSession( ).getAttribute( WorkflowSignalementConstants.PARAMETER_WEBSERVICE_RAISON_REJET ) != null ) )
            {
                // Récupération de la raison de rejet envoyé par le prestataire via WS
                notifModel.put( MARK_RAISONS_REJET, request.getSession( ).getAttribute( WorkflowSignalementConstants.PARAMETER_WEBSERVICE_RAISON_REJET ) );
            }
            else
            {
                // Sinon afin de ne pas avoir d'erreur, on set la raison de rejet à vide
                notifModel.put( MARK_RAISONS_REJET, "" );
            }

        if ( signalement.getArrondissement( ) != null && signalement.getArrondissement( ).getId( ) != null )
        {
            notifModel.put( MARK_ARRONDISSEMENT, signalement.getArrondissement( ).getId( ) );
        }
        else
        {
            notifModel.put( MARK_ARRONDISSEMENT, "" );
        }

        if ( signalement.getTypeSignalement( ) != null && signalement.getTypeSignalement().getId()  != null )
        {
            notifModel.put( MARK_ID_TYPO, signalement.getTypeSignalement().getId() );
        }
        else
        {
            notifModel.put( MARK_ID_TYPO, "" );
        }


        // save the email (notification) in the workflow history
        NotificationSuiviValue notificationSuiviValue = new NotificationSuiviValue( );
        notificationSuiviValue.setIdResourceHistory( nIdResourceHistory );
        notificationSuiviValue.setIdTask( getId( ) );

        if ( CollectionUtils.isNotEmpty( followersMails ) )
        {
            String message = AppTemplateService.getTemplateFromStringFtl( config.getMailMessage( ), locale, notifModel ).getHtml( );
            String subject = AppTemplateService.getTemplateFromStringFtl( config.getSubject( ), locale, notifModel ).getHtml( );

            if ( notifModel.get( MARK_RAISONS_REJET ) != null )
            {
                String message2;
                Pattern p = Pattern.compile( MARK_RAISONS_REJET );
                Matcher m = p.matcher( message );
                while ( m.find( ) )
                {
                    message2 = AppTemplateService.getTemplateFromStringFtl( message, locale, notifModel ).getHtml( );
                    message = message2;
                }
            }

            List<PhotoDMR> photos = signalement.getPhotos( );
            List<FileAttachment> files = new ArrayList<>( );

            for ( PhotoDMR photo : photos )
            {
                if ( ( photo.getImage( ) != null ) && ( photo.getImage( ).getImage( ) != null ) )
                {

                    String [ ] mime = photo.getImage( ).getMimeType( ).split( "/" );

                    if ( photo.getVue( ) == 1 )
                    {

                        files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_ENSEMBLE_PJ + mime [1], photo.getImage( ).getImage( ),
                                photo.getImage( ).getMimeType( ) ) );

                    }
                    else
                    {
                        files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_PRES_PJ + mime [1], photo.getImage( ).getImage( ),
                                photo.getImage( ).getMimeType( ) ) );
                    }
                }
            }

            for ( String followerMail : followersMails )
            {
                MailService.sendMailMultipartHtml( followerMail, null, null, config.getSender( ),
                        AppPropertiesService.getProperty( "mail.noreply.email", "noreply-dansmarue@paris.fr" ), subject, message, null, files );
            }

            notificationSuiviValue.setMailNotificationValue( message );
            _notificationSuiviValueService.create( notificationSuiviValue );
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
        _notificationSuiviTaskConfigDAO.delete( getId( ), SignalementUtils.getPlugin( ) );
        _notificationSuiviValueService.removeByTask( getId( ), SignalementUtils.getPlugin( ) );
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
        _notificationSuiviValueService.removeByHistory( nIdHistory, getId( ), null );
    }

}
