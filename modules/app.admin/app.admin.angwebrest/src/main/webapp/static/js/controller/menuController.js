
if (typeof module !== "undefined" && typeof exports !== "undefined" && module.exports === exports){
  module.exports = 'angWebApp';
}

angular.module('angWebApp', ['treeControl']).controller('MenuController', ['$http', '$timeout', function ($http, $timeout) {
  var ctrl = this;

  ctrl.loadingTime = 1500;
  ctrl.treeModel = [];
  ctrl.treeOptions = {
    dirSelectable: false,    // Click a folder name to expand (not select)
    isLeaf: function isLeafFn(node) {
      return !node.hasOwnProperty('url');
    }
  };

  function loadTreeModel( entityTOs ){
    	
		var treeModel = [];
    	  for(var i=0; i<entityTOs.length; i++) {
			
			var entity = entityTOs[i].item;
			var displayName = entity.anzeigeName + " ( " + entity.typ + " ) ";
			
			var currentNode = { "name": displayName, "url": "nav/menu/" + entity.id };
			treeModel[i] = currentNode;
			console.log('node json: ' + angular.toJson(currentNode));
			
		} 	
    	
		return treeModel;
    }
  
  ctrl.fetchChildNodes = function fetchChildNodes(node, expanded) {
    function doFetch(node) {
      if (node.hasOwnProperty('url')) {
        console.log('GET ' + node.url);
        $http.get(node.url)
          .success(function(data) {
            console.log('GET ' + node.url + ' ... ok! ');
			
			node.children = loadTreeModel( data.entityTOs );
            // node.children = data;
          });
      } else {
        // Leaf node
      }
    }

    if (node._sent_request) {
      return;
    }
    node._sent_request = true;
    // Add a dummy node.
    node.children = [{name: 'Loading ...'}];
    $timeout(function() { doFetch(node) }, ctrl.loadingTime);
  };

  //'../static/root.json'
  // 'http://localhost:8080/app.admin.angwebrest/admin/menues/860102'
  $http.get('http://localhost:8080/app.admin.angwebrest/admin/nav/')
    .success(function(data) {
      // ctrl.treeModel = data;
	  
	  ctrl.treeModel = [];
	  ctrl.treeModel = loadTreeModel( data.entityTOs );
	  
    });

}]);