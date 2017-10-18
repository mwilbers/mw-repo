/**
 * date: 29.04.2012
 * @author: Wilbers, Markus
 * 
 */

// create namespace mwUtils
var mwUtils = {};
/**
 * @author mwilbers
 * @returns the value of the given HTTP-Get-Parameter or false if not existent
 */
mwUtils.getParam = function(variable){  
    var query = window.location.search.substring(1);  
    var vars = query.split("&");  
	 for (var i=0;i<vars.length;i++) {  
	       var pair = vars[i].split("=");  
	       if(pair[0] == variable){return pair[1];}  
	 }       
     return(false);  
};