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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationsignalement.service;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationDTO;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;

/**
 * RequalificationSignalemenTask class.
 */
public class RequalificationSignalementTask extends AbstractSignalementTask
{

    /** The Constant TASK_TITLE. */
    private static final String TASK_TITLE = "Requalification du signalement";

    /** The Constant PARAMETER_WEBSERVICE_ID_TYPE_ANOMALIE. */
    // PARAMETER
    private static final String PARAMETER_WEBSERVICE_ID_TYPE_ANOMALIE = "idTypeAnomalie";

    /** The Constant PARAM_ID_SECTOR. */
    private static final String PARAM_ID_SECTOR = "hiddenIdSector";

    /** The Constant PARAM_COMMENTAIRE_AGENT_TERRAIN. */
    private static final String PARAM_COMMENTAIRE_AGENT_TERRAIN = "commentaireAgentTerrain";

    /** The Constant MARK_PHOTOS. */
    // MARKERS
    public static final String MARK_PHOTOS = "photos";

    /** The Constant MARK_ADRESSE. */
    public static final String MARK_ADRESSE = "adresse";

    /** The Constant MARK_SIGNALEUR. */
    public static final String MARK_SIGNALEUR = "signaleur";

    /** The Constant MARK_PROPOSED_ADDRESSES. */
    public static final String MARK_PROPOSED_ADDRESSES = "proposedAddresses";

    /** The Constant MARK_NO_VALID_ADDRESSES. */
    public static final String MARK_NO_VALID_ADDRESSES = "noValidAddresses";

    /** The Constant EMPTY_STRING. */
    // CONSTANTS
    private static final String EMPTY_STRING = "";

    /** The signalement service. */
    // SERVICES
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The type signalement service. */
    private ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /** The adresse service. */
    private IAdresseService _adresseService = SpringContextService.getBean( "adresseSignalementService" );

    /** The signalement workflow service. */
    private IWorkflowService _signalementWorkflowService = SpringContextService.getBean( "signalement.workflowService" );

    /** The sector service. */
    private ISectorService _sectorService = SpringContextService.getBean( "unittree-dansmarue.sectorService" );

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
        RequalificationDTO requalification = new RequalificationDTO( );
        SignalementUtils.populate( requalification, request );

        if ( request.getSession( ).getAttribute( PARAM_ID_SECTOR ) != null )
        {
            // requalification effectuée par un prestataire
            Integer idSector = (Integer) request.getSession( ).getAttribute( PARAM_ID_SECTOR );
            requalification.setSector( idSector );
            request.getSession( ).removeAttribute( PARAM_ID_SECTOR );
        }

        Adresse adresse = null;

        if ( request.getSession( ).getAttribute( PARAMETER_WEBSERVICE_ID_TYPE_ANOMALIE ) != null )
        {
            // La tache est execute suite a l'appel du WS "rest/signalement/api/changeStatus" par un prestataire
            long idTypeAnomalie = (long) request.getSession( ).getAttribute( PARAMETER_WEBSERVICE_ID_TYPE_ANOMALIE );
            request.getSession( ).removeAttribute( PARAMETER_WEBSERVICE_ID_TYPE_ANOMALIE );
            requalification.setTypeSignalement( (int) idTypeAnomalie );

            ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
            requalification.setIdSignalement( resourceHistory.getIdResource( ) );

            adresse = _adresseService.loadByIdSignalement( resourceHistory.getIdResource( ) );
            requalification.setLng( adresse.getLng( ) );
            requalification.setLat( adresse.getLat( ) );

            // Secteur calculé par défaut de l'anomalie
            Unit majorUnit = _signalementService.getMajorUnit( (int) idTypeAnomalie, requalification.getLng( ), requalification.getLat( ) );
            Sector computedSector = null;
            if ( null != majorUnit )
            {
                computedSector = _sectorService.getSectorByGeomAndUnit( requalification.getLng( ), requalification.getLat( ), majorUnit.getIdUnit( ) );
            }

            if ( null != computedSector )
            {
                requalification.setSector( computedSector.getIdSector( ) );
            }
        }

        Integer idSignalement = requalification.getIdSignalement( );
        Signalement signalement = _signalementService.getSignalement( idSignalement );

        // changement de l'adresse
        if ( adresse == null )
        {
            adresse = _adresseService.loadByIdSignalement( signalement.getId( ) );
            adresse.setAdresse( requalification.getAdresseRequalif( ) );
            adresse.setLat( requalification.getLat( ) );
            adresse.setLng( requalification.getLng( ) );

            signalement.getAdresses( ).remove( 0 );
            signalement.getAdresses( ).add( adresse );

        }

        // changement du type de signalement
        TypeSignalement typeSignalement = _typeSignalementService.findByIdTypeSignalement( requalification.getTypeSignalement( ) );
        signalement.setTypeSignalement( typeSignalement );

        // Changement du secteur
        Sector sector = _sectorService.findByPrimaryKey( requalification.getSector( ) );
        signalement.setSecteur( sector );

        // suppression datePrevueTraitement
        signalement.setDatePrevueTraitement( EMPTY_STRING );

        // suppression commentaireProgrammation
        signalement.setCommentaireProgrammation( EMPTY_STRING );

        Arrondissement arrondissement = _adresseService.getArrondissementByGeom( requalification.getLng( ), requalification.getLat( ) );
        signalement.setArrondissement( arrondissement );

        if ( request.getSession( ).getAttribute( PARAM_COMMENTAIRE_AGENT_TERRAIN ) != null )
        {
            // requalification effectuée par un prestataire
            signalement.setCommentaireAgentTerrain( request.getSession( ).getAttribute( PARAM_COMMENTAIRE_AGENT_TERRAIN ).toString( ) );
            request.getSession( ).removeAttribute( PARAM_COMMENTAIRE_AGENT_TERRAIN );
        }

        _adresseService.update( adresse );
        _signalementService.update( signalement );

        // set the state of the signalement with the workflow
        WorkflowService workflowService = WorkflowService.getInstance( );
        if ( workflowService.isAvailable( ) )
        {
            // récupération de l'identifiant du workflow
            Integer workflowId = _signalementWorkflowService.getSignalementWorkflowId( );
            if ( workflowId != null )
            {
                // création de l'état initial et exécution des tâches automatiques
                workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, workflowId, null );
                workflowService.executeActionAutomatic( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, workflowId, null );
            }
            else
            {
                AppLogService.error( "Signalement : No workflow selected" );
            }
        }
        else
        {
            AppLogService.error( "Signalement : Workflow not available" );
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
}
