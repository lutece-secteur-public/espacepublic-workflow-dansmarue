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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business;

import org.hibernate.validator.constraints.NotBlank;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

/**
 * NotificationSignalementTaskConfigUnit class
 *
 */
public class NotificationSignalementTaskConfigUnit extends TaskConfig
{
    @NotBlank
    private String _strDestinataires;
    private Unit   _unit = new Unit( );

    /**
     * return the destinataires of the notification
     *
     * @return _strDestinataires
     */
    public String getDestinataires( )
    {
        return _strDestinataires;
    }

    /**
     * set the destinataires of the notification
     *
     * @param destinataires
     *            the destinataires (separator ;)
     */
    public void setDestinataires( String destinataires )
    {
        _strDestinataires = destinataires;
    }

    /**
     * return the unit
     *
     * @return the unit
     */
    public Unit getUnit( )
    {
        return _unit;
    }

    /**
     * set the unit
     *
     * @param unit
     *            the unit
     */
    public void setUnit( Unit unit )
    {
        _unit = unit;
    }

}
