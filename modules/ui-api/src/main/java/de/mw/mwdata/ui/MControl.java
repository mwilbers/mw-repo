package de.mw.mwdata.ui;

/**
 * Common interface for defining ui-controls for IO-operations.
 * 
 * @author mwilbers
 * 
 */
public interface MControl {

	/**
	 * The ID of the control
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * The field type (e.g. input, textarea etc. ...)
	 * @return
	 */
	public String getFieldType();
	
	/**
	 * The name attribute of the control
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * The type-attribute of the control (e.g. text, radio etc.)
	 * 
	 * @return
	 */
	public Type getType();

	/**
	 * The label of the control
	 * 
	 * @return
	 */
	public String getLabel();

	/**
	 * Some debug-informations of the control
	 */
	public String getDebugInfo();
	
	/**
	 * The binding path of the control
	 * @return
	 */
	public String getPathName();
	
	/**
	 * The style class of the control
	 * @return
	 */
	public String getStyleClass();
	
	public boolean hasErrors();

}
