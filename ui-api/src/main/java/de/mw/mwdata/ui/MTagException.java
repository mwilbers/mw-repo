package de.mw.mwdata.ui;

public class MTagException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8098047966199618577L;

	public MTagException(final String message, final Throwable t) {
		super(message, t);
	}
	
	public MTagException(final String message) {
		super(message);
	}
	
}
