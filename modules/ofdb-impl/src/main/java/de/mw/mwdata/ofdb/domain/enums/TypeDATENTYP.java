package de.mw.mwdata.ofdb.domain.enums;

import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.core.domain.FxEnumType;

public class TypeDATENTYP extends FxEnumType<DBTYPE> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7691260993415629048L;

	protected TypeDATENTYP(final Class<DBTYPE> c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	public TypeDATENTYP() {
		super(DBTYPE.class);
	}

}
