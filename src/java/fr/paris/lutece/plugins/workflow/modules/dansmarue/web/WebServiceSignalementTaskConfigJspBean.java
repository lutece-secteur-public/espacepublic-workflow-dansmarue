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
import fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.service.WebServiceSignalementTaskConfigService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
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
 * The class WebServiceSignalementTaskConfigJspBean
 *
 */
public class WebServiceSignalementTaskConfigJspBean extends AbstractJspBean
{
    // MARKERS
    private static final String                    MARK_CONFIG_UNIT                                 = "configUnit";
    private static final String                    MARK_UNIT                                        = "unit";

    // PARAMETERS
    private static final String                    PARAMETER_ID_TASK                                = "id_task";
    private static final String                    PARAMETER_ID_UNIT                                = "id_unit";
    private static final String                    PARAMETER_SAVE_BUTTON                            = "save";

    // MESSAGES
    private static final String                    MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT     = "module.workflow.dansmarue.message.confirm_delete_signalement_task_unit";
    private static final String                    MESSAGE_MANDATORY_FIELD                          = "module.workflow.dansmarue.task_webservice_config.modify.mandatory.field";

    // JSP
    private static final String                    JSP_DO_DELETE_WEBSERVICE_SIGNALEMENT_UNIT        = "jsp/admin/plugins/workflow/modules/signalement/DoDeleteWebServiceSignalementTaskUnit.jsp";
    private static final String                    JSP_MODIFY_TASK                                  = "jsp/admin/plugins/workflow/ModifyTask.jsp";

    // TEMPLATES
    private static final String                    TEMPLATE_MODIFY_WEBSERVICE_SIGNALEMENT_TASK_UNIT = "admin/plugins/workflow/modules/signalement/modify_webservice_signalement_task_unit.html";

    // SERVICES
    private WebServiceSignalementTaskConfigService _webserviceSignalementTaskConfigService          = SpringContextService.getBean( "signalement.webserviceSignalementTaskConfigService" );
    private IUnitService                           _unitService                                     = ( IUnitService ) SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    /**
     * Return AdminMessage page content to confirm the webserviceSignalementTaskUnit delete
     *
     * @param request
     *            the HttpServletRequest
     * @return the AdminMessage
     */
    public String confirmDeleteWebServiceSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_DO_DELETE_WEBSERVICE_SIGNALEMENT_UNIT );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );
        url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_SIGNALEMENT_TASK_UNIT, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * The doDeleteWebServiceSignalementTaskUnit implementation
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doDeleteWebServiceSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        int nIdTask = WorkflowUtils.convertStringToInt( strIdTask );
        int nIdUnit = WorkflowUtils.convertStringToInt( strIdUnit );

        _webserviceSignalementTaskConfigService.deleteUnit( nIdTask, nIdUnit );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }

    /**
     * The ModifyWebServiceSignalementTaskUnit page
     *
     * @param request
     *            the HttpServletRequest
     * @return page content
     */
    public String getModifyWebServiceSignalementTaskUnit( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        String strIdTask = request.getParameter( PARAMETER_ID_TASK );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        model.put( PARAMETER_ID_TASK, strIdTask );
        model.put( PARAMETER_ID_UNIT, strIdUnit );

        int nIdTask = WorkflowUtils.convertStringToInt( strIdTask );
        int nIdUnit = WorkflowUtils.convertStringToInt( strIdUnit );

        WebServiceSignalementTaskConfigUnit configUnit = _webserviceSignalementTaskConfigService.findUnitByPrimaryKey( nIdTask, nIdUnit );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            configUnit = ( WebServiceSignalementTaskConfigUnit ) ve.getBean( );
            model.put( "error", getHtmlError( ve ) );
        }
        model.put( MARK_CONFIG_UNIT, configUnit );

        Unit unit = _unitService.getUnit( nIdUnit, false );
        model.put( MARK_UNIT, unit );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_WEBSERVICE_SIGNALEMENT_TASK_UNIT, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * The doModifyWebServiceSignalementTaskUnit implementation
     *
     * @param request
     *            the HttpServletRequest
     * @return url return
     */
    public String doModifyWebServiceSignalementTaskUnit( HttpServletRequest request )
    {
        String strIdTask = request.getParameter( PARAMETER_ID_TASK );

        if ( StringUtils.isBlank( request.getParameter( PARAMETER_SAVE_BUTTON ) ) )
        {
            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
            url.addParameter( PARAMETER_ID_TASK, strIdTask );
            return url.getUrl( );
        }

        WebServiceSignalementTaskConfigUnit configUnit = new WebServiceSignalementTaskConfigUnit( );
        populate( configUnit, request );

        if ( ( configUnit.getPrestataireSansWS( ) == null ) && StringUtils.isBlank( configUnit.getUrlPrestataire( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, AdminMessage.TYPE_STOP );
        }

        // Controls mandatory fields
        List<ValidationError> errors = validate( configUnit, "" );
        if ( errors.isEmpty( ) )
        {
            _webserviceSignalementTaskConfigService.updateUnit( configUnit );
        } else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors );
        }

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
        url.addParameter( PARAMETER_ID_TASK, strIdTask );

        return url.getUrl( );
    }
}
