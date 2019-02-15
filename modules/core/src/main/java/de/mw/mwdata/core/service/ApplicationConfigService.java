package de.mw.mwdata.core.service;

import de.mw.mwdata.core.Identifiable;

/**
 * Global configuration service for every app that serves system wide and for
 * user specific application properties
 *
 * @author WilbersM
 *
 */
public interface ApplicationConfigService extends Identifiable {

	public static final String KEY_APPLICATION_URL = "app.applicationUrl";
	public static final String KEY_USERAREA = "app.userArea";
	public static final String KEY_PAGESIZE_FOR_LOAD = "app.hibernate.pageSizeForLoad";
	public static final String KEY_DEFAULT_ENTITY = "app.defaultEntity";

	public static final String KEY_SHOW_NOT_MAPPED_COLS = "app.showNotMappedColumnsInGrid";
	public static final String KEY_SHOW_SYSTEM_COLUMNS = "app.showSystemColumnsInGrid";

	public String getPropertyValue(final String key);

	public void initApplication();

}
