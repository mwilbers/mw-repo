/**
 * 
 */
angular.module('angWebApp').directive('slickgridjs',  function(  ) {
    
    return {
        require: '?ngModel',
        restrict: 'E',
        replace: true,
		// controller: 'EntityController',
        template: '<div></div>',
        link: function($scope, element, attrs) {
            
            var grid;
            var data = [];
			var controller;
			// var injector = angular.injector([ 'ng', 'angWebApp' ]);
			// var controller = injector.get('entityController');
            //SETUP GRID COLUMN HEADERS
            var columns = [{
                id: "id",
                name: "ID",
                field: "id",
                width: 120,
                cssClass: "cell-title",
                editor: Slick.Editors.Text
            },
            {
                id: "alias",
                name: "Alias",
                field: "alias",
                width: 100,
                //editor: Slick.Editors.LongText
            },
            {
                id: "name",
                name: "Name",
                field: "name",
                editor: Slick.Editors.Text},
            {
                id: "bezeichnung",
                name: "Bezeichnung",
                field: "bezeichnung",
                width: 80,
                resizable: false,
                // formatter: Slick.Formatters.PercentCompleteBar,
                editor: Slick.Editors.Text},
            ];

            //SETUP GRID OPTIONS
            var options = {
                editable: true,
                //enableAddRow: true,
                enableCellNavigation: true,
                asyncEditorLoading: false,
                autoEdit: true                        
            };
            
            grid = new Slick.Grid("#angSlickGrid", [], columns, options);
            grid.setSelectionModel(new Slick.CellSelectionModel());
            
            console.log("Created Grid");
            console.log(grid);

            grid.onAddNewRow.subscribe(function(e, args) {
                console.log("onAddNewRow");
                var item = args.item;
                grid.invalidateRow(data.length);
                data.push(item);
                grid.updateRowCount();
                grid.render();
            });

            grid.onCellChange.subscribe(function(e, args) {
                console.log("onCellChange");
                $scope.state.rows = grid.getData();
				controller = $scope.ctrl;
				controller.initialize( grid.getData()[grid.getSelectedRows()[0]].id );
				controller.setRowDirty();
				
				if( controller.hasRowChanged() && controller.isRowDirty() ) {
					controller.submit();
					controller.reset();
				}
				
				
                $scope.$apply();
            });
			/**
			grid.onBeforeEditCell.subscribe(function(e, args) {
                console.log("onBeforeEditCell");
                $scope.$apply();
            });
			grid.onSelectedRowsChanged.subscribe(function(e, args) {
                console.log("onSelectedRowsChanged");
                $scope.$apply();
            });
			**/
			
			grid.onClick.subscribe(function(e, args) {
                console.log("onClick");
                // $scope.state.rows = grid.getData();
				controller = $scope.ctrl;
					
				controller.setCurrentRowIndex( args.row );
				
				if( controller.hasRowChanged() && controller.isRowDirty() ) {
					controller.submit();
					controller.reset();
				}
				// controller.initialize( grid.getData()[grid.getSelectedRows()[0]].id );
                $scope.$apply();
            });
			
			/**
			grid.onKeyDown.subscribe(function(e) {
                console.log("onKeyDown");
                // $scope.state.rows = grid.getData();
				controller = $scope.ctrl;
				// controller.initialize( grid.getData()[grid.getSelectedRows()[0]].id );
                $scope.$apply();
            });
			
			
			
			grid.onSelectedRowsChanged.subscribe(function(e) {
                console.log("onSelectedRowsChanged");
                // $scope.state.rows = grid.getData();
				controller = $scope.ctrl;
				// if(controller.isRowDirty()) {
				// 	controller.submit();
				// 	controller.reset();
				// }
                // $scope.$apply();
            });
          **/
            var redraw = function(newScopeData) {
                console.log("redraw");

                grid.setData(newScopeData);
                grid.render();
            };

            $scope.$watch(attrs.data, redraw, true); 
        }
    }
});