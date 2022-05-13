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
package fr.paris.lutece.plugins.workflow.modules.dansmarue.service;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;

/**
 * TaksUtils.
 */
public final class TaskUtils
{

    /** The Constant MESSAGE_ERROR. */
    private static final String MESSAGE_ERROR = "dansmarue.message.error.args";

    /**
     * Utility class.
     */
    private TaskUtils( )
    {
        // nothing
    }

    /**
     * Récupère le message d'erreurs de validation.
     *
     * @param <T>
     *            type du bean et des ConstraintViolation
     * @param bean
     *            le bean en erreur
     * @param locale
     *            la lcale courante
     * @param errors
     *            le jeu d'erreurs por le bean
     * @param request
     *            la requête courante
     * @return le message d'erreurs de validation
     */
    public static <T> String getValidationErrorsMessage( T bean, Locale locale, Set<ConstraintViolation<T>> errors, HttpServletRequest request )
    {
        String [ ] messageArgs = new String [ 1];
        StringBuilder sbMessage = new StringBuilder( );
        for ( ConstraintViolation<T> error : errors )
        {
            String typeName = bean.getClass( ).getSimpleName( );
            sbMessage.append( "<br />" );
            String fieldName = I18nService.getLocalizedString( "ramen.field." + typeName + "." + error.getPropertyPath( ), locale );
            sbMessage.append( fieldName ).append( " : " );
            sbMessage.append( error.getMessage( ) );
        }
        messageArgs [0] = sbMessage.toString( );

        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR, messageArgs, AdminMessage.TYPE_STOP );
    }

    /**
     * Checks if signalement is of type encombrant.
     *
     * @param listeTypeSignalement
     *            the listeTypeSignalement
     * @param signalement
     *            the signalement
     * @return true, if signalement is of type encombrant
     */
    public static boolean isSignalementOfTypeEncombrant( List<TypeSignalement> listeTypeSignalement, Signalement signalement )
    {
        Iterator<TypeSignalement> iterator = listeTypeSignalement.iterator( );
        boolean hasType = false;
        while ( !hasType && iterator.hasNext( ) )
        {
            TypeSignalement next = iterator.next( );
            hasType = signalement.getTypeSignalement( ).getId( ).equals( next.getId( ) );
        }
        return hasType;
    }

    /**
     * Gets the id type ano lvl 1.
     *
     * @param typeSignalement
     *            the id type ano
     * @return the id type ano lvl 1
     */
    public static int getIdTypeAnoLvl1( TypeSignalement typeSignalement )
    {
        boolean isLastLevel = false;
        int idTypeLvl1 = -1;

        while ( !isLastLevel )
        {
            if ( typeSignalement.getTypeSignalementParent( ).getTypeSignalementParent( ) != null )
            {
                typeSignalement = typeSignalement.getTypeSignalementParent( );
            }
            else
            {
                idTypeLvl1 = typeSignalement.getTypeSignalementParent( ).getId( );
                isLastLevel = true;
            }

        }
        return idTypeLvl1;
    }

    /**
     * Gets the CP from adresse.
     *
     * @param adresse
     *            the adresse
     * @return the CP from adresse
     */
    public static String getCPFromAdresse( String adresse )
    {
        Pattern pattern = Pattern.compile( "75[0-9]{3}" );
        Matcher matcher = pattern.matcher( adresse );
        if ( matcher.find( ) )
        {
            return matcher.group( );
        }
        else
        {
            return StringUtils.EMPTY;
        }
    }
}
