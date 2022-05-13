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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.programmationsignalement.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.TaskUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * The Class ProgrammationComponent.
 */
public class ProgrammationComponent extends AbstractTaskComponent
{

    /** The Constant TEMPLATE_TASK_FORM. */
    private static final String TEMPLATE_TASK_FORM = "admin/plugins/workflow/modules/signalement/task_programmation_signalement_form.html";

    /** The Constant MESSAGE_WRONG_DATE. */
    // MESSAGES
    private static final String MESSAGE_WRONG_DATE = "module.workflow.dansmarue.programmation.wrongdate";

    /** The Constant MESSAGE_ERROR_EXCEPTION. */
    private static final String MESSAGE_ERROR_EXCEPTION = "module.workflow.dansmarue.programmation.errordate";

    /** The Constant MESSAGE_ERROR_EMPTY_DATE. */
    private static final String MESSAGE_ERROR_EMPTY_DATE = "module.workflow.dansmarue.programmation.error.emptydate";

    /** The Constant MARK_SIGNALEMENT_ID. */
    // MARKERS
    private static final String MARK_SIGNALEMENT_ID = "signalement_id";

    /** The Constant EMPTY_STRING. */
    // CONSTANTS
    private static final String EMPTY_STRING = "";

    /** The signalement service. */
    // SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /**
     * Gets the display task form.
     *
     * @param nIdResource
     *            the n id resource
     * @param strResourceType
     *            the str resource type
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the display task form
     */
    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<>( );

        Signalement signalement = _signalementService.getSignalement( nIdResource );

        // get the signalement
        model.put( MARK_SIGNALEMENT_ID, signalement.getId( ) );
        model.put( "locale", Locale.FRANCE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FORM, locale, model );

        return template.getHtml( );
    }

    /**
     * Gets the display config form.
     *
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the display config form
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * Gets the display task information.
     *
     * @param nIdHistory
     *            the n id history
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the display task information
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * Gets the task information xml.
     *
     * @param nIdHistory
     *            the n id history
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the task information xml
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * Do validate task.
     *
     * @param nIdResource
     *            the n id resource
     * @param strResourceType
     *            the str resource type
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the string
     */
    @Override
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        Signalement signalementTemp = new Signalement( );
        SignalementUtils.populate( signalementTemp, request );

        Set<ConstraintViolation<Signalement>> errors = BeanValidationUtil.validate( signalementTemp );
        if ( !errors.isEmpty( ) )
        {
            return TaskUtils.getValidationErrorsMessage( signalementTemp, locale, errors, request );
        }

        if ( !signalementTemp.getDatePrevueTraitement( ).equals( EMPTY_STRING ) )
        {
            SimpleDateFormat sdfDate = new SimpleDateFormat( "dd/MM/yyyy" );
            try
            {
                Date datePrevueTraitement = sdfDate.parse( signalementTemp.getDatePrevueTraitement( ) );
                Date dateDuJour = sdfDate.parse( sdfDate.format( Calendar.getInstance( ).getTime( ) ) );
                if ( dateDuJour.after( datePrevueTraitement ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_DATE, AdminMessage.TYPE_STOP );
                }

            }
            catch( Exception e )
            {
                AppLogService.error( e );
                // Invalid date was entered
                throw new BusinessException( signalementTemp, MESSAGE_ERROR_EXCEPTION );
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_EMPTY_DATE, AdminMessage.TYPE_STOP );
        }

        return null;
    }
}
