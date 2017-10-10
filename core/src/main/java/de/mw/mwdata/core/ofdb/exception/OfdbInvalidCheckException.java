package de.mw.mwdata.core.ofdb.exception;

public class OfdbInvalidCheckException extends OfdbRuntimeException {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 3683919909694890623L;

	public OfdbInvalidCheckException(final String message) {
		super( message );
	}

	public OfdbInvalidCheckException(final String message, final Throwable cause) {
		super( message, cause );
	}

}
