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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigUnitDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * NotificationSignalementTaskConfigService class
 *
 */
public class NotificationSignalementTaskConfigService
{
    // DAO
    @Inject
    @Named( "signalement.notificationSignalementTaskConfigDAO" )
    private NotificationSignalementTaskConfigDAO     _notificationSignalementTaskConfigDAO;

    @Inject
    @Named( "signalement.notificationSignalementTaskConfigUnitDAO" )
    private NotificationSignalementTaskConfigUnitDAO _notificationSignalementTaskConfigUnitDAO;

    @Inject
    @Named( "unittree.unitService" )
    private IUnitService                             _unitService;

    /**
     * Add a new NotificationSignalementTask configuration
     *
     * @param configDTO
     *            the NotificationSignalementTask configuration
     */
    public void insert( NotificationSignalementTaskConfigDTO configDTO )
    {
        Plugin plugin = SignalementUtils.getPlugin( );

        NotificationSignalementTaskConfig config = new NotificationSignalementTaskConfig( );
        config.setIdTask( configDTO.getIdTask( ) );
        config.setSubject( configDTO.getSubject( ) );
        config.setMessage( configDTO.getMessage( ) );
        config.setSender( configDTO.getSender( ) );
        _notificationSignalementTaskConfigDAO.insert( config, plugin );
    }

    /**
     * Add a new NotificationSignalementTask configuration for unit
     *
     * @param configUnit
     *            the NotificationSignalementTask configuration for unit
     */
    public void insertUnit( NotificationSignalementTaskConfigUnit configUnit )
    {
        Plugin plugin = SignalementUtils.getPlugin( );
        _notificationSignalementTaskConfigUnitDAO.insert( configUnit, plugin );
    }

    /**
     * Update a NotificationSignalementTask configuration
     *
     * @param configDTO
     *            the NotificationSignalementTaskConfigDTO
     */
    public void update( NotificationSignalementTaskConfigDTO configDTO )
    {
        Plugin plugin = SignalementUtils.getPlugin( );

        NotificationSignalementTaskConfig config = new NotificationSignalementTaskConfig( );
        config.setIdTask( configDTO.getIdTask( ) );
        config.setSubject( configDTO.getSubject( ) );
        config.setMessage( configDTO.getMessage( ) );
        config.setSender( configDTO.getSender( ) );
        _notificationSignalementTaskConfigDAO.update( config, plugin );
    }

    /**
     * Update a NotificationSignalementTask configuration for unit
     *
     * @param configUnit
     *            the configuration
     */
    public void updateUnit( NotificationSignalementTaskConfigUnit configUnit )
    {
        Plugin plugin = SignalementUtils.getPlugin( );
        _notificationSignalementTaskConfigUnitDAO.update( configUnit, plugin );
    }

    /**
     * Find a NotificationSignalementTaskConfigDTO by id
     *
     * @param nIdTask
     *            the task id
     * @return configDTO
     */
    public NotificationSignalementTaskConfigDTO findByPrimaryKey( int nIdTask )
    {
        Plugin plugin = SignalementUtils.getPlugin( );
        NotificationSignalementTaskConfigDTO configDTO = new NotificationSignalementTaskConfigDTO( );

        NotificationSignalementTaskConfig config = _notificationSignalementTaskConfigDAO.findByPrimaryKey( nIdTask, plugin );
        if ( config != null )
        {
            configDTO = new NotificationSignalementTaskConfigDTO( );
            configDTO.setIdTask( config.getIdTask( ) );
            configDTO.setSubject( config.getSubject( ) );
            configDTO.setMessage( config.getMessage( ) );
            configDTO.setSender( config.getSender( ) );
        }

        List<NotificationSignalementTaskConfigUnit> listeConfigUnit = _notificationSignalementTaskConfigUnitDAO.findByIdTask( nIdTask, plugin );

        Unit unit;
        for ( NotificationSignalementTaskConfigUnit configUnit : listeConfigUnit )
        {
            // Ajout de l'unit
            unit = _unitService.getUnit( configUnit.getUnit( ).getIdUnit( ), false );
            configUnit.setUnit( unit );
        }
        configDTO.setListConfigUnit( listeConfigUnit );

        return configDTO;
    }

    /**
     * Return a configuration for a given task id and unit id
     *
     * @param nIdTask
     *            the task id
     * @param nIdUnit
     *            the unit id
     * @return the configuration
     */
    public NotificationSignalementTaskConfigUnit findUnitByPrimaryKey( int nIdTask, int nIdUnit )
    {
        Plugin plugin = SignalementUtils.getPlugin( );
        return _notificationSignalementTaskConfigUnitDAO.findByPrimaryKey( nIdTask, nIdUnit, plugin );
    }

    /**
     *
     * @param nIdTask
     *            the task id
     */
    public void delete( int nIdTask )
    {
        Plugin plugin = SignalementUtils.getPlugin( );
        _notificationSignalementTaskConfigUnitDAO.deleteAll( nIdTask, plugin );
        _notificationSignalementTaskConfigDAO.delete( nIdTask, plugin );
    }

    /**
     *
     * @param nIdTask
     *            the task id
     * @param nIdUnit
     *            the unit id
     */
    public void deleteUnit( int nIdTask, int nIdUnit )
    {
        Plugin plugin = SignalementUtils.getPlugin( );
        _notificationSignalementTaskConfigUnitDAO.delete( nIdTask, nIdUnit, plugin );
    }

    /**
     * Return a list of unit to notify for a given configuration
     *
     * @param configDTO
     *            the configuration
     * @return listUnits
     */
    public List<Unit> getListUnitToNotify( NotificationSignalementTaskConfigDTO configDTO )
    {
        List<Unit> listUnits = new ArrayList<Unit>( );
        for ( NotificationSignalementTaskConfigUnit configUnit : configDTO.getListConfigUnit( ) )
        {
            Unit unit = _unitService.getUnit( configUnit.getUnit( ).getIdUnit( ), false );
            listUnits.add( unit );
        }

        return listUnits;
    }

}
