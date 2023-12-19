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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.utils.ISignalementUtils;
import fr.paris.lutece.plugins.dansmarue.utils.impl.SignalementUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigUnitDAO;
import fr.paris.lutece.plugins.workflowcore.service.config.TaskConfigService;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * WebServiceSignalementTaskConfigService class.
 */
public class WebServiceSignalementTaskConfigService extends TaskConfigService
{

    /** The webservice signalement task config DAO. */
    // DAO
    @Inject
    @Named( "signalement.webserviceSignalementTaskConfigDAO" )
    private WebServiceSignalementTaskConfigDAO _webserviceSignalementTaskConfigDAO;

    /** The webservice signalement task config unit DAO. */
    @Inject
    @Named( "signalement.webserviceSignalementTaskConfigUnitDAO" )
    private WebServiceSignalementTaskConfigUnitDAO _webserviceSignalementTaskConfigUnitDAO;

    /** The unit service. */
    @Inject
    @Named( "unittree.unitService" )
    private IUnitService _unitService;

    /** The signalement utils */
    @Inject
    @Named( "signalement.signalementUtils" )
    private ISignalementUtils _signalementUtils;

    /**
     * Add a new WebServiceSignalementTask configuration.
     *
     * @param configDTO
     *            the WebServiceSignalementTask configuration
     */
    public void insert( WebServiceSignalementTaskConfigDTO configDTO )
    {
        Plugin plugin = _signalementUtils.getPlugin( );

        WebServiceSignalementTaskConfig config = new WebServiceSignalementTaskConfig( );
        config.setIdTask( configDTO.getIdTask( ) );
        _webserviceSignalementTaskConfigDAO.insert( config, plugin );
    }

    /**
     * Add a new WebServiceSignalementTask configuration for unit.
     *
     * @param configUnit
     *            the WebServiceSignalementTask configuration for unit
     */
    public void insertUnit( WebServiceSignalementTaskConfigUnit configUnit )
    {
        Plugin plugin = _signalementUtils.getPlugin( );
        _webserviceSignalementTaskConfigUnitDAO.insert( configUnit, plugin );
    }

    /**
     * Update a WebServiceSignalementTask configuration.
     *
     * @param configDTO
     *            the WebServiceSignalementTaskConfigDTO
     */
    public void update( WebServiceSignalementTaskConfigDTO configDTO )
    {
        Plugin plugin = _signalementUtils.getPlugin( );

        WebServiceSignalementTaskConfig config = new WebServiceSignalementTaskConfig( );
        config.setIdTask( configDTO.getIdTask( ) );
        _webserviceSignalementTaskConfigDAO.update( config, plugin );
    }

    /**
     * Update a WebServiceSignalementTask configuration for unit.
     *
     * @param configUnit
     *            the configuration
     */
    public void updateUnit( WebServiceSignalementTaskConfigUnit configUnit )
    {
        Plugin plugin = _signalementUtils.getPlugin( );
        _webserviceSignalementTaskConfigUnitDAO.update( configUnit, plugin );
    }

    /**
     * Find by primary key.
     *
     * @param nIdTask
     *            the n id task
     * @return the web service signalement task config DTO
     */
    @Override
    public WebServiceSignalementTaskConfigDTO findByPrimaryKey( int nIdTask )
    {
        Plugin plugin = _signalementUtils.getPlugin( );
        WebServiceSignalementTaskConfigDTO configDTO = null;

        WebServiceSignalementTaskConfig config = _webserviceSignalementTaskConfigDAO.findByPrimaryKey( nIdTask, plugin );
        if ( config != null )
        {
            configDTO = new WebServiceSignalementTaskConfigDTO( );
            configDTO.setIdTask( config.getIdTask( ) );
            List<WebServiceSignalementTaskConfigUnit> listeConfigUnit = _webserviceSignalementTaskConfigUnitDAO.findByIdTask( nIdTask, plugin );

            Unit unit;
            for ( WebServiceSignalementTaskConfigUnit configUnit : listeConfigUnit )
            {
                // Ajout de l'unit
                unit = _unitService.getUnit( configUnit.getUnit( ).getIdUnit( ), false );
                configUnit.setUnit( unit );
            }
            configDTO.setListConfigUnit( listeConfigUnit );
        }

        return configDTO;
    }

    /**
     * Return a configuration for a given task id and unit id.
     *
     * @param nIdTask
     *            the task id
     * @param nIdUnit
     *            the unit id
     * @return the configuration
     */
    public WebServiceSignalementTaskConfigUnit findUnitByPrimaryKey( int nIdTask, int nIdUnit )
    {
        Plugin plugin = _signalementUtils.getPlugin( );
        return _webserviceSignalementTaskConfigUnitDAO.findByPrimaryKey( nIdTask, nIdUnit, plugin );
    }

    /**
     * Delete.
     *
     * @param nIdTask
     *            the task id
     */
    public void delete( int nIdTask )
    {
        Plugin plugin = _signalementUtils.getPlugin( );
        _webserviceSignalementTaskConfigUnitDAO.deleteAll( nIdTask, plugin );
        _webserviceSignalementTaskConfigDAO.delete( nIdTask, plugin );
    }

    /**
     * Delete unit.
     *
     * @param nIdTask
     *            the task id
     * @param nIdUnit
     *            the unit id
     */
    public void deleteUnit( int nIdTask, int nIdUnit )
    {
        Plugin plugin = _signalementUtils.getPlugin( );
        _webserviceSignalementTaskConfigUnitDAO.delete( nIdTask, nIdUnit, plugin );
    }

    /**
     * Return a list of unit to notify for a given configuration.
     *
     * @param configDTO
     *            the configuration
     * @return listUnits
     */
    public List<Unit> getListUnitToNotify( WebServiceSignalementTaskConfigDTO configDTO )
    {
        List<Unit> listUnits = new ArrayList<>( );
        for ( WebServiceSignalementTaskConfigUnit configUnit : configDTO.getListConfigUnit( ) )
        {
            Unit unit = _unitService.getUnit( configUnit.getUnit( ).getIdUnit( ), false );
            listUnits.add( unit );
        }

        return listUnits;
    }

}
