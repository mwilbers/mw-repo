package de.mw.mwdata.app.admin.client.uimodel;

import de.mw.mwdata.rest.service.uimodel.UiJsonConvertable;

/**
 * Class for transporting all systemwide and user specific properties from
 * backend to ui.<br>
 * FIXME: class should be redesigned / refactored, e.g. for transporting
 * key-value-based properties by a map<br>
 * 
 * @see http://magicmonster.com/kb/prg/java/spring/webmvc/jackson_custom.html
 *      write own objectMapper and serializer for UiUserConfig with maps
 *
 * @author WilbersM
 *
 */
public class UiUserConfig implements UiJsonConvertable {

	// FIXME: dynamice url
	// private static final String DEFAULT_REST_URL =
	// "http://localhost:8080/app.admin.client/admin/tabDef/";

	private String defaultRestUrl;
	private boolean showNotMappedColumnsInGrid;

	public String getDefaultRestUrl() {
		return this.defaultRestUrl;
	}

	public void setDefaultRestUrl(final String defaultRestUrl) {
		this.defaultRestUrl = defaultRestUrl;
	}

	public boolean isShowNotMappedColumnsInGrid() {
		return showNotMappedColumnsInGrid;
	}

	public void setShowNotMappedColumnsInGrid(boolean showNotMappedColumnsInGrid) {
		this.showNotMappedColumnsInGrid = showNotMappedColumnsInGrid;
	}

}
