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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * NotificationSuiviTaskConfigDAO.
 */
public class NotificationSuiviTaskConfigDAO implements ITaskConfigDAO<NotificationSuiviTaskConfig>
{

    /** The Constant SQL_QUERY_FIND_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task,subject, sender,mail_message "
            + " FROM signalement_workflow_notification_suivi_config WHERE id_task=?";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_workflow_notification_suivi_config  "
            + "(id_task,subject,sender,mail_message)VALUES(?,?,?,?)";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_workflow_notification_suivi_config "
            + "SET id_task=?,subject=?, sender=?, mail_message=?" + " WHERE id_task=?";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_workflow_notification_suivi_config WHERE id_task=? ";

    /**
     * Insert.
     *
     * @param config
     *            the task config
     * @param plugin
     *            the plugin
     */
    public synchronized void insert( NotificationSuiviTaskConfig config, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) ; )
        {

            int nIndex = 1;

            daoUtil.setInt( nIndex++, config.getIdTask( ) );
            daoUtil.setString( nIndex++, config.getSubject( ) );
            daoUtil.setString( nIndex++, config.getSender( ) );
            daoUtil.setString( nIndex, config.getMailMessage( ) );

            daoUtil.executeUpdate( );

            daoUtil.free( );
        }
    }

    /**
     * Update.
     *
     * @param config
     *            the task config
     * @param plugin
     *            the plugin
     */
    public void store( NotificationSuiviTaskConfig config, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) ; )
        {

            int nIndex = 1;

            daoUtil.setInt( nIndex++, config.getIdTask( ) );
            daoUtil.setString( nIndex++, config.getSubject( ) );
            daoUtil.setString( nIndex++, config.getSender( ) );
            daoUtil.setString( nIndex++, config.getMailMessage( ) );
            daoUtil.setInt( nIndex, config.getIdTask( ) );

            daoUtil.executeUpdate( );

            daoUtil.free( );
        }
    }

    /**
     * retrieve a notification task with id id.
     *
     * @param nIdTask
     *            the id task
     * @param plugin
     *            the plugin
     * @return the notification task
     */
    public NotificationSuiviTaskConfig load( int nIdTask, Plugin plugin )
    {
        NotificationSuiviTaskConfig suiviTaskConfig = new NotificationSuiviTaskConfig( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin ) ; )
        {

            int nIndex = 1;

            daoUtil.setInt( nIndex++, nIdTask );

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nIndex = 1;
                suiviTaskConfig.setIdTask( daoUtil.getInt( nIndex++ ) );
                suiviTaskConfig.setSubject( daoUtil.getString( nIndex++ ) );
                suiviTaskConfig.setSender( daoUtil.getString( nIndex++ ) );
                suiviTaskConfig.setMailMessage( daoUtil.getString( nIndex ) );
            }

            daoUtil.free( );
        }

        return suiviTaskConfig;
    }

    /**
     * Delete a notifications task.
     *
     * @param nIdTask
     *            the id task
     * @param plugin
     *            the plugin
     */
    public void delete( int nIdTask, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) ; )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex, nIdTask );

            daoUtil.executeUpdate( );

            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( NotificationSuiviTaskConfig config )
    {
        insert( config, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( NotificationSuiviTaskConfig config )
    {
        store( config, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationSuiviTaskConfig load( int nIdTask )
    {
        return load( nIdTask, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdTask )
    {
        delete( nIdTask, null );
    }

}
