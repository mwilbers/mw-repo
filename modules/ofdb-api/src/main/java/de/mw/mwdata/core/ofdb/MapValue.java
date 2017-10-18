package de.mw.mwdata.core.ofdb;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class MapValue implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5663024704210545368L;
	private String				value;

	public MapValue(final String value) {
		this.value = value;
	}

	public MapValue() {

	}

	public String getValue() {
		return this.value;
	}

	public void setValue( final String value ) {

		// ... hier fehler wenn Liste von Menues und dann filtern auf feld AnsichtDef "FX_Entwickler"
		if ( StringUtils.isEmpty( value ) ) {
			this.value = null;
		} else {
			this.value = value;
		}
	}

	@Override
	public String toString() {
		return this.value;
	}

}
