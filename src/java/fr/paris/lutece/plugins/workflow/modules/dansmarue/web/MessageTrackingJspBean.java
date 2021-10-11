/*
 * Copyright (c) 2002-2021, City of Paris
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.service.role.SignalementViewRoleService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigUnit;
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
 * the MessageTrackingJspBean class.
 */
public class MessageTrackingJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5430488988015810896L;

    /** The Constant RIGHT_MANAGE_MESSAGE_TRACKING. */
    // RIGHT
    public static final String RIGHT_MANAGE_MESSAGE_TRACKING = "MESSAGE_TRACKING_MANAGEMENT";

    /** The Constant TEMPLATE_MANAGE_TRACKING_MESSAGE. */
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_TRACKING_MESSAGE = "admin/plugins/workflow/modules/signalement/manage_tracking_message.html";

    /** The Constant MARK_UNITS_LIST. */
    // MARKERS
    private static final String MARK_UNITS_LIST = "units_list";

    /** The Constant MARK_UNITS_LIST_USER. */
    private static final String MARK_UNITS_LIST_USER = "units_list_user";

    /** The Constant MARK_MAIL_USER. */
    private static final String MARK_MAIL_USER = "mail_user";

    /** The Constant MARK_TYPE_LIST. */
    private static final String MARK_TYPE_LIST = "type_list";

    /** The Constant MARK_TYPE_LIST_USER. */
    private static final String MARK_TYPE_LIST_USER = "type_list_user";

    /** The Constant MESSAGE_ERROR_NO_UNIT_SELECTED. */
    // MESSAGES
    private static final String MESSAGE_ERROR_NO_UNIT_SELECTED = "module.workflow.dansmarue.message.error.noUnitSelected";

    /** The Constant MESSAGE_ERROR_UNIT_ALREADY_SELECTED. */
    private static final String MESSAGE_ERROR_UNIT_ALREADY_SELECTED = "module.workflow.dansmarue.message.error.unitAlreadySelected";

    /** The Constant MESSAGE_ERROR_NO_TYPE_SELECTED. */
    private static final String MESSAGE_ERROR_NO_TYPE_SELECTED = "module.workflow.dansmarue.message.error.noTypeSelected";

    /** The Constant MESSAGE_ERROR_TYPE_ALREADY_SELECTED. */
    private static final String MESSAGE_ERROR_TYPE_ALREADY_SELECTED = "module.workflow.dansmarue.message.error.typeAlreadySelected";

    /** The Constant TASK_NOTIFICATION_SIGNALEMENT_NAME. */
    // PROPERTIES
    private static final String TASK_NOTIFICATION_SIGNALEMENT_NAME = AppPropertiesService
            .getProperty( "workflow.signalement.taskSignalementNotification.name" );

    private static final String ID_TASK_NOTIFICATION_EXCLUDE = AppPropertiesService.getProperty( "workflow.signalement.abonnementMail.idTask.exclude" );

    /** The Constant PARAMETER_ID_UNIT. */
    // PARAMETERS
    private static final String PARAMETER_ID_UNIT = "idUnit";

    /** The Constant PARAMETER_ID_TYPE_SIGNALEMENT. */
    private static final String PARAMETER_ID_TYPE_SIGNALEMENT = "idTypeSignalement";

    /** The Constant PARAMETER_BACK. */
    private static final String PARAMETER_BACK = "back";

    /** The Constant PARAMETER_UNIT_ID_UNIT. */
    private static final String PARAMETER_UNIT_ID_UNIT = "unit.idUnit";

    /** The Constant PARAMETER_NO_UNIT_SELECTED. */
    private static final String PARAMETER_NO_UNIT_SELECTED = "-1";

    /** The Constant PARAMETER_TYPE_ID_TYPE. */
    private static final String PARAMETER_TYPE_ID_TYPE = "typeSignalement";

    /** The Constant JSP_MANAGE_TRACKING_MESSAGE. */
    // JSP
    private static final String JSP_MANAGE_TRACKING_MESSAGE = "jsp/admin/plugins/workflow/modules/signalement/GetMessageTrackingManagement.jsp";

    /** The Constant JSP_MENU_LUTECE. */
    private static final String JSP_MENU_LUTECE = "jsp/admin/AdminMenu.jsp";

    /** The unit service. */
    // SERVICES
    private transient IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    /** The notification signalement task config unit service. */
    private transient NotificationSignalementTaskConfigUnitService _notificationSignalementTaskConfigUnitService = SpringContextService
            .getBean( "signalement.notificationSignalementTaskConfigUnitService" );

    /** The type signalement service. */
    private transient ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /** The signalement view role service. */
    private transient SignalementViewRoleService _signalementViewRoleService = SpringContextService.getBean( "signalement.signalementViewRoleService" );

    /** The signalement workflow service. */
    private transient IWorkflowService _signalementWorkflowService = SpringContextService.getBean( "signalement.workflowService" );

    /**
     * signalement.signalementViewRoleService The tracking message management page
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String getManageTrackingMessage( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        // Get the current user's email
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        String mailCurrentUser = adminUser.getEmail( );
        model.put( MARK_MAIL_USER, mailCurrentUser );

        boolean bIsUserRestrictedByViewRoles = _signalementViewRoleService.checkIsUserRestrictedByViewRoles( adminUser.getUserId( ), request );

        List<Integer> listRestrictedTypeSignalement = _signalementViewRoleService.getUserRestrictedTypeSignalementList( adminUser.getUserId( ) );

        /******* UNIT *******/

        // Get the allowed entities for user (select list)
        List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );
        List<Unit> listAllAllowedUnits = new ArrayList<>( );
        for ( Unit userUnit : listUnits )
        {
            if ( userUnit.getIdUnit( ) != 0 )
            {
                listAllAllowedUnits.add( userUnit );
                getUnitsForSelectList( userUnit.getIdUnit( ), listAllAllowedUnits );
            }
            else
            {
                // If the user is associated with the root unit
                listAllAllowedUnits = _unitService.getAllUnits( false );
                break;
            }
        }
        sortListEntitiesAlphabetical( listAllAllowedUnits );
        ReferenceList listeUnits = ListUtils.toReferenceList( listAllAllowedUnits, PARAMETER_ID_UNIT, "label", "" );
        model.put( MARK_UNITS_LIST, listeUnits );

        // Get the entities already linked to the user email (notification enabled)
        // IMPORTANT : Cf workflow-signalement.properties to see the id task
        List<Unit> listEntitiesUser = getEntitiesLinkedToMailUser( mailCurrentUser );
        sortListEntitiesAlphabetical( listEntitiesUser );
        model.put( MARK_UNITS_LIST_USER, listEntitiesUser );

        /******* UNIT *******/

        /******* TYPES *******/

        // Get the allowed report types for user (select list)
        List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActif( );

        if ( bIsUserRestrictedByViewRoles && !CollectionUtils.isEmpty( listRestrictedTypeSignalement ) )
        {

            Iterator<TypeSignalement> iteratorType = types.iterator( );
            while ( iteratorType.hasNext( ) )
            {
                TypeSignalement type = iteratorType.next( );
                if ( !listRestrictedTypeSignalement.contains( type.getId( ) ) )
                {
                    iteratorType.remove( );
                }
            }

        }

        /******* TYPES *******/

        sortListTypeSignalementAlphabetical( types );
        ReferenceList listeTypes = ListUtils.toReferenceList( types, "id", "formatTypeSignalement", "", false );
        model.put( MARK_TYPE_LIST, listeTypes );

        // Get the report types already linked to the user email (notification enabled)
        // IMPORTANT : Cf workflow-signalement.properties to see the id task
        List<TypeSignalement> listTypesUser = getTypeSignalementLinkedToMailUser( mailCurrentUser );
        sortListTypeSignalementAlphabetical( listTypesUser );
        model.put( MARK_TYPE_LIST_USER, listTypesUser );

        /******* TYPES *******/

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_TRACKING_MESSAGE, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Sort a list of entities (alphabetical).
     *
     * @param listEntitiesUser
     *            the list of entities
     */
    private void sortListEntitiesAlphabetical( List<Unit> listEntitiesUser )
    {

        listEntitiesUser.sort( ( Unit o1, Unit o2 ) -> o1.getLabel( ).compareTo( o2.getLabel( ) ) );
    }

    /**
     * Sort a list of report types (alphabetical).
     *
     * @param listTypesUser
     *            the list of report types
     */
    private void sortListTypeSignalementAlphabetical( List<TypeSignalement> listTypesUser )
    {

        listTypesUser.sort( ( TypeSignalement o1, TypeSignalement o2 ) -> o1.getFormatTypeSignalement( ).compareTo( o2.getFormatTypeSignalement( ) ) );
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
        List<Long> idsTask = _signalementWorkflowService.findIdTaskByTaskKey( TASK_NOTIFICATION_SIGNALEMENT_NAME );

        List<Unit> listEntitiesUser = new ArrayList<>( );

        for ( Long idTask : idsTask )
        {
            List<NotificationSignalementTaskConfigUnit> lstConfigUnit = _notificationSignalementTaskConfigUnitService.findByIdTask( idTask.intValue( ),
                    getPlugin( ) );

            for ( NotificationSignalementTaskConfigUnit configUnit : lstConfigUnit )
            {
                long exist = listEntitiesUser.stream( ).filter( unit -> unit.getIdUnit( ) == configUnit.getUnit( ).getIdUnit( ) ).count( );

                if ( configUnit.getDestinataires( ).contains( mailCurrentUser ) && ( exist < 1 ) )
                {

                    listEntitiesUser.add( _unitService.getUnit( configUnit.getUnit( ).getIdUnit( ), false ) );
                }
            }
        }
        return listEntitiesUser;
    }

    /**
     * Get the report types linked to the user's email.
     *
     * @param mailCurrentUser
     *            the user mail
     * @return the list of report types the list of report types
     */
    private List<TypeSignalement> getTypeSignalementLinkedToMailUser( String mailCurrentUser )
    {
        List<Long> idsTask = _signalementWorkflowService.findIdTaskByTaskKey( TASK_NOTIFICATION_SIGNALEMENT_NAME );
        List<TypeSignalement> listTypesUser = new ArrayList<>( );

        for ( Long idTask : idsTask )
        {

            List<NotificationSignalementTaskConfigUnit> lstConfigUnit = _notificationSignalementTaskConfigUnitService.findByIdTaskWithTypeSignalement( idTask.intValue( ), getPlugin( ) );

            for ( NotificationSignalementTaskConfigUnit configUnit : lstConfigUnit )
            {
                long exist = listTypesUser.stream( ).filter( typeSignalement -> typeSignalement.getId( ).intValue( ) == configUnit.getTypeSignalement( ).getId( ).intValue( ) ).count( );

                if ( configUnit.getDestinataires( ).contains( mailCurrentUser ) && ( exist < 1 ) )
                {
                    if ( ( configUnit.getTypeSignalement( ).getTypeSignalementParent( ) != null ) && ( configUnit.getTypeSignalement( ).getTypeSignalementParent( ).getId( ) > 0 ) )
                    {
                        listTypesUser.add( _typeSignalementService.getTypeSignalementByIdWithParents( configUnit.getTypeSignalement( ).getId( ) ) );
                    } else
                    {
                        //Abonnement type signalement de niveau 1
                        TypeSignalement signalementN1 = new TypeSignalement( );
                        signalementN1.setId( configUnit.getTypeSignalement( ).getId( ) );
                        signalementN1.setLibelle( configUnit.getTypeSignalement( ).getLibelle( ) );
                        listTypesUser.add( signalementN1 );
                    }
                }

            }
        }
        return listTypesUser;
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
            List<NotificationSignalementTaskConfigUnit> listConfigUnit = _notificationSignalementTaskConfigUnitService.findByIdUnit( nIdUnitToDelete,
                    getPlugin( ) );

            // remove the email everywhere (format of destinataires : email1@mail.com;email2@mail.com;email3@mail.com)
            for ( NotificationSignalementTaskConfigUnit configUnit : listConfigUnit )
            {
                String [ ] listRecipient = configUnit.getDestinataires( ).split( ";" );
                List<String> listNewRecipient = new ArrayList<>( );
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
                }
                else
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
     * Delete a report type for a user from message tracking management page.
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doDeleteTypeSignalementMessageTracking( HttpServletRequest request )
    {

        String strIdTypeToDelete = request.getParameter( PARAMETER_ID_TYPE_SIGNALEMENT );
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        String mailCurrentUser = adminUser.getEmail( );

        // 1-split ; 2-remove from the list ; 3-concat ; 4-update in database
        if ( strIdTypeToDelete != null )
        {
            int nIdTypeToDelete = Integer.parseInt( strIdTypeToDelete );

            // get all the config where the email is saved
            List<NotificationSignalementTaskConfigUnit> listConfigType = _notificationSignalementTaskConfigUnitService.findByIdTypeSignalement( nIdTypeToDelete,
                    getPlugin( ) );

            // remove the email everywhere (format of destinataires : email1@mail.com;email2@mail.com;email3@mail.com)
            for ( NotificationSignalementTaskConfigUnit configType : listConfigType )
            {
                String [ ] listRecipient = configType.getDestinataires( ).split( ";" );
                List<String> listNewRecipient = new ArrayList<>( );
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
                    _notificationSignalementTaskConfigUnitService.deleteByTypeSignalement( configType.getIdTask( ), nIdTypeToDelete, getPlugin( ) );
                }
                else
                {
                    // else just update by removing the current user mail
                    configType.setDestinataires( strNewRecipients );
                    _notificationSignalementTaskConfigUnitService.updateWithTypeSignalement( configType, getPlugin( ) );
                }
            }

        }

        return doGoBack( request );
    }

    /**
     * Add a unit for a user from message tracking management page.
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doAddUnitMessageTracking( HttpServletRequest request )
    {
        String url;

        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_BACK ) ) )
        {
            url = AppPathService.getBaseUrl( request ) + JSP_MENU_LUTECE;
        }
        else
        {

            String strIdUnit = request.getParameter( PARAMETER_UNIT_ID_UNIT );

            if ( strIdUnit.equals( PARAMETER_NO_UNIT_SELECTED ) )
            {
                url = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_NO_UNIT_SELECTED, AdminMessage.TYPE_STOP );
            }
            else
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
                }
                else
                {
                    // get all the config where the email must be added
                    List<NotificationSignalementTaskConfigUnit> listConfigUnit = _notificationSignalementTaskConfigUnitService.findByIdUnit( nIdUnit,
                            getPlugin( ) );

                    if ( listConfigUnit.isEmpty( ) )
                    {
                        List<Long> idsTask = _signalementWorkflowService.findIdTaskByTaskKey( TASK_NOTIFICATION_SIGNALEMENT_NAME );
                        List<Long> idTaskExclude = Arrays.asList( ID_TASK_NOTIFICATION_EXCLUDE.split( "," ) ).stream( ).map( str -> Long.valueOf( str ) )
                                .collect( Collectors.toList( ) );
                        idsTask.removeAll( idTaskExclude );
                        for ( Long idTask : idsTask )
                        {
                            // create new line(s) in database
                            NotificationSignalementTaskConfigUnit configUnit = new NotificationSignalementTaskConfigUnit( );
                            configUnit.setIdTask( idTask.intValue( ) );
                            configUnit.setDestinataires( mailCurrentUser );
                            configUnit.setUnit( _unitService.getUnit( nIdUnit, true ) );
                            _notificationSignalementTaskConfigUnitService.insert( configUnit, getPlugin( ) );
                        }
                    }
                    else
                    {
                        // add the email in line that already exists (format of destinataires : email1@mail.com;email2@mail.com;email3@mail.com)
                        for ( NotificationSignalementTaskConfigUnit configUnit : listConfigUnit )
                        {
                            String [ ] listRecipient = configUnit.getDestinataires( ).split( ";" );
                            List<String> listNewRecipient = new ArrayList<>( );

                            Collections.addAll( listNewRecipient, listRecipient );

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
     * Add a report type for a user from message tracking management page.
     *
     * @param request
     *            the HttpServletRequest
     * @return the url return
     */
    public String doAddTypeSignalementMessageTracking( HttpServletRequest request )
    {
        String url;

        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_BACK ) ) )
        {
            url = AppPathService.getBaseUrl( request ) + JSP_MENU_LUTECE;
        }
        else
        {

            String strIdTypeSignalement = request.getParameter( PARAMETER_TYPE_ID_TYPE );

            if ( StringUtils.EMPTY.equals( strIdTypeSignalement ) )
            {
                url = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_NO_TYPE_SELECTED, AdminMessage.TYPE_STOP );
            }
            else
            {

                // if the selected unit is already in the list -> alert
                int nIdTypeSignalement = Integer.parseInt( strIdTypeSignalement );
                AdminUser adminUser = AdminUserService.getAdminUser( request );
                String mailCurrentUser = adminUser.getEmail( );
                boolean alreadyInList = false;

                List<TypeSignalement> listTypes = getTypeSignalementLinkedToMailUser( mailCurrentUser );

                for ( TypeSignalement type : listTypes )
                {
                    if ( type.getId( ) == nIdTypeSignalement )
                    {
                        alreadyInList = true;
                    }
                }

                if ( alreadyInList )
                {
                    url = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_TYPE_ALREADY_SELECTED, AdminMessage.TYPE_STOP );
                }
                else
                {
                    // get all the config where the email must be added
                    List<NotificationSignalementTaskConfigUnit> listConfigType = _notificationSignalementTaskConfigUnitService
                            .findByIdTypeSignalement( nIdTypeSignalement, getPlugin( ) );

                    if ( listConfigType.isEmpty( ) )
                    {
                        // create new line(s) in database
                        List<Long> idsTask = _signalementWorkflowService.findIdTaskByTaskKey( TASK_NOTIFICATION_SIGNALEMENT_NAME );
                        List<Long> idTaskExclude = Arrays.asList( ID_TASK_NOTIFICATION_EXCLUDE.split( "," ) ).stream( ).map( str -> Long.valueOf( str ) )
                                .collect( Collectors.toList( ) );
                        idsTask.removeAll( idTaskExclude );

                        for ( Long idTask : idsTask )
                        {
                            NotificationSignalementTaskConfigUnit configType = new NotificationSignalementTaskConfigUnit( );
                            configType.setIdTask( idTask.intValue( ) );
                            configType.setDestinataires( mailCurrentUser );
                            configType.setTypeSignalement( _typeSignalementService.getTypeSignalementByIdWithParentsWithoutUnit( nIdTypeSignalement ) );
                            _notificationSignalementTaskConfigUnitService.insertWithTypeSignalement( configType, getPlugin( ) );
                        }
                    }
                    else
                    {
                        // add the email in line that already exists (format of destinataires : email1@mail.com;email2@mail.com;email3@mail.com)
                        for ( NotificationSignalementTaskConfigUnit configType : listConfigType )
                        {
                            String [ ] listRecipient = configType.getDestinataires( ).split( ";" );
                            List<String> listNewRecipient = new ArrayList<>( );

                            Collections.addAll( listNewRecipient, listRecipient );
                            listNewRecipient.add( mailCurrentUser );

                            String strNewRecipients = StringUtils.join( listNewRecipient, ";" );

                            configType.setDestinataires( strNewRecipients );
                            _notificationSignalementTaskConfigUnitService.updateWithTypeSignalement( configType, getPlugin( ) );

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

        return StringUtils.isNotBlank( strJspBack ) ? ( AppPathService.getBaseUrl( request ) + strJspBack )
                : ( AppPathService.getBaseUrl( request ) + JSP_MANAGE_TRACKING_MESSAGE );
    }

    /**
     * Get all the hierarchy of units allowed for current user.
     *
     * @param nIdUnitUser
     *            the id of the user unit
     * @param listAllUnitUser
     *            the final list
     * @return the units for select list
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
