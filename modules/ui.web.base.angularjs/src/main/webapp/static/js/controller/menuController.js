
if (typeof module !== "undefined" && typeof exports !== "undefined" && module.exports === exports){
  module.exports = 'angWebApp';
}

App.controller('MenuController', ['$http', '$timeout', 'AppConfigService', function ($http, $timeout, appConfigService) {
  var self = this;

  self.loadingTime = 1;
  self.treeModel = [];
  self.expandedNodes = [];
  self.selectedNode = {};
  self.treeOptions = {
	multiSelection: false,
    dirSelectable: false,    // Click a folder name to expand (not select)
    isLeaf: function isLeafFn(node) { return !node.hasOwnProperty('url'); }
  };

  function loadTreeModel( uiMenuNodes, parentNode ){
    	
		var treeModel = [];
		for(var i=0; i<uiMenuNodes.length; i++) {
			
			var currentNode = loadSingleNode( uiMenuNodes[i] );
			
			treeModel[i] = currentNode;		

			if( uiMenuNodes[i].children.length > 0 ) {
				self.expandedNodes[self.expandedNodes.length] = currentNode;
				loadTreeModel( uiMenuNodes[i].children, currentNode );
			}
			
			if( parentNode ) {
				parentNode["children"] = treeModel;
			}
			
			if( uiMenuNodes[i].selected ) {
				self.selectedNode = currentNode;
			}
			
		} 	
    	
		return treeModel;
  }
  
  function loadSingleNode( uiMenuNode ) {
	  
	var currentNode = {};
	currentNode["name"] = uiMenuNode.displayName;
	if(uiMenuNode.url !== null) {
		currentNode["url"] = uiMenuNode.url; //"nav/menu/" + uiEntity.getId();
	} 
	if(uiMenuNode.restUrl !== null) {
		currentNode["restUrl"] = uiMenuNode.restUrl;
	}
	if(uiMenuNode.entityFullClassName !== null) {
		currentNode["entityFullClassName"] = uiMenuNode.entityFullClassName;
	}
	
	return currentNode;
  }
  
  function applyExpandedNodes( data ) {
	console.log('applyExpandedNodes');
	if(!data) {
		return;
	}
	
	for(var i = 0; i<= 0; i++) {
		self.expandedNodes[i] = data[i];
	}
	  
	  
  }
  
  self.getExpandedNodes = function() {
	  return self.expandedNodes;
  };
  self.getSelectedNode = function() {
	  return self.selectedNode;
  };
  
  self.fetchChildNodes = function (node, expanded) {
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
    $timeout(function() { doFetch(node) }, self.loadingTime);
  };

  
  self.callNode = function (node, selected) {
	if(undefined !== node.restUrl) {
		globalEntityController.applyViewProperties( node );
		globalEntityController.fetchAllEntities( globalEntityInsertController );	
	}
  };

  
  $http.get('nav/')
	.success(function(data) {
	  self.treeModel = loadTreeModel( data );	  
	});

}]);