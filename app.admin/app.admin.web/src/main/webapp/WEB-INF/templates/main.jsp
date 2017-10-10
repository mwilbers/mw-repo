<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
  <head>
  	
  	<title>Main Page - <tiles:insertAttribute name="title" /></title>
  	<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/stylesheet.css"/>" />
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.jstree.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/mwMain.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/mwUtils.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/mwConf.js" />"></script>
     
  </head>
  <body>
  
  <script>
  	$(document).ready(
		function(){
			mwUI.registerMenuTree();
		}
	);
   </script>
  
    <table width="1200" height="800" cellspacing="0" cellpadding="0" border="1">
      <tr height="100" class="header">
        <td colspan="2">
           <tiles:insertAttribute name="header" />
        </td>
      </tr>
      <tr>
        <td valign="top" width="200">
           <tiles:insertAttribute name="menu" />
        </td>
        <td width="1000" valign="top">
           <tiles:insertAttribute name="content" />
        </td>
      </tr>
      
    </table>
  </body>
</html>
