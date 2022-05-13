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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationpush.business;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

/**
 * The Class NotificationPushTaskConfig.
 */
public class NotificationPushTaskConfig extends TaskConfig
{

    /** The mobile subject. */
    private String _mobileSubject;

    /** The mobile message. */
    private String _mobileMessage;

    /** The is diffusion declarant. */
    private boolean _isDiffusionDeclarant;

    /** The is diffusion suiveur. */
    private boolean _isDiffusionSuiveur;

    /**
     * Gets the mobile message.
     *
     * @return the mobileMessage
     */
    public String getMobileMessage( )
    {
        return _mobileMessage;
    }

    /**
     * Sets the mobile message.
     *
     * @param mobileMessage
     *            the mobileMessage to set
     */
    public void setMobileMessage( String mobileMessage )
    {
        this._mobileMessage = mobileMessage;
    }

    /**
     * Gets the mobile subject.
     *
     * @return the mobileSubject
     */
    public String getMobileSubject( )
    {
        return _mobileSubject;
    }

    /**
     * Sets the mobile subject.
     *
     * @param mobileSubject
     *            the mobileSubject to set
     */
    public void setMobileSubject( String mobileSubject )
    {
        this._mobileSubject = mobileSubject;
    }

    /**
     * Gets the checks if is diffusion declarant.
     *
     * @return the checks if is diffusion declarant
     */
    public boolean getIsDiffusionDeclarant( )
    {
        return _isDiffusionDeclarant;
    }

    /**
     * Sets the checks if is diffusion declarant.
     *
     * @param isDiffusionDeclarant
     *            the new checks if is diffusion declarant
     */
    public void setIsDiffusionDeclarant( boolean isDiffusionDeclarant )
    {
        this._isDiffusionDeclarant = isDiffusionDeclarant;
    }

    /**
     * Gets the checks if is diffusion suiveur.
     *
     * @return the checks if is diffusion suiveur
     */
    public boolean getIsDiffusionSuiveur( )
    {
        return _isDiffusionSuiveur;
    }

    /**
     * Sets the checks if is diffusion suiveur.
     *
     * @param isDiffusionSuiveur
     *            the new checks if is diffusion suiveur
     */
    public void setIsDiffusionSuiveur( boolean isDiffusionSuiveur )
    {
        this._isDiffusionSuiveur = isDiffusionSuiveur;
    }

}
