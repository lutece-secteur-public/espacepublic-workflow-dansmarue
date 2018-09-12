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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.creationsignalement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.WorkflowSignalementConstants;
import fr.paris.lutece.plugins.workflowcore.service.state.IStateService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * The Class CreationSignalementTask.
 */
public class CreationSignalementTask extends AbstractSignalementTask
{
    private static final String TASK_TITLE    = "Création d'un signalement";

    private IStateService       _stateService = SpringContextService.getBean( "workflow.stateService" );

    @Override
    public String getTitle( Locale locale )
    {
        return TASK_TITLE;
    }

    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        int idSignalement = this.getIdSignalement( nIdResourceHistory );
        Signalement signalement = this.getSignalementService( ).getSignalement( idSignalement );
        boolean isDPE = false;
        if ( signalement != null && signalement.getTypeSignalement( ) != null && signalement.getTypeSignalement( ).getUnit( ) != null
                && signalement.getTypeSignalement( ).getUnit( ).getIdUnit( ) == SignalementConstants.UNIT_DPE )
        {
            isDPE = true;
        }
        if ( signalement != null && this.isSignalementOfTypeEncombrant( signalement ) && isDPE )
        {
            int stateATraiter = AppPropertiesService.getPropertyInt( WorkflowSignalementConstants.ETAT_CREATION_ENCOMBRANT, -1 );
            this.getAction( ).setStateAfter( _stateService.findByPrimaryKey( stateATraiter ) );
        }
    }

    /**
     * Checks if signalement is of type encombrant.
     * 
     * @param signalement
     *            the signalement
     * @return true, if signalement is of type encombrant
     */
    private boolean isSignalementOfTypeEncombrant( Signalement signalement )
    {
        boolean hasType = false;
        TypeSignalement typeSignalement = signalement.getTypeSignalement( );
        if ( typeSignalement == null )
        {
            AppLogService.error( "Pas de type de signalement pour le signalement " + signalement.getId( ) );
        } else
        {
            Integer typeSignalementId = AppPropertiesService.getPropertyInt( SignalementConstants.TYPE_SIGNALEMENT_ENCOMBRANT, -1 );
            List<TypeSignalement> listeTypeSignalement = new ArrayList<TypeSignalement>( );
            this.getTypeSignalementService( ).getAllSousTypeSignalementCascade( typeSignalementId, listeTypeSignalement );

            Integer signalementTypeId = typeSignalement.getId( );
            if ( signalementTypeId != null )
            {
                Iterator<TypeSignalement> iterator = listeTypeSignalement.iterator( );
                while ( !hasType && iterator.hasNext( ) )
                {
                    TypeSignalement next = iterator.next( );
                    hasType = signalementTypeId.equals( next.getId( ) );
                }
            }
        }
        return hasType;
    }

    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )
    {
        return null;
    }
}
