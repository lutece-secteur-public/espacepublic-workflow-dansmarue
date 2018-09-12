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

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

/**
 * NotificationSignalementUserTaskConfig class
 *
 */
public class NotificationSignalementUserMultiContentsTaskConfig extends TaskConfig
{
    private Integer _nIdTask;
    private Long    _nIdMessage;
    private String  _strSubject;
    private String  _strMessage;
    private String  _strSender;
    private String  _strTitle;

    /**
     *
     * @return the subject of the message
     */
    public String getSubject( )
    {
        return _strSubject;
    }

    /**
     * set the subject of the message
     *
     * @param subject
     *            the subject of the message
     */
    public void setSubject( String subject )
    {
        _strSubject = subject;
    }

    /**
     * return the sender of the notification
     *
     * @return _strSender
     */
    public String getSender( )
    {
        return _strSender;
    }

    /**
     * set the sender of the notification
     *
     * @param sender
     *            the sender
     */
    public void setSender( String sender )
    {
        _strSender = sender;
    }

    /**
     * @return the _idMessage
     */
    public Long getIdMessage( )
    {
        return _nIdMessage;
    }

    /**
     * @param idMessage
     *            the idMessage to set
     */
    public void setIdMessage( Long idMessage )
    {
        _nIdMessage = idMessage;
    }

    /**
     * @return the _strMessage
     */
    public String getMessage( )
    {
        return _strMessage;
    }

    /**
     * @param strMessage
     *            the strMessage to set
     */
    public void setMessage( String strMessage )
    {
        _strMessage = strMessage;
    }

    /**
     * @return the _strTitle
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * @param strTitle
     *            the strTitle to set
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * @return the _idTask
     */
    @Override
    public int getIdTask( )
    {
        return _nIdTask;
    }

    /**
     * @param idTask
     *            the idTask to set
     */
    public void setIdTask( Integer idTask )
    {
        _nIdTask = idTask;
    }

}
