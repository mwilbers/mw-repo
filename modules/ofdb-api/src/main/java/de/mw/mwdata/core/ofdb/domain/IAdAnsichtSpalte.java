package de.mw.mwdata.core.ofdb.domain;

import de.mw.mwdata.core.domain.IEntity;

public interface IAdAnsichtSpalte extends IEntity {

	public abstract void setAdAnsicht( IAdAnsicht adAnsicht );

	public abstract IAdAnsicht getAdAnsicht();

}