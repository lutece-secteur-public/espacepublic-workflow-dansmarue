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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

/**
 * Requalifier signalement task configuration
 */
public class RequalificationAutoConfigUnit extends TaskConfig
{
    private int _nIdConfigUnit;
    private int _nIdSourceUnit;
    private int _nIdTypeSignalement;
    private int _nIdTargetUnit;
    private int _nIdStateAfter;

    /**
     * Get the id
     *
     * @return The id
     */
    public int getIdConfigUnit( )
    {
        return _nIdConfigUnit;
    }

    /**
     * Set the id
     *
     * @param nIdConfigUnit
     *            The id
     */
    public void setIdConfigUnit( int nIdConfigUnit )
    {
        _nIdConfigUnit = nIdConfigUnit;
    }

    /**
     * Get the id of the source unit of the requalification
     *
     * @return The id of the source unit of the requalification
     */
    public int getIdSourceUnit( )
    {
        return _nIdSourceUnit;
    }

    /**
     * Set the id of the source unit of the requalification
     *
     * @param nIdSourceUnit
     *            The id of the source unit of the requalification
     */
    public void setIdSourceUnit( int nIdSourceUnit )
    {
        _nIdSourceUnit = nIdSourceUnit;
    }

    /**
     * Get the id of the type signalement, or -1 if no type signalement is associated with this requalification unit
     *
     * @return The id of the type signalement
     */
    public int getIdTypeSignalement( )
    {
        return _nIdTypeSignalement;
    }

    /**
     * Set the id of the type signalement, or -1 if no type signalement is associated with this requalification unit
     *
     * @param nIdTypeSignalement
     *            The id of the type signalement
     */
    public void setIdTypeSignalement( int nIdTypeSignalement )
    {
        _nIdTypeSignalement = nIdTypeSignalement;
    }

    /**
     * Get the id of the target unit of this requalification
     *
     * @return The id of the target unit of this requalification
     */
    public int getIdTargetUnit( )
    {
        return _nIdTargetUnit;
    }

    /**
     * Set the id of the target unit of this requalification
     *
     * @param nIdTargetUnit
     *            The id of the target unit of this requalification
     */
    public void setIdTargetUnit( int nIdTargetUnit )
    {
        _nIdTargetUnit = nIdTargetUnit;
    }

    /**
     * Get the id of the workflow state to set the resource after executing the requalification
     *
     * @return The id of the workflow state to set the resource after executing the requalification, or a negative value to keep the original state
     */
    public int getIdStateAfter( )
    {
        return _nIdStateAfter;
    }

    /**
     * Set the id of the workflow action to execute after this task
     *
     * @param nIdStateAfter
     *            The id of the workflow state to set the resource after executing the requalification, or a negative value to keep the original state
     */
    public void setIdStateAfter( int nIdStateAfter )
    {
        _nIdStateAfter = nIdStateAfter;
    }

}
