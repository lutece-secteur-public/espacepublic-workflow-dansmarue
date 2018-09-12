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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.service.WebServiceSignalementTaskConfigService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.service.WebServiceValueService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.business.state.StateFilter;
import fr.paris.lutece.plugins.workflowcore.service.state.IStateService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 *
 * WebServiceComponent
 *
 */
public class WebServiceComponent extends AbstractTaskComponent
{
    // I18N
    private static final String                    I18N_FIELD_WITHWS_SUCCESS            = "module.workflow.dansmarue.task_webservice_config.withWS.success";
    private static final String                    I18N_FIELD_WITHWS_FAILURE            = "module.workflow.dansmarue.task_webservice_config.withWS.failure";
    private static final String                    I18N_FIELD_WITHOUTWS                 = "module.workflow.dansmarue.task_webservice_config.withoutWS";

    // MARKERS
    private static final String                    MARK_LIST_STATE                      = "state_list";
    private static final String                    MARK_CONFIG                          = "config";
    private static final String                    MARK_LISTE_UNITS                     = "liste_units";
    private static final String                    MARK_CONFIG_DTO                      = "configDTO";
    private static final String                    MARK_WEBAPP_URL                      = "webapp_url";
    private static final String                    MARK_LOCALE                          = "locale";
    private static final String                    MARK_WEBSERVICE_VALUE                = "webservice_value";

    // PARAMETERS
    private static final String                    PARAMETER_ADD_UNIT                   = "add_unit";
    private static final String                    PARAMETER_ID_TASK                    = "id_task";

    // CONSTANTS
    public static final int                        CONSTANT_STATE_NULL                  = 0;

    // MESSAGES
    private static final String                    MESSAGE_MANDATORY_FIELD              = "module.workflow.dansmarue.task_webservice_config.message.mandatory.field";
    private static final String                    MESSAGE_INVALID_STATE                = "module.workflow.dansmarue.task_webservice_config.message.invalid.state";
    private static final String                    MESSAGE_EXCEPTION_OCCURED            = "module.workflow.dansmarue.task_webservice_config.message.exception";
    private static final String                    ERROR_UNIT                           = "module.workflow.dansmarue.task_webservice_config.error.entite";
    private static final String                    ERROR_URL                            = "module.workflow.dansmarue.task_webservice_config.error.url";
    private static final String                    MESSAGE_ERROR_UNIT_ALLREADY_EXISTS   = "module.workflow.dansmarue.task_webservice_config.error.entite.exists";

    // TEMPLATES
    private static final String                    TEMPLATE_TASK_WEBSERVICE_CONFIG      = "admin/plugins/workflow/modules/signalement/task_webservice_config.html";
    private static final String                    TEMPLATE_TASK_WEBSERVICE_INFORMATION = "admin/plugins/workflow/modules/signalement/task_webservice_information.html";

    // JSP
    private static final String                    JSP_MODIFY_TASK                      = "jsp/admin/plugins/workflow/ModifyTask.jsp";

    @Inject
    @Named( "signalement.webserviceSignalementTaskConfigService" )
    private WebServiceSignalementTaskConfigService _webserviceSignalementTaskConfigService;

    @Inject
    @Named( IUnitService.BEAN_UNIT_SERVICE )
    private IUnitService                           _unitService;

    @Inject
    @Named( "signalement.workflowService" )
    private IWorkflowService                       _signalementWorkflowService;

    @Inject
    @Named( "signalement.webserviceValueService" )
    private WebServiceValueService                 _webserviceValueService;

    @Inject
    @Named( "signalement.webserviceSignalementTaskConfigDAO" )
    private WebServiceSignalementTaskConfigDAO     _configDAO;

    @Inject
    @Named( "workflow.stateService" )
    private IStateService                          _stateService;

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        WebServiceSignalementTaskConfig config = _configDAO.findByPrimaryKey( task.getId( ), null );

        if ( null == config )
        {
            config = new WebServiceSignalementTaskConfig( );
        }

        WebServiceSignalementTaskConfigDTO configDTO = _webserviceSignalementTaskConfigService.findByPrimaryKey( task.getId( ) );

        if ( null == configDTO )
        {
            configDTO = new WebServiceSignalementTaskConfigDTO( );
        }

        model.put( MARK_CONFIG_DTO, configDTO );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, request.getLocale( ) );

        StateFilter filter = new StateFilter( );
        filter.setIdWorkflow( _signalementWorkflowService.getSignalementWorkflowId( ) );

        ReferenceList listeUnits = ListUtils.toReferenceList( _unitService.getAllUnits( false ), "idUnit", "label", "" );
        model.put( MARK_LISTE_UNITS, listeUnits );

        List<State> stateList = _stateService.getListStateByFilter( filter );
        ReferenceList refListState = new ReferenceList( );
        refListState.addItem( "", "" );

        for ( State state : stateList )
        {
            refListState.addItem( state.getId( ), state.getName( ) );
        }

        model.put( MARK_LIST_STATE, refListState );
        model.put( MARK_CONFIG, config );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_WEBSERVICE_CONFIG, locale, model );

        return template.getHtml( );
    }

    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        WebServiceValue webserviceValue = _webserviceValueService.findByPrimaryKey( nIdHistory, task.getId( ), null );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_WEBSERVICE_VALUE, webserviceValue );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_WEBSERVICE_INFORMATION, locale, model );

        return template.getHtml( );
    }

    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        if ( request.getParameter( PARAMETER_ADD_UNIT ) != null )
        {
            // Bouton Ajouter : ajout d'une unit de webservice
            WebServiceSignalementTaskConfigUnit configUnit = new WebServiceSignalementTaskConfigUnit( );
            try
            {
                BeanUtils.populate( configUnit, request.getParameterMap( ) );
                configUnit.setIdTask( task.getId( ) );
            } catch ( Exception e )
            {
                AppLogService.error( e.getMessage( ), e );
                Object[] tabError = { e.getMessage( ) };
                return AdminMessageService.getMessageUrl( request, MESSAGE_EXCEPTION_OCCURED, tabError, AdminMessage.TYPE_STOP );
            }

            // Gestion des erreurs
            if ( configUnit.getUnit( ).getIdUnit( ) == WorkflowUtils.CONSTANT_ID_NULL )
            {
                strError = ERROR_UNIT;
            } else if ( ( configUnit.getPrestataireSansWS( ) == null ) && StringUtils.isBlank( configUnit.getUrlPrestataire( ) ) )
            {
                strError = ERROR_URL;
            }

            if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
            } else if ( _webserviceSignalementTaskConfigService.findUnitByPrimaryKey( task.getId( ), configUnit.getUnit( ).getIdUnit( ) ) != null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_UNIT_ALLREADY_EXISTS, AdminMessage.TYPE_STOP );
            }

            // Insertion de l'unit
            _webserviceSignalementTaskConfigService.insertUnit( configUnit );

            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
            url.addParameter( PARAMETER_ID_TASK, task.getId( ) );

            return url.getUrl( );
        } else
        {
            WebServiceSignalementTaskConfig config = new WebServiceSignalementTaskConfig( );

            try
            {
                BeanUtils.populate( config, request.getParameterMap( ) );
            } catch ( Exception e )
            {
                AppLogService.error( e.getMessage( ), e );

                Object[] tabError = { e.getMessage( ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabError, AdminMessage.TYPE_STOP );
            }

            // Gestion des erreurs

            // Les champs n'ont pas été remplis.
            if ( config.getStateWithWSSuccess( ) == CONSTANT_STATE_NULL )
            {
                strError = I18N_FIELD_WITHWS_SUCCESS;
            } else if ( config.getStateWithWSFailure( ) == CONSTANT_STATE_NULL )
            {
                strError = I18N_FIELD_WITHWS_FAILURE;
            } else if ( config.getStateWithoutWS( ) == CONSTANT_STATE_NULL )
            {
                strError = I18N_FIELD_WITHOUTWS;
            }

            if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            // Les états n'existent pas
            if ( _stateService.findByPrimaryKey( config.getStateWithWSSuccess( ) ) == null )
            {
                strError = I18N_FIELD_WITHWS_SUCCESS;
            } else if ( _stateService.findByPrimaryKey( config.getStateWithWSFailure( ) ) == null )
            {
                strError = I18N_FIELD_WITHWS_FAILURE;
            } else if ( _stateService.findByPrimaryKey( config.getStateWithoutWS( ) ) == null )
            {
                strError = I18N_FIELD_WITHWS_FAILURE;
            }

            if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
            {

                Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_STATE, tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            config.setIdTask( task.getId( ) );

            // Ajoute la conf' sur les états de sortie en BDD si rien a été configuré pour cette tâche
            if ( _configDAO.findByPrimaryKey( task.getId( ), SignalementUtils.getPlugin( ) ) == null )
            {
                _configDAO.insert( config, SignalementUtils.getPlugin( ) );
            } else
                // Si une précédente conf' était déjà en BDD, une mise à jour est effectuée avec les nouveaux états de sortie
            {
                _configDAO.update( config, SignalementUtils.getPlugin( ) );
            }
            return null;
        }
    }

}
