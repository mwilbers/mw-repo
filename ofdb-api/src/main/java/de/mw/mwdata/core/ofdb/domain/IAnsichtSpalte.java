package de.mw.mwdata.core.ofdb.domain;

import de.mw.mwdata.core.domain.IEntity;

public interface IAnsichtSpalte extends IEntity {

	public void setAnsichtDef( IAnsichtDef ansichtDef );

	public IAnsichtDef getAnsichtDef();

	public void setViewTab( IAnsichtTab viewTab );

	public IAnsichtTab getViewTab();

	/**
	 * 
	 * @return number of precisions shown in view
	 */
	public Integer getAnzahlNachkommastellen();
	

	/**
	 * 
	 * @return true if the view column can be filtered
	 */
	public Boolean getFilter();

	/**
	 * 
	 * @return true if to show in view
	 */
	public Boolean getInGridAnzeigen();

	/**
	 * 
	 * @return true if value must not be null in view
	 */
	public Boolean getEingabeNotwendig();

	/**
	 * 
	 * @return true if user can add new value on insert action
	 */
	public Boolean getBearbHinzufZugelassen();

	/**
	 * 
	 * @return true if user can change value on update action
	 */
	public Boolean getBearbZugelassen();

	/**
	 * 
	 * @return the name of the mapped table alias from the {@link AnsichtTab} entity
	 */
	public String getTabAKey();

	/**
	 * 
	 * @return the name of the column alias used in the view
	 */
	public String getSpalteAKey();

	public String getVerdeckenDurchTabAKey();

	public String getVerdeckenDurchSpalteAKey();

	public String getSuchwertAusTabAKey();

	public String getSuchwertAusSpalteAKey();

	public String getAnsichtSuchen();

	public void setTabSpEig(final ITabSpeig tabSpeig);

}