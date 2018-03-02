/**
 *
 */
package de.mw.mwdata.ofdb.service.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.common.utils.FxDateUtils;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.Sequence;
import de.mw.mwdata.core.ofdb.exception.OfdbException;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.ofdb.query.DefaultOfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.OfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.OperatorEnum;
import de.mw.mwdata.core.ofdb.query.ValueType;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef.DATENBANK;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.ITabSpeig.DBTYPE;
import de.mw.mwdata.ofdb.domain.impl.AnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;
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
// @ContextHierarchy({ @ContextConfiguration(classes = TestAppConfig.class),
// @ContextConfiguration(classes = WebConfig.class) })
// @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
// @Test(groups = "hsql")
@ContextConfiguration(locations = { "classpath:/appContext-ofdb.xml", "classpath:/appContext-ofdb-test.xml",
		"classpath:/appContext-common-test-db.xml" })
@TransactionConfiguration
public class CRUDTest extends AbstractOfdbInitializationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CRUDTest.class);

	private static final String TABLENAME_TAB1 = "TestTabDef1";
	private static final String TABLENAME_TAB2 = "TestTabDef2";

	// 1. ansichtorder.tabdefid muss in hsql-db-ddl hinzugefügt werden -> erledigt
	// dann das ganze für
	// 2. ansichtspalten.tabspeigid -> noch in hsql
	// 3. ansichtspalten.ansichttabid -> noch in hsql
	// 4. validierungsregeln prüfen, ob überflüssige dabei sind
	// 5. dann weiter mit Umbau ofdbCrudInterceptor und die chain verlegen nach
	// ofdbService

	@Test
	public void testSequenceIncrementationAndInterceptor() throws OfdbMissingMappingException {

		// FIXME: check if test belongs to core component

		// ... check oracle compatibility:
		// http://hsqldb.org/doc/2.0/guide/management-chapt.html#mtc_compatibility_oracle
		// ... check oracle param:
		// http://stackoverflow.com/questions/4628857/junit-hsqldb-how-to-get-around-errors-with-oracle-syntax-when-testing-using-hsq?rq=1

		// http://stackoverflow.com/questions/3805478/internal-hsql-database-complains-about-privileges
		// hibernate incompatibility:
		// You need to use Hibernate 3.5.6 or later, together with HSQLDB version 2.2.x
		// or later. Otherwise, older
		// Hibernte jars work with HSQLDB 1.8.x.

		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("+++++testSequenceIncrementationAndInterceptor+++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");

		this.applicationFactory.configure();

		IAnsichtTab ansichtTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);

		this.applicationFactory.init();

		// insert TabDef for tests
		TabDef tabDef = DomainMockFactory.createTabDefMock("Testtable1", this.getTestBereich(), !isAppInitialized());

		// test state before insert
		Assert.assertNull(tabDef.getAngelegtAm(), "AngelegtAm is not null.");
		Assert.assertNull(tabDef.getAngelegtVon(), "AngelegtVon is not null.");

		// first get next expected TabDef.ID
		String sql = " SELECT LetzteBelegteNr, Inkrement  FROM " + TestConstants.TABLENAME_SYSSEQUENZ
				+ "  WHERE name = '" + tabDef.getSequenceKey() + "' ";
		List<Map<String, Object>> items = this.jdbcTemplate.queryForList(sql);
		// List<Map<String, Object>> items = this.simpleJdbcTemplate.queryForList( sql
		// );
		Map<String, Object> map = items.get(0);
		Long letzteBelegteNr = (Long) (map.get("LetzteBelegteNr"));
		Long inkrement = (Long) (map.get("Inkrement"));
		Sequence sequence3 = new Sequence();
		sequence3.setLetzteBelegteNr(letzteBelegteNr.longValue());
		sequence3.setInkrement(inkrement.longValue());

		// do Test
		// saveForTest( tabDef );
		this.getCrudService().insert(tabDef);

		// here we have to query the hibernate-session-factory for objects. This way the
		// factory does in implicit flush
		// to db before we use the simpleJdbcTemplate for querying new saved objects
		// (e.g. sequence-values)
		List<TabDef> allTabDefs = this.getCrudDao().findAll(TabDef.class);
		Assert.assertTrue(allTabDefs.size() == 2, "Wrong number of TabDef objects was saved.");

		// Test 1: check if inteceptor- and field-defaults were set
		Assert.assertNotNull(tabDef.getAngelegtVon(), "AngelegtVon is null.");
		Assert.assertNotNull(tabDef.getAngelegtAm(), "AngelegtAm is null.");
		Assert.assertNotNull(tabDef.getId(), "Id not correctly set.");
		Assert.assertEquals(tabDef.getAngelegtVon(), Constants.SYS_USER_DEFAULT);
		Assert.assertEquals(0, FxDateUtils.compare(new Date(), tabDef.getAngelegtAm(), false),
				"AngelegtAm not correctly set.");

		// Test 2: check if next id was set for tabDef
		Assert.assertEquals(sequence3.getNaechsteNr(), tabDef.getId(), "Wrong Sequence-ID was set");

		// Test 3: check incrementation of next number in TabDef-Sequence-Generator
		items = this.jdbcTemplate.queryForList(sql);
		map = items.get(0);
		Long neueLetzteBelegteNr = (Long) (map.get("LetzteBelegteNr"));
		Assert.assertNotSame(letzteBelegteNr.longValue(), neueLetzteBelegteNr.longValue(),
				"Sequence-number not incremented after insert-operation.");

		this.getCrudDao().delete(tabDef);
		List<TabDef> tabDefs = this.getCrudDao().findAll(TabDef.class);
		Assert.assertEquals(tabDefs.size(), 1);

	}

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
		this.setUpAnsichtAndTab(TestConstants.TABLENAME_BENUTZERBEREICH, BenutzerBereich.class.getName(),
				"benutzerBereich", BenutzerBereich.class);
		IAnsichtTab ansichtTab_TabDef = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);
		ansichtTab_TabDef.getTabDef().setDatenbank(DATENBANK.K);
		saveForTest(ansichtTab_TabDef.getTabDef());

		AnsichtTab ansichtTab_TabSpeig = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABSPEIG,
				TabSpeig.class.getName(), "tabSpeig", TabSpeig.class);

		this.applicationFactory.init();

		// 1. test find TabDef with "TABELLE" = "FX_TabDef_K"
		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable("TabDef", "tDef").fromTable("TabDef", "tDef")
				.andWhereRestriction("tDef", "name", OperatorEnum.Eq, TestConstants.TABLENAME_TABDEF, ValueType.STRING)
				.buildSQL();
		List<IEntity[]> results = this.getCrudService().executeSql(sql);

		List<TabDef> tabDefs = new ArrayList<TabDef>();
		for (int i = 0; i < results.size(); i++) {
			IEntity[] entityArray = results.get(i);
			tabDefs.add((TabDef) entityArray[0]);
		}

		Assert.assertEquals(tabDefs.size(), 1);
		Assert.assertEquals(tabDefs.get(0).getName(), TestConstants.TABLENAME_TABDEF,
				"Not correct TabDef found: " + tabDefs.get(0).getName());

		// 2. test findByCriteria with enum-value (strategy: we fetch objects from
		// hibernate-sessions, change them
		// and do the test with findByCriteria. There is no update-call to database)
		b = new DefaultOfdbQueryBuilder();
		sql = b.selectTable("tabDef", "tDef").fromTable("AnsichtTab", "aTab").joinEntity("tabDef", "tDef")
				.andWhereRestriction("tDef", "datenbank", OperatorEnum.Eq, DATENBANK.K.name(), ValueType.STRING)
				.buildSQL();
		results = this.getCrudService().executeSql(sql);

		tabDefs = new ArrayList<TabDef>();
		for (int i = 0; i < results.size(); i++) {
			IEntity[] entityArray = results.get(i);
			tabDefs.add((TabDef) entityArray[0]);
		}

		Assert.assertEquals(tabDefs.size(), 1);

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(ansichtTab_TabSpeig.getTabDef().getName());
		ITabSpeig tabSpeigEingabeNotwendig = viewHandle
				.findTabSpeigByTabAKeyAndSpalteAKey(ansichtTab_TabSpeig.getTabAKey(), "EINGABENOTWENDIG");
		((TabSpeig) tabSpeigEingabeNotwendig).setEingabeNotwendig(true);
		saveForTest(tabSpeigEingabeNotwendig);

		List<TabSpeig> tabSpeigs = new ArrayList<TabSpeig>();
		b = new DefaultOfdbQueryBuilder();
		sql = b.selectTable("TabSpeig", "tSpeig").fromTable("TabSpeig", "tSpeig")
				.andWhereRestriction("tSpeig", "eingabeNotwendig", OperatorEnum.Eq, Boolean.TRUE, ValueType.BOOLEAN)
				.buildSQL();
		results = this.getCrudService().executeSql(sql);
		tabSpeigs.clear();
		for (int i = 0; i < results.size(); i++) {
			IEntity[] entityArray = results.get(i);
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

		IAnsichtTab viewTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(), "tabDef",
				TabDef.class);

		this.applicationFactory.init();

		ITabSpeig tabSpeigMock = DomainMockFactory.createTabSpeigMock((TabDef) viewTab.getTabDef(), "ALIAS", 1,
				DBTYPE.STRING);
		TabSpeig tabSpeigId = DomainMockFactory.createTabSpeigMock((TabDef) viewTab.getTabDef(), "DSID", 2,
				DBTYPE.LONGINTEGER);
		tabSpeigId.setPrimSchluessel(Boolean.TRUE);

		// start test ...
		// List<TabSpeig> tList = new ArrayList<TabSpeig>();
		// tList.add( tabSpeigMock );
		// tList.add( tabSpeigId );
		// IOfdbService oService = this.ofdbService;
		ViewConfigHandle viewHandle = this.ofdbCacheManager
				.findViewConfigByTableName(tabSpeigMock.getTabDef().getName());

		OfdbPropMapper propMapMock = viewHandle.findPropertyMapperByTabProp(tabSpeigMock);
		// this.ofdbCacheManager.findPropertyMapperByTabSpeig( tabSpeigMock );
		String propNameMock = propMapMock.getPropertyName();
		OfdbPropMapper propMapId = viewHandle.findPropertyMapperByTabProp(tabSpeigId);
		// this.ofdbCacheManager.findPropertyMapperByTabSpeig( tabSpeigId );
		String propNameId = propMapId.getPropertyName();
		// Map<String, OfdbPropMapper> propMap = oService.loadPropertyMapping( tList );

		// Assert.assertTrue( propMap.size() == tList.size() );
		Assert.assertEquals(propNameMock, "alias");
		Assert.assertEquals(propNameId, Constants.SYS_PROP_ID);

	}

}
