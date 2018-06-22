package de.mw.mwdata.ofdb.impl;

import java.util.Comparator;

import de.mw.mwdata.core.to.OfdbField;

public class OfdbFieldComparator implements Comparator<OfdbField> {

	public int compare(final OfdbField o1, final OfdbField o2) {

		if (o1.getReihenfolge() < o2.getReihenfolge()) {
			return -1;
		} else if (o1.getReihenfolge() == o2.getReihenfolge()) {
			return 0;
		} else {
			return 1;
		}

	}

}
