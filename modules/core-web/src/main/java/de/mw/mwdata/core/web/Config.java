package de.mw.mwdata.core.web;

public class Config {

	public static final String	SERVLET_PATH				= "admin"; // FIXME: should be moved to web admin app

	// ************************** HTML-control-Configuration **************************** //

	public static final String	SELECTLIST_DEFAULTVALUE		= "";

	public static final String	SELECTLIST_DEFAULTLABLE		= "- keine Filter -";

	// **************************** Conroller-Configuration ***************************** //

	public static final String	PREFIX_FILTEROBJECT			= "filterObject";
	public static final String	SUFFIX_CONTROLLER			= "Controller";

	// @Deprecated
	// public static final String NAME_ENTITY_VALIDATOR = "genericEntityValidator";

	// **************************** Tiles-View-Definitions ***************************** //

	public static final String	TILES_VIEWDEF_LIST_ENTITIES	= "mwdata.listEntity";
	public static final String	TILES_VIEWDEF_EDIT_ENTITY	= "mwdata.editEntity";

	// **************************** Path-Definitions ***************************** //

	public static final String	PROPERTIES_PATH_ICONS		= "/de/mw/mwdata/core/web/icons.properties";

	// public static final String PAGE_FORBIDDEN = "";

}
