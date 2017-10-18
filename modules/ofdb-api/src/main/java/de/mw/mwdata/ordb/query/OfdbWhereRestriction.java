package de.mw.mwdata.ordb.query;

import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;

public class OfdbWhereRestriction {

	private ITabDef			whereTabDef;
	private ITabSpeig		whereTabSpeig;
	private OperatorEnum	whereOperator;
	private Object			whereValue;

	public OfdbWhereRestriction(final ITabDef whereTabDef, final ITabSpeig whereTabSpeig,
			final OperatorEnum whereOperator, final Object whereValue) {
		this.whereTabDef = whereTabDef;
		this.whereTabSpeig = whereTabSpeig;
		this.whereOperator = whereOperator;
		this.whereValue = whereValue;
	}

	// public void setWhereTabDef( final TabDef whereTabDef ) {
	// this.whereTabDef = whereTabDef;
	// }

	public ITabDef getWhereTabDef() {
		return this.whereTabDef;
	}

	// public void setWhereTabSpeig( final TabSpeig whereTabSpeig ) {
	// this.whereTabSpeig = whereTabSpeig;
	// }

	public ITabSpeig getWhereTabSpeig() {
		return this.whereTabSpeig;
	}

	// public void setWhereOperator( final String whereOperator ) {
	// this.whereOperator = whereOperator;
	// }

	public OperatorEnum getWhereOperator() {
		return this.whereOperator;
	}

	// public void setWhereValue( final Object whereValue ) {
	// this.whereValue = whereValue;
	// }

	public Object getWhereValue() {
		return this.whereValue;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append( this.getClass().getSimpleName() );
		b.append( " [ " );
		b.append( this.whereTabSpeig.getSpalte() );
		b.append( " " + this.whereOperator + " " );
		b.append( this.whereValue );
		b.append( " ] " );
		return b.toString();
	}

}
