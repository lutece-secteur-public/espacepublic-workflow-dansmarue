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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.impl.SignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationUserMultiContentsValue;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.ResourceHistoryService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;

/**
 * NotificationSignalementUserTask class
 *
 */
public class NotificationSignalementUserMultiContentsTask extends AbstractSignalementTask
{

    // MARKER
    private static final String                                   MARK_NUMERO                                            = "numero";
    private static final String                                   MARK_TYPE                                              = "type";
    private static final String                                   MARK_ADRESSE                                           = "adresse";
    private static final String                                   MARK_PRIORITE                                          = "priorite";
    private static final String                                   MARK_COMMENTAIRE                                       = "commentaire";
    private static final String                                   MARK_PRECISION                                         = "precision";
    private static final String                                   MARK_LIEN_CONSULTATION                                 = "lien_consultation";
    private static final String                                   MARK_DATE_PROGRAMMATION                                = "date_programmation";
    private static final String                                   MARK_DATE_DE_TRAITEMENT                                = "datetraitement";
    private static final String                                   MARK_HEURE_DE_TRAITEMENT                               = "heuretraitement";
    private static final String                                   MARK_ALIAS_ANOMALIE                                    = "alias_anomalie";
    private static final String                                   MARK_ID_ANOMALIE                                       = "id_anomalie";

    // PARAMETERS
    private static final String                                   PARAMETER_CHOSEN_MESSAGE                               = "chosenMessage";
    private static final String                                   PARAMETER_MESSAGE_CONTENT                              = "message";
    private static final String                                   PARAMETER_ISROADMAP                                    = "isRoadMap";

    // SERVICES
    private SignalementService                                    _signalementService                                    = SpringContextService.getBean( "signalementService" );
    private NotificationUserMultiContentsValueService             _notificationUserMultiContentsValueService             = SpringContextService
            .getBean( "signalement.notificationUserMultiContentsValueService" );
    private ResourceHistoryService                                _resourceHistoryService                                = SpringContextService.getBean( "workflow.resourceHistoryService" );

    // DAO
    private NotificationSignalementUserMultiContentsTaskConfigDAO _notificationSignalementUserMultiContentsTaskConfigDAO = SpringContextService
            .getBean( "signalement.notificationSignalementUserMultiContentsTaskConfigDAO" );

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
            } else
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

        String strChosenMessage = request.getParameter( PARAMETER_CHOSEN_MESSAGE ) != null ? request.getParameter( PARAMETER_CHOSEN_MESSAGE ) : StringUtils.EMPTY;
        String attrChosenMessage = request.getSession( ).getAttribute( PARAMETER_CHOSEN_MESSAGE ) != null ? ( String ) request.getSession( ).getAttribute( PARAMETER_CHOSEN_MESSAGE )
                : StringUtils.EMPTY;

        NotificationSignalementUserMultiContentsTaskConfig config;
        String message = StringUtils.EMPTY;

        if ( !strChosenMessage.isEmpty( ) )
        {
            config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( Long.parseLong( strChosenMessage ), SignalementUtils.getPlugin( ) );
            // Vérifie si on a pas modifié le message de base
            if ( !config.getMessage( ).equals( request.getParameter( PARAMETER_MESSAGE_CONTENT + Long.parseLong( strChosenMessage ) ) ) )
            {
                message = request.getParameter( PARAMETER_MESSAGE_CONTENT + Long.parseLong( strChosenMessage ) );
            } else
            {
                message = config.getMessage( );
            }

        } else if ( !attrChosenMessage.isEmpty( ) && ( !"ramen_ok".equals( attrChosenMessage ) || !"ramen_ko".equals( attrChosenMessage ) ) )
        {
            config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( Long.parseLong( attrChosenMessage ), SignalementUtils.getPlugin( ) );
            message = config.getMessage( );
        } else if ( !attrChosenMessage.isEmpty( ) && "ramen_ko".equals( attrChosenMessage ) )
        {
            List<Long> listIdMessageTask = _notificationSignalementUserMultiContentsTaskConfigDAO.selectAllMessageTask( getId( ), SignalementUtils.getPlugin( ) );
            config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( listIdMessageTask.get( 1 ), SignalementUtils.getPlugin( ) );
            message = config.getMessage( );
        } else
        {
            List<Long> listIdMessageTask = _notificationSignalementUserMultiContentsTaskConfigDAO.selectAllMessageTask( getId( ), SignalementUtils.getPlugin( ) );
            config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( listIdMessageTask.get( 0 ), SignalementUtils.getPlugin( ) );
            message = config.getMessage( );
        }

        // if message is null, get the first contenu
        if ( message == null )
        {
            List<Long> listIdMessageTask = _notificationSignalementUserMultiContentsTaskConfigDAO.selectAllMessageTask( getId( ), SignalementUtils.getPlugin( ) );
            config = _notificationSignalementUserMultiContentsTaskConfigDAO.findByPrimaryKey( listIdMessageTask.get( 0 ), SignalementUtils.getPlugin( ) );
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
        emailModel.put( MARK_TYPE, signalement.getType( ) );
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
        String messageHtml = "";
        if ( hasMail )
        {
            messageHtml = AppTemplateService.getTemplateFromStringFtl( messageFormat, locale, emailModel ).getHtml( );
        } else if ( ( dateDeTraitement != null ) && ( heureDeTraitement != null ) )
        {
            messageHtml = "Passage le " + dateDeTraitement + " \u00E0 " + heureDeTraitement;
        }

        String subject = AppTemplateService.getTemplateFromStringFtl( config.getSubject( ), locale, emailModel ).getHtml( );

        if ( hasMail )
        {
            List<PhotoDMR> photos = signalement.getPhotos( );
            List<FileAttachment> files = new ArrayList<>( );

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

            MailService.sendMailMultipartHtml( email, null, null, config.getSender( ), AppPropertiesService.getProperty( "mail.noreply.email", "noreply-dansmarue@paris.fr" ), subject, messageHtml,
                    null, files );
        }

        // save the email (notification) in the workflow history
        NotificationUserMultiContentsValue notificationUserMultiContentsValue = new NotificationUserMultiContentsValue( );
        notificationUserMultiContentsValue.setIdResourceHistory( nIdResourceHistory );
        notificationUserMultiContentsValue.setIdTask( getId( ) );
        notificationUserMultiContentsValue.setValue( messageHtml );
        _notificationUserMultiContentsValueService.create( notificationUserMultiContentsValue );

    }

    @Override
    public void doRemoveConfig( )
    {
        _notificationSignalementUserMultiContentsTaskConfigDAO.delete( getId( ), SignalementUtils.getPlugin( ) );
        _notificationUserMultiContentsValueService.removeByTask( getId( ), SignalementUtils.getPlugin( ) );

    }

    @Override
    public String getTitle( Locale locale )
    {
        return "Notification par email pour l'usager (multi-contenus)";
    }

    public boolean isTaskForActionAutomatic( )
    {
        return true;
    }

    @Override
    public void doRemoveTaskInformation( int nIdHistory )
    {
        _notificationUserMultiContentsValueService.removeByHistory( nIdHistory, getId( ), null );
    }

    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )
    {
        return null;
    }

}
