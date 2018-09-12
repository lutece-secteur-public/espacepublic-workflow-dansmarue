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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * WebServiceSignalementTaskConfigUnitDAO
 *
 */
public class WebServiceSignalementTaskConfigUnitDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task, id_unit, prestatairesansws, urlprestataire "
            + " FROM signalement_workflow_webservice_config_unit WHERE id_task=? AND id_unit=?";
    private static final String SQL_QUERY_FIND_BY_ID_TASK     = "SELECT id_task, id_unit, prestatairesansws, urlprestataire" + " FROM signalement_workflow_webservice_config_unit WHERE id_task=?";
    private static final String SQL_QUERY_INSERT              = "INSERT INTO signalement_workflow_webservice_config_unit " + "(id_task, id_unit, prestatairesansws, urlprestataire) VALUES(?,?,?,?)";
    private static final String SQL_QUERY_UPDATE              = "UPDATE signalement_workflow_webservice_config_unit "
            + "SET id_task=?, id_unit=?, prestatairesansws=?, urlprestataire=? WHERE id_task=? AND id_unit=?";
    private static final String SQL_QUERY_DELETE              = "DELETE FROM signalement_workflow_webservice_config_unit WHERE id_task=? AND id_unit=?";
    private static final String SQL_QUERY_DELETE_ALL          = "DELETE FROM signalement_workflow_webservice_config_unit WHERE id_task=?";
    private static final String SQL_QUERY_FIND_BY_ID_UNIT     = "SELECT id_task, id_unit, prestatairesansws, urlprestataire " + " FROM signalement_workflow_webservice_config_unit WHERE id_unit=?";

    /**
     *
     * @param config
     *            the task configuration
     * @param plugin
     *            the plugin
     */
    public void insert( WebServiceSignalementTaskConfigUnit config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nPos = 0;
        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setInt( ++nPos, config.getUnit( ).getIdUnit( ) );
        daoUtil.setString( ++nPos, config.getPrestataireSansWS( ) );
        daoUtil.setString( ++nPos, config.getUrlPrestataire( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     *
     * @param config
     *            the task configuration
     * @param plugin
     *            the plugin
     */
    public void update( WebServiceSignalementTaskConfigUnit config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nPos = 0;
        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setInt( ++nPos, config.getUnit( ).getIdUnit( ) );
        daoUtil.setString( ++nPos, config.getPrestataireSansWS( ) );
        daoUtil.setString( ++nPos, config.getUrlPrestataire( ) );

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setInt( ++nPos, config.getUnit( ).getIdUnit( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     *
     * @param nIdTask
     *            the task id
     * @param nIdUnit
     *            the unit id
     * @param plugin
     *            the plugin
     * @return webserviceSignalementTask configuration
     */
    public WebServiceSignalementTaskConfigUnit findByPrimaryKey( int nIdTask, int nIdUnit, Plugin plugin )
    {
        WebServiceSignalementTaskConfigUnit config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        daoUtil.setInt( 1, nIdTask );
        daoUtil.setInt( 2, nIdUnit );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            config = new WebServiceSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            config.setUnit( unit );
            config.setPrestataireSansWS( daoUtil.getString( ++nPos ) );
            config.setUrlPrestataire( daoUtil.getString( ++nPos ) );
        }

        daoUtil.free( );

        return config;
    }

    /**
     *
     * @param nIdTask
     *            the task id
     * @param plugin
     *            the plugin
     * @return the list of webserviceSignalement task configuration
     */
    public List<WebServiceSignalementTaskConfigUnit> findByIdTask( int nIdTask, Plugin plugin )
    {
        List<WebServiceSignalementTaskConfigUnit> listConfigs = new ArrayList<WebServiceSignalementTaskConfigUnit>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_TASK, plugin );
        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeQuery( );

        int nPos = 0;

        while ( daoUtil.next( ) )
        {
            nPos = 0;
            WebServiceSignalementTaskConfigUnit config = new WebServiceSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            config.setUnit( unit );
            config.setPrestataireSansWS( daoUtil.getString( ++nPos ) );
            config.setUrlPrestataire( daoUtil.getString( ++nPos ) );

            listConfigs.add( config );
        }

        daoUtil.free( );

        return listConfigs;
    }

    /**
     * Find by id unit
     *
     * @param nIdUnit
     *            the unit id
     * @param plugin
     *            the plugin
     * @return the list of webserviceSignalement task configuration
     */
    public List<WebServiceSignalementTaskConfigUnit> findByIdUnit( int nIdUnit, Plugin plugin )
    {
        List<WebServiceSignalementTaskConfigUnit> listConfigs = new ArrayList<WebServiceSignalementTaskConfigUnit>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeQuery( );

        int nPos = 0;

        while ( daoUtil.next( ) )
        {
            nPos = 0;
            WebServiceSignalementTaskConfigUnit config = new WebServiceSignalementTaskConfigUnit( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( ++nPos ) );
            config.setUnit( unit );
            config.setPrestataireSansWS( daoUtil.getString( ++nPos ) );
            config.setUrlPrestataire( daoUtil.getString( ++nPos ) );

            listConfigs.add( config );
        }

        daoUtil.free( );

        return listConfigs;
    }

    /**
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
        daoUtil.setInt( 1, nIdTask );
        daoUtil.setInt( 2, nIdUnit );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
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
        daoUtil.free( );
    }

}
