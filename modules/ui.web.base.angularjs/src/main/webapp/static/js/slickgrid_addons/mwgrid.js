

function mwGrid() {
	var self = this;
	var grid = {};
	var columns = [];
	var columnFilters = {};
	self.data = [];
	var dataView;

	var options = {};
	var scope = {};
	
	/* private functions */
	
	function initializeColumns( uiInputConfigs, appConfig ) {
		
		var columnCounter = 0;
		columns = [];	// FIXME: why to clear local columns array here ?
		
		if(uiInputConfigs.length > 0) {
			columns[columnCounter] = {};
			columns[columnCounter]["id"] = "btnDelete";
			columns[columnCounter]["name"] = "Delete";
			columns[columnCounter]["field"] = "btnDelete";
			columns[columnCounter]["width"] = 50;
			columns[columnCounter]["formatter"] = Slick.Formatters.DeleteButton;
			columnCounter++;
		}
		
		for(var i = 0; i < uiInputConfigs.length; i++) {
			var ofdbField = uiInputConfigs[i];
	
			if( evaluator.isShowColumn( ofdbField, appConfig ) ) {
				columns[columnCounter] = {};
				columns[columnCounter]["id"] = ofdbField.propName;
				columns[columnCounter]["name"] = ofdbField.columnTitle;
				columns[columnCounter]["field"] = ofdbField.propName;
				if(undefined != ofdbField.listOfValues && null != ofdbField.listOfValues) {
					var result = buildListOfValuesString( ofdbField.listOfValues );
					columns[columnCounter]["options"] = result;
					columns[columnCounter]["editor"] = SelectCellEditor;
				}
				
				columnCounter++;
			} 
		}
		
		if(uiInputConfigs.length > 0) {
			columns[columnCounter] = {};
			columns[columnCounter]["id"] = "type";
			columns[columnCounter]["name"] = "type";
			columns[columnCounter]["field"] = "type";
			columns[columnCounter]["width"] = 1;
			columnCounter++;
		}
		
	}
	
	
	
	function buildListOfValuesString( listOfValuesArray ) {
		var result = "";
		for(var l=0; l<listOfValuesArray.length; l++) {
			result += listOfValuesArray[l] + ",";
		}
		return result.substring(0, result.length - 1);	
	}
	
	self.getControllerScope = function () {
		return angular.element($("#controllerScope")).scope();
	};
	
	
	
	/* public functions */
	
	self.getColumns = function () {
		return columns;
	};
	
	self.getColumnFilters = function() {
		return columnFilters;
	};
	
	self.initialize = function() {
		console.log("mwGrid: initialize");

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
			console.log("mwGrid: $function ");

			mwGrid.dataView = new Slick.Data.DataView();
			mwGrid.grid = new Slick.Grid("#innerGrid", mwGrid.dataView, mwGrid.getColumns(), mwGrid.options);
		});
		
		
	};
	
	self.filter = function (item) {
		
		var gridFiltersArray = mwGrid.getColumnFilters();
		for (var columnId in gridFiltersArray ) {
			
		    if (columnId !== undefined && gridFiltersArray[columnId] !== "") {
				var c = mwGrid.grid.getColumns()[mwGrid.grid.getColumnIndex(columnId)];			
				if (item[c.field] === null) {
					return false;
				} else if( item[c.field].indexOf( gridFiltersArray[columnId] ) === -1  ) {
				    return false;
				}
		    }
		  
		}
		return true;
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
			
			mwGrid.getColumnFilters()['type'] = rows[0]['type'];
			
		}
		
		for (var i = 0; i < rows.length; i++) {
			for(var j=0; j < uiInputConfigs.length; j++) {
				var ofdbField = uiInputConfigs[j];
				if( evaluator.isShowColumn( ofdbField, appConfig ) ) {
					self.data[i][ofdbField.propName] = rows[i][ofdbField.propName];
				}
				
			}
			
			if(uiInputConfigs.length > 0) {
				self.data[i]["type"] = rows[i]["type"];
				self.data[i]["btnDelete"] = "";
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
			} else if (args.grid.getColumns()[args.cell].field == 'btnDelete') {
			   // FIXME: perform real delete
			   alert('delete not yet implemented');
			   // assume delete function uses data field id; simply pass args.row if row number is accepted for delete
			   mwGrid.dataView.deleteItem(args.grid.getDataItem(args.row).id);
			   args.grid.invalidate();
			}
			
			mwGrid.getControllerScope().$apply();
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
		  if (columnId != null) {
			mwGrid.getColumnFilters()[columnId] = $.trim($(this).val());
			globalEntityController.applyFilteredEntity( mwGrid.getColumnFilters() );
			
		  }
		});
		
		
		self.grid.onHeaderRowCellRendered.subscribe(function(e, args) {
			
			if( args.column.id === "type") {
				$(args.node).empty();
				// columnInvisible
			} else if(args.column.id === "btnDelete") {
				$(args.node).empty();
				$("<input type='submit' value='Filter' class='btn-primary' ng-disabled='gridFilterForm.$invalid'>")
				   .addClass('buttonGrid')
				   .appendTo(args.node);
				   // <input type="submit" value="Filter" class="btn btn-primary btn-sm" ng-disabled="gridFilterForm.$invalid">
			} else if(args.column.id !== undefined) {
				$(args.node).empty();
				$("<input type='text'>")
				   .data("columnId", args.column.id)
				   .val(mwGrid.getColumnFilters()[args.column.id])
				   .appendTo(args.node);
			}
		});
		
		if(columns.length > 0) {
			self.grid.init();
			self.dataView.beginUpdate();
			self.dataView.setItems(self.data);
			self.dataView.setFilter(self.filter);
			self.dataView.endUpdate();
			
			self.grid.render();
			console.log(self.grid);
		}
			
	};
	
	self.clear = function() {
		mwGrid.data = [];
		var cols = mwGrid.getColumns();
		cols = [];
		columnFilters = {};
	}
	
}