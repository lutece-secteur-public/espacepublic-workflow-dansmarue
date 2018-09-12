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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationsignalement.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementRequalification;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignaleurService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.leaflet.modules.dansmarue.entities.Address;
import fr.paris.lutece.plugins.leaflet.modules.dansmarue.service.IAddressSuggestPOIService;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.unit.IUnitSiraService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.TaskUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationDTO;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 *
 * RequalificationSignalementComponent
 *
 */
public class RequalificationSignalementComponent extends AbstractTaskComponent
{
    private static final String TEMPLATE_TASK_FORM = "admin/plugins/workflow/modules/signalement/task_requalification_signalement_form.html";

    //MESSAGES
    private static final String MESSAGE_ERROR_ADRESSE_NULL = "module.workflow.dansmarue.requalification.adresseNull";
    private static final String MESSAGE_ERROR_ADRESSE_HORS_PARIS = "module.workflow.dansmarue.requalification.horsParis";
    private static final String MESSAGE_ERROR_TYPE_SIGNALEMENT_NULL = "module.workflow.dansmarue.requalification.typeNull";
    private static final String MESSAGE_ERROR_SECTEUR_NULL = "module.workflow.dansmarue.requalification.secteurNull";

    //MARKERS
    public static final String MARK_PHOTOS = "photos";
    public static final String MARK_ADRESSE = "adresse";
    public static final String MARK_SIGNALEUR = "signaleur";
    public static final String MARK_PROPOSED_ADDRESSES = "proposedAddresses";
    public static final String MARK_NO_VALID_ADDRESSES = "noValidAddresses";
    public static final String MARK_NEXT = "next";
    private static final String MARK_SIGNALEMENT = "signalement";
    private static final String MARK_STATE_SIGNALEMENT = "stateSignalement";
    private static final String MARK_TYPE_LISTE = "type_list";
    private static final String MARK_KEY_MAPS = "key_maps";
    private static final String MARK_SECTEUR_LIST = "secteur_list";
    private static final String MARK_ISREQUALIFICATION_PRESTATAIRE = "isRequalificationPrestataire";
    private static final String MARK_ISROADMAP = "isRoadMap";
    private static final String MARK_SIGNALEMENT_REQUALIFICATION = "signalement_requalification";

    //PARAM
    private static final String PARAM_ID_SECTOR = "hiddenIdSector";

    //CONSTANTS
    private static final String ADRESSE_NULL = "";

    //PROPERTIES
    private static final String PROPERTY_UNITS_RADIUS = "signalement.near.units.radius";
    private static final int ID_WORKFLOW_ACTION_78 = 78;
    private static final int ID_WORKFLOW_ACTION_79 = 79;

    private static final String TEMPLATE_TASK_SIGNALEMENT_REQUALIFICATION_INFORMATION = "admin/plugins/workflow/modules/signalement/task_signalement_requalification_information.html";

    //SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;
    @Inject
    @Named( "typeSignalementService" )
    private ITypeSignalementService _typeSignalementService;
    @Inject
    @Named( "adresseSignalementService" )
    private IAdresseService _adresseService;
    @Inject
    @Named( "signalement.workflowService" )
    private IWorkflowService _signalementWorkflowService;
    @Inject
    @Named( "photoService" )
    private IPhotoService _photoService;
    @Inject
    @Named( "signaleurService" )
    private ISignaleurService _signaleurService;
    @Inject
    @Named("unittree-dansmarue.sectorService")
    private ISectorService _sectorService;
    @Inject
    @Named("unittree-dansmarue.unitSiraService")
    private IUnitSiraService _unitSiraService;
    @Inject
    @Named("addressSuggestPOIService")
    private IAddressSuggestPOIService _addressSuggestPOIService;

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request,
            Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        RequalificationDTO requalifStep = (RequalificationDTO) request.getSession( ).getAttribute( "requalifStep" );
        RequalificationDTO requalifStep2 = (RequalificationDTO) request.getSession( ).getAttribute( "requalifStep2" );
        request.getSession( ).removeAttribute( "requalifStep" );
        request.getSession( ).removeAttribute( "requalifStep2" );

        // If search or change address
        if ( requalifStep != null )
        {
            if ( requalifStep.getAdresseRequalif( ) != null )
            {
                //get all the addresses proposed by "base adresse" when the user clicks on search
                List<Address> result = _addressSuggestPOIService.getAddressItem(requalifStep.getAdresseRequalif());
                if(CollectionUtils.isNotEmpty(result)){
                    model.put( MARK_PROPOSED_ADDRESSES, result );
                }else{
                    model.put( MARK_PROPOSED_ADDRESSES, null );
                    model.put( MARK_NO_VALID_ADDRESSES, MARK_NO_VALID_ADDRESSES );
                }
            }
        }

        Signalement signalement = _signalementService.getSignalement( nIdResource );

        // get the signalement
        List<PhotoDMR> photos = _photoService.findBySignalementId( nIdResource );
        signalement = _signalementService.getSignalement( nIdResource );
        Adresse adresse = _adresseService.loadByIdSignalement( nIdResource );
        Signaleur signaleur = _signaleurService.loadByIdSignalement( nIdResource );

        // ACCESSIBILITY : If valid proposed address
        if ( requalifStep2 != null )
        {
            if ( requalifStep2.getAdresseRequalif( ) != null )
            {
                signalement.setTypeSignalement( _typeSignalementService.getByIdTypeSignalement( requalifStep2
                        .getTypeSignalement( ) ) );
                signalement.getAdresses( ).get( 0 ).setAdresse( requalifStep2.getAdresseRequalif( ) );
                signalement.getAdresses( ).get( 0 ).setLat( requalifStep2.getLat( ) );
                signalement.getAdresses( ).get( 0 ).setLng( requalifStep2.getLng( ) );
                adresse.setAdresse( requalifStep2.getAdresseRequalif( ) );
                adresse.setLat( requalifStep2.getLat( ) );
                adresse.setLng( requalifStep2.getLng( ) );

            }
        }
        if ( requalifStep != null )
        {
            if ( requalifStep.getAdresseRequalif( ) != null )
            {
                signalement.setTypeSignalement( _typeSignalementService.getByIdTypeSignalement( requalifStep
                        .getTypeSignalement( ) ) );
                signalement.getAdresses( ).get( 0 ).setAdresse( requalifStep.getAdresseRequalif( ) );
                signalement.getAdresses( ).get( 0 ).setLat( requalifStep.getLat( ) );
                signalement.getAdresses( ).get( 0 ).setLng( requalifStep.getLng( ) );
                adresse.setAdresse( requalifStep.getAdresseRequalif( ) );
                adresse.setLat( requalifStep.getLat( ) );
                adresse.setLng( requalifStep.getLng( ) );

            }
        }

        if(( adresse.getLng() > 0 )  && ( adresse.getLat() > 0 ) && ( signalement.getTypeSignalement() != null )){
            //Entite calculée par défaut de l'anomalie
            Unit defaultUnit = _unitSiraService.findUnitByGeomAndTypeSignalement(adresse.getLng(), adresse.getLat(), signalement.getTypeSignalement().getId());
            //Si défault unit == null, ce qui veut dire pas de secteur correspondant au type et aux coordonnées, c'est du DEVE
            if(defaultUnit == null){
                defaultUnit = _signalementService.getMajorUnit(signalement.getTypeSignalement().getId(),adresse.getLng(), adresse.getLat());
            }
            //Récupération des secteurs liés à cette direction et celle de DEVE
            List<Integer> idUnits = new ArrayList<>();
            idUnits.add(defaultUnit.getIdUnit());
            idUnits.add(Integer.parseInt(SignalementConstants.UNIT_DEVE));

            List<Sector> sectors = _sectorService.findSectorsByDirectionsAndGeom(adresse.getLng(), adresse.getLat(), AppPropertiesService.getPropertyInt(PROPERTY_UNITS_RADIUS, 0),idUnits);
            ReferenceList sectorList = ListUtils.toReferenceList( sectors, "idSector", "name", "", false );
            model.put( MARK_SECTEUR_LIST, sectorList);
        }

        model.put( MARK_SIGNALEMENT, signalement );
        model.put( MARK_PHOTOS, photos );
        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_SIGNALEUR, signaleur );
        model.put( MARK_NEXT, request.getParameter( MARK_NEXT ) );
        model.put( MARK_ISROADMAP, request.getParameter( MARK_ISROADMAP ) );
        model.put( "locale", Locale.FRANCE );

        model.put( MARK_KEY_MAPS, SignalementConstants.GOOGLE_MAPS_API_KEY );


        // get the signalement's state
        WorkflowService workflowService = WorkflowService.getInstance( );
        State stateSignalement = workflowService.getState( signalement.getId( ).intValue( ),
                Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ), null );
        model.put( MARK_STATE_SIGNALEMENT, stateSignalement );

        //get all the type signalement
        List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActifLinkedToUnit( );
        ReferenceList listeTypes = ListUtils.toReferenceList( types, "id", "formatTypeSignalement", "", false );
        model.put( MARK_TYPE_LISTE, listeTypes );

        model.put( "locale", Locale.FRANCE );

        //DMR 127 pour la requalification des anomalies à l'état Transféré à un prestataire et à l'état Service programmé prestataire on ne doit pas pouvoir modifier l'adresse et le secteur
        boolean isRequalificationPrestataire = ( task.getAction( ) != null ) && (( task.getAction( ).getId( ) == ID_WORKFLOW_ACTION_78 ) || ( task.getAction( ).getId( ) == ID_WORKFLOW_ACTION_79 ));
        model.put( MARK_ISREQUALIFICATION_PRESTATAIRE, isRequalificationPrestataire );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FORM, locale, model );


        return template.getHtml( );
    }

    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        SignalementRequalification signalementRequalifiation = _signalementService.getSignalementRequalificationByTaskHistory( nIdHistory, task.getId( ) );

        Map<String, Object> model = new HashMap<String, Object>( );

        if( signalementRequalifiation.getIdSignalement( ) != null ) {
            model.put( MARK_SIGNALEMENT_REQUALIFICATION, signalementRequalifiation );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_SIGNALEMENT_REQUALIFICATION_INFORMATION, locale,
                model );

        return template.getHtml( );
    }

    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
            ITask task )
    {
        RequalificationDTO requalification = new RequalificationDTO( );
        SignalementUtils.populate( requalification, request );

        Set<ConstraintViolation<RequalificationDTO>> errors = BeanValidationUtil.validate( requalification );
        if ( !errors.isEmpty( ) )
        {
            return TaskUtils.getValidationErrorsMessage( requalification, locale, errors, request );
        }

        // teste si une adresse a bien été saisie
        if ( requalification.getAdresseRequalif( ).equals( ADRESSE_NULL ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ADRESSE_NULL, AdminMessage.TYPE_STOP );
        }

        // et si elle est dans paris
        if ( StringUtils.isBlank( request.getParameter( "searchAddress" ) )
                && StringUtils.isBlank( request.getParameter( "validAddress" ) )
                && ( _adresseService.getArrondissementByGeom( requalification.getLng( ), requalification.getLat( ) ) == null ) )
        {
            return AdminMessageService
                    .getMessageUrl( request, MESSAGE_ERROR_ADRESSE_HORS_PARIS, AdminMessage.TYPE_STOP );
        }

        // teste si un type signalement a bien été choisi sinon erreur
        if ( requalification.getTypeSignalement( ) < 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_TYPE_SIGNALEMENT_NULL,
                    AdminMessage.TYPE_STOP );
        }


        //ACCESSIBILITY
        if ( request.getParameter( "access" ) != null )
        {
            String addressLoad = request.getParameter( "adresseLoad" );
            String address = request.getParameter( "adresseRequalif" );
            if ( StringUtils.isNotBlank( request.getParameter( "searchAddress" ) )
                    || ( StringUtils.isNotBlank( addressLoad ) && StringUtils.isNotBlank( address ) && !address
                            .equals( addressLoad ) ) )
            {
                requalification.setLat( 0.0 );
                requalification.setLng( 0.0 );
                requalification.setAdresseRequalif( address );
                request.getSession( ).setAttribute( "requalifStep", requalification );

                String next = request.getParameter( "next" );
                String isRoadMap = request.getParameter( MARK_ISROADMAP );

                return AppPathService.getBaseUrl( request )
                        + "jsp/admin/plugins/signalement/WorkflowAction.jsp?action_id=" + task.getAction( ).getId( )
                        + "&signalement_id=" + requalification.getIdSignalement( ) + "&next=" + next + "&isRoadMap="
                        + isRoadMap;
            }

            // ACCESSIBILITY
            if ( StringUtils.isNotBlank( request.getParameter( "validAddress" ) ) )
            {

                String allParameter = request.getParameter( "validAddress" );

                String delimiter = "/";

                String[] temp = allParameter.split( delimiter );

                // get the address libel
                String libelAddress = temp[0].toLowerCase( );

                // get the lat/lng in lambert 27561
                String strLat = temp[1];
                String strLng = temp[2];

                Double dLat = Double.parseDouble( strLat );
                Double dLng = Double.parseDouble( strLng );

                //transform the lambert coordinates to WGS84 for the database
                Double[] geom = _signalementService.getGeomFromLambertToWgs84( dLat, dLng );

                requalification.setLat( geom[1] );
                requalification.setLng( geom[0] );
                requalification.setAdresseRequalif( libelAddress );

                request.getSession( ).setAttribute( "requalifStep2", requalification );
                String next = request.getParameter( "next" );
                String isRoadMap = request.getParameter( "isRoadMap" );

                return AppPathService.getBaseUrl( request )
                        + "jsp/admin/plugins/signalement/WorkflowAction.jsp?action_id=" + task.getAction( ).getId( )
                        + "&signalement_id=" + requalification.getIdSignalement( ) + "&next=" + next + "&isRoadMap="
                        + isRoadMap;
            }
        }

        boolean isRequalificationPrestataire = ( task.getAction( ) != null ) && (( task.getAction( ).getId( ) == ID_WORKFLOW_ACTION_78 ) || ( task.getAction( ).getId( ) == ID_WORKFLOW_ACTION_79 ));
        if(isRequalificationPrestataire) {
            Integer idSector = Integer.valueOf( request.getParameter( PARAM_ID_SECTOR ) );
            requalification.setSector( idSector);
            request.getSession( ).setAttribute( PARAM_ID_SECTOR , idSector );
        }

        // teste si un secteur a bien été choisi sinon erreur
        if (( requalification.getSector( ) == null ) || ( requalification.getSector( ) < 0 ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_SECTEUR_NULL,
                    AdminMessage.TYPE_STOP );
        }

        return null;
    }

}
