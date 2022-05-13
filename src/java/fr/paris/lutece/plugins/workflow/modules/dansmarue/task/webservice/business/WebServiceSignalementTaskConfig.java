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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.webservice.business;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

/**
 * WebServiceSignalementTaskConfig.
 */
public class WebServiceSignalementTaskConfig extends TaskConfig
{
    /** The state with WS success. */
    private Integer _stateWithWSSuccess;

    /** The state with WS failure. */
    private Integer _stateWithWSFailure;

    /** The state without WS. */
    private Integer _stateWithoutWS;

    /**
     * Gets the state with WS success.
     *
     * @return the state with WS success
     */
    public Integer getStateWithWSSuccess( )
    {
        return _stateWithWSSuccess;
    }

    /**
     * Gets the state with WS failure.
     *
     * @return the state with WS failure
     */
    public Integer getStateWithWSFailure( )
    {
        return _stateWithWSFailure;
    }

    /**
     * Gets the state without WS.
     *
     * @return the state without WS
     */
    public Integer getStateWithoutWS( )
    {
        return _stateWithoutWS;
    }

    /**
     * Sets the state with WS success.
     *
     * @param stateWithWSSuccess
     *            the new state with WS success
     */
    public void setStateWithWSSuccess( Integer stateWithWSSuccess )
    {
        _stateWithWSSuccess = stateWithWSSuccess;
    }

    /**
     * Sets the state with WS failure.
     *
     * @param stateWithWSFailure
     *            the new state with WS failure
     */
    public void setStateWithWSFailure( Integer stateWithWSFailure )
    {
        _stateWithWSFailure = stateWithWSFailure;
    }

    /**
     * Sets the state without WS.
     *
     * @param stateWithoutWS
     *            the new state without WS
     */
    public void setStateWithoutWS( Integer stateWithoutWS )
    {
        _stateWithoutWS = stateWithoutWS;
    }

}
