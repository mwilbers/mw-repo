
function ColumnHandle() {
	var self = this;
	// var columns = cols;
	
	function buildListOfValuesString( listOfValuesArray, joinedProperty ) {
		var result = "";
		for(var l=0; l<listOfValuesArray.length; l++) {
			var listItem = listOfValuesArray[l];
			
			if(joinedProperty) {
				result += listItem[joinedProperty.propName] + ":" + listItem['id'] + ",";
			} else {
				result += listItem + ":" + listItem + ",";
			}
			
		}
		return result.substring(0, result.length - 1);	
	}
	
	self.initializeColumnButtonDelete =function () {
		var col = {};
		// columns[colIndex] = {};
		col["id"] = "btnDelete";
		col["name"] = "Delete";
		col["field"] = "btnDelete";
		col["width"] = 50;
		col["formatter"] = Slick.Formatters.DeleteButton;
		return col;
	};
	
	self.initializeColumnOfdbField = function( ofdbField ) {
		var col = {};				
		col["id"] = ofdbField.propName;
		col["name"] = ofdbField.columnTitle;
		
		if( ofdbField.joinedProperty ) {
			col["field"] = ofdbField.joinedProperty.entityName + "." + ofdbField.joinedProperty.propName;
		} else {
			col["field"] = ofdbField.propName;
		}
		
		if(undefined != ofdbField.listOfValues && null != ofdbField.listOfValues) {
			var result = buildListOfValuesString( ofdbField.listOfValues, ofdbField.joinedProperty );
			col["options"] = result;
			col["editor"] = SelectCellEditor;
		}
		
		return col;
	};
	
	self.initializeColumnType = function() {
		var col = {};
		col["id"] = "type";
		col["name"] = "type";
		col["field"] = "type";
		col["width"] = 1;
		
		return col;
	};
	
}

function mwGrid() {
	var self = this;
	var grid = {};
	var columns = [];
	var columnFilters = {};
	self.data = [];
	var dataView;

	var options = {};
	var scope = {};
	
	var columnHandle = new ColumnHandle();
	
	/* private functions */
	
	function initializeColumns( uiInputConfigs, appConfig ) {
		
		var columnCounter = 0;

		if(uiInputConfigs.length > 0) {
			var col = columnHandle.initializeColumnButtonDelete();
			columns[columnCounter] = col;
			columnCounter++;
		}
		
		for(var i = 0; i < uiInputConfigs.length; i++) {
			var ofdbField = uiInputConfigs[i];
	
			if( ofdbFieldHandle.isShowColumn( ofdbField, appConfig ) ) {
				var col = columnHandle.initializeColumnOfdbField( ofdbField );
				columns[columnCounter] = col;
				columnCounter++;
			} 
		}
		
		if(uiInputConfigs.length > 0) {
			var col = columnHandle.initializeColumnType();
			columns[columnCounter] = col;
			columnCounter++;
		}
		
	}

	/* public functions */
	
	self.getControllerScope = function () {
		return angular.element($("#controllerScope")).scope();
	};
	
	self.getColumns = function () {
		return columns;
	};
	
	self.getColumnByKey = function( key ) {
		return self.grid.getColumns()[self.grid.getColumnIndex(key)];
	};
	
	self.getColumnFilterValueByKey = function( key ) {
		if(undefined === self.getColumnFilters()[key]) {
			return null;
		} else {
			return self.getColumnFilters()[key];
		}
	};
	
	self.setColumnFilterValueByKey = function( key, value) {
		self.getColumnFilters()[key] = value;
	};
	
	self.getColumnFilters = function() {
		return columnFilters;
	};
	
	self.initialize = function() {
		console.log("mwGrid: initialize");
		
		// self.clear();

		this.options = {
			enableCellNavigation: true,
			enableColumnReorder: true, // #ViewLayout#
			asyncEditorLoading: false,
			showHeaderRow: true,
			headerRowHight: 30,
			explicitInitialization: true,
            autoEdit: true, 
			editable: true
		},
		
		$(function () {
			console.log("mwGrid: $function ");

			mwGrid.dataView = new Slick.Data.DataView();
			mwGrid.grid = new Slick.Grid("#innerGrid", mwGrid.dataView, mwGrid.getColumns(), mwGrid.options);
		});
		
		
	};
	
	self.filter = function (item) {
		
		var gridColumnFilters = mwGrid.getColumnFilters();
		for (var columnId in gridColumnFilters ) {
			
			if(columnId === 'type') {
				continue;
		    } else {
				var col = mwGrid.getColumnByKey(columnId);		
				if (item[col.id] === null) {
					return false;
				} else {					
					if( !JsUtils.compareValues(item[col.id], mwGrid.getColumnFilterValueByKey(columnId) ) ) {
						return false;
					}					
				}
		    }
		  
		}
		return true;
	};
	
	self.hasFilters = function() {		
		for(var key in self.getColumnFilters()) {	
			if(key === "type") {
				continue;
			}			
			return true;
		}		
		return false;
	};
	
	self.load = function( rows, uiInputConfigs, appConfig ) {
		console.log("mwGrid: load");
		
		initializeColumns( uiInputConfigs, appConfig );
		
		self.data = [];
		// NOTE: because mwGrid.data references memory of rows we must not reinitialize data objects by '{}' every time
		if(rows.length > 0 && undefined == self.data[0]) {
			for (var i = 0; i < rows.length; i++) {
				self.data[i] = {};			
			}
			
//			mwGrid.setColumnFilterValueByKey( 'type', rows[0].type);
			// mwGrid.getColumnFilters()['type'] = rows[0].type;
			
		}
		
		mwGrid.setColumnFilterValueByKey( 'type', appConfig.viewConfig.entityFullClassName);
		
		for (var i = 0; i < rows.length; i++) {
			var entityTO = rows[i];
			
			for(var j=0; j < uiInputConfigs.length; j++) {
				var ofdbField = uiInputConfigs[j];
				if( ofdbField.joinedProperty ) {
					entityTO[ofdbField.joinedProperty.entityName + "." + ofdbField.joinedProperty.propName] = entityTO[ofdbField.joinedProperty.entityName][ofdbField.joinedProperty.propName];		
				}
			}
			
			if(uiInputConfigs.length > 0) {
				entityTO["btnDelete"] = "";
			}
		}
		
		self.dataView = new Slick.Data.DataView();
		self.grid = new Slick.Grid("#innerGrid", self.dataView, self.getColumns(), self.options);
		self.grid.setSelectionModel(new Slick.CellSelectionModel());
		
		self.grid.onClick.subscribe(function(e, args) {
			console.log("mwgrid: onClick");
			globalEntityController.setCurrentRowIndex( args.row );
			
			if( globalEntityController.hasRowChanged() && globalEntityController.isRowDirty() ) {
				globalEntityController.submit();
				globalEntityController.reset();
				mwGrid.getControllerScope().$apply();
			} else if (args.grid.getColumns()[args.cell].field == 'btnDelete') {
			   // TODO: perform real delete
			   alert('delete not yet implemented');
			   // assume delete function uses data field id; simply pass args.row if row number is accepted for delete
			   mwGrid.dataView.deleteItem(args.grid.getDataItem(args.row).id);
			   args.grid.invalidate();
			   mwGrid.getControllerScope().$apply();
			}
			
			
		});
			
		self.grid.onCellChange.subscribe(function(e, args) {
			console.log("mwgrid: onCellChange");
			
			globalEntityController.setRows( args.grid.getData().getItems() );
			globalEntityController.processChange( args.item.id, args.item );
			
			mwGrid.getControllerScope().$apply();
		});
		
		self.dataView.onRowCountChanged.subscribe(function (e, args) {
			mwGrid.grid.updateRowCount();
			mwGrid.grid.render();
		});
		self.dataView.onRowsChanged.subscribe(function (e, args) {
		  mwGrid.grid.invalidateRows(args.rows);
		  mwGrid.grid.render();
		});
		
		$(self.grid.getHeaderRow()).delegate(":input", "change keyup", function (e, args) {
			
		  // NOTE: do not replace this by self here, because this in function body here references different objects ...
		  var columnId = $(this).data("columnId");
		  if (columnId != null && columnId !== undefined) {
			mwGrid.setColumnFilterValueByKey( columnId, $.trim($(this).val()) );
			globalEntityController.applyFilteredEntity( mwGrid.getColumnFilters() );
			
		  }
		});

		self.grid.onHeaderRowCellRendered.subscribe(function(e, args) {
			
			if( args.column.id === "type") {	// columnInvisible
				$(args.node).empty();
				
			} else if(args.column.options !== null && args.column.options !== undefined) {	// list of values
				$(args.node).empty();
				var selectNode = $("<select class='" + args.column.id + "_select'>")
				.data("columnId", args.column.id)
				.val(mwGrid.getColumnFilterValueByKey(args.column.id));
				selectNode.appendTo(args.node);
				
				var filterValue = null;
				if( mwGrid.hasFilters() ) {
					var filterValue = mwGrid.getColumnFilterValueByKey(args.column['id']);					
				}
				
				
				// empty option first
				$("<option value=''>---</option>").appendTo(selectNode);
				var options = args.column.options.split(',');
				for(var i=0; i<options.length; i++) {
					var idNamePair = options[i].split(':');
					var sSelected = ( JsUtils.compareValues(filterValue, idNamePair[1]) ? ' selected ': '' );
					$("<option value='" + idNamePair[1] + "' " + sSelected + ">" + idNamePair[0] + "</option>").appendTo(selectNode);
				}
				   
			} else if(args.column.id === "btnDelete") {	// button delete
				$(args.node).empty();
				$("<input type='submit' value='Filter' class='btn-primary' ng-disabled='gridFilterForm.$invalid'>")
				   .addClass('buttonGrid')
				   .appendTo(args.node);
				   
			} else if(args.column.id !== undefined) {
				$(args.node).empty();
				$("<input type='text'>")
				   .data("columnId", args.column.id)
				   .val(mwGrid.getColumnFilterValueByKey(args.column.id))
				   .appendTo(args.node);
			}
		});
		
		if(columns.length > 0) {
			self.grid.init();
			self.dataView.beginUpdate();
			self.dataView.setItems(rows);
			self.dataView.setFilter(self.filter);
			self.dataView.endUpdate();
			
			self.grid.render();
			console.log(self.grid);
		}
			
	};
	
	self.clear = function() {
		self.data = [];
		columns = [];
		columnFilters = {};
	}
	
}