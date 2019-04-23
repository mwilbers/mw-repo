<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
    <head>  
		<title>AngularJS based MWDATA Web Admin Interface</title>  
		<style>
		/*
		  .entity.ng-valid {
			  background-color: lightgreen;
		  }
		  .entity.ng-dirty.ng-invalid-required {
			  background-color: red;
		  }
		  .entity.ng-dirty.ng-invalid-maxlength {
			  background-color: yellow;
		  }*/
		  .markRequired {
			  color: red;
		  }
		  .mw-pagination {
			  margin-top: 10px;	
			  margin-right: 0px;
			  margin-bottom: 10px;
			  margin-left: 0px;			  
		  }
		  .pagination > li {
			  background: none;				  
		  }
		  .buttonGrid {
			  padding: 2px;
			  font-size: 10px;
			  width: 40px;
			  border-radius: 4px;
			  border: 1px solid transparent;
		  }
		  .columnInvisible {
			display: none;  
		  }
		  .expandedGrid {
			  width:100%;
			  height:300px;
		  }
		  .collapsedGrid {
			  width:100%;
			  height:100px;
		  }

		</style>
		<!-- simple angularjs readonly table -->
		<link rel="stylesheet" href="./../static/css/bootstrap.css">
		<link rel="stylesheet" href="./../static/css/app.css" >
		<link rel="stylesheet" href="./../static/css/tree-control.css" >
		 
		<!-- slickgrid grid -->
		<link rel="stylesheet" href="./../static/slickgrid/slick.grid.css">
		<link rel="stylesheet" href="./../static/slickgrid/examples/examples.css"> 
		<link rel="stylesheet" href="./../static/jQuery/jquery-ui-1.8.16.custom.css">
		 
		<script src="./../static/js/angular/angular.js"></script>
		<script src="./../static/js/angular/angular-route.min.js"></script>
	 
		<script src="./../static/jQuery/jquery-1.7.min.js"></script>
		<script src="./../static/jQuery/jquery.event.drag-2.2.js"></script>
		<script src="./../static/jQuery/jquery-ui-1.8.16.custom.min.js"></script>
		<script src="./../static/js/bootstrap/bootstrap.js"></script>
		<script src="./../static/js/bootstrap/ui-bootstrap-tpls-0.11.0.min.js"></script>
		  
		<script src="./../static/slickgrid/slick.core.js"></script>
		<script src="./../static/slickgrid/slick.grid.js"></script>
		<script src="./../static/slickgrid/slick.editors.js"></script>
		  
		<script src="./../static/slickgrid/slick.formatters.js"></script>
		  
		<script src="./../static/slickgrid/plugins/slick.cellrangedecorator.js"></script>
		<script src="./../static/slickgrid/plugins/slick.cellrangeselector.js"></script>
		<script src="./../static/slickgrid/plugins/slick.cellselectionmodel.js"></script>
		  
		<script src="./../static/slickgrid/slick.dataview.js"></script>	  
		<script src="./../static/js/slickgrid_addons/slick.selectcelleditor.js"></script>
		<script src="./../static/js/slickgrid_addons/mwgrid.js"></script>
		 
		<script src="./../static/js/app.js"></script>
		<script src="./../static/js/JsUtils.js"></script>		
		
		<script src="./../static/js/angular/angular-tree-control.js"></script>
		
		<script src="./../static/js/service/appConfigService.js"></script>
		<script src="./../static/js/service/entityService.js"></script>
		<script src="./../static/js/service/viewService.js"></script>
		<script src="./../static/js/controller/entityController.js"></script>
		<script src="./../static/js/controller/viewController.js"></script>
		<script src="./../static/js/controller/mainController.js"></script>		
		<script src="./../static/js/controller/menuController.js"></script>
	 
    </head>
  
  <!-- FIXME: build responsive design of html5: see https://www.youtube.com/watch?v=g2taIe7ZFUA -->
  
    <body ng-app="angWebApp" ng-controller="MainController" ng-view class="ng-cloak" >
		      
    </body>
</html>