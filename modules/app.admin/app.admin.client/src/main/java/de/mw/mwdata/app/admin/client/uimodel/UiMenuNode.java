package de.mw.mwdata.app.admin.client.uimodel;

import de.mw.mwdata.core.web.uimodel.UiJsonConvertable;

public class UiMenuNode implements UiJsonConvertable {

	private String displayName;
	private String url; // url for loading children of current node
	private String restUrl; // url for requesting REST-API for loading entities
	private String nodeType;
	private long id;

	public boolean hasChildren() {
		return (this.nodeType != "ANSICHT" && this.nodeType != "AKTION" ? true : false);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRestUrl() {
		return restUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

}
