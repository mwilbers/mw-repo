/**
 * 
 */
package de.mw.mwdata.core.ofdb.exception;

/**
 * Base-class for all Fx-Ofdb-Exception-types.
 * 
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 * 
 */
public class OfdbException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5793167547261286798L;

	public OfdbException(final String message) {
		super( message );
	}

	public OfdbException(final String message, final Throwable cause) {
		super( message, cause );
	}

}
