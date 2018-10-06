
function SelectCellEditor(args) {
	var $select;
	var defaultValue;
	var scope = this;

	function parseOptionValue( option ) {
		var value = parseInt(option);
		if(isNaN( value )) {
			value = option;
		}
		
		return value;
	}
	
	this.init = function() {

		var optItems = [];
		if(args.column.options){
		  optItems = args.column.options.split(',');	// optItems = Abrechnung:42 ( ~ <name>:<id>)
		}else{
		  optItems ="yes,no".split(',');
		}
		var option_str = ""
		for( var i in optItems ){
		  var item = optItems[i].split(':');
		  var value = item[1];
		  var label = item[0];
		  
		  var selected = "";
		  if( String(args.item[args.column.id]) === value ) {
			  selected = " selected ";
		  }
		  
		  option_str += "<OPTION value='"+ value +"' " +selected+ ">"+ label +"</OPTION>";
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
		defaultValue = item[args.column.id];
		// defaultValue = item[args.column.field];
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
		// state can be 'value:value' or 'value:id'
		
		
		var id = parseOptionValue( state );

		item[args.column.id] = id;
		
		var options = args.container.childNodes[0];
		for(var i = 0; i< options.length; i++) {
			var currentId = parseOptionValue( options[i].value ); // parseInt( options[i].value );
			if( id === currentId ) {
				item[args.column.field] = options[i].label;
				break;
			}
		}
		
		// item[args.column.field] = state;		
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