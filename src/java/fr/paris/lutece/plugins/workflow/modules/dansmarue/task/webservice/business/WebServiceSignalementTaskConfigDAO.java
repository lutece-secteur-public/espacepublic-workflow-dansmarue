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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * WebServiceSignalementTaskConfigDAO.
 */
public class WebServiceSignalementTaskConfigDAO implements ITaskConfigDAO<WebServiceSignalementTaskConfig>
{

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_workflow_webservice_config  WHERE  id_task = ?";

    /** The Constant SQL_QUERY_FIND_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task, id_state_withws_success, id_state_withws_failure, id_state_withoutws FROM signalement_workflow_webservice_config WHERE id_task=?";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_workflow_webservice_config (id_task, id_state_withws_success, id_state_withws_failure, id_state_withoutws) VALUES (?, ?, ?, ?)";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_workflow_webservice_config SET  id_state_withws_success = ?, id_state_withws_failure = ?, id_state_withoutWS = ? WHERE id_task = ?";

    /**
     * Find By Primary Key.
     *
     * @param nPrimaryKey
     *            the primary key
     * @param plugin
     *            the plugin
     * @return the task config
     */
    public WebServiceSignalementTaskConfig findByPrimaryKey( int nPrimaryKey, Plugin plugin )
    {
        WebServiceSignalementTaskConfig config = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin ) ; )
        {
            daoUtil.setInt( 1, nPrimaryKey );

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                config = new WebServiceSignalementTaskConfig( );

                int nIndex = 1;
                config.setIdTask( daoUtil.getInt( nIndex++ ) );
                config.setStateWithWSSuccess( daoUtil.getInt( nIndex++ ) );
                config.setStateWithWSFailure( daoUtil.getInt( nIndex++ ) );
                config.setStateWithoutWS( daoUtil.getInt( nIndex ) );
            }

            daoUtil.free( );
        }
        return config;
    }

    /**
     * Insert task config.
     *
     * @param config
     *            task config to insert
     * @param plugin
     *            the plugin
     */
    public void insert( WebServiceSignalementTaskConfig config, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) ; )
        {

            int nIndex = 1;
            daoUtil.setInt( nIndex++, config.getIdTask( ) );
            daoUtil.setInt( nIndex++, config.getStateWithWSSuccess( ) );
            daoUtil.setInt( nIndex++, config.getStateWithWSFailure( ) );
            daoUtil.setInt( nIndex, config.getStateWithoutWS( ) );

            daoUtil.executeUpdate( );

            daoUtil.free( );
        }
    }

    /**
     * Delete task config.
     *
     * @param nPrimaryKey
     *            the primary key
     * @param plugin
     *            the plugin
     */
    public void delete( int nPrimaryKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) ; )
        {
            daoUtil.setInt( 1, nPrimaryKey );

            daoUtil.executeUpdate( );

            daoUtil.free( );
        }
    }

    /**
     * Update task config.
     *
     * @param config
     *            task config to update
     * @param plugin
     *            the plugin
     */
    public void update( WebServiceSignalementTaskConfig config, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) ; )
        {

            int nIndex = 1;
            daoUtil.setInt( nIndex++, config.getStateWithWSSuccess( ) );
            daoUtil.setInt( nIndex++, config.getStateWithWSFailure( ) );
            daoUtil.setInt( nIndex++, config.getStateWithoutWS( ) );
            daoUtil.setInt( nIndex, config.getIdTask( ) );

            daoUtil.executeUpdate( );

            daoUtil.free( );
        }
    }

    /**
     * Insert.
     *
     * @param config
     *            the config
     */
    @Override
    public void insert( WebServiceSignalementTaskConfig config )
    {
        insert( config, null );
    }

    /**
     * Store.
     *
     * @param config
     *            the config
     */
    @Override
    public void store( WebServiceSignalementTaskConfig config )
    {
        update( config, null );
    }

    /**
     * Load.
     *
     * @param nIdTask
     *            the n id task
     * @return the web service signalement task config
     */
    @Override
    public WebServiceSignalementTaskConfig load( int nIdTask )
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
        delete( nIdTask, null );
    }
}
