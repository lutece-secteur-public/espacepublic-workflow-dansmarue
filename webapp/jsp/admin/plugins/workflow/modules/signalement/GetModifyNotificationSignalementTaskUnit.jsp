
<jsp:useBean id="notificationSignalementConfig" scope="session" class="fr.paris.lutece.plugins.workflow.modules.dansmarue.web.NotificationSignalementTaskConfigJspBean" />
<%
notificationSignalementConfig.init( request, fr.paris.lutece.plugins.workflow.web.WorkflowJspBean.RIGHT_MANAGE_WORKFLOW );
%>
<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= notificationSignalementConfig.getModifyNotificationSignalementTaskUnit( request ) %>

<%@ include file="../../../../AdminFooter.jsp" %>
