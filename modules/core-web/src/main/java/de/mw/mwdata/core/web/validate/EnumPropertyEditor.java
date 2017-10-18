/**
 * 
 */
package de.mw.mwdata.core.web.validate;

import java.beans.PropertyEditorSupport;
import de.mw.mwdata.core.domain.IFxEnum;
import de.mw.mwdata.core.web.Config;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Feb, 2011
 * 
 */
public class EnumPropertyEditor<E extends Enum<E>> extends PropertyEditorSupport {

	// public static String SELECTLIST_DEFAULTVALUE = "";

	private final Class<E>	enumType;

	public EnumPropertyEditor(final Class<E> enumType) {
		this.enumType = enumType;
	}

	@Override
	public String getAsText() {

		IFxEnum en = (IFxEnum) getValue();
		if ( null == getValue() ) { // if "-" (means : do not filter this
									// enum-property)
			return Config.SELECTLIST_DEFAULTVALUE;
			// } else if ( en.isEmpty() ) { // if NULL-entry of enum
			// return "";
		}
		return en.getDescription();

	}

	@Override
	public void setAsText( final String text ) throws IllegalArgumentException {

		if ( Config.SELECTLIST_DEFAULTVALUE.equals( text ) ) {
			// super.setValue( null );
			return;
			// } else if ( "".equals( text ) ) {
			// super.setValue( Enum.valueOf( this.enumType, "NULL" ) );
		} else {

			IFxEnum e = (IFxEnum) Enum.valueOf( this.enumType, text.trim().toUpperCase() );
			super.setValue( e );
		}
	}
}
