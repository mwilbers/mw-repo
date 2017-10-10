
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.markus-wilbers.de/mwTags" prefix="mw" %> 
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div id="admin">

	<p></p>
	
	<c:set var="urlSave" >
   		<mw:url action="save" servletSubPath="${ servletSubPath }" menue="${ urlManager.menue }" />
   	</c:set>
	<form:form method="POST" action="${ urlSave }" commandName="currentObject">
	<!-- <form:errors path="*" cssClass="error" /> -->
	<form:input  path="item.id" />
	<table border="1" cellpadding="1" cellspacing="1">
	    <thead>
		    <tr>
		    	<th class="list_headline">OFDB-Name</th>
		        <th class="list_headline">propValue</th>
		        <th class="list_headline">reihenfolge</th>
		        <th class="list_headline">maxlength</th>
		        <th class="list_headline">nullable</th>
		        <th class="list_headline">editable</th>
		    </tr>
	    </thead>
	    <tbody>
	    
	    <c:forEach items="${ofPropList}" var="ofProp" >
	    	<tr>
		    	<td class="list_row">${ofProp.columnTitle}</td>
		    	<td class="list_row">ss
		    		<c:choose>
					<c:when test="${ofProp.mapped == 'true'}">
					
						<c:choose>
						<c:when test="${ofProp.editable == 'true'}">	
					
							<%@ include file="control.jsp" %>
					
						</c:when>
						<c:otherwise>
							<form:input title="${ofProp.diagnose}" path="item.${ofProp.propName}" maxlength='${ofProp.maxlength}' readonly="true" />not editable
						</c:otherwise>
						</c:choose>
						
					</c:when>
					<c:otherwise>not mapped</c:otherwise>
					</c:choose>
		    		<!-- <input type="text" id="${ofProp.propName}" name="${ofProp.propName}" value="${ofProp.propName}" maxlength="${ofProp.maxlength}" >  -->
                </td>
		        <td class="list_row">${ofProp.reihenfolge}</td>
		    	<td class="list_row">${ofProp.maxlength}</td>
		    	<td class="list_row">${ofProp.nullable}</td>
		    	<td class="list_row">${ofProp.editable}</td>
		    </tr>
	    </c:forEach>
		       
		    
		    <tr>  
		        <td class="list_row">
		        	<input type="submit" id="saveTabDef" name="saveTabDef" value="<fmt:message key="main.save"/>" />
		        </td>
		        <td class="list_row">
		        	<input type="reset"  value="<fmt:message key="main.cancel"/>" />
		        </td>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		    </tr>
	    </tbody>
	</table>
	
	</form:form>
	
</div>