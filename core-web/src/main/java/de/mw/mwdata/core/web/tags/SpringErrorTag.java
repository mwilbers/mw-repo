package de.mw.mwdata.core.web.tags;


import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.util.ObjectUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.tags.form.ErrorsTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import de.mw.mwdata.ui.EntityTOPathBuilder;
import de.mw.mwdata.ui.MControl;
import de.mw.mwdata.ui.MTag;
import de.mw.mwdata.ui.Type;

public class SpringErrorTag extends ErrorsTag implements MControl, MTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9175844592251184442L;

	private MControl originControl;
	
	public SpringErrorTag(final MControl originControl, final EntityTOPathBuilder pathBuilder, final PageContext pageContext) {
		
		this.originControl = originControl;
		setPageContext(pageContext);
		this.setCssClass("error");
	}

	@Override
	public String getId() {
		return originControl.getId() + ".errors";
	}
	
	public void startTag() {
		try {
			
			this.doStartTag();
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void endTag() {
		try {
			this.doEndTag();
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void renderDefaultContent(TagWriter tagWriter)
			throws JspException {
		
		tagWriter.startTag(getElement());
		writeDefaultAttributes(tagWriter);
		
		String delimiter = ObjectUtils.getDisplayString(evaluate("delimiter", getDelimiter()));
		List<ObjectError> errors = getBindStatus().getErrors().getAllErrors();
		
		for (int i = 0; i < errors.size(); i++) {

			String message = getRequestContext().getMessage(errors.get(i), false);			
			if (i > 0) {
				tagWriter.appendValue(delimiter);
			}
			tagWriter.appendValue(getDisplayString(message));
			
		}
		tagWriter.endTag();
		
	}

	public String getFieldType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDebugInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPathName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStyleClass() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getName()  {
		return this.originControl.getName() + ".errors";
	}

	public boolean hasErrors() {
		return false;
	}
	
	@Override
	protected boolean shouldRender() throws JspException {
		return true;
	}
	
}
