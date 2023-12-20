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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.rejetsignalement.web;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.utils.IListUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
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
 * The Class RejetSignalementComponent.
 */
public class RejetSignalementComponent extends AbstractTaskComponent
{

    /** The Constant TEMPLATE_TASK_FORM. */
    private static final String TEMPLATE_TASK_FORM = "admin/plugins/workflow/modules/signalement/task_rejet_signalement_form.html";

    /** The Constant MESSAGE_ERROR_EMPTY_MOTIF_REJET. */
    // MESSAGES
    private static final String MESSAGE_ERROR_EMPTY_MOTIF_REJET = "module.workflow.dansmarue.rejet.error.emptymotif";

    /** The Constant MESSAGE_ERROR_EMPTY_MOTIF_AUTRE. */
    private static final String MESSAGE_ERROR_EMPTY_MOTIF_AUTRE = "module.workflow.dansmarue.rejet.error.empty.motif.autre";

    /** The Constant MARK_SIGNALEMENT. */
    // MARKERS
    private static final String MARK_SIGNALEMENT = "signalement";

    /** The Constant MARK_OBSERVATION_REJET_LIST. */
    private static final String MARK_OBSERVATION_REJET_LIST = "observation_list";

    /** The Constant MARK_HAS_EMAIL_SIGNALEUR. */
    private static final String MARK_HAS_EMAIL_SIGNALEUR = "has_email_signaleur";

    /** The Constant PARAMETER_MOTIF_REJET. */
    // PARAMETERS
    private static final String PARAMETER_MOTIF_REJET = "motif_rejet";

    /** The Constant PARAMETER_MOTIF_AUTRE_CHECKBOX. */
    private static final String PARAMETER_MOTIF_AUTRE_CHECKBOX = "motif_autre_checkbox";

    /** The Constant PARAMETER_MOTIF_AUTRE. */
    private static final String PARAMETER_MOTIF_AUTRE = "motif_autre";

    /** The signalement service. */
    // SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /** The signalement workflow service. */
    @Inject
    @Named( "signalement.workflowService" )
    private IWorkflowService _signalementWorkflowService;

    /** The observation rejet service. */
    @Inject
    @Named( "observationRejetService" )
    private IObservationRejetService _observationRejetService;

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
        Map<String, Object> model = new HashMap<>( );

        Signalement signalement = _signalementService.getSignalement( nIdResource );

        boolean hasEmailSignaleurOrFallower = false;

        if ( ( signalement.getSuivi( ) > 0 ) || ( ( null != signalement ) && CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) )
                && !StringUtils.isBlank( signalement.getSignaleurs( ).get( 0 ).getMail( ) ) ) )
        {
            hasEmailSignaleurOrFallower = true;

        }

        model.put( MARK_HAS_EMAIL_SIGNALEUR, hasEmailSignaleurOrFallower );

        // get the signalement
        model.put( MARK_SIGNALEMENT, signalement );

        // get all the type signalement
        List<ObservationRejet> types = _observationRejetService.getAllObservationRejetActif( );
        ReferenceList listeObservation = _listUtils.toReferenceList( types, "id", "libelle", null );
        model.put( MARK_OBSERVATION_REJET_LIST, listeObservation );
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
        boolean hasEmailSignaleur = false;
        Signalement signalement = _signalementService.getSignalement( nIdResource );
        if ( ( null != signalement ) && CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) )
                && !StringUtils.isBlank( signalement.getSignaleurs( ).get( 0 ).getMail( ) ) )
        {

            hasEmailSignaleur = true;

        }

        // Pas de vérification si le mail n'est pas renseigné
        if ( !hasEmailSignaleur )
        {
            return null;
        }

        String [ ] motifsRejetIds = request.getParameterValues( PARAMETER_MOTIF_REJET );
        boolean motifAutreCheckbox = StringUtils.isNotBlank( request.getParameter( PARAMETER_MOTIF_AUTRE_CHECKBOX ) );

        boolean emptyMotif = ArrayUtils.isEmpty( motifsRejetIds );

        if ( emptyMotif && !motifAutreCheckbox )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_EMPTY_MOTIF_REJET, AdminMessage.TYPE_STOP );
        }

        // Vérification si les motifs sont valides
        if ( !emptyMotif )
        {
            List<ObservationRejet> observationList = _observationRejetService.getAllObservationRejetActif( );
            List<ObservationRejet> motifsRejet = new ArrayList<>( );
            for ( ObservationRejet observation : observationList )
            {
                for ( String motifRejetId : motifsRejetIds )
                {
                    Integer motifRejetInt = Integer.parseInt( motifRejetId );
                    if ( observation.getActif( ) && observation.getId( ).equals( motifRejetInt ) )
                    {
                        motifsRejet.add( observation );
                    }
                }
            }

            if ( CollectionUtils.isEmpty( motifsRejet ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_EMPTY_MOTIF_REJET, AdminMessage.TYPE_STOP );
            }
        }
        String motifAutre = request.getParameter( PARAMETER_MOTIF_AUTRE );
        if ( motifAutreCheckbox && StringUtils.isBlank( motifAutre ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_EMPTY_MOTIF_AUTRE, AdminMessage.TYPE_STOP );
        }

        return null;
    }

}
