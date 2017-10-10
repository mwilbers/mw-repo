package de.mw.mwdata.core.ofdb.query;

/**
 * Holds all information for a hsql compatible value. This is type and real data object.
 *
 * @author mwilbers
 *
 */
public class QueryValue {

	private ValueType	type;
	private String		data;

	public QueryValue(final String data, final ValueType type) {
		this.data = data;
		this.type = type;
	}

	public String getData() {
		return this.data;
	}

	public ValueType getType() {
		return this.type;
	}

}
