package de.mw.mwdata.ofdb.service.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.ApplicationFactory;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.ofdb.cache.ViewConfigFactory;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabDef.DATENBANK;
import de.mw.mwdata.ofdb.domain.ITabDef.ZEITTYP;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;
import de.mw.mwdata.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;
import de.mw.mwdata.ofdb.test.AbstractOfdbInitializationTest;

@ContextConfiguration(locations = { "classpath:/appContext-ofdb.xml", "classpath:/appContext-ofdb-test.xml",
		"classpath:/appContext-common-test-db.xml" })
@TransactionConfiguration
public class TabSpeigDefaultsTest extends AbstractOfdbInitializationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TabSpeigDefaultsTest.class);

	@Autowired
	private ViewConfigFactory viewConfigFactory;

	@Autowired
	private ApplicationFactory applicationFactory;

	@Test(enabled = true)
	public void testInsertDefaults_String_And_Enum() throws OfdbMissingMappingException {

		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("+++++ testInsertDefaults_String_And_Enum +++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");

		this.applicationFactory.configure();

		// FIXME: hier fehler, da AnsichtDef mit name = FX_TabDef_K zweimal persistiert
		// wurde
		// create some views and tables
		IAnsichtTab ansichtTabMock = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);
		ITabDef tabDef = ansichtTabMock.getTabDef();
		ViewConfigHandle viewHandleTabDef = this.viewConfigFactory
				.createViewConfiguration(ansichtTabMock.getAnsichtDef().getName());
		this.getOfdbCacheManger().registerView(viewHandleTabDef);

		IAnsichtTab ansichtTab_TabSpeig = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABSPEIG,
				TabSpeig.class.getName(), "tabSpeig", TabSpeig.class);
		ViewConfigHandle viewHandle = this.viewConfigFactory.createViewConfiguration(ansichtTab_TabSpeig.getName());
		this.getOfdbCacheManger().registerView(viewHandle);

		// define default values
		TabSpeig tabSpeig = (TabSpeig) viewHandleTabDef.findTabSpeigByTabAKeyAndSpalteAKey(tabDef.getName(), "ALIAS");
		tabSpeig.setDefaultWert("defaultAlias");
		saveForTest(tabSpeig);

		tabSpeig = (TabSpeig) viewHandleTabDef.findTabSpeigByTabAKeyAndSpalteAKey(tabDef.getName(), "DATENBANK");
		tabSpeig.setDefaultWert(DATENBANK.FW.name());
		saveForTest(tabSpeig);

		tabSpeig = (TabSpeig) viewHandleTabDef.findTabSpeigByTabAKeyAndSpalteAKey(tabDef.getName(), "ZEITTYP");
		tabSpeig.setDefaultWert(ZEITTYP.GUELTIGVON.name());
		saveForTest(tabSpeig);

		// unregister ofdb-configuration for this table
		this.getOfdbCacheManger().unregisterView(TestConstants.TABLENAME_TABDEF);

		// load configuration and register table
		this.applicationFactory.init();

		// start test
		String expected = "Testtable";
		TabDef t = DomainMockFactory.createTabDefMock(expected, this.getTestBereich(), !isAppInitialized());
		t.setAlias(null);
		t.setDatenbank(null);
		t.setZeittyp(null);

		saveForTest(t);

		// check defaults
		Assert.assertEquals(t.getAlias(), "defaultAlias", "No default-string set.");
		Assert.assertEquals(t.getDatenbank(), DATENBANK.FW, "No default-datenbank-enum set.");
		Assert.assertEquals(t.getZeittyp(), ZEITTYP.GUELTIGVON, "No default-zeittyp-enum set.");

	}

	@Test(enabled = true)
	public void test_InsertDefaults_Integer_And_Boolean_AndFind() throws OfdbMissingMappingException {

		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOGGER.info("+++++ test_InsertDefaults_Integer_And_Boolean_AndFind +++++++++");
		LOGGER.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");

		this.applicationFactory.configure();

		IAnsichtTab ansichtTab_TabDefMock = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF,
				TabDef.class.getName(), "tabDef", TabDef.class);
		IAnsichtTab ansichtTab_TabSpeig = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABSPEIG,
				TabSpeig.class.getName(), "tabSpeig", TabSpeig.class);
		ViewConfigHandle viewHandle = this.viewConfigFactory
				.createViewConfiguration(ansichtTab_TabSpeig.getAnsichtDef().getName());
		this.getOfdbCacheManger().registerView(viewHandle);
		ITabDef tabDef = ansichtTab_TabSpeig.getTabDef();

		// create TabSpeig and AnsichtSpalte "reihenfolge"
		TabSpeig tabSpeig = (TabSpeig) viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(tabDef.getName(), "REIHENFOLGE");
		tabSpeig.setDefaultWert("42");
		saveForTest(tabSpeig);

		// create TabSpeig and AnsichtSpalte "PS"
		tabSpeig = (TabSpeig) viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(tabDef.getName(), "PS");
		tabSpeig.setDefaultWert("true");
		saveForTest(tabSpeig);

		IAnsichtTab ansichtTab_ansichtSpalteMock = this.setUpAnsichtAndTab(TestConstants.TABLENAME_ANSICHTSPALTEN,
				AnsichtSpalten.class.getName(), "ansichtSpalten", AnsichtSpalten.class);

		IAnsichtSpalte aSpaltePS = null;
		for (IAnsichtSpalte aSpalte : viewHandle.getViewColumns()) {
			if (aSpalte.getName().equals("PS")) {
				aSpaltePS = aSpalte;
				break;
			}
		}
		// IAnsichtSpalte aSpaltePS = viewHandle.getViewColumns().get("PS");
		saveForTest(aSpaltePS);

		// load configuration and register table
		this.applicationFactory.init();

		// start test
		String expected = "TestColumn";
		ITabSpeig ts = DomainMockFactory.createTabSpeigMock((TabDef) tabDef, expected, 3, DBTYPE.LONGINTEGER);
		TabSpeig tsImpl = (TabSpeig) ts;
		tsImpl.setReihenfolge(null);
		tsImpl.setPrimSchluessel(null);
		saveForTest(ts);

		IAnsichtSpalte aSpalteTestColumn = DomainMockFactory
				.createAnsichtSpalteMock((AnsichtDef) ansichtTab_TabSpeig.getAnsichtDef(), ts, ansichtTab_TabSpeig);
		saveForTest(aSpalteTestColumn);
		Assert.assertEquals(ts.getReihenfolge(), Long.valueOf(42), "No default-integer set.");
		Assert.assertEquals(ts.getPrimSchluessel(), Boolean.TRUE, "No default boolean set.");

		// Test 4: check if custom fxboolean-values were set correctly in database
		String sql1 = " SELECT PS " //
				+ "  FROM " + TestConstants.TABLENAME_TABSPEIG + " WHERE TABDEFID = " + tabDef.getId() //
				+ " AND SPALTE = '" + expected + "' ";
		List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList(sql1);
		Assert.assertEquals(resultList.size(), 1, "Wrong number of result-rows.");
		BigDecimal dbDec = (BigDecimal) resultList.get(0).get("PS");
		Assert.assertTrue(dbDec.equals(new BigDecimal(Constants.SYS_VAL_TRUE)), "Database-Value of PS not -1 (true).");

	}
}
