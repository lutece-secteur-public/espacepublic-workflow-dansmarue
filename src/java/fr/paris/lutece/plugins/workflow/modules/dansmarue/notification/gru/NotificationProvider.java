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
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.TaskUtils;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.IProvider;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.NotifyGruMarker;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * NotificationProvider.
 */
public class NotificationProvider implements IProvider
{

    /** The Constant MARK_MESSAGE. */
    // MARKERS
    private static final String MARK_MESSAGE = "message";

    /** The Constant MARK_NUMERO. */
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

    /** The Constant MARK_ALIAS_ANOMALIE. */
    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    /** The Constant MARK_ALIAS_MOBILE_ANOMALIE. */
    private static final String MARK_ALIAS_MOBILE_ANOMALIE = "alias_mobile_anomalie";

    /** The Constant MARK_ID_ANOMALIE. */
    private static final String MARK_ID_ANOMALIE = "id_anomalie";

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

    /** The Constant MARK_RAISONS_REJET. */
    private static final String MARK_RAISONS_REJET = "raisons_rejet";

    /** The Constant MARK_MESSAGE_DESC. */
    // DESCRIPTION
    private static final String MARK_MESSAGE_DESC = "Message associé";

    /** The Constant MARK_NUMERO_DESC. */
    private static final String MARK_NUMERO_DESC = "Numéro de l'anomalie";

    /** The Constant MARK_TYPE_DESC. */
    private static final String MARK_TYPE_DESC = "Type d'anomalie";

    /** The Constant MARK_ADRESSE_DESC. */
    private static final String MARK_ADRESSE_DESC = "Adresse de l'anomalie";

    /** The Constant MARK_PRIORITE_DESC. */
    private static final String MARK_PRIORITE_DESC = "Priorité";

    /** The Constant MARK_COMMENTAIRE_DESC. */
    private static final String MARK_COMMENTAIRE_DESC = "Commentaire";

    /** The Constant MARK_PRECISION_DESC. */
    private static final String MARK_PRECISION_DESC = "Précision de la localisation";

    /** The Constant MARK_LIEN_CONSULTATION_DESC. */
    private static final String MARK_LIEN_CONSULTATION_DESC = "Lien de consultation du message";

    /** The Constant MARK_ALIAS_ANOMALIE_DESC. */
    private static final String MARK_ALIAS_ANOMALIE_DESC = "Alias de l'anomalie";

    /** The Constant MARK_ALIAS_MOBILE_ANOMALIE_DESC. */
    private static final String MARK_ALIAS_MOBILE_ANOMALIE_DESC = "Alias mobile de l'anomalie";

    /** The Constant MARK_ID_ANOMALIE_DESC. */
    private static final String MARK_ID_ANOMALIE_DESC = "Id de l'anomalie";

    /** The Constant MARK_DATE_PROGRAMMATION_DESC. */
    private static final String MARK_DATE_PROGRAMMATION_DESC = "Date prévue du traitement de l'anomalie";

    /** The Constant MARK_DATE_DE_TRAITEMENT_DESC. */
    private static final String MARK_DATE_DE_TRAITEMENT_DESC = "Date de traitement de l'anomalie";

    /** The Constant MARK_HEURE_DE_TRAITEMENT_DESC. */
    private static final String MARK_HEURE_DE_TRAITEMENT_DESC = "Heure de traitement de l'anomalie";

    /** The Constant MARK_DATE_ENVOI_DESC. */
    private static final String MARK_DATE_ENVOI_DESC = "Date d'envoi du signalement";

    /** The Constant MARK_HEURE_ENVOI_DESC. */
    private static final String MARK_HEURE_ENVOI_DESC = "Heure d'envoi du signalement";

    /** The Constant MARK_EMAIL_USAGER_DESC. */
    private static final String MARK_EMAIL_USAGER_DESC = "Email de l'usager";

    /** The Constant MARK_RAISONS_REJET_DESC. */
    private static final String MARK_RAISONS_REJET_DESC = "Raisons du rejet";

    /** The Constant MOTIF_REJET_PREPEND. */
    // REJET
    private static final String MOTIF_REJET_PREPEND = "- ";

    /** The Constant MOTIF_REJET_SEPARATOR. */
    private static final String MOTIF_REJET_SEPARATOR = "<br/>";

    /** The Constant PARAMETER_PAGE. */
    // PARAMETERS
    private static final String PARAMETER_PAGE = "page";

    /** The Constant PARAMETER_INSTANCE. */
    private static final String PARAMETER_INSTANCE = "instance";

    /** The Constant PARAMETER_INSTANCE_VALUE. */
    private static final String PARAMETER_INSTANCE_VALUE = "signalement";

    /** The Constant PARAMETER_SUIVI. */
    private static final String PARAMETER_SUIVI = "suivi";

    /** The Constant PARAMETER_TOKEN. */
    private static final String PARAMETER_TOKEN = "token";

    /** The Constant PROPERTY_BASE_URL. */
    // PROPERTIES
    private static final String PROPERTY_BASE_URL = "lutece.ts.prod.url";

    /** The Constant JSP_PORTAL. */
    // JSP
    private static final String JSP_PORTAL = "jsp/site/Portal.jsp";

    /** The signalement service. */
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The signalement workflow service. */
    private IWorkflowService _signalementWorkflowService = SpringContextService.getBean( "signalement.workflowService" );

    /** The signalement. */
    private Signalement _signalement;

    /** The signaleur. */
    private Signaleur _signaleur;

    /** The message. */
    private String _message;

    /** The str demande type id. */
    private String _strDemandeTypeId = AppPropertiesService.getProperty( "dmr-signalement.guichet.id.type.demande" );

    private static final String URL_SONDAGE_DEMANDE = "sitelabels.site_property.message.url.sondage.demande";

    private static final String URL_SONDAGE_SERVICE = "sitelabels.site_property.message.url.sondage.sevice";

    private static final String MARK_URL_SONDAGE_DEMANDE = "urlSondageDemande";

    private static final String MARK_URL_SONDAGE_SERVICE = "urlSondageService";

    private static final String MARK_URL_SONDAGE_DEMANDE_DESC = "Url du sondage de demande";

    private static final String MARK_URL_SONDAGE_SERVICE_DESC = "Url du sondage de service";

    private static final String MARK_CP = "code_postal";

    private static final String MARK_CP_DESC = "Code postal";

    private static final String MARK_ID_TYPO = "id_type";

    private static final String MARK_ID_TYPO_DESC = "Id du type de l'anomalie";

    private static final String MARK_ARRONDISSEMENT = "arrondissement";

    /**
     * Constructor.
     *
     * @param resourceHistory
     *            the resourceHistory
     */
    public NotificationProvider( ResourceHistory resourceHistory )
    {
        _signalement = _signalementService.getSignalementWithFullPhoto( resourceHistory.getIdResource( ) );

        _message = ( _signalementWorkflowService.selectMultiContentsMessageNotification( resourceHistory.getId( ) ) );

        if ( ( _signalement != null ) && CollectionUtils.isNotEmpty( _signalement.getSignaleurs( ) ) )
        {
            _signaleur = _signalement.getSignaleurs( ).get( 0 );
        }

    }

    /**
     * Get The Signaleur.
     *
     * @return Signaleur
     */
    public Signaleur getSignaleur( )
    {
        return _signaleur;
    }

    /**
     * Provide customer connection id.
     *
     * @return the string
     */
    @Override
    public String provideCustomerConnectionId( )
    {
        return _signaleur.getGuid( );
    }

    /**
     * Provide customer email.
     *
     * @return the string
     */
    @Override
    public String provideCustomerEmail( )
    {
        return _signaleur.getMail( );
    }

    /**
     * Provide customer id.
     *
     * @return the string
     */
    @Override
    public String provideCustomerId( )
    {
        return String.valueOf( _signaleur.getId( ) );
    }

    /**
     * Provide customer mobile phone.
     *
     * @return the string
     */
    @Override
    public String provideCustomerMobilePhone( )
    {
        return _signaleur.getIdTelephone( );
    }

    /**
     * Provide demand id.
     *
     * @return the string
     */
    @Override
    public String provideDemandId( )
    {
        return String.valueOf( _signalement.getId( ) );
    }

    /**
     * Provide demand reference.
     *
     * @return the string
     */
    @Override
    public String provideDemandReference( )
    {
        return _signalement.getNumeroSignalement( );
    }

    /**
     * Provide demand type id.
     *
     * @return the string
     */
    @Override
    public String provideDemandTypeId( )
    {
        return _strDemandeTypeId;
    }

    /**
     * Get the link of the "consultation page" (front office signalement).
     *
     * @param signalement
     *            the signalement
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

    /**
     * Provide marker values.
     *
     * @return the collection
     */
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
        }
        else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_PROGRAMMATION, StringUtils.EMPTY ) );
        }

        // Date de traitement
        String dateDeTraitement = _signalement.getDateServiceFaitTraitement( );
        if ( StringUtils.isNotBlank( dateDeTraitement ) )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_DE_TRAITEMENT, dateDeTraitement ) );
        }
        else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_DE_TRAITEMENT, StringUtils.EMPTY ) );
        }

        // Heure de traitement
        String heureDeTraitement = _signalement.getHeureServiceFaitTraitement( );
        if ( StringUtils.isNotBlank( heureDeTraitement ) )
        {
            heureDeTraitement = heureDeTraitement.substring( 0, 2 ) + ":" + heureDeTraitement.substring( 2 );
        }
        else
        {
            heureDeTraitement = StringUtils.EMPTY;
        }
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_HEURE_DE_TRAITEMENT, heureDeTraitement ) );

        // Date d'envoi
        String dateEnvoi = _signalement.getDateCreation( );
        if ( StringUtils.isNotBlank( dateEnvoi ) )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_ENVOI, dateEnvoi ) );
        }
        else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_DATE_ENVOI, StringUtils.EMPTY ) );
        }

        // Heure d'envoi
        Date heureEnvoiTmstp = _signalement.getHeureCreation( );
        if ( null != heureEnvoiTmstp )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_HEURE_ENVOI, DateUtils.getHourWithSecondsFr( heureEnvoiTmstp ) ) );
        }
        else
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
        }
        else
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

        collectionNotifyGruMarkers.add( createMarkerValues( MARK_URL_SONDAGE_DEMANDE, DatastoreService.getDataValue( URL_SONDAGE_DEMANDE, "" ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_URL_SONDAGE_SERVICE, DatastoreService.getDataValue( URL_SONDAGE_SERVICE, "" ) ) );

        String codePostal = _signalement.getAdresses( ) != null && _signalement.getAdresses( ).get( 0 ) != null
                && _signalement.getAdresses( ).get( 0 ).getAdresse( ) != null ? TaskUtils.getCPFromAdresse( _signalement.getAdresses( ).get( 0 ).getAdresse( ) )
                        : "";
        collectionNotifyGruMarkers.add( createMarkerValues( MARK_CP, codePostal ) );

        if ( _signalement.getTypeSignalement( ) != null && _signalement.getTypeSignalement().getId()  != null )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_ID_TYPO, Integer.toString( _signalement.getTypeSignalement( ).getId() ) ) );
        }
        else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_ID_TYPO, "") );
        }

        if ( _signalement.getArrondissement( ) != null && _signalement.getArrondissement( ).getId( ) != null )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_ARRONDISSEMENT, String.valueOf( _signalement.getArrondissement( ).getId( ) ) ) );
        }
        else
        {
            collectionNotifyGruMarkers.add( createMarkerValues( MARK_ARRONDISSEMENT, "" ) );
        }

        return collectionNotifyGruMarkers;
    }

    /**
     * Gives the marker descriptions.
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

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_URL_SONDAGE_DEMANDE, MARK_URL_SONDAGE_DEMANDE_DESC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_URL_SONDAGE_SERVICE, MARK_URL_SONDAGE_SERVICE_DESC ) );

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_CP, MARK_CP_DESC ) );

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_ID_TYPO, MARK_ID_TYPO_DESC ) );

        collectionNotifyGruMarkers.add( createMarkerDescriptions( MARK_ARRONDISSEMENT, MARK_ARRONDISSEMENT ) );

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

    /**
     * Provide demand subtype id.
     *
     * @return the string
     */
    @Override
    public String provideDemandSubtypeId( )
    {
        return null;
    }

    /**
     * Provide sms sender.
     *
     * @return the string
     */
    @Override
    public String provideSmsSender( )
    {
        return null;
    }

}
