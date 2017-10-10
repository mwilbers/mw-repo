<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.markus-wilbers.de/mwTags" prefix="mw" %> 
	
		<c:if test="${ not empty ofProp.listOfValues }">
			    					
			<c:choose>
				<c:when test="${ ofProp.dbtype == 'ENUM' }">		
				
					<mw:ofdbInput entityTO="${ filterSet }" ofProp="${ ofProp }" />
				<!--+ 		 
					 <form:select path="item.${ofProp.propName}">
						<form:option value="" label="-" />
						<form:options items="${ofProp.listOfValues}" itemValue="description" itemLabel="description" />
					</form:select>
				+-->
					<form:errors path="item.${ofProp.propName}" cssClass="error" />
				</c:when>
				<c:when test="${ ofProp.dbtype == 'STRING' or ofProp.dbtype == 'LONGINTEGER' }">
				
					<mw:ofdbInput entityTO="${ filterSet }" ofProp="${ ofProp }" />
				<!--+
					<form:select path="map['${ofProp.itemKey}'].value">
						<form:option value="" label="-" />
						<form:options items="${ofProp.listOfValues}" itemValue="${ofProp.itemValue}" itemLabel="${ofProp.itemLabel}" />
					</form:select>
				+-->
					<form:errors path="map['${ofProp.itemKey}'].value" cssClass="error" />
					
				</c:when>					    					
			</c:choose>
			    			
		</c:if>
		<c:if test="${ empty ofProp.listOfValues }">
			    				
				
					<mw:ofdbInput entityTO="${ filterSet }" ofProp="${ ofProp }" />
					<!--+ 
	    		<form:input path="item.${ofProp.propName}" title="${ofProp.diagnose}" maxlength='${ofProp.maxlength}' />
	    		<form:errors path="item.${ofProp.propName}" cssClass="error" />	
	    	 		+-->
					
		
		</c:if>