package de.mw.mwdata.rest.service;

public class InvalidRestUrlException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3054670760357339466L;

	public InvalidRestUrlException(final String message, final Throwable t) {
		super(message, t);
	}

}
