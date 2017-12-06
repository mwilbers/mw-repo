'use strict';

var gridView = {
	grid: {},
	columns: [],
	data: [],

	options: {},
	scope: {},

	initialize: function( ofdbFields ) {
		console.log("gridView initialize");
		// this.scope = angular.element($("#controllerScope")).scope();
		
		this.columns = [];
		
		//for(var i = 0; i < ofdbFields.length; i++) {
		for(var i = 0; i < ofdbFields.length; i++) {
			this.columns[i] = {};
			this.columns[i] = {id: ofdbFields[i].propOfdbName, name: ofdbFields[i].columnTitle, field: ofdbFields[i].propName};
		}
		/*
		this.columns = [
			
			{id: ofdbFields[0].propOfdbName, name: ofdbFields[0].columnTitle, field: ofdbFields[0].propName},
			{id: ofdbFields[1].propOfdbName, name: ofdbFields[1].columnTitle, field: ofdbFields[1].propName}
			//{id: "duration", name: "Duration", field: "duration"}//,
			//{id: "%", name: "% Complete", field: "percentComplete", editor: Slick.Editors.Text},
			//{id: "start", name: "Start", field: "start"},
			//{id: "finish", name: "Finish", field: "finish"},
			//{id: "effort-driven", name: "Effort Driven", field: "effortDriven"}
		],
		  */
		this.options = {
			enableCellNavigation: true,
			enableColumnReorder: true,
			asyncEditorLoading: false,
            autoEdit: true, 
			editable: true
		},
		
		$(function () {
			var data = [];
			console.log("gridView $function ");
			//gridView.data = {};
			
			

			this.grid = new Slick.Grid("#innerGrid", gridView.data, gridView.columns, gridView.options);
			
			//this.grid.setSelectionModel(new Slick.CellSelectionModel());
			// grid.registerPlugin( new Slick.AutoTooltips({ enableForHeaderCells: true }) );
			//this.grid.render();
			//console.log(this.grid);
			
			this.grid.onClick.subscribe(function(e, args) {
				console.log("onClick");
				this.scope = angular.element($("#controllerScope")).scope(); //$scope.ctrl;
				var controller = this.scope.ctrl;
					
				controller.setCurrentRowIndex( args.row );
				
				if( controller.hasRowChanged() && controller.isRowDirty() ) {
					controller.submit();
					controller.reset();
				}
				
				this.scope.$apply();
			});
			
			this.grid.onCellChange.subscribe(function(e, args) {
                console.log("onCellChange");
				
				this.scope = angular.element($("#controllerScope")).scope();
                this.scope.state.rows = args.grid.getData();
				var controller = this.scope.ctrl;
				controller.initialize( args.grid.getData()[args.grid.getSelectedRows()[0]].id, args.grid.getData()[args.grid.getSelectedRows()[0]] );
				controller.setRowDirty();
				
				if( controller.hasRowChanged() && controller.isRowDirty() ) {
					controller.submit();
					controller.reset();
				}
				
                //$scope.$apply();
            });
			
		});
		
		
	},
	
	load: function( rows, ofdbFields ) {
			console.log("gridView load");
			
			for (var i = 0; i < rows.length; i++) {
				gridView.data[i] = {};
				
				for(var j=0; j < ofdbFields.length; j++) {
					gridView.data[i][ofdbFields[j].propName] = rows[i][ofdbFields[j].propName];
					//gridView.data[i][ofdbFields[j].propName] = rows[i][ofdbFields[j].propName];
				}
				/*
				gridView.data[i] = {
					
					alias: "xxx",
					//title: "Task " + i,
					duration: "5 days",
					percentComplete: Math.round(Math.random() * 100),
					start: "01/01/2009",
					finish: "01/05/2009",
					effortDriven: (i % 5 == 0)
					
				};
				*/
			}
			
			//for (var i = 0; i < rows.length; i++) {
				
				
				
				/*
				gridView.data[i] = {
					Tabelle: "yyy",
					Alias: "xxx"
				}
				*/
				//gridView.data[i] = new Array(); //gridView.data[i];
				//for(var j = 0; j < 2; j++) {
					
				//	gridView.data[i][ofdbFields[j].propOfdbName] = rows[i][ofdbFields[j].propName];
						/*
						duration: "5 days",
						percentComplete: Math.round(Math.random() * 100),
						start: "01/01/2009",
						finish: "01/05/2009",
						effortDriven: (i % 5 == 0)
						*/
					
				//}
				
			   
			//}
			
			this.grid = new Slick.Grid("#innerGrid", gridView.data, gridView.columns, gridView.options);
			this.grid.setSelectionModel(new Slick.CellSelectionModel());
			// grid.registerPlugin( new Slick.AutoTooltips({ enableForHeaderCells: true }) );
			this.grid.render();
			console.log(this.grid);
			
	}
	
}

// gridView.initialize();

angular.module('angWebApp').controller('EntityController', ['$scope', 'EntityService', function($scope, entityService) {
    console.log("entityController");
	
	var self = this;
    self.user={id:null,username:'',address:'',email:''};
    self.users=[];
	
    self.submit = submit;		// define submit-method to self-object and set javascript-reference to function submit below
    self.edit = edit;
    self.remove = remove;
    self.reset = reset;
    self.loadSlickAngularjsGrid = loadSlickAngularjsGrid;
	// self.initializeColumns = initializeColumns;
	self.initialize = initialize;
	self.setRowDirty = setRowDirty;
	self.isRowDirty = isRowDirty;
	self.setCurrentRowIndex = setCurrentRowIndex;
	self.hasRowChanged = hasRowChanged;
	
    self.entity = { id:null, name:'', alias:'', benutzerbereich:'' };
    var rowDirty = false;
	var currentRowIndex = 0;
	var rowChanged = false;
    
    $scope.state = {};
    $scope.state.rows = [];

    fetchAllEntities();
	
	/*
	function initializeColumns( ofdbFields ) {
		
		for(var i = 0; i < ofdbFields.length; i++){            
    	
		}
		
	}
	*/
	
    function loadSlickAngularjsGrid( entities ){
    	
    	for(var i = 0; i < entities.entityTOs.length; i++){            
    		var entityTO = entities.entityTOs[i].item;
    		var row = { "id": entityTO.id, "alias": entityTO.alias, "name": entityTO.name, "bezeichnung": entityTO.bezeichnung};
    		$scope.state.rows.push(row);    		
        }    	
    	
    }
    
    function fetchAllEntities(){
    	entityService.fetchAllEntities()
            .then(
            function(d) {
                console.log("ctrl.fetchAllEntities");
				
				gridView.initialize( d.ofdbFields );
				self.loadSlickAngularjsGrid( d );
				gridView.load( $scope.state.rows, d.ofdbFields );
				
            },
            function(errResponse){
                console.error('Error while fetching Entities');
            }
        );
    }
	
	function initialize( entityId, entity ){
    	self.entity.id = entityId;
		self.entity = entity;
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
		console.log("ctrl.submit");
		
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
