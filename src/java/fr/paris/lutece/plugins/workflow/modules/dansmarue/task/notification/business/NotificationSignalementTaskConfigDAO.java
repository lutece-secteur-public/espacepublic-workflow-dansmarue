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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * NotificationSignalementTaskConfigDAO class
 * 
 */
public class NotificationSignalementTaskConfigDAO implements ITaskConfigDAO<NotificationSignalementTaskConfig>
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task,subject,message,sender "
            + " FROM signalement_workflow_notification_config WHERE id_task=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_workflow_notification_config  "
            + "(id_task,subject,message,sender) VALUES(?,?,?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_workflow_notification_config "
            + "SET id_task=?,subject=?,message=?,sender=?" + " WHERE id_task=?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_workflow_notification_config WHERE id_task=? ";

    /**
     * {@inheritDoc}
     */
    public synchronized void insert( NotificationSignalementTaskConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getSubject( ) );
        daoUtil.setString( ++nPos, config.getMessage( ) );
        daoUtil.setString( ++nPos, config.getSender( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    public void update( NotificationSignalementTaskConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getSubject( ) );
        daoUtil.setString( ++nPos, config.getMessage( ) );
        daoUtil.setString( ++nPos, config.getSender( ) );
        daoUtil.setInt( ++nPos, config.getIdTask( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    public NotificationSignalementTaskConfig findByPrimaryKey( int nIdTask, Plugin plugin )
    {
        NotificationSignalementTaskConfig taskConfig = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            taskConfig = new NotificationSignalementTaskConfig( );
            taskConfig.setIdTask( daoUtil.getInt( ++nPos ) );
            taskConfig.setSubject( daoUtil.getString( ++nPos ) );
            taskConfig.setMessage( daoUtil.getString( ++nPos ) );
            taskConfig.setSender( daoUtil.getString( ++nPos ) );
        }

        daoUtil.free( );

        return taskConfig;
    }

    /**
     * {@inheritDoc}
     */
    public void delete( int nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    @Override
    public void insert( NotificationSignalementTaskConfig config )
    {
        insert( config, null );
    }

    @Override
    public void store( NotificationSignalementTaskConfig config )
    {
        update( config, null );
    }

    @Override
    public NotificationSignalementTaskConfig load( int nIdTask )
    {
        return findByPrimaryKey( nIdTask, null );
    }

    @Override
    public void delete( int nIdTask )
    {
        delete( nIdTask, null );
    }
}
