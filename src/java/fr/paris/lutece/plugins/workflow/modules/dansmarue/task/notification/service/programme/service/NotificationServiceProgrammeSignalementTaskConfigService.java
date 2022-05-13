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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.programme.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.programme.business.NotificationServiceProgrammeSignalementTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.programme.business.NotificationServiceProgrammeSignalementTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.programme.business.NotificationServiceProgrammeSignalementTaskConfigDTO;

/**
 * NotificationSignalementTaskConfigService class
 *
 */
public class NotificationServiceProgrammeSignalementTaskConfigService
{
    // DAO
    @Inject
    @Named( "signalement.notificationServiceProgrammeSignalementTaskConfigDAO" )
    private NotificationServiceProgrammeSignalementTaskConfigDAO _notificationSignalementTaskConfigDAO;

    /**
     * Add a new NotificationSignalementTask configuration
     *
     * @param configDTO
     *            the NotificationSignalementTask configuration
     */
    public void insert( NotificationServiceProgrammeSignalementTaskConfigDTO configDTO )
    {
        NotificationServiceProgrammeSignalementTaskConfig config = new NotificationServiceProgrammeSignalementTaskConfig( );
        config.setIdTask( configDTO.getIdTask( ) );
        config.setSubject( configDTO.getSubject( ) );
        config.setMessage( configDTO.getMessage( ) );
        config.setSender( configDTO.getSender( ) );
        _notificationSignalementTaskConfigDAO.insert( config );
    }

    /**
     * Update a NotificationSignalementTask configuration
     *
     * @param configDTO
     *            the NotificationServiceProgrammeSignalementTaskConfigDTO
     */
    public void update( NotificationServiceProgrammeSignalementTaskConfigDTO configDTO )
    {
        NotificationServiceProgrammeSignalementTaskConfig config = new NotificationServiceProgrammeSignalementTaskConfig( );
        config.setIdTask( configDTO.getIdTask( ) );
        config.setSubject( configDTO.getSubject( ) );
        config.setMessage( configDTO.getMessage( ) );
        config.setSender( configDTO.getSender( ) );
        _notificationSignalementTaskConfigDAO.store( config );
    }

    /**
     * Find a NotificationServiceProgrammeSignalementTaskConfigDTO by id
     *
     * @param nIdTask
     *            the task id
     * @return configDTO
     */
    public NotificationServiceProgrammeSignalementTaskConfigDTO findByPrimaryKey( int nIdTask )
    {
        NotificationServiceProgrammeSignalementTaskConfigDTO configDTO = new NotificationServiceProgrammeSignalementTaskConfigDTO( );

        NotificationServiceProgrammeSignalementTaskConfig config = _notificationSignalementTaskConfigDAO.load( nIdTask );
        if ( config != null )
        {
            configDTO = new NotificationServiceProgrammeSignalementTaskConfigDTO( );
            configDTO.setIdTask( config.getIdTask( ) );
            configDTO.setSubject( config.getSubject( ) );
            configDTO.setMessage( config.getMessage( ) );
            configDTO.setSender( config.getSender( ) );
        }

        return configDTO;
    }

    /**
     *
     * @param nIdTask
     *            the task id
     */
    public void delete( int nIdTask )
    {
        _notificationSignalementTaskConfigDAO.delete( nIdTask );
    }

    /**
     * Gets the destinataire.
     *
     * @param idsUnit
     *            the id unit
     * @param idTypeSignalement
     *            the id type signalement
     * @return the destinataire
     */
    public List<String> getDestinataireServiceProgramme( List<Integer> idsUnit, int idTypeSignalement )
    {
        List<String> destinataires = _notificationSignalementTaskConfigDAO.getDestinataireServiceProgramme( idsUnit, idTypeSignalement );
        List<String> destinatairesDistinct = new ArrayList<>( );

        // Il est possible d'avoir des destinataires concaténés
        for ( String destinataire : destinataires )
        {
            if ( destinataire.contains( ";" ) )
            {
                String [ ] tabDestinataires = destinataire.split( ";" );
                for ( String email : tabDestinataires )
                {
                    if ( !destinatairesDistinct.contains( email.trim( ) ) )
                    {
                        destinatairesDistinct.add( email.trim( ) );
                    }
                }
            }
            else
            {
                if ( !destinatairesDistinct.contains( destinataire.trim( ) ) )
                {
                    destinatairesDistinct.add( destinataire.trim( ) );
                }
            }
        }

        return destinatairesDistinct;
    }

    /**
     * Insert notification.
     *
     * @param idHistory
     *            the id history
     * @param idTask
     *            the id task
     * @param message
     *            the message
     */
    public void insertNotification( int idHistory, int idTask, String message )
    {
        _notificationSignalementTaskConfigDAO.insertNotification( idHistory, idTask, message );
    }

    /**
     * Gets the notification.
     *
     * @param idHistory
     *            the id history
     * @param idTask
     *            the id task
     * @return the notification
     */
    public String getNotification( int idHistory, int idTask )
    {
        return _notificationSignalementTaskConfigDAO.getNotification( idHistory, idTask );
    }

}
