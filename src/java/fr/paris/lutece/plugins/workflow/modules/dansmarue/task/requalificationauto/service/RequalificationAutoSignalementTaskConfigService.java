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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.impl.TypeSignalementService;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.WorkflowSignalementPlugin;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationAutoConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationAutoSignalementTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationAutoUnitDTO;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.config.TaskConfigService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * RequalificationAutoSignalementTaskConfigService.
 */
public class RequalificationAutoSignalementTaskConfigService extends TaskConfigService
{

    /** The Constant MESSAGE_EVERY_TYPE_SIGNALEMENT. */
    private static final String MESSAGE_EVERY_TYPE_SIGNALEMENT = "module.workflow.dansmarue.task_requalification_auto_config.type_signalement.all";

    /** The Constant MESSAGE_NO_STATE_AFTER. */
    private static final String MESSAGE_NO_STATE_AFTER = "module.workflow.dansmarue.task_requalification_auto_config.noStateAfter";

    /** The requalification auto signalement DAO. */
    @Inject
    @Named( "workflow-signalement.requalificationAutoConfigDAO" )
    private RequalificationAutoSignalementTaskConfigDAO _requalificationAutoSignalementDAO;

    /** The type signalement service. */
    @Inject
    @Named( "typeSignalementService" )
    private TypeSignalementService _typeSignalementService;

    /** The workflow signalement plugin. */
    private Plugin _workflowSignalementPlugin;

    /**
     * Create Unit.
     *
     * @param requalificationAutoUnit
     *            the requalification auto task
     */
    public void createUnit( RequalificationAutoConfigUnit requalificationAutoUnit )
    {
        _requalificationAutoSignalementDAO.createUnit( requalificationAutoUnit, getPlugin( ) );
    }

    /**
     * Find requalification task.
     *
     * @param nTaskId
     *            the id task
     * @return the list of task.
     */
    public List<RequalificationAutoConfigUnit> findByTaskId( int nTaskId )
    {
        return _requalificationAutoSignalementDAO.findByTaskId( nTaskId, getPlugin( ) );
    }

    /**
     * Find requalification task.
     *
     * @param nTaskId
     *            the id task
     *
     * @param nSourceUnitId
     *            the source unit id
     *
     * @return the list of task.
     */
    public List<RequalificationAutoConfigUnit> findByTaskIdAndSourceUnitId( int nTaskId, int nSourceUnitId )
    {
        return _requalificationAutoSignalementDAO.findByTaskIdAndSourceUnitId( nTaskId, nSourceUnitId, getPlugin( ) );
    }

    /**
     * Update unit.
     *
     * @param requalificationAutoUnit
     *            requalification to update.
     */
    public void updateUnit( RequalificationAutoConfigUnit requalificationAutoUnit )
    {
        _requalificationAutoSignalementDAO.updateUnit( requalificationAutoUnit, getPlugin( ) );
    }

    /**
     * Delete task.
     *
     * @param nTaskId
     *            the id task
     */
    public void deleteByTaskId( int nTaskId )
    {
        _requalificationAutoSignalementDAO.deleteByTaskId( nTaskId, getPlugin( ) );
    }

    /**
     * Delete task.
     *
     * @param nId
     *            the id.
     */
    public void deleteById( int nId )
    {
        _requalificationAutoSignalementDAO.deleteById( nId, getPlugin( ) );
    }

    /**
     * Convert requalification unit to DTO.
     *
     * @param listRequalifsUnits
     *            list of requalification units
     * @param listUnits
     *            list units
     * @param listStates
     *            list of states
     * @param locale
     *            the local
     * @return list of requalification
     */
    public List<RequalificationAutoUnitDTO> convertRequalifAutoUnitsToDTO( List<RequalificationAutoConfigUnit> listRequalifsUnits, List<Unit> listUnits,
            List<State> listStates, Locale locale )
    {
        List<RequalificationAutoUnitDTO> listDTOs = new ArrayList<>( listRequalifsUnits.size( ) );

        for ( RequalificationAutoConfigUnit requalifUnit : listRequalifsUnits )
        {
            RequalificationAutoUnitDTO requalifDTO = new RequalificationAutoUnitDTO( requalifUnit );

            boolean bSourceFound = false;
            boolean bTargetFound = false;

            for ( Unit unit : listUnits )
            {
                if ( unit.getIdUnit( ) == requalifDTO.getIdSourceUnit( ) )
                {
                    requalifDTO.setSourceUnitName( unit.getLabel( ) );
                    bSourceFound = true;
                }
                else
                    if ( unit.getIdUnit( ) == requalifDTO.getIdTargetUnit( ) )
                    {
                        requalifDTO.setTargetUnitName( unit.getLabel( ) );
                        bTargetFound = true;
                    }
                if ( bSourceFound && bTargetFound )
                {
                    break;
                }
            }
            if ( requalifDTO.getIdTypeSignalement( ) > 0 )
            {
                TypeSignalement typeSignalement = _typeSignalementService.getTypeSignalement( requalifDTO.getIdTypeSignalement( ) );
                requalifDTO.setTypeSignalementName( typeSignalement.getFormatTypeSignalement( ) );
            }
            else
            {
                requalifDTO.setTypeSignalementName( I18nService.getLocalizedString( MESSAGE_EVERY_TYPE_SIGNALEMENT, locale ) );
            }
            if ( requalifUnit.getIdStateAfter( ) > 0 )
            {
                for ( State state : listStates )
                {
                    if ( state.getId( ) == requalifUnit.getIdStateAfter( ) )
                    {
                        requalifDTO.setWorkflowStateAfterName( state.getName( ) );
                        break;
                    }
                }
                if ( requalifDTO.getWorkflowStateAfterName( ) == null )
                {
                    requalifDTO.setWorkflowStateAfterName( I18nService.getLocalizedString( MESSAGE_NO_STATE_AFTER, locale ) );
                }
            }
            else
            {
                requalifDTO.setWorkflowStateAfterName( I18nService.getLocalizedString( MESSAGE_NO_STATE_AFTER, locale ) );
            }
            listDTOs.add( requalifDTO );
        }

        return listDTOs;
    }

    /**
     * Find by primary key.
     *
     * @param nIdConfigUnit
     *            the n id config unit
     * @return the requalification auto config unit
     */
    @Override
    public RequalificationAutoConfigUnit findByPrimaryKey( int nIdConfigUnit )
    {
        return _requalificationAutoSignalementDAO.findByPrimaryKey( nIdConfigUnit, getPlugin( ) );
    }

    /**
     * Get the workflow signalement plugin.
     *
     * @return The workflow signalement plugin
     */
    private Plugin getPlugin( )
    {
        if ( _workflowSignalementPlugin == null )
        {
            _workflowSignalementPlugin = PluginService.getPlugin( WorkflowSignalementPlugin.PLUGIN_NAME );
        }
        return _workflowSignalementPlugin;
    }
}
