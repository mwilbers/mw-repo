'use strict';

App.factory('ViewService',  ['$http', '$q',  function($http, $q){

    var factory = {
        fetchAllViews: fetchAllViews,
		createView: createView
    };
		
    return factory;
	
    function fetchAllViews( restUrl ) {
        var deferred = $q.defer();

        $http.get(restUrl)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while fetching Entities');
                deferred.reject(errResponse);
            }
        );
		// ofdbFields = deferred.promise.ofdbFields;
        return deferred.promise;
    }
	
	function createView( uiUserView, restUrl ) {
        var deferred = $q.defer();
		
        $http.post(restUrl, uiUserView)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while creating Entity');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

}]);
