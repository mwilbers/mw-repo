/**
 * date: 05.02.2012
 * @author: Wilbers, Markus
 * 
 */

// create namespace mwUI
var mwUI = {};

/**
 * @author mwilbers
 * @returns the value of the given HTTP-Get-Parameter or false if not existent
 */
mwUI._construct = function()
{

	/**
	 * Activates for each treenode the toggling-mechanism by the jsTree-Module. 
	 * The nodes are identified by the HTML-ID 'mw-core-menuListItem<id>'.
	 */
	var activateTreeToggling = function() {

		//jsTree-Doc: http://www.jstree.com/documentation/core#
		// for each link-element of all list-items of the highest-level ul-List

		$("ul.mw-core-ul-menuList0").each(function(index){
			$(this).click(function() {
				// call jsTree-Action toggle_node to relevant list-element
				$("#mw-core-div-treeArea").jstree("toggle_node","#mw-core-menuListItem" + index);
			});
		});
	};
	
	/**
	 * Builds a string for the representing CSS-styleclass for the current selected jsTree-Item in the
	 * application. The current selected item is identified by the GET-Parameter 'menue'.
	 */
	var buildSelectedMenueListItemid = function() {
		
		//get the menu-dsid and set the relevant jsTree-Node opened
		var menuOpenedId = mwUtils.getParam("menue");
		var listItemOpened = "";
		if(menuOpenedId != false) {
			listItemOpened = "mw-core-menuListItem" + menuOpenedId;
		}
		
		return listItemOpened;
		
	};
	
	var activateTreeLinks = function() {
		
		// JsTree: seems to be a jsTree-Bug: look here: http://osdir.com/ml/jstree/2010-03/msg00027.html
        // by click on node get the underlying a.href-value and go there by javascript
		$("#mw-core-div-treeArea").bind("select_node.jstree", function (event, data) {
            var id = data.rslt.obj.attr("id");
            var a = $("#" + id + " > a").get(0);
            if(a.href != "") {
            	document.location.href= a.href;
            }
        });
		
	};
	
	/**
	 * Registers the jsTree-Module for the web-application. Is Called within jQuery-document-ready-function
	 * @see also main.jsp
	 */
	this.registerMenuTree = function () {
		
		activateTreeToggling();		
		listItemOpened = buildSelectedMenueListItemid();
		
		$("#mw-core-div-treeArea").jstree({
			plugins:["themes","html_data", "ui"],
			"core" : {
				"initially_open" : [ listItemOpened ]
			}
		});
		
		activateTreeLinks();
				
		// preload images for first application-call
		var imageJsTree = $('<img />').attr('src', mwConf.URL_ICON);
		
	};
};

mwUI._construct();
