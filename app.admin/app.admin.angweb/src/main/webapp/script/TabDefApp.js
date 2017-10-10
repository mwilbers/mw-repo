/**
 * 
 */
var TabDef = ['$resource','context', function($resource, context) {
 return $resource(context + '/adminAngWeb/tabDef/tabDef/:id');
}];
 
app.factory('TabDef', TabDef);
 
var TabDefController = ['$scope','$state','TabDef',function($scope,$state,TabDef) {
 $scope.$on('$viewContentLoaded', function(event){
  $('html, body').animate({
      scrollTop: $("#tabDefs").offset().top
  }, 1000);
 });
 
 $scope.tabDefs = TabDef.query();
}];