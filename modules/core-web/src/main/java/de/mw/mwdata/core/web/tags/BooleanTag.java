/**
 * 
 */
package de.mw.mwdata.core.web.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import de.mw.mwdata.core.web.Config;

/**
 * @author mwilbers
 * 
 */
@Deprecated
public class BooleanTag extends SimpleTagSupport {

	private String	id;

	private String	itemValue;

	public void setItemValue( final String itemValue ) {
		this.itemValue = itemValue;
	}

	public void setId( final String id ) {
		this.id = id;
	}

	@Override
	public void doTag() throws JspException, IOException {

		JspWriter out = getJspContext().getOut();
		String tag = buildTag();
		out.write( tag );

	}

	private String buildTag() {
		StringBuffer buf = new StringBuffer();
		boolean value = Boolean.parseBoolean( this.itemValue );

		// TODO: property propValue noch einf�gen, dann hier checked="checked" einbauen

		int i = 1;
		buf.append( "<input id=\"" );
		buf.append( this.id + i );
		buf.append( "\" type=\"radio\" value=\"true\" name=\"" );
		buf.append( this.id );
		buf.append( "\" " + (this.itemValue.equalsIgnoreCase( "true" ) ? "checked=\"checked\"" : "") + " >true<br>\n" );
		i++;

		buf.append( "<input id=\"" );
		buf.append( this.id + i );
		buf.append( "\" type=\"radio\" value=\"false\" name=\"" );
		buf.append( this.id );
		buf.append( "\" " + (this.itemValue.equalsIgnoreCase( "false" ) ? "checked=\"checked\"" : "") + " >false<br>\n" );
		i++;

		buf.append( "<input id=\"" );
		buf.append( this.id + i );
		buf.append( "\" type=\"radio\" value=\"\" name=\"" );
		buf.append( this.id );
		buf.append( "\" " + (this.itemValue.isEmpty() ? "checked=\"checked\" >" : " >") );
		buf.append( Config.SELECTLIST_DEFAULTLABLE );
		buf.append( "<br>\n" );

		// true
		// <br>
		// <input id="system2" type="radio" value="false" name="system">
		// false
		// <br>
		// <input id="system3" type="radio" checked="checked" value="" name="system">
		// unknown
		// <br>

		// buf.append( "<form:radiobutton path=\"" );
		// buf.append( this.propName );
		// buf.append( "\" value=\"true\" />true<br>\n" );
		//
		// buf.append( "<form:radiobutton path=\"" );
		// buf.append( this.propName );
		// buf.append( "\" value=\"false\" />false<br>\n" );
		//
		// buf.append( "<form:radiobutton path=\"" );
		// buf.append( this.propName );
		// buf.append( "\" value=\"\" />unknown<br>\n" );

		return buf.toString();

	}

}
