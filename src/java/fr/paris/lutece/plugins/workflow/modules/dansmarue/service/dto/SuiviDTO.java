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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.service.dto;

/**
 * SuiviDTO.
 */
public class SuiviDTO
{

    /** The method. */
    private String _method;

    /** The token. */
    private String _token;

    /** The valide. */
    private Boolean _valide;

    /**
     * Gets the method.
     *
     * @return the method
     */
    public String getMethod( )
    {
        return _method;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken( )
    {
        return _token;
    }

    /**
     * Gets the valide.
     *
     * @return the valide
     */
    public Boolean getValide( )
    {
        return _valide;
    }

    /**
     * Sets the method.
     *
     * @param method
     *            the new method
     */
    public void setMethod( String method )
    {
        _method = method;
    }

    /**
     * Sets the token.
     *
     * @param token
     *            the new token
     */
    public void setToken( String token )
    {
        _token = token;
    }

    /**
     * Sets the valide.
     *
     * @param valide
     *            the new valide
     */
    public void setValide( Boolean valide )
    {
        _valide = valide;
    }

}
