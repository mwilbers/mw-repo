package de.mw.mwdata.core.ofdb.cache;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.LocalizedMessages;

public class ViewConfigValidationResult {

	public enum RESULT {
		VALID, INVALID;
	}

	private RESULT	result;

	private String	errorMessage;

	public ViewConfigValidationResult() {
		this.result = RESULT.VALID;
	}

	public ViewConfigValidationResult(final String errorMessageKey, final String... args) {

		// String msg = LocalizedMessages.getString( Config.BUNDLE_NAME, messageKey, args );

		this.errorMessage = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, errorMessageKey, args );
		this.result = RESULT.INVALID;
	}

	public void setErrorMessage( final String message ) {
		this.errorMessage = message;
		this.result = RESULT.INVALID;
	}

	public boolean isValid() {
		return this.result.equals( RESULT.VALID );
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append( this.errorMessage );
		return builder.toString();
	}

}
