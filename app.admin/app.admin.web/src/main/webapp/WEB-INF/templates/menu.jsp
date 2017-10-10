<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.markus-wilbers.de/mwTags" prefix="mw" %> 

    <div id="mw-core-div-menu">
		<br>    
    	<div id="mw-core-div-treeArea">
    	<!--  Ebene 0 -->
    	<ul id="mw-core-ul-menuList0">
    	<c:set var="index" value="-1" />
    	<c:forEach items="${menues.rootItem}" var="menuItem" >
    		<c:set var="index" value="${ index + 1 }" />
    		<c:set var="menu" value="${ menuItem.entity }" />
    		
    		<li id="mw-core-menuListItem${ menu.id }">
    		<c:choose>
				<c:when test="${ menu.typ == 'KNOTEN' }">
					<a>${ menu.anzeigeName }</a>
    			</c:when>
    			<c:otherwise>
    				<a href="<mw:url servletSubPath="${ menu.ansichtDef.urlPath }" action="list" menue="${ menu.id }" />">${ menu.anzeigeName }</a>	
    			</c:otherwise>
    		</c:choose>
    		
   			<c:if test="${ menuItem.hasChildren }">
   				<!-- Ebene 1 -->
   				<ul id="mw-core-ul-menuList1">
   				<c:forEach items="${menuItem.children}" var="menuItem1" >
   					<c:set var="index" value="${ index + 1 }" />
   					<c:set var="menu1" value="${ menuItem1.entity }" />
   					
   					<li id="mw-core-menuListItem${ menu1.id }">
   					<c:choose>
						<c:when test="${ menu1.typ == 'KNOTEN' }">
		    				<a>${ menu1.anzeigeName }</a>
		    			</c:when>
		    			<c:otherwise>
		    				<a href="<mw:url servletSubPath="${ menu1.ansichtDef.urlPath }" action="list" menue="${ menu.id }" />">${ menu1.anzeigeName }</a>	
		    			</c:otherwise>
    				</c:choose>
    				
    				<c:if test="${ menuItem1.hasChildren }">
    					<!-- Ebene 2 -->
		   				<ul id="mw-core-ul-menuList2">
		   				<c:forEach items="${menuItem1.children}" var="menuItem2" >
		   					<c:set var="index" value="${ index + 1 }" />
		   					<c:set var="menu2" value="${ menuItem2.entity }" />
		   					
		   					<li id="mw-core-menuListItem${ menu2.id }">
		   					<c:choose>
								<c:when test="${ menu2.typ == 'KNOTEN' }">
									<a>${ menu2.anzeigeName }</a>
				    			</c:when>
				    			<c:otherwise>
				    				<a href="<mw:url servletSubPath="${ menu2.ansichtDef.urlPath }" action="list" menue="${ menu1.id }" />">${ menu2.anzeigeName }</a>	
				    			</c:otherwise>
		    				</c:choose>
		    				
		    				<c:if test="${ menuItem2.hasChildren }">
		    					<!-- Ebene 3 -->
				   				<ul id="mw-core-ul-menuList3">
				   				<c:forEach items="${menuItem2.children}" var="menuItem3" >
				   					<c:set var="index" value="${ index + 1 }" />
				   					<c:set var="menu3" value="${ menuItem3.entity }" />
				   					
				   					<li id="mw-core-menuListItem${ menu3.id }">
				   					<c:choose>
										<c:when test="${ menu3.typ == 'KNOTEN' }">
						    				<a>${ menu3.anzeigeName }</a>
						    			</c:when>
						    			<c:otherwise>
						    				<a href="<mw:url servletSubPath="${ menu3.ansichtDef.urlPath }" action="list" menue="${ menu2.id }" />">${ menu3.anzeigeName }</a>	
						    			</c:otherwise>
				    				</c:choose>
				    				
				    				<c:if test="${ menuItem3.hasChildren }">
						    			<!-- Ebene 4 -->
						   				<ul id="mw-core-ul-menuList3">
						   				<c:forEach items="${menuItem3.children}" var="menuItem4" >
						   					<c:set var="index" value="${ index + 1 }" />
						   					<c:set var="menu4" value="${ menuItem4.entity }" />
						   					
						   					<li id="mw-core-menuListItem${ menu4.id }">
						   					<c:choose>
												<c:when test="${ menu4.typ == 'KNOTEN' }">
								    				<a>${ menu4.anzeigeName }</a>
								    			</c:when>
								    			<c:otherwise>
								    				<a href="<mw:url servletSubPath="${ menu4.ansichtDef.urlPath }" action="list" menue="${ menu3.id }" />">${ menu4.anzeigeName }</a>	
								    			</c:otherwise>
						    				</c:choose>
						    					<!-- no more ebene -->
						    				</li>
						    				
						   				</c:forEach>
						   				</ul>		
				    				</c:if>
				    				
				    				</li>
				    				
				   				</c:forEach>
				   				</ul>	
		    				</c:if>
		    				
		    				</li>
		    				
		   				</c:forEach>
		   				</ul>
    				</c:if>
    				
    				</li>
    				
   				</c:forEach>
   				</ul>
   			</c:if>
    		
    		</li>
		</c:forEach>
    	</ul>
    	</div>
    </div>
    
