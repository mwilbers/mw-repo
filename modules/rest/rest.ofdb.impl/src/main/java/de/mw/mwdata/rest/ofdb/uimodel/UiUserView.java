package de.mw.mwdata.rest.ofdb.uimodel;

import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.rest.uimodel.UiInputConfig;

public class UiUserView {

	private String name;
	private long userId;
	private long viewDefId;

	private List<UiInputConfig> uiInputConfigs = new ArrayList<>();

	public UiUserView() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getViewDefId() {
		return viewDefId;
	}

	public void setViewDefId(long viewDefId) {
		this.viewDefId = viewDefId;
	}

	public List<UiInputConfig> getUiInputConfigs() {
		return uiInputConfigs;
	}

	public void setUiInputConfigs(List<UiInputConfig> uiInputConfigs) {
		this.uiInputConfigs = uiInputConfigs;
	}

}
