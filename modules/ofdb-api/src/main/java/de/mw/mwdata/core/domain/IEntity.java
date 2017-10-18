/**
 * 
 */
package de.mw.mwdata.core.domain;

/**
 * Common interface for persistable entities in MWData. Every entity has an
 * unique id and an unique name. The unique name is important for mapping same
 * entities between different systems.<br>
 * FIXME: interface should be extended from JsonConvertible
 * 
 * @author Wilbers, Markus
 * @version 2.0
 * @since Mar, 2011
 * 
 */
public interface IEntity {

	/**
	 * 
	 * @return
	 */
	public String getName();

	public void setName(final String name);

	public Long getId();

	public void setId(final Long id);

	public String getSequenceKey();

	public boolean isInDB();

	// public String getTablename();

}
