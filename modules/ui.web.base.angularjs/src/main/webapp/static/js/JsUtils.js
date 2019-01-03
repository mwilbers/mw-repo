
function JsUtils() {
	var self = this;
	
	self.compareValues = function(untypedValue, sCompareValue) {
		
		if( null === untypedValue && sCompareValue === null) {
			return true;
		} else  if(null === untypedValue && sCompareValue !== null) {
			return false;
		} else if( typeof untypedValue === "number" ) {
			if( untypedValue.toString() !== sCompareValue  ) {
				return false;
			}
			return true;
		} else {
			if( untypedValue.indexOf( sCompareValue ) === -1  ) {
				return false;
			}
			return true;
		}
		
	};
	
}