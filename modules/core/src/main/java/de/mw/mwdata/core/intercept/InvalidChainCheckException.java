package de.mw.mwdata.core.intercept;

public class InvalidChainCheckException extends RuntimeException {

	private static final long serialVersionUID = 3683919909694890623L;

	public InvalidChainCheckException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
