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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.programme.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * NotificationSignalementTaskConfigDAO class
 *
 */
public class NotificationServiceProgrammeSignalementTaskConfigDAO implements ITaskConfigDAO<NotificationServiceProgrammeSignalementTaskConfig>
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task,subject,message,sender "
            + " FROM signalement_workflow_notification_service_programme_config WHERE id_task=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_workflow_notification_service_programme_config  "
            + "(id_task,subject,message,sender) VALUES(?,?,?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_workflow_notification_service_programme_config "
            + "SET id_task=?,subject=?,message=?,sender=?" + " WHERE id_task=?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_workflow_notification_service_programme_config WHERE id_task=? ";
    private static final String SQL_QUERY_DESTINATAIRE_SP = "select distinct(destinataires) from signalement_workflow_notification_config_unit "
            + "where id_type_signalement in (select id_type_signalement from  v_signalement_type_signalement_with_parents_links where id_parent= ?) or id_type_signalement=? ";
    private static final String SQL_QUERY_INSERT_NOTIFICATION = "INSERT INTO  signalement_workflow_notification_service_programme (id_history,id_task, message )VALUES(?,?,?)";
    private static final String SQL_QUERY_GET_NOTIFICATION = "select message from signalement_workflow_notification_service_programme where id_history=? and id_task=?";

    /**
     * {@inheritDoc}
     */

    @Override
    public synchronized void insert( NotificationServiceProgrammeSignalementTaskConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getSubject( ) );
        daoUtil.setString( ++nPos, config.getMessage( ) );
        daoUtil.setString( ++nPos, config.getSender( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( NotificationServiceProgrammeSignalementTaskConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setString( ++nPos, config.getSubject( ) );
        daoUtil.setString( ++nPos, config.getMessage( ) );
        daoUtil.setString( ++nPos, config.getSender( ) );
        daoUtil.setInt( ++nPos, config.getIdTask( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationServiceProgrammeSignalementTaskConfig load( int nIdTask )
    {
        NotificationServiceProgrammeSignalementTaskConfig taskConfig = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            taskConfig = new NotificationServiceProgrammeSignalementTaskConfig( );
            taskConfig.setIdTask( daoUtil.getInt( ++nPos ) );
            taskConfig.setSubject( daoUtil.getString( ++nPos ) );
            taskConfig.setMessage( daoUtil.getString( ++nPos ) );
            taskConfig.setSender( daoUtil.getString( ++nPos ) );
        }

        daoUtil.close( );

        return taskConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdTask )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * Gets the destinataire service programme.
     *
     * @param idsUnit
     *            the id unit
     * @param idTypeSignalement
     *            the id type signalement
     * @return the destinataire service programme
     */
    public List<String> getDestinataireServiceProgramme( List<Integer> idsUnit, int idTypeSignalement )
    {
        List<String> result = new ArrayList<>( );

        StringBuilder stringBuilder = new StringBuilder( );

        stringBuilder.append( SQL_QUERY_DESTINATAIRE_SP );

        if ( !idsUnit.isEmpty( ) )
        {
            Object [ ] qMarks = new Object [ idsUnit.size( )];
            for ( int i = 0; i < idsUnit.size( ); i++ )
            {
                qMarks [i] = '?';
            }
            String joinQuestionMarks = StringUtils.join( qMarks, ", " );
            stringBuilder.append( " or id_unit in (" + joinQuestionMarks + ") " );
        }

        DAOUtil daoUtil = new DAOUtil( stringBuilder.toString( ) );

        int nIndex = 0;
        daoUtil.setInt( ++nIndex, idTypeSignalement );
        daoUtil.setInt( ++nIndex, idTypeSignalement );

        for ( int idUnit : idsUnit )
        {
            daoUtil.setInt( ++nIndex, idUnit );
        }

        daoUtil.executeQuery( );

        // Pour chaque resultat retourne
        while ( daoUtil.next( ) )
        {
            result.add( daoUtil.getString( 1 ) );
        }

        daoUtil.close( );

        return result;
    }

    /**
     * Insert notification.
     *
     * @param idHistory
     *            the id history
     * @param idTtask
     *            the id ttask
     * @param message
     *            the message
     */
    public void insertNotification( int idHistory, int idTtask, String message )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_NOTIFICATION ) ; )
        {

            int nPos = 0;

            daoUtil.setInt( ++nPos, idHistory );
            daoUtil.setInt( ++nPos, idTtask );
            daoUtil.setString( ++nPos, message );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Gets the notification.
     *
     * @param idHistory
     *            the id history
     * @param idTtask
     *            the id ttask
     * @return the notification
     */
    public String getNotification( int idHistory, int idTtask )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_NOTIFICATION );

        int nIndex = 0;

        daoUtil.setInt( ++nIndex, idHistory );
        daoUtil.setInt( ++nIndex, idTtask );

        daoUtil.executeQuery( );

        int nPos = 0;
        String notification = "";

        if ( daoUtil.next( ) )
        {
            notification = daoUtil.getString( ++nPos );
        }

        daoUtil.close( );

        return notification;
    }

}
