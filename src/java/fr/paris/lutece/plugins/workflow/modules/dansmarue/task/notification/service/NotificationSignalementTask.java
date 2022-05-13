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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.url.UrlItem;

/**
 * NotificationSignalementTask class.
 */
public class NotificationSignalementTask extends AbstractSignalementTask
{

    /** The Constant PROPERTY_BASE_URL. */
    // PROPERTIES
    private static final String PROPERTY_BASE_URL = "lutece.prod.url";

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
    private static final String MARK_COMMENTAIRE = "description";

    /** The Constant MARK_PRECISION. */
    private static final String MARK_PRECISION = "precision";

    /** The Constant MARK_LIEN_CONSULT. */
    private static final String MARK_LIEN_CONSULT = "lien";

    /** The Constant MARK_LIEN_SIGNALEMENT_WS. */
    private static final String MARK_LIEN_SIGNALEMENT_WS = "wsSignalement";

    /** The Constant MARK_DATE_ENVOI. */
    private static final String MARK_DATE_ENVOI = "dateEnvoi";

    /** The Constant MARK_HEURE_ENVOI. */
    private static final String MARK_HEURE_ENVOI = "heureEnvoi";

    /** The Constant MARK_EMAIL_USAGER. */
    private static final String MARK_EMAIL_USAGER = "emailUsager";

    /** The Constant MARK_ID_ANOMALIE. */
    private static final String MARK_ID_ANOMALIE = "id_anomalie";

    /** The Constant MARK_ALIAS_ANOMALIE. */
    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    /** The Constant PARAMETER_ID_SIGNALEMENT. */
    // PARAMETERS
    private static final String PARAMETER_ID_SIGNALEMENT = "signalement_id";

    /** The Constant PARAMETER_PAGE. */
    private static final String PARAMETER_PAGE = "page";

    /** The Constant PARAMETER_ID. */
    private static final String PARAMETER_ID = "id";

    /** The Constant PARAMETER_TOKEN. */
    private static final String PARAMETER_TOKEN = "token";

    /** The Constant JSP_VIEW_SIGNALEMENT. */
    // JSP
    private static final String JSP_VIEW_SIGNALEMENT = "jsp/admin/plugins/signalement/ViewSignalement.jsp";

    /** The Constant JSP_MANAGE_SIGNALEMENT_WITHOUT_WS. */
    private static final String JSP_MANAGE_SIGNALEMENT_WITHOUT_WS = "jsp/site/Portal.jsp";

    /** The unit service. */
    // SERVICES
    private IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    /** The notification signalement task config service. */
    private NotificationSignalementTaskConfigService _notificationSignalementTaskConfigService = SpringContextService
            .getBean( "signalement.notificationSignalementTaskConfigService" );

    /** The signalement service. */
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

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

        if ( null == signalement )
        {
            return;
        }

        NotificationSignalementTaskConfigDTO configDTOUnit = _notificationSignalementTaskConfigService.findByPrimaryKey( getId( ) );
        NotificationSignalementTaskConfigDTO configDTOType = _notificationSignalementTaskConfigService.findByPrimaryKeyWithTypeSignalement( getId( ) );

        // Check if the signalement type is not TypeEncombrant
        if ( signalement.getSecteur( ) != null )
        {

            List<String> listeAdressesEmail = getAdressesEmailANotifier( signalement.getSecteur( ).getIdSector( ), configDTOUnit, configDTOType, signalement );
            if ( !listeAdressesEmail.isEmpty( ) )
            {
                // Obtention des informations de l'email par remplacement des eventuelles balises freemarkers
                // ==> ajout des données pouvant être demandées (
                // correspondant à la map "balises" dans
                // getDisplayConfigForm(...) )
                Map<String, Object> emailModel = new HashMap<>( );
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

                emailModel.put( MARK_LIEN_CONSULT, getLienConsultSignalement( signalement ) );
                emailModel.put( MARK_LIEN_SIGNALEMENT_WS, getLienWsSignalement( signalement ) );

                if ( CollectionUtils.isNotEmpty( signalement.getAdresses( ) ) )
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
                    emailModel.put( MARK_PRECISION, "" );
                }
                emailModel.put( MARK_PRIORITE, signalement.getPrioriteName( ) );
                emailModel.put( MARK_COMMENTAIRE, signalement.getCommentaire( ) );

                // Email de l'usager
                List<Signaleur> signaleurs = signalement.getSignaleurs( );
                String emailUsager = StringUtils.EMPTY;
                if ( CollectionUtils.isNotEmpty( signaleurs ) )
                {
                    for ( Signaleur signaleur : signaleurs )
                    {
                        if ( StringUtils.isNotEmpty( signaleur.getMail( ) ) )
                        {
                            emailUsager = signaleur.getMail( );
                        }
                    }
                }
                emailModel.put( MARK_EMAIL_USAGER, emailUsager );

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

                List<PhotoDMR> photos = signalement.getPhotos( );
                List<FileAttachment> files = new ArrayList<>( );

                for ( PhotoDMR photo : photos )
                {
                    if ( ( photo.getImage( ) != null ) && ( photo.getImage( ).getImage( ) != null ) )
                    {

                        String [ ] mime = photo.getImage( ).getMimeType( ).split( "/" );

                        if ( photo.getVue( ).intValue( ) == SignalementConstants.OVERVIEW )
                        {
                            files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_ENSEMBLE_PJ + mime [1], photo.getImage( ).getImage( ),
                                    photo.getImage( ).getMimeType( ) ) );
                        }
                        else
                            if ( photo.getVue( ).intValue( ) == SignalementConstants.SERVICE_DONE_VIEW )
                            {
                                files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_SERVICE_FAIT_PJ + mime [1], photo.getImage( ).getImage( ),
                                        photo.getImage( ).getMimeType( ) ) );
                            }
                            else
                            {
                                files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_PRES_PJ + mime [1], photo.getImage( ).getImage( ),
                                        photo.getImage( ).getMimeType( ) ) );
                            }
                    }
                }

                if ( StringUtils.isNotBlank( configDTOUnit.getMessage( ) ) && StringUtils.isNotBlank( configDTOUnit.getSubject( ) )
                        && StringUtils.isNotBlank( configDTOUnit.getSender( ) ) )
                {
                    String message = AppTemplateService.getTemplateFromStringFtl( configDTOUnit.getMessage( ), locale, emailModel ).getHtml( );
                    String subject = AppTemplateService.getTemplateFromStringFtl( configDTOUnit.getSubject( ), locale, emailModel ).getHtml( );
                    for ( String email : listeAdressesEmail )
                    {
                        MailService.sendMailMultipartHtml( email, null, null, configDTOUnit.getSender( ),
                                AppPropertiesService.getProperty( "mail.noreply.email", "noreply-dansmarue@paris.fr" ), subject, message, null, files );
                    }
                }
            }

        }
    }

    /**
     * Get the link to manage signalement without WebServices.
     *
     * @param signalement
     *            the signalement
     * @return the link
     */
    private String getLienWsSignalement( Signalement signalement )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( JSP_MANAGE_SIGNALEMENT_WITHOUT_WS );

        urlItem.addParameter( PARAMETER_PAGE, "webService" );
        urlItem.addParameter( PARAMETER_ID, signalement.getId( ).intValue( ) );
        urlItem.addParameter( PARAMETER_TOKEN, signalement.getToken( ) );

        return urlItem.getUrl( );

    }

    /**
     * Return the list of email to notify for a given id sector and configuration.
     *
     * @param idSector
     *            the sector id
     * @param configDTOUnit
     *            the config DTO unit
     * @param configDTOTypeSignalement
     *            the config DTO type signalement
     * @param signalement
     *            the signalement
     * @return the list of email
     */
    private List<String> getAdressesEmailANotifier( int idSector, NotificationSignalementTaskConfigDTO configDTOUnit,
            NotificationSignalementTaskConfigDTO configDTOTypeSignalement, Signalement signalement )
    {
        List<String> resultList = new ArrayList<>( );

        List<Unit> unitsLinkedToTheSector = _unitService.findBySectorId( idSector );
        List<Unit> unitsToNotify = _notificationSignalementTaskConfigService.getListUnitToNotify( configDTOUnit );

        List<TypeSignalement> typesToNotify = _notificationSignalementTaskConfigService.getListTypeToNotify( configDTOTypeSignalement, signalement );

        Set<Unit> unitsToNotifySector = new HashSet<>( );
        for ( Unit unitSector : unitsLinkedToTheSector )
        {
            if ( contains( unitsToNotify, unitSector ) )
            {
                unitsToNotifySector.add( unitSector );
            }
        }

        for ( Unit unitToNotify : unitsToNotifySector )
        {
            // récupérer la configUnit, puis le(s) destinataire(s)
            NotificationSignalementTaskConfigUnit configUnit = _notificationSignalementTaskConfigService.findUnitByPrimaryKey( getId( ),
                    unitToNotify.getIdUnit( ) );

            if ( configUnit != null )
            {
                String strDestinataires = configUnit.getDestinataires( );
                String [ ] tabDestinataires = strDestinataires.split( ";" );
                for ( String email : tabDestinataires )
                {
                    if ( !resultList.contains( email.trim( ) ) )
                    {
                        resultList.add( email.trim( ) );
                    }
                }
            }

        }

        for ( TypeSignalement typeToNotify : typesToNotify )
        {
            // récupérer la configType, puis le(s) destinataire(s)
            NotificationSignalementTaskConfigUnit configType = _notificationSignalementTaskConfigService.findByIdTypeSignalement( getId( ),
                    typeToNotify.getId( ) );

            if ( configType != null )
            {
                String strDestinataires = configType.getDestinataires( );
                String [ ] tabDestinataires = strDestinataires.split( ";" );
                for ( String email : tabDestinataires )
                {
                    if ( !resultList.contains( email.trim( ) ) )
                    {
                        resultList.add( email.trim( ) );
                    }
                }

            }

        }

        return resultList;
    }

    // Ecriture de cette méthode car la méthode de la classe List ne se base pas su les valeurs des parametres
    /**
     * Return true if the given list contains the given unit.
     *
     * @param listUnits
     *            the listUnits
     * @param unit
     *            the unit
     * @return true or false
     */
    private Boolean contains( List<Unit> listUnits, Unit unit )
    {
        for ( Unit u : listUnits )
        {
            if ( ( u.getIdUnit( ) == unit.getIdUnit( ) ) && ( u.getIdParent( ) == unit.getIdParent( ) ) && u.getLabel( ).equals( unit.getLabel( ) )
                    && u.getDescription( ).equals( unit.getDescription( ) ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Do remove config.
     */
    @Override
    public void doRemoveConfig( )
    {
        _notificationSignalementTaskConfigService.delete( getId( ) );
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
        return "Notification par email";
    }

    /**
     * Return the url to the viewSignalement jsp for the given signalement.
     *
     * @param signalement
     *            the signalement
     * @return the url
     */
    private String getLienConsultSignalement( Signalement signalement )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( JSP_VIEW_SIGNALEMENT );

        urlItem.addParameter( PARAMETER_ID_SIGNALEMENT, signalement.getId( ).intValue( ) );

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
