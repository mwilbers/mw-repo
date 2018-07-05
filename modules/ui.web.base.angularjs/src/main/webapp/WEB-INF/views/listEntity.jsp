<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.markus-wilbers.de/mwTags" prefix="mw" %> 
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<div id="admin">

	<p></p>
	<!-- <form:errors path="*" cssClass="error" /> -->
	<table border="1" cellpadding="1" cellspacing="1">
	    
	    <c:if test="${empty entitiesArray}">
	    	<c:out value="xxx"></c:out>
	    </c:if>
	    
    	<!-- ............. set some variables ...................... -->
    	<c:set var="varUrlFilter" ><mw:url action="filter" servletSubPath="${ navigationState.urlPath }" menue="${ navigationState.menue }" /></c:set>
    	<c:set var="varIconArrowUp" ><mw:iconUrl iconName="sortarrow_up"/></c:set>
    	<c:set var="varIconArrowDown" ><mw:iconUrl iconName="sortarrow_down"/></c:set>
    	<c:set var="urlEdit" ><mw:url action="edit" servletSubPath="${ navigationState.urlPath }" /></c:set>
    	<c:set var="urlAdd" ><mw:url action="edit" servletSubPath="${ navigationState.urlPath }" menue="${ navigationState.menue }" /></c:set>
    	<c:set var="varIconBearbeiten" ><mw:iconUrl iconName="bearbeiten_button" /></c:set>
    	
		<form:form method="POST" action="${ varUrlFilter }" commandName="filterSet">
    
	    <thead>
	    <tr>
	    	<th><div class="list_headline"><fmt:message key="main.number"/></div></th>
	    	
	    	<!--+ 
	    		headers for filters
	    	 +-->
	    	
		    <c:forEach items="${ofPropList}" var="ofProp" >
	    		<c:if test="${ofProp.visible == 'true'}">
					<th>
					<table border="1" cellspacing="1" cellpadding="1">
					<tr>
						<td><div title="${ofProp.diagnose}" class="list_headline"><c:out value='${ofProp.columnTitle}'/></div></td>							
						<td>
							<a href="<mw:url servletSubPath="${ navigationState.urlPath }" action="sort" col="${ofProp.propName}" asc="1" menue="${ navigationState.menue }" />">
								<img border="1" src="${ varIconArrowUp }">
							</a><br>
							<a href="<mw:url servletSubPath="${ navigationState.urlPath }" action="sort" col="${ofProp.propName}" asc="0" menue="${ navigationState.menue }" />">
								<img border="1" src="${ varIconArrowDown }">
							</a>
						</td>
					</tr>								
					</table>
					</th>
				</c:if>
			</c:forEach>  			
			<th><div>cc</div></th>
	    </tr> 
	    
	    <!--+ 
	    		filter controls
	    	 +-->
		<tr>
	    	<th class="list_row">
				<input type="submit" value="<fmt:message key="main.filter"/>" />
				<p></p>
				<a href="${ urlAdd }" style="border: solid">+</a>
			</th>
	    	<c:forEach items="${ofPropList}" var="ofProp" >
	    		<th>
	    			<mw:ofdbInput entityTO="${ filterSet }" ofProp="${ ofProp }" />				    				
				</th>
		    </c:forEach>    
		    <th class="list_row">
	        	<input type="submit" value="<fmt:message key="main.filter"/>" />		        	
	        </th>
	    </tr>
	    </thead>

		</form:form>

		
		<form:form method="POST" action="${ urlEdit }" commandName="currentObject">
	    
	    <tbody>
	    <c:set var="no" value="${(entitiesPl.indexCurrentStep - 1) * entitiesPl.defaultStepSize}" />
	    <c:forEach items="${entitiesArray}" var="entityRow">
	    
	    	<c:set var="no" value="${no + 1}"></c:set>	    	
	    	<c:set var="entity" value="${entityRow[0]}"></c:set>	    	
	    	
	    	<tr>
	    		<td><div class="list_row"><c:out value="${no}"/></div></td>
		    		
		    	<c:forEach items="${ofPropList}" var="ofProp" >
		    	
		    		<c:choose>
	    			<c:when test="${ofProp.mapped == 'true'}">
			    		<c:if test="${ofProp.visible == 'true'}">		    		
			    			<c:if test="${ not empty ofProp.listOfValues }">
			    				<c:if test="${ ofProp['enum'] == 'true'}">
			    					<td><div class="list_row"><c:out value='${entity[ofProp.propName]}'/></div></td>	
			    				</c:if>
			    				<c:if test="${ofProp['enum'] == 'false'}">
			    					<td><div class="list_row"><c:out value='${entityRow[ofProp.resultIndex]}'/></div></td>
			    				</c:if>
			    			</c:if>
			    			<c:if test="${ empty ofProp.listOfValues }">
				    			<c:if test="${ ofProp.dbtype == 'DATETIME' }">
				    				<td><div class="list_row"><fmt:formatDate value='${entity[ofProp.propName]}'/></div></td>
				    			</c:if>
				    			<c:if test="${ ofProp.dbtype != 'DATETIME' }">
				    				<td><div class="list_row"><c:out value='${entity[ofProp.propName]}'/></div></td>
				    			</c:if>
			    			</c:if>
			    		</c:if>
			    		<c:if test="${ofProp.visible == 'false'}">
							<td><div>'${ ofProp.propName }' not visible</div></td>
						</c:if>
		    		</c:when>
		    		<c:otherwise>
		    			<td><div class="list_row"><c:out value=''/></div></td>
		    		</c:otherwise>
		    		</c:choose>
	
				</c:forEach>
				<c:set var="id" value="${entity.id}"></c:set> 
				<td><div class="list_row">
					<a href="<mw:url servletSubPath="${ navigationState.urlPath }" action="edit" entityId="${ id }" menue="${ navigationState.menue }" />">
						<img src="${ varIconBearbeiten }" >
					</a>
					</div>
				</td>
				  
			</tr>      
	    </c:forEach>
	    </tbody>
		
		</form:form>
					
	</table>
	<p><mw:pager paginatedList="${entitiesPl}" servletSubPath="${ navigationState.urlPath }" />&nbsp; ${entitiesPl.count} Datensaetze</p>
</div>