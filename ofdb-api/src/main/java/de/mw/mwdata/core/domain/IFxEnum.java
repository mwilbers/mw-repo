/**
 * 
 */
package de.mw.mwdata.core.domain;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Feb, 2011
 * 
 */
public interface IFxEnum {

	/**
	 * An enum always has an additional description
	 * @param description
	 */
	public void setDescription( String description );

	/**
	 * 
	 * @return the description-value in view
	 */
	public String getDescription();

//	public String toString();

	/**
	 * 
	 * @return true if enum value is null er empty
	 */
	public boolean isEmpty();

	/**
	 * Returns the java-based enum-name of the enum-item
	 * 
	 * @return
	 */
	public Object getName();

}
