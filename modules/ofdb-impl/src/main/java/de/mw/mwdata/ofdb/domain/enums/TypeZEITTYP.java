package de.mw.mwdata.ofdb.domain.enums;

import de.mw.mwdata.core.domain.FxEnumType;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabDef.ZEITTYP;

public class TypeZEITTYP extends FxEnumType<ITabDef.ZEITTYP> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3921685341929097112L;

	public TypeZEITTYP(final Class<ZEITTYP> c) {
		super(c);

	}

	public TypeZEITTYP() {
		super(ZEITTYP.class);
	}

}
