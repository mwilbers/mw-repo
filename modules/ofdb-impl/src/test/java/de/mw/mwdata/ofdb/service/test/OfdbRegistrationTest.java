package de.mw.mwdata.ofdb.service.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.ApplicationFactory;
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
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.ofdb.test.AbstractOfdbInitializationTest;
import de.mw.mwdata.ofdb.test.impl.ApplicationTestFactory;

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
	private ApplicationFactory applicationFactory;

	@Autowired
	private ICrudDao crudDao;

	@Test(enabled = true)
	public void testEmptyRegistration() throws OfdbMissingMappingException {

		this.applicationFactory.configure();
		this.applicationFactory.init();

		boolean registered = this.getOfdbCacheManger().isViewRegistered(TestConstants.TABLENAME_TABDEF);
		Assert.assertFalse(registered, "View already registered.");

		this.applicationFactory.configure();

		IAnsichtTab ansichtTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);

		saveForTest(ansichtTab.getTabDef());
		this.applicationFactory.init();

		Assert.assertTrue(this.getOfdbCacheManger().isViewRegistered(TestConstants.TABLENAME_TABDEF),
				"Table should be registered.");

		Map<String, String> sortColums = new HashMap<String, String>();
		sortColums.put("name", "asc");
		List<TabDef> tabDefs = this.crudDao.findAll(TabDef.class, sortColums);
		TabDef t = tabDefs.get(0);
		Assert.assertEquals(t.getFullClassName(), TabDef.class.getName());

	}

	@Test
	public void testRegisterViewWithMissingUrlPath() throws OfdbMissingMappingException {

		this.applicationFactory.configure();

		IAnsichtTab ansichtTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);

		AnsichtDef ansichtDef = (AnsichtDef) ansichtTab.getAnsichtDef();
		ansichtDef.setUrlPath(StringUtils.EMPTY);
		saveForTest(ansichtDef);

		try {
			this.applicationFactory.init();
			// this.ofdbInitializer.init( "admin" );
		} catch (OfdbInvalidConfigurationException e) {
			// ok, because TabDef.fullClassName is missing
		}
		Assert.assertFalse(this.getOfdbCacheManger().isViewRegistered(TestConstants.TABLENAME_TABDEF),
				"Table should not be registered.");

	}

	@Test
	public void testRegisterViewWithMissingFullClassName() throws OfdbMissingMappingException {

		this.applicationFactory.configure();

		IAnsichtTab ansichtTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);

		ansichtTab.getTabDef().setFullClassName(StringUtils.EMPTY);
		saveForTest(ansichtTab.getTabDef());

		try {
			this.applicationFactory.init();
			// this.ofdbInitializer.init( "admin" );
			Assert.fail();
		} catch (OfdbInvalidConfigurationException e) {
			// ok, because TabDef.fullClassName is missing
		}
		Assert.assertFalse(this.getOfdbCacheManger().isViewRegistered(TestConstants.TABLENAME_TABDEF),
				"Table should not be registered.");

	}

	@Test(enabled = true)
	public void testRegistration_And_1JoinResult() throws OfdbMissingMappingException {

		this.applicationFactory.configure();

		IAnsichtTab aTabBenutzerBereich = this.setUpAnsichtAndTab(TestConstants.TABLENAME_BENUTZERBEREICH,
				BenutzerBereich.class.getName(), "benutzerBereich", BenutzerBereich.class);
		IAnsichtTab aTabTabDef = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);

		this.applicationFactory.init();

		PagingModel pagingModel = new PagingModel(100, 1);

		@SuppressWarnings("unchecked")
		QueryResult paginatedResult = this.entityService.executeViewQuery(TestConstants.TABLENAME_TABDEF, pagingModel);

		Assert.assertNotNull(paginatedResult);
		Assert.assertEquals(paginatedResult.getRows().size(), 2);

		// Object[] item = paginatedResult.getRows().get(0);
		Object[] row = paginatedResult.getRows().get(0);
		Assert.assertTrue(row.length == 1);
		// Object[] item = paginatedResult.getRows().get(0);
		// Assert.assertTrue(item.length == 1);
		Assert.assertTrue(row[0] instanceof TabDef);

		QueryResult queryResult = this.entityService.executeViewQuery(TestConstants.TABLENAME_TABDEF, pagingModel);
		List<IEntity[]> entities = queryResult.getRows();
		// .loadView(TestConstants.TABLENAME_TABDEF);
		Assert.assertEquals(entities.size(), 2);
		Assert.assertEquals(entities.get(0)[0], aTabBenutzerBereich.getTabDef());

		List<IAnsichtSpalte> viewPropList = this.ofdbService
				.findAnsichtSpaltenByAnsichtId(aTabTabDef.getAnsichtDef().getId());

		IAnsichtSpalte aSpalteBereichsId = null;
		for (IAnsichtSpalte aSpalte : viewPropList) {
			if (aSpalte.getName().equals("BEREICHSID")) {
				aSpalteBereichsId = aSpalte;
				break;
			}
		}
		AnsichtSpalten ansichtSpalteMock = (AnsichtSpalten) aSpalteBereichsId; // (AnsichtSpalten)
																				// viewPropMap.get("BEREICHSID");

		// find column "NAME" from table BenutzerBereicheDef and add it to db
		ViewConfigHandle viewHandleBereich = this.getOfdbCacheManger()
				.findViewConfigByTableName(aTabBenutzerBereich.getTabDef().getName());
		ITabSpeig tablePropBereichName = viewHandleBereich.findTablePropByProperty(aTabBenutzerBereich.getTabDef(),
				"name", false);

		// FIXME: method configure should only be inherited from test based interface
		this.applicationFactory.configure();

		// 2.test: reset cache
		this.getOfdbCacheManger().unregisterView(ansichtSpalteMock.getAnsichtDef().getName());
		this.getOfdbCacheManger().unregisterView(aTabBenutzerBereich.getAnsichtDef().getName());

		// link TabDef.BereichsID with BenutzerBereicheDef.BereichsId
		ansichtSpalteMock.setAnsichtSuchen(TestConstants.TABLENAME_BENUTZERBEREICH);
		ansichtSpalteMock.setSuchwertAusTabAKey(TestConstants.TABLENAME_BENUTZERBEREICH);
		ansichtSpalteMock.setSuchwertAusSpalteAKey("BEREICHSID");
		ansichtSpalteMock.setVerdeckenDurchTabAKey(TestConstants.TABLENAME_BENUTZERBEREICH);
		ansichtSpalteMock.setVerdeckenDurchSpalteAKey("NAME");

		IAnsichtTab ansichtTabMock = DomainMockFactory.createAnsichtTabMock((AnsichtDef) aTabTabDef.getAnsichtDef(),
				(TabDef) aTabBenutzerBereich.getTabDef(), 2, "=", "BEREICHSID", "BEREICHSID", "FX_TabDef_K",
				!isAppInitialized());

		saveForTest(ansichtTabMock);

		AnsichtSpalten viewColBereichName = DomainMockFactory
				.createAnsichtSpalteMock((AnsichtDef) aTabTabDef.getAnsichtDef(), tablePropBereichName, ansichtTabMock);
		viewColBereichName.setAngelegtAm(new Date());
		viewColBereichName.setAngelegtVon(Constants.SYS_USER_DEFAULT);
		this.saveForTest(viewColBereichName);
		// ITabSpeig tabPropBereichName =
		// DomainMockFactory.createTabSpeigMock(aTabBenutzerBereich.getTabDef(), "Name",
		// 100, DBTYPE.STRING);
		// this.saveForTest(tabPropBereichName);
		//
		// AnsichtSpalten viewColBereichName = DomainMockFactory
		// .createAnsichtSpalteMock((AnsichtDef) aTabTabDef.getAnsichtDef(),
		// tabPropBereichName, aTabTabDef);
		// viewColBereichName.setAngelegtAm(new Date());
		// viewColBereichName.setAngelegtVon(Constants.SYS_USER_DEFAULT);
		// this.saveForTest(viewColBereichName);

		// register new
		this.applicationFactory.init();

		pagingModel = new PagingModel(100, 1);

		paginatedResult = this.entityService.executeViewQuery(aTabTabDef.getTabDef().getName(), pagingModel);

		Assert.assertNotNull(paginatedResult);
		Assert.assertEquals(paginatedResult.getRows().size(), 2);

		// FIXME: should be improved by internal access methods of QueryResult
		row = paginatedResult.getRows().get(0);

		// item = paginatedResult.getRows().get(0);
		// Assert.assertTrue(item instanceof Object[]);
		// Object[] resArray = item;
		Assert.assertTrue(row[0] instanceof TabDef);

		ApplicationTestFactory appFactory = (ApplicationTestFactory) this.applicationFactory;

		// ... hier fehler, da Bereich.name über die AnsichtSpalten noch nicht
		// eingeblendet ist.
		// Regel: wenn eine solche AnsichtSpalte fehlt, dann nur interne
		// entity-verknüpfung über JoinedPropertyTO
		// falls vorhanden, dann als alias ins QueryModel (ViewMetaDataGenerator) mit
		// aufnehmen und hier auf index 1 auswerten
		// siehe auch beispiele etwa in FO_REchnungsDef in KD_RRE_PROD

		Assert.assertTrue(row[1].equals(
				appFactory.getApplicationConfigService().getPropertyValue(ApplicationConfigService.KEY_USERAREA)));

		EntityTO<TabDef> entityTO = new EntityTO<TabDef>(new TabDef());
		// OfdbEntityMapping entityMapping =
		// this.getOfdbCacheManger().getEntityMapping(aTabTabDef.getTabDef().getName());
		// OfdbPropMapper propMapping =
		// entityMapping.getMapper(ansichtSpalteMock.getTabSpEig());

		// ViewConfigHandle viewHandle =
		// this.getOfdbCacheManger().getViewConfig(aTabTabDef.getName());
		// ITabSpeig suchTabSpeig = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(
		// ansichtSpalteMock.getVerdeckenDurchTabAKey(),
		// ansichtSpalteMock.getVerdeckenDurchSpalteAKey());

		// String mappedPropName =
		// this.getOfdbService().mapTabSpeig2Property(suchTabSpeig);

		entityTO.addJoinedValue(
				appFactory.getApplicationConfigService().getPropertyValue(ApplicationConfigService.KEY_USERAREA));

		pagingModel = new PagingModel(100, 0);

		PaginatedList<IEntity[]> items = this.entityService.executePaginatedViewQuery(TestConstants.TABLENAME_TABDEF,
				entityTO, pagingModel, null);

		Assert.assertEquals(items.getItems().size(), 2);

	}
}
