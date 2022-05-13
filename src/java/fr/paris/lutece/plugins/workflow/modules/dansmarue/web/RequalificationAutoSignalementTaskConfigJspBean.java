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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.web;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationAutoConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.service.RequalificationAutoSignalementTaskConfigService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * The class NotificationSignalementTaskConfigJspBean.
 */
public class RequalificationAutoSignalementTaskConfigJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2360051975067924761L;

    /** The Constant PARAMETER_ID_TASK. */
    // PARAMETERS
    private static final String PARAMETER_ID_TASK = "id_task";

    /** The Constant PARAMETER_ID_CONFIG_UNIT. */
    private static final String PARAMETER_ID_CONFIG_UNIT = "idConfigUnit";

    /** The Constant MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT. */
    // MESSAGES
    private static final String MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT = "module.workflow.dansmarue.task_requalification_auto_config.confirm_delete_signalement_task_unit";

    /** The Constant JSP_DO_DELETE_REQUALIFICATION_AUTO_SIGNALEMENT_UNIT. */
    // JSP
    private static final String JSP_DO_DELETE_REQUALIFICATION_AUTO_SIGNALEMENT_UNIT = "jsp/admin/plugins/workflow/modules/signalement/requalificationauto/DoDeleteRequalificationAutoSignalementTaskUnit.jsp";

    /** The Constant JSP_MODIFY_TASK. */
    private static final String JSP_MODIFY_TASK = "jsp/admin/plugins/workflow/ModifyTask.jsp";

    /** The requalification auto signalement task config service. */
    // SERVICES
    private transient RequalificationAutoSignalementTaskConfigService _requalificationAutoSignalementTaskConfigService = SpringContextService
            .getBean( "workflow-signalement.requalificationAutoConfigService" );

    /**
     * Return AdminMessage page content to confirm the removal of the config unit.
     *
     * @param request
     *            the HttpServletRequest
     * @return the AdminMessage
     */
    public String confirmDeleteRequalificationAutoSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdConfigUnit = request.getParameter( PARAMETER_ID_CONFIG_UNIT );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_DO_DELETE_REQUALIFICATION_AUTO_SIGNALEMENT_UNIT );
        url.addParameter( PARAMETER_ID_CONFIG_UNIT, strIdConfigUnit );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * The doDeleteNotificationSignalementTaskUnit implementation.
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doDeleteRequalificationAutoSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdConfigUnit = request.getParameter( PARAMETER_ID_CONFIG_UNIT );

        int nIdConfigUnit = WorkflowUtils.convertStringToInt( strIdConfigUnit );
        RequalificationAutoConfigUnit requalifUnit = _requalificationAutoSignalementTaskConfigService.findByPrimaryKey( nIdConfigUnit );
        _requalificationAutoSignalementTaskConfigService.deleteById( nIdConfigUnit );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, requalifUnit.getIdTask( ) );

        return url.getUrl( );
    }
}
