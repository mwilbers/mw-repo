package de.mw.mwdata.core.web.tags;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.ui.EntityTOPathBuilder;
import de.mw.mwdata.ui.MInput;
import de.mw.mwdata.ui.MTag;
import de.mw.mwdata.ui.Type;

public class SpringInputTag extends AbstractBaseSpringTag implements MInput, MTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -154154325948671449L;
	
	public SpringInputTag(final OfdbField ofProp, final EntityTOPathBuilder pathBuilder, final PageContext pageContext, final String label) {
		super(ofProp, pathBuilder, pageContext, label);
	}
	
	public Type getType() {
		return Type.INPUT;
	}

	public String getFieldType() {
		return "input";
	}

	public String getMaxLength() {
		return Integer.valueOf( this.getOfProp().getMaxlength() ).toString();
	}

	public boolean hasErrors() {
		
		Errors errors =null;
		try {
			errors = this.getBindStatus().getErrors();
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ( errors.getFieldErrorCount() > 0
				&& errors.getFieldError().getField().endsWith( this.getOfProp().getPropName() ) ) {
			return true;
		}
		return false;

	}

	@Override
	public void writeOptionalAttributes(TagWriter tagWriter) throws JspException {
		writeOptionalAttribute(tagWriter, InputTag.MAXLENGTH_ATTRIBUTE, getMaxLength());
		
	}

	
}
