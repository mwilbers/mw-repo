package de.mw.mwdata.core.ofdb.domain.enums;

import de.mw.mwdata.core.db.FxEnumType;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabDef.ZEITTYP;

public class TypeZEITTYP extends FxEnumType<ITabDef.ZEITTYP> {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -3921685341929097112L;

	public TypeZEITTYP(final Class<ZEITTYP> c) {
		super( c );

	}

	public TypeZEITTYP() {
		super( ZEITTYP.class );
	}

}
