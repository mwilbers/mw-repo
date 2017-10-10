package de.mw.mwdata.core.web.tags;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.LocalizedMessages;
import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.ui.EntityTOPathBuilder;
import de.mw.mwdata.ui.MCheck;
import de.mw.mwdata.ui.NullBooleanEnum;
import de.mw.mwdata.ui.Type;

public class SpringRadioTag extends AbstractBaseSpringTag implements MCheck {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4232586035022493170L;
	
	private NullBooleanEnum booleanEnum;


	public SpringRadioTag(OfdbField ofProp, EntityTOPathBuilder pathBuilder,
			PageContext pageContext, final NullBooleanEnum booleanEnum) {
		super(ofProp, pathBuilder, pageContext, convertLabel(booleanEnum));
		this.booleanEnum = booleanEnum;
		initValue(booleanEnum);
		
	}
	
	private final static String convertLabel(final NullBooleanEnum booleanEnum) {
		
		switch(booleanEnum) {
			case TRUE: {
				return LocalizedMessages.getString(Constants.BUNDLE_NAME_COMMON, "mwdata.common.yes");
				
			}
			case FALSE: {
				return LocalizedMessages.getString(Constants.BUNDLE_NAME_COMMON, "mwdata.common.no");
				
			}
			case NULL: {
				return  LocalizedMessages.getString(Constants.BUNDLE_NAME_COMMON, "mwdata.common.null");
				
			} default: {
				return StringUtils.EMPTY;
			}
		}
		
	}
	
	private void initValue(final NullBooleanEnum booleanEnum) {
		
		switch(booleanEnum) {
		case TRUE: {
			setValue(Integer.toString(Constants.SYS_VAL_TRUE), Constants.SYS_VAL_TRUE_STRING);
			
			break;
		}
		case FALSE: {
			setValue(Integer.toString(Constants.SYS_VAL_FALSE), Constants.SYS_VAL_FALSE_STRING);
			break;
		}
		case NULL: {
			setValue(StringUtils.EMPTY, Constants.SYS_VAL_NULL_STRING);
			break;
		}
		}
		
	}


	public String getFieldType() {
		return "input";
	}


	public Type getType() {
		return Type.RADIO;
	}


	public String getStyleClass() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void writeOptionalAttributes(TagWriter tagWriter)
			throws JspException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void writeValue(TagWriter tagWriter) throws JspException {
		
		Object checkedValue = getCurrentValue();	
		
		Object trueValueCandidate = this.getValue(Integer.valueOf(Constants.SYS_VAL_TRUE).toString());
		Object falseValueCandidate = this.getValue(Integer.valueOf(Constants.SYS_VAL_FALSE).toString());
		
		if(ObjectUtils.nullSafeEquals(trueValueCandidate, Constants.SYS_VAL_TRUE_STRING)) {
			tagWriter.writeAttribute("value", Constants.SYS_VAL_TRUE_STRING);
			if(ObjectUtils.nullSafeEquals(checkedValue, Boolean.TRUE)) {
				tagWriter.writeAttribute("checked", "checked");
			}
		} else if(ObjectUtils.nullSafeEquals(falseValueCandidate, Constants.SYS_VAL_FALSE_STRING)) {
			tagWriter.writeAttribute("value", Constants.SYS_VAL_FALSE_STRING);
			if(ObjectUtils.nullSafeEquals(checkedValue, Boolean.FALSE)) {
				tagWriter.writeAttribute("checked", "checked");
			}
		} else {
			tagWriter.writeAttribute("value", Constants.SYS_VAL_NULL_STRING);
			if(null == checkedValue) {
				tagWriter.writeAttribute("checked", "checked");
			}
		}
		
	}


	public NullBooleanEnum getValue() {
		return this.booleanEnum;
	}


}
