/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

/**
 *
 * NotificationSuiviValue
 *
 */
public class NotificationSuiviValue
{
    private int    idResourceHistory;
    private int    idTask;
    private String mailNotificationValue;
    private String mobileNotificationValue;

    /**
     *
     * @return the notification value id
     */
    public int getIdResourceHistory( )
    {
        return idResourceHistory;
    }

    /**
     * the notification value id
     *
     * @param id
     *            the notification value id
     */
    public void setIdResourceHistory( int id )
    {
        idResourceHistory = id;
    }

    /**
     *
     * @return the task id
     */
    public int getIdTask( )
    {
        return idTask;
    }

    /**
     * the task id
     *
     * @param idTask
     *            the task id
     */
    public void setIdTask( int idTask )
    {
        this.idTask = idTask;
    }

    /**
     * @return the mailNotificationValue
     */
    public String getMailNotificationValue( )
    {
        return mailNotificationValue;
    }

    /**
     * @param mailNotificationValue
     *            the mailNotificationValue to set
     */
    public void setMailNotificationValue( String mailNotificationValue )
    {
        this.mailNotificationValue = mailNotificationValue;
    }

    /**
     * @return the mobileNotificationValue
     */
    public String getMobileNotificationValue( )
    {
        return mobileNotificationValue;
    }

    /**
     * @param mobileNotificationValue
     *            the mobileNotificationValue to set
     */
    public void setMobileNotificationValue( String mobileNotificationValue )
    {
        this.mobileNotificationValue = mobileNotificationValue;
    }

}
