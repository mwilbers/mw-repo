package de.mw.mwdata.ofdb.domain;

import de.mw.mwdata.core.domain.IEntity;

public interface IAnsichtOrderBy extends IEntity {

	public void setAnsichtTab( IAnsichtTab ansichtTab );

	public IAnsichtTab getAnsichtTab();

	public void setAnsichtSpalten( IAnsichtSpalte ansichtSpalten );

	public IAnsichtSpalte getAnsichtSpalten();

	/**
	 * 
	 * @return the name of the column alias of the belonging table for ordering
	 */
	public String getSpalteAKey();

	/**
	 * 
	 * @return true if column should be sorted in ascending order
	 */
	public Boolean getAufsteigend();

}