'use strict';

function gridView() {
	var grid = {};
	var columns = [];
	var columnFilters = {};
	this.data = [];
	var dataView;

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
					var result = buildListOfValuesString( ofdbFields[i].listOfValues );
					columns[i]["options"] = result;
					columns[i]["editor"] = SelectCellEditor;
				}
				
			}
		}
		
		columns[ofdbFields.length] = {};
		columns[ofdbFields.length]["id"] = "type";
		columns[ofdbFields.length]["name"] = "type";
		columns[ofdbFields.length]["field"] = "type";
		
	}
	
	function buildListOfValuesString( listOfValuesArray ) {
		var result = "";
		for(var l=0; l<listOfValuesArray.length; l++) {
			result += listOfValuesArray[l] + ",";
		}
		return result.substring(0, result.length - 1);	
	}
	
	function getControllerScope() {
		return angular.element($("#controllerScope")).scope();
	}
	
	/* public functions */
	
	this.getColumns = function () {
		return columns;
	};
	
	this.getColumnFilters = function() {
		return columnFilters;
	};
	
	this.initialize = function( ofdbFields ) {
		console.log("gridView initialize");
		
		buildColumns( ofdbFields );

		this.options = {
			enableCellNavigation: true,
			enableColumnReorder: false,
			asyncEditorLoading: false,
			showHeaderRow: true,
			headerRowHight: 30,
			explicitInitialization: true,
            autoEdit: true, 
			editable: true
		},
		
		$(function () {
			console.log("gridView $function ");

			this.dataView = new Slick.Data.DataView();
			this.grid = new Slick.Grid("#innerGrid", this.dataView, gridView.getColumns(), gridView.options);
		});
		
		
	};
	
	this.filter = function (item) {
		
		var gridFiltersArray = gridView.getColumnFilters();
		for (var columnId in gridFiltersArray ) {
			
		    if (columnId !== undefined && gridFiltersArray[columnId] !== "") {
				var c = gridView.grid.getColumns()[gridView.grid.getColumnIndex(columnId)];			
				if ( item[c.field].indexOf( gridFiltersArray[columnId] ) === -1  ) {
				    return false;
				}
		    }
		  
		}
		return true;
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
		
		this.dataView = new Slick.Data.DataView();
		this.grid = new Slick.Grid("#innerGrid", this.dataView, gridView.getColumns(), gridView.options);
		this.grid.setSelectionModel(new Slick.CellSelectionModel());
		
		this.grid.onClick.subscribe(function(e, args) {
			console.log("onClick");
			this.scope = getControllerScope();
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
			
			this.scope = getControllerScope();
			var dataview = args.grid.getData();
			this.scope.state.rows = dataview.getItems();
			
			var controller = this.scope.ctrl;
			controller.processChange( args.item.id, args.item );
			
			this.scope.$apply();
		});
			
		this.dataView.onRowCountChanged.subscribe(function (e, args) {
			gridView.grid.updateRowCount();
			gridView.grid.render();
		});
		this.dataView.onRowsChanged.subscribe(function (e, args) {
		  gridView.grid.invalidateRows(args.rows);
		  gridView.grid.render();
		});
		$(this.grid.getHeaderRow()).delegate(":input", "change keyup", function (e) {
		  var columnId = $(this).data("columnId");
		  if (columnId != null) {
			gridView.getColumnFilters()[columnId] = $.trim($(this).val());
			gridView.dataView.refresh();
		  }
		});
		this.grid.onHeaderRowCellRendered.subscribe(function(e, args) {
			
			if(args.column.id !== undefined) {
				$(args.node).empty();
				$("<input type='text'>")
				   .data("columnId", args.column.id)
				   .val(gridView.getColumnFilters()[args.column.id])
				   .appendTo(args.node);
			}
		});
		this.grid.init();
		this.dataView.beginUpdate();
		this.dataView.setItems(gridView.data);
		this.dataView.setFilter(this.filter);
		this.dataView.endUpdate();
		
		this.grid.render();
		console.log(this.grid);
			
	};
	
}

var gridView = new gridView();

angular.module('angWebApp').controller('EntityController', ['$scope', 'EntityService', function($scope, entityService) {
    console.log("entityController");
	
	var self = this;
    //self.user={id:null,username:'',address:'',email:''};
    //self.users=[];
	
    self.submit = submit;		// define submit-method to self-object and set javascript-reference to function submit below
    self.edit = edit;
    self.remove = remove;
    self.reset = reset;
    self.loadGridRows = loadGridRows;
	self.initialize = initialize;
	self.setRowDirty = setRowDirty;
	self.isRowDirty = isRowDirty;
	self.setCurrentRowIndex = setCurrentRowIndex;
	self.hasRowChanged = hasRowChanged;
	self.processChange = processChange;
	
    self.entity = {};
    var rowDirty = false;
	var currentRowIndex = 0;
	var rowChanged = false;
    
    $scope.state = {};
    $scope.state.rows = [];

    fetchAllEntities();
	
    function loadGridRows( entityTOs, ofdbFields ){
    	
    	for(var i = 0; i < entityTOs.length; i++){            
    		var entityTO = entityTOs[i].item;
			
			var row = {};
			for(var j = 0; j < ofdbFields.length; j++) {
				if(ofdbFields[j].mapped) {
					row[ofdbFields[j].propName] = entityTO[ofdbFields[j].propName];
				}
			}
			
			row["type"] = entityTO.type;
			
    		$scope.state.rows.push(row);    		
        }    	
    	
    }
    
    function fetchAllEntities(){
    	entityService.fetchAllEntities()
            .then(
            function(d) {
                console.log("ctrl.fetchAllEntities");
				$scope.state.rows = [];
				
				gridView.initialize( d.ofdbFields );
				self.loadGridRows( d.entityTOs, d.ofdbFields );
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
    	entityService.createEntity(entity)
            .then(
            fetchAllEntities,
            function(errResponse){
                console.error('Error while creating Entity');
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
		self.entity = {};
        $scope.myForm.$setPristine(); //reset Form
		rowDirty = false;
		rowChanged = false;
    }

}]);
