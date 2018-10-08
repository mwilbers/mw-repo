package de.mw.mwdata.ofdb.query.impl;

import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.core.query.MetaDataQueryBuilder;
import de.mw.mwdata.core.query.SimpleQueryBuilder;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.query.MetaDataGenerator;
import de.mw.mwdata.ofdb.service.IOfdbService;

public class OfdbMetaDataQueryBuilder extends SimpleQueryBuilder implements MetaDataQueryBuilder {

	private ViewConfigHandle viewHandle;
	private IOfdbService ofdbService;

	public OfdbMetaDataQueryBuilder(final ViewConfigHandle viewHandle, final IOfdbService ofdbService) {
		this.viewHandle = viewHandle;
		this.ofdbService = ofdbService;
	}

	@Override
	public List<OfdbField> buildMetaData() {

		List<ITabSpeig> tableProps = this.viewHandle.getTableProps(this.viewHandle.getMainAnsichtTab().getTabDef());
		List<OfdbField> ofFields = new ArrayList<>();

		for (ITabSpeig tableProp : tableProps) {
			MetaDataGenerator metaDataGenerator = new TableMetaDataGenerator(this.viewHandle, tableProp,
					this.ofdbService);
			OfdbField ofField = metaDataGenerator.createColumnMetaData();
			ofFields.add(ofField);
		}

		return null;
	}

}
