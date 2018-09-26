package de.mw.mwdata.core.query;

import java.util.List;

import de.mw.mwdata.core.to.OfdbField;

public interface MetaDataQueryBuilder extends QueryBuilder {

	public List<OfdbField> buildMetaData();

}
