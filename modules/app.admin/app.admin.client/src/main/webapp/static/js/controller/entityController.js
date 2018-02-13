'use strict';

function OfdbFieldEvaluator() {
	
	this.isShowColumn = function( ofdbField, gridPropertiesModel ) {
		
		if(ofdbField.mapped) {
			return true;
		} else {
			if(gridPropertiesModel.showNotMappedColumns) {
				return true;
			} else {
				return false;
			}
		}
		
	};
	
}

/**
 * Definition for routeProvider: Before loading EntityController and showing grid load all user 
 * specific server based properties for configuring client based constants
 *
 */
App.config(function($routeProvider){
  $routeProvider
    .when('/',{
		controller:'EntityController',
		templateUrl:'../static/includes/mwgrid_include.html',
		resolve:{
			'MyServiceData':function(AppConfigService){
				return AppConfigService.promise;
			}
		}})
  });

App.service('AppConfigService', function($http) {
    var appConfig = null;

	var promise = $http.get('userConfig/').success(function (data) {
		appConfig = data;
    });

    return {
      promise:promise,
      setData: function (data) {
    	  appConfig = data;
      },
      getApplicationConfig: function () {
          return appConfig;
      }
    };
});

App.controller('EntityController', ['$scope', 'EntityService', 'AppConfigService', function($scope, entityService, appConfigService) {
    console.log("entityController");
	
	var self = this;
	globalEntityController = self;
	
	if(null === appConfigService.getApplicationConfig()) {
			return;
	}
	
	var appConfig = [];
	console.log('Promise is now resolved: '+appConfigService.getApplicationConfig().data);
	appConfig['defaultRestUrl'] = appConfigService.getApplicationConfig().defaultRestUrl;
	
	self.submit = submit;		// define submit-method to self-object and set javascript-reference to function submit below
    self.edit = edit;
    self.remove = remove;
    self.reset = reset;
    self.loadGridRows = loadGridRows;
	self.initialize = initialize;
	self.setRowDirty = setRowDirty;
	self.isRowDirty = isRowDirty;
	self.setCurrentRowIndex = setCurrentRowIndex;
	self.hasRowChanged = hasRowChanged;
	self.processChange = processChange;
	self.setRows = setRows;
	self.fetchAllEntities = fetchAllEntities;
	self.setCurrentUrl = setCurrentUrl;

    self.entity = {};
    var rowDirty = false;
	var currentRowIndex = 0;
	var rowChanged = false;
	var currentUrl = null;
    
    $scope.state = {};
    $scope.state.rows = [];
	
	$scope.gridPropertiesModel = {
		showNotMappedColumns: false
	};
	
	$scope.reloadGrid = function() {
        mwGrid.clear();
		fetchAllEntities(); 
    };

    fetchAllEntities();
	
	
	function setRows( newRows ) {
		if(undefined === $scope.state) {
			$scope.state = {};
		}
		$scope.state.rows = newRows;
	}
	
	function setCurrentUrl( newUrl ) {
		currentUrl = newUrl;
	}
	
	function getCurrentUrl() {
		if(null !== currentUrl) {
			return currentUrl;
		} else {
			return appConfig['defaultRestUrl'];
		}
		
	}
	
    function loadGridRows( entityTOs, ofdbFields ){
    	
    	for(var i = 0; i < entityTOs.length; i++){            
    		var entityTO = entityTOs[i].item;
			
			var row = {};
			for(var j = 0; j < ofdbFields.length; j++) {
				if(ofdbFields[j].mapped) {
					row[ofdbFields[j].propName] = entityTO[ofdbFields[j].propName];
				} else {
					row[j] = "-";
				}
			}
			
			row["type"] = entityTO.type;
			
    		$scope.state.rows.push(row);    		
        }    	
    	
    }
    
    function fetchAllEntities(){
    	entityService.fetchAllEntities( getCurrentUrl() )
            .then(
            function(d) {
                console.log("ctrl.fetchAllEntities for " + getCurrentUrl());
				$scope.state.rows = [];
				
				mwGrid.initialize();
				loadGridRows( d.entityTOs, d.ofdbFields );
				mwGrid.load( $scope.state.rows, d.ofdbFields, $scope.gridPropertiesModel );
				
            },
            function(errResponse){
                console.error('Error while fetching Entities');
            }
        );
    }
	
	function initialize( entityId, entity ){
    	self.entity.id = entityId;
		self.entity = entity;
    }
	
	function setRowDirty() {
		rowDirty = true;
	}	
	
	function isRowDirty() {
		return rowDirty;
	}
	
	function setCurrentRowIndex( rowIndex ) {
		if(currentRowIndex !== rowIndex) {
			rowChanged = true;
		} else {
			rowChanged = false;
		}		
		currentRowIndex = rowIndex;
	}
	
	function hasRowChanged() {
		return rowChanged;
	}
    
	function processChange( entityId, entity ) {
	
		initialize( entityId, entity );
		setRowDirty();
		
		if( hasRowChanged() && isRowDirty() ) {
			submit();
			reset();
		}
	
	}
	
	function createEntity( entity ){
    	entityService.createEntity(entity, getCurrentUrl())
            .then(
            fetchAllEntities,
            function(errResponse){
                console.error('Error while creating Entity');
            }
        );
    }

	function updateEntity( entity, id){
    	entityService.updateEntity( entity, id, getCurrentUrl())
            .then(
            fetchAllEntities,
            function(errResponse){
                console.error('Error while updating Entity');
            }
        );
    }

    function deleteUser(id){
    	entityService.deleteUser(id, getCurrentUrl())
            .then(
            fetchAllUsers,
            function(errResponse){
                console.error('Error while deleting User');
            }
        );
    }

    function submit() {
		console.log("ctrl.submit");
		
        if(self.entity.id===null){
            console.log('Saving New Entity', self.entity);
            createEntity( self.entity );
        }else{
            updateEntity(self.entity, self.entity.id);
            console.log('Entity updated with id ', self.entity.id);
        }
        reset();
    }

    function edit(id){
        console.log('id to be edited', id);
        for(var i = 0; i < self.users.length; i++){
            if(self.users[i].id === id) {
                self.user = angular.copy(self.users[i]);
                break;
            }
        }
    }

    function remove(id){
        console.log('id to be deleted', id);
        if(self.user.id === id) {//clean form if the user to be deleted is shown there.
            reset();
        }
        deleteUser(id);
    }


    function reset(){
		self.entity = {};
        $scope.myForm.$setPristine(); //reset Form
		rowDirty = false;
		rowChanged = false;
    }

}]);

/**
 * globalEntityController is necessary for getting access to controller later in mwgrid
 */
var globalEntityController = null;



var mwGrid = new mwGrid();
var evaluator = new OfdbFieldEvaluator(  );