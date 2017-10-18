/**
 * 
 */
package de.mw.mwdata.core.web.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import de.mw.mwdata.core.domain.IFxEnum;
import de.mw.mwdata.core.web.Config;

/**
 * @author mwilbers
 * 
 */
@Deprecated
public class SelectTag extends SimpleTagSupport {

	private String		id;

	private List<Enum>	items;		// <? extends IFxEnum>

	private Enum		itemValue;

	// private Class enumClass;

	// private String itemLabel;

	public void setId( final String id ) {
		this.id = id;
	}

	public void setItems( final List<Enum> items ) {
		this.items = items;

	}

	public void setItemValue( final Enum itemValue ) {
		this.itemValue = itemValue;
	}

	// public void setItemLabel( final String itemLabel ) {
	// this.itemLabel = itemLabel;
	// }

	@Override
	public void doTag() throws JspException, IOException {

		JspWriter out = getJspContext().getOut();
		String tag = buildTag();
		out.write( tag );

	}

	private String buildTag() {
		StringBuffer buf = new StringBuffer();
		IFxEnum fxItemValue = (IFxEnum) this.itemValue;
		String sel = "";

		buf.append( "<select id=\"" );
		buf.append( this.id );
		buf.append( "\" name=\"" );
		buf.append( this.id );
		buf.append( "\">\n" );

		buf.append( "	<option value=\"\" " + (null == fxItemValue ? "selected=\"selected\"" : "") + " />" );
		buf.append( Config.SELECTLIST_DEFAULTLABLE );
		buf.append( "</option>" );

		for ( Enum item : this.items ) {
			IFxEnum fxEnum = (IFxEnum) item;
			// IFxEnum val = (IFxEnum) Enum.valueOf( item.getClass(), item.name() );
			// fxEnum.getDescription().trim().toUpperCase()

			buf.append( "	<option value=\"" );
			buf.append( item.name() );

			if ( null != this.itemValue && item.name().equals( this.itemValue.name() ) ) {
				sel = "selected=\"selected\"";
			} else {
				sel = "";
			}

			buf.append( "\" " + sel + ">" );
			buf.append( fxEnum.getDescription() );
			buf.append( "</option>\n" );
		}

		buf.append( "</select>\n" );

		// buf.append( "<form:select path=\"" );
		// buf.append( this.path );
		// buf.append( "\">\n" );
		// buf.append( "<form:option value=\"\" label=\"-\" />\n" );
		// buf.append( "<form:options items=\"" );
		// buf.append( this.items );
		// buf.append( "\" " );
		// if ( !StringUtils.EMPTY.equals( this.itemValue ) ) {
		// buf.append( "itemValue=\"" );
		// buf.append( this.itemValue );
		// buf.append( "\" " );
		// }
		//
		// buf.append( "itemLabel=\"" );
		// buf.append( this.itemLabel );
		// buf.append( "\" />\n" );
		//
		// buf.append( "</form:select>\n" );

		// <form:select path="${ofProp.propName}">
		// <form:option value="" label="-" />
		// <form:options items="${ofProp.listOfValues}" itemValue="name" itemLabel="name" />
		// </form:select>

		return buf.toString();

	}
}
