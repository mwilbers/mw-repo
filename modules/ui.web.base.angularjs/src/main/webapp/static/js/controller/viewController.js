'use strict';

App.controller('ViewController', ['$scope', 'ViewService', 'AppConfigService', function($scope, viewService, appConfigService) {
    console.log("ViewController");
	
	var self = this;
	
	if(null === appConfigService.getApplicationConfig()) {
		return;
	}
	
	$scope.appConfig = {
		uiUserView: {
			name: '',
			userId: 1,
			uiInputConfigs : appConfigService.getApplicationConfig().uiInputConfigs,
			viewDefId: null
		}
	};

	self.createView = createView;
	
	function createView() {
		
		// $scope.appConfig.uiUserView.uiInputConfigs = $scope.appConfig.uiInputConfigs;
		
    	viewService.createView( $scope.appConfig.uiUserView, 'view/')
            .then(
            //fetchAllEntities,
            function(errResponse){
                console.error('Error while creating view');
            }
        );
		
	}
	
	viewService.fetchAllViews('view/1');
	
	// $http.get('view/1')
	// .success(function(data) {
		// alert('call of load views successfull');
	  // //self.treeModel = loadTreeModel( data );	  
	// });

}]);
