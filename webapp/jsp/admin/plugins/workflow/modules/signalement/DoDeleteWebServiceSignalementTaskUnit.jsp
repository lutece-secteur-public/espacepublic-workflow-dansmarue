<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="webserviceSignalementConfig" scope="session" class="fr.paris.lutece.plugins.workflow.modules.dansmarue.web.WebServiceSignalementTaskConfigJspBean" />
<%
    webserviceSignalementConfig.init( request, fr.paris.lutece.plugins.workflow.web.WorkflowJspBean.RIGHT_MANAGE_WORKFLOW );
	response.sendRedirect( webserviceSignalementConfig.doDeleteWebServiceSignalementTaskUnit( request ) );
%>