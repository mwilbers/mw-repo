package de.mw.mwdata.core.web.tags;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.ui.MControl;
import de.mw.mwdata.ui.MControlFactory;
import de.mw.mwdata.ui.MTag;
import de.mw.mwdata.ui.MTagException;

public class OfdbAwareTag extends AbstractHtmlElementTag {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 4765139832006196611L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private EntityTO<AbstractMWEntity>	entityTO;

	private OfdbField					ofProp;

	@Override
	protected int writeTagContent( final TagWriter tagWriter ) throws JspException {
		
		if(!this.ofProp.isFilterable()) {
			String msg = MessageFormat.format("OfdbField {0} not filterable.", this.ofProp);
			logger.info(msg);
			return SKIP_BODY;
		}
		if(!this.ofProp.isVisible()) {
			String msg = MessageFormat.format("OfdbField {0} not visible.", this.ofProp);
			logger.info(msg);
			return SKIP_BODY;
		}
		if(!this.ofProp.isMapped()) {
			JspWriter writer = this.pageContext.getOut();
			try {
				writer.write("property not mapped");
			} catch (IOException e) {
				logger.error("JspWriter not found.");
			}
			return SKIP_BODY;
		}
		
		MControlFactory controlFactory = new SpringWebControlFactory(this.pageContext);
		List<MControl> controls = controlFactory.createMControls(this.ofProp);
		
		for (MControl mControl : controls) {
			if(!(mControl instanceof MTag)) {
				throw new MTagException("Invalid MControl created by MControlFactory.");
			}
			
		}
		
		for (MControl mControl : controls) {
			MTag controlTag = (MTag) mControl;
			controlTag.startTag();
			controlTag.endTag();
	
			if ( mControl.hasErrors() ) {
	
				MControl errorControl = controlFactory.createErrorControl(mControl);
				if(!(errorControl instanceof MTag)) {
					throw new MTagException("Invalid Error MControl created by MControlFactory.");
				}
				
				MTag errorControlTag = (MTag) errorControl;
				errorControlTag.startTag();
				errorControlTag.endTag();
	
			}
		}

		return SKIP_BODY;
	}

	public void setEntityTO( final EntityTO<AbstractMWEntity> entityTO ) {
		this.entityTO = entityTO;
	}

	public EntityTO<AbstractMWEntity> getEntityTO() {
		return this.entityTO;
	}

	public void setOfProp( final OfdbField ofProp ) {
		this.ofProp = ofProp;
	}

	public OfdbField getOfProp() {
		return this.ofProp;
	}

}
