package de.mw.mwdata.ofdb.query;

import de.mw.mwdata.core.to.OfdbField;

/**
 * The {@link OfdbField } can be generated in different ways. Therefore the
 * {@link MetaDataGenerator } generates it done like a strategy pattern.
 * 
 * @author WilbersM
 *
 */
public interface MetaDataGenerator {

	public OfdbField createColumnMetaData();

}
