package de.mw.mwdata.core.ofdb.domain;

import org.apache.commons.lang.StringUtils;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.IFxEnum;

public interface IMenue extends IEntity {

	public enum MENUETYP implements IFxEnum {
		KNOTEN("Knoten"), AKTION("Aktion"), ANSICHT("Ansicht"), LADEN("Laden");

		private String	description;

		private MENUETYP(final String description) {
			this.description = description;
		}

		public void setDescription( final String description ) {
			this.description = description;
		}

		// @Override
		public String getDescription() {
			return this.description;
		}

		// // @Override
		// public IFxEnum getValue( final String value ) {
		// return MENUETYP.valueOf( value );
		// }

		public boolean isEmpty() {
			return StringUtils.isEmpty( getDescription() );
		}

		@Override
		public String toString() {
			return this.description;
		}

		public Object getName() {
			return this.name();
		}

	}
	
}