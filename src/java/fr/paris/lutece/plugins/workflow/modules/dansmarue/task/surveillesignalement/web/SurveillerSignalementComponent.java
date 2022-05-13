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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.surveillesignalement.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * SurveillerSignalementComponent.
 */
public class SurveillerSignalementComponent extends AbstractTaskComponent
{

    /** The Constant TEMPLATE_TASK_FORM. */
    private static final String TEMPLATE_TASK_FORM = "admin/plugins/workflow/modules/signalement/task_surveille_signalement_form.html";

    /** The signalement service. */
    // SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /** The Constant MARK_SIGNALEMENT. */
    // MARKERS
    private static final String MARK_SIGNALEMENT = "signalement";

    /**
     * Do validate task.
     *
     * @param arg0
     *            the arg 0
     * @param arg1
     *            the arg 1
     * @param arg2
     *            the arg 2
     * @param arg3
     *            the arg 3
     * @param arg4
     *            the arg 4
     * @return the string
     */
    @Override
    public String doValidateTask( int arg0, String arg1, HttpServletRequest arg2, Locale arg3, ITask arg4 )
    {
        return null;
    }

    /**
     * Gets the display config form.
     *
     * @param arg0
     *            the arg 0
     * @param arg1
     *            the arg 1
     * @param arg2
     *            the arg 2
     * @return the display config form
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest arg0, Locale arg1, ITask arg2 )
    {
        return null;
    }

    /**
     * Gets the display task form.
     *
     * @param nIdResource
     *            the n id resource
     * @param arg1
     *            the arg 1
     * @param arg2
     *            the arg 2
     * @param locale
     *            the locale
     * @param arg4
     *            the arg 4
     * @return the display task form
     */
    @Override
    public String getDisplayTaskForm( int nIdResource, String arg1, HttpServletRequest arg2, Locale locale, ITask arg4 )
    {
        Map<String, Object> model = new HashMap<>( );

        Signalement signalement = _signalementService.getSignalement( nIdResource );

        // get the signalement
        model.put( MARK_SIGNALEMENT, signalement );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FORM, locale, model );

        return template.getHtml( );
    }

    /**
     * Gets the display task information.
     *
     * @param arg0
     *            the arg 0
     * @param arg1
     *            the arg 1
     * @param arg2
     *            the arg 2
     * @param arg3
     *            the arg 3
     * @return the display task information
     */
    @Override
    public String getDisplayTaskInformation( int arg0, HttpServletRequest arg1, Locale arg2, ITask arg3 )
    {
        return null;
    }

    /**
     * Gets the task information xml.
     *
     * @param arg0
     *            the arg 0
     * @param arg1
     *            the arg 1
     * @param arg2
     *            the arg 2
     * @param arg3
     *            the arg 3
     * @return the task information xml
     */
    @Override
    public String getTaskInformationXml( int arg0, HttpServletRequest arg1, Locale arg2, ITask arg3 )
    {
        return null;
    }

}
