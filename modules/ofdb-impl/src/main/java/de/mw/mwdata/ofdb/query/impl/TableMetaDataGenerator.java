package de.mw.mwdata.ofdb.query.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;
import de.mw.mwdata.ofdb.service.IOfdbService;

public class TableMetaDataGenerator extends AbstractMetaDataGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(TableMetaDataGenerator.class);

	private ViewConfigHandle viewHandle;
	private ITabSpeig tabProp;
	private IOfdbService ofdbService;

	public TableMetaDataGenerator(final ViewConfigHandle viewHandle, final ITabSpeig tableProp,
			final IOfdbService ofdbService) {
		this.viewHandle = viewHandle;
		this.tabProp = tableProp;
		this.ofdbService = ofdbService;
	}

	@Override
	public OfdbField createColumnMetaData() {

		OfdbField ofField = createOfdbField();
		OfdbEntityMapping mainEntityMapping = this.viewHandle.getEntityMapping(this.tabProp.getTabDef().getName());
		OfdbPropMapper propMapper = mainEntityMapping.getMapper(this.tabProp);

		String propName = StringUtils.EMPTY;
		if (null != propMapper) {
			propName = propMapper.getPropertyName();
			ofField.setPropName(propName);
		}
		ofField.setMaxlength(calculateMaxLength(this.tabProp));
		ofField.setMinlength(calculateMinLength(this.tabProp));

		if (ofField.isEnum()) {
			ofField.setItemLabel(propName);
			ofField.setItemValue(propName);
			ofField.setItemKey(propName);

			List<Object> listOfValues = this.ofdbService.getListOfValues(ofField, this.tabProp, null, null);
			ofField.setListOfValues(listOfValues);
			// this.ofdbDao.setListOfValues( ofField, tabSpeig, null, null );

		} else {

		}
		ofField.setResultIndex(0);

		return ofField;

	}

	public OfdbField createOfdbField() {

		OfdbField ofdbField = new OfdbField();

		ofdbField.setTabSpeigBearbErlaubt(this.tabProp.getBearbErlaubt());
		ofdbField.setTabSpeigSystemWert(this.tabProp.getSystemWert());
		ofdbField.setAnsichtSpalteBearbZugelassen(false);
		ofdbField.setAnsichtSpalteBearbHinzufuegenZugelassen(false);

		ofdbField.setCurrency(ofdbField.DEFAULT_CURRENCY);

		ofdbField.setFilterable(false);
		ofdbField.setVisible(true);
		ofdbField.setReihenfolge(this.tabProp.getReihenfolge());
		ofdbField.setPropOfdbName(this.tabProp.getSpalte());
		ofdbField.setNullable(!this.tabProp.getEingabeNotwendig());
		ofdbField.setDbtype(this.tabProp.getDbDatentyp());
		ofdbField.setColumnTitle(this.tabProp.getSpaltenkopf());

		// this.refreshEditMode(crud);
		return ofdbField;

	}

}
