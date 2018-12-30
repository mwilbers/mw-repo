package de.mw.mwdata.ofdb.cache;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import de.mw.mwdata.core.LocalizedMessages;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.ofdb.cache.ViewConfiguration.Builder;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtOrderBy;
import de.mw.mwdata.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.ofdb.impl.ConfigOfdb;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbFieldComparator;
import de.mw.mwdata.ofdb.impl.OfdbUtils;
import de.mw.mwdata.ofdb.query.MetaDataGenerator;
import de.mw.mwdata.ofdb.query.OfdbQueryModel;
import de.mw.mwdata.ofdb.query.impl.DefaultOfdbQueryModel;
import de.mw.mwdata.ofdb.query.impl.ViewMetaDataGenerator;
import de.mw.mwdata.ofdb.service.IOfdbService;

public class ViewConfigFactoryBean implements ViewConfigFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewConfigFactoryBean.class);

	private OfdbCacheManager ofdbCacheManager;
	private IOfdbService ofdbService;

	public void setOfdbCacheManager(OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	private void handleValidationErrors(final ViewConfigValidationResultSet resultSet) {

		StringBuilder b = new StringBuilder();
		for (ViewConfigValidationResult result : resultSet) {
			LOGGER.error(result.toString());
			b.append(result.toString()).append("\n");
		}

		if (resultSet.hasErrors()) {
			String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB, "invalidConfiguration",
					ConfigOfdb.T_TABDEF);
			// abort directly: no further validation-process needed
			b.append(msg);
			throw new OfdbInvalidConfigurationException(b.toString());
		}
	}

	@Override
	public ViewConfigHandle createViewConfiguration(final String viewName) {

		// initialize AnsichtDef
		IAnsichtDef ansicht = this.ofdbService.findAnsichtByName(viewName);
		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		// initialize AnsichtDef
		ViewConfigValidationResultSet partSet = this.ofdbService.isAnsichtValid(ansicht);
		set.merge(partSet);
		ViewConfiguration.Builder builder = new Builder(ansicht);

		// check AnsichtTab - entries
		List<IAnsichtTab> ansichtTabList = this.ofdbService.findAnsichtTabByAnsichtId(ansicht.getId());
		if (CollectionUtils.isEmpty(ansichtTabList)) {
			set.addValidationResult("invalidOfdbConfig.FX_AnsichtTab.missingAnsichtTabEntry", ansicht.getName());
			// if no AnsichtTab-entries show errors and abort
			this.handleValidationErrors(set);
		}
		partSet = this.ofdbService.isAnsichtTabListValid(ansicht, ansichtTabList);
		set.merge(partSet);
		if (set.hasErrors()) {
			this.handleValidationErrors(set);
		}

		// before validating ansichtTab-objects do validation to underlying tabDefs and
		// tabSpeigs
		String tableName = null;
		IAnsichtTab mainAnsichtTab = OfdbUtils.getMainAnsichtTab(ansichtTabList);
		OfdbEntityMapping mainEntityMapping = new OfdbEntityMapping(mainAnsichtTab.getTabDef().getName());
		for (IAnsichtTab ansichtTab : ansichtTabList) {

			tableName = ansichtTab.findTableName();

			// check if table is already registered. possible because one table can belong
			// to several views
			// ITabDef tabDef = this.ofdbCacheManager.findRegisteredTableDef(tableName);
			List<ITabSpeig> tabSpeigs = null;
			ITabDef tabDef = ansichtTab.getTabDef();
			tabSpeigs = this.ofdbService.loadTablePropListByTableName(tableName);

			partSet = this.ofdbService.isTableValid(tabDef, tabSpeigs);
			set.merge(partSet);
			if (set.hasErrors()) {
				this.handleValidationErrors(set);
			}

			if (CollectionUtils.isEmpty(tabSpeigs)) {
				String msg = MessageFormat.format("No TabSpeigs found for tablename {0} in ViewConfiguration.",
						tableName);
				LOGGER.warn(msg);
			}

			// builder.addTableDef(tabDef);
			// builder.addTableProps(tabDef, tabSpeigs);

			// // initialize TabBez
			// List<TabBez> tabBez = this.ofdbDao.findTabBezByTable( tableName );
			// // FIXME: do validation-check of all tabBezs

			builder.addViewTab(ansichtTab);

			tableName = ansichtTab.getTabDef().getName();

			// if table props are called for first time, than do initialize properties and
			// add them to cache
			OfdbEntityMapping entityMapping = this.ofdbCacheManager.getEntityMapping(tableName);
			if (null == entityMapping) {

				Class<? extends AbstractMWEntity> entityClassType = checkFullClassName(ansichtTab.getTabDef());
				entityMapping = this.ofdbService.initializeMapping(entityClassType, tableName, tabSpeigs);

				partSet = checkMappingTabSpeig2Property(tabSpeigs, entityMapping);
				set.merge(partSet);
				if (set.hasErrors()) {
					this.handleValidationErrors(set);
				}
				if (builder.buildHandle().getMainAnsichtTab().getTabDef().equals(tabDef)) {
					mainEntityMapping = entityMapping;
				}

			}

			builder.addTableConfig(tabDef, tabSpeigs, entityMapping);

		} // end for each AnsichtTab

		// initialize ansichtOrderBy
		List<AnsichtOrderBy> viewOrderByList = this.ofdbService.findAnsichtOrderByAnsichtId(ansicht.getId());
		for (IAnsichtOrderBy viewOrderBy : viewOrderByList) {

			partSet = this.ofdbService.isViewOrderByValid(viewOrderBy, builder.buildHandle());
			set.merge(partSet);
			if (!partSet.hasErrors()) {
				builder.addViewOrderBy(viewOrderBy);
			}

		}

		// initialize AnsichtSpalten
		List<IAnsichtSpalte> ansichtSpalten = this.ofdbService.findAnsichtSpaltenByAnsichtId(ansicht.getId());
		List<IAnsichtSpalte> resultSpalten = new ArrayList<>();
		for (IAnsichtSpalte ansichtSpalte : ansichtSpalten) {
			// ... TestQueryBuilder: get the relevant tabSpeig of the ansichtSpalte
			partSet = this.ofdbService.isAnsichtSpalteValid(ansichtSpalte, builder.buildHandle());
			set.merge(partSet);

			resultSpalten.add(ansichtSpalte);
		}

		if (set.hasErrors()) {
			handleValidationErrors(set);
		}
		builder.setViewColumns(resultSpalten);

		// ... FIXME: no mainAnsichtTab for ADAnsichtenDef
		OfdbQueryModel queryModel = new DefaultOfdbQueryModel();

		IAnsichtTab ansichtTab = builder.buildHandle().getMainAnsichtTab();
		if (null == ansichtTab) {
			String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB, "missingAnsichtTabJoinTypX",
					viewName);
			throw new OfdbInvalidConfigurationException(msg);
		}

		queryModel.setMainTable(ansichtTab.getTabDef());
		// FIXME: getTabDef WRONG !!! We have to use T entity from method-argument here.

		builder.setQueryModel(queryModel);
		List<OfdbField> ofFieldList = createViewMetaData(builder.buildHandle());
		queryModel.addMetaData(ofFieldList);

		return builder.buildHandle();
	}

	private ViewConfigValidationResultSet checkMappingTabSpeig2Property(final List<ITabSpeig> tabSpeigs,
			final OfdbEntityMapping entityMapping) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		for (ITabSpeig tabProp : tabSpeigs) {

			if (tabProp.isEindeutig() && !entityMapping.hasMapping(tabProp)) {
				set.addValidationResult("invalidOfdbConfig.FX_TabSpeig.uniquePropertyNotMapped", tabProp.getName(),
						tabProp.getTabDef().getName());
			} else if (!entityMapping.hasMapping(tabProp)) {
				String msg = MessageFormat.format("Table property {0} of table {1} could not be mapped to property.",
						tabProp.getSpalte(), tabProp.getTabDef().getName());
				LOGGER.info(msg);
			}

		}

		return set;
	}

	private Class<? extends AbstractMWEntity> checkFullClassName(final ITabDef tabDef)
			throws OfdbInvalidConfigurationException {
		Class<? extends AbstractMWEntity> entityClassType = null;
		try {
			entityClassType = ClassNameUtils.getClassType(tabDef.getFullClassName());
		} catch (ClassNotFoundException e) {
			// do nothing. Conversion already checked in OfdbValidator.
			String msg = MessageFormat.format(
					"Invalid TabDef-configuration: Field fullClassName {0} of TabDef {1} not valid: ",
					tabDef.getFullClassName(), tabDef);
			LOGGER.error(msg);
			throw new OfdbInvalidConfigurationException(msg);
		}
		return entityClassType;
	}

	// private void initQueryModel(final String viewName, final Builder builder) {
	//
	// OfdbQueryModel queryModel = new DefaultOfdbQueryModel();
	//
	// IAnsichtTab ansichtTab = builder.buildHandle().getMainAnsichtTab();
	// if (null == ansichtTab) {
	// String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB,
	// "missingAnsichtTabJoinTypX",
	// viewName);
	// throw new OfdbInvalidConfigurationException(msg);
	// }
	//
	// queryModel.setMainTable(ansichtTab.getTabDef());
	// // FIXME: getTabDef WRONG !!! We have to use T entity from method-argument
	// here.
	//
	// builder.setQueryModel(queryModel);
	//
	// }

	@Transactional(propagation = Propagation.REQUIRED)
	private List<OfdbField> createViewMetaData(final ViewConfigHandle viewHandle) {
		List<OfdbField> ofFields = new ArrayList<OfdbField>();

		List<IAnsichtSpalte> ansichtSpalten = viewHandle.getViewColumns();
		int resultIndex = 0;

		for (IAnsichtSpalte ansichtSpalte : ansichtSpalten) {
			ITabSpeig tabSpeig = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(ansichtSpalte.getTabAKey(),
					ansichtSpalte.getSpalteAKey());
			resultIndex = findHighestResultIndex(ofFields);

			// FIXME: exception can be removed if there is foreign key from
			// FX_AnsichtSpalten_K.spalteakey to
			// FX_TabSpEig_K.spalte
			if (null == tabSpeig) {
				String msg = MessageFormat.format(
						"No TabSpEig can be found for AnsichtSpalte with spalteAKey {0} and tabAKey {1}.",
						ansichtSpalte.getSpalteAKey(), ansichtSpalte.getTabAKey());
				throw new OfdbInvalidConfigurationException(msg);
			}

			MetaDataGenerator metaDataGenerator = new ViewMetaDataGenerator(viewHandle, tabSpeig, ansichtSpalte,
					resultIndex, this.ofdbCacheManager, this.ofdbService);
			OfdbField ofField = metaDataGenerator.createColumnMetaData();
			ofFields.add(ofField);

		}

		// OfdbQueryModel queryModel = viewHandle.getQueryModel();
		Collections.sort(ofFields, new OfdbFieldComparator());
		// queryModel.addMetaData(ofFields);

		return ofFields;

	}

	private int findHighestResultIndex(final List<OfdbField> ofFields) {
		int result = 0;
		for (OfdbField field : ofFields) {
			result = (field.getResultIndex() > result ? field.getResultIndex() : result);
		}
		return result;
	}

}
