/**
 * 
 */
package de.mw.mwdata.ofdb.exception;

/**
 * Exception that is thrown if
 * <ul>
 * <li>a domain-property has not TabSpeig-Mapping</li>
 * <li>a TabDef-object has a TabBez-entry but no mapping-dao</li>
 * </ul>
 * 
 * @author mwilbers
 * 
 */
public class OfdbMissingMappingException extends OfdbRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4941311775038042996L;

	public OfdbMissingMappingException(final String message) {
		super(message);

	}

}
