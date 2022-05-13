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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * class NotificationUserValueDAO.
 */
public class NotificationUserValueDAO
{

    /** The Constant SQL_QUERY_FIND_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_history,id_task,notification_value  "
            + "FROM signalement_workflow_notification_user_value WHERE id_history=? AND id_task=?";

    /** The Constant SQL_QUERY_LOAD_BY_HISTORY. */
    private static final String SQL_QUERY_LOAD_BY_HISTORY = "SELECT id_history,id_task,notification_value  "
            + "FROM signalement_workflow_notification_user_value WHERE id_history=? ";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO  signalement_workflow_notification_user_value "
            + "(id_history,id_task,notification_value )VALUES(?,?,?)";

    /** The Constant SQL_QUERY_DELETE_BY_HISTORY. */
    private static final String SQL_QUERY_DELETE_BY_HISTORY = "DELETE FROM signalement_workflow_notification_user_value  WHERE id_history=? AND id_task=?";

    /** The Constant SQL_QUERY_DELETE_BY_TASK. */
    private static final String SQL_QUERY_DELETE_BY_TASK = "DELETE FROM signalement_workflow_notification_user_value  WHERE id_task=?";

    /**
     * Insert.
     *
     * @param notificationUserValue
     *            notification to insert
     */
    public synchronized void insert( NotificationUserValue notificationUserValue )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) ; )
        {

            int nPos = 0;

            daoUtil.setInt( ++nPos, notificationUserValue.getIdResourceHistory( ) );
            daoUtil.setInt( ++nPos, notificationUserValue.getIdTask( ) );
            daoUtil.setString( ++nPos, notificationUserValue.getValue( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * retrieve notification value.
     *
     * @param nIdHistory
     *            the id history
     * @param nIdTask
     *            the id task
     * @param plugin
     *            the plugin
     * @return notification value
     */
    public NotificationUserValue load( int nIdHistory, int nIdTask, Plugin plugin )
    {
        NotificationUserValue notificationValue = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin ) ; )
        {
            int nPos = 0;
            daoUtil.setInt( ++nPos, nIdHistory );
            daoUtil.setInt( ++nPos, nIdTask );

            nPos = 0;

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                notificationValue = new NotificationUserValue( );
                notificationValue.setIdResourceHistory( daoUtil.getInt( ++nPos ) );
                notificationValue.setIdTask( daoUtil.getInt( ++nPos ) );
                notificationValue.setValue( daoUtil.getString( ++nPos ) );
            }

            daoUtil.free( );
        }

        return notificationValue;
    }

    /**
     * Delete notification.
     *
     * @param nIdHistory
     *            the id history
     * @param nIdTask
     *            the id task
     * @param plugin
     *            the plugin
     */
    public void deleteByHistory( int nIdHistory, int nIdTask, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_HISTORY, plugin ) ; )
        {
            int nPos = 0;
            daoUtil.setInt( ++nPos, nIdHistory );
            daoUtil.setInt( ++nPos, nIdTask );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Delete notification.
     *
     * @param nIdTask
     *            the id task
     * @param plugin
     *            the plugin
     */
    public void deleteByTask( int nIdTask, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_TASK, plugin ) ; )
        {
            int nPos = 0;
            daoUtil.setInt( ++nPos, nIdTask );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Load notification.
     *
     * @param nIdHistory
     *            the id history
     * @return the notification value
     */
    public NotificationUserValue loadByHistory( int nIdHistory )
    {
        NotificationUserValue notificationValue = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_BY_HISTORY ) ; )
        {
            int nPos = 0;
            daoUtil.setInt( ++nPos, nIdHistory );

            nPos = 0;

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                notificationValue = new NotificationUserValue( );
                notificationValue.setIdResourceHistory( daoUtil.getInt( ++nPos ) );
                notificationValue.setIdTask( daoUtil.getInt( ++nPos ) );
                notificationValue.setValue( daoUtil.getString( ++nPos ) );
            }

            daoUtil.free( );
        }
        return notificationValue;
    }
}
