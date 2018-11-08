package de.mw.mwdata.ofdb.query.impl;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.JoinedPropertyTO;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;
import de.mw.mwdata.ofdb.impl.OfdbUtils;
import de.mw.mwdata.ofdb.service.IOfdbService;

public class ViewMetaDataGenerator extends AbstractMetaDataGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewMetaDataGenerator.class);

	private ViewConfigHandle viewHandle;
	private ITabSpeig tabProp;
	private IAnsichtSpalte viewColumn;
	final int highestResultIndex;

	private IOfdbService ofdbService;
	private OfdbCacheManager ofdbCacheManager;

	public ViewMetaDataGenerator(final ViewConfigHandle viewHandle, final ITabSpeig tabSpeig,
			final IAnsichtSpalte ansichtSpalte, final int highestResultIndex, final OfdbCacheManager ofdbCacheManager,
			final IOfdbService ofdbService) {
		this.viewHandle = viewHandle;
		this.tabProp = tabSpeig;
		this.viewColumn = ansichtSpalte;
		this.highestResultIndex = highestResultIndex;

		this.ofdbCacheManager = ofdbCacheManager;
		this.ofdbService = ofdbService;

	}

	@Override
	public OfdbField createColumnMetaData() {

		OfdbField ofField = OfdbUtils.createOfdbField(this.tabProp, this.viewColumn);
		OfdbEntityMapping mainEntityMapping = this.viewHandle.getEntityMapping(this.tabProp.getTabDef().getName());
		OfdbPropMapper propMapper = mainEntityMapping.getMapper(this.tabProp);

		String propName = StringUtils.EMPTY;
		if (null != propMapper) {
			propName = propMapper.getPropertyName();
			ofField.setPropName(propName);
		}
		ofField.setMaxlength(calculateMaxLength(this.tabProp));
		ofField.setMinlength(calculateMinLength(this.tabProp));

		if (this.viewHandle.isJoinedViewColumn(this.viewColumn)) {

			this.viewHandle.getQueryModel().addAlias(this.viewColumn.getViewTab(), this.tabProp);

		} else if (!StringUtils.isEmpty(this.viewColumn.getAnsichtSuchen())) {

			// zu 1.:
			ViewConfigHandle viewHandleSuchen = this.ofdbCacheManager.getViewConfig(this.viewColumn.getAnsichtSuchen());

			IAnsichtTab ansichtTab = this.viewHandle.findAnsichtTabByTabAKey(this.viewColumn.getSuchwertAusTabAKey());

			if (null == ansichtTab) {
				String msg = MessageFormat.format(
						"Fehlende Tabelle für Verknüpfung in FX_AnsichtTab_K zum Feld FX_AnsichtSpalten_K.SuchWertAusTabAKey für Ansicht: {0}, Spalte: {1}",
						this.viewColumn.getAnsichtDef().getName(), this.viewColumn.getSpalteAKey());
				throw new OfdbInvalidConfigurationException(msg);
			}

			ITabDef tabDef = null;
			if (this.viewColumn.getSuchwertAusTabAKey().equals(this.viewColumn.getTabAKey())) {
				tabDef = this.viewHandle.getMainAnsichtTab().getTabDef();
				viewHandleSuchen = this.viewHandle;
			} else {
				tabDef = this.ofdbCacheManager.findRegisteredTableDef(this.viewColumn.getSuchwertAusTabAKey());
				if (null == tabDef) {
					String msg = MessageFormat.format(
							"Wrong order of loading registered views. View {0} is referencing missing view {1}",
							this.viewColumn.getAnsichtDef().getName(), this.viewColumn.getSuchwertAusTabAKey());
					LOGGER.error(msg);
				}
			}

			if (ofField.isMapped()) {

				// TODO: tabBeziehnungen here still needed? this.daoMap still needed ?
				Class<IEntity> clazz = ClassNameUtils.loadClass(tabDef.getFullClassName());
				List<Object> listOfValues = this.ofdbService.getListOfValues(ofField, this.tabProp,
						viewHandleSuchen.getViewOrders(), clazz);
				ofField.setListOfValues(listOfValues);

				ITabSpeig suchWertTabSpeig = viewHandleSuchen.findTabSpeigByTabAKeyAndSpalteAKey(
						this.viewColumn.getSuchwertAusTabAKey(), this.viewColumn.getSuchwertAusSpalteAKey());

				// e.g. TableA references TableA recursively ...
				OfdbPropMapper suchPropMapper = null;
				if (this.tabProp.getTabDef().equals(suchWertTabSpeig.getTabDef())) {
					suchPropMapper = mainEntityMapping.getMapper(suchWertTabSpeig);
				} else {
					suchPropMapper = viewHandleSuchen.findPropertyMapperByTabProp(suchWertTabSpeig);
				}

				String listPropValueName = suchPropMapper.getPropertyName();
				String itemKey = null;

				if (!StringUtils.isEmpty(this.viewColumn.getVerdeckenDurchTabAKey())
						&& !StringUtils.isEmpty(this.viewColumn.getVerdeckenDurchSpalteAKey())) {

					suchWertTabSpeig = viewHandleSuchen.findTabSpeigByTabAKeyAndSpalteAKey(
							this.viewColumn.getVerdeckenDurchTabAKey(), this.viewColumn.getVerdeckenDurchSpalteAKey());

					OfdbPropMapper mapper = null;
					if (this.tabProp.getTabDef().equals(suchWertTabSpeig.getTabDef())) {
						mapper = mainEntityMapping.getMapper(suchWertTabSpeig);
					} else {
						mapper = viewHandleSuchen.findPropertyMapperByTabProp(suchWertTabSpeig);
					}

					listPropValueName = mapper.getPropertyName();

					ofField.setResultIndex(highestResultIndex + 1);
					ofField.setColumnTitle(suchWertTabSpeig.getSpaltenkopf());
					itemKey = OfdbUtils.generateItemKey(suchWertTabSpeig.getTabDef(), listPropValueName);

					JoinedPropertyTO joinedProperty = new JoinedPropertyTO(propMapper.getAssociatedEntityName(),
							listPropValueName, highestResultIndex + 1);
					ofField.setJoinedProperty(joinedProperty);

				} else {

					ofField.setResultIndex(0);
					itemKey = OfdbUtils.generateItemKey(ansichtTab.getTabDef(), listPropValueName);

				}
				this.viewHandle.getQueryModel().addJoinTable(ansichtTab);

			} else {
				// no index needed here
			}

		} else { // AnsichtSuchen is empty

			if (ofField.isEnum()) {
				List<Object> listOfValues = this.ofdbService.getListOfValues(ofField, this.tabProp, null, null);
				ofField.setListOfValues(listOfValues);
			}
			ofField.setResultIndex(0);
		}

		return ofField;
	}

}
