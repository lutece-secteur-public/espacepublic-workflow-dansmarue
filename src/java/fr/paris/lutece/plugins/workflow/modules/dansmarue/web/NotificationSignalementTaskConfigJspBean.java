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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.NotificationSignalementTaskConfigService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationSignalementUserMultiContentsTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * The class NotificationSignalementTaskConfigJspBean
 *
 */
public class NotificationSignalementTaskConfigJspBean extends AbstractJspBean
{
    // MARKERS
    private static final String                                   MARK_CONFIG_UNIT                                       = "configUnit";
    private static final String                                   MARK_UNIT                                              = "unit";

    // PARAMETERS
    private static final String                                   PARAMETER_ID_TASK                                      = "id_task";
    private static final String                                   PARAMETER_ID_UNIT                                      = "id_unit";
    private static final String                                   PARAMETER_SAVE_BUTTON                                  = "save";

    private static final String                                   ERROR_TITLE                                            = "module.workflow.dansmarue.task_notification_config.error.title";
    private static final String                                   ERROR_MESSAGE                                          = "module.workflow.dansmarue.task_notification_config.error.message";
    private static final String                                   MESSAGE_MANDATORY_FIELD                                = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";

    // MESSAGES
    private static final String                                   MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT           = "module.workflow.dansmarue.message.confirm_delete_signalement_task_unit";

    // JSP
    private static final String                                   JSP_DO_DELETE_NOTIFICATION_SIGNALEMENT_UNIT            = "jsp/admin/plugins/workflow/modules/signalement/DoDeleteNotificationSignalementTaskUnit.jsp";
    private static final String                                   JSP_MODIFY_TASK                                        = "jsp/admin/plugins/workflow/ModifyTask.jsp";

    // TEMPLATES
    private static final String                                   TEMPLATE_MODIFY_NOTIFICATION_SIGNALEMENT_TASK_UNIT     = "admin/plugins/workflow/modules/signalement/modify_notification_signalement_task_unit.html";

    // SERVICES
    private NotificationSignalementTaskConfigService              _notificationSignalementTaskConfigService              = SpringContextService
            .getBean( "signalement.notificationSignalementTaskConfigService" );
    private IUnitService                                          _unitService                                           = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
    private NotificationSignalementUserMultiContentsTaskConfigDAO _notificationSignalementUserMultiContentsTaskConfigDAO = SpringContextService
            .getBean( "signalement.notificationSignalementUserMultiContentsTaskConfigDAO" );

    /**
     * Return AdminMessage page content to confirm the notificationSignalementTaskUnit delete
     *
     * @param request
     *            the HttpServletRequest
     * @return the AdminMessage
     */
    public String confirmDeleteNotificationSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_DO_DELETE_NOTIFICATION_SIGNALEMENT_UNIT );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );
        url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * The doDeleteNotificationSignalementTaskUnit implementation
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doDeleteNotificationSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        int nIdTask = WorkflowUtils.convertStringToInt( strIdTask );
        int nIdUnit = WorkflowUtils.convertStringToInt( strIdUnit );

        _notificationSignalementTaskConfigService.deleteUnit( nIdTask, nIdUnit );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }

    /**
     * The ModifyNotificationSignalementTaskUnit page
     *
     * @param request
     *            the HttpServletRequest
     * @return page content
     */
    public String getModifyNotificationSignalementTaskUnit( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        model.put( PARAMETER_ID_TASK, strIdTask );
        model.put( PARAMETER_ID_UNIT, strIdUnit );

        int nIdTask = WorkflowUtils.convertStringToInt( strIdTask );
        int nIdUnit = WorkflowUtils.convertStringToInt( strIdUnit );

        NotificationSignalementTaskConfigUnit configUnit = _notificationSignalementTaskConfigService.findUnitByPrimaryKey( nIdTask, nIdUnit );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            configUnit = ( NotificationSignalementTaskConfigUnit ) ve.getBean( );
            model.put( "error", getHtmlError( ve ) );
        }
        model.put( MARK_CONFIG_UNIT, configUnit );

        Unit unit = _unitService.getUnit( nIdUnit, false );
        model.put( MARK_UNIT, unit );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_NOTIFICATION_SIGNALEMENT_TASK_UNIT, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * The doModifyNotificationSignalementTaskUnit implementation
     *
     * @param request
     *            the HttpServletRequest
     * @return url return
     */
    public String doModifyNotificationSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdTask = request.getParameter( PARAMETER_ID_TASK );

        if ( StringUtils.isBlank( request.getParameter( PARAMETER_SAVE_BUTTON ) ) )
        {
            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
            url.addParameter( PARAMETER_ID_TASK, strIdTask );
            return url.getUrl( );
        }

        NotificationSignalementTaskConfigUnit configUnit = new NotificationSignalementTaskConfigUnit( );
        populate( configUnit, request );

        // Controls mandatory fields
        List<ValidationError> errors = validate( configUnit, "" );
        if ( errors.isEmpty( ) )
        {
            _notificationSignalementTaskConfigService.updateUnit( configUnit );
        } else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors );
        }

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }

    /**
     * Delete
     *
     * @param request
     *            the HttpServletRequest
     * @return url return
     */
    public String doDeleteNotificationUserMultiContents( HttpServletRequest request )
    {

        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdMessage = request.getParameter( "idMessageDelete" );

        _notificationSignalementUserMultiContentsTaskConfigDAO.deleteMessage( Long.parseLong( strIdMessage ), Integer.parseInt( strIdTask ), SignalementUtils.getPlugin( ) );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }

    /**
     * Do add notification.
     *
     * @param request
     *            the HttpServletRequest
     * @return url return
     */
    public String doAddNotificationUserMultiContents( HttpServletRequest request )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String sender = request.getParameter( "sender" );
        String subject = request.getParameter( "subject" );
        String title = request.getParameter( "title_new" );
        String message = request.getParameter( "message_new" );

        NotificationSignalementUserMultiContentsTaskConfig config = new NotificationSignalementUserMultiContentsTaskConfig( );
        config.setSender( sender );
        config.setSubject( subject );
        config.setTitle( title );
        config.setMessage( message );

        if ( StringUtils.EMPTY.equals( config.getTitle( ) ) )
        {
            strError = ERROR_TITLE;
        } else if ( StringUtils.EMPTY.equals( config.getMessage( ) ) )
        {
            strError = ERROR_MESSAGE;
        }
        if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, request.getLocale( ) ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        _notificationSignalementUserMultiContentsTaskConfigDAO.insert( config, Integer.parseInt( strIdTask ), SignalementUtils.getPlugin( ) );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }

}
