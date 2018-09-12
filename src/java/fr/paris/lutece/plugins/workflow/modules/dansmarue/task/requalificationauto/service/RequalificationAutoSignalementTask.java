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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationAutoConfigUnit;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.state.IStateService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * Tache de requalification automatique de signalements
 */
public class RequalificationAutoSignalementTask extends AbstractSignalementTask
{
    private static final String                             MESSAGE_TASK_TITLE                               = "module.workflow.dansmarue.task_requalification_auto_config.title";
    private static final String                             MESSAGE_UNIT_ALREADY_EXISTS                      = "module.workflow.dansmarue.task_requalification_auto_config.unitAlreadyExists";

    private static final String                             PARAMETER_ID_TASK                                = "id_task";
    private static final String                             PARAMETER_ADD_UNIT                               = "add_unit";
    private static final String                             PARAMETER_SOURCE_UNIT                            = "sourceUnit";
    private static final String                             PARAMETER_TARGET_UNIT                            = "targetUnit";
    private static final String                             PARAMETER_TYPE_SIGNALEMENT                       = "type_signalement";
    private static final String                             PARAMETER_WORKFLOW_STATE_AFTER                   = "workflow_state_after";

    private static final String                             JSP_MODIFY_TASK                                  = "jsp/admin/plugins/workflow/ModifyTask.jsp";

    private static final int                                CONSTANT_ALL_TYPE_SIGNALEMENT                    = -1;
    private static final int                                CONSTANT_NO_STATE_AFTER                          = -1;

    private RequalificationAutoSignalementTaskConfigService _requalificationAutoSignalementTaskConfigService = SpringContextService.getBean( "workflow-signalement.requalificationAutoConfigService" );
    private IUnitService                                    _unitService                                     = ( IUnitService ) SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
    private ISignalementService                             _signalementService                              = ( ISignalementService ) SpringContextService.getBean( "signalementService" );
    private IAdresseService                                 _adresseService                                  = ( IAdresseService ) SpringContextService.getBean( "adresseSignalementService" );
    private ISectorService                                  _sectorService                                   = ( ISectorService ) SpringContextService.getBean( "unittree-dansmarue.sectorService" );
    private IStateService                                   _stateService                                    = SpringContextService.getBean( "workflow.stateService" );

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        Signalement signalement = _signalementService.getSignalement( getIdSignalement( nIdResourceHistory ) );

        // sector.get
        Unit unitSignalement = null;
        List<Unit> listUnitsSector = _sectorService.findUnitsWithNoChildrenByIdSector( signalement.getSecteur( ).getIdSector( ) );
        if ( listUnitsSector.size( ) == 1 )
        {
            unitSignalement = listUnitsSector.get( 0 );
        } else
        {
            for ( Unit unit : listUnitsSector )
            {
                if ( _unitService.isParent( signalement.getTypeSignalement( ).getUnit( ), unit ) )
                {
                    unitSignalement = unit;
                    break;
                }
            }
        }

        // If we didn't found the unit
        if ( unitSignalement == null )
        {
            AppLogService.error( "RequalificationAutoSignalement : Couldn't find unit of signalement " + signalement.getId( ) );
            return;
        }
        List<RequalificationAutoConfigUnit> listRequalifs = _requalificationAutoSignalementTaskConfigService.findByTaskId( getId( ) );
        RequalificationAutoConfigUnit selectedRequalifConfUnit = null;

        for ( RequalificationAutoConfigUnit requalifUnit : listRequalifs )
        {
            Unit unitSourceRequalif = _unitService.getUnit( requalifUnit.getIdSourceUnit( ), false );
            if ( ( ( unitSourceRequalif.getIdUnit( ) == unitSignalement.getIdUnit( ) ) || _unitService.isParent( unitSourceRequalif, unitSignalement ) )
                    && ( ( requalifUnit.getIdTypeSignalement( ) == CONSTANT_ALL_TYPE_SIGNALEMENT ) || ( requalifUnit.getIdTypeSignalement( ) == signalement.getTypeSignalement( ).getId( ) ) ) )
            {
                selectedRequalifConfUnit = requalifUnit;
                break;
            }
        }
        if ( selectedRequalifConfUnit != null )
        {
            Adresse adresse = _adresseService.loadByIdSignalement( signalement.getId( ) );

            // Changement du secteur
            Sector secteur = _adresseService.getSectorByGeomAndIdUnitParent( adresse.getLng( ), adresse.getLat( ), selectedRequalifConfUnit.getIdTargetUnit( ) );

            signalement.setSecteur( secteur );
            _signalementService.update( signalement );

            // If we must change the next state
            if ( selectedRequalifConfUnit.getIdStateAfter( ) > 0 )
            {
                State stateAfter = _stateService.findByPrimaryKey( selectedRequalifConfUnit.getIdStateAfter( ) );
                getAction( ).setStateAfter( stateAfter );
            }
        }
    }

    /**
     * Add a new unit to this task
     *
     * @param request
     *            request
     * @param locale
     *            locale
     * @param plugin
     *            plugin
     * @return the url to redirect to, or null if the user should be redirected to the default URL
     */
    public String doSaveConfig( HttpServletRequest request, Locale locale, Plugin plugin )
    {
        if ( request.getParameter( PARAMETER_ADD_UNIT ) != null )
        {
            String strIdSourceUnit = request.getParameter( PARAMETER_SOURCE_UNIT );
            String strIdTargetUnit = request.getParameter( PARAMETER_TARGET_UNIT );
            String strIdTypeSignalement = request.getParameter( PARAMETER_TYPE_SIGNALEMENT );
            String strIdWorkflowStateAfter = request.getParameter( PARAMETER_WORKFLOW_STATE_AFTER );

            if ( StringUtils.isNumeric( strIdSourceUnit ) && StringUtils.isNumeric( strIdTargetUnit ) )
            {
                int nIdSourceUnit = Integer.parseInt( strIdSourceUnit );
                int nIdTargetUnit = Integer.parseInt( strIdTargetUnit );
                RequalificationAutoConfigUnit requalifUnit = new RequalificationAutoConfigUnit( );
                requalifUnit.setIdTask( getId( ) );
                requalifUnit.setIdSourceUnit( nIdSourceUnit );
                requalifUnit.setIdTargetUnit( nIdTargetUnit );
                if ( StringUtils.isNumeric( strIdTypeSignalement ) )
                {
                    int nIdTypeSignalement = Integer.parseInt( strIdTypeSignalement );
                    requalifUnit.setIdTypeSignalement( nIdTypeSignalement );
                } else
                {
                    requalifUnit.setIdTypeSignalement( CONSTANT_ALL_TYPE_SIGNALEMENT );
                }

                if ( StringUtils.isNumeric( strIdWorkflowStateAfter ) )
                {
                    int nIdNextWorkflowAction = Integer.parseInt( strIdWorkflowStateAfter );
                    requalifUnit.setIdStateAfter( nIdNextWorkflowAction );
                } else
                {
                    requalifUnit.setIdStateAfter( CONSTANT_NO_STATE_AFTER );
                }

                List<RequalificationAutoConfigUnit> listRequalif = _requalificationAutoSignalementTaskConfigService.findByTaskIdAndSourceUnitId( getId( ), nIdSourceUnit );
                if ( ! listRequalif.isEmpty( ) )
                {
                    if ( requalifUnit.getIdTypeSignalement( ) == CONSTANT_ALL_TYPE_SIGNALEMENT )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_UNIT_ALREADY_EXISTS );
                    }
                    for ( RequalificationAutoConfigUnit requalif : listRequalif )
                    {
                        if ( ( requalif.getIdTypeSignalement( ) == CONSTANT_ALL_TYPE_SIGNALEMENT ) || ( requalif.getIdTypeSignalement( ) == requalifUnit.getIdTypeSignalement( ) ) )
                        {
                            return AdminMessageService.getMessageUrl( request, MESSAGE_UNIT_ALREADY_EXISTS );
                        }
                    }
                }

                _requalificationAutoSignalementTaskConfigService.createUnit( requalifUnit );
            }

            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
            url.addParameter( PARAMETER_ID_TASK, getId( ) );

            return url.getUrl( );
        }

        return null;
    }

    @Override
    public void doRemoveConfig( )
    {
        _requalificationAutoSignalementTaskConfigService.deleteByTaskId( getId( ) );
    }

    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_TASK_TITLE, locale );
    }

    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )
    {
        return null;
    }

}
