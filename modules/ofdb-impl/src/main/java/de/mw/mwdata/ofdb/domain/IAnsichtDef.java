package de.mw.mwdata.ofdb.domain;

import de.mw.mwdata.core.domain.IEntity;

public interface IAnsichtDef extends IEntity {

	public abstract void setUrlPath(String urlPath);

	public abstract String getUrlPath();

}