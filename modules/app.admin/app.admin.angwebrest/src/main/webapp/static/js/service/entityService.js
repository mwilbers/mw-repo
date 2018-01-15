'use strict';

App.factory('EntityService',  ['$http', '$q',  function($http, $q){

	// FIXME: build dynamic restbased url
    // var REST_SERVICE_URI = userConfigService.getUserProperty('DEFAULT_REST_URL'); 
	// 'http://localhost:8080/app.admin.angwebrest/admin/menues/';

    var factory = {
        fetchAllEntities: fetchAllEntities,
        createEntity: createEntity,
        updateEntity:updateEntity,
        deleteUser:deleteUser
    };

    return factory;

    function fetchAllEntities( restUrl ) {
        var deferred = $q.defer();
		// var restUrl = user.value;
		//var restUrl = myService.getOptions(); //.getUserProperty('DEFAULT_REST_URL');
		
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
        return deferred.promise;
    }
    
    function createEntity( entity, restUrl ) {
        var deferred = $q.defer();
		// var restUrl = userConfigService.getUserProperty('DEFAULT_REST_URL');
        $http.post(restUrl, entity)
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


    function updateEntity( entity, id, restUrl) {
        var deferred = $q.defer();
		// var restUrl = userConfigService.getUserProperty('DEFAULT_REST_URL');
        $http.put(restUrl+id, entity)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while updating Entity');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

    function deleteUser(id, restUrl) {
        var deferred = $q.defer();
		// var restUrl = userConfigService.getUserProperty('DEFAULT_REST_URL');
        $http.delete(restUrl+id)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while deleting User');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

}]);
