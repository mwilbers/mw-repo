package de.mw.mwdata.core.ofdb.exception;

public class OfdbInvalidConfigurationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8319676197444659754L;

	public OfdbInvalidConfigurationException(final String message) {
		super( message );
	}

	public OfdbInvalidConfigurationException(final String message, final Throwable exception) {
		super( message, exception );
	}

}
