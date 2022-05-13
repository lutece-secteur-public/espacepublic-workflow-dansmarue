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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.servicefaitsignalement.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.DepotManager;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.servicefaitsignalement.service.ServiceFaitSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.utils.ServiceOption;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * ServiceFaitComponent.
 */
public class ServiceFaitComponent extends AbstractTaskComponent
{

    /** The Constant PARAMETER_ADRESSE_EMAIL. */
    private static final String PARAMETER_ADRESSE_EMAIL = "adresseEmail";

    /** The Constant KEY_INVALID_EMAIL_FORMAT. */
    private static final String KEY_INVALID_EMAIL_FORMAT = "module.workflow.dansmarue.serviceFaitSignalement.adresse.email.mauvais.format";

    /** The Constant KEY_HEURE_DE_PASSAGE_MAUVAIS_FORMAT. */
    private static final String KEY_HEURE_DE_PASSAGE_MAUVAIS_FORMAT = "module.workflow.dansmarue.serviceFaitSignalement.heure.de.passage.mauvais.format";

    /** The Constant KEY_DATE_DE_PASSAGE_MAUVAIS_FORMAT. */
    private static final String KEY_DATE_DE_PASSAGE_MAUVAIS_FORMAT = "module.workflow.dansmarue.serviceFaitSignalement.date.de.passage.mauvais.format";

    /** The Constant KEY_HEURE_DE_PASSAGE_NULL. */
    private static final String KEY_HEURE_DE_PASSAGE_NULL = "module.workflow.dansmarue.serviceFaitSignalement.heure.de.passage.null";

    /** The Constant KEY_DATE_DE_PASSAGE_NULL. */
    private static final String KEY_DATE_DE_PASSAGE_NULL = "module.workflow.dansmarue.serviceFaitSignalement.date.de.passage.null";

    /** The Constant MARK_IS_ROAD_MAP. */
    // MARK
    private static final String MARK_IS_ROAD_MAP = "isRoadMap";

    /** The Constant MARK_LIST_DEPOTS. */
    private static final String MARK_LIST_DEPOTS = "listDepots";

    /** The Constant PARAMETER_IS_ROAD_MAP. */
    // PARAMETERS
    private static final String PARAMETER_IS_ROAD_MAP = "isRoadMap";

    /** The Constant PARAMETER_SERVICE_OPTION. */
    private static final String PARAMETER_SERVICE_OPTION = "serviceOption";

    /** The Constant TEMPLATE_TASK_FORM. */
    // TEMPLATES
    private static final String TEMPLATE_TASK_FORM = "admin/plugins/workflow/modules/signalement/task_service_fait_form.html";

    /** The signalement service. */
    // SERVICES
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /** The adresse service. */
    @Inject
    @Named( "adresseSignalementService" )
    private IAdresseService _adresseService;

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
        String strRoadMap = request.getParameter( PARAMETER_IS_ROAD_MAP );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_IS_ROAD_MAP, Boolean.valueOf( strRoadMap ) );
        Signalement signalement = _signalementService.getSignalement( nIdResource );
        if ( signalement != null )
        {
            List<Adresse> listAdresses = new ArrayList<>( );
            Adresse adresse = _adresseService.loadByIdSignalement( nIdResource );
            if ( adresse != null )
            {
                listAdresses.add( adresse );
            }

            signalement.setAdresses( listAdresses );
        }

        model.put( SignalementConstants.MARK_LOCALE, locale );

        DepotManager.fillModel( request, signalement, model, MARK_LIST_DEPOTS );

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
     * Gets the task information xml.
     *
     * @param nIdHistory
     *            the n id history
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the task information xml
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
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
        boolean bIsRoadMap = Boolean.parseBoolean( request.getParameter( PARAMETER_IS_ROAD_MAP ) );
        if ( bIsRoadMap )
        {
            String strServiceOption = request.getParameter( PARAMETER_SERVICE_OPTION );
            if ( StringUtils.isBlank( strServiceOption ) || !StringUtils.isNumeric( strServiceOption ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }
            int nServiceOption = Integer.parseInt( strServiceOption );
            if ( nServiceOption == ServiceOption.SIGNALE.getId( ) )
            {
                try
                {
                    DepotManager.doValidate( request );
                }
                catch( FunctionnalException fe )
                {
                    AppLogService.error( fe );
                    return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
                }
            }

            String dateDePassage = request.getParameter( ServiceFaitSignalementTask.PARAMETER_DATE_DE_PASSAGE );
            String heureDePassage = request.getParameter( ServiceFaitSignalementTask.PARAMETER_HEURE_DE_PASSAGE );

            if ( StringUtils.isBlank( dateDePassage ) )
            {
                return AdminMessageService.getMessageUrl( request, KEY_DATE_DE_PASSAGE_NULL, AdminMessage.TYPE_STOP );
            }
            else
                if ( StringUtils.isBlank( heureDePassage ) )
                {
                    return AdminMessageService.getMessageUrl( request, KEY_HEURE_DE_PASSAGE_NULL, AdminMessage.TYPE_STOP );
                }
                else
                {

                    if ( !isValidFormat( DateUtils.DATE_FR, dateDePassage ) )
                    {

                        return AdminMessageService.getMessageUrl( request, KEY_DATE_DE_PASSAGE_MAUVAIS_FORMAT, AdminMessage.TYPE_STOP );
                    }
                    else
                        if ( !isValidFormat( DateUtils.HOUR_FR, heureDePassage ) )
                        {
                            return AdminMessageService.getMessageUrl( request, KEY_HEURE_DE_PASSAGE_MAUVAIS_FORMAT, AdminMessage.TYPE_STOP );
                        }
                }

            String email = request.getParameter( PARAMETER_ADRESSE_EMAIL );
            if ( StringUtils.isNotBlank( email ) && !validateEmail( email ) )
            {

                return AdminMessageService.getMessageUrl( request, KEY_INVALID_EMAIL_FORMAT, AdminMessage.TYPE_STOP );

            }

        }
        return null;
    }

    /**
     * method to check whether date format is value.
     *
     * @param format
     *            string format to validate
     * @param value
     *            string to validate
     * @return isValid boolean
     */
    public static boolean isValidFormat( String format, String value )
    {
        Date date = null;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat( format );
            date = sdf.parse( value );
            if ( !value.equals( sdf.format( date ) ) )
            {
                date = null;
            }
        }
        catch( ParseException ex )
        {
            AppLogService.error( ex );
        }
        return date != null;
    }

    /**
     * Validate hex with regular expression.
     *
     * @param email
     *            email for validation
     * @return true valid email, false invalid email
     */
    public boolean validateEmail( final String email )
    {

        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile( emailPattern );
        matcher = pattern.matcher( email );
        return matcher.matches( );

    }

}
