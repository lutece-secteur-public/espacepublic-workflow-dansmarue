<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="notificationSignalementConfig" scope="session" class="fr.paris.lutece.plugins.workflow.modules.dansmarue.web.NotificationSignalementTaskConfigJspBean" />
<%
    notificationSignalementConfig.init( request, fr.paris.lutece.plugins.workflow.web.WorkflowJspBean.RIGHT_MANAGE_WORKFLOW );
	response.sendRedirect( notificationSignalementConfig.doModifyNotificationSignalementTaskUnit( request ) );
%>