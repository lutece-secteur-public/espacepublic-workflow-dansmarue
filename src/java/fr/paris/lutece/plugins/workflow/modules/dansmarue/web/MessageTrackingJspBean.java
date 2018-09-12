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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.NotificationSignalementTaskConfigService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.NotificationSignalementTaskConfigUnitService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * the MessageTrackingJspBean class
 *
 */
public class MessageTrackingJspBean extends AbstractJspBean
{
    // RIGHT
    public static final String                           RIGHT_MANAGE_MESSAGE_TRACKING                 = "MESSAGE_TRACKING_MANAGEMENT";

    // TEMPLATES
    private static final String                          TEMPLATE_MANAGE_TRACKING_MESSAGE              = "admin/plugins/workflow/modules/signalement/manage_tracking_message.html";

    // MARKERS
    private static final String                          MARK_UNITS_LIST                               = "units_list";
    private static final String                          MARK_UNITS_LIST_USER                          = "units_list_user";
    private static final String                          MARK_MAIL_USER                                = "mail_user";

    // MESSAGES
    private static final String                          MESSAGE_ERROR_NO_UNIT_SELECTED                = "module.workflow.dansmarue.message.error.noUnitSelected";
    private static final String                          MESSAGE_ERROR_UNIT_ALREADY_SELECTED           = "module.workflow.dansmarue.message.error.unitAlreadySelected";

    // PROPERTIES
    private static final String                          PROPERTY_ID_TASK_NOTIFICATION_SIGNALEMENT     = "workflow.signalement.idTaskSignalementNotification";

    // PARAMETERS
    private static final String                          PARAMETER_ID_UNIT                             = "idUnit";
    private static final String                          PARAMETER_BACK                                = "back";
    private static final String                          PARAMETER_UNIT_ID_UNIT                        = "unit.idUnit";
    private static final String                          PARAMETER_NO_UNIT_SELECTED                    = "-1";

    // JSP
    private static final String                          JSP_MANAGE_TRACKING_MESSAGE                   = "jsp/admin/plugins/workflow/modules/signalement/GetMessageTrackingManagement.jsp";
    private static final String                          JSP_MENU_LUTECE                               = "jsp/admin/AdminMenu.jsp";

    // SERVICES
    private IUnitService                                 _unitService                                  = ( IUnitService ) SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
    private NotificationSignalementTaskConfigService     _notificationSignalementTaskConfigService     = ( NotificationSignalementTaskConfigService ) SpringContextService
            .getBean( "signalement.notificationSignalementTaskConfigService" );
    private NotificationSignalementTaskConfigUnitService _notificationSignalementTaskConfigUnitService = ( NotificationSignalementTaskConfigUnitService ) SpringContextService
            .getBean( "signalement.notificationSignalementTaskConfigUnitService" );

    /**
     * The tracking message management page
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String getManageTrackingMessage( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        // Get the current user's email
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        String mailCurrentUser = adminUser.getEmail( );
        model.put( MARK_MAIL_USER, mailCurrentUser );

        // Get the allowed entities for user (select list)
        List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );
        List<Unit> listAllAllowedUnits = new ArrayList<Unit>( );
        for ( Unit userUnit : listUnits )
        {
            if ( userUnit.getIdUnit( ) != 0 )
            {
                listAllAllowedUnits.add( userUnit );
                getUnitsForSelectList( userUnit.getIdUnit( ), listAllAllowedUnits );
            } else
            {
                // If the user is associated with the root unit
                listAllAllowedUnits = _unitService.getAllUnits( false );
                break;
            }
        }

        sortListEntitiesAlphabetical( listAllAllowedUnits );
        ReferenceList listeUnits = ListUtils.toReferenceList( listAllAllowedUnits, "idUnit", "label", "" );
        model.put( MARK_UNITS_LIST, listeUnits );

        // Get the entities already linked to the user email (notification enabled)
        // IMPORTANT : Cf workflow-signalement.properties to see the id task
        List<Unit> listEntitiesUser = getEntitiesLinkedToMailUser( mailCurrentUser );
        sortListEntitiesAlphabetical( listEntitiesUser );

        model.put( MARK_UNITS_LIST_USER, listEntitiesUser );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_TRACKING_MESSAGE, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Sort a list of entities (alphabetical)
     *
     * @param listEntitiesUser
     *            the list of entities
     */
    private void sortListEntitiesAlphabetical( List<Unit> listEntitiesUser )
    {
        Collections.sort( listEntitiesUser, new Comparator<Unit>( )
        {
            @Override
            public int compare( Unit o1, Unit o2 )
            {
                return o1.getLabel( ).compareTo( o2.getLabel( ) );
            }
        } );
    }

    /**
     * Get the entities linked to the user's email.
     *
     * @param mailCurrentUser
     *            the user mail
     * @return the list of units the list of units
     */
    private List<Unit> getEntitiesLinkedToMailUser( String mailCurrentUser )
    {
        String strIdsTaskNotification = AppPropertiesService.getProperty( PROPERTY_ID_TASK_NOTIFICATION_SIGNALEMENT );
        String[] idsTaskNotification = strIdsTaskNotification.split( "," );
        List<Unit> listEntitiesUser = new ArrayList<Unit>( );

        for ( int i = 0; i < idsTaskNotification.length; i++ )
        {
            int nIdTask = Integer.parseInt( idsTaskNotification[i] );
            NotificationSignalementTaskConfigDTO configDTO = _notificationSignalementTaskConfigService.findByPrimaryKey( nIdTask );

            for ( NotificationSignalementTaskConfigUnit unit : configDTO.getListConfigUnit( ) )
            {
                if ( unit.getDestinataires( ).contains( mailCurrentUser ) )
                {
                    listEntitiesUser.add( unit.getUnit( ) );
                }
            }
        }
        return listEntitiesUser;
    }

    /**
     * Delete a unit for a user from message tracking management page.
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doDeleteUnitMessageTracking( HttpServletRequest request )
    {

        String strIdUnitToDelete = request.getParameter( PARAMETER_ID_UNIT );
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        String mailCurrentUser = adminUser.getEmail( );

        // 1-split ; 2-remove from the list ; 3-concat ; 4-update in database
        if ( strIdUnitToDelete != null )
        {
            int nIdUnitToDelete = Integer.parseInt( strIdUnitToDelete );

            // get all the config where the email is saved
            List<NotificationSignalementTaskConfigUnit> listConfigUnit = _notificationSignalementTaskConfigUnitService.findByIdUnit( nIdUnitToDelete, getPlugin( ) );

            // remove the email everywhere (format of destinataires : email1@mail.com;email2@mail.com;email3@mail.com)
            for ( NotificationSignalementTaskConfigUnit configUnit : listConfigUnit )
            {
                String[] listRecipient = configUnit.getDestinataires( ).split( ";" );
                List<String> listNewRecipient = new ArrayList<String>( );
                for ( String recipient : listRecipient )
                {
                    if ( !recipient.equals( mailCurrentUser ) )
                    {
                        listNewRecipient.add( recipient );
                    }
                }

                String strNewRecipients = StringUtils.join( listNewRecipient, ";" );

                if ( strNewRecipients.equals( StringUtils.EMPTY ) )
                {
                    // US06-RCI02 : if the recipient string becomes empty -> delete the line in database
                    _notificationSignalementTaskConfigUnitService.delete( configUnit.getIdTask( ), configUnit.getUnit( ).getIdUnit( ), getPlugin( ) );
                } else
                {
                    // else just update by removing the current user mail
                    configUnit.setDestinataires( strNewRecipients );
                    _notificationSignalementTaskConfigUnitService.update( configUnit, getPlugin( ) );
                }
            }

        }

        return doGoBack( request );
    }

    /**
     * Add a unit for a user from message tracking management page
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doAddUnitMessageTracking( HttpServletRequest request )
    {
        String url = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_BACK ) ) )
        {
            url = AppPathService.getBaseUrl( request ) + JSP_MENU_LUTECE;
        } else
        {

            String strIdUnit = request.getParameter( PARAMETER_UNIT_ID_UNIT );

            if ( strIdUnit.equals( PARAMETER_NO_UNIT_SELECTED ) )
            {
                url = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_NO_UNIT_SELECTED, AdminMessage.TYPE_STOP );
            } else
            {

                // if the selected unit is already in the list -> alert
                int nIdUnit = Integer.parseInt( strIdUnit );
                AdminUser adminUser = AdminUserService.getAdminUser( request );
                String mailCurrentUser = adminUser.getEmail( );
                boolean alreadyInList = false;

                List<Unit> listEntities = getEntitiesLinkedToMailUser( mailCurrentUser );

                for ( Unit unit : listEntities )
                {
                    if ( unit.getIdUnit( ) == nIdUnit )
                    {
                        alreadyInList = true;
                    }
                }

                if ( alreadyInList )
                {
                    url = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_UNIT_ALREADY_SELECTED, AdminMessage.TYPE_STOP );
                } else
                {
                    // get all the config where the email must be added
                    List<NotificationSignalementTaskConfigUnit> listConfigUnit = _notificationSignalementTaskConfigUnitService.findByIdUnit( nIdUnit, getPlugin( ) );

                    if ( listConfigUnit.isEmpty( ) )
                    {
                        // create new line(s) in database
                        String strIdsTaskNotification = AppPropertiesService.getProperty( PROPERTY_ID_TASK_NOTIFICATION_SIGNALEMENT );
                        String[] idsTaskNotification = strIdsTaskNotification.split( "," );

                        for ( String strIdTask : idsTaskNotification )
                        {
                            int nIdTask = Integer.parseInt( strIdTask );
                            NotificationSignalementTaskConfigUnit configUnit = new NotificationSignalementTaskConfigUnit( );
                            configUnit.setIdTask( nIdTask );
                            configUnit.setDestinataires( mailCurrentUser );
                            configUnit.setUnit( _unitService.getUnit( nIdUnit, true ) );
                            _notificationSignalementTaskConfigUnitService.insert( configUnit, getPlugin( ) );
                        }
                    } else
                    {
                        // add the email in line that already exists (format of destinataires : email1@mail.com;email2@mail.com;email3@mail.com)
                        for ( NotificationSignalementTaskConfigUnit configUnit : listConfigUnit )
                        {
                            String[] listRecipient = configUnit.getDestinataires( ).split( ";" );
                            List<String> listNewRecipient = new ArrayList<String>( );
                            for ( String recipient : listRecipient )
                            {
                                listNewRecipient.add( recipient );
                            }
                            listNewRecipient.add( mailCurrentUser );

                            String strNewRecipients = StringUtils.join( listNewRecipient, ";" );

                            configUnit.setDestinataires( strNewRecipients );
                            _notificationSignalementTaskConfigUnitService.update( configUnit, getPlugin( ) );

                        }
                    }

                    url = doGoBack( request );
                }
            }
        }

        return url;
    }

    /**
     * Return the url of the JSP which called the last action.
     *
     * @param request
     *            The Http request
     * @return The url of the last JSP
     */
    private String doGoBack( HttpServletRequest request )
    {
        String strJspBack = request.getParameter( SignalementConstants.MARK_JSP_BACK );

        return StringUtils.isNotBlank( strJspBack ) ? AppPathService.getBaseUrl( request ) + strJspBack : AppPathService.getBaseUrl( request ) + JSP_MANAGE_TRACKING_MESSAGE;
    }

    /**
     * Get all the hierarchy of units allowed for current user
     *
     * @param nIdUnitUser
     *            the id of the user unit
     * @param listAllUnitUser
     *            the final list
     */
    private void getUnitsForSelectList( int nIdUnitUser, List<Unit> listAllUnitUser )
    {

        List<Unit> listChildren = _unitService.getSubUnits( nIdUnitUser, false );

        if ( ( listChildren != null ) && !listChildren.isEmpty( ) )
        {
            for ( Unit child : listChildren )
            {
                listAllUnitUser.add( child );
                getUnitsForSelectList( child.getIdUnit( ), listAllUnitUser );
            }

        }
    }

}
