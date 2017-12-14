'use strict';

function SelectCellEditor(args) {
	var $select;
	var defaultValue;
	var scope = this;

	this.init = function() {

		var opt_values = [];
		if(args.column.options){
		  opt_values = args.column.options.split(',');
		}else{
		  opt_values ="yes,no".split(',');
		}
		var option_str = ""
		for( var i in opt_values ){
		  var v = opt_values[i];
		  option_str += "<OPTION value='"+v+"'>"+v+"</OPTION>";
		}
		$select = $("<SELECT tabIndex='0' class='editor-select'>"+ option_str +"</SELECT>");
		$select.appendTo(args.container);
		$select.focus();
	};

	this.destroy = function() {
		$select.remove();
	};

	this.focus = function() {
		$select.focus();
	};

	this.loadValue = function(item) {
		defaultValue = item[args.column.field];
		$select.val(defaultValue);
	};

	this.serializeValue = function() {
		if(args.column.options){
		  return $select.val();
		}else{
		  return ($select.val() == "yes");
		}
	};

	this.applyValue = function(item,state) {
		item[args.column.field] = state;
		
		var controller = angular.element($("#controllerScope")).scope().ctrl;
		controller.processChange( item["id"], item );
		
	};

	this.isValueChanged = function() {
		return ($select.val() != defaultValue);
	};

	this.validate = function() {
		return {
			valid: true,
			msg: null
		};
	};
	
	this.init();
}

function gridView() {
	var grid = {};
	var columns = [];
	this.data = [];

	var options = {};
	var scope = {};

	/* private functions */
	
	function buildColumns( ofdbFields ) {
		
		for(var i = 0; i < ofdbFields.length; i++) {
			if(ofdbFields[i].mapped) {
				columns[i] = {};
				
				columns[i]["id"] = ofdbFields[i].propOfdbName;
				columns[i]["name"] = ofdbFields[i].columnTitle;
				columns[i]["field"] = ofdbFields[i].propName;
				
				if(undefined != ofdbFields[i].listOfValues && null != ofdbFields[i].listOfValues) {
					var result = "";
					for(var l=0; l<ofdbFields[i].listOfValues.length; l++) {
						result += ofdbFields[i].listOfValues[l] + ",";
					}
					result = result.substring(0, result.length - 1);
					columns[i]["options"] = result;
					columns[i]["editor"] = SelectCellEditor;
				}
				
				// this.columns[i] = {id: ofdbFields[i].propOfdbName, name: ofdbFields[i].columnTitle, field: ofdbFields[i].propName};
			}
		}
		
		columns[ofdbFields.length] = {};
		columns[ofdbFields.length]["id"] = "type";
		columns[ofdbFields.length]["name"] = "type";
		columns[ofdbFields.length]["field"] = "type";
		
	}
	
	/* public functions */
	
	this.getColumns = function () {
		return columns;
	};
	
	this.initialize = function( ofdbFields ) {
		console.log("gridView initialize");
		
		buildColumns( ofdbFields );

		this.options = {
			enableCellNavigation: true,
			enableColumnReorder: false,
			asyncEditorLoading: false,
            autoEdit: true, 
			editable: true
		},
		
		$(function () {
			//var data = [];
			console.log("gridView $function ");

			this.grid = new Slick.Grid("#innerGrid", gridView.data, gridView.getColumns(), gridView.options);
			
			
			
		});
		
		
	};
	
	this.load = function( rows, ofdbFields ) {
		console.log("gridView load");
		
		// NOTE: because gridView.data references memory of rows we must not reinitialize data objects by '{}' every time
		if(rows.length > 0 && undefined == gridView.data[0]) {
			for (var i = 0; i < rows.length; i++) {
				gridView.data[i] = {};			
			}
		}
		
		for (var i = 0; i < rows.length; i++) {
			for(var j=0; j < ofdbFields.length; j++) {
				if(ofdbFields[j].mapped) {
					gridView.data[i][ofdbFields[j].propName] = rows[i][ofdbFields[j].propName];
				}
			}
			gridView.data[i]["type"] = rows[i]["type"];
			
		}
		
		this.grid = new Slick.Grid("#innerGrid", gridView.data, gridView.getColumns(), gridView.options);
		this.grid.setSelectionModel(new Slick.CellSelectionModel());
		
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
				
				controller.processChange( args.grid.getData()[args.grid.getSelectedRows()[0]].id, args.grid.getData()[args.grid.getSelectedRows()[0]] );
				/*
				controller.initialize( args.grid.getData()[args.grid.getSelectedRows()[0]].id, args.grid.getData()[args.grid.getSelectedRows()[0]] );
				controller.setRowDirty();
				
				if( controller.hasRowChanged() && controller.isRowDirty() ) {
					controller.submit();
					controller.reset();
				}
				*/
                this.scope.$apply();
            });
		
		this.grid.render();
		console.log(this.grid);
			
	};
	
}

var gridView = new gridView();

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
	self.processChange = processChange;
	
    self.entity = { id:null, name:'', alias:'', benutzerbereich:'' };
    var rowDirty = false;
	var currentRowIndex = 0;
	var rowChanged = false;
    
    $scope.state = {};
    $scope.state.rows = [];

    fetchAllEntities();
	
    function loadSlickAngularjsGrid( entityTOs, ofdbFields ){
    	
    	for(var i = 0; i < entityTOs.length; i++){            
    		var entityTO = entityTOs[i].item;
			
			var row = {};
			for(var j = 0; j < ofdbFields.length; j++) {
				if(ofdbFields[j].mapped) {
					row[ofdbFields[j].propName] = entityTO[ofdbFields[j].propName];
				}
				//gridView.data[i][ofdbFields[j].propName] = rows[i][ofdbFields[j].propName];
			}
			
			row["type"] = entityTO.type;
			
    		//var row = { "id": entityTO.id, "alias": entityTO.alias, "name": entityTO.name, "bezeichnung": entityTO.bezeichnung, "datenbank": entityTO.datenbank};
    		$scope.state.rows.push(row);    		
        }    	
    	
    }
    
    function fetchAllEntities(){
    	entityService.fetchAllEntities()
            .then(
            function(d) {
                console.log("ctrl.fetchAllEntities");
				
				gridView.initialize( d.ofdbFields );
				self.loadSlickAngularjsGrid( d.entityTOs, d.ofdbFields );
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
	
	function processChange( entityId, entity ) {
	
		initialize( entityId, entity );
		setRowDirty();
		
		if( hasRowChanged() && isRowDirty() ) {
			submit();
			reset();
		}
	
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
