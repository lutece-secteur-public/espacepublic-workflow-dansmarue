/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.FileMessageCreationService;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationSignalementUserTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationSignalementUserTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationUserValue;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.url.UrlItem;

/**
 * NotificationSignalementUserTask class
 *
 */
public class NotificationSignalementUserTask extends AbstractSignalementTask
{

    // PROPERTIES
    private static final String                      PROPERTY_TS_BASE_URL                      = "lutece.ts.prod.url";
    private static final String                      PROPERTY_FILE_FOLDER_PATH                 = "signalement.pathForFileMessageCreation";

    // JSP
    private static final String                      JSP_PORTAL_USER                           = "jsp/site/Portal.jsp?instance=signalement";

    // MARKERS
    private static final String                      MARK_NUMERO                               = "numero";
    private static final String                      MARK_TYPE                                 = "type";
    private static final String                      MARK_ADRESSE                              = "adresse";
    private static final String                      MARK_PRIORITE                             = "priorite";
    private static final String                      MARK_COMMENTAIRE                          = "commentaire";
    private static final String                      MARK_PRECISION                            = "precision";
    private static final String                      MARK_LIEN_CONSULTATION                    = "lien_consultation";

    private static final String                      MARK_DATE_PROGRAMMATION                   = "date_programmation";
    private static final String                      MARK_DATE_DE_TRAITEMENT                   = "datetraitement";
    private static final String                      MARK_HEURE_DE_TRAITEMENT                  = "heuretraitement";

    private static final String                      MARK_DATE_ENVOI                           = "dateEnvoi";
    private static final String                      MARK_HEURE_ENVOI                          = "heureEnvoi";

    private static final String                      MARK_RAISONS_REJET                        = "raisons_rejet";

    private static final String                      MARK_ALIAS_ANOMALIE                       = "alias_anomalie";

    private static final String                      MARK_ID_ANOMALIE                          = "id_anomalie";

    // PARAMETERS
    private static final String                      PARAMETER_PAGE                            = "page";
    private static final String                      PARAMETER_SUIVI                           = "suivi";
    private static final String                      PARAMETER_TOKEN                           = "token";
    private static final String                      PARAMETER_MESSAGE_FOR_USER                = "messageForUser";
    private static final String                      PARAMETER_MOTIF_REJET                     = "motif_rejet";
    private static final String                      PARAMETER_MOTIF_AUTRE_CHECKBOX            = "motif_autre_checkbox";
    private static final String                      PARAMETER_MOTIF_AUTRE                     = "motif_autre";

    private static final String                      MOTIF_REJET_PREPEND                       = "- ";
    private static final String                      MOTIF_REJET_SEPARATOR                     = "<br/>";

    // SERVICES
    private ISignalementService                      _signalementService                       = ( ISignalementService ) SpringContextService.getBean( "signalementService" );
    private NotificationUserValueService             _notificationUserValueService             = SpringContextService.getBean( "signalement.notificationUserValueService" );
    private FileMessageCreationService               _fileMessageCreationService               = SpringContextService.getBean( "fileMessageCreationService" );
    private IObservationRejetService                 _observationRejetService                  = SpringContextService.getBean( "observationRejetService" );

    // DAO
    private NotificationSignalementUserTaskConfigDAO _notificationSignalementUserTaskConfigDAO = SpringContextService.getBean( "signalement.notificationSignalementUserTaskConfigDAO" );

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
        NotificationSignalementUserTaskConfig config = _notificationSignalementUserTaskConfigDAO.findByPrimaryKey( getId( ), SignalementUtils.getPlugin( ) );

        String messageForUser = StringUtils.EMPTY;

        // 1 - Si via une tâche (Form), modification du contenu par le user
        if ( null != request )
        {
            messageForUser = request.getParameter( PARAMETER_MESSAGE_FOR_USER );
        }

        // 2 - Sinon récupération du fichier
        if ( StringUtils.isBlank( messageForUser ) )
        {
            String strFilePath = AppPathService.getAbsolutePathFromRelativePath( AppPropertiesService.getProperty( PROPERTY_FILE_FOLDER_PATH ) );
            String strFileName = "messagecreation_" + signalement.getId( ).toString( );
            messageForUser = _fileMessageCreationService.readFile( strFilePath + strFileName );
            try
            {
                _fileMessageCreationService.deleteFile( strFilePath, strFileName );
            } catch ( IOException e )
            {
                AppLogService.error( e );
            }
        }
        if ( !StringUtils.isBlank( messageForUser ) )
        {
            messageForUser = messageForUser.replaceAll( "\r\n", "<br />" ).replaceAll( "\r|\n", "<br />" );
            config.setMessage( messageForUser );
        }
        // 3 - Sinon message de la tâche

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
        Map<String, Object> emailModel = new HashMap<String, Object>( );

        emailModel.put( MARK_ID_ANOMALIE, idRessource );
        emailModel.put( MARK_NUMERO, signalement.getNumeroSignalement( ) );
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
            } else
            {
                emailModel.put( MARK_PRECISION, "" );
            }
        } else
        {
            emailModel.put( MARK_ADRESSE, "" );
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

        // get the message (contenu)
        if ( null != request )
        {
            List<String> motifsRejet = null;
            motifsRejet = getMotifsRejet( request );
            boolean motifAutreCheckBox = StringUtils.isNotBlank( request.getParameter( PARAMETER_MOTIF_AUTRE_CHECKBOX ) );
            // Construction de la variable raisons_rejet
            if ( CollectionUtils.isNotEmpty( motifsRejet ) || motifAutreCheckBox )
            {
                StringBuilder motifsRejetStr = new StringBuilder( );
                if ( CollectionUtils.isNotEmpty( motifsRejet ) )
                {
                    for ( int i = 0; i < motifsRejet.size( ); i++ )
                    {
                        String motifRejet = motifsRejet.get( i );
                        motifsRejetStr.append( MOTIF_REJET_PREPEND ).append( motifRejet );
                        if ( ( i < ( motifsRejet.size( ) - 1 ) ) || motifAutreCheckBox )
                        {
                            motifsRejetStr.append( MOTIF_REJET_SEPARATOR );
                        }
                    }
                }

                if ( motifAutreCheckBox )
                {
                    String motifAutre = request.getParameter( PARAMETER_MOTIF_AUTRE );
                    motifsRejetStr.append( MOTIF_REJET_PREPEND ).append( motifAutre );
                }
                emailModel.put( MARK_RAISONS_REJET, motifsRejetStr.toString( ) );
            }
        }

        String message = AppTemplateService.getTemplateFromStringFtl( config.getMessage( ), locale, emailModel ).getHtml( );
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

            MailService.sendMailMultipartHtml( email, null, null, config.getSender( ), AppPropertiesService.getProperty( "mail.noreply.email", "noreply-dansmarue@paris.fr" ), subject, message, null,
                    files );

            // save the email (notification) in the workflow history
            NotificationUserValue notificationUserValue = new NotificationUserValue( );
            notificationUserValue.setIdResourceHistory( nIdResourceHistory );
            notificationUserValue.setIdTask( getId( ) );
            notificationUserValue.setValue( message );
            _notificationUserValueService.create( notificationUserValue );

        }

    }

    /**
     * Get reject reason.
     * @param request
     *         the http reuest
     * @return list of reject reason
     */
    private List<String> getMotifsRejet( HttpServletRequest request )
    {
        if ( null == request )
        {
            return Collections.emptyList( );
        }
        List<String> motifsRejet = new ArrayList<String>( );
        String[] motifsRejetIds = request.getParameterValues( PARAMETER_MOTIF_REJET );
        if ( !ArrayUtils.isEmpty( motifsRejetIds ) )
        {
            List<ObservationRejet> observationList = _observationRejetService.getAllObservationRejetActif( );

            for ( ObservationRejet observation : observationList )
            {
                for ( String motifRejetId : motifsRejetIds )
                {
                    Integer motifRejetInt = Integer.parseInt( motifRejetId );
                    if ( observation.getActif( ) && observation.getId( ).equals( motifRejetInt ) )
                    {
                        motifsRejet.add( observation.getLibelle( ) );
                    }
                }
            }
        }
        return motifsRejet;
    }

    @Override
    public void doRemoveConfig( )
    {
        _notificationSignalementUserTaskConfigDAO.delete( getId( ), SignalementUtils.getPlugin( ) );
        _notificationUserValueService.removeByTask( getId( ), SignalementUtils.getPlugin( ) );
    }

    @Override
    public String getTitle( Locale locale )
    {
        return "Notification par email pour l'usager";
    }

    @Override
    public void doRemoveTaskInformation( int nIdHistory )
    {
        _notificationUserValueService.removeByHistory( nIdHistory, getId( ), null );
    }

    /**
     * Get the link of the "consultation page" (front office signalement)
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

    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )
    {
        return null;
    }

}
