package de.mw.mwdata.core.ofdb.domain;

import de.mw.mwdata.core.domain.IEntity;


public interface IAnsichtTab extends IEntity {

	public abstract IAnsichtDef getAnsichtDef();

	public abstract void setTabDef( ITabDef tabDef );

	public abstract ITabDef getTabDef();

	public abstract String findTableName();

	/**
	 * 
	 * @return a string representing an operator for joining two views
	 */
	public String getJoinTyp();

	/**
	 * 
	 * @return the name of the mapped table alias in this view table mapping set
	 */
	public Object getTabAKey();

	public String getJoin1SpalteAKey();

	public String getJoin2SpalteAKey();

}