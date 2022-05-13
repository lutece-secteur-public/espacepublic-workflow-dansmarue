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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business;

/**
 * DTO for RequalificationAutoUnit.
 */
public class RequalificationAutoUnitDTO extends RequalificationAutoConfigUnit
{

    /** The str source unit name. */
    private String _strSourceUnitName;

    /** The str type signalement name. */
    private String _strTypeSignalementName;

    /** The str target unit name. */
    private String _strTargetUnitName;

    /** The str workflow state after name. */
    private String _strWorkflowStateAfterName;

    /**
     * Creates a RequalificationAutoUnitDTO with values of a RequalificationAutoUnit.
     *
     * @param requalifUnit
     *            The object this DTO represents
     */
    public RequalificationAutoUnitDTO( RequalificationAutoConfigUnit requalifUnit )
    {
        setIdConfigUnit( requalifUnit.getIdConfigUnit( ) );
        setIdTask( requalifUnit.getIdTask( ) );
        setIdSourceUnit( requalifUnit.getIdSourceUnit( ) );
        setIdTargetUnit( requalifUnit.getIdTargetUnit( ) );
        setIdTypeSignalement( requalifUnit.getIdTypeSignalement( ) );
        setIdStateAfter( requalifUnit.getIdStateAfter( ) );
    }

    /**
     * Get the name of the source unit.
     *
     * @return The name of the source unit
     */
    public String getSourceUnitName( )
    {
        return _strSourceUnitName;
    }

    /**
     * Set the name of the source unit.
     *
     * @param strSourceUnitName
     *            The name of the source unit
     */
    public void setSourceUnitName( String strSourceUnitName )
    {
        _strSourceUnitName = strSourceUnitName;
    }

    /**
     * Get the name of the type signalement.
     *
     * @return The name of the type signalement
     */
    public String getTypeSignalementName( )
    {
        return _strTypeSignalementName;
    }

    /**
     * Set the name of the type signalement.
     *
     * @param strTypeSignalementName
     *            The name of the type signalement
     */
    public void setTypeSignalementName( String strTypeSignalementName )
    {
        _strTypeSignalementName = strTypeSignalementName;
    }

    /**
     * Get the name of the target unit.
     *
     * @return The name of the target unit
     */
    public String getTargetUnitName( )
    {
        return _strTargetUnitName;
    }

    /**
     * Set the name of the target unit.
     *
     * @param strTargetUnitName
     *            The name of the target unit
     */
    public void setTargetUnitName( String strTargetUnitName )
    {
        _strTargetUnitName = strTargetUnitName;
    }

    /**
     * Get the name of the next workflow state.
     *
     * @return The name of the next workflow state
     */
    public String getWorkflowStateAfterName( )
    {
        return _strWorkflowStateAfterName;
    }

    /**
     * Set the name of the next workflow state.
     *
     * @param strWorkflowStateAfterName
     *            The name of the next workflow state
     */
    public void setWorkflowStateAfterName( String strWorkflowStateAfterName )
    {
        _strWorkflowStateAfterName = strWorkflowStateAfterName;
    }

}
