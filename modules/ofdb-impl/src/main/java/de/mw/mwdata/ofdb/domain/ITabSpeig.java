package de.mw.mwdata.ofdb.domain;

import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.core.domain.IEntity;

public interface ITabSpeig extends IEntity {

	public void setTabDef(ITabDef tabDef);

	public ITabDef getTabDef();

	public String getSpalte();

	public void setSpalte(String spalte);

	public Long getEindeutig();

	public void setEindeutig(Long eindeutig);

	public void setDbDatentyp(DBTYPE dbDatentyp);

	public DBTYPE getDbDatentyp();

	/**
	 * 
	 * @return true if value of this column has to be unique
	 */
	public boolean isEindeutig();

	public void setTabDefId(Long tabDefId);

	public Long getTabDefId();

	/**
	 * 
	 * @return the position for ordering
	 */
	public Long getReihenfolge();

	/**
	 * 
	 * @return true if value must not be null
	 */
	public Boolean getEingabeNotwendig();

	/**
	 * 
	 * @return the caption for showing column in view
	 */
	public String getSpaltenkopf();

	/**
	 * 
	 * @return true if column is set by system only
	 */
	public Boolean getSystemWert();

	/**
	 * 
	 * @return true if column can be edited by user
	 */
	public Boolean getBearbErlaubt();

	/**
	 * 
	 * @return the maximum value. Can be number or e.g. a max date
	 */
	public String getMaximum();

	/**
	 * 
	 * @return the minimum value. Can be number or e.g. a min date
	 */
	public String getMinimum();

	/**
	 * 
	 * @return true if the column has a special range of valid values
	 */
	public boolean isEnum();

	/**
	 * 
	 * @return common object for a default value if no value is given by user
	 */
	public String getDefaultWert();

	/**
	 * 
	 * @return true if column holds primary key
	 */
	public Boolean getPrimSchluessel();

}