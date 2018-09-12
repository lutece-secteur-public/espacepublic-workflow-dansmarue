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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.rejetsignalement.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * RejetSignalementTask class
 *
 */
public class RejetSignalementTask extends AbstractSignalementTask
{
    private static final String                 TASK_TITLE                          = "Rejet du signalement";

    // MARKERS
    private static final String                 PARAMETER_MOTIF_REJET               = "motif_rejet";
    private static final String                 PARAMETER_MOTIF_AUTRE_CHECKBOX      = "motif_autre_checkbox";
    private static final String                 PARAMETER_MOTIF_AUTRE               = "motif_autre";

    // SERVICES
    private IObservationRejetSignalementService _observationRejetSignalementService = SpringContextService.getBean( "observationRejetSignalementService" );
    private ISignalementService                 _signalementService                 = SpringContextService.getBean( "signalementService" );

    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        Integer idRessource = resourceHistory.getIdResource( );
        String[] motifsRejetIds = request.getParameterValues( PARAMETER_MOTIF_REJET );
        SimpleDateFormat sdfDate = new SimpleDateFormat( DateUtils.DATE_FR );
        String date = sdfDate.format( Calendar.getInstance( ).getTime( ) );

        boolean motifAutreCheckBox = StringUtils.isNotBlank( request.getParameter( PARAMETER_MOTIF_AUTRE_CHECKBOX ) );

        if ( !ArrayUtils.isEmpty( motifsRejetIds ) )
        {
            for ( String observationRejetId : motifsRejetIds )
            {
                _observationRejetSignalementService.insert( idRessource, Integer.parseInt( observationRejetId ), null );
                _signalementService.setDateRejet( idRessource, date );
            }
        }
        String motifAutre = null;
        if ( motifAutreCheckBox )
        {
            motifAutre = request.getParameter( PARAMETER_MOTIF_AUTRE );
            _observationRejetSignalementService.insert( idRessource, null, motifAutre );
            _signalementService.setDateRejet( idRessource, date );
        }

    }

    @Override
    public String getTitle( Locale locale )
    {
        return TASK_TITLE;
    }

    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )
    {
        return null;
    }
}
