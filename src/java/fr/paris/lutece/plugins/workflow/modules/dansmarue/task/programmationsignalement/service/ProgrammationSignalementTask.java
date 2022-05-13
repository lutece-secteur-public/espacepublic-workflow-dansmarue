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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.programmationsignalement.service;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;

/**
 * ProgrammationSignalementTask class.
 */
public class ProgrammationSignalementTask extends AbstractSignalementTask
{

    /** The Constant PARAMETER_WEBSERVICE_DATE_PREVUE_TRAITEMENT. */
    // Parameters
    private static final String PARAMETER_WEBSERVICE_DATE_PREVUE_TRAITEMENT = "DatePrevueTraitement";

    /** The Constant TASK_TITLE. */
    private static final String TASK_TITLE = "Intervention sur le signalement programmée";

    /** The signalement service. */
    // SERVICES
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The signalement workflow service. */
    private IWorkflowService _signalementWorkflowService = SpringContextService.getBean( "signalement.workflowService" );

    /**
     * Process task.
     *
     * @param nIdResourceHistory
     *            the n id resource history
     * @param request
     *            the request
     * @param locale
     *            the locale
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        Signalement signalementTemp = new Signalement( );

        if ( request.getSession( ).getAttribute( PARAMETER_WEBSERVICE_DATE_PREVUE_TRAITEMENT ) != null )
        {
            // Cas ou la tache est declenchee suite a l'appel du WS /rest/signalement/api/changeStatus
            String datePrevueTraitement = (String) request.getSession( ).getAttribute( PARAMETER_WEBSERVICE_DATE_PREVUE_TRAITEMENT );
            request.getSession( ).removeAttribute( PARAMETER_WEBSERVICE_DATE_PREVUE_TRAITEMENT );
            ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
            Integer idRessource = resourceHistory.getIdResource( );

            signalementTemp.setId( idRessource.longValue( ) );
            signalementTemp.setDatePrevueTraitement( datePrevueTraitement );
        }
        else
        {
            SignalementUtils.populate( signalementTemp, request );
        }

        Signalement signalement = _signalementService.getSignalement( signalementTemp.getId( ) );

        // Ajout de la date
        signalement.setDatePrevueTraitement( signalementTemp.getDatePrevueTraitement( ) );

        _signalementService.update( signalement );

        // set the state of the signalement with the workflow
        WorkflowService workflowService = WorkflowService.getInstance( );
        if ( workflowService.isAvailable( ) )
        {
            // récupération de l'identifiant du workflow
            Integer workflowId = _signalementWorkflowService.getSignalementWorkflowId( );
            if ( workflowId != null )
            {
                workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, workflowId, null );
                workflowService.executeActionAutomatic( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, workflowId, null );
            }
            else
            {
                AppLogService.error( "Signalement : No workflow selected" );
            }
        }
        else
        {
            AppLogService.error( "Signalement : Workflow not available" );
        }
    }

    /**
     * Gets the title.
     *
     * @param locale
     *            the locale
     * @return the title
     */
    @Override
    public String getTitle( Locale locale )
    {
        return TASK_TITLE;
    }

    /**
     * Gets the task form entries.
     *
     * @param locale
     *            the locale
     * @return the task form entries
     */
    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )
    {
        return null;
    }
}
