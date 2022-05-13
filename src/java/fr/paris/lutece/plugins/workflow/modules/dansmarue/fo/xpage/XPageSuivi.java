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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.fo.xpage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.fo.xpage.AbstractXPage;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.service.dto.HistorySignalementDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.SuiviDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.business.NotificationUserValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationuser.service.NotificationUserValueService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.business.NotificationUserMultiContentsValue;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationusermulticontents.service.NotificationUserMultiContentsValueService;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

/**
 * XPageSuivi.
 */
@Controller( xpageName = AbstractXPage.XPAGE_SUIVI, pageTitleI18nKey = "dansmarue.xpage.dansmarue.pageTitle", pagePathI18nKey = "dansmarue.xpage.dansmarue.pagePathLabel" )
public class XPageSuivi extends AbstractXPage
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant TEMPLATE_XPAGE_SUIVI_SIGNALEMENT. */
    // TEMPLATES
    private static final String TEMPLATE_XPAGE_SUIVI_SIGNALEMENT = "/admin/plugins/workflow/modules/signalement/suivi_signalement.html";

    /** The Constant ACTION_VALIDER_CHECKBOX. */
    // ACTIONS
    private static final String ACTION_VALIDER_CHECKBOX = "validate_checkbox";

    /** The Constant PARAMETER_VALIDATE_SUIVI. */
    // PARAMETERS
    private static final String PARAMETER_VALIDATE_SUIVI = "validate_suivi";

    /** The Constant MESSAGE_ERREUR_SIGNALEMENT_INTROUVABLE. */
    // MESSAGES
    private static final String MESSAGE_ERREUR_SIGNALEMENT_INTROUVABLE = "dansmarue.page.suivi.erreur.signalement.introuvable";

    /** The Constant MARK_SIGNALEMENT. */
    // MARKER
    private static final String MARK_SIGNALEMENT = "signalement";

    /** The Constant MARK_MESSAGE_ERREUR. */
    private static final String MARK_MESSAGE_ERREUR = "erreur";

    /** The Constant MARK_CURRENT_STATE. */
    private static final String MARK_CURRENT_STATE = "currentState";

    /** The Constant MARK_DATE_LAST_STATE. */
    private static final String MARK_DATE_LAST_STATE = "currentStateDate";

    /** The Constant MARK_HISTORY. */
    private static final String MARK_HISTORY = "history";

    /** The Constant MARK_SERVICE_FAIT_AVAILABLE. */
    private static final String MARK_SERVICE_FAIT_AVAILABLE = "service_fait_available";

    /** The Constant MARK_TOKEN. */
    private static final String MARK_TOKEN = "token";

    /** The Constant PROPERTY_ID_STATE_SERVICE_FAIT. */
    // PROPERTIES
    public static final String PROPERTY_ID_STATE_SERVICE_FAIT = "signalement.idStateServiceFait";

    /** The signalement service. */
    // SERVICES
    private transient ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The signalement workflow service. */
    private transient IWorkflowService _signalementWorkflowService = SpringContextService.getBean( "signalement.workflowService" );

    /** The notification user multi contents value service. */
    private transient NotificationUserMultiContentsValueService _notificationUserMultiContentsValueService = SpringContextService
            .getBean( "signalement.notificationUserMultiContentsValueService" );

    /** The notification user value service. */
    private transient NotificationUserValueService _notificationUserValueService = SpringContextService.getBean( "signalement.notificationUserValueService" );

    /** The resource history service. */
    private transient IResourceHistoryService _resourceHistoryService = SpringContextService.getBean( "workflow.resourceHistoryService" );

    /**
     * Returns the content of the page accueil.
     *
     * @param request
     *            The HTTP request
     * @return The view
     * @throws UserNotSignedException
     *             {@link UserNotSignedException}
     */
    @View( value = AbstractXPage.XPAGE_SUIVI, defaultView = true )
    public XPage viewAccueil( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        String erreur = "";
        SuiviDTO suiviDTO = new SuiviDTO( );
        populate( suiviDTO, request );

        try
        {
            getSignalement( request, model, suiviDTO );
        }
        catch( BusinessException e )
        {
            AppLogService.error( e );
            erreur = e.getCode( );
        }

        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );
        model.put( MARK_MESSAGE_ERREUR, erreur );

        return getXPage( TEMPLATE_XPAGE_SUIVI_SIGNALEMENT, request.getLocale( ), model );
    }

    /**
     * Returns the content of the page accueil.
     *
     * @param request
     *            The HTTP request
     * @return The view
     */
    @Action( ACTION_VALIDER_CHECKBOX )
    public XPage validerCheckbox( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        return getXPage( TEMPLATE_XPAGE_SUIVI_SIGNALEMENT, request.getLocale( ), model );
    }

    /**
     * Fill model with Signalement data.
     *
     * @param request
     *            The HTTP request
     * @param model
     *            The model
     * @param suiviDTO
     *            SuiviDTO
     * @return the signalement
     */
    private void getSignalement( HttpServletRequest request, Map<String, Object> model, SuiviDTO suiviDTO )
    {
        String token = suiviDTO.getToken( );
        int nIdWorkflow = _signalementWorkflowService.getSignalementWorkflowId( );

        Signalement signalement;

        if ( token != null )
        {
            signalement = _signalementService.getSignalementByToken( token );

            if ( signalement == null )
            {
                throw new BusinessException( new Signalement( ),
                        I18nService.getLocalizedString( MESSAGE_ERREUR_SIGNALEMENT_INTROUVABLE, request.getLocale( ) ) );
            }

            WorkflowService workflowService = WorkflowService.getInstance( );

            // check if this incident can be resolved
            State stateOfSignalement = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, null );
            int idStatutServiceFait = AppPropertiesService.getPropertyInt( PROPERTY_ID_STATE_SERVICE_FAIT, -1 );
            int idActionServiceFait = _signalementWorkflowService.selectIdActionByStates( stateOfSignalement.getId( ), idStatutServiceFait );

            if ( idActionServiceFait > -1 )
            {
                model.put( MARK_SERVICE_FAIT_AVAILABLE, true );

                if ( request.getParameter( PARAMETER_VALIDATE_SUIVI ) != null )
                {
                    workflowService.doProcessAction( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, idActionServiceFait, null, request,
                            request.getLocale( ), true );
                    // Une fois service fait, réaffichage avec les nouvelles données
                    model.remove( MARK_SERVICE_FAIT_AVAILABLE );
                    stateOfSignalement = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, null );
                }
            }

            // get the current state
            String strState = _signalementService.changeToGoodStateForSuivi( stateOfSignalement );

            // get the current state's date
            ResourceHistory rhLastState = _resourceHistoryService.getLastHistoryResource( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE,
                    nIdWorkflow );

            Date dateLastState = new Date( rhLastState.getCreationDate( ).getTime( ) );
            SimpleDateFormat sdfDate = new SimpleDateFormat( "dd/MM/yyyy" );
            String strDateLastState = sdfDate.format( dateLastState );

            // get the history list
            List<HistorySignalementDTO> listHistory = getHistorySignalement( signalement.getId( ).intValue( ) );

            model.put( MARK_SIGNALEMENT, signalement );
            model.put( MARK_CURRENT_STATE, strState );
            model.put( MARK_DATE_LAST_STATE, strDateLastState );
            model.put( MARK_HISTORY, listHistory );
            model.put( MARK_TOKEN, token );
        }
        else
        {
            signalement = null;
            throw new BusinessException( signalement, I18nService.getLocalizedString( MESSAGE_ERREUR_SIGNALEMENT_INTROUVABLE, request.getLocale( ) ) );
        }
    }

    /**
     * Get the history of a signalement with its id.
     *
     * @param nIdSignalement
     *            the signalement id
     * @return the list of history signalement dto
     */
    private List<HistorySignalementDTO> getHistorySignalement( int nIdSignalement )
    {
        List<HistorySignalementDTO> listHistoryDTO = new ArrayList<>( );

        List<ResourceHistory> listHistory = _resourceHistoryService.getAllHistoryByResource( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE,
                _signalementWorkflowService.getSignalementWorkflowId( ) );

        ListIterator<ResourceHistory> iterator = listHistory.listIterator( );

        while ( iterator.hasNext( ) )
        {
            ResourceHistory rh = iterator.next( );

            HistorySignalementDTO dto = new HistorySignalementDTO( );

            // get the date of action
            Date dateLastState = new Date( rh.getCreationDate( ).getTime( ) );
            SimpleDateFormat sdfDate = new SimpleDateFormat( "dd/MM/yyyy" );
            dto.setDate( sdfDate.format( dateLastState ) );

            // get the state
            State stateAfterAction = rh.getAction( ).getStateAfter( );
            dto.setState( _signalementService.changeToGoodStateForSuivi( stateAfterAction ) );

            // get the message
            NotificationUserMultiContentsValue notificationUser3ContentsValue = _notificationUserMultiContentsValueService.loadByHistory( rh.getId( ) );

            NotificationUserValue notificationUserValue = _notificationUserValueService.loadByHistory( rh.getId( ) );

            if ( ( notificationUser3ContentsValue != null ) && !notificationUser3ContentsValue.getValue( ).equals( StringUtils.EMPTY ) )
            {
                // add the history only if there's a saved email in database
                dto.setMessage( notificationUser3ContentsValue.getValue( ) );
                listHistoryDTO.add( dto );
            }

            if ( ( notificationUserValue != null ) && !notificationUserValue.getValue( ).equals( StringUtils.EMPTY ) )
            {
                // add the history only if there's a saved email in database (case of creation only)
                dto.setMessage( notificationUserValue.getValue( ) );
                listHistoryDTO.add( dto );
            }
        }

        return listHistoryDTO;
    }
}
