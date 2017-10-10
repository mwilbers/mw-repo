package de.mw.mwdata.core.web.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig.DBTYPE;
import de.mw.mwdata.ui.EntityTOPathBuilder;
import de.mw.mwdata.ui.MControl;
import de.mw.mwdata.ui.MControlFactory;
import de.mw.mwdata.ui.NullBooleanEnum;

public class SpringWebControlFactory implements MControlFactory {

//	private OfdbField ofProp;
	private EntityTOPathBuilder pathBuilder;
	private PageContext pageContext;
	
	public SpringWebControlFactory(final PageContext pageContext) {
		this.pathBuilder = new EntityTOPathBuilder();
		this.pageContext = pageContext;
	}
	
	public List<MControl> createMControls(OfdbField ofProp) {
		
		
		List<MControl> controls = new ArrayList<MControl>();
		
		if(!CollectionUtils.isEmpty(ofProp.getListOfValues())) {
			
			if(ofProp.getDbtype().equals(DBTYPE.ENUM)) {
				
				SpringSelectTag selectTag = new SpringSelectTag(ofProp, pathBuilder, pageContext, StringUtils.EMPTY, true);
				controls.add(selectTag);
				
			} else if(ofProp.getDbtype().equals(DBTYPE.STRING) || ofProp.getDbtype().equals(DBTYPE.LONGINTEGER)) {
				
				SpringSelectTag selectTag = new SpringSelectTag(ofProp, pathBuilder, pageContext, StringUtils.EMPTY, true);
				controls.add(selectTag);
				
			}
			
		} else {
			
			if(ofProp.getDbtype().equals(DBTYPE.BOOLEAN)) {
				
				// radio buttons
				SpringRadioTag radioTag = new SpringRadioTag(ofProp, this.pathBuilder, this.pageContext, NullBooleanEnum.TRUE);
				controls.add(radioTag);
				radioTag = new SpringRadioTag(ofProp, this.pathBuilder, this.pageContext, NullBooleanEnum.FALSE);
				controls.add(radioTag);
				radioTag = new SpringRadioTag(ofProp, this.pathBuilder, this.pageContext, NullBooleanEnum.NULL);
				controls.add(radioTag);
				
			} else { 
				
				// input text field
				controls.add(new SpringInputTag( ofProp, this.pathBuilder, this.pageContext, StringUtils.EMPTY ));
				
			}

		}
		
		

		return controls;
	}
	
	public MControl createErrorControl(final MControl control) {
		return new SpringErrorTag(control, this.pathBuilder, this.pageContext);
	}

}
