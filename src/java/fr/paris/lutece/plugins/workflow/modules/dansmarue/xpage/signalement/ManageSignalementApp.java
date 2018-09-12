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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.xpage.signalement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * the XPage app to manage signalement without Web Service
 */
public class ManageSignalementApp implements XPageApplication
{

    private final static String TEMPLATE_SUCCESS    = "skin/plugins/workflow/modules/signalement/managewithoutws/manage_success.html";
    private final static String TEMPLATE_ACTIONS    = "skin/plugins/workflow/modules/signalement/managewithoutws/mange_actions.html";

    private static final String MARK_MOTIFS         = "motifs";
    private static final String MARK_ID             = "id";
    private static final String MARK_ACTION         = "action";

    private ISignalementService _signalementService = ( ISignalementService ) SpringContextService.getBean( "signalementService" );

    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin ) throws UserNotSignedException, SiteMessageException
    {
        XPage page = new XPage( );
        String templatePath = null;
        Map<String, Object> model = new HashMap<String, Object>( );

        String action = getAction( request );
        Signalement bean = getSignalement( request );

        // if blank, only display the choice page
        if ( true || ( StringUtils.isBlank( action ) && ( bean != null ) ) )
        {
            addMotifs( model );
            templatePath = TEMPLATE_ACTIONS;
        }
        // else, validate or not the action
        else if ( bean != null )
        {
            templatePath = TEMPLATE_SUCCESS;

        }

        HtmlTemplate template = AppTemplateService.getTemplate( templatePath, request.getLocale( ), model );

        page.setContent( template.getHtml( ) );
        page.setPathLabel( "Gestion des signalements" );
        page.setTitle( "Gestion des signalements" );

        return page;
    }

    /**
     * AddMotifs.
     *
     * @param model
     *            the model.
     */
    private void addMotifs( Map<String, Object> model )
    {
        List<String[]> motifs = new ArrayList<String[]>( );
        motifs.add( new String[] { "1", "Observation de rejet 1" } );
        motifs.add( new String[] { "2", "Observation de rejet 2" } );
        model.put( MARK_MOTIFS, motifs );
    }

    /**
     * Get signalement if parameters are correct.
     *
     * @param request
     *            the http request
     * @return the signalement
     */
    private Signalement getSignalement( HttpServletRequest request )
    {
        Signalement bean = null;
        String strIdSignalement = request.getParameter( MARK_ID );
        String token = request.getParameter( "token" );
        if ( StringUtils.isNumeric( strIdSignalement ) )
        {
            Integer id = Integer.valueOf( strIdSignalement );
            bean = _signalementService.getSignalement( id );
            if ( ( bean != null ) && ( StringUtils.isBlank( token ) || !token.equals( bean.getToken( ) ) ) )
            {
                bean = null;
                AppLogService.error( "Cannot get signalement bean with id " + strIdSignalement + " and token " + token );
            }
        }
        return bean;
    }

    /**
     * Get the action asked by user
     *
     * @param request
     *            the http request
     *
     * @return the action
     */
    private String getAction( HttpServletRequest request )
    {
        return request.getParameter( MARK_ACTION );
    }

}
