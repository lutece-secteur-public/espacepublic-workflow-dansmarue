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

package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.TechnicalException;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementWebService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.service.TaskUtils;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.AbstractSignalementTask;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigUnit;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceSignalementTaskConfigUnitDAO;
import fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business.WebServiceValue;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.state.IStateService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import net.sf.json.JSONObject;

/**
 * WebServiceSignalementTask class.
 */

public class WebServiceSignalementTask extends AbstractSignalementTask

{

    // CONSTANTS

    /** The Constant CONSTANT_STATE_NULL. */
    public static final int CONSTANT_STATE_NULL = 0;

    /** The Constant ID_STATE_NOUVEAU. */
    private static final String ID_STATE_NOUVEAU = "signalement.idStateNouveau";

    /** The Constant ID_TASK_SIGNALEMENT_WS_DEFAULT_CONFIG. */
    private static final String ID_TASK_SIGNALEMENT_WS_DEFAULT_CONFIG = "signalement.idTaskSignalementWebServiceDefaultConfig";

    /** The Constant ID_STATE_ECHEC_WS. */
    private static final String ID_STATE_ECHEC_WS = "signalement.idStateEchecEnvoiWS";

    // MESSAGES

    /** The Constant MESSAGE_PROVIDER_WEBSERVICE_SUCCESS. */
    private static final String MESSAGE_PROVIDER_WEBSERVICE_SUCCESS = "module.workflow.dansmarue.task_webservice_config.history.provider_webservice_success";

    /** The Constant MESSAGE_WEBSERVICE_DONE_SUCCESS. */
    private static final String MESSAGE_WEBSERVICE_DONE_SUCCESS = "module.workflow.dansmarue.task_webservice_config.history.webservice_done_success";

    /** The Constant MESSAGE_PROVIDER_WEBSERVICE_FAILURE. */
    private static final String MESSAGE_PROVIDER_WEBSERVICE_FAILURE = "module.workflow.dansmarue.task_webservice_config.history.provider_webservice_failure";

    /** The Constant MESSAGE_PROVIDER_WEBSERVICE_FAILURE_WRONG_RESPONSE. */
    private static final String MESSAGE_PROVIDER_WEBSERVICE_FAILURE_WRONG_RESPONSE = "module.workflow.dansmarue.task_webservice_config.history.provider_webservice_failure_wrong_response";

    /** The Constant MESSAGE_WEBSERVICE_FAILURE_WRONG_ADDRESS. */
    private static final String MESSAGE_WEBSERVICE_FAILURE_WRONG_ADDRESS = "module.workflow.dansmarue.task_webservice_config.history.webservice_failure_wrong_address";

    /** The Constant MESSAGE_PROVIDER_WEBSERVICE_NO_CONFIG_SAVED. */
    private static final String MESSAGE_PROVIDER_WEBSERVICE_NO_CONFIG_SAVED = "module.workflow.dansmarue.task_webservice_config.history.no_config_saved";

    /** The Constant MESSAGE_PROVIDER_WITHOUT_WEBSERVICE. */
    private static final String MESSAGE_PROVIDER_WITHOUT_WEBSERVICE = "module.workflow.dansmarue.task_webservice_config.history.without_webservice";

    /** The Constant PARAMETER_WEBSERVICE_SERVICE_FAIT_PRESTA. */
    // PARAMETERS
    private static final String PARAMETER_WEBSERVICE_SERVICE_FAIT_PRESTA = "webservice_service_fait_presta";

    // SERVICES

    /** The signalement web service. */
    private ISignalementWebService _signalementWebService = SpringContextService

            .getBean( "signalement.signalementWebService" );

    /** The signalement service. */
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The webservice value service. */
    private WebServiceValueService _webserviceValueService = SpringContextService

            .getBean( "signalement.webserviceValueService" );

    /** The unit service. */
    private IUnitService _unitService = SpringContextService.getBean( "unittree.unitService" );

    // DAO

    /** The config DAO. */
    private WebServiceSignalementTaskConfigDAO _configDAO = SpringContextService

            .getBean( "signalement.webserviceSignalementTaskConfigDAO" );

    /** The configunit DAO. */
    private WebServiceSignalementTaskConfigUnitDAO _configunitDAO = SpringContextService

            .getBean( "signalement.webserviceSignalementTaskConfigUnitDAO" );

    /** The state service. */
    private IStateService _stateService = SpringContextService.getBean( "workflow.stateService" );

    /** The Constant JSON_TAG_ANSWER. */
    // Constant
    private static final String JSON_TAG_ANSWER = "answer";

    /** The Constant JSON_TAG_ERROR. */
    private static final String JSON_TAG_ERROR = "error";

    /**
     * Process task.
     *
     * @param nIdResourceHistory
     *            the n id resource history
     * @param request
     *            the request
     * @param locale
     *            the locale
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )

    {
        // Si on vient du Webservice Service fait, ne fait rien

        if ( ( request != null ) && ( request.getSession( ).getAttribute( PARAMETER_WEBSERVICE_SERVICE_FAIT_PRESTA ) != null )
                && (boolean) request.getSession( ).getAttribute( PARAMETER_WEBSERVICE_SERVICE_FAIT_PRESTA ) )
        {
            request.getSession( ).removeAttribute( PARAMETER_WEBSERVICE_SERVICE_FAIT_PRESTA );
            return;
        }

        int nIdSignalement = getIdSignalement( nIdResourceHistory );

        Signalement bean = _signalementService.getSignalementWithFullPhoto( nIdSignalement );

        if ( bean == null )
        {
            return;
        }

        // Initialize the state after with the first value of the action

        int nNewState = getAction( ).getStateAfter( ).getId( );

        int idUnit = bean.getTypeSignalement( ).getUnit( ).getIdUnit( );

        // Specificity for DEVE entity

        if ( idUnit == Integer.valueOf( AppPropertiesService.getProperty( SignalementConstants.UNIT_ATELIER_JARDINAGE ) ) )

        {

            idUnit = 1;

        }

        // For service done case
        if ( getAction( ).getStateAfter( ).getId( ) == Integer
                .parseInt( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_VALUE ) ) )
        {
            if ( checkDateServiceFaitTraitement( bean ) )
            {
                // if Task is call to notify partner service done
                callWSPartnerServiceDone( nIdResourceHistory, bean, idUnit );
                return;
            }
            else
            {
                return;
            }
        }

        // id task to use, for deamon us task id 102
        int idTaskDaemonWSPartner = Integer.parseInt( AppPropertiesService.getProperty( ID_TASK_SIGNALEMENT_WS_DEFAULT_CONFIG ) );
        int idTaskToUse = getAction( ).getId( ) == AppPropertiesService.getPropertyInt( SignalementConstants.ID_ACTION_TRANSFERT_PARTNER, -1 )
                ? idTaskDaemonWSPartner
                : getId( );

        WebServiceSignalementTaskConfig wsconfig = _configDAO.findByPrimaryKey( idTaskToUse,

                SignalementPlugin.getPlugin( ) );

        WebServiceSignalementTaskConfigUnit wsunitconfig = null;

        // DMR-1476 - Gestion des WS prestataires
        if ( bean.getSecteur( ) != null )
        {
            Integer idSector = bean.getSecteur( ).getIdSector( );
            // Récupération de la conf pour les sous entités (id_parent !=0)
            wsunitconfig = _configunitDAO.findBySector( idTaskToUse, idSector, SignalementPlugin.getPlugin( ), false );

            // Si pas de conf, récupération de la conf de l'entité parent (id_parent==0)
            if ( wsunitconfig == null )
            {
                wsunitconfig = _configunitDAO.findBySector( idTaskToUse, idSector, SignalementPlugin.getPlugin( ), true );
            }
        }

        // save the JSON response in the workflow history

        WebServiceValue webservicevalue = new WebServiceValue( );

        webservicevalue.setIdTask( getId( ) );

        webservicevalue.setIdResourceHistory( nIdResourceHistory );

        Integer typeSignalementId = AppPropertiesService.getPropertyInt(

                SignalementConstants.TYPE_SIGNALEMENT_ENCOMBRANT, -1 );

        List<TypeSignalement> listeTypeSignalement = new ArrayList<>( );

        getTypeSignalementService( ).getAllSousTypeSignalementCascade( typeSignalementId, listeTypeSignalement );

        // Check if the signalement type is not TypeEncombrant and of unit DPE and in not direction DEVE

        if ( !( TaskUtils.isSignalementOfTypeEncombrant( listeTypeSignalement, bean ) && ( idUnit == SignalementConstants.UNIT_DPE ) )
                && !checkIsDirectionDEVE( bean ) )

        {

            // check if the WebServiceSignalementTask is configured in BO

            if ( wsconfig != null )

            {

                // Vérifie que des informations ont été renseignées/trouvées concernant l'entité concernée

                if ( wsunitconfig != null )

                {

                    // Une URL de WS a été renseignée

                    if ( StringUtils.isBlank( wsunitconfig.getPrestataireSansWS( ) ) )

                    {

                        JSONObject wsResult = _signalementWebService.getJSONResponse( bean,

                                wsunitconfig.getUrlPrestataire( ) );

                        if ( checkResponse( bean, wsResult ) )

                        {
                            // etat de sortie renseigné en conf' si l'envoi par WS a fontionné

                            nNewState = wsconfig.getStateWithWSSuccess( );

                            webservicevalue.setValue( I18nService.getLocalizedString(

                                    MESSAGE_PROVIDER_WEBSERVICE_SUCCESS, Locale.FRENCH ) );

                            bean.setSendWs( true );
                            _signalementService.update( bean );

                        }

                        else

                        {
                            // Etat de sortie renseigné en conf' si l'envoi en WS n'a pas fonctionné

                            nNewState = wsconfig.getStateWithWSFailure( );

                            if ( bean.getAdresses( ).isEmpty( ) || !SignalementUtils.isValidAddress( bean.getAdresses( ).get( 0 ).getAdresse( ) ) )
                            {

                                webservicevalue.setValue( I18nService.getLocalizedString( MESSAGE_WEBSERVICE_FAILURE_WRONG_ADDRESS, Locale.FRENCH ) );

                            }
                            else
                            {
                                if ( wsResult.containsKey( JSON_TAG_ANSWER ) && ( wsResult.getJSONObject( JSON_TAG_ANSWER ) != null ) )
                                {
                                    webservicevalue.setValue( I18nService.getLocalizedString(

                                            MESSAGE_PROVIDER_WEBSERVICE_FAILURE, Locale.FRENCH )

                                            + wsResult.getJSONObject( JSON_TAG_ANSWER ).getString( JSON_TAG_ERROR ) );
                                }
                                else
                                    if ( wsResult.containsKey( JSON_TAG_ERROR ) )
                                    {
                                        webservicevalue.setValue( I18nService.getLocalizedString(

                                                MESSAGE_PROVIDER_WEBSERVICE_FAILURE, Locale.FRENCH )

                                                + wsResult.getString( JSON_TAG_ERROR ) );
                                    }
                                    else
                                    {
                                        throw new TechnicalException(
                                                I18nService.getLocalizedString( MESSAGE_PROVIDER_WEBSERVICE_FAILURE_WRONG_RESPONSE, Locale.FRENCH ) );
                                    }
                            }

                        }

                    }

                    // Anomalie avec prestataire mais sans URL de WS renseignée

                    else

                    {

                        // etat de sortie renseigné en conf'

                        nNewState = wsconfig.getStateWithWSSuccess( );

                        webservicevalue.setValue( I18nService.getLocalizedString( MESSAGE_PROVIDER_WITHOUT_WEBSERVICE,

                                Locale.FRENCH ) );

                        bean.setSendWs( false );
                        _signalementService.update( bean );

                    }

                }

                else

                {

                    // etat de sortie renseigné en conf' si aucune URL de WS n'a été renseignée

                    nNewState = wsconfig.getStateWithoutWS( );

                    bean.setSendWs( false );
                    _signalementService.update( bean );

                }

            }

            else

            {

                webservicevalue.setValue( I18nService.getLocalizedString( MESSAGE_PROVIDER_WEBSERVICE_NO_CONFIG_SAVED,

                        Locale.FRENCH ) );

            }

            // save the JSON response in the workflow history

            _webserviceValueService.create( webservicevalue, SignalementPlugin.getPlugin( ) );

        }

        // For Direction == DEVE new state is nouveau

        if ( checkIsDirectionDEVE( bean ) )
        {
            nNewState = Integer.valueOf( AppPropertiesService.getProperty( ID_STATE_NOUVEAU ) );
        }

        State state = _stateService.findByPrimaryKey( nNewState );

        getAction( ).setStateAfter( state );

    }

    /**
     * Call Partner Web Service to notify service done.
     *
     * @param nIdResourceHistory
     *            id resoure history.
     * @param signalement
     *            the report object.
     * @param idUnit
     *            the idUnit
     */
    public void callWSPartnerServiceDone( int nIdResourceHistory, Signalement signalement, int idUnit )
    {

        int idTaskDaemonWSPartner = Integer.parseInt( AppPropertiesService.getProperty( ID_TASK_SIGNALEMENT_WS_DEFAULT_CONFIG ) );
        WebServiceSignalementTaskConfigUnit wsunitconfig = _configunitDAO.findByPrimaryKey( idTaskDaemonWSPartner, idUnit, SignalementPlugin.getPlugin( ) );

        if ( ( wsunitconfig != null ) && !StringUtils.isBlank( wsunitconfig.getUrlPrestataire( ) ) )
        {

            WebServiceValue webservicevalue = new WebServiceValue( );
            webservicevalue.setIdTask( getId( ) );
            webservicevalue.setIdResourceHistory( nIdResourceHistory );

            JSONObject responseJson = null;
            try
            {
                responseJson = _signalementWebService.callWSPartnerServiceDone( signalement, wsunitconfig.getUrlPrestataire( ) );
            }
            catch( BusinessException e )
            {
                AppLogService.error( e.getMessage( ), e );
                responseJson = new JSONObject( );
                responseJson.accumulate( JSON_TAG_ERROR, true );
            }

            if ( !checkResponse( signalement, responseJson ) )
            {

                webservicevalue.setValue( I18nService.getLocalizedString( MESSAGE_PROVIDER_WEBSERVICE_FAILURE, Locale.FRENCH ) );

                State state = _stateService.findByPrimaryKey( Integer.valueOf( AppPropertiesService.getProperty( ID_STATE_ECHEC_WS ) ) );
                getAction( ).setStateAfter( state );
            }
            else
            {
                webservicevalue.setValue( I18nService.getLocalizedString( MESSAGE_WEBSERVICE_DONE_SUCCESS, Locale.FRENCH ) );
            }

            _webserviceValueService.create( webservicevalue, SignalementPlugin.getPlugin( ) );
        }

    }

    /**
     * Check if direction of the signalement is DEVE.
     *
     * @param signalement
     *            report to check
     * @return true if is DEVE
     */
    private boolean checkIsDirectionDEVE( Signalement signalement )
    {

        boolean isDirectionDEVE = false;
        List<Unit> listUnitsSector = _unitService.findBySectorId( signalement.getSecteur( ).getIdSector( ) );
        for ( Unit unit : listUnitsSector )
        {
            if ( ( unit.getIdParent( ) == 0 ) && ( unit.getIdUnit( ) == Integer.parseInt( SignalementConstants.UNIT_DEVE ) ) )
            {
                isDirectionDEVE = true;
            }
        }
        return isDirectionDEVE;
    }

    /**
     * Check the response.
     *
     * @param bean
     *            the report
     * @param wsResult
     *            ws response
     * @return true is response is accept
     */
    private boolean checkResponse( Signalement bean, JSONObject wsResult )

    {

        boolean result = false;

        try

        {

            if ( wsResult.containsKey( JSON_TAG_ANSWER ) && wsResult.getJSONObject( JSON_TAG_ANSWER ).containsKey( "id" ) )

            {

                int idSignalementFromJson = wsResult.getJSONObject( JSON_TAG_ANSWER ).getInt( "id" );

                if ( bean.getId( ) == idSignalementFromJson )

                {

                    result = true;

                }

            }

        }

        catch( Exception e )

        {

            AppLogService.error( e );

        }

        return result;

    }

    /**
     * Do remove task information.
     *
     * @param nIdHistory
     *            the n id history
     */
    @Override
    public void doRemoveTaskInformation( int nIdHistory )

    {

        _webserviceValueService.removeByHistory( nIdHistory, getId( ), null );

    }

    /**
     * Do remove config.
     */
    @Override
    public void doRemoveConfig( )

    {

        _configDAO.delete( getId( ), SignalementUtils.getPlugin( ) );

        _configunitDAO.deleteAll( getId( ), SignalementUtils.getPlugin( ) );

    }

    /**
     * Gets the title.
     *
     * @param locale
     *            the locale
     * @return the title
     */
    @Override
    public String getTitle( Locale locale )

    {

        return "Envoi du signalement par WS";

    }

    /**
     * Gets the task form entries.
     *
     * @param locale
     *            the locale
     * @return the task form entries
     */
    @Override
    public Map<String, String> getTaskFormEntries( Locale locale )

    {

        return null;

    }

    /**
     * Check if the signalement has a non null date service fait value
     * 
     * @param signalement
     * @return False if the DateServiceFaitTraitement value is null (DateServiceFaitTraitement), True otherwise
     */
    private boolean checkDateServiceFaitTraitement( Signalement signalement )
    {
        return ( signalement != null ) && ( signalement.getDateServiceFaitTraitement( ) != null );
    }

}
