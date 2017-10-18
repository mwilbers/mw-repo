package de.mw.mwdata.core.ofdb;

import java.util.List;

public class OfdbUtils {

	public static SortKey findSortKeyByColumnName( final List<SortKey> sortKeys, final String colName ) {

		for ( SortKey key : sortKeys ) {
			if ( key.getSortColumn().equals( colName ) ) {
				return key;
			}
		}

		return null;
	}

}
