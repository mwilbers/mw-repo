package de.mw.mwdata.rest.uimodel;

import java.util.List;

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

	private String defaultRestUrl;
	private boolean showNotMappedColumnsInGrid;
	private List<UiInputConfig> uiInputConfigs;

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

	public List<UiInputConfig> getUiInputConfigs() {
		return this.uiInputConfigs;
	}

	public void setUiInputConfigs(List<UiInputConfig> uiInputConfigs) {
		this.uiInputConfigs = uiInputConfigs;
	}

}
