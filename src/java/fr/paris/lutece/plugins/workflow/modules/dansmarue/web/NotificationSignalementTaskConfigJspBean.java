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

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.ISignalementUtils;
import fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitService;
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
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class NotificationSignalementTaskConfigJspBean.
 */
public class NotificationSignalementTaskConfigJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2082143299570797960L;

    /** The Constant MARK_CONFIG_UNIT. */
    // MARKERS
    private static final String MARK_CONFIG_UNIT = "configUnit";

    /** The Constant MARK_UNIT. */
    private static final String MARK_UNIT = "unit";

    /** The Constant MARK_CONFIG_TYPE. */
    private static final String MARK_CONFIG_TYPE = "configType";

    /** The Constant MARK_TYPE_SIGNALEMENT. */
    private static final String MARK_TYPE_SIGNALEMENT = "typeSignalement";

    /** The Constant MARK_IS_UNIT. */
    private static final String MARK_IS_UNIT = "isUnit";

    /** The Constant PARAMETER_ID_TASK. */
    // PARAMETERS
    private static final String PARAMETER_ID_TASK = "id_task";

    /** The Constant PARAMETER_ID_UNIT. */
    private static final String PARAMETER_ID_UNIT = "id_unit";

    /** The Constant PARAMETER_ID_TYPE_SIGNALEMENT. */
    private static final String PARAMETER_ID_TYPE_SIGNALEMENT = "id_type_signalement";

    /** The Constant PARAMETER_SAVE_BUTTON. */
    private static final String PARAMETER_SAVE_BUTTON = "save";

    /** The Constant ERROR_TITLE. */
    private static final String ERROR_TITLE = "module.workflow.dansmarue.task_notification_config.error.title";

    /** The Constant ERROR_MESSAGE. */
    private static final String ERROR_MESSAGE = "module.workflow.dansmarue.task_notification_config.error.message";

    /** The Constant MESSAGE_MANDATORY_FIELD. */
    private static final String MESSAGE_MANDATORY_FIELD = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";

    /** The Constant MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT. */
    // MESSAGES
    private static final String MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT = "module.workflow.dansmarue.message.confirm_delete_signalement_task_unit";

    /** The Constant MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_TYPE. */
    private static final String MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_TYPE = "module.workflow.dansmarue.message.confirm_delete_signalement_task_type";

    /** The Constant JSP_DO_DELETE_NOTIFICATION_SIGNALEMENT_UNIT. */
    // JSP
    private static final String JSP_DO_DELETE_NOTIFICATION_SIGNALEMENT_UNIT = "jsp/admin/plugins/workflow/modules/signalement/DoDeleteNotificationSignalementTaskUnit.jsp";

    /** The Constant JSP_MODIFY_TASK. */
    private static final String JSP_MODIFY_TASK = "jsp/admin/plugins/workflow/ModifyTask.jsp";

    /** The Constant TEMPLATE_MODIFY_NOTIFICATION_SIGNALEMENT_TASK_UNIT. */
    // TEMPLATES
    private static final String TEMPLATE_MODIFY_NOTIFICATION_SIGNALEMENT_TASK_UNIT = "admin/plugins/workflow/modules/signalement/modify_notification_signalement_task_unit.html";

    /** The notification signalement task config service. */
    // SERVICES
    private transient NotificationSignalementTaskConfigService _notificationSignalementTaskConfigService = SpringContextService
            .getBean( "signalement.notificationSignalementTaskConfigService" );

    /** The unit service. */
    private transient IUnitService _unitService = SpringContextService.getBean( UnitService.BEAN_UNIT_SERVICE );

    /** The notification signalement user multi contents task config DAO. */
    private transient NotificationSignalementUserMultiContentsTaskConfigDAO _notificationSignalementUserMultiContentsTaskConfigDAO = SpringContextService
            .getBean( "signalement.notificationSignalementUserMultiContentsTaskConfigDAO" );

    /** The type signalement service. */
    private transient ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /** The signalement utils */
    // UTILS
    private ISignalementUtils _signalementUtils = SpringContextService.getBean( "signalement.signalementUtils" );

    /**
     * Return AdminMessage page content to confirm the notificationSignalementTaskUnit delete.
     *
     * @param request
     *            the HttpServletRequest
     * @return the AdminMessage
     */
    public String confirmDeleteNotificationSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdTypeSignalement = request.getParameter( PARAMETER_ID_TYPE_SIGNALEMENT );

        String messageToShow = "";

        boolean isUnit = false;

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_DO_DELETE_NOTIFICATION_SIGNALEMENT_UNIT );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );
        if ( !StringUtils.EMPTY.equals( strIdUnit ) && ( strIdUnit != null ) )
        {
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );
            isUnit = true;
            messageToShow = MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT;
        }
        if ( !StringUtils.EMPTY.equals( strIdTypeSignalement ) && ( strIdTypeSignalement != null ) && !isUnit )
        {
            url.addParameter( PARAMETER_ID_TYPE_SIGNALEMENT, strIdTypeSignalement );
            messageToShow = MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_TYPE;
        }

        return AdminMessageService.getMessageUrl( request, messageToShow, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * The doDeleteNotificationSignalementTaskUnit implementation.
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doDeleteNotificationSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdType = request.getParameter( PARAMETER_ID_TYPE_SIGNALEMENT );

        int nIdTask = WorkflowUtils.convertStringToInt( strIdTask );
        int nIdUnit;
        int nIdType;

        boolean isUnit = false;

        if ( !StringUtils.EMPTY.equals( strIdUnit ) && ( strIdUnit != null ) )
        {
            nIdUnit = WorkflowUtils.convertStringToInt( strIdUnit );
            _notificationSignalementTaskConfigService.deleteUnit( nIdTask, nIdUnit );
            isUnit = true;
        }
        if ( !StringUtils.EMPTY.equals( strIdType ) && ( strIdType != null ) && !isUnit )
        {
            nIdType = WorkflowUtils.convertStringToInt( strIdType );
            _notificationSignalementTaskConfigService.deleteWithTypeSignalement( nIdTask, nIdType );
        }

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }

    /**
     * The ModifyNotificationSignalementTaskUnit page.
     *
     * @param request
     *            the HttpServletRequest
     * @return page content
     */
    public String getModifyNotificationSignalementTaskUnit( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdTypeSignalement = request.getParameter( PARAMETER_ID_TYPE_SIGNALEMENT );
        model.put( PARAMETER_ID_TASK, strIdTask );
        model.put( PARAMETER_ID_UNIT, strIdUnit );
        model.put( PARAMETER_ID_TYPE_SIGNALEMENT, strIdTypeSignalement );

        boolean isUnit = false;

        int nIdUnit = -1;
        int nIdTypeSignalement = -1;
        int nIdTask = WorkflowUtils.convertStringToInt( strIdTask );
        if ( !StringUtils.EMPTY.equals( strIdUnit ) && ( strIdUnit != null ) )
        {
            nIdUnit = WorkflowUtils.convertStringToInt( strIdUnit );
            isUnit = true;
        }
        if ( !StringUtils.EMPTY.equals( strIdTypeSignalement ) && ( strIdTypeSignalement != null ) )
        {
            nIdTypeSignalement = WorkflowUtils.convertStringToInt( strIdTypeSignalement );
            isUnit = false;
        }

        model.put( MARK_IS_UNIT, isUnit );

        if ( isUnit )
        {
            NotificationSignalementTaskConfigUnit configUnit = _notificationSignalementTaskConfigService.findUnitByPrimaryKey( nIdTask, nIdUnit );

            // Manage validation errors
            FunctionnalException ve = getErrorOnce( request );
            if ( ve != null )
            {
                configUnit = (NotificationSignalementTaskConfigUnit) ve.getBean( );
                model.put( "error", getHtmlError( ve ) );
            }
            model.put( MARK_CONFIG_UNIT, configUnit );

            Unit unit = _unitService.getUnit( nIdUnit, false );
            model.put( MARK_UNIT, unit );
        }
        else
        {
            NotificationSignalementTaskConfigUnit configType = _notificationSignalementTaskConfigService.findByIdTypeSignalement( nIdTask, nIdTypeSignalement );

            // Manage validation errors
            FunctionnalException ve = getErrorOnce( request );
            if ( ve != null )
            {
                configType = (NotificationSignalementTaskConfigUnit) ve.getBean( );
                model.put( "error", getHtmlError( ve ) );
            }
            model.put( MARK_CONFIG_TYPE, configType );

            TypeSignalement type = _typeSignalementService.getTypeSignalementByIdWithParents( nIdTypeSignalement );
            model.put( MARK_TYPE_SIGNALEMENT, type );
        }

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_NOTIFICATION_SIGNALEMENT_TASK_UNIT, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * The doModifyNotificationSignalementTaskUnit implementation.
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

        String strIsUnit = request.getParameter( MARK_IS_UNIT );
        Boolean isUnit = Boolean.getBoolean( strIsUnit );

        // Controls mandatory fields
        List<ValidationError> errors = validate( configUnit, "" );
        if ( errors.isEmpty( ) )
        {
            if ( isUnit )
            {
                _notificationSignalementTaskConfigService.updateUnit( configUnit );
            }
            else
            {
                _notificationSignalementTaskConfigService.updateWithTypeSignalement( configUnit );
            }

        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors );
        }

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }

    /**
     * Delete.
     *
     * @param request
     *            the HttpServletRequest
     * @return url return
     */
    public String doDeleteNotificationUserMultiContents( HttpServletRequest request )
    {

        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdMessage = request.getParameter( "idMessageDelete" );

        _notificationSignalementUserMultiContentsTaskConfigDAO.deleteMessage( Long.parseLong( strIdMessage ), Integer.parseInt( strIdTask ),
                _signalementUtils.getPlugin( ) );

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
        }
        else
            if ( StringUtils.EMPTY.equals( config.getMessage( ) ) )
            {
                strError = ERROR_MESSAGE;
            }
        if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
        {
            Object [ ] tabRequiredFields = {
                    I18nService.getLocalizedString( strError, request.getLocale( ) )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        _notificationSignalementUserMultiContentsTaskConfigDAO.insert( config, Integer.parseInt( strIdTask ), _signalementUtils.getPlugin( ) );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }

}
