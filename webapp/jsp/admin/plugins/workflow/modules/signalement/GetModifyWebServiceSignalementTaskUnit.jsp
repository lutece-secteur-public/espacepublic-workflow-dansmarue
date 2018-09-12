
<jsp:useBean id="webserviceSignalementConfig" scope="session" class="fr.paris.lutece.plugins.workflow.modules.dansmarue.web.WebServiceSignalementTaskConfigJspBean" />
<% 
webserviceSignalementConfig.init( request, fr.paris.lutece.plugins.workflow.web.WorkflowJspBean.RIGHT_MANAGE_WORKFLOW );
%>
<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= webserviceSignalementConfig.getModifyWebServiceSignalementTaskUnit( request ) %>

<%@ include file="../../../../AdminFooter.jsp" %>
