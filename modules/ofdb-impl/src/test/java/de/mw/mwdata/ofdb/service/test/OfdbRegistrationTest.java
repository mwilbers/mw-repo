package de.mw.mwdata.ofdb.service.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.daos.ICrudDao;
import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.core.service.IViewService;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.ofdb.test.AbstractOfdbInitializationTest;
import de.mw.mwdata.ofdb.test.impl.ConfigurableApplicationFactory;
import de.mw.mwdata.ofdb.test.impl.TestApplicationFactory;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Nov, 2010
 *
 */
@Test(enabled = true)
@ContextConfiguration(locations = { "classpath:/appContext-ofdb.xml", "classpath:/appContext-ofdb-test.xml",
		"classpath:/appContext-common-test-db.xml" })
public class OfdbRegistrationTest extends AbstractOfdbInitializationTest {

	@Autowired
	private IViewService<IEntity> entityService;

	@Autowired
	private IOfdbService ofdbService;

	@Autowired
	private ConfigurableApplicationFactory applicationFactory;

	@Autowired
	private ICrudDao crudDao;

	@BeforeMethod
	public void prepareOfdb() {

		this.applicationFactory.configure();
		IAnsichtTab a2 = this.setUpAnsichtAndTab(TestConstants.TABLENAME_BENUTZERBEREICH, "benutzerBereich",
				BenutzerBereich.class);
		saveForTest(a2.getTabDef());
		IAnsichtTab ansichtTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, "tabDef", TabDef.class);
		saveForTest(ansichtTab.getTabDef());
		this.applicationFactory.init();

	}

	@Test(enabled = true)
	public void testEmptyRegistration() throws OfdbMissingMappingException {

		Assert.assertTrue(this.getOfdbCacheManger().isViewRegistered(TestConstants.TABLENAME_TABDEF),
				"Table should be registered.");

		Map<String, String> sortColums = new HashMap<String, String>();
		sortColums.put("name", "asc");
		List<TabDef> tabDefs = this.crudDao.findAll(TabDef.class, sortColums);
		Assert.assertTrue(tabDefs.size() == 2);
		TabDef t = tabDefs.get(1);
		Assert.assertEquals(t.getFullClassName(), TabDef.class.getName());

	}

	@Test
	public void testRegisterViewWithMissingUrlPath() throws OfdbMissingMappingException {

		this.getOfdbCacheManger().unregisterView(TestConstants.TABLENAME_TABDEF);
		this.applicationFactory.configure();

		IAnsichtTab viewTab_tableDef = (IAnsichtTab) this.getCrudService().findByName(IAnsichtTab.class,
				TestConstants.TABLENAME_TABDEF);

		AnsichtDef ansichtDef = (AnsichtDef) viewTab_tableDef.getAnsichtDef();
		ansichtDef.setUrlPath(StringUtils.EMPTY);
		saveForTest(ansichtDef);

		try {
			this.applicationFactory.init();
		} catch (OfdbInvalidConfigurationException e) {
			// ok, because TabDef.fullClassName is missing
		}
		Assert.assertFalse(this.getOfdbCacheManger().isViewRegistered(TestConstants.TABLENAME_TABDEF),
				"Table should not be registered.");

	}

	@Test
	public void testRegisterViewWithMissingFullClassName() throws OfdbMissingMappingException {

		this.getOfdbCacheManger().unregisterView(TestConstants.TABLENAME_TABDEF);
		this.applicationFactory.configure();

		ITabDef tabDef_tabDef = (ITabDef) this.getCrudService().findByName(TabDef.class,
				TestConstants.TABLENAME_TABDEF);
		tabDef_tabDef.setFullClassName(StringUtils.EMPTY);
		saveForTest(tabDef_tabDef);

		try {
			this.applicationFactory.init();
			Assert.fail();
		} catch (OfdbInvalidConfigurationException e) {
			// ok, because TabDef.fullClassName is missing
		}
		Assert.assertFalse(this.getOfdbCacheManger().isViewRegistered(TestConstants.TABLENAME_TABDEF),
				"Table should not be registered.");

	}

	@Test(enabled = true)
	public void testRegistration_And_1JoinResult() throws OfdbMissingMappingException {

		this.getOfdbCacheManger().unregisterView(TestConstants.TABLENAME_TABDEF);
		this.getOfdbCacheManger().unregisterView(TestConstants.TABLENAME_BENUTZERBEREICH);

		this.applicationFactory.configure();

		IAnsichtTab viewTab_BenutzerBereich = (IAnsichtTab) this.getCrudService().findByName(IAnsichtTab.class,
				TestConstants.TABLENAME_BENUTZERBEREICH);
		IAnsichtTab viewTab_tableDef = (IAnsichtTab) this.getCrudService().findByName(IAnsichtTab.class,
				TestConstants.TABLENAME_TABDEF);

		this.applicationFactory.init();

		PagingModel pagingModel = new PagingModel(100, 1);

		@SuppressWarnings("unchecked")
		QueryResult paginatedResult = this.entityService.executeViewQuery(TestConstants.TABLENAME_TABDEF, pagingModel);

		Assert.assertNotNull(paginatedResult);
		Assert.assertEquals(paginatedResult.size(), 2); // BenutzerBereicheDef und FX_TabDef_K
		Assert.assertTrue(paginatedResult.columnSize() == 1);
		Assert.assertTrue(paginatedResult.getEntityByRowIndex(0) instanceof TabDef);
		Assert.assertEquals(paginatedResult.getEntityByRowIndex(0), viewTab_BenutzerBereich.getTabDef());

		List<IAnsichtSpalte> viewPropList = this.ofdbService
				.findAnsichtSpaltenByAnsichtId(viewTab_tableDef.getAnsichtDef().getId());

		AnsichtSpalten viewColumn_bereichsId = null;
		for (IAnsichtSpalte aSpalte : viewPropList) {
			if (aSpalte.getName().equals("BEREICHSID")) {
				viewColumn_bereichsId = (AnsichtSpalten) aSpalte;
				break;
			}
		}

		// find column "NAME" from table BenutzerBereicheDef and add it to db
		ViewConfigHandle viewHandleBereich = this.getOfdbCacheManger()
				.findViewConfigByTableName(viewTab_BenutzerBereich.getTabDef().getName());
		ITabSpeig tablePropBereichName = viewHandleBereich.findTablePropByProperty(viewTab_BenutzerBereich.getTabDef(),
				"name", false);

		this.applicationFactory.configure();

		// 2.test: reset cache
		this.getOfdbCacheManger().unregisterView(viewColumn_bereichsId.getAnsichtDef().getName());
		this.getOfdbCacheManger().unregisterView(viewTab_BenutzerBereich.getAnsichtDef().getName());

		// link TabDef.BereichsID with BenutzerBereicheDef.BereichsId
		viewColumn_bereichsId.setAnsichtSuchen(TestConstants.TABLENAME_BENUTZERBEREICH);
		viewColumn_bereichsId.setSuchwertAusTabAKey(TestConstants.TABLENAME_BENUTZERBEREICH);
		viewColumn_bereichsId.setSuchwertAusSpalteAKey("BEREICHSID");
		viewColumn_bereichsId.setVerdeckenDurchTabAKey(TestConstants.TABLENAME_BENUTZERBEREICH);
		viewColumn_bereichsId.setVerdeckenDurchSpalteAKey("NAME");

		// create AnsichtTab TableDef -> BenutzerBereich
		IAnsichtTab viewTab_tableDef_BenBereich = DomainMockFactory.createAnsichtTabMock(
				(AnsichtDef) viewTab_tableDef.getAnsichtDef(), (TabDef) viewTab_BenutzerBereich.getTabDef(), 2, "=",
				"BEREICHSID", "BEREICHSID", "FX_TabDef_K", !isAppInitialized());
		saveForTest(viewTab_tableDef_BenBereich);

		// create additional table column Bereich.name for view TableDef
		AnsichtSpalten viewColBereichName = DomainMockFactory.createAnsichtSpalteMock(
				(AnsichtDef) viewTab_tableDef.getAnsichtDef(), tablePropBereichName, viewTab_tableDef_BenBereich);
		viewColBereichName.setAngelegtAm(new Date());
		viewColBereichName.setAngelegtVon(Constants.SYS_USER_DEFAULT);
		this.saveForTest(viewColBereichName);

		// register new
		this.applicationFactory.init();

		pagingModel = new PagingModel(100, 1);

		paginatedResult = this.entityService.executeViewQuery(viewTab_tableDef.getTabDef().getName(), pagingModel);

		Assert.assertNotNull(paginatedResult);
		Assert.assertEquals(paginatedResult.size(), 2);
		Assert.assertTrue(paginatedResult.getEntityByRowIndex(0) instanceof TabDef);

		TestApplicationFactory appFactory = (TestApplicationFactory) this.applicationFactory;

		// ... hier fehler, da Bereich.name über die AnsichtSpalten noch nicht
		// eingeblendet ist.
		// Regel: wenn eine solche AnsichtSpalte fehlt, dann nur interne
		// entity-verknüpfung über JoinedPropertyTO
		// falls vorhanden, dann als alias ins QueryModel (ViewMetaDataGenerator) mit
		// aufnehmen und hier auf index 1 auswerten
		// siehe auch beispiele etwa in FO_REchnungsDef in KD_RRE_PROD

		Assert.assertTrue(paginatedResult.getAliasByRowAndColumnIndex(0, 1).equals(
				appFactory.getApplicationConfigService().getPropertyValue(ApplicationConfigService.KEY_USERAREA)));

		EntityTO<TabDef> entityTO = new EntityTO<TabDef>(new TabDef());

		entityTO.addJoinedValue(
				appFactory.getApplicationConfigService().getPropertyValue(ApplicationConfigService.KEY_USERAREA));

		pagingModel = new PagingModel(100, 0);

		QueryResult queryResult = this.entityService.executePaginatedViewQuery(TestConstants.TABLENAME_TABDEF, entityTO,
				pagingModel, null);

		Assert.assertEquals(queryResult.size(), 2);

		// next test changing additional property only by id: change bereichsId from 1
		// (Administrator) to 2 (Testbereich2)
		TabDef tabDef_tabDef = (TabDef) viewTab_tableDef.getTabDef();
		BenutzerBereich testBereich2 = DomainMockFactory.createBenutzerBereichMock("Testbereich2");
		this.crudDao.insert(testBereich2);

		tabDef_tabDef.setBereichsId(testBereich2.getId());

		Assert.assertEquals(tabDef_tabDef.getBereich().getName(), "Administrator");
		this.getCrudService().insert(tabDef_tabDef);
		Assert.assertEquals(tabDef_tabDef.getBereich().getName(), "Testbereich2");
	}
}
