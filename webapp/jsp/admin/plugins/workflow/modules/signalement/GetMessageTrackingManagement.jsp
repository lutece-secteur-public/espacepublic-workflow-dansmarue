
<%@page import="fr.paris.lutece.plugins.workflow.modules.dansmarue.service.role.WorkflowSignalementResourceIdService"%>

<jsp:useBean id="messageTracking" scope="session" class="fr.paris.lutece.plugins.workflow.modules.dansmarue.web.MessageTrackingJspBean" />

<% 
	messageTracking.init( request, "MESSAGE_TRACKING_MANAGEMENT", WorkflowSignalementResourceIdService.KEY_ID_RESOURCE, WorkflowSignalementResourceIdService.PERMISSION_GESTION_TRACKING_MESSAGE );	
%>
<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />
<%= messageTracking.getManageTrackingMessage(request) %>

<%@ include file="../../../../AdminFooter.jsp" %>