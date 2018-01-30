

function mwGrid() {
	var grid = {};
	var columns = [];
	var columnFilters = {};
	this.data = [];
	var dataView;

	var options = {};
	var scope = {};
	
	/* private functions */
	
	function initializeColumns( ofdbFields, gridPropertiesModel ) {
		
		var columnCounter = 0;
		columns = [];	// FIXME: why to clear local columns array here ?
		for(var i = 0; i < ofdbFields.length; i++) {
			var ofdbField = ofdbFields[i];
			
			if( evaluator.isShowColumn( ofdbField, gridPropertiesModel ) ) {
				columns[columnCounter] = {};
				
				columns[columnCounter]["id"] = ofdbField.propOfdbName;
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
		
		columns[columnCounter] = {};
		columns[columnCounter]["id"] = "type";
		columns[columnCounter]["name"] = "type";
		columns[columnCounter]["field"] = "type";
		columnCounter++;
		
	}
	
	function buildListOfValuesString( listOfValuesArray ) {
		var result = "";
		for(var l=0; l<listOfValuesArray.length; l++) {
			result += listOfValuesArray[l] + ",";
		}
		return result.substring(0, result.length - 1);	
	}
	
	this.getControllerScope = function () {
		return angular.element($("#controllerScope")).scope();
	};
	
	
	
	/* public functions */
	
	this.getColumns = function () {
		return columns;
	};
	
	this.getColumnFilters = function() {
		return columnFilters;
	};
	
	this.initialize = function() {
		console.log("mwGrid initialize");

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
			console.log("mwGrid $function ");

			mwGrid.dataView = new Slick.Data.DataView();
			mwGrid.grid = new Slick.Grid("#innerGrid", mwGrid.dataView, mwGrid.getColumns(), mwGrid.options);
		});
		
		
	};
	
	this.filter = function (item) {
		
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
	
	this.load = function( rows, ofdbFields, gridPropertiesModel ) {
		console.log("mwGrid load");
		
		initializeColumns( ofdbFields, gridPropertiesModel );
		
		// NOTE: because mwGrid.data references memory of rows we must not reinitialize data objects by '{}' every time
		if(rows.length > 0 && undefined == this.data[0]) {
			for (var i = 0; i < rows.length; i++) {
				this.data[i] = {};			
			}
		}
		
		for (var i = 0; i < rows.length; i++) {
			
			for(var j=0; j < ofdbFields.length; j++) {
				var ofdbField = ofdbFields[j];
				if( evaluator.isShowColumn( ofdbField, gridPropertiesModel ) ) {
					this.data[i][ofdbField.propName] = rows[i][ofdbField.propName];
				}
				
			}
			this.data[i]["type"] = rows[i]["type"];
			
		}
		
		this.dataView = new Slick.Data.DataView();
		this.grid = new Slick.Grid("#innerGrid", this.dataView, this.getColumns(), this.options);
		this.grid.setSelectionModel(new Slick.CellSelectionModel());
		
		this.grid.onClick.subscribe(function(e, args) {
			console.log("onClick");
			globalEntityController.setCurrentRowIndex( args.row );
			
			if( globalEntityController.hasRowChanged() && globalEntityController.isRowDirty() ) {
				globalEntityController.submit();
				globalEntityController.reset();
			}
			
			mwGrid.getControllerScope().$apply();
		});
			
		this.grid.onCellChange.subscribe(function(e, args) {
			console.log("onCellChange");
			
			globalEntityController.setRows( args.grid.getData().getItems() );
			globalEntityController.processChange( args.item.id, args.item );
			
			mwGrid.getControllerScope().$apply();
		});
			
		this.dataView.onRowCountChanged.subscribe(function (e, args) {
			mwGrid.grid.updateRowCount();
			mwGrid.grid.render();
		});
		this.dataView.onRowsChanged.subscribe(function (e, args) {
		  mwGrid.grid.invalidateRows(args.rows);
		  mwGrid.grid.render();
		});
		$(this.grid.getHeaderRow()).delegate(":input", "change keyup", function (e) {
		  var columnId = $(this).data("columnId");
		  if (columnId != null) {
			mwGrid.getColumnFilters()[columnId] = $.trim($(this).val());
			mwGrid.dataView.refresh();
		  }
		});
		this.grid.onHeaderRowCellRendered.subscribe(function(e, args) {
			
			if(args.column.id !== undefined) {
				$(args.node).empty();
				$("<input type='text'>")
				   .data("columnId", args.column.id)
				   .val(mwGrid.getColumnFilters()[args.column.id])
				   .appendTo(args.node);
			}
		});
		this.grid.init();
		this.dataView.beginUpdate();
		this.dataView.setItems(this.data);
		this.dataView.setFilter(this.filter);
		this.dataView.endUpdate();
		
		this.grid.render();
		console.log(this.grid);
			
	};
	
	this.clear = function() {
		mwGrid.data = [];
		var cols = mwGrid.getColumns();
		cols = [];
		columnFilters = {};
	}
	
}