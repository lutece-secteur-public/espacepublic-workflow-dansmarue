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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.IWorkflowSignalementUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;

/**
 * The Class WorkflowSignalementUtil.
 */
public class WorkflowSignalementUtil implements IWorkflowSignalementUtil
{

    /** The Constant PARAMETER_MOTIF_REJET. */
    private final String PARAMETER_MOTIF_REJET = "motif_rejet";

    /** The Constant PARAMETER_MOTIF_AUTRE_CHECKBOX. */
    private final String PARAMETER_MOTIF_AUTRE_CHECKBOX = "motif_autre_checkbox";

    /** The Constant PARAMETER_MOTIF_AUTRE. */
    private final String PARAMETER_MOTIF_AUTRE = "motif_autre";

    /** The Constant MOTIF_REJET_PREPEND. */
    private final String MOTIF_REJET_PREPEND = "- ";

    /** The Constant MOTIF_REJET_SEPARATOR. */
    private final String MOTIF_REJET_SEPARATOR = "<br/>";

    /**
     * Instantiates a new workflow signalement util.
     */
    private WorkflowSignalementUtil( )
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getMotifsRejet( HttpServletRequest request, List<ObservationRejet> observationList )
    {

        if ( null == request )
        {
            return Collections.emptyList( );
        }
        List<String> motifsRejet = new ArrayList<>( );
        String [ ] motifsRejetIds = request.getParameterValues( PARAMETER_MOTIF_REJET );
        if ( !ArrayUtils.isEmpty( motifsRejetIds ) )
        {
            for ( ObservationRejet observation : observationList )
            {
                for ( String motifRejetId : motifsRejetIds )
                {
                    Integer motifRejetInt = Integer.parseInt( motifRejetId );
                    if ( observation.getActif( ) && observation.getId( ).equals( motifRejetInt ) )
                    {
                        motifsRejet.add( observation.getLibelle( ) );
                    }
                }
            }
        }
        return motifsRejet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildValueMotifRejetForEmailNotification( HttpServletRequest request, List<ObservationRejet> observationList )
    {

        if ( request == null )
        {
            return null;
        }

        List<String> motifsRejet = getMotifsRejet( request, observationList );
        boolean motifAutreCheckBox = StringUtils.isNotBlank( request.getParameter( PARAMETER_MOTIF_AUTRE_CHECKBOX ) );

        StringBuilder motifsRejetStr = new StringBuilder( );
        // Construction de la variable raisons_rejet
        if ( CollectionUtils.isNotEmpty( motifsRejet ) || motifAutreCheckBox )
        {

            if ( CollectionUtils.isNotEmpty( motifsRejet ) )
            {
                int motifsRejetSize = motifsRejet.size( );
                for ( int i = 0; i < motifsRejetSize; i++ )
                {
                    String motifRejet = motifsRejet.get( i );
                    motifsRejetStr.append( MOTIF_REJET_PREPEND ).append( motifRejet );
                    if ( ( i < ( motifsRejetSize - 1 ) ) || motifAutreCheckBox )
                    {
                        motifsRejetStr.append( MOTIF_REJET_SEPARATOR );
                    }
                }
            }

            if ( motifAutreCheckBox )
            {
                String motifAutre = request.getParameter( PARAMETER_MOTIF_AUTRE );
                motifsRejetStr.append( MOTIF_REJET_PREPEND ).append( motifAutre );
            }
        }
        return motifsRejetStr.toString( );
    }
}
