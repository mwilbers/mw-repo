package de.mw.mwdata.core.web.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.core.web.Config;
import de.mw.mwdata.ui.EntityTOPathBuilder;
import de.mw.mwdata.ui.MSelect;
import de.mw.mwdata.ui.Type;

public class SpringSelectTag extends AbstractBaseSpringTag implements MSelect {


	/**
	 * FIXME: generate real id
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean emptyOption;

	public SpringSelectTag(OfdbField ofProp, EntityTOPathBuilder pathBuilder,
			PageContext pageContext, String label, boolean emptyOption) {
		super(ofProp, pathBuilder, pageContext, label);
		this.emptyOption = emptyOption;
	}

	public boolean hasEmptyOption() {
		return this.emptyOption;
	}

	public List<Object> getListOfValues() {
		return this.getOfProp().getListOfValues();
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public String getFieldType() {
		return "select";
	}

	@Override
	public void writeOptionalAttributes(TagWriter tagWriter)
			throws JspException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		
		tagWriter.startTag(getFieldType());
		writeDefaultAttributes(tagWriter);

		if(this.hasEmptyOption()) {
			writeOption(this.getCurrentValue(), Config.SELECTLIST_DEFAULTVALUE, tagWriter);
		}
		
		List<Object> items = getListOfValues();
		if (items != null) {
			if (!CollectionUtils.isEmpty(items)) {
				
				for (Object item : items) {
					writeOption(this.getCurrentValue(), item, tagWriter);
				}
				
			}
			tagWriter.endTag(true);
			
		}
		return SKIP_BODY;
		
	}

	private void writeOption(Object value, Object item, TagWriter tagWriter) throws JspException {
		
		tagWriter.startTag("option");
		
		
//		... unescape auf Filter auf Bereich = "GP Kündigung" testen
		String itemDisplayString = HtmlUtils.htmlUnescape(getDisplayString(item)); // actually we get toString value of Entity-object here
		String labelDisplayString = itemDisplayString; // getDisplayString(label);

		// allows render values to handle some strange browser compat issues.
		tagWriter.writeAttribute("value", itemDisplayString);
		
		// here we use toString() - method for values of Enums !
		String val = ( null == value ? StringUtils.EMPTY : value.toString());
		
		if (itemDisplayString.equals(val)) {
			tagWriter.writeAttribute("selected", "selected");
		}
		tagWriter.appendValue(labelDisplayString);
		tagWriter.endTag();
		
	}
	
	
}
