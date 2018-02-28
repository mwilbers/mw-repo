package de.mw.mwdata.ofdb.domain.enums;

import de.mw.mwdata.core.domain.FxEnumType;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabDef.DATENBANK;

public class TypeDATENBANK extends FxEnumType<ITabDef.DATENBANK> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5910434504585013915L;

	protected TypeDATENBANK(final Class<DATENBANK> c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	public TypeDATENBANK() {
		super(DATENBANK.class);
	}

}
