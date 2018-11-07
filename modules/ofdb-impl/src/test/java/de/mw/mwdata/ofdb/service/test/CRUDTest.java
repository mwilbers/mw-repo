/**
 *
 */
package de.mw.mwdata.ofdb.service.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryBuilder;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.query.SimpleQueryBuilder;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef.DATENBANK;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;
import de.mw.mwdata.ofdb.exception.OfdbException;
import de.mw.mwdata.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;
import de.mw.mwdata.ofdb.test.AbstractOfdbInitializationTest;

/**
 * Test for basic database-functionality like create, read, update, delete
 * (CRUD). Furthermore db-sequence-generator ist tested here, some filter- and
 * sorting-functionality and default-handling
 *
 * @author mwilbers
 *
 */
@ContextConfiguration(locations = { "classpath:/appContext-ofdb.xml", "classpath:/appContext-ofdb-test.xml",
		"classpath:/appContext-common-test-db.xml" })
@TransactionConfiguration
public class CRUDTest extends AbstractOfdbInitializationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CRUDTest.class);

	private static final String TABLENAME_TAB1 = "TestTabDef1";
	private static final String TABLENAME_TAB2 = "TestTabDef2";

	@Test
	public void testFindAllWithSorting() throws OfdbMissingMappingException {

		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("+++++testFindAllWithSorting+++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");

		// test empty resultset
		List<TabDef> tabDefs = this.getCrudDao().findAll(TabDef.class);
		Assert.assertTrue(tabDefs.isEmpty());

		this.applicationFactory.configure();

		// insert TabDefs for tests
		TabDef tabDef1 = DomainMockFactory.createTabDefMock(TABLENAME_TAB1, this.getTestBereich(), !isAppInitialized());
		saveForTest(tabDef1);
		TabDef tabDef2 = DomainMockFactory.createTabDefMock(TABLENAME_TAB2, this.getTestBereich(), !isAppInitialized());
		saveForTest(tabDef2);

		this.applicationFactory.init();

		// 1. test findAll-method
		tabDefs = this.getCrudDao().findAll(TabDef.class);
		Assert.assertTrue(tabDefs.size() > 1, "Not enough TabDef-Entities found.");

		// 2. test default-sorting: ascending
		tabDef1 = tabDefs.get(0);
		tabDef2 = tabDefs.get(1);
		Assert.assertEquals(tabDef1.getName(), TABLENAME_TAB1);
		Assert.assertEquals(tabDef2.getName(), TABLENAME_TAB2);
		Assert.assertTrue(tabDef1.getName().toUpperCase().compareTo(tabDef2.getName().toUpperCase()) < 0, "");

		// 3. test sorting descending
		Map<String, String> sortColumns = new LinkedHashMap<String, String>();
		sortColumns.put("name", "desc");
		tabDefs = this.getCrudDao().findAll(TabDef.class, sortColumns);

		tabDef1 = tabDefs.get(0);
		tabDef2 = tabDefs.get(1);
		Assert.assertEquals(tabDef1.getName(), TABLENAME_TAB2);
		Assert.assertEquals(tabDef2.getName(), TABLENAME_TAB1);
		Assert.assertFalse(tabDef1.getName().toUpperCase().compareTo(tabDef2.getName().toUpperCase()) < 0, "");

		// 4. test sorting ascending
		sortColumns = new LinkedHashMap<String, String>();
		sortColumns.put("name", "asc");
		tabDefs = this.getCrudDao().findAll(TabDef.class, sortColumns);
		tabDef1 = tabDefs.get(0);
		tabDef2 = tabDefs.get(1);
		Assert.assertEquals(tabDef1.getName(), TABLENAME_TAB1);
		Assert.assertEquals(tabDef2.getName(), TABLENAME_TAB2);
		Assert.assertTrue(tabDef1.getName().toUpperCase().compareTo(tabDef2.getName().toUpperCase()) < 0, "");

		// 4. test sorting descending with any sort-value
		sortColumns.clear();
		sortColumns.put("name", "foo");
		tabDefs = this.getCrudDao().findAll(TabDef.class, sortColumns);
		tabDef1 = tabDefs.get(0);
		tabDef2 = tabDefs.get(1);
		Assert.assertFalse(tabDef1.getName().toUpperCase().compareTo(tabDef2.getName().toUpperCase()) < 0, "");

	}

	@Test
	public void testFindByCriteria() throws OfdbException {

		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("+++++testFindByCriteria+++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");

		this.applicationFactory.configure();

		// ... xml-spring-bean config zu sessionFactory anpassen: packagesToScan - Wert
		// muss generisch von aufrufender anwendung kommen
		// z.B. admin oder calendar
		//
		// add ansichtDef, ansichtTab, tabDef for FX_TabDef_K, BenutzerBereicheDef
		this.setUpAnsichtAndTab(TestConstants.TABLENAME_BENUTZERBEREICH, "benutzerBereich", BenutzerBereich.class);
		IAnsichtTab ansichtTab_TabDef = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, "tabDef", TabDef.class);
		ansichtTab_TabDef.getTabDef().setDatenbank(DATENBANK.K);
		saveForTest(ansichtTab_TabDef.getTabDef());

		AnsichtTab ansichtTab_TabSpeig = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABSPEIG, "tabSpeig",
				TabSpeig.class);

		this.applicationFactory.init();

		// 1. test find TabDef with "TABELLE" = "FX_TabDef_K"
		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b.selectEntity("TabDef", "tDef").fromEntity("TabDef", "tDef")
				.andWhereRestriction("tDef", "name", OperatorEnum.Eq, TestConstants.TABLENAME_TABDEF, ValueType.STRING)
				.buildSQL();
		QueryResult result = this.getCrudService().executeSql(sql);

		List<TabDef> tabDefs = new ArrayList<TabDef>();
		for (int i = 0; i < result.size(); i++) {
			tabDefs.add((TabDef) result.getEntityByRowIndex(0));
		}

		Assert.assertEquals(tabDefs.size(), 1);
		Assert.assertEquals(tabDefs.get(0).getName(), TestConstants.TABLENAME_TABDEF,
				"Not correct TabDef found: " + tabDefs.get(0).getName());

		// 2. test findByCriteria with enum-value (strategy: we fetch objects from
		// hibernate-sessions, change them
		// and do the test with findByCriteria. There is no update-call to database)
		b = new SimpleQueryBuilder();
		sql = b.selectEntity("tabDef", "tDef").fromEntity("AnsichtTab", "aTab").joinEntity("tabDef", "tDef")
				.andWhereRestriction("tDef", "datenbank", OperatorEnum.Eq, DATENBANK.K.name(), ValueType.STRING)
				.buildSQL();
		result = this.getCrudService().executeSql(sql);

		tabDefs = new ArrayList<TabDef>();
		for (int i = 0; i < result.size(); i++) {
			IEntity[] entityArray = result.getRows().get(i);
			tabDefs.add((TabDef) entityArray[0]);
		}

		Assert.assertEquals(tabDefs.size(), 1);

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(ansichtTab_TabSpeig.getTabDef().getName());
		ITabSpeig tabSpeigEingabeNotwendig = viewHandle
				.findTabSpeigByTabAKeyAndSpalteAKey(ansichtTab_TabSpeig.getTabAKey(), "EINGABENOTWENDIG");
		((TabSpeig) tabSpeigEingabeNotwendig).setEingabeNotwendig(true);
		saveForTest(tabSpeigEingabeNotwendig);

		List<TabSpeig> tabSpeigs = new ArrayList<TabSpeig>();
		b = new SimpleQueryBuilder();
		sql = b.selectEntity("TabSpeig", "tSpeig").fromEntity("TabSpeig", "tSpeig")
				.andWhereRestriction("tSpeig", "eingabeNotwendig", OperatorEnum.Eq, Boolean.TRUE, ValueType.BOOLEAN)
				.buildSQL();
		result = this.getCrudService().executeSql(sql);
		tabSpeigs.clear();
		for (int i = 0; i < result.size(); i++) {
			IEntity[] entityArray = result.getRows().get(i);
			tabSpeigs.add((TabSpeig) entityArray[0]);
		}

		Assert.assertEquals(tabSpeigs.size(), 1);

		TabSpeig t = tabSpeigs.get(0);
		// t.setSpalte( "aaa" );
		t.setSpaltenkopf("changedSpaltenkopf");
		saveForTest(t);
		Assert.assertNotNull(t);

	}

	@Test
	public void testInitializeOfdbMapper() throws OfdbMissingMappingException {
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("+++++ testInitializeOfdbMapper +++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");

		this.applicationFactory.configure();

		IAnsichtTab viewTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, "tabDef", TabDef.class);

		this.applicationFactory.init();

		ITabSpeig tabSpeigMock = DomainMockFactory.createTabSpeigMock((TabDef) viewTab.getTabDef(), "ALIAS", 1,
				DBTYPE.STRING);
		ITabSpeig tabSpeigId = DomainMockFactory.createTabSpeigMock((TabDef) viewTab.getTabDef(), "DSID", 2,
				DBTYPE.LONGINTEGER);
		TabSpeig tabSpeigIdImpl = (TabSpeig) tabSpeigId;
		tabSpeigIdImpl.setPrimSchluessel(Boolean.TRUE);

		// start test ...
		ViewConfigHandle viewHandle = this.ofdbCacheManager
				.findViewConfigByTableName(tabSpeigMock.getTabDef().getName());

		OfdbPropMapper propMapMock = viewHandle.findPropertyMapperByTabProp(tabSpeigMock);
		String propNameMock = propMapMock.getPropertyName();
		OfdbPropMapper propMapId = viewHandle.findPropertyMapperByTabProp(tabSpeigId);
		String propNameId = propMapId.getPropertyName();

		// Assert.assertTrue( propMap.size() == tList.size() );
		Assert.assertEquals(propNameMock, "alias");
		Assert.assertEquals(propNameId, Constants.SYS_PROP_ID);

	}

}
