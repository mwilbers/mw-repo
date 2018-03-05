package de.mw.mwdata.ofdb.service.test;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.ofdb.ApplicationFactory;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.ofdb.exception.OfdbRuntimeException;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.ofdb.cache.ViewConfigValidationResultSet;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;
import de.mw.mwdata.ofdb.test.AbstractOfdbInitializationTest;

@Test
@ContextConfiguration(locations = { "classpath:/appContext-ofdb.xml", "classpath:/appContext-ofdb-test.xml",
		"classpath:/appContext-common-test-db.xml" })
public class OfdbValidationTest extends AbstractOfdbInitializationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfdbValidationTest.class);

	@Autowired
	private ApplicationFactory applicationFactory;

	@Test
	public void testRegisterViewWithEmptyAnsichtDef() throws OfdbMissingMappingException {

		this.applicationFactory.init();

		List<IAnsichtTab> ansichtTabList = new ArrayList<IAnsichtTab>();
		IAnsichtDef ansichtDef = new AnsichtDef();
		ViewConfigValidationResultSet resultSet = this.getOfdbService().isAnsichtTabListValid(ansichtDef,
				ansichtTabList);
		Assert.assertFalse(resultSet.hasErrors(), "Empty AnsichtTab-List not valid if ansichtDef is not empty.");
		resultSet = new ViewConfigValidationResultSet();

	}

	@Test(enabled = true)
	public void testRegisterViewWithInvalidAnsichtDef() throws OfdbMissingMappingException {

		this.applicationFactory.configure();

		List<IAnsichtTab> ansichtTabList = new ArrayList<IAnsichtTab>();
		ViewConfigValidationResultSet resultSet = new ViewConfigValidationResultSet();

		AnsichtTab ansichtTab1 = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);
		ansichtTabList.add(ansichtTab1);

		AnsichtTab ansichtTabMock = DomainMockFactory.createAnsichtTabMock(ansichtTab1.getAnsichtDef(),
				(TabDef) ansichtTab1.getTabDef(), 2, "x", null, null, null, !isAppInitialized());
		// configure second valid TabDef-Reference for test
		ansichtTabMock.setTabAKey(TestConstants.TABLENAME_TABDEF + "someAliasSuffix");
		ansichtTabMock.setTabelle(TestConstants.TABLENAME_TABDEF);
		saveForTest(ansichtTabMock);

		try {
			this.applicationFactory.init();
			Assert.fail("Test failed. validation of ViewTab should alert duplicated usage of with Jointype 'x'.");
		} catch (OfdbInvalidConfigurationException e) {
			// ok, go on
		}

		ansichtTabMock.setJoinTyp("left");
		saveForTest(ansichtTabMock);
		this.applicationFactory.init();

		ansichtTabList.add(ansichtTabMock);

		resultSet = this.getOfdbService().isAnsichtTabListValid(ansichtTabMock.getAnsichtDef(), ansichtTabList);
		Assert.assertFalse(resultSet.hasErrors());

		// simulate duplicate tableName -> should fail
		ansichtTabMock.setTabAKey(TestConstants.TABLENAME_TABDEF);
		resultSet = this.getOfdbService().isAnsichtTabListValid(ansichtTabMock.getAnsichtDef(), ansichtTabList);
		Assert.assertTrue(resultSet.hasErrors());
		LOGGER.error(resultSet.toString());

		// simulate missing original tableName when alias is different -> should fail
		ansichtTabMock.setTabAKey(TestConstants.TABLENAME_TABDEF + "someAliasSuffix");
		ansichtTabMock.setTabelle(null);
		resultSet = this.getOfdbService().isAnsichtTabListValid(ansichtTabMock.getAnsichtDef(), ansichtTabList);
		Assert.assertTrue(resultSet.hasErrors());
		LOGGER.error(resultSet.toString());

	}

	@Test
	public void testUniqueCheck() throws OfdbMissingMappingException {

		// ... test
		// 1. alles fixen
		// 2. foreign keys testen
		// 3. debuggen: chain construkt, checkUniques

		this.applicationFactory.configure();

		// 1. build up ofdb configuration
		IAnsichtTab ansichtTab_config = this.setUpAnsichtAndTab(TestConstants.TABLENAME_ANSICHTDEF,
				AnsichtDef.class.getName(), "ansichtDef", AnsichtDef.class);
		ITabDef tabDef = ansichtTab_config.getTabDef();
		List<ITabSpeig> registeredTabSpeigs = this.getOfdbService().loadTablePropListByTableName(tabDef.getName());
		ITabSpeig tabSpeig = null;
		for (ITabSpeig t : registeredTabSpeigs) {
			if (t.getSpalte().toUpperCase().equals("URLPATH")) {
				tabSpeig = t;
				break;
			}
		}

		tabSpeig.setEindeutig(Long.valueOf(10l));
		saveForTest(tabSpeig);

		this.applicationFactory.init();

		// 2. execute test: insert table two times
		AnsichtDef view1 = DomainMockFactory.createAnsichtDefMock("Testansicht", getTestBereich(), !isAppInitialized());
		view1.setUrlPath("duplicatedUrlPath");
		saveForTest(view1);

		AnsichtDef view2 = DomainMockFactory.createAnsichtDefMock("Testansicht2", getTestBereich(),
				!isAppInitialized());
		view2.setUrlPath("notduplicatedUrlPath");
		saveForTest(view2);

		// set error state
		view2.setUrlPath("duplicatedUrlPath");
		try {
			saveForTest(view2);
			Assert.fail();
		} catch (OfdbRuntimeException e) {
			// test ok
			LOGGER.info("Exception thrown, Test ok.");
		}

	}
}
