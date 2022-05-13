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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * DAO of RequalifierSignalementTaskConfig.
 */
public class RequalificationAutoSignalementTaskConfigDAO implements ITaskConfigDAO<RequalificationAutoConfigUnit>
{

    /** The Constant SQL_QUERY_NEW_PRIMARY_KEY. */
    private static final String SQL_QUERY_NEW_PRIMARY_KEY = " SELECT COUNT( id_config_unit ) FROM signalement_workflow_rac_unit ";

    /** The Constant SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_PRIMARY_ID. */
    private static final String SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_PRIMARY_ID = " SELECT id_config_unit, id_task, id_unit_source, id_unit_target, id_type_signalement, id_state_after FROM signalement_workflow_rac_unit WHERE id_config_unit = ? ";

    /** The Constant SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_TASK_ID. */
    private static final String SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_TASK_ID = " SELECT id_config_unit, id_task, id_unit_source, id_unit_target, id_type_signalement, id_state_after FROM signalement_workflow_rac_unit WHERE id_task = ? ";

    /** The Constant SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_TASK_ID_SOURCE_UNIT_ID. */
    private static final String SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_TASK_ID_SOURCE_UNIT_ID = " SELECT id_config_unit, id_task, id_unit_source, id_unit_target, id_type_signalement, id_state_after FROM signalement_workflow_rac_unit WHERE id_task = ? AND id_unit_source = ? ";

    /** The Constant SQL_QUERY_CREATE_TASK_CONFIG_UNIT. */
    private static final String SQL_QUERY_CREATE_TASK_CONFIG_UNIT = " INSERT INTO signalement_workflow_rac_unit( id_config_unit, id_task, id_unit_source, id_unit_target, id_type_signalement, id_state_after ) VALUES ( ?, ?, ?, ?, ?, ?) ";

    /** The Constant SQL_QUERY_UPDATE_TASK_CONFIG_UNIT. */
    private static final String SQL_QUERY_UPDATE_TASK_CONFIG_UNIT = " UPDATE signalement_workflow_rac_unit SET id_task = ?, id_unit_source = ?, id_unit_target = ?, id_type_signalement = ?, id_state_after = ? WHERE id_config_unit = ? ";

    /** The Constant SQL_QUERY_DELETE_TASK_CONFIG_UNIT_BY_TASK_ID. */
    private static final String SQL_QUERY_DELETE_TASK_CONFIG_UNIT_BY_TASK_ID = " DELETE FROM signalement_workflow_rac_unit WHERE id_task = ? ";

    /** The Constant SQL_QUERY_DELETE_TASK_CONFIG_UNIT_BY_ID. */
    private static final String SQL_QUERY_DELETE_TASK_CONFIG_UNIT_BY_ID = " DELETE FROM signalement_workflow_rac_unit WHERE id_config_unit = ? ";

    /**
     * Find By primary Key.
     *
     * @param nIdConfigUnit
     *            the id config unit
     * @param plugin
     *            the plugin
     * @return requalification
     */
    public RequalificationAutoConfigUnit findByPrimaryKey( int nIdConfigUnit, Plugin plugin )
    {
        RequalificationAutoConfigUnit unit = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_PRIMARY_ID, plugin ) ; )
        {
            daoUtil.setInt( 1, nIdConfigUnit );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                unit = new RequalificationAutoConfigUnit( );
                unit.setIdConfigUnit( daoUtil.getInt( 1 ) );
                unit.setIdTask( daoUtil.getInt( 2 ) );
                unit.setIdSourceUnit( daoUtil.getInt( 3 ) );
                unit.setIdTargetUnit( daoUtil.getInt( 4 ) );
                unit.setIdTypeSignalement( daoUtil.getInt( 5 ) );
                unit.setIdStateAfter( daoUtil.getInt( 6 ) );
            }
            daoUtil.free( );
        }
        return unit;

    }

    /**
     * Create an unit.
     *
     * @param requalificationAutoUnit
     *            the requalification unit
     * @param plugin
     *            the plugin
     */
    public synchronized void createUnit( RequalificationAutoConfigUnit requalificationAutoUnit, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CREATE_TASK_CONFIG_UNIT, plugin ) ; )
        {
            requalificationAutoUnit.setIdConfigUnit( newPrimaryKey( plugin ) );
            daoUtil.setInt( 1, requalificationAutoUnit.getIdConfigUnit( ) );
            daoUtil.setInt( 2, requalificationAutoUnit.getIdTask( ) );
            daoUtil.setInt( 3, requalificationAutoUnit.getIdSourceUnit( ) );
            daoUtil.setInt( 4, requalificationAutoUnit.getIdTargetUnit( ) );
            daoUtil.setInt( 5, requalificationAutoUnit.getIdTypeSignalement( ) );
            daoUtil.setInt( 6, requalificationAutoUnit.getIdStateAfter( ) );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Find task by id.
     *
     * @param nTaskId
     *            the task id.
     * @param plugin
     *            the plugin
     * @return list task
     */
    public List<RequalificationAutoConfigUnit> findByTaskId( int nTaskId, Plugin plugin )
    {
        List<RequalificationAutoConfigUnit> listUnits = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_TASK_ID, plugin ) ; )
        {
            daoUtil.setInt( 1, nTaskId );

            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                RequalificationAutoConfigUnit unit = new RequalificationAutoConfigUnit( );
                unit.setIdConfigUnit( daoUtil.getInt( 1 ) );
                unit.setIdTask( daoUtil.getInt( 2 ) );
                unit.setIdSourceUnit( daoUtil.getInt( 3 ) );
                unit.setIdTargetUnit( daoUtil.getInt( 4 ) );
                unit.setIdTypeSignalement( daoUtil.getInt( 5 ) );
                unit.setIdStateAfter( daoUtil.getInt( 6 ) );
                listUnits.add( unit );
            }
            daoUtil.free( );
        }
        return listUnits;
    }

    /**
     * Find by task id and source unit id.
     *
     * @param nTaskId
     *            the task id
     * @param nSourceUnitId
     *            the source unit id
     * @param plugin
     *            the plugin
     * @return list task
     */
    public List<RequalificationAutoConfigUnit> findByTaskIdAndSourceUnitId( int nTaskId, int nSourceUnitId, Plugin plugin )
    {
        List<RequalificationAutoConfigUnit> listUnits = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TASK_CONFIG_UNIT_BY_TASK_ID_SOURCE_UNIT_ID, plugin ) ; )
        {
            daoUtil.setInt( 1, nTaskId );
            daoUtil.setInt( 2, nSourceUnitId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                RequalificationAutoConfigUnit unit = new RequalificationAutoConfigUnit( );
                unit.setIdConfigUnit( daoUtil.getInt( 1 ) );
                unit.setIdTask( daoUtil.getInt( 2 ) );
                unit.setIdSourceUnit( daoUtil.getInt( 3 ) );
                unit.setIdTargetUnit( daoUtil.getInt( 4 ) );
                unit.setIdTypeSignalement( daoUtil.getInt( 5 ) );
                unit.setIdStateAfter( daoUtil.getInt( 6 ) );
                listUnits.add( unit );
            }
            daoUtil.free( );
        }

        return listUnits;
    }

    /**
     * Update.
     *
     * @param requalificationAutoUnit
     *            the unit.
     * @param plugin
     *            the plugin
     */
    public void updateUnit( RequalificationAutoConfigUnit requalificationAutoUnit, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_TASK_CONFIG_UNIT, plugin ) ; )
        {
            daoUtil.setInt( 1, requalificationAutoUnit.getIdTask( ) );
            daoUtil.setInt( 2, requalificationAutoUnit.getIdSourceUnit( ) );
            daoUtil.setInt( 3, requalificationAutoUnit.getIdTargetUnit( ) );
            daoUtil.setInt( 4, requalificationAutoUnit.getIdTypeSignalement( ) );
            daoUtil.setInt( 5, requalificationAutoUnit.getIdStateAfter( ) );
            daoUtil.setInt( 6, requalificationAutoUnit.getIdConfigUnit( ) );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Delete task.
     *
     * @param nTaskId
     *            the is task
     * @param plugin
     *            the plugin
     */
    public void deleteByTaskId( int nTaskId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_TASK_CONFIG_UNIT_BY_TASK_ID, plugin ) ; )
        {
            daoUtil.setInt( 1, nTaskId );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Delete task.
     *
     * @param nId
     *            the id task
     * @param plugin
     *            the plugin
     */
    public void deleteById( int nId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_TASK_CONFIG_UNIT_BY_ID, plugin ) ; )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Get a new primary key.
     *
     * @param plugin
     *            The plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        int nResult = 1;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PRIMARY_KEY, plugin ) ; )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nResult = daoUtil.getInt( 1 ) + 1;
            }
            daoUtil.free( );

        }
        return nResult;
    }

    /**
     * Insert.
     *
     * @param config
     *            the config
     */
    @Override
    public void insert( RequalificationAutoConfigUnit config )
    {
        createUnit( config, null );
    }

    /**
     * Store.
     *
     * @param config
     *            the config
     */
    @Override
    public void store( RequalificationAutoConfigUnit config )
    {
        updateUnit( config, null );
    }

    /**
     * Load.
     *
     * @param nIdTask
     *            the n id task
     * @return the requalification auto config unit
     */
    @Override
    public RequalificationAutoConfigUnit load( int nIdTask )
    {
        return findByPrimaryKey( nIdTask, null );
    }

    /**
     * Delete.
     *
     * @param nIdTask
     *            the n id task
     */
    @Override
    public void delete( int nIdTask )
    {
        deleteById( nIdTask, null );
    }
}
