package de.mw.mwdata.core.ofdb.exception;

public class OfdbRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 397229644821632267L;

	public OfdbRuntimeException(final String message) {
		super( message );
	}

	public OfdbRuntimeException(final String message, final Throwable cause) {
		super( message, cause );
	}

}
