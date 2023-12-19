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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologie;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.IMessageTypologieService;
import fr.paris.lutece.plugins.dansmarue.service.impl.SignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ISignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.TaskUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationUserMultiContentsValue;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;

/**
 * NotificationSignalementUserTask class.
 */
public class NotificationSignalementUserMultiContentsTask extends AbstractSignalementTask
{

    /** The Constant MARK_NUMERO. */
    // MARKER
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

    /** The Constant MARK_DATE_DE_TRAITEMENT. */
    private static final String MARK_DATE_DE_TRAITEMENT = "datetraitement";

    /** The Constant MARK_HEURE_DE_TRAITEMENT. */
    private static final String MARK_HEURE_DE_TRAITEMENT = "heuretraitement";

    /** The Constant MARK_ALIAS_ANOMALIE. */
    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    /** The Constant MARK_ID_ANOMALIE. */
    private static final String MARK_ID_ANOMALIE = "id_anomalie";
    private static final String MARK_URL_SONDAGE_DEMANDE = "urlSondageDemande";
    private static final String MARK_URL_SONDAGE_SERVICE = "urlSondageService";
    private static final String URL_SONDAGE_DEMANDE = "sitelabels.site_property.message.url.sondage.demande";
    private static final String URL_SONDAGE_SERVICE = "sitelabels.site_property.message.url.sondage.sevice";
    private static final String MARK_CP = "code_postal";
    private static final String MARK_ID_TYPO_LVL_1 = "id_typologie_lvl_1";
    private static final String MARK_ARRONDISSEMENT = "arrondissement";

    /** The Constant PARAMETER_CHOSEN_MESSAGE. */
    // PARAMETERS
    private static final String PARAMETER_CHOSEN_MESSAGE = "chosenMessage";

    /** The Constant PARAMETER_IS_MESSAGE_TYPO. */
    private static final String PARAMETER_IS_MESSAGE_TYPO = "isMessageTypo";

    private static final String MESSAGE_TYPO = "messageTypo";

    /** The Constant PARAMETER_MESSAGE_CONTENT. */
    private static final String PARAMETER_MESSAGE_CONTENT = "message";

    /** The Constant PARAMETER_ISROADMAP. */
    private static final String PARAMETER_ISROADMAP = "isRoadMap";

    /** The Constant PROPERTY_TACHES_NOTIF_SUIVEURS. */
    // PROPERTY
    private static final String PROPERTY_TACHES_NOTIF_SUIVEURS = "signalement.workflow.taches.notification.suiveurs";

    /** The Constant SUBJECT_MESSAGE_TYPOLOGIE. */
    private static final String SUBJECT_MESSAGE_TYPOLOGIE = "Dansmarue : Suivi de l’anomalie ${numero}";

    /** The signalement service. */
    // SERVICES
    private SignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The notification user multi contents value service. */
    private NotificationUserMultiContentsValueService _notificationUserMultiContentsValueService = SpringContextService
            .getBean( "signalement.notificationUserMultiContentsValueService" );

    /** The message typologie service. */
    private IMessageTypologieService _messageTypologieService = SpringContextService.getBean( "messageTypologieService" );

    /** The notification signalement user multi contents task config DAO. */
    // DAO
    private NotificationSignalementUserMultiContentsTaskConfigDAO _notificationSignalementUserMultiContentsTaskConfigDAO = SpringContextService
            .getBean( "signalement.notificationSignalementUserMultiContentsTaskConfigDAO" );

    /** The signalement utils */
    // UTILS
    private ISignalementUtils _signalementUtils = SpringContextService.getBean( "signalement.signalementUtils" );

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

        boolean hasMail = false;
        String email = "";
        List<Signaleur> signaleurs = signalement.getSignaleurs( );
        for ( Signaleur signaleur : signaleurs )
        {
            if ( signaleur.getMail( ).trim( ).length( ) != 0 )
            {
                hasMail = true;
                email = signaleur.getMail( );
            }
        }

        String emailAdresse = request.getParameter( "adresseEmail" );
        if ( !hasMail && StringUtils.isNotBlank( emailAdresse ) )
        {
            hasMail = true;
            email = emailAdresse;
        }

        // Si déclaration de service fait DMR, via RAMEN
        String strRoadMap = request.getParameter( PARAMETER_ISROADMAP );
        if ( ( null != strRoadMap ) && Boolean.valueOf( strRoadMap ) )
        {
            if ( StringUtils.isNotBlank( emailAdresse ) )
            {
                hasMail = true;
                email = emailAdresse;
            }
            else
            {
                hasMail = false;
                email = "";
            }
        }

        if ( !hasMail )
        {
            return;
        }

        // get the config

        String strChosenMessage = request.getParameter( PARAMETER_CHOSEN_MESSAGE ) != null ? request.getParameter( PARAMETER_CHOSEN_MESSAGE )
                : StringUtils.EMPTY;
        String attrChosenMessage = request.getSession( ).getAttribute( PARAMETER_CHOSEN_MESSAGE ) != null
                ? request.getSession( ).getAttribute( PARAMETER_CHOSEN_MESSAGE ).toString( )
                        : StringUtils.EMPTY;

        Boolean isMessageTypo = ( ( ( request.getParameter( PARAMETER_IS_MESSAGE_TYPO ) != null )
                && Boolean.valueOf( request.getParameter( PARAMETER_IS_MESSAGE_TYPO ) ) )
                || ( ( request.getSession( ).getAttribute( PARAMETER_IS_MESSAGE_TYPO ) != null )
                        && Boolean.valueOf( request.getSession( ).getAttribute( PARAMETER_IS_MESSAGE_TYPO ).toString( ) ) ) );

        NotificationSignalementUserMultiContentsTaskConfig config = new NotificationSignalementUserMultiContentsTaskConfig( );
        String message;

        if ( !isMessageTypo )
        {
            if ( !strChosenMessage.isEmpty( ) )
            {
                config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( Long.parseLong( strChosenMessage ),
                        _signalementUtils.getPlugin( ) );
                // Vérifie si on a pas modifié le message de base
                if ( !config.getMessage( ).equals( request.getParameter( PARAMETER_MESSAGE_CONTENT + Long.parseLong( strChosenMessage ) ) ) )
                {
                    message = request.getParameter( PARAMETER_MESSAGE_CONTENT + Long.parseLong( strChosenMessage ) );
                }
                else
                {
                    message = config.getMessage( );
                }

            }
            else
                if ( !attrChosenMessage.isEmpty( ) && ( !"ramen_ok".equals( attrChosenMessage ) ) && ( !"ramen_ko".equals( attrChosenMessage ) ) )
                {
                    config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( Long.parseLong( attrChosenMessage ),
                            _signalementUtils.getPlugin( ) );
                    message = config.getMessage( );
                }
                else
                {
                    List<Long> listIdMessageTask = _notificationSignalementUserMultiContentsTaskConfigDAO.selectAllMessageTask( getId( ),
                            _signalementUtils.getPlugin( ) );
                    config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( listIdMessageTask.get( 0 ),
                            _signalementUtils.getPlugin( ) );
                    message = config.getMessage( );
                }
        }
        else
        {
            if ( !StringUtils.isEmpty( attrChosenMessage ) || ( !StringUtils.isEmpty( strChosenMessage )
                    && StringUtils.isEmpty( request.getParameter( MESSAGE_TYPO + Long.parseLong( strChosenMessage ) ) ) ) )
            {
                // Ajout de l'entete
                message = DatastoreService.getDataValue( "sitelabels.site_property.message.typologie.entete.htmlblock", "" );
                // Message lié à la typologie
                MessageTypologie messageTypologie = new MessageTypologie( );
                if ( !strChosenMessage.isEmpty( ) )
                {
                    messageTypologie = _messageTypologieService.loadMessageTypologie( Integer.parseInt( strChosenMessage ) );
                    config.setIdMessage( Long.valueOf( strChosenMessage ) );
                }
                else
                    if ( !attrChosenMessage.isEmpty( ) )
                    {
                        messageTypologie = _messageTypologieService.loadMessageTypologie( Integer.parseInt( attrChosenMessage ) );
                        config.setIdMessage( Long.valueOf( attrChosenMessage ) );
                    }
                message = message.concat( messageTypologie.getContenuMessage( ).replace( "<p>", "" ).replace( "</p>", "" ) );

                // Ajout du pied de page

                message = message.concat( DatastoreService.getDataValue( "sitelabels.site_property.message.typologie.pieddepage.htmlblock", "" ) );
            }
            else
            {
                message = request.getParameter( MESSAGE_TYPO + Long.parseLong( strChosenMessage ) );
                config.setIdMessage( Long.valueOf( strChosenMessage ) );
            }
            config.setMessage( message );
            config.setSubject( SUBJECT_MESSAGE_TYPOLOGIE );
        }

        // if message is null, get the first contenu
        if ( message == null )
        {
            List<Long> listIdMessageTask = _notificationSignalementUserMultiContentsTaskConfigDAO.selectAllMessageTask( getId( ),
                    _signalementUtils.getPlugin( ) );
            config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( listIdMessageTask.get( 0 ), _signalementUtils.getPlugin( ) );
            message = config.getMessage( );
        }
        String messageFormat = message.replaceAll( "\r\n", "<br />" ).replaceAll( "\r|\n", "<br />" );

        // Obtention des informations de l'email par remplacement des
        // eventuelles balises freemarkers
        // ==> ajout des données pouvant être demandées ( correspondant à la
        // map "balises" dans getDisplayConfigForm(...) )
        Map<String, Object> emailModel = new HashMap<>( );
        emailModel.put( MARK_ID_ANOMALIE, idRessource );
        emailModel.put( MARK_NUMERO, signalement.getNumeroSignalement( ) );
        emailModel.put( MARK_ID_TYPE, signalement.getTypeSignalement( ).getId( ) );
        emailModel.put( MARK_TYPE, signalement.getType( ) );

        if ( ( signalement.getArrondissement( ) != null ) && ( signalement.getArrondissement( ).getId( ) != null ) )
        {
            emailModel.put( MARK_ARRONDISSEMENT, signalement.getArrondissement( ).getId( ) );
        }
        else
        {
            emailModel.put( MARK_ARRONDISSEMENT, "" );
        }

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
        emailModel.put( MARK_LIEN_CONSULTATION, _signalementService.getLienConsultation( signalement, request ) );
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

        String messageHtml = "";
        if ( hasMail )
        {
            messageHtml = AppTemplateService.getTemplateFromStringFtl( messageFormat, locale, emailModel ).getHtml( );
        }
        else
            if ( ( dateDeTraitement != null ) && ( heureDeTraitement != null ) )
            {
                messageHtml = "Passage le " + dateDeTraitement + " \u00E0 " + heureDeTraitement;
            }

        String subject = AppTemplateService.getTemplateFromStringFtl( config.getSubject( ), locale, emailModel ).getHtml( );

        List<String> listMailSuiveurs = _signalementService.getAllSuiveursMail( idRessource );

        String strListTachesNotifSuiveurs = AppPropertiesService.getProperty( PROPERTY_TACHES_NOTIF_SUIVEURS );
        List<String> listTachesNotifSuiveurs = Arrays.asList( strListTachesNotifSuiveurs.split( "," ) );

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

                    if ( photo.getVue( ).intValue( ) == SignalementConstants.OVERVIEW )
                    {

                        files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_ENSEMBLE_PJ + mime, photo.getImage( ).getImage( ), fileAttachmentMime ) );

                    } else if ( photo.getVue( ).intValue( ) == SignalementConstants.SERVICE_DONE_VIEW )
                    {
                        files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_SERVICE_FAIT_PJ + mime, photo.getImage( ).getImage( ), fileAttachmentMime ) );
                    } else
                    {
                        files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_PRES_PJ + mime, photo.getImage( ).getImage( ), fileAttachmentMime ) );
                    }
                }
            }

            if ( listTachesNotifSuiveurs.contains( String.valueOf( getId( ) ) ) )
            {
                if ( !listMailSuiveurs.contains( email ) )
                {
                    listMailSuiveurs.add( email );
                }

                for ( String mail : listMailSuiveurs )
                {
                    MailService.sendMailMultipartHtml( mail, null, null, config.getSender( ),
                            AppPropertiesService.getProperty( "mail.noreply.email", "noreply-dansmarue@paris.fr" ), subject, messageHtml, null, files );
                }
            }
            else
            {
                MailService.sendMailMultipartHtml( email, null, null, config.getSender( ),
                        AppPropertiesService.getProperty( "mail.noreply.email", "noreply-dansmarue@paris.fr" ), subject, messageHtml, null, files );
            }

        }

        // save the email (notification) in the workflow history
        NotificationUserMultiContentsValue notificationUserMultiContentsValue = new NotificationUserMultiContentsValue( );
        notificationUserMultiContentsValue.setIdResourceHistory( nIdResourceHistory );
        notificationUserMultiContentsValue.setIdTask( getId( ) );
        notificationUserMultiContentsValue.setValue( messageHtml );
        notificationUserMultiContentsValue.setIdMessage( config.getIdMessage( ).intValue( ) );
        _notificationUserMultiContentsValueService.create( notificationUserMultiContentsValue );

    }

    /**
     * Do remove config.
     */
    @Override
    public void doRemoveConfig( )
    {
        _notificationSignalementUserMultiContentsTaskConfigDAO.delete( getId( ), _signalementUtils.getPlugin( ) );
        _notificationUserMultiContentsValueService.removeByTask( getId( ), _signalementUtils.getPlugin( ) );

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
        return "Notification par email pour l'usager (multi-contenus)";
    }

    /**
     * Checks if is task for action automatic.
     *
     * @return true, if is task for action automatic
     */
    public boolean isTaskForActionAutomatic( )
    {
        return true;
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
        _notificationUserMultiContentsValueService.removeByHistory( nIdHistory, getId( ), null );
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
