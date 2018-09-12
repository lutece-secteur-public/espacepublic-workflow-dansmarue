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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * NotificationSignalementUserTaskConfigDAO class
 *
 */
public class NotificationSignalementUserTaskConfigDAO implements ITaskConfigDAO<NotificationSignalementUserTaskConfig>
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task,subject,sender,title,message " + " FROM signalement_workflow_notification_user_config WHERE id_task=?";
    private static final String SQL_QUERY_INSERT              = "INSERT INTO signalement_workflow_notification_user_config  " + "(id_task,subject,sender,message)VALUES(?,?,?,?)";
    private static final String SQL_QUERY_UPDATE              = "UPDATE signalement_workflow_notification_user_config " + "SET id_task=?,subject=?,sender=?,message=?" + " WHERE id_task=?";
    private static final String SQL_QUERY_DELETE              = "DELETE FROM signalement_workflow_notification_user_config WHERE id_task=? ";

    /**
     * Insert.
     *
     * @param config
     *            the task config
     * @param plugin
     *            the plugin
     */
    public synchronized void insert( NotificationSignalementUserTaskConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getSubject( ) );
        daoUtil.setString( ++nPos, config.getSender( ) );
        daoUtil.setString( ++nPos, config.getMessage( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Update.
     *
     * @param config
     *            the task config
     * @param plugin
     *            the plugin
     */
    public void update( NotificationSignalementUserTaskConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getSubject( ) );
        daoUtil.setString( ++nPos, config.getSender( ) );
        daoUtil.setString( ++nPos, config.getMessage( ) );
        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * findByPrimaryKey.
     * @param nIdTask
     *          id task
     * @param plugin
     *         the plugin
     * @return NotificationSignalementUserTaskConfig
     */
    public NotificationSignalementUserTaskConfig findByPrimaryKey( int nIdTask, Plugin plugin )
    {
        NotificationSignalementUserTaskConfig userTaskConfig = new NotificationSignalementUserTaskConfig( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            userTaskConfig.setIdTask( daoUtil.getInt( ++nPos ) );
            userTaskConfig.setSubject( daoUtil.getString( ++nPos ) );
            userTaskConfig.setSender( daoUtil.getString( ++nPos ) );
            userTaskConfig.setTitle( daoUtil.getString( ++nPos ) );
            userTaskConfig.setMessage( daoUtil.getString( ++nPos ) );
        }

        daoUtil.free( );

        return userTaskConfig;
    }

    /**
     * delete.
     *
     * @param nIdTask
     *          id task
     * @param plugin
     *         the plugin
     */
    public void delete( int nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    @Override
    public void insert( NotificationSignalementUserTaskConfig config )
    {
        insert( config, null );
    }

    @Override
    public void store( NotificationSignalementUserTaskConfig config )
    {
        update( config, null );
    }

    @Override
    public NotificationSignalementUserTaskConfig load( int nIdTask )
    {
        return findByPrimaryKey( nIdTask, null );
    }

    @Override
    public void delete( int nIdTask )
    {
        delete( nIdTask, null );
    }

}
