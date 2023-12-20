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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.web;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.IListUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.BaliseFreemarkerDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.NotificationSignalementTaskConfigService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.NotificationSignalementTaskConfigUnitService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The notification component.
 */
public class NotificationComponent extends AbstractTaskComponent
{

    /** The Constant MARK_CONFIG_DTO_UNIT. */
    // MARKERS
    private static final String MARK_CONFIG_DTO_UNIT = "configDTOUnit";

    /** The Constant MARK_CONFIG_DTO_TYPE. */
    private static final String MARK_CONFIG_DTO_TYPE = "configDTOType";

    /** The Constant MARK_WEBAPP_URL. */
    private static final String MARK_WEBAPP_URL = "webapp_url";

    /** The Constant MARK_LOCALE. */
    private static final String MARK_LOCALE = "locale";

    /** The Constant MARK_LISTE_UNITS. */
    private static final String MARK_LISTE_UNITS = "liste_units";

    /** The Constant MARK_BALISES. */
    private static final String MARK_BALISES = "balises";

    /** The Constant MARK_NUMERO. */
    private static final String MARK_NUMERO = "numero";

    /** The Constant MARK_TYPE. */
    private static final String MARK_TYPE = "type";

    /** The Constant MARK_ADRESSE. */
    private static final String MARK_ADRESSE = "adresse";

    /** The Constant MARK_PRIORITE. */
    private static final String MARK_PRIORITE = "priorite";

    /** The Constant MARK_COMMENTAIRE. */
    private static final String MARK_COMMENTAIRE = "commentaire";

    /** The Constant MARK_PRECISION. */
    private static final String MARK_PRECISION = "precision";

    /** The Constant MARK_LIEN_CONSULT. */
    private static final String MARK_LIEN_CONSULT = "lien";

    /** The Constant MARK_LIEN_SIGNALEMENT_WS. */
    private static final String MARK_LIEN_SIGNALEMENT_WS = "wsSignalement";

    /** The Constant MARK_DATE_ENVOI. */
    private static final String MARK_DATE_ENVOI = "dateEnvoi";

    /** The Constant MARK_HEURE_ENVOI. */
    private static final String MARK_HEURE_ENVOI = "heureEnvoi";

    /** The Constant MARK_EMAIL_USAGER. */
    private static final String MARK_EMAIL_USAGER = "emailUsager";

    /** The Constant MARK_ALIAS_ANOMALIE. */
    private static final String MARK_ALIAS_ANOMALIE = "alias_anomalie";

    /** The Constant MARK_ID_ANOMALIE. */
    private static final String MARK_ID_ANOMALIE = "id_anomalie";

    /** The Constant MARK_TYPE_LIST. */
    private static final String MARK_TYPE_LIST = "type_list";

    /** The Constant PARAMETER_ID_TASK. */
    // PARAMETERS
    private static final String PARAMETER_ID_TASK = "id_task";

    /** The Constant PARAMETER_ADD_UNIT. */
    private static final String PARAMETER_ADD_UNIT = "add_unit";

    /** The Constant PARAMETER_ADD_TYPE. */
    private static final String PARAMETER_ADD_TYPE = "add_type";

    /** The Constant MESSAGE_EXCEPTION_OCCURED. */
    // MESSAGES
    private static final String MESSAGE_EXCEPTION_OCCURED = "module.workflow.dansmarue.task_notification_config.message.exception";

    /** The Constant MESSAGE_MANDATORY_FIELD. */
    private static final String MESSAGE_MANDATORY_FIELD = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";

    /** The Constant ERROR_SENDER. */
    private static final String ERROR_SENDER = "module.workflow.dansmarue.task_notification_config.error.sender";

    /** The Constant ERROR_SUBJECT. */
    private static final String ERROR_SUBJECT = "module.workflow.dansmarue.task_notification_config.error.subject";

    /** The Constant ERROR_MESSAGE. */
    private static final String ERROR_MESSAGE = "module.workflow.dansmarue.task_notification_config.error.message";

    /** The Constant ERROR_UNIT. */
    private static final String ERROR_UNIT = "module.workflow.dansmarue.task_notification_config.error.entite";

    /** The Constant ERROR_TYPE. */
    private static final String ERROR_TYPE = "module.workflow.dansmarue.task_notification_config.error.typeSignalement";

    /** The Constant ERROR_DESTINATAIRES. */
    private static final String ERROR_DESTINATAIRES = "module.workflow.dansmarue.task_notification_config.error.destinataires";

    /** The Constant MESSAGE_ERROR_UNIT_ALLREADY_EXISTS. */
    private static final String MESSAGE_ERROR_UNIT_ALLREADY_EXISTS = "module.workflow.dansmarue.task_notification_config.error.entite.exists";

    /** The Constant MESSAGE_ERROR_TYPE_ALLREADY_EXISTS. */
    private static final String MESSAGE_ERROR_TYPE_ALLREADY_EXISTS = "module.workflow.dansmarue.task_notification_config.error.typeSignalement.exists";

    /** The Constant MESSAGE_ERROR_RECIPIENT_FORMAT. */
    private static final String MESSAGE_ERROR_RECIPIENT_FORMAT = "module.workflow.dansmarue.task_notification_config.error.recipient.format";

    /** The Constant TEMPLATE_TASK_NOTIFICATION_SIGNALEMENT_CONFIG. */
    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFICATION_SIGNALEMENT_CONFIG = "admin/plugins/workflow/modules/signalement/task_notification_signalement_config.html";

    /** The Constant RECIPIENT_SEPARATOR. */
    // CONSTANTS
    private static final String RECIPIENT_SEPARATOR = ";";

    /** The Constant NOTHING_SELECT. */
    private static final String NOTHING_SELECT = "-1";

    /** The Constant JSP_MODIFY_TASK. */
    // JSP
    private static final String JSP_MODIFY_TASK = "jsp/admin/plugins/workflow/ModifyTask.jsp";

    /** The Constant LABEL_DATE_ENVOI. */
    // LABELS
    private static final String LABEL_DATE_ENVOI = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.dateenvoi.label}";

    /** The Constant LABEL_HEURE_ENVOI. */
    private static final String LABEL_HEURE_ENVOI = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.heureenvoi.label}";

    /** The Constant LABEL_EMAIL_USAGER. */
    private static final String LABEL_EMAIL_USAGER = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.emailusager.label}";

    /** The unit service. */
    // SERVICES
    @Inject
    @Named( UnitService.BEAN_UNIT_SERVICE )
    private IUnitService _unitService;

    /** The notification signalement task config service. */
    @Inject
    @Named( "signalement.notificationSignalementTaskConfigService" )
    private NotificationSignalementTaskConfigService _notificationSignalementTaskConfigService;

    /** The signalement service. */
    @Inject
    @Named( "signalementService" )
    private ISignalementService _signalementService;

    /** The resource history service. */
    @Inject
    @Named( "workflow.resourceHistoryService" )
    private IResourceHistoryService _resourceHistoryService;

    /** The type signalement service. */
    @Inject
    @Named( "typeSignalementService" )
    private ITypeSignalementService _typeSignalementService;

    /** The notification signalement task config unit service. */
    @Inject
    @Named( "signalement.notificationSignalementTaskConfigUnitService" )
    private NotificationSignalementTaskConfigUnitService _notificationSignalementTaskConfigUnitService;

    /** The date utils. */
    // UTILS
    @Inject
    @Named( "signalement.listUtils" )
    private IListUtils _listUtils;

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
        return null;
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
        Map<String, Object> model = new HashMap<>( );

        NotificationSignalementTaskConfigDTO configDTOUnit = _notificationSignalementTaskConfigService.findByPrimaryKey( task.getId( ) );

        NotificationSignalementTaskConfigDTO configDTOType = _notificationSignalementTaskConfigService.findByPrimaryKeyWithTypeSignalement( task.getId( ) );

        model.put( MARK_CONFIG_DTO_UNIT, configDTOUnit );
        model.put( MARK_CONFIG_DTO_TYPE, configDTOType );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, request.getLocale( ) );

        ReferenceList listeUnits = _listUtils.toReferenceList( _unitService.getAllUnits( false ), "idUnit", "label", "" );
        model.put( MARK_LISTE_UNITS, listeUnits );

        List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActif( );
        ReferenceList listeTypes = _listUtils.toReferenceList( types, "id", "formatTypeSignalement", "", false );
        model.put( MARK_TYPE_LIST, listeTypes );

        // Liste des balises freemaker pouvant être utilisées (à ajouter dans emailModel dans processAction))
        List<BaliseFreemarkerDTO> balises = new ArrayList<>( );
        BaliseFreemarkerDTO dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Id de l'anomalie" );
        dto.setValeur( MARK_ID_ANOMALIE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Numéro de l'anomalie" );
        dto.setValeur( MARK_NUMERO );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Type d'anomalie" );
        dto.setValeur( MARK_TYPE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Alias de l'anomalie" );
        dto.setValeur( MARK_ALIAS_ANOMALIE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Adresse de l'anomalie" );
        dto.setValeur( MARK_ADRESSE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Précision de la localisation" );
        dto.setValeur( MARK_PRECISION );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Priorité" );
        dto.setValeur( MARK_PRIORITE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Commentaire" );
        dto.setValeur( MARK_COMMENTAIRE );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Lien de consultation du message" );
        dto.setValeur( MARK_LIEN_CONSULT );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( "Lien vers le formulaire de gestion d'une anomalie par un prestataire" );
        dto.setValeur( MARK_LIEN_SIGNALEMENT_WS );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_DATE_ENVOI );
        dto.setValeur( MARK_DATE_ENVOI );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_HEURE_ENVOI );
        dto.setValeur( MARK_HEURE_ENVOI );
        balises.add( dto );
        dto = new BaliseFreemarkerDTO( );
        dto.setNom( LABEL_EMAIL_USAGER );
        dto.setValeur( MARK_EMAIL_USAGER );
        balises.add( dto );
        model.put( MARK_BALISES, balises );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFICATION_SIGNALEMENT_CONFIG, locale, model );
        return template.getHtml( );
    }

    /**
     * Do save config.
     *
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param task
     *            the task
     * @return the string
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        if ( ( request.getParameter( PARAMETER_ADD_UNIT ) != null ) || ( request.getParameter( PARAMETER_ADD_TYPE ) != null ) )
        {
            // Bouton Ajouter : ajout d'une unit de notification
            NotificationSignalementTaskConfigUnit configUnit = new NotificationSignalementTaskConfigUnit( );
            try
            {
                Unit unit = new Unit( );
                TypeSignalement type = new TypeSignalement( );
                unit.setIdUnit( Integer.parseInt( request.getParameter( "unit.idUnit" ) ) );
                type.setId( Integer.parseInt( request.getParameter( "typeSignalement" ) ) );
                configUnit.setIdTask( task.getId( ) );
                if ( unit.getIdUnit( ) != -1 )
                {
                    configUnit.setUnit( unit );
                    configUnit.setDestinataires( request.getParameter( "destinatairesUnit" ) );
                }
                if ( type.getId( ) != -1 )
                {
                    configUnit.setTypeSignalement( type );
                    configUnit.setDestinataires( request.getParameter( "destinatairesType" ) );
                }

            }
            catch( Exception e )
            {
                AppLogService.error( e.getMessage( ), e );
                Object [ ] tabError = {
                        e.getMessage( )
                };
                return AdminMessageService.getMessageUrl( request, MESSAGE_EXCEPTION_OCCURED, tabError, AdminMessage.TYPE_STOP );
            }

            // Gestion des erreurs pour entite
            if ( request.getParameter( PARAMETER_ADD_UNIT ) != null )
            {
                if ( NOTHING_SELECT.equals( request.getParameter( "unit.idUnit" ) ) )
                {
                    strError = ERROR_UNIT;
                }
                else
                    if ( StringUtils.isBlank( configUnit.getDestinataires( ) ) )
                    {
                        strError = ERROR_DESTINATAIRES;
                    }
                if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
                {
                    Object [ ] tabRequiredFields = {
                            I18nService.getLocalizedString( strError, locale )
                    };

                    return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
                }
                else
                    if ( _notificationSignalementTaskConfigService.findUnitByPrimaryKey( task.getId( ), configUnit.getUnit( ).getIdUnit( ) ) != null )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_UNIT_ALLREADY_EXISTS, AdminMessage.TYPE_STOP );
                    }
                    else
                    {
                        // Check format fields
                        EmailValidator emailValidator = new EmailValidator( );
                        String [ ] listRecipient = configUnit.getDestinataires( ).split( RECIPIENT_SEPARATOR );
                        for ( String recipient : listRecipient )
                        {
                            if ( !emailValidator.isValid( recipient.trim( ), null ) )
                            {
                                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_RECIPIENT_FORMAT, AdminMessage.TYPE_STOP );
                            }
                        }
                    }

                List<NotificationSignalementTaskConfigUnit> listConfigUnit = _notificationSignalementTaskConfigUnitService
                        .findByIdUnitAndIdTask( configUnit.getUnit( ).getIdUnit( ), task.getId( ), null );

                if ( listConfigUnit.isEmpty( ) )
                {
                    int nIdTask = task.getId( );
                    NotificationSignalementTaskConfigUnit config = new NotificationSignalementTaskConfigUnit( );
                    config.setIdTask( nIdTask );
                    config.setDestinataires( configUnit.getDestinataires( ) );
                    config.setUnit( _unitService.getUnit( configUnit.getUnit( ).getIdUnit( ), true ) );
                    _notificationSignalementTaskConfigUnitService.insert( config, null );
                }
                else
                {
                    // add the email in line that already exists (format of destinataires : email1@mail.com;email2@mail.com;email3@mail.com)
                    for ( NotificationSignalementTaskConfigUnit config : listConfigUnit )
                    {
                        String [ ] listRecipient = config.getDestinataires( ).split( ";" );
                        String [ ] listArrayNewRecipient = configUnit.getDestinataires( ).split( ";" );
                        List<String> listNewRecipient = new ArrayList<>( );
                        Collections.addAll( listNewRecipient, listRecipient );

                        for ( String recipient : listArrayNewRecipient )
                        {
                            if ( !listNewRecipient.contains( recipient ) )
                            {
                                listNewRecipient.add( recipient );
                            }
                        }

                        String strNewRecipients = StringUtils.join( listNewRecipient, ";" );

                        config.setDestinataires( strNewRecipients );
                        _notificationSignalementTaskConfigUnitService.update( config, null );
                    }
                }
            }

            // Gestion des erreurs pour type signalement
            if ( request.getParameter( PARAMETER_ADD_TYPE ) != null )
            {
                if ( NOTHING_SELECT.equals( request.getParameter( "typeSignalement" ) ) )
                {
                    strError = ERROR_TYPE;
                }
                else
                    if ( StringUtils.isBlank( configUnit.getDestinataires( ) ) )
                    {
                        strError = ERROR_DESTINATAIRES;
                    }
                if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
                {
                    Object [ ] tabRequiredFields = {
                            I18nService.getLocalizedString( strError, locale )
                    };

                    return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
                }
                else
                    if ( _notificationSignalementTaskConfigService.findByIdTypeSignalement( task.getId( ), configUnit.getUnit( ).getIdUnit( ) ) != null )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_TYPE_ALLREADY_EXISTS, AdminMessage.TYPE_STOP );
                    }
                    else
                    {
                        // Check format fields
                        EmailValidator emailValidator = new EmailValidator( );
                        String [ ] listRecipient = configUnit.getDestinataires( ).split( RECIPIENT_SEPARATOR );
                        for ( String recipient : listRecipient )
                        {
                            if ( !emailValidator.isValid( recipient.trim( ), null ) )
                            {
                                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_RECIPIENT_FORMAT, AdminMessage.TYPE_STOP );
                            }
                        }
                    }

                List<NotificationSignalementTaskConfigUnit> listConfigType = _notificationSignalementTaskConfigUnitService
                        .findByIdTypeSignalement( configUnit.getTypeSignalement( ).getId( ), null );

                if ( listConfigType.isEmpty( ) )
                {
                    int nIdTask = task.getId( );
                    NotificationSignalementTaskConfigUnit configType = new NotificationSignalementTaskConfigUnit( );
                    configType.setIdTask( nIdTask );
                    configType.setDestinataires( configUnit.getDestinataires( ) );
                    configType.setTypeSignalement(
                            _typeSignalementService.getTypeSignalementByIdWithParentsWithoutUnit( configUnit.getTypeSignalement( ).getId( ) ) );
                    _notificationSignalementTaskConfigUnitService.insertWithTypeSignalement( configType, null );
                }
                else
                {
                    // add the email in line that already exists (format of destinataires : email1@mail.com;email2@mail.com;email3@mail.com)
                    for ( NotificationSignalementTaskConfigUnit configType : listConfigType )
                    {
                        String [ ] listRecipient = configType.getDestinataires( ).split( ";" );
                        String [ ] listArrayNewRecipient = configUnit.getDestinataires( ).split( ";" );
                        List<String> listNewRecipient = new ArrayList<>( );
                        Collections.addAll( listNewRecipient, listRecipient );

                        for ( String recipient : listArrayNewRecipient )
                        {
                            if ( !listNewRecipient.contains( recipient ) )
                            {
                                listNewRecipient.add( recipient );
                            }
                        }

                        String strNewRecipients = StringUtils.join( listNewRecipient, ";" );

                        configType.setDestinataires( strNewRecipients );
                        _notificationSignalementTaskConfigUnitService.updateWithTypeSignalement( configType, null );

                    }
                }
            }

            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
            url.addParameter( PARAMETER_ID_TASK, task.getId( ) );

            return url.getUrl( );
        }
        else
        {
            // Sauvegarde de la config
            NotificationSignalementTaskConfigDTO configDTO = new NotificationSignalementTaskConfigDTO( );

            try
            {
                BeanUtils.populate( configDTO, request.getParameterMap( ) );
                configDTO.setIdTask( task.getId( ) );
            }
            catch( Exception e )
            {
                AppLogService.error( e.getMessage( ), e );
                Object [ ] tabError = {
                        e.getMessage( )
                };
                return AdminMessageService.getMessageUrl( request, e.getMessage( ), tabError, AdminMessage.TYPE_STOP );
            }

            if ( configDTO.getSender( ).equals( StringUtils.EMPTY ) )
            {
                strError = ERROR_SENDER;
            }
            else
                if ( configDTO.getSubject( ).equals( StringUtils.EMPTY ) )
                {
                    strError = ERROR_SUBJECT;
                }
                else
                    if ( configDTO.getMessage( ).equals( StringUtils.EMPTY ) )
                    {
                        strError = ERROR_MESSAGE;
                    }
            if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
            {
                Object [ ] tabRequiredFields = {
                        I18nService.getLocalizedString( strError, locale )
                };

                return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            if ( _notificationSignalementTaskConfigService.findByPrimaryKey( task.getId( ) ).getIdTask( ) == 0 )
            {
                _notificationSignalementTaskConfigService.insert( configDTO );
            }
            else
            {
                _notificationSignalementTaskConfigService.update( configDTO );
            }

            return null;
        }
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
        return null;
    }

}
