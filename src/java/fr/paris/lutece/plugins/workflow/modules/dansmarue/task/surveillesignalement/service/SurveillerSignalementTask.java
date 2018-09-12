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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.surveillesignalement.service;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;

/**
 *
 * SurveillerSignalementTask
 *
 */
public class SurveillerSignalementTask extends AbstractSignalementTask
{


    @Inject
    @Named( "signalementService" )
    private ISignalementService            _signalementService;

    private static final String TASK_TITLE = "Mise en surveillance de l'anomalie";

    private static final String PARAMETER_MISE_EN_SURVEILLANCE = "miseEnsurveillance";
    private static final String PARAMETER_SIGNALEMENT_ID = "signalement_id";

    @Override
    public Map<String, String> getTaskFormEntries( Locale arg0 )
    {
        return null;
    }

    @Override
    public String getTitle( Locale arg0 )
    {
        return TASK_TITLE;
    }

    @Override
    public void processTask( int nIdResource, HttpServletRequest request, Locale local )
    {
        String dateMiseEnSurveillance = request.getParameter( PARAMETER_MISE_EN_SURVEILLANCE );
        String idSignalement = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        _signalementService.doMettreSousSurveillance( Integer.parseInt( idSignalement ), dateMiseEnSurveillance );

    }

}
