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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.ajoutentitehistorique.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * NotificationSignalementTask class.
 */
public class AjoutEntiteHistoriqueTask extends AbstractSignalementTask
{

    /** The Constant TASK_TITLE. */
    // TITLE
    private static final String TASK_TITLE = "Ajout entité historique";

    /** The ajout entite service. */
    // SERVICES
    @Inject
    @Named( "signalement.ajoutEntiteService" )
    private AjoutEntiteService _ajoutEntiteService;

    @Inject
    private ISignalementService _signalementService;

    /** The unit service. */
    private IUnitService _unitService = SpringContextService.getBean( "unittree.unitService" );

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

        String labelEntite = "";

        boolean signalementPrestataire = ( this.getAction( ).getStateBefore( ).getId( ) == SignalementConstants.ID_STATE_TRANSFERE_PRESTATAIRE.intValue( ) )
                || ( this.getAction( ).getStateBefore( ).getId( ) == SignalementConstants.ID_STATE_PROGRAMME_PRESTATAIRE.intValue( ) );

        if ( signalementPrestataire )
        {
            // Action prestataire effectue via WS
            int idSignalement = this.getIdSignalement( nIdResourceHistory );
            labelEntite = this._signalementService.findPrestataireSignalement( idSignalement );
        }
        else
        {
            // Action effectue depuis le BO
            // Récupération de l'entité de l'user connecté
            AdminUser adminUser = AdminUserService.getAdminUser( request );

            List<Unit> userUnitsList = new ArrayList<>( );

            if ( adminUser != null )
            {
                userUnitsList = this._unitService.getUnitsByIdUser( adminUser.getUserId( ), false );
            }
            labelEntite = ( !userUnitsList.isEmpty( ) && ( userUnitsList.get( 0 ) != null ) ) ? userUnitsList.get( 0 ).getLabel( ) : "";
        }

        // Stockage de l'entité
        if ( StringUtils.isNotBlank( labelEntite ) )
        {
            this._ajoutEntiteService.create( nIdResourceHistory, this.getId( ), labelEntite );
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
