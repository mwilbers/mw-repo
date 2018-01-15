<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>  
    <title>AngularJS based MWDATA Web Admin Interface</title>  
    <style>
      .username.ng-valid {
          background-color: lightgreen;
      }
      .username.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .username.ng-dirty.ng-invalid-minlength {
          background-color: yellow;
      }

      .email.ng-valid {
          background-color: lightgreen;
      }
      .email.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .email.ng-dirty.ng-invalid-email {
          background-color: yellow;
      }

    </style>
    <!-- simple angularjs readonly table -->
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
     <link rel="stylesheet" href="<c:url value='/static/css/app.css' />"></link>
	 
     <!-- slickgrid grid -->
     <link rel="stylesheet" href="<c:url value='/static/slickgrid/slick.grid.css' />">
     <link rel="stylesheet" href="<c:url value='/static/slickgrid/examples/examples.css' />">
	 <link rel="stylesheet" href="<c:url value='/static/jQuery/jquery-ui-1.8.16.custom.css' />">
     
	 
  </head>
  
  <!-- FIXME: build responsive design of html5: see https://www.youtube.com/watch?v=g2taIe7ZFUA -->
  
  <body ng-app="angWebApp" class="ng-cloak" >
  
  <div style="">
  
  
  
	<div class="generic-container" style="width:400px;display:inline-block;vertical-align:top;height:1000px;" >
		
		<div class='container'  ng-controller="MenuController as menuCtrl">
		  Fake Loading Time: <input type=number ng-model='menuCtrl.loadingTime'> ms
		  <br>
		  <br>
		  <treecontrol class="tree-classic"
			tree-model="menuCtrl.treeModel"
			on-node-toggle="menuCtrl.fetchChildNodes(node, expanded)"
			options="menuCtrl.treeOptions">
			{{node.name}}
		  </treecontrol>
		</div>
	  </div>
	  
	  
  
      <div class="generic-container" style="width:800px;display:inline-block;" ng-controller="EntityController as ctrl" id="controllerScope"  >
          <div class="panel panel-default">
              <div class="panel-heading"><span class="lead">Entity Registration Form </span></div>
              <div class="formcontainer">
                  <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
                      <input type="hidden" ng-model="ctrl.user.id" />
                      <div class="row">
                          <div class="form-group col-md-12">
                              <label class="col-md-2 control-lable" for="file">Name</label>
                              <div class="col-md-7">
                                  <input type="text" ng-model="ctrl.user.username" name="uname" class="username form-control input-sm" placeholder="Enter your name" required ng-minlength="3"/>
                                  <div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.uname.$error.required">This is a required field</span>
                                      <span ng-show="myForm.uname.$error.minlength">Minimum length required is 3</span>
                                      <span ng-show="myForm.uname.$invalid">This field is invalid </span>
                                  </div>
                              </div>
                          </div>
                      </div>
                        
                      
                      <div class="row">
                          <div class="form-group col-md-12">
                              <label class="col-md-2 control-lable" for="file">Address</label>
                              <div class="col-md-7">
                                  <input type="text" ng-model="ctrl.user.address" class="form-control input-sm" placeholder="Enter your Address. [This field is validation free]"/>
                              </div>
                          </div>
                      </div>

                      <div class="row">
                          <div class="form-group col-md-12">
                              <label class="col-md-2 control-lable" for="file">Email</label>
                              <div class="col-md-7">
                                  <input type="email" ng-model="ctrl.user.email" name="email" class="email form-control input-sm" placeholder="Enter your Email" required/>
                                  <div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.email.$error.required">This is a required field</span>
                                      <span ng-show="myForm.email.$invalid">This field is invalid </span>
                                  </div>
                              </div>
                          </div>
                      </div>

                      <div class="row">
                          <div class="form-actions floatRight">
                              <input type="submit"  value="{{!ctrl.user.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-sm" ng-disabled="myForm.$invalid">
                              <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm" ng-disabled="myForm.$pristine">Reset Form</button>
                          </div>
                      </div>
                  </form>
              </div>
          </div>
          
          <!-- simple angularjs readonly table -->
          <div class="panel panel-default">
                <!-- Default panel contents -->
              <div class="panel-heading"><span class="lead">List of Entities </span></div>
              <div class="tablecontainer">
                  <table class="table table-hover">
                      <thead>
                          <tr>
                              <th>ID.</th>
                              <th>Name</th>
                              <th>Tabelle</th>
                              <th>Benutzerbereich</th>
                              <th width="20%"></th>
                          </tr>
                      </thead>
                      <tbody>
                          <tr ng-repeat="u in ctrl.entities.uiEntities">
                              <td><span ng-bind="u.entityArray[0]['alias']"></span></td>
                              <td><span ng-bind="u.username"></span></td>
                              <td><span ng-bind="u.address"></span></td>
                              <td><span ng-bind="u.email"></span></td>
                              <td>
                              <button type="button" ng-click="ctrl.edit(u.id)" class="btn btn-success custom-width">Edit</button>  <button type="button" ng-click="ctrl.remove(u.id)" class="btn btn-danger custom-width">Remove</button>
                              </td>
                          </tr>
                      </tbody>
                  </table>
              </div>
          </div>
          
          <!-- slickgrid grid -->
		  
		  <div>
			  innerGrid Properties<br>
			  <input type="checkbox" ng-model="gridPropertiesModel.showNotMappedColumns" ng-change="reloadGrid()" id="ng-show-notMapped-Columns" />
			  Show not mapped columns and values<br>
		  </div>
		  <br>
		  inner grid<br>
          <div  style="display:inline-block; width:700px;" ng-view>
          
          </div>  
      </div>
	  
	  
	  
	 </div>
	  
      
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.js"></script>
	  <script src="<c:url value='/static/js/angular/angular-route.min.js' />"></script>
	  
	  <script src="<c:url value='/static/jQuery/jquery-1.7.min.js' />"></script>
	  <script src="<c:url value='/static/jQuery/jquery.event.drag-2.2.js' />"></script>
	  <script src="<c:url value='/static/jQuery/jquery-ui-1.8.16.custom.min.js' />"></script>
	  
	  
	  <script src="<c:url value='/static/slickgrid/slick.core.js' />"></script>
	  <script src="<c:url value='/static/slickgrid/slick.grid.js' />"></script>
	  <script src="<c:url value='/static/slickgrid/slick.editors.js' />"></script>
	  
	  <script src="<c:url value='/static/slickgrid/slick.formatters.js' />"></script>
	  <script src="<c:url value='/static/slickgrid/plugins/slick.cellrangedecorator.js' />"></script>
	  <script src="<c:url value='/static/slickgrid/plugins/slick.cellrangeselector.js' />"></script>
	  <script src="<c:url value='/static/slickgrid/plugins/slick.cellselectionmodel.js' />"></script>
	  
	  <script src="<c:url value='/static/slickgrid/slick.dataview.js' />"></script>	  
	  <script src="<c:url value='/static/js/slickgrid_addons/slick.selectcelleditor.js' />"></script>
	  <script src="<c:url value='/static/js/slickgrid_addons/mwgrid.js' />"></script>
	  
      <script src="<c:url value='/static/js/app.js' />"></script>
	  
	  <script src="<c:url value='/static/js/angular/angular-tree-control.js' />"></script>
	  <script src="<c:url value='/static/js/controller/menuController.js' />"></script>
	  <script src="<c:url value='/static/js/controller/entityController.js' />"></script>
	  <script src="<c:url value='/static/js/service/entityService.js' />"></script>
	  
	 
	  
  </body>
</html>