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

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * WebServiceSignalementTaskConfigUnitDAO.
 */
public class WebServiceSignalementTaskConfigUnitDAO
{

    /** The Constant SQL_QUERY_FIND_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task, id_unit, prestatairesansws, urlprestataire "
            + " FROM signalement_workflow_webservice_config_unit WHERE id_task=? AND id_unit=?";

    /** The Constant SQL_QUERY_FIND_BY_ID_TASK. */
    private static final String SQL_QUERY_FIND_BY_ID_TASK = "SELECT id_task, id_unit, prestatairesansws, urlprestataire"
            + " FROM signalement_workflow_webservice_config_unit WHERE id_task=?";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_workflow_webservice_config_unit "
            + "(id_task, id_unit, prestatairesansws, urlprestataire) VALUES(?,?,?,?)";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_workflow_webservice_config_unit "
            + "SET id_task=?, id_unit=?, prestatairesansws=?, urlprestataire=? WHERE id_task=? AND id_unit=?";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_workflow_webservice_config_unit WHERE id_task=? AND id_unit=?";

    /** The Constant SQL_QUERY_DELETE_ALL. */
    private static final String SQL_QUERY_DELETE_ALL = "DELETE FROM signalement_workflow_webservice_config_unit WHERE id_task=?";

    /** The Constant SQL_QUERY_FIND_BY_ID_UNIT. */
    private static final String SQL_QUERY_FIND_BY_ID_UNIT = "SELECT id_task, id_unit, prestatairesansws, urlprestataire "
            + " FROM signalement_workflow_webservice_config_unit WHERE id_unit=?";

    /** The Constant SQL_QUERY_FIND_BY_SECTOR_FOR_CHILD. */
    private static final String SQL_QUERY_FIND_BY_SECTOR_FOR_CHILD = "select  distinct(id_task), unit.id_unit, prestatairesansws, urlprestataire from signalement_signalement signalement "
            + "join unittree_unit_sector sector on sector.id_sector = signalement.fk_id_sector " + "join unittree_unit unit on unit.id_unit=sector.id_unit "
            + "join signalement_workflow_webservice_config_unit conf on conf.id_unit=unit.id_unit " + "where id_task =?  and id_sector=? and unit.id_parent<>0";

    /** The Constant SQL_QUERY_FIND_BY_SECTOR_FOR_PARENT. */
    private static final String SQL_QUERY_FIND_BY_SECTOR_FOR_PARENT = "select distinct(id_task), unit.id_unit, prestatairesansws, urlprestataire from signalement_signalement signalement "
            + "join unittree_unit_sector sector on sector.id_sector = signalement.fk_id_sector " + "join unittree_unit unit on unit.id_unit=sector.id_unit "
            + "join signalement_workflow_webservice_config_unit conf on conf.id_unit=unit.id_unit " + "where id_task =?  and id_sector=? and unit.id_parent=0";

    /**
     * Insert.
     *
     * @param config
     *            the task configuration
     * @param plugin
     *            the plugin
     */
    public void insert( WebServiceSignalementTaskConfigUnit config, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) ; )
        {

            int nPos = 0;
            daoUtil.setInt( ++nPos, config.getIdTask( ) );
            daoUtil.setInt( ++nPos, config.getUnit( ).getIdUnit( ) );
            daoUtil.setString( ++nPos, config.getPrestataireSansWS( ) );
            daoUtil.setString( ++nPos, config.getUrlPrestataire( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Update.
     *
     * @param config
     *            the task configuration
     * @param plugin
     *            the plugin
     */
    public void update( WebServiceSignalementTaskConfigUnit config, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) ; )
        {

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
     * @return webserviceSignalementTask configuration
     */
    public WebServiceSignalementTaskConfigUnit findByPrimaryKey( int nIdTask, int nIdUnit, Plugin plugin )
    {
        WebServiceSignalementTaskConfigUnit config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin ) ; )
        {

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
        }
        return config;
    }

    /**
     * Find by id task.
     *
     * @param nIdTask
     *            the task id
     * @param plugin
     *            the plugin
     * @return the list of webserviceSignalement task configuration
     */
    public List<WebServiceSignalementTaskConfigUnit> findByIdTask( int nIdTask, Plugin plugin )
    {
        List<WebServiceSignalementTaskConfigUnit> listConfigs = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_TASK, plugin ) ; )
        {
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
        }

        return listConfigs;
    }

    /**
     * Find by id unit.
     *
     * @param nIdUnit
     *            the unit id
     * @param plugin
     *            the plugin
     * @return the list of webserviceSignalement task configuration
     */
    public List<WebServiceSignalementTaskConfigUnit> findByIdUnit( int nIdUnit, Plugin plugin )
    {
        List<WebServiceSignalementTaskConfigUnit> listConfigs = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_UNIT, plugin ) ; )
        {
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
        }

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
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) ; )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.setInt( 2, nIdUnit );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
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
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL, plugin ) ; )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Find by sector.
     *
     * @param nIdTask
     *            the n id task
     * @param idSector
     *            the id sector
     * @param plugin
     *            the plugin
     * @param isParent
     *            the is parent
     * @return the web service signalement task config unit
     */
    public WebServiceSignalementTaskConfigUnit findBySector( int nIdTask, int idSector, Plugin plugin, boolean isParent )
    {
        WebServiceSignalementTaskConfigUnit config = null;

        DAOUtil daoUtil;

        if ( isParent )
        {
            daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_SECTOR_FOR_PARENT, plugin );
        }
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_SECTOR_FOR_CHILD, plugin );
        }

        try
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.setInt( 2, idSector );

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
        }
        finally
        {
            daoUtil.close( );
        }

        return config;
    }

}
