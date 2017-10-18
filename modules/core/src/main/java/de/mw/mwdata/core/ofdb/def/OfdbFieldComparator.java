package de.mw.mwdata.core.ofdb.def;

import java.util.Comparator;

public class OfdbFieldComparator implements Comparator<OfdbField> {

	public int compare( final OfdbField o1, final OfdbField o2 ) {

		if ( o1.getReihenfolge() < o2.getReihenfolge() ) {
			return -1;
		} else if ( o1.getReihenfolge() == o2.getReihenfolge() ) {
			return 0;
		} else {
			return 1;
		}

	}

}
