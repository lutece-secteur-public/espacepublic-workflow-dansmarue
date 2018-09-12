<%@ page errorPage="../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.workflow.modules.dansmarue.service.role.WorkflowSignalementResourceIdService"%>
<jsp:useBean id="messageTracking" scope="session" class="fr.paris.lutece.plugins.workflow.modules.dansmarue.web.MessageTrackingJspBean" />
<%
	messageTracking.init( request, fr.paris.lutece.plugins.workflow.modules.dansmarue.web.MessageTrackingJspBean.RIGHT_MANAGE_MESSAGE_TRACKING );
	response.sendRedirect( messageTracking.doAddUnitMessageTracking(request) );
%>