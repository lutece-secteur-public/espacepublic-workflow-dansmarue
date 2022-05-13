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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.Task;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Allow to add task to the workflow.
 */
public abstract class AbstractSignalementTask extends Task
{

    /** The Constant MARK_NEXT. */
    public static final String MARK_NEXT = "next";

    /** The _signalement service. */
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The resource history service. */
    protected IResourceHistoryService _resourceHistoryService = SpringContextService.getBean( "workflow.resourceHistoryService" );

    /** The type signalement service. */
    private ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /**
     * Do remove config.
     */
    @Override
    public void doRemoveConfig( )
    {
    }

    /**
     * A surcharger si une tâche stock des informations complémentaires dans une autre table.
     *
     * @param nIdHistory
     *            the id history
     */
    @Override
    public void doRemoveTaskInformation( int nIdHistory )
    {
    }

    /**
     * Rentourne l'id d'un signalement.
     *
     * @param nIdResourceHistory
     *            l'id
     * @return l'id d'un signalement
     */
    protected int getIdSignalement( int nIdResourceHistory )
    {
        return _resourceHistoryService.findByPrimaryKey( nIdResourceHistory ).getIdResource( );
    }

    /**
     * Return url.
     *
     * @param strI18nKey
     *            le clé i18n
     * @param request
     *            the request
     * @return l'url
     */
    protected String getMessageUrl( String strI18nKey, HttpServletRequest request )
    {
        return AdminMessageService.getMessageUrl( request, strI18nKey, AdminMessage.TYPE_STOP );
    }

    /**
     * Accesseur de signalementService.
     *
     * @return signalementService
     */
    protected ISignalementService getSignalementService( )
    {
        return _signalementService;
    }

    /**
     * Gets the type signalement service.
     *
     * @return the type signalement service
     */
    protected ITypeSignalementService getTypeSignalementService( )
    {
        return _typeSignalementService;
    }

    /**
     * Inits the.
     */
    @Override
    public void init( )
    {
    }

    /**
     * Sets the type signalement service.
     *
     * @param typeSignalementService
     *            the new type signalement service
     */
    protected void setTypeSignalementService( ITypeSignalementService typeSignalementService )
    {
        _typeSignalementService = typeSignalementService;
    }
}
