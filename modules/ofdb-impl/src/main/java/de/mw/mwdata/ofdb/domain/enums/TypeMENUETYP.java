package de.mw.mwdata.ofdb.domain.enums;

import de.mw.mwdata.core.domain.FxEnumType;
import de.mw.mwdata.ofdb.domain.IMenue;
import de.mw.mwdata.ofdb.domain.IMenue.MENUETYP;

public class TypeMENUETYP extends FxEnumType<IMenue.MENUETYP> {

	private static final long serialVersionUID = 7990375707439507953L;

	protected TypeMENUETYP(Class<MENUETYP> c) {
		super(c);
	}

	public TypeMENUETYP() {
		super(MENUETYP.class);
	}

}
