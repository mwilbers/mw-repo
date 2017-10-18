app.controller('galleryController', function($scope) {
    $scope.headingTitle = "Photo Gallery Items";
});

app.controller('contactusController', function($scope) {
    $scope.headingTitle = "Contact Info";
});

app.controller('tabDefController', function($scope, $http) {
    
	var config = {
		//params: tabDefId;
		//,
		//headers : {'Accept' : 'application/json'}
	};
	
	var url = "/app.admin.angweb3/tabDef/";
	$http.get(url).success( function(response) {
		$scope.tabDef = response;
	});
	
});
