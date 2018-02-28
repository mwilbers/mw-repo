package de.mw.mwdata.ofdb.service.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.daos.ICrudDao;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.ApplicationFactory;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.service.IPagingEntityService;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.ofdb.test.AbstractOfdbInitializationTest;

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
	private IPagingEntityService entityService;

	@Autowired
	private IOfdbService ofdbService;

	@Autowired
	private ApplicationFactory applicationFactory;

	// @Autowired
	// private IOfdbDao ofdbDao;

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

		// FIXME: use here getClass().getName() instead hardcoded packagename
		IAnsichtTab aTabBenutzerBereich = this.setUpAnsichtAndTab(TestConstants.TABLENAME_BENUTZERBEREICH,
				BenutzerBereich.class.getName(), "benutzerBereich", BenutzerBereich.class);
		IAnsichtTab aTabTabDef = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, TabDef.class.getName(),
				"tabDef", TabDef.class);

		this.applicationFactory.init();

		@SuppressWarnings("unchecked")
		PaginatedList<IEntity[]> paginatedResult = this.entityService.loadViewPaginated(TestConstants.TABLENAME_TABDEF,
				1);

		Assert.assertNotNull(paginatedResult);
		Assert.assertEquals(paginatedResult.getItems().size(), 2);
		Object[] item = paginatedResult.getItems().get(0);
		Assert.assertTrue(item.length == 1);
		Assert.assertTrue(item[0] instanceof TabDef);

		List<IEntity[]> entities = this.entityService.loadView(TestConstants.TABLENAME_TABDEF);
		Assert.assertEquals(entities.size(), 2);
		Assert.assertEquals(entities.get(0)[0], aTabBenutzerBereich.getTabDef());

		Map<String, IAnsichtSpalte> viewPropMap = this.ofdbService
				.findAnsichtSpaltenMapByAnsichtId(aTabTabDef.getAnsichtDef().getId());
		AnsichtSpalten ansichtSpalteMock = (AnsichtSpalten) viewPropMap.get("BEREICHSID");

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

		// register new
		this.applicationFactory.init();

		paginatedResult = this.entityService.loadViewPaginated(aTabTabDef.getTabDef().getName(), 1);

		Assert.assertNotNull(paginatedResult);
		Assert.assertEquals(paginatedResult.getItems().size(), 2);
		item = paginatedResult.getItems().get(0);
		Assert.assertTrue(item instanceof Object[]);
		Object[] resArray = item;
		Assert.assertTrue(resArray[0] instanceof TabDef);
		Assert.assertTrue(resArray[1].equals("Testbereich"));

		EntityTO<TabDef> entityTO = new EntityTO<TabDef>(new TabDef());
		// Map<String, MapValue> map = new HashMap<String, MapValue>();
		// map.put( "BenutzerBereich.name", new MapValue( "Testbereich" ) );
		// entityTO.setMap( map );
		entityTO.addJoinedValue("BenutzerBereich.name", "Testbereich");

		PaginatedList<IEntity[]> items = this.entityService.findByCriteriaPaginated(TestConstants.TABLENAME_TABDEF,
				entityTO, 0, null);

		Assert.assertEquals(items.getItems().size(), 2);

	}
}
