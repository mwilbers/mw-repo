'use strict';

angular.module('angWebApp').factory('EntityService', ['$http', '$q', function($http, $q){

	// FIXME: build dynamic restbased url
    var REST_SERVICE_URI = 'http://localhost:8080/app.admin.angwebrest/admin/menues/';

    var factory = {
        fetchAllEntities: fetchAllEntities,
        createEntity: createEntity,
        updateEntity:updateEntity,
        deleteUser:deleteUser
    };

    return factory;

    function fetchAllEntities() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI)
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
    
    function createEntity( entity ) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI, entity)
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


    function updateEntity( entity, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI+id, entity)
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

    function deleteUser(id) {
        var deferred = $q.defer();
        $http.delete(REST_SERVICE_URI+id)
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
