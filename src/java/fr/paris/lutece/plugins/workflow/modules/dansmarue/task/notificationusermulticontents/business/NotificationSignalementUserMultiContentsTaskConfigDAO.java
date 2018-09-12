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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * NotificationSignalementUser3ContentsTaskConfigDAO class
 *
 */
public class NotificationSignalementUserMultiContentsTaskConfigDAO implements ITaskConfigDAO<NotificationSignalementUserMultiContentsTaskConfig>
{

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK                  = "SELECT nextval('seq_signalement_workflow_notifuser_multi_contents_config')";

    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY     = "SELECT id_message,subject,sender,title,message" + " FROM signalement_workflow_notifuser_multi_contents_config WHERE id_message=?";
    private static final String SQL_QUERY_INSERT                  = "INSERT INTO signalement_workflow_notifuser_multi_contents_config  " + "(id_message,subject,sender,title,message)VALUES(?,?,?,?,?)";
    private static final String SQL_QUERY_INSERT_TASK             = "INSERT INTO signalement_workflow_notifuser_multi_contents_task  " + "(id_task,id_message)VALUES(?,?)";
    private static final String SQL_QUERY_UPDATE                  = "UPDATE signalement_workflow_notifuser_multi_contents_config " + "SET subject=?,sender=?,title=?,message=?" + " WHERE id_message=?";
    private static final String SQL_QUERY_DELETE_MESSAGE          = "DELETE FROM signalement_workflow_notifuser_multi_contents_config WHERE id_message=? ";
    private static final String SQL_QUERY_DELETE_MESSAGE_LINK     = "DELETE FROM signalement_workflow_notifuser_multi_contents_task WHERE id_message=? and id_task=?";
    private static final String SQL_QUERY_SELECT_ALL_MESSAGE_TASK = "SELECT id_message from signalement_workflow_notifuser_multi_contents_task WHERE id_task=? ORDER BY id_message";

    /**
     * Generates a new primary key.
     *
     * @return The new primary key
     */
    private Long newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Long nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getLong( 1 );
        }
        daoUtil.free( );
        return nKey;
    }

    /**
     * Insert.
     *
     * @param config
     *         Task config
     * @param idTask
     *         the id task
     * @param plugin
     *         the plugin
     * @return the id message
     */
    public synchronized Long insert( NotificationSignalementUserMultiContentsTaskConfig config, Integer idTask, Plugin plugin )
    {
        if ( ( config.getIdMessage( ) == null ) || config.getIdMessage( ).equals( new Long( 0 ) ) )
        {
            config.setIdMessage( newPrimaryKey( ) );
        }

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nPos = 0;

        daoUtil.setLong( ++nPos, config.getIdMessage( ) );
        daoUtil.setString( ++nPos, config.getSubject( ) );
        daoUtil.setString( ++nPos, config.getSender( ) );
        daoUtil.setString( ++nPos, config.getTitle( ) );
        daoUtil.setString( ++nPos, config.getMessage( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        insertTask( idTask, config.getIdMessage( ), plugin );

        return config.getIdMessage( );

    }

    /**
     * Insert.
     * @param idTask
     *           the id task
     * @param idMessage
     *           the message id
     * @param plugin
     *          the plugin
     */
    private void insertTask( Integer idTask, Long idMessage, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_TASK, plugin );

        daoUtil.setInt( 1, idTask );
        daoUtil.setLong( 2, idMessage );

        daoUtil.executeUpdate( );
        daoUtil.free( );

    }

    /**
     * Update.
     *
     * @param config
     *          Task config
     * @param plugin
     *         the plugin
     */
    public void update( NotificationSignalementUserMultiContentsTaskConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nPos = 0;

        daoUtil.setString( ++nPos, config.getSubject( ) );
        daoUtil.setString( ++nPos, config.getSender( ) );
        daoUtil.setString( ++nPos, config.getTitle( ) );
        daoUtil.setString( ++nPos, config.getMessage( ) );
        daoUtil.setLong( ++nPos, config.getIdMessage( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }


    /**
     * Find by primary key.
     *
     * @param nIdMessage
     *          the id message.
     * @param plugin
     *         the plugin
     * @return task config
     */
    public NotificationSignalementUserMultiContentsTaskConfig findByPrimaryKey( Long nIdMessage, Plugin plugin )
    {
        NotificationSignalementUserMultiContentsTaskConfig userTaskConfig = new NotificationSignalementUserMultiContentsTaskConfig( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        daoUtil.setLong( 1, nIdMessage );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            userTaskConfig.setIdMessage( daoUtil.getLong( ++nPos ) );
            userTaskConfig.setSubject( daoUtil.getString( ++nPos ) );
            userTaskConfig.setSender( daoUtil.getString( ++nPos ) );
            userTaskConfig.setTitle( daoUtil.getString( ++nPos ) );
            userTaskConfig.setMessage( daoUtil.getString( ++nPos ) );
        }

        daoUtil.free( );

        return userTaskConfig;
    }

    /**
     * Delete message.
     *
     * @param nIdMessage
     *          the id message.
     * @param nIdTask
     *          the id task
     * @param plugin
     *          the plug in
     */
    public void deleteMessage( Long nIdMessage, Integer nIdTask, Plugin plugin )
    {
        deleteLinkMessageTask( nIdMessage, nIdTask, plugin );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_MESSAGE, plugin );

        daoUtil.setLong( 1, nIdMessage );
        daoUtil.executeUpdate( );
        daoUtil.free( );

    }

    /**
     * Delete link message.
     *
     * @param nIdMessage
     *          the id message.
     * @param nIdTask
     *          the id task
     * @param plugin
     *          the plug in
     */
    private void deleteLinkMessageTask( Long nIdMessage, Integer nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_MESSAGE_LINK, plugin );

        daoUtil.setLong( 1, nIdMessage );
        daoUtil.setInt( 2, nIdTask );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    @Override
    public void store( NotificationSignalementUserMultiContentsTaskConfig config )
    {
        update( config, null );
    }

    public NotificationSignalementUserMultiContentsTaskConfig load( Long nIdMessage )
    {
        return findByPrimaryKey( nIdMessage, null );
    }

    @Override
    public NotificationSignalementUserMultiContentsTaskConfig load( int nIdTask )
    {
        return null;
    }

    public void delete( int nIdTask, Plugin plugin )
    {
        deleteTask( nIdTask, plugin );
    }

    public List<Long> selectAllMessageTask( int nIdTask, Plugin plugin )
    {
        List<Long> listIdMessage = new ArrayList<>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_MESSAGE_TASK, plugin );

        daoUtil.setLong( 1, nIdTask );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            listIdMessage.add( daoUtil.getLong( nIndex ) );
        }

        return listIdMessage;
    }

    public void deleteTask( int nIdTask, Plugin plugin )
    {
        List<Long> listIdMessage = selectAllMessageTask( nIdTask, plugin );

        for ( Long idMessage : listIdMessage )
        {
            deleteMessage( idMessage, nIdTask, plugin );
        }
    }

    @Override
    public void delete( int nIdTask )
    {
        // Do nothing

    }

    @Override
    public void insert( NotificationSignalementUserMultiContentsTaskConfig config )
    {
        // Do nothing
    }
}
