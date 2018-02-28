package de.mw.mwdata.ofdb.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewConfigValidationResultSet implements Iterable<ViewConfigValidationResult> {

	private List<ViewConfigValidationResult>	validationResultList	= new ArrayList<ViewConfigValidationResult>();

	private boolean								hasErrors				= false;

	public void addValidationResult( final String errorMessageKey, final String... args ) {

		ViewConfigValidationResult result = new ViewConfigValidationResult( errorMessageKey, args );
		this.validationResultList.add( result );

		if ( !this.hasErrors ) {
			this.hasErrors = (!result.isValid());
		}

	}

	private void addValidationResult( final ViewConfigValidationResult result ) {

		this.validationResultList.add( result );

		if ( !this.hasErrors ) {
			this.hasErrors = (!result.isValid());
		}
	}

	public boolean isEmpty() {
		return this.validationResultList.isEmpty();
	}

	// @Override
	public Iterator<ViewConfigValidationResult> iterator() {
		return this.validationResultList.iterator();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for ( ViewConfigValidationResult result : this ) {
			b.append( result.toString() );
		}

		return b.toString();
	}

	public boolean hasErrors() {
		return this.hasErrors;
	}

	public void merge( final ViewConfigValidationResultSet validationResultSet ) {
		for ( ViewConfigValidationResult result : validationResultSet ) {
			this.addValidationResult( result );
		}
	}

}
