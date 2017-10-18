/**
 * 
 */
var app = angular.module('entities', []);
// Create a controller with name personsList to bind to the html page.
app.controller('entitiesList', function ($scope, $http) {
	
	$http.get(
			'http://localhost:8080/app.admin.web/adminAngular/tabDef/listAngular'
	).then( function(response) { $scope.greeting = {"id":1,"content":"Hello, Markus!"}; } );
	
    // Makes the REST request to get the data to populate the grid.
//    $scope.refreshGrid = function (page) {
//        $http({
//            url: 'adminAngular/listAngular',
//            method: 'GET' //,
////            params: {
////                page: page,
////                sortFields: $scope.sortInfo.fields[0],
////                sortDirections: $scope.sortInfo.directions[0]
////            }
//        }).success(function (data) {
//            $scope.entities = data;
//        });
//    };
 
//    // Do something when the grid is sorted.
//    // The grid throws the ngGridEventSorted that gets picked up here and assigns the sortInfo to the scope.
//    // This will allow to watch the sortInfo in the scope for changed and refresh the grid.
//    $scope.$on('ngGridEventSorted', function (event, sortInfo) {
//        $scope.sortInfo = sortInfo;
//    });
 
//    // Watch the sortInfo variable. If changes are detected than we need to refresh the grid.
//    // This also works for the first page access, since we assign the initial sorting in the initialize section.
//    $scope.$watch('sortInfo', function () {
//        $scope.refreshGrid($scope.persons.currentPage);
//    }, true);
 
    // Initialize required information: sorting, the first page to show and the grid options.
//    $scope.sortInfo = {fields: ['id'], directions: ['asc']};
//    $scope.entities = {currentPage : 1};
//    $scope.gridOptions = {
//        data: 'entities.list',
//        useExternalSorting: true,
//        sortInfo: $scope.sortInfo
//    };
});