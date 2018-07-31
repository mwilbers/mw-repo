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

		</style>
		<!-- simple angularjs readonly table -->
		<link rel="stylesheet" href="./../static/css/bootstrap.css">
		<link rel="stylesheet" href="./../static/css/app.css"></link>
		 
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
		  
		<script src="./../static/js/angular/angular-tree-control.js"></script>
		<script src="./../static/js/service/entityService.js"></script>
		<script src="./../static/js/controller/entityController.js"></script>
		<script src="./../static/js/controller/menuController.js"></script>
	 
    </head>
  
  <!-- FIXME: build responsive design of html5: see https://www.youtube.com/watch?v=g2taIe7ZFUA -->
  
    <body ng-app="angWebApp" class="ng-cloak" >
  
		<div class="generic-container" style="width:300px;display:inline-block;vertical-align:top;height:1000px;" >		
			<div class='container' style="width:100%;" ng-controller="MenuController as menuCtrl">
			  <div class="panel-heading"><span class="lead">Menu</span></div>
				
			  <br>
			  <treecontrol class="tree-classic" on-selection="menuCtrl.callNode(node, selected)"
				tree-model="menuCtrl.treeModel" on-node-toggle="menuCtrl.fetchChildNodes(node, expanded)"
				options="menuCtrl.treeOptions">
				{{node.name}}
			  </treecontrol>
			</div>
		</div>
	
        <div class="generic-container" style="width:800px;display:inline-block;"  >
            <div class="panel panel-default">
          
				<!-- bootstrap example for showing modal info panel with ofdb relevant validation infos -->
				<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
				  Launch demo modal
				</button>
				
				<!-- Modal -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				  <div class="modal-dialog" role="document">
					<div class="modal-content">
					  <div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">Example: Ofdb validation results</h4>
					  </div>
					  <div class="modal-body">
						...
					  </div>
					  <div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="button" class="btn btn-primary">Save changes</button>
					  </div>
					</div>
				  </div>
				</div>
          
				<div class="panel-heading"><span class="lead">Entity Registration Form</span></div>
				<div class="formcontainer" >
					<div ng-view ng-controller="EntityGridController as ctrl" id="controllerScope">
						
					</div>
				</div>
            </div>
          
            <!-- slickgrid grid -->
		    
        </div>      
    </body>
</html>