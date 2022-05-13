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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * NotificationSignalementTaskConfigUnitDAO.
 */
public class NotificationSignalementTaskConfigUnitDAO
{

    /** The Constant SQL_SELECT_IDTASK_DEST_IDUNIT. */
    private static final String SQL_SELECT_IDTASK_DEST_IDUNIT = "SELECT id_task,destinataires,id_unit ";

    /** The Constant SQL_QUERY_FIND_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = SQL_SELECT_IDTASK_DEST_IDUNIT
            + " FROM signalement_workflow_notification_config_unit WHERE id_task=? AND id_unit=?";

    /** The Constant SQL_QUERY_FIND_BY_PRIMARY_KEY_AND_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY_AND_TYPE_SIGNALEMENT = SQL_SELECT_IDTASK_DEST_IDUNIT
            + " FROM signalement_workflow_notification_config_unit WHERE id_task=? AND id_type_signalement=?";

    /** The Constant SQL_QUERY_FIND_BY_ID_TASK. */
    private static final String SQL_QUERY_FIND_BY_ID_TASK = SQL_SELECT_IDTASK_DEST_IDUNIT
            + " FROM signalement_workflow_notification_config_unit WHERE id_task=? and id_type_signalement is null";

    /** The Constant SQL_QUERY_FIND_BY_ID_TASK_WITH_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_FIND_BY_ID_TASK_WITH_TYPE_SIGNALEMENT = "SELECT swncu.id_task,destinataires,swncu.id_type_signalement, sts.libelle , sts.fk_id_type_signalement, sts.fk_id_unit"
            + " FROM signalement_workflow_notification_config_unit swncu, signalement_type_signalement sts WHERE sts.id_type_signalement = swncu.id_type_signalement and swncu.id_task=? and swncu.id_unit is null";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_workflow_notification_config_unit "
            + "(id_task,destinataires,id_unit) VALUES(?,?,?)";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_workflow_notification_config_unit "
            + "SET id_task=?,destinataires=?,id_unit=? WHERE id_task=? AND id_unit=?";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_workflow_notification_config_unit WHERE id_task=? AND id_unit=?";

    /** The Constant SQL_QUERY_DELETE_ALL. */
    private static final String SQL_QUERY_DELETE_ALL = "DELETE FROM signalement_workflow_notification_config_unit WHERE id_task=?";

    /** The Constant SQL_QUERY_FIND_BY_ID_UNIT. */
    private static final String SQL_QUERY_FIND_BY_ID_UNIT = SQL_SELECT_IDTASK_DEST_IDUNIT
            + " FROM signalement_workflow_notification_config_unit WHERE id_unit=?";

    /** The Constant SQL_QUERY_FIND_BY_ID_UNIT_AND_ID_TASK. */
    private static final String SQL_QUERY_FIND_BY_ID_UNIT_AND_ID_TASK = SQL_SELECT_IDTASK_DEST_IDUNIT
            + " FROM signalement_workflow_notification_config_unit WHERE id_unit=? and id_task=?";

    /** The Constant SQL_QUERY_FIND_BY_ID_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_FIND_BY_ID_TYPE_SIGNALEMENT = "SELECT id_task,destinataires,id_type_signalement "
            + " FROM signalement_workflow_notification_config_unit WHERE id_type_signalement=?";

    /** The Constant SQL_QUERY_DELETE_BY_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_DELETE_BY_TYPE_SIGNALEMENT = "DELETE FROM signalement_workflow_notification_config_unit WHERE id_task=? AND id_type_signalement=?";

    /** The Constant SQL_QUERY_INSERT_WITH_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_INSERT_WITH_TYPE_SIGNALEMENT = "INSERT INTO signalement_workflow_notification_config_unit "
            + "(id_task,destinataires,id_type_signalement) VALUES(?,?,?)";

    /** The Constant SQL_QUERY_UPDATE_WITH_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_UPDATE_WITH_TYPE_SIGNALEMENT = "UPDATE signalement_workflow_notification_config_unit "
            + "SET id_task=?,destinataires=?,id_type_signalement=? WHERE id_task=? AND id_type_signalement=?";

    /**
     * Insert.
     *
     * @param config
     *            the task configuration
     * @param plugin
     *            the plugin
     */
    public void insert( NotificationSignalementTaskConfigUnit config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nPos = 0;
        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getDestinataires( ) );
        daoUtil.setInt( ++nPos, config.getUnit( ).getIdUnit( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * Insert with type signalement.
     *
     * @param config
     *            the task configuration
     * @param plugin
     *            the plugin
     */
    public void insertWithTypeSignalement( NotificationSignalementTaskConfigUnit config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_WITH_TYPE_SIGNALEMENT, plugin );

        int nPos = 0;
        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getDestinataires( ) );
        daoUtil.setInt( ++nPos, config.getTypeSignalement( ).getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * Update.
     *
     * @param config
     *            the task configuration
     * @param plugin
     *            the plugin
     */
    public void update( NotificationSignalementTaskConfigUnit config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nPos = 0;
        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getDestinataires( ) );
        daoUtil.setInt( ++nPos, config.getUnit( ).getIdUnit( ) );

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setInt( ++nPos, config.getUnit( ).getIdUnit( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * Update with type signalement.
     *
     * @param config
     *            the task configuration
     * @param plugin
     *            the plugin
     */
    public void updateWithTypeSignalement( NotificationSignalementTaskConfigUnit config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_WITH_TYPE_SIGNALEMENT, plugin );

        int nPos = 0;
        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getDestinataires( ) );
        daoUtil.setInt( ++nPos, config.getTypeSignalement( ).getId( ) );

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setInt( ++nPos, config.getTypeSignalement( ).getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * Find by primary key.
     *
     * @param nIdTask
     *            the task id
     * @param nIdUnit
     *            the unit id
     * @param plugin
     *            the plugin
     * @return notificationSignalementTask configuration
     */
    public NotificationSignalementTaskConfigUnit findByPrimaryKey( int nIdTask, int nIdUnit, Plugin plugin )
    {
        NotificationSignalementTaskConfigUnit config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, nIdTask );
        daoUtil.setInt( nIndex, nIdUnit );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            config = new NotificationSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setDestinataires( daoUtil.getString( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            config.setUnit( unit );
        }

        daoUtil.close( );

        return config;
    }

    /**
     * Find by primary key type signalement.
     *
     * @param nIdTask
     *            the task id
     * @param nIdTypeSignalement
     *            the n id type signalement
     * @param plugin
     *            the plugin
     * @return notificationSignalementTask configuration
     */
    public NotificationSignalementTaskConfigUnit findByPrimaryKeyTypeSignalement( int nIdTask, int nIdTypeSignalement, Plugin plugin )
    {
        NotificationSignalementTaskConfigUnit config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY_AND_TYPE_SIGNALEMENT, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, nIdTask );
        daoUtil.setInt( nIndex, nIdTypeSignalement );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            config = new NotificationSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setDestinataires( daoUtil.getString( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            config.setUnit( unit );
        }

        daoUtil.close( );

        return config;
    }

    /**
     * Find by id task.
     *
     * @param nIdTask
     *            the task id
     * @param plugin
     *            the plugin
     * @return the list of notificationSignalement task configuration
     */
    public List<NotificationSignalementTaskConfigUnit> findByIdTask( int nIdTask, Plugin plugin )
    {
        List<NotificationSignalementTaskConfigUnit> listConfigs = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_TASK, plugin );
        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeQuery( );

        int nPos = 0;

        while ( daoUtil.next( ) )
        {
            nPos = 0;
            NotificationSignalementTaskConfigUnit config = new NotificationSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setDestinataires( daoUtil.getString( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            config.setUnit( unit );

            listConfigs.add( config );
        }

        daoUtil.close( );

        return listConfigs;
    }

    /**
     * Find by id task with type signalement.
     *
     * @param nIdTask
     *            the task id
     * @param plugin
     *            the plugin
     * @return the list of notificationSignalement task configuration
     */
    public List<NotificationSignalementTaskConfigUnit> findByIdTaskWithTypeSignalement( int nIdTask, Plugin plugin )
    {
        List<NotificationSignalementTaskConfigUnit> listConfigs = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_TASK_WITH_TYPE_SIGNALEMENT, plugin );
        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeQuery( );

        int nPos = 0;

        while ( daoUtil.next( ) )
        {
            nPos = 0;
            NotificationSignalementTaskConfigUnit config = new NotificationSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setDestinataires( daoUtil.getString( ++nPos ) );
            TypeSignalement type = new TypeSignalement( );
            type.setId( daoUtil.getInt( ++nPos ) );
            type.setLibelle( daoUtil.getString( ++nPos ) );
            TypeSignalement typeParent = new TypeSignalement( );
            typeParent.setId( daoUtil.getInt( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            type.setUnit( unit );
            type.setTypeSignalementParent( typeParent );
            config.setTypeSignalement( type );

            listConfigs.add( config );
        }

        daoUtil.close( );

        return listConfigs;
    }

    /**
     * Find by id unit.
     *
     * @param nIdUnit
     *            the unit id
     * @param plugin
     *            the plugin
     * @return the list of notificationSignalement task configuration
     */
    public List<NotificationSignalementTaskConfigUnit> findByIdUnit( int nIdUnit, Plugin plugin )
    {
        List<NotificationSignalementTaskConfigUnit> listConfigs = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeQuery( );

        int nPos = 0;

        while ( daoUtil.next( ) )
        {
            nPos = 0;
            NotificationSignalementTaskConfigUnit config = new NotificationSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setDestinataires( daoUtil.getString( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            config.setUnit( unit );

            listConfigs.add( config );
        }

        daoUtil.close( );

        return listConfigs;
    }

    /**
     * Find by id unit and id task.
     *
     * @param nIdUnit
     *            the n id unit
     * @param nIdTask
     *            the n id task
     * @param plugin
     *            the plugin
     * @return the list
     */
    public List<NotificationSignalementTaskConfigUnit> findByIdUnitAndIdTask( int nIdUnit, int nIdTask, Plugin plugin )
    {
        List<NotificationSignalementTaskConfigUnit> listConfigs = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_UNIT_AND_ID_TASK, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.setInt( 2, nIdTask );
        daoUtil.executeQuery( );

        int nPos = 0;

        while ( daoUtil.next( ) )
        {
            nPos = 0;
            NotificationSignalementTaskConfigUnit config = new NotificationSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setDestinataires( daoUtil.getString( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            config.setUnit( unit );

            listConfigs.add( config );
        }

        daoUtil.close( );

        return listConfigs;
    }

    /**
     * Find by id unit.
     *
     * @param nIdTypeSignalement
     *            the n id type signalement
     * @param plugin
     *            the plugin
     * @return the list of notificationSignalement task configuration
     */
    public List<NotificationSignalementTaskConfigUnit> findByIdTypeSignalement( int nIdTypeSignalement, Plugin plugin )
    {
        List<NotificationSignalementTaskConfigUnit> listConfigs = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_TYPE_SIGNALEMENT, plugin );
        daoUtil.setInt( 1, nIdTypeSignalement );
        daoUtil.executeQuery( );

        int nPos = 0;

        while ( daoUtil.next( ) )
        {
            nPos = 0;
            NotificationSignalementTaskConfigUnit config = new NotificationSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setDestinataires( daoUtil.getString( ++nPos ) );
            TypeSignalement type = new TypeSignalement( );
            type.setId( daoUtil.getInt( ++nPos ) );
            config.setTypeSignalement( type );

            listConfigs.add( config );
        }

        daoUtil.close( );

        return listConfigs;
    }

    /**
     * Delete.
     *
     * @param nIdTask
     *            the task id
     * @param nIdUnit
     *            the unit id
     * @param plugin
     *            the plugin
     */
    public void delete( int nIdTask, int nIdUnit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, nIdTask );
        daoUtil.setInt( nIndex, nIdUnit );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * Delete by type signalement.
     *
     * @param nIdTask
     *            the task id
     * @param nIdTypeSignalement
     *            the report type id
     * @param plugin
     *            the plugin
     */
    public void deleteByTypeSignalement( int nIdTask, int nIdTypeSignalement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_TYPE_SIGNALEMENT, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, nIdTask );
        daoUtil.setInt( nIndex, nIdTypeSignalement );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * Delete all.
     *
     * @param nIdTask
     *            the task id
     * @param plugin
     *            the plugin
     */
    public void deleteAll( int nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL, plugin );
        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

}
