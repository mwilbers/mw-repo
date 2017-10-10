package de.mw.mwdata.core.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.ui.EntityTOPathBuilder;
import de.mw.mwdata.ui.MControl;
import de.mw.mwdata.ui.MTag;
import de.mw.mwdata.ui.MTagException;
import de.mw.mwdata.ui.Type;

public abstract class AbstractBaseSpringTag extends AbstractHtmlElementTag implements MControl, MTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6475137846382243248L;
	private OfdbField ofProp;
	private EntityTOPathBuilder pathBuilder;
	private String label;

	
	public AbstractBaseSpringTag(final OfdbField ofProp, final EntityTOPathBuilder pathBuilder, final PageContext pageContext, final String label) {
		this.ofProp = ofProp;
		this.pathBuilder = pathBuilder;
		setPageContext(pageContext);
		this.label = label;
	}
	
	protected EntityTOPathBuilder getPathBuilder() {
		return this.pathBuilder;
	}
	
	protected OfdbField getOfProp() {
		return this.ofProp;
	}
	
	public String getId() {
		return this.pathBuilder.getPropertyPath(this.ofProp);
	}

	public String getName() {
		return  this.pathBuilder.getPropertyPath(this.ofProp);
	}


	public abstract Type getType();
	
	public abstract String getFieldType();

	public abstract void writeOptionalAttributes(TagWriter tagWriter) throws JspException;

	public String getDebugInfo() {
		return this.ofProp.getDiagnose();
	}


	public String getPathName() {
		return this.pathBuilder.getPropertyPath(this.ofProp);
	}

	public String getStyleClass() {		
		return null;
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
				&& errors.getFieldError().getField().endsWith( this.ofProp.getPropName() ) ) {
			return true;
		}
		return false;
	}

	
	public void startTag() {
		try {
			
			this.doStartTag();
		} catch (JspException e) {
			throw new MTagException("SpringInputTag could not be started: ", e);
		}
		
	}

	public void endTag() {
		try {
			this.doEndTag();
		} catch (JspException e) {
			throw new MTagException("SpringInputTag could not be ended: ", e);
		}
	}
	
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag(this.getFieldType());
		
		writeDefaultAttributes(tagWriter);
		tagWriter.writeAttribute("type", getType().getDescription());
		writeValue(tagWriter);

		// custom optional attributes
		this.writeOptionalAttributes(tagWriter);

		tagWriter.endTag();
		
		tagWriter.startTag("label");
		tagWriter.writeAttribute("for",this.getId());
		tagWriter.appendValue(convertToDisplayString(this.getLabel()));
		tagWriter.endTag();
		
		
		return SKIP_BODY;
	}
	
	protected Object getCurrentValue() throws JspException {
		
		BeanPropertyBindingResult beanResult = (BeanPropertyBindingResult) this.getBindStatus().getErrors();
		BeanWrapper beanWrapper = (BeanWrapper) beanResult.getPropertyAccessor();
		return beanWrapper.getPropertyValue(this.getPropertyPath());		
//		String displayedValue = getDisplayString(value, getPropertyEditor());
		
	}
	
	/**
	 * Writes the '{@code value}' attribute to the supplied {@link TagWriter}.
	 * Subclasses may choose to override this implementation to control exactly
	 * when the value is written.
	 */
	protected void writeValue(TagWriter tagWriter) throws JspException {
		
		Object value = getCurrentValue();		
		String displayedValue = getDisplayString(value, getPropertyEditor());
		tagWriter.writeAttribute("value", processFieldValue(getName(), displayedValue, getType().getDescription()));
	}
	
	@Override
	protected String getPropertyPath() throws JspException {
		return getPathName();
	}
	
	@Override
	protected String getTitle() {
		return getDebugInfo();
	}
	
	public String getLabel() {
		return this.label;
	}
	
	@Override
	public String toString() {
		
		StringBuffer b = new StringBuffer();
		b.append("AbstractBaseSpringTag [ type='");
		b.append(this.getType().name());
		b.append("', fieldType='");
		b.append(this.getFieldType());
		b.append("', path='");
		b.append(this.getPathName());
		b.append("' ]");
		
		return super.toString();
	}

}
