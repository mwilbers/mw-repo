package de.mw.mwdata.ordb.query;

import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;

public class OfdbOrderSet {

	private ITabDef		orderTabDef;
	private ITabSpeig	orderTabSpeig;
	private String		orderDirection;

	public OfdbOrderSet(final ITabDef orderTabDef, final ITabSpeig orderTabSpeig, final String orderDirection) {
		this.orderTabDef = orderTabDef;
		this.orderTabSpeig = orderTabSpeig;
		this.orderDirection = orderDirection;
	}

	public ITabDef getOrderTabDef() {
		return this.orderTabDef;
	}

	public ITabSpeig getOrderTabSpeig() {
		return this.orderTabSpeig;
	}

	public String getOrderDirection() {
		return this.orderDirection;
	}

}
