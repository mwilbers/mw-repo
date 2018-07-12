
if (typeof module !== "undefined" && typeof exports !== "undefined" && module.exports === exports){
  module.exports = 'angWebApp';
}

App.controller('MenuController', ['$http', '$timeout', 'AppConfigService', function ($http, $timeout, appConfigService) {
  var ctrl = this;

  ctrl.loadingTime = 1500;
  ctrl.treeModel = [];
  
  ctrl.treeOptions = {
    dirSelectable: false,    // Click a folder name to expand (not select)
    isLeaf: function isLeafFn(node) {
      return !node.hasOwnProperty('url');
    }
  };

  function loadTreeModel( uiMenuNode ){
    	
		var treeModel = [];
		var entityTOs = [];
		  for(var i=0; i<uiMenuNode.length; i++) {
			
			var currentNode = {};
			currentNode["name"] = uiMenuNode[i].displayName;
			if(uiMenuNode[i].url !== null) {
				currentNode["url"] = uiMenuNode[i].url; //"nav/menu/" + uiEntity.getId();
			} 
			//else {
			if(uiMenuNode[i].restUrl !== null) {
				currentNode["restUrl"] = uiMenuNode[i].restUrl;
			}
				// globalEntityController.fetchAllEntities();
				/*
				$http.get('nav/').success(function(data) {
					ctrl.treeModel = [];
					var wEntities = new UiEntityResult( data );
					ctrl.treeModel = loadTreeModel( wEntities );
				});
				*/
			//}
			
			treeModel[i] = currentNode;
			// console.log('node json: ' + angular.toJson(currentNode));
			
		} 	
    	
		return treeModel;
  }
  
  ctrl.fetchChildNodes = function fetchChildNodes(node, expanded) {
    function doFetch(node) {
      if (node.hasOwnProperty('url')) {
		  
        console.log('GET ' + node.url);
        $http.get(node.url).success(function(data) {
            console.log('GET ' + node.url + ' ... ok! ');
			
			node.children = loadTreeModel( data );
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
  
  ctrl.callNode = function callNode(node, selected) {
	if(undefined !== node.restUrl) {
		globalEntityController.setCurrentUrl( node.restUrl );
		globalEntityController.fetchAllEntities( globalEntityInsertController );	
	}
  };

  $http.get('nav/')
    .success(function(data) {
	  ctrl.treeModel = [];	  
	  ctrl.treeModel = loadTreeModel( data );
    });

}]);