'use strict';

App.factory('EntityService',  ['$http', '$q',  function($http, $q){

    var factory = {
        fetchAllEntities: fetchAllEntities,
        createEntity: createEntity,
        updateEntity:updateEntity,
        deleteUser:deleteUser
    };

    return factory;

    function fetchAllEntities( restUrl ) {
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
        return deferred.promise;
    }
    
    function createEntity( entity, restUrl ) {
        var deferred = $q.defer();
		
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
		
        $http.put(restUrl+ '/' + id, entity)
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
		
        $http.delete(restUrl+ '/' + id)
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
