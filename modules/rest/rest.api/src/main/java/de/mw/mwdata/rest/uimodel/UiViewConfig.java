package de.mw.mwdata.rest.uimodel;

import java.util.ArrayList;
import java.util.List;

public class UiViewConfig {

	private List<UiInputConfig> uiInputConfigs = new ArrayList<>();

	public UiViewConfig() {
	}

	public static UiViewConfig createEmptyUiViewConfig() {
		return new UiViewConfig();
	}

	public void addUiInputConfig(final UiInputConfig uiInputConfig) {
		this.uiInputConfigs.add(uiInputConfig);
	}

	public List<UiInputConfig> getUiInputConfigs() {
		return this.uiInputConfigs;
	}

}
