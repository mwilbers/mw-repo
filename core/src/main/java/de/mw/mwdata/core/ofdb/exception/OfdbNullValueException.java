/**
 * 
 */
package de.mw.mwdata.core.ofdb.exception;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Oct, 2011
 * 
 */
public class OfdbNullValueException extends OfdbRuntimeException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2739638025282496405L;

	public OfdbNullValueException(final String message) {
		super( message );

	}

	public OfdbNullValueException(final String message, final Throwable t) {
		super( message, t );
	}

}
