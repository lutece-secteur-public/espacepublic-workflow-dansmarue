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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.web;

import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.IListUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationAutoConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business.RequalificationAutoUnitDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.service.RequalificationAutoSignalementTaskConfigService;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.action.IActionService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * RequalificationAutoComponent.
 */
public class RequalificationAutoComponent extends AbstractTaskComponent
{

    /** The Constant MESSAGE_EVERY_TYPE_SIGNALEMENT. */
    private static final String MESSAGE_EVERY_TYPE_SIGNALEMENT = "module.workflow.dansmarue.task_requalification_auto_config.type_signalement.all";

    /** The Constant MESSAGE_NO_STATE_AFTER. */
    private static final String MESSAGE_NO_STATE_AFTER = "module.workflow.dansmarue.task_requalification_auto_config.noStateAfter";

    /** The Constant PROPERTY_ID. */
    private static final String PROPERTY_ID = "id";

    /** The Constant MARK_FIRST_LEVEL_UNITS. */
    private static final String MARK_FIRST_LEVEL_UNITS = "first_level_units";

    /** The Constant MARK_LISTE_UNITS. */
    private static final String MARK_LISTE_UNITS = "liste_units";

    /** The Constant MARK_LIST_REQUALIF_UNITS. */
    private static final String MARK_LIST_REQUALIF_UNITS = "list_requalif_units";

    /** The Constant MARK_LIST_TYPE_SIGNALEMENT. */
    private static final String MARK_LIST_TYPE_SIGNALEMENT = "list_type_signalement";

    /** The Constant MARK_LIST_ACTIONS. */
    private static final String MARK_LIST_ACTIONS = "liste_states";

    /** The Constant TEMPLATE_TASK_CONFIGURATION. */
    private static final String TEMPLATE_TASK_CONFIGURATION = "admin/plugins/workflow/modules/signalement/task_requalification_auto_signalement_config.html";

    /** The requalification auto signalement task config service. */
    @Inject
    private RequalificationAutoSignalementTaskConfigService _requalificationAutoSignalementTaskConfigService;

    /** The unit service. */
    @Inject
    @Named( UnitService.BEAN_UNIT_SERVICE )
    private IUnitService _unitService;

    /** The type signalement service. */
    @Inject
    private ITypeSignalementService _typeSignalementService;

    /** The action service. */
    @Inject
    private IActionService _actionService;

    /** The date utils. */
    // UTILS
    @Inject
    @Named( "signalement.listUtils" )
    private IListUtils _listUtils;

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
        return null;
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
        List<Unit> listUnits = _unitService.getAllUnits( false );
        List<Unit> listFirstLevelUnits = _unitService.getUnitsFirstLevel( false );
        ReferenceList refListUnits = _listUtils.toReferenceList( listUnits, "idUnit", "label", StringUtils.EMPTY, false );
        ReferenceList refListFirstLevelUnits = _listUtils.toReferenceList( listFirstLevelUnits, "idUnit", "label", StringUtils.EMPTY, false );

        List<RequalificationAutoConfigUnit> listRequalifUnits = _requalificationAutoSignalementTaskConfigService.findByTaskId( task.getId( ) );
        ReferenceList refListTypeSignalement = _listUtils.toReferenceList( _typeSignalementService.getAllTypeSignalement( ), PROPERTY_ID,
                "formatTypeSignalement", I18nService.getLocalizedString( MESSAGE_EVERY_TYPE_SIGNALEMENT, locale ), false );
        Action currentAction = _actionService.findByPrimaryKey( task.getAction( ).getId( ) );
        List<State> listStates = new ArrayList<>( );
        for ( State state : WorkflowService.getInstance( ).getAllStateByWorkflow( currentAction.getWorkflow( ).getId( ),
                AdminUserService.getAdminUser( request ) ) )
        {
            listStates.add( state );
        }

        ReferenceList refListStates = _listUtils.toReferenceList( listStates, "id", "name", I18nService.getLocalizedString( MESSAGE_NO_STATE_AFTER, locale ),
                false );

        List<RequalificationAutoUnitDTO> listDTO = _requalificationAutoSignalementTaskConfigService.convertRequalifAutoUnitsToDTO( listRequalifUnits, listUnits,
                listStates, locale );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LISTE_UNITS, refListUnits );
        model.put( MARK_LIST_REQUALIF_UNITS, listDTO );
        model.put( MARK_LIST_TYPE_SIGNALEMENT, refListTypeSignalement );
        model.put( MARK_FIRST_LEVEL_UNITS, refListFirstLevelUnits );
        model.put( MARK_LIST_ACTIONS, refListStates );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_CONFIGURATION, locale, model );

        return template.getHtml( );
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
        return null;
    }

}
