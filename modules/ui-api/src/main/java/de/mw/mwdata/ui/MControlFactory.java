package de.mw.mwdata.ui;

import java.util.List;

import de.mw.mwdata.core.to.OfdbField;

public interface MControlFactory {

	/**
	 * The factory creates a list of ui-controls with only the informations of the underlying {@link OfdbField} 
	 * @param ofProp
	 * @return
	 */
	public List<MControl> createMControls(final OfdbField ofProp);
	
	/**
	 * Creates bound error tag to given {@link MControl} if method hasErrors of MControl returns true
	 * @return
	 */
	public MControl createErrorControl(final MControl control);
	
}
