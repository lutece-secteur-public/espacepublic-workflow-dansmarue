/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto.BaliseFreemarkerDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigDTO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.business.NotificationSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notification.service.NotificationSignalementTaskConfigService;
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

/**
 * The notification component
 */
public class NotificationComponent extends AbstractTaskComponent
{
    // MARKERS
    private static final String                      MARK_CONFIG_DTO                               = "configDTO";
    private static final String                      MARK_WEBAPP_URL                               = "webapp_url";
    private static final String                      MARK_LOCALE                                   = "locale";
    private static final String                      MARK_LISTE_UNITS                              = "liste_units";
    private static final String                      MARK_BALISES                                  = "balises";

    private static final String                      MARK_NUMERO                                   = "numero";
    private static final String                      MARK_TYPE                                     = "type";
    private static final String                      MARK_ADRESSE                                  = "adresse";
    private static final String                      MARK_PRIORITE                                 = "priorite";
    private static final String                      MARK_COMMENTAIRE                              = "commentaire";
    private static final String                      MARK_PRECISION                                = "precision";
    private static final String                      MARK_LIEN_CONSULT                             = "lien";
    private static final String                      MARK_LIEN_SIGNALEMENT_WS                      = "wsSignalement";

    private static final String                      MARK_DATE_ENVOI                               = "dateEnvoi";
    private static final String                      MARK_HEURE_ENVOI                              = "heureEnvoi";
    private static final String                      MARK_EMAIL_USAGER                             = "emailUsager";

    private static final String                      MARK_ALIAS_ANOMALIE                           = "alias_anomalie";

    private static final String                      MARK_ID_ANOMALIE                              = "id_anomalie";

    // PARAMETERS
    private static final String                      PARAMETER_ID_TASK                             = "id_task";
    private static final String                      PARAMETER_ADD_UNIT                            = "add_unit";

    // MESSAGES
    private static final String                      MESSAGE_EXCEPTION_OCCURED                     = "module.workflow.dansmarue.task_notification_config.message.exception";
    private static final String                      MESSAGE_MANDATORY_FIELD                       = "module.workflow.dansmarue.task_notification_config.message.mandatory.field";
    private static final String                      ERROR_SENDER                                  = "module.workflow.dansmarue.task_notification_config.error.sender";
    private static final String                      ERROR_SUBJECT                                 = "module.workflow.dansmarue.task_notification_config.error.subject";
    private static final String                      ERROR_MESSAGE                                 = "module.workflow.dansmarue.task_notification_config.error.message";
    private static final String                      ERROR_UNIT                                    = "module.workflow.dansmarue.task_notification_config.error.entite";
    private static final String                      ERROR_DESTINATAIRES                           = "module.workflow.dansmarue.task_notification_config.error.destinataires";
    private static final String                      MESSAGE_ERROR_UNIT_ALLREADY_EXISTS            = "module.workflow.dansmarue.task_notification_config.error.entite.exists";
    private static final String                      MESSAGE_ERROR_RECIPIENT_FORMAT                = "module.workflow.dansmarue.task_notification_config.error.recipient.format";

    // TEMPLATES
    private static final String                      TEMPLATE_TASK_NOTIFICATION_SIGNALEMENT_CONFIG = "admin/plugins/workflow/modules/signalement/task_notification_signalement_config.html";

    // CONSTANTS
    private static final String                      RECIPIENT_SEPARATOR                           = ";";

    // JSP
    private static final String                      JSP_MODIFY_TASK                               = "jsp/admin/plugins/workflow/ModifyTask.jsp";

    // LABELS
    private static final String                      LABEL_DATE_ENVOI                              = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.dateenvoi.label}";
    private static final String                      LABEL_HEURE_ENVOI                             = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.heureenvoi.label}";
    private static final String                      LABEL_EMAIL_USAGER                            = "#i18n{module.workflow.dansmarue.task_notification_config.freemarker.emailusager.label}";

    // SERVICES
    @Inject
    @Named( IUnitService.BEAN_UNIT_SERVICE )
    private IUnitService                             _unitService;

    @Inject
    @Named( "signalement.notificationSignalementTaskConfigService" )
    private NotificationSignalementTaskConfigService _notificationSignalementTaskConfigService;

    @Inject
    @Named( "signalementService" )
    private ISignalementService                      _signalementService;
    @Inject
    @Named( "workflow.resourceHistoryService" )
    private IResourceHistoryService                  _resourceHistoryService;

    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        NotificationSignalementTaskConfigDTO configDTO = _notificationSignalementTaskConfigService.findByPrimaryKey( task.getId( ) );

        model.put( MARK_CONFIG_DTO, configDTO );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, request.getLocale( ) );

        ReferenceList listeUnits = ListUtils.toReferenceList( _unitService.getAllUnits( false ), "idUnit", "label", "" );
        model.put( MARK_LISTE_UNITS, listeUnits );

        // Liste des balises freemaker pouvant être utilisées (à ajouter dans emailModel dans processAction))
        List<BaliseFreemarkerDTO> balises = new ArrayList<BaliseFreemarkerDTO>( );
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

    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = WorkflowUtils.EMPTY_STRING;

        if ( request.getParameter( PARAMETER_ADD_UNIT ) != null )
        {
            // Bouton Ajouter : ajout d'une unit de notification
            NotificationSignalementTaskConfigUnit configUnit = new NotificationSignalementTaskConfigUnit( );
            try
            {
                BeanUtils.populate( configUnit, request.getParameterMap( ) );
                configUnit.setIdTask( task.getId( ) );
            } catch ( Exception e )
            {
                AppLogService.error( e.getMessage( ), e );
                Object[] tabError = { e.getMessage( ) };
                return AdminMessageService.getMessageUrl( request, MESSAGE_EXCEPTION_OCCURED, tabError, AdminMessage.TYPE_STOP );
            }

            // Gestion des erreurs
            if ( configUnit.getUnit( ).getIdUnit( ) == WorkflowUtils.CONSTANT_ID_NULL )
            {
                strError = ERROR_UNIT;
            } else if ( configUnit.getDestinataires( ).equals( StringUtils.EMPTY ) )
            {
                strError = ERROR_DESTINATAIRES;
            }
            if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
            } else if ( _notificationSignalementTaskConfigService.findUnitByPrimaryKey( task.getId( ), configUnit.getUnit( ).getIdUnit( ) ) != null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_UNIT_ALLREADY_EXISTS, AdminMessage.TYPE_STOP );
            } else
            {
                // Check format fields
                EmailValidator emailValidator = new EmailValidator( );
                String[] listRecipient = configUnit.getDestinataires( ).split( RECIPIENT_SEPARATOR );
                for ( String recipient : listRecipient )
                {
                    if ( !emailValidator.isValid( recipient.trim( ), null ) )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_RECIPIENT_FORMAT, AdminMessage.TYPE_STOP );
                    }
                }
            }

            // Insertion de l'unit
            _notificationSignalementTaskConfigService.insertUnit( configUnit );

            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MODIFY_TASK );
            url.addParameter( PARAMETER_ID_TASK, task.getId( ) );

            return url.getUrl( );
        } else
        {
            // Sauvegarde de la config
            NotificationSignalementTaskConfigDTO configDTO = new NotificationSignalementTaskConfigDTO( );

            try
            {
                BeanUtils.populate( configDTO, request.getParameterMap( ) );
                configDTO.setIdTask( task.getId( ) );
            }

            catch ( Exception e )
            {
                AppLogService.error( e.getMessage( ), e );
                Object[] tabError = { e.getMessage( ) };
                return AdminMessageService.getMessageUrl( request, e.getMessage( ), tabError, AdminMessage.TYPE_STOP );
            }

            if ( configDTO.getSender( ).equals( StringUtils.EMPTY ) )
            {
                strError = ERROR_SENDER;
            } else if ( configDTO.getSubject( ).equals( StringUtils.EMPTY ) )
            {
                strError = ERROR_SUBJECT;
            } else if ( configDTO.getMessage( ).equals( StringUtils.EMPTY ) )
            {
                strError = ERROR_MESSAGE;
            }
            if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            if ( _notificationSignalementTaskConfigService.findByPrimaryKey( task.getId( ) ).getIdTask( ) == 0 )
            {
                _notificationSignalementTaskConfigService.insert( configDTO );
            } else
            {
                _notificationSignalementTaskConfigService.update( configDTO );
            }

            return null;
        }
    }

    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    @Override
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

}
