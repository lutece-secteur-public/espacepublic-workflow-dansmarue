<%@ page errorPage="../../../../../ErrorPage.jsp" %>
<jsp:useBean id="requalifiactionAutoSignalementConfig" scope="session" class="fr.paris.lutece.plugins.workflow.modules.dansmarue.web.RequalificationAutoSignalementTaskConfigJspBean" />
<%
	requalifiactionAutoSignalementConfig.init( request, fr.paris.lutece.plugins.workflow.web.WorkflowJspBean.RIGHT_MANAGE_WORKFLOW );
	response.sendRedirect( requalifiactionAutoSignalementConfig.confirmDeleteRequalificationAutoSignalementTaskUnit( request ) );
%>