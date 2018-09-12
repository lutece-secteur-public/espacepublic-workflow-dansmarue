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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.informationssignalement.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignaleurService;
import fr.paris.lutece.plugins.workflow.web.task.NoConfigTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * The notification component
 */
public class InformationsSignalementComponent extends NoConfigTaskComponent
{
    //MARKERS
    private static final String MARK_PHOTOS = "photos";
    private static final String MARK_ADRESSE = "adresse";
    private static final String MARK_SIGNALEMENT = "signalement";
    private static final String MARK_SIGNALEUR = "signaleur";

    //PARAMETERS

    //MESSAGES


    //TEMPLATES
    private static final String TEMPLATE_TASK_INFORMATIONS_SIGNALEMENT_FORM = "admin/plugins/workflow/modules/signalement/task_informations_signalement_form.html";

    //CONSTANTS

    //JSP

    //SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    @Inject
    @Named( "adresseSignalementService" )
    private IAdresseService _adresseService;

    @Inject
    @Named("signaleurService")
    private ISignaleurService signaleurService;

    @Inject
    @Named( "photoService" )
    private IPhotoService _photoService;

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request,
            Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        Adresse adresse = null;
        Signaleur signaleur = null;
        List<PhotoDMR> photos = new ArrayList<PhotoDMR>();
        Signalement signalement = _signalementService.getSignalement(nIdResource);

        adresse = _adresseService.loadByIdSignalement(nIdResource);
        signaleur = signaleurService.loadByIdSignalement(nIdResource);
        photos = _photoService.findBySignalementId(nIdResource);

        model.put(MARK_SIGNALEMENT, signalement);
        model.put(MARK_PHOTOS, photos);
        model.put(MARK_ADRESSE, adresse);
        model.put(MARK_SIGNALEUR, signaleur);

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_INFORMATIONS_SIGNALEMENT_FORM, locale,
                model );
        return template.getHtml( );
    }

    @Override
    public String getDisplayTaskInformation(int nIdHistory, HttpServletRequest request, Locale locale, ITask task) {
        return null;
    }

    @Override
    public String getTaskInformationXml(int nIdHistory, HttpServletRequest request, Locale locale, ITask task) {
        return null;
    }

    @Override
    public String doValidateTask(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
            ITask task) {
        return null;
    }

}
