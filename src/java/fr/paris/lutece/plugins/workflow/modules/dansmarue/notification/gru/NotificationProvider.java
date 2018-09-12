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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.notification.gru;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.IProvider;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.NotifyGruMarker;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

/**
 *
 * NotificationProvider
 *
 */
public class NotificationProvider implements IProvider
{

    // MARKERS
    private static final String MARK_MESSAGE                    = "message";
    private static final String MARK_NUMERO                     = "numero";
    private static final String MARK_TYPE                       = "type";
    private static final String MARK_ADRESSE                    = "adresse";
    private static final String MARK_PRIORITE                   = "priorite";
    private static final String MARK_COMMENTAIRE                = "commentaire";
    private static final String MARK_PRECISION                  = "precision";
    private static final String MARK_LIEN_CONSULTATION          = "lien_consultation";
    private static final String MARK_ALIAS_ANOMALIE             = "alias_anomalie";
    private static final String MARK_ALIAS_MOBILE_ANOMALIE      = "alias_mobile_anomalie";
    private static final String MARK_ID_ANOMALIE                = "id_anomalie";

    private static final String MARK_DATE_PROGRAMMATION         = "date_programmation";
    private static final String MARK_DATE_DE_TRAITEMENT         = "datetraitement";
    private static final String MARK_HEURE_DE_TRAITEMENT        = "heuretraitement";

    private static final String MARK_DATE_ENVOI                 = "dateEnvoi";
    private static final String MARK_HEURE_ENVOI                = "heureEnvoi";
    private static final String MARK_EMAIL_USAGER               = "emailUsager";

    private static final String MARK_RAISONS_REJET              = "raisons_rejet";

    // DESCRIPTION
    private static final String MARK_MESSAGE_DESC               = "Message associé";
    private static final String MARK_NUMERO_DESC                = "Numéro de l'anomalie";
    private static final String MARK_TYPE_DESC                  = "Type d'anomalie";
    private static final String MARK_ADRESSE_DESC               = "Adresse de l'anomalie";
    private static final String MARK_PRIORITE_DESC              = "Priorité";
    private static final String MARK_COMMENTAIRE_DESC           = "Commentaire";
    private static final String MARK_PRECISION_DESC             = "Précision de la localisation";
    private static final String MARK_LIEN_CONSULTATION_DESC     = "Lien de consultation du message";
    private static final String MARK_ALIAS_ANOMALIE_DESC        = "Alias de l'anomalie";
    private static final String MARK_ALIAS_MOBILE_ANOMALIE_DESC = "Alias mobile de l'anomalie";
    private static final String MARK_ID_ANOMALIE_DESC           = "Id de l'anomalie";

    private static final String MARK_DATE_PROGRAMMATION_DESC    = "Date prévue du traitement de l'anomalie";
    private static final String MARK_DATE_DE_TRAITEMENT_DESC    = "Date de traitement de l'anomalie";
    private static final String MARK_HEURE_DE_TRAITEMENT_DESC   = "Heure de traitement de l'anomalie";

    private static final String MARK_DATE_ENVOI_DESC            = "Date d'envoi du signalement";
    private static final String MARK_HEURE_ENVOI_DESC           = "Heure d'envoi du signalement";
    private static final String MARK_EMAIL_USAGER_DESC          = "Email de l'usager";

    private static final String MARK_RAISONS_REJET_DESC         = "Raisons du rejet";

    // REJET
    private static final String MOTIF_REJET_PREPEND             = "- ";
    private static final String MOTIF_REJET_SEPARATOR           = "<br/>";

    // PARAMETERS
    private static final String PARAMETER_PAGE                  = "page";
    private static final String PARAMETER_INSTANCE              = "instance";
    private static final String PARAMETER_INSTANCE_VALUE        = "signalement";
    private static final String PARAMETER_SUIVI                 = "suivi";
    private static final String PARAMETER_TOKEN                 = "token";

    // PROPERTIES
    private static final String PROPERTY_BASE_URL               = "lutece.ts.prod.url";

    // JSP
    private static final String JSP_PORTAL                      = "jsp/site/Portal.jsp";

    private ISignalementService _signalementService             = ( ISignalementService ) SpringContextService.getBean( "signalementService" );
    private IWorkflowService    _signalementWorkflowService     = ( IWorkflowService ) SpringContextService.getBean( "signalement.workflowService" );

    private Signalement         _signalement;
    private Signaleur           _signaleur;
    private String              _message;

    private String              _strDemandeTypeId               = AppPropertiesService.getProperty( "dmr-signalement.guichet.id.type.demande" );

    /**
     * Constructor
     *
     * @param resourceHistory
     *             the resourceHistory
     *
     */
    public NotificationProvider( ResourceHistory resourceHistory )
    {
        _signalement = _signalementService.getSignalementWithFullPhoto( resourceHistory.getIdResource( ) );

        _message = ( _signalementWorkflowService.selectMultiContentsMessageNotification( resourceHistory.getId( ) ) );
        if ( _message != null )
        {
            _message.replaceAll( "<br/>|<br>|<br />|<p>", System.getProperty( "line.separator" ) ).replaceAll( "<[^>]*>", "" );
        }

        if ( CollectionUtils.isNotEmpty( _signalement.getSignaleurs( ) ) )
        {
            _signaleur = _signalement.getSignaleurs( ).get( 0 );
        }

    }

    @Override
    public String provideCustomerConnectionId( )
    {
        return _signaleur.getGuid( );
    }

    @Override
    public String provideCustomerEmail( )
    {
        return _signaleur.getMail( );
    }

    @Override
    public String provideCustomerId( )
    {
        return String.valueOf( _signaleur.getId( ) );
    }

    @Override
    public String provideCustomerMobilePhone( )
    {
        return _signaleur.getIdTelephone( );
    }

    @Override
    public String provideDemandId( )
    {
        return String.valueOf( _signalement.getId( ) );
    }

    @Override
    public String provideDemandReference( )
    {
        return _signalement.getNumeroSignalement( );
    }

    @Override
    public String provideDemandTypeId( )
    {
        return _strDemandeTypeId;
    }

    /**
     * Get the link of the "consultation page" (front office signalement)
     *
     * @param signalement
     *            the signalement
     *
     * @return the url of the link
     */
    private String getLienConsultation( Signalement signalement )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( AppPropertiesService.getProperty( PROPERTY_BASE_URL ) + JSP_PORTAL );

        urlItem.addParameter( PARAMETER_INSTANCE, PARAMETER_INSTANCE_VALUE );
        urlItem.addParameter( PARAMETER_PAGE, PARAMETER_SUIVI );
        urlItem.addParameter( PARAMETER_TOKEN, signalement.getToken( ) );

        return urlItem.getUrl( );
    }

    @Override
    public Collection<NotifyGruMarker> provideMarkerValues( )
    {
        Collection<NotifyGruMarker> collectionNotifyGruMarkers = new ArrayList<>( );

        collectionNotifyGruMarkers.add( createMarkerValues( MARK_MESSAGE, _message ) );
        // Récupérer directement de _signalement

        collectionNotifyGruMarkers.add( createMarkerValues( MARK_ID_ANOMALIE, String.valueOf( _signalement.getId( ) ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_NUMERO, _signalement.getNumeroSignalement( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_TYPE, _signalement.getType( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_ADRESSE, _signalement.getAdresses( ).get( 0 ).getAdresse( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_PRIORITE, _signalement.getPrioriteName( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_COMMENTAIRE, _signalement.getCommentaire( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_LIEN_CONSULTATION, getLienConsultation( _signalement ) ) );

        // Traitement nécessaire

        // Alias signalement
        String aliasType = _signalement.getTypeSignalement( ).getAlias( );
        if ( null == aliasType )
        {
            aliasType = StringUtils.EMPTY;
        }
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_ALIAS_ANOMALIE, aliasType ) );

        // Alias mobile signalement
        String aliasMobileType = _signalement.getTypeSignalement( ).getAliasMobile( );
        if ( null == aliasMobileType )
        {
            aliasMobileType = StringUtils.EMPTY;
        }
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_ALIAS_MOBILE_ANOMALIE, aliasMobileType ) );

        // Date prévue de traiement
        if ( StringUtils.isNotBlank( _signalement.getDatePrevueTraitement( ) ) )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_PROGRAMMATION, _signalement.getDatePrevueTraitement( ) ) );
        } else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_PROGRAMMATION, StringUtils.EMPTY ) );
        }

        // Date de traitement
        String dateDeTraitement = _signalement.getDateServiceFaitTraitement( );
        if ( StringUtils.isNotBlank( dateDeTraitement ) )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_DE_TRAITEMENT, dateDeTraitement ) );
        } else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_DE_TRAITEMENT, StringUtils.EMPTY ) );
        }

        // Heure de traitement
        String heureDeTraitement = _signalement.getHeureServiceFaitTraitement( );
        if ( StringUtils.isNotBlank( heureDeTraitement ) )
        {
            heureDeTraitement = heureDeTraitement.substring( 0, 2 ) + ":" + heureDeTraitement.substring( 2 );
        } else
        {
            heureDeTraitement = StringUtils.EMPTY;
        }
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_HEURE_DE_TRAITEMENT, heureDeTraitement ) );

        // Date d'envoi
        String dateEnvoi = _signalement.getDateCreation( );
        if ( StringUtils.isNotBlank( dateEnvoi ) )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_ENVOI, dateEnvoi ) );
        } else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_ENVOI, StringUtils.EMPTY ) );
        }

        // Heure d'envoi
        Date heureEnvoiTmstp = _signalement.getHeureCreation( );
        if ( null != heureEnvoiTmstp )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_HEURE_ENVOI, DateUtils.getHourWithSecondsFr( heureEnvoiTmstp ) ) );
        } else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_HEURE_ENVOI, StringUtils.EMPTY ) );
        }

        // Email de l'usager
        List<Signaleur> signaleurs = _signalement.getSignaleurs( );
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
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_EMAIL_USAGER, emailUsager ) );

        // Précisions adresse
        if ( _signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) != null )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_PRECISION, _signalement.getAdresses( ).get( 0 ).getPrecisionLocalisation( ) ) );
        } else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_PRECISION, "" ) );
        }

        // Raisons rejets
        List<ObservationRejet> motifsRejet = _signalement.getObservationsRejet( );

        // Construction de la variable raisons_rejet
        if ( CollectionUtils.isNotEmpty( motifsRejet ) )
        {
            StringBuilder motifsRejetStr = new StringBuilder( );
            if ( CollectionUtils.isNotEmpty( motifsRejet ) )
            {
                for ( int i = 0; i < motifsRejet.size( ); i++ )
                {
                    String motifRejet = motifsRejet.get( i ).getLibelle( );
                    motifsRejetStr.append( MOTIF_REJET_PREPEND ).append( motifRejet );
                    if ( i < ( motifsRejet.size( ) - 1 ) )
                    {
                        motifsRejetStr.append( MOTIF_REJET_SEPARATOR );
                    }
                }
            }
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_RAISONS_REJET, motifsRejetStr.toString( ) ) );
        }

        return collectionNotifyGruMarkers;
    }

    /**
     * Gives the marker descriptions
     *
     * @return the marker descritions
     */
    public static Collection<NotifyGruMarker> provideMarkerDescriptions( )
    {

        Collection<NotifyGruMarker> collectionNotifyGruMarkers = new ArrayList<>( );

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_MESSAGE, MARK_MESSAGE_DESC ) );

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_ID_ANOMALIE, MARK_ID_ANOMALIE_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_NUMERO, MARK_NUMERO_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_TYPE, MARK_TYPE_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_ALIAS_ANOMALIE, MARK_ALIAS_ANOMALIE_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_ALIAS_MOBILE_ANOMALIE, MARK_ALIAS_MOBILE_ANOMALIE_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_ADRESSE, MARK_ADRESSE_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_PRECISION, MARK_PRECISION_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_PRIORITE, MARK_PRIORITE_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_COMMENTAIRE, MARK_COMMENTAIRE_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_LIEN_CONSULTATION, MARK_LIEN_CONSULTATION_DESC ) );

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_DATE_PROGRAMMATION, MARK_DATE_PROGRAMMATION_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_DATE_DE_TRAITEMENT, MARK_DATE_DE_TRAITEMENT_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_HEURE_DE_TRAITEMENT, MARK_HEURE_DE_TRAITEMENT_DESC ) );

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_DATE_ENVOI, MARK_DATE_ENVOI_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_HEURE_ENVOI, MARK_HEURE_ENVOI_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_EMAIL_USAGER, MARK_EMAIL_USAGER_DESC ) );

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_RAISONS_REJET, MARK_RAISONS_REJET_DESC ) );

        return collectionNotifyGruMarkers;

    }

    /**
     * Creates a {@code NotifyGruMarker} object with the specified marker and value.
     *
     * @param strMarker
     *            the marker
     * @param strValue
     *            the value to inject into the {@code NotifyGruMarker} object
     * @return the {@code NotifyGruMarker} object
     */

    private static NotifyGruMarker createMarkerValues( String strMarker, String strValue )
    {
        NotifyGruMarker notifyGruMarker = new NotifyGruMarker( strMarker );
        notifyGruMarker.setValue( strValue );

        return notifyGruMarker;
    }

    /**
     * Creates a {@code NotifyGruMarker} object with the specified marker and description.
     *
     * @param strMarker
     *            the marker
     * @param strDescription
     *            the description to inject into the {@code NotifyGruMarker} object
     * @return the {@code NotifyGruMarker} object
     */
    private static NotifyGruMarker createMarkerDescriptions( String strMarker, String strDescription )
    {
        NotifyGruMarker notifyGruMarker = new NotifyGruMarker( strMarker );
        notifyGruMarker.setDescription( strDescription );

        return notifyGruMarker;
    }

    @Override
    public String provideDemandSubtypeId( )
    {
        return null;
    }

    @Override
    public String provideSmsSender( )
    {
        return null;
    }

}
