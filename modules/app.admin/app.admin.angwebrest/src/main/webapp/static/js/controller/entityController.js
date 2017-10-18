'use strict';

angular.module('angWebApp').controller('EntityController', ['$scope', 'EntityService', function($scope, entityService) {
    var self = this;
    self.user={id:null,username:'',address:'',email:''};
    self.users=[];

    self.submit = submit;		// define submit-method to self-object and set javascript-reference to function submit below
    self.edit = edit;
    self.remove = remove;
    self.reset = reset;
    self.loadSlickAngularjsGrid = loadSlickAngularjsGrid;
	self.initialize = initialize;
	self.setRowDirty = setRowDirty;
	self.isRowDirty = isRowDirty;
	self.setCurrentRowIndex = setCurrentRowIndex;
	self.hasRowChanged = hasRowChanged;
    
    self.entity = { id:null, name:'', alias:'', benutzerbereich:'' };
    self.entities = [];
	var rowDirty = false;
	var currentRowIndex = 0;
	var rowChanged = false;
    
    $scope.state = {};
    $scope.state.rows = [];

    fetchAllEntities();
    
//    loadSlickAngularjsGrid();

    function loadSlickAngularjsGrid(){
    	
    	for(var i = 0; i < self.entities.uiEntities.length; i++){            
    		var uiEntity = self.entities.uiEntities[i];
    		var row = { "id": uiEntity.entityArray[0].id, "alias": uiEntity.entityArray[0].alias, "name": uiEntity.entityArray[0].name, "bezeichnung": uiEntity.entityArray[0].bezeichnung};
    		$scope.state.rows.push(row);    		
        }    	
    	
    }
    
    function fetchAllEntities(){
    	entityService.fetchAllEntities()
            .then(
            function(d) {
                self.entities = d;
				self.loadSlickAngularjsGrid();
            },
            function(errResponse){
                console.error('Error while fetching Entities');
            }
        );
    }
	
	function initialize( entityId ){
    	self.entity.id = entityId;
    }
	
	function setRowDirty() {
		rowDirty = true;
	}	
	
	function isRowDirty() {
		return rowDirty;
	}
	
	function setCurrentRowIndex( rowIndex ) {
		if(currentRowIndex !== rowIndex) {
			rowChanged = true;
		} else {
			rowChanged = false;
		}		
		currentRowIndex = rowIndex;
	}
	
	function hasRowChanged() {
		return rowChanged;
	}
    
    function createUser(user){
    	entityService.createUser(user)
            .then(
            fetchAllUsers,
            function(errResponse){
                console.error('Error while creating User');
            }
        );
    }
	
	function createEntity( entity ){
    	entityService.createEntity(user)
            .then(
            fetchAllEntities,
            function(errResponse){
                console.error('Error while creating Entity');
            }
        );
    }

    function updateUser(user, id){
    	entityService.updateUser(user, id)
            .then(
            fetchAllUsers,
            function(errResponse){
                console.error('Error while updating User');
            }
        );
    }
	
	function updateEntity( entity, id){
    	entityService.updateEntity( entity, id)
            .then(
            fetchAllEntities,
            function(errResponse){
                console.error('Error while updating Entity');
            }
        );
    }

    function deleteUser(id){
    	entityService.deleteUser(id)
            .then(
            fetchAllUsers,
            function(errResponse){
                console.error('Error while deleting User');
            }
        );
    }

    function submit() {
        if(self.entity.id===null){
            console.log('Saving New Entity', self.entity);
            createEntity( self.entity );
        }else{
            updateEntity(self.entity, self.entity.id);
            console.log('Entity updated with id ', self.entity.id);
        }
        reset();
    }

    function edit(id){
        console.log('id to be edited', id);
        for(var i = 0; i < self.users.length; i++){
            if(self.users[i].id === id) {
                self.user = angular.copy(self.users[i]);
                break;
            }
        }
    }

    function remove(id){
        console.log('id to be deleted', id);
        if(self.user.id === id) {//clean form if the user to be deleted is shown there.
            reset();
        }
        deleteUser(id);
    }


    function reset(){
        // self.user={id:null,username:'',address:'',email:''};
		self.entity = { id:null, name:'', alias:'', benutzerbereich:'' };
        $scope.myForm.$setPristine(); //reset Form
		rowDirty = false;
		rowChanged = false;
    }

}]);
