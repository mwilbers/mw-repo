package de.mw.mwdata.core.ofdb.domain.enums;

import de.mw.mwdata.core.db.FxEnumType;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig.DBTYPE;

public class TypeDATENTYP extends FxEnumType<ITabSpeig.DBTYPE> {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -7691260993415629048L;

	protected TypeDATENTYP(final Class<DBTYPE> c) {
		super( c );
		// TODO Auto-generated constructor stub
	}

	public TypeDATENTYP() {
		super( DBTYPE.class );
	}

}
