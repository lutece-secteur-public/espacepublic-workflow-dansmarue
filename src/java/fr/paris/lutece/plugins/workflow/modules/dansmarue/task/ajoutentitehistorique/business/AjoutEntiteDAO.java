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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.ajoutentitehistorique.business;

import fr.paris.lutece.util.sql.DAOUtil;

/**
 * NotificationSuiviValueDAO.
 */
public class AjoutEntiteDAO
{

    /** The Constant SQL_QUERY_FIND_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT entite FROM signalement_workflow_entite_value WHERE id_history=? AND id_task=?";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO  signalement_workflow_entite_value " + "(id_history,id_task,entite) VALUES(?,?,?)";

    /**
     * Insert.
     *
     * @param idResourceHistory
     *            the id resource history
     * @param idTask
     *            the id task
     * @param entiteValue
     *            the entite value
     */
    public synchronized void insert( Integer idResourceHistory, Integer idTask, String entiteValue )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        int nPos = 0;

        daoUtil.setInt( ++nPos, idResourceHistory );
        daoUtil.setInt( ++nPos, idTask );
        daoUtil.setString( ++nPos, entiteValue );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * retrieve a notification with id id task and id history.
     *
     * @param nIdHistory
     *            the id history
     * @param nIdTask
     *            the id task
     * @return the notification
     */
    public String load( int nIdHistory, int nIdTask )
    {
        String entite = "";

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY );
        int nPos = 0;
        daoUtil.setInt( ++nPos, nIdHistory );
        daoUtil.setInt( ++nPos, nIdTask );

        nPos = 0;

        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            entite = daoUtil.getString( ++nPos );
        }

        daoUtil.close( );

        return entite;
    }
}
