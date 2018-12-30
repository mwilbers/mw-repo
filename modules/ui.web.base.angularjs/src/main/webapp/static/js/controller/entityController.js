'use strict';

function OfdbFieldHandle() {
	
	var self = this;
	self.isShowColumn = function( uiInputConfig, appConfig ) {
		
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
	
	self.hasListOfValues = function( ofdbField ) {
		return (undefined != ofdbField.listOfValues && null != ofdbField.listOfValues);
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
	
	console.log('Promise is now resolved: '+appConfigService.getApplicationConfig());
	
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
	self.applyFilteredEntity = applyFilteredEntity;

    self.entity = {};
	self.filteredEntity = null;
    var rowDirty = false;
	var currentRowIndex = 0;
	var rowChanged = false;
	
    $scope.state = {};
    $scope.state.entityRows = [];
	$scope.state.pagingModel = {};
	
	$scope.appConfig = {
		showNotMappedColumns: appConfigService.getApplicationConfig().showNotMappedColumnsInGrid,
		currentUrl: appConfigService.getApplicationConfig().defaultRestUrl,
		uiInputConfigs: appConfigService.getApplicationConfig().uiInputConfigs
	};
	
	$scope.reloadGrid = function() {
		// alert('reload');
        mwGrid.clear();
		fetchAllEntities( undefined, $scope.state.pagingModel.pageIndex, $scope.state.pagingModel.pageSize ); 
    };
	
	$scope.updatePagingModel = function( newPageIndex, newPageSize ) {
		$scope.state.pagingModel.pageIndex = newPageIndex;
		$scope.state.pagingModel.pageSize = newPageSize;
		$scope.reloadGrid();
	};

	$scope.submitFilter = function() {
		console.log('filterEntities');
		if(null !== self.filteredEntity ) {
			filterEntities( self.filteredEntity );
		}
	};
	
	$scope.hasEntityRows = function() {
		return $scope.state.entityRows.length > 0;
	};
	
    fetchAllEntities();
	
	function applyFilteredEntity( entity ) {
			self.filteredEntity = entity;
	}
	
	function setRows( newRows ) {
		if(undefined === $scope.state) {
			$scope.state = {};
		}
		$scope.state.entityRows = newRows;
	}
	
	function setCurrentUrl( newUrl ) {
		$scope.appConfig.currentUrl = newUrl;
	}
	
	function getCurrentUrl() {		
		return $scope.appConfig.currentUrl;		
	}
	
    function loadGridRows( entityTOs ){
    	
    	for(var i = 0; i < entityTOs.length; i++){            
    		var entityTO = entityTOs[i].item;
			
    		$scope.state.entityRows.push(entityTO);    		
        }    	
    	
    }
    
    function fetchAllEntities(otherController, newPageIndex, newPageSize){
		
		var pIndex = ( newPageIndex === undefined ? '1' : newPageIndex);
		var pSize = ( newPageSize === undefined ? '0' : newPageSize);
		
		var newUrl = replaceUrlParameter( getCurrentUrl(), "pageIndex", pIndex );
		newUrl = replaceUrlParameter( newUrl, "pageSize", pSize );
		
		console.log("fetchAllEntities for " + newUrl);
    	entityService.fetchAllEntities( newUrl )
            .then(
            function(d) {
                
				$scope.state.entityRows = [];
				
//				if( undefined === d.uiInputConfigs ) {
//					return;
//				}
				
				if(d.uiInputConfigs.length == 0) {
					console.warn('Could not load Ofdb informations.');
				}
				
				// #ViewLayout# here save uiInputConfigs in controller and get them later ...
				globalEntityInsertController.initializeConfig( d.uiInputConfigs );
				
				mwGrid.initialize();
				loadGridRows( d.entityTOs );
				mwGrid.load( $scope.state.entityRows, d.uiInputConfigs, $scope.appConfig );
				console.log(d.pagingModel.count);
				$scope.state.pagingModel = d.pagingModel;
            },
            function(errResponse){
                console.error('Error while fetching Entities');
            }
        );
		
    }
	
	function addServletPath( restUrl, path ) {
		return restUrl + '/' + path;
	}
	
	function replaceUrlParameter( url, paramName, paramValue ) {
		
		var newUrl = "";
		
		var pos = url.lastIndexOf("?");
		if(-1 === pos) {
			if(url.endsWith("/")) {
				newUrl = url.substring(0, url.length-2); // cut slash (/)
			} else {
				newUrl = url;
			}
			return newUrl + "?" + paramName + "=" + paramValue;  
		}

		var queryPart = url.substring(pos +1, url.length);
		var tokens = queryPart.split("&");
		var newQueryPart = "";
		var tokenFound = false;
		for(var i = 0; i<tokens.length; i++) {
			if(tokens[i].startsWith(paramName)) {
				pos = tokens[i].lastIndexOf("=");
				tokens[i] = tokens[i].substring(0, pos+1) + paramValue;
				tokenFound = true;
			}
		  
			newQueryPart = newQueryPart + tokens[i] + ( i === tokens.length-1 ? "" : "&");
		}
		if(!tokenFound) {
			newQueryPart = newQueryPart + "&"+ paramName + "=" + paramValue;
		}
		
		pos = url.lastIndexOf("?");
		newUrl = url.substring(0, pos+1) + newQueryPart;

		return newUrl;
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
		
		var restUrl = addServletPath( getCurrentUrl(), id );
		restUrl = replaceUrlParameter( restUrl, "pageIndex", $scope.state.pagingModel.pageIndex );
		restUrl = replaceUrlParameter( restUrl, "pageSize", $scope.state.pagingModel.pageSize );
		
    	entityService.updateEntity( entity, restUrl)
            .then(
            fetchAllEntities,
            function(errResponse){
                console.error('Error while updating Entity');
            }
        );
    }
	
	function filterEntities( filteredEntity ){
		
		var newUrl = replaceUrlParameter( getCurrentUrl(), "filter", "" );
		
    	entityService.filterEntities( self.filteredEntity, newUrl )
            .then(
            function(d) {
                console.log("ctrl.filterEntities for " + newUrl);
				$scope.state.entityRows = [];
				
				if(d.uiInputConfigs.length == 0) {
					console.warn('Could not load Ofdb informations.');
				}
				
				mwGrid.initialize();
				loadGridRows( d.entityTOs, d.uiInputConfigs );
				mwGrid.load( $scope.state.entityRows, d.uiInputConfigs, $scope.appConfig );
				console.log(d.pagingModel.count);
				$scope.state.pagingModel = d.pagingModel;
            },
            function(errResponse){
                console.error('Error while filtering Entities');
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
var ofdbFieldHandle = new OfdbFieldHandle();