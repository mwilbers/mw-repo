'use strict';

function OfdbFieldEvaluator() {
	
	this.isShowColumn = function( uiInputConfig, appConfig ) {
		
		if(uiInputConfig.mapped) {
			return true;
		} else {
			if(appConfig.showNotMappedColumns) {
				return true;
			} else {
				return false;
			}
		}
		
	};
	
}

/**
 * Definition for routeProvider: Before loading EntityGridController and showing grid load all user 
 * specific server based properties for configuring client based constants
 *
 */
App.config(function($routeProvider){
  $routeProvider
    .when('/',{
		controller:'EntityGridController',
		templateUrl:'../static/includes/mwgrid_include.html',
		resolve:{
			'MyServiceData':function(AppConfigService){
				return AppConfigService.promise;
			}
		}})
  });
  

App.service('AppConfigService', function($http) {
    var appConfig = null;

	var promiseConfig = $http.get('userConfig/').success(function (data) {
		appConfig = data;
    });
	
    return {
      promise:promiseConfig,
      setData: function (data) {
    	  appConfig = data;
      },
      getApplicationConfig: function () {
          return appConfig;
      }
    };
});

App.controller('EntityGridController', ['$scope', 'EntityService', 'AppConfigService', function($scope, entityService, appConfigService) {
    console.log("EntityGridController");
	
	var self = this;
	globalEntityController = self;
	
	if(null === appConfigService.getApplicationConfig()) {
			return;
	}
	
	console.log('Promise is now resolved: '+appConfigService.getApplicationConfig().data);
	
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
	
    $scope.state = {};
    $scope.state.rows = [];
	
	$scope.appConfig = {
		showNotMappedColumns: appConfigService.getApplicationConfig().showNotMappedColumnsInGrid,
		currentUrl: appConfigService.getApplicationConfig().defaultRestUrl
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
		$scope.appConfig.currentUrl = newUrl;
	}
	
	function getCurrentUrl() {		
		return $scope.appConfig.currentUrl;		
	}
	
    function loadGridRows( entityTOs, uiInputConfigs ){
    	
    	for(var i = 0; i < entityTOs.length; i++){            
    		var entityTO = entityTOs[i].item;
			
			var row = {};
			for(var j = 0; j < uiInputConfigs.length; j++) {
				if(uiInputConfigs[j].mapped) {
					row[uiInputConfigs[j].propName] = entityTO[uiInputConfigs[j].propName];
				} else {
					row[j] = "-";
				}
			}
			
			row["type"] = entityTO.type;
			
    		$scope.state.rows.push(row);    		
        }    	
    	
    }
    
    function fetchAllEntities(otherController){
		
    	entityService.fetchAllEntities( getCurrentUrl() )
            .then(
            function(d) {
                console.log("ctrl.fetchAllEntities for " + getCurrentUrl());
				$scope.state.rows = [];
				
				if(d.uiInputConfigs.length == 0) {
					console.warn('Could not load Ofdb informations.');
				}
				
				if(undefined !== otherController) {
					otherController.initializeConfig( d.uiInputConfigs );
				}
				
				mwGrid.initialize();
				loadGridRows( d.entityTOs, d.uiInputConfigs );
				mwGrid.load( $scope.state.rows, d.uiInputConfigs, $scope.appConfig );
				
            },
            function(errResponse){
                console.error('Error while fetching Entities');
            }
        );
		
    }
	
	function initialize( entityId, entity ){
		self.entity = entity;
		self.entity.id = entityId;
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
            console.log('Error state: entity.id should not be null', self.entity);
            // createEntity( self.entity );
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
        // $scope.myForm.$setPristine(); //reset Form
		rowDirty = false;
		rowChanged = false;
    }

}]);

App.controller('EntityInsertController', ['$scope', 'EntityService', 'AppConfigService', function($scope, entityService, appConfigService) {
    console.log("EntityInsertController");
	
	var self = this;
	globalEntityInsertController = self;
	
	if(null === appConfigService.getApplicationConfig()) {
			return;
	}
	
	self.submit = submit;		// define submit-method to self-object and set javascript-reference to function submit below
	self.entity = {};
	self.initializeConfig = initializeConfig;
	
	$scope.entity = {};
	
	$scope.appConfig = {
		currentUrl: appConfigService.getApplicationConfig().defaultRestUrl,
		uiInputConfigs: appConfigService.getApplicationConfig().uiInputConfigs
	};

    function submit() {
		console.log("ctrl.submit");
	    createEntity( $scope.entity );
    }
	
	function initializeConfig( uiInputConfigs ) {
		// $scope.appConfig.currentUrl = appConfigService.getApplicationConfig().defaultRestUrl;
		$scope.appConfig.uiInputConfigs = uiInputConfigs;
	}

	function createEntity( entity ){
    	entityService.createEntity(entity, '')
            .then(
            fetchAllEntities,
            function(errResponse){
                console.error('Error while creating Entity');
            }
        );
    }

}]);

/**
 * globalEntityController is necessary for getting access to controller later in mwgrid
 */
var globalEntityController = null;
var globalEntityInsertController = null;

var mwGrid = new mwGrid();
var evaluator = new OfdbFieldEvaluator(  );