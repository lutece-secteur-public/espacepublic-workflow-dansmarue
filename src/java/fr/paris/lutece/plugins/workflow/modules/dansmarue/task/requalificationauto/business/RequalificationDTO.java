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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.requalificationauto.business;

import javax.validation.constraints.NotNull;

/**
 * RequalificationDTO class
 *
 */
public class RequalificationDTO
{
    private Double  _dLat;
    private Double  _dLng;
    private Integer _nIdSignalement;
    @NotNull
    private Integer _nTypeSignalement;
    private String  _strAdresseRequalif;
    private String  _strCommentaireRequalification;
    private Integer _nSector;

    /**
     * Return the adresse of requalification
     *
     * @return _strAdresseRequalif
     */
    public String getAdresseRequalif( )
    {
        return _strAdresseRequalif;
    }

    /**
     * Return the signalement id
     *
     * @return _nIdSignalement
     */
    public Integer getIdSignalement( )
    {
        return _nIdSignalement;
    }

    /**
     * Return the latitude
     *
     * @return _dLat
     */
    public Double getLat( )
    {
        return _dLat;
    }

    /**
     * Return the longitude
     *
     * @return _dLng
     */
    public Double getLng( )
    {
        return _dLng;
    }

    /**
     * Return the type signalement id
     *
     * @return _nTypeSignalement
     */
    public Integer getTypeSignalement( )
    {
        return _nTypeSignalement;
    }

    /**
     * Set the requalification adresse
     *
     * @param adresse
     *            the requalification adresse
     */
    public void setAdresseRequalif( String adresse )
    {
        _strAdresseRequalif = adresse;
    }

    /**
     * Set the signalement id
     *
     * @param idSignalement
     *            the signalement id
     */
    public void setIdSignalement( Integer idSignalement )
    {
        _nIdSignalement = idSignalement;
    }

    /**
     * Set the latitude
     *
     * @param dLat
     *            the latitude
     */
    public void setLat( Double dLat )
    {
        _dLat = dLat;
    }

    /**
     * Set the longitude
     *
     * @param dLng
     *            the longitude
     */
    public void setLng( Double dLng )
    {
        _dLng = dLng;
    }

    /**
     * Set the type signalement id
     *
     * @param typeSignalement
     *            the type signalement id
     */
    public void setTypeSignalement( Integer typeSignalement )
    {
        _nTypeSignalement = typeSignalement;
    }

    /**
     * Return the requalification commentaire
     *
     * @return _strCommentaireRequalification
     */
    public String getCommentaireRequalification( )
    {
        return _strCommentaireRequalification;
    }

    /**
     * Set the requalification commentaire
     *
     * @param commentaireRequalification
     *            the requalification commentaire
     */
    public void setCommentaireRequalification( String commentaireRequalification )
    {
        _strCommentaireRequalification = commentaireRequalification;
    }

    /**
     * Set the sector id
     *
     * @param sectorId
     *            the type sector id
     */
    public void setSector( Integer sectorId )
    {
        _nSector = sectorId;
    }

    /**
     * Get the sector id
     *
     * @return The signalement sector id
     */
    public Integer getSector( )
    {
        return _nSector;
    }

}
