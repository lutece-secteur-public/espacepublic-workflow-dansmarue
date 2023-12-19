package fr.paris.lutece.plugins.workflow.modules.dansmarue.utils;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IWorkflowSignalementUtil
{
    /**
     * Get reject reason.
     *
     * @param request
     *            the http reuest
     * @param observationList
     *            list observation rejet
     *
     * @return list of reject reason
     */
    List<String> getMotifsRejet( HttpServletRequest request, List<ObservationRejet> observationList );

    /**
     * Construct reject reason string for email notification.
     *
     * @param request
     *            the http reuest
     * @param observationList
     *            list observation rejet
     *
     * @return string reject reason
     */
    String buildValueMotifRejetForEmailNotification( HttpServletRequest request, List<ObservationRejet> observationList );
}
