package de.mw.mwdata.ofdb.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.mw.mwdata.core.ApplicationState;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.daos.ICrudDao;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.Sequence;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.dao.IOfdbDao;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.AnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.ofdb.test.impl.ConfigurableApplicationFactory;
import de.mw.mwdata.ofdb.test.impl.TestApplicationFactory;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class AbstractOfdbInitializationTest<T> extends AbstractTransactionalTestNGSpringContextTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOfdbInitializationTest.class);

	private List<Sequence> sequenceCache = new ArrayList<Sequence>();

	@Autowired
	private IOfdbService ofdbService;

	@Autowired
	private ICrudService crudService;

	@Autowired
	protected ConfigurableApplicationFactory applicationFactory;

	// entnommen aus zeb-hasql-dbscript:
	// SET DATABASE SQL SYNTAX ORA TRUE
	// SET WRITE_DELAY 0
	// CREATE USER SA PASSWORD ""
	// ALTER USER SA SET LOCAL TRUE
	// CREATE SCHEMA PUBLIC AUTHORIZATION DBA
	// SET SCHEMA PUBLIC
	// CREATE MEMORY TABLE "USER_TAB_COLUMNS"("TABLE_NAME" VARCHAR(50) NOT NULL
	// PRIMARY KEY,"COLUMN_NAME" VARCHAR(100))
	// SET DATABASE DEFAULT INITIAL SCHEMA PUBLIC
	// GRANT DBA TO SA

	@Autowired
	@Qualifier(value = "crudDao")
	protected ICrudDao<BenutzerBereich> benutzerBereichDao;

	private BenutzerBereich testBereich;

	@Autowired
	@Qualifier(value = "crudDao")
	protected ICrudDao<Sequence> ofdbSequenceDao;

	@Autowired
	protected OfdbCacheManager ofdbCacheManager;

	@Autowired
	private IOfdbDao ofdbDao;

	@Autowired
	private ICrudDao<? extends AbstractMWEntity> crudDao;

	private Stack<IEntity> entityStack = new Stack<IEntity>();

	protected ICrudDao getCrudDao() {
		return this.crudDao;
	}

	@BeforeClass
	public void setUp() {

		LOGGER.info("####################################################");
		LOGGER.info("########setUp " + this.getClass().getName() + "#####");
		LOGGER.info("####################################################");

		Sequence sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_SYSSEQUENZ, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);

		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_TABDEF, 1l, 0l);

		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);
		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_TABSPEIG, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);
		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_MENUE, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);
		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_BENUTZERBEREICH, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);
		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_ANSICHT, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);
		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_BENUTZER, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);
		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_ANSICHTORDER, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);
		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_ANSICHTTAB, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);
		sequence = DomainMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_ANSICHTSPALTEN, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);

		TestApplicationFactory appFactory = (TestApplicationFactory) this.applicationFactory;
		this.testBereich = DomainMockFactory.createBenutzerBereichMock(
				appFactory.getApplicationConfigService().getPropertyValue(ApplicationConfigService.KEY_USERAREA));
		this.benutzerBereichDao.insert(this.testBereich);

	}

	protected boolean isAppInitialized() {
		return this.applicationFactory.getState().equals(ApplicationState.RUNNING);
	}

	protected TabDef saveOrUpdateTabDefAndTableProps(final String tableName, final String fullClassName,
			final OfdbEntityMapping entityMapping) {

		// save TabDef
		TabDef tabDef = (TabDef) this.getCrudDao().findByName(TabDef.class, tableName);
		if (null == tabDef) {
			tabDef = DomainMockFactory.createTabDefMock(tableName, this.getTestBereich(), !isAppInitialized());
			tabDef.setFullClassName(fullClassName);

			saveForTest(tabDef);

			// create and save all Tabprops
			saveOrUpdateAllTabProps(entityMapping, tabDef);

		} else {
			tabDef.setFullClassName(fullClassName);
			this.getCrudDao().update(tabDef);
		}

		return tabDef;
	}

	protected void saveOrUpdateAllTabProps(final OfdbEntityMapping entityMapping, final TabDef tabDef) {

		int reihenfolge = 0;
		for (OfdbPropMapper mapper : entityMapping.getMappings()) {
			reihenfolge++;

			ITabSpeig tabSpeig = DomainMockFactory.createTabSpeigMock(tabDef, mapper.getColumnName(), reihenfolge,
					mapper.getDbType());
			TabSpeig tabSpeigImpl = (TabSpeig) tabSpeig;

			if ("ANGELEGTAM".equals(mapper.getColumnName().toUpperCase())) {
				tabSpeigImpl.setDefaultWert(Constants.MWDATADEFAULT.NOW.getName());
			}
			if ("ANGELEGTVON".equals(mapper.getColumnName().toUpperCase())) {
				tabSpeigImpl.setDefaultWert(Constants.MWDATADEFAULT.USERID.getName());
			}

			saveForTest(tabSpeig);
		}

	}

	protected AnsichtTab setUpAnsichtAndTab(final String tableName, final String urlPath,
			final Class<? extends AbstractMWEntity> type) {

		OfdbEntityMapping entityMapping = this.getOfdbDao().initializeMapping(type, tableName);

		// save TabDef
		TabDef tabDef = (TabDef) this.getCrudService().findByName((Class<T>) TabDef.class, tableName);

		// ... flush an hibernate crudDao.delete löscht alle tabDefs...
		// http://stackoverflow.com/questions/26597440/how-do-you-test-spring-transactional-without-just-hitting-hibernate-level-1-cac

		if (null == tabDef) {
			tabDef = DomainMockFactory.createTabDefMock(tableName, this.getTestBereich(), !isAppInitialized());
			tabDef.setFullClassName(type.getName());

			saveForTest(tabDef);

			// create and save all Tabprops
			saveOrUpdateAllTabProps(entityMapping, tabDef);

		} else {
			tabDef.setFullClassName(type.getName());
			this.getCrudDao().update(tabDef);
		}

		AnsichtDef ansichtDefMock = (AnsichtDef) this.getCrudService().findByName((Class<T>) AnsichtDef.class,
				tableName);
		AnsichtTab ansichtTabMock = null;
		if (null == ansichtDefMock) {
			ansichtDefMock = DomainMockFactory.createAnsichtDefMock(tableName, this.getTestBereich(),
					!isAppInitialized());
			ansichtDefMock.setUrlPath(urlPath);
			saveForTest(ansichtDefMock);

			// save AnsichtTab
			ansichtTabMock = DomainMockFactory.createAnsichtTabMock(ansichtDefMock, tabDef, 1, "x", null, null, null,
					!isAppInitialized());
			saveForTest(ansichtTabMock);

			// create and save all ViewColumns
			saveOrUpdateAllViewProps(entityMapping, ansichtDefMock, ansichtTabMock);

		} else {
			ansichtDefMock.setUrlPath(urlPath);
			this.getCrudDao().update(ansichtDefMock);

			ansichtTabMock = (AnsichtTab) this.getCrudService().findByName((Class<T>) AnsichtTab.class, tableName);

		}

		return ansichtTabMock;
	}

	private ITabSpeig loadTablePropByTableName(final String tableName, final String columnName) {

		List<ITabSpeig> tableProps = this.getOfdbService().loadTablePropListByTableName(tableName);
		for (ITabSpeig tabSpeig : tableProps) {
			if (columnName.equals(tabSpeig.getSpalte())) {
				return tabSpeig;
			}
		}

		return null;
	}

	private void saveOrUpdateAllViewProps(final OfdbEntityMapping entityMapping, final AnsichtDef ansichtDefMock,
			final IAnsichtTab ansichtTabMock) {

		for (OfdbPropMapper mapper : entityMapping.getMappings()) {

			ITabSpeig tabProp = loadTablePropByTableName(ansichtTabMock.getTabDef().getName(), mapper.getColumnName());
			AnsichtSpalten viewColumn = DomainMockFactory.createAnsichtSpalteMock(ansichtDefMock, tabProp,
					ansichtTabMock);

			ITabSpeig tableProp = loadTablePropByTableName(mapper.getTableName(), mapper.getColumnName());
			viewColumn.setTabSpEig(tableProp);
			viewColumn.setViewTab(ansichtTabMock);

			if (!isAppInitialized()) {
				viewColumn.setAngelegtAm(new Date());
				viewColumn.setAngelegtVon(Constants.SYS_USER_DEFAULT);
			}

			saveForTest(viewColumn);
		}

	}

	@AfterClass
	public void tearDownAfterClass() {
		for (Sequence sequence : this.sequenceCache) {
			this.ofdbSequenceDao.delete(sequence);
		}

		this.benutzerBereichDao.delete(this.testBereich);

	}

	@AfterMethod
	public void tearDown() throws Exception {

		LOGGER.info("####################################################");
		LOGGER.info("########tearDown " + this.getClass().getName() + "#####");
		LOGGER.info("####################################################");

		LOGGER.warn("Deleting entityStack: " + this.entityStack.toString());

		// FIXME: do delete all entities after method or better after class ?
		while (!this.entityStack.empty()) {
			IEntity entity = this.entityStack.pop();

			if (entity instanceof AnsichtDef) {
				this.ofdbCacheManager.unregisterView(entity.getName());
			}

			this.getCrudDao().delete(entity);

		}

		// this.clearDatabase();

		// TODO: statt alle Datensätze wieder löschen ausprobieren: session.rollback !?!
		// siehe folgendes beispiel
		// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		// session.beginTransaction();
		// Long id = (Long) session.save(chg);
		//
		// session.getTransaction().commit();
		// session.close();

	}

	@Override
	@Autowired
	@Qualifier(value = "mwSpyDataSource")
	@Test(enabled = false)
	public void setDataSource(final DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	protected IOfdbService getOfdbService() {
		return this.ofdbService;
	}

	protected ICrudService<T> getCrudService() {
		return this.crudService;
	}

	protected BenutzerBereich getTestBereich() {
		return this.testBereich;
	}

	protected OfdbCacheManager getOfdbCacheManger() {
		return this.ofdbCacheManager;
	}

	protected IOfdbDao getOfdbDao() {
		return this.ofdbDao;
	}

	protected void saveForTest(final IEntity entity) {

		if (!entity.isInDB()) {

			if (this.isAppInitialized()) {
				this.crudService.insert(entity);
			} else {
				this.getCrudDao().insert(entity);
			}
			this.entityStack.push(entity);

		} else {

			if (this.isAppInitialized()) {
				this.crudService.update(entity);
			} else {
				this.getCrudDao().update(entity);
			}
		}

	}

	// public void clearDatabase() throws Exception {
	// DataSource ds = this.dataSource; // (DataSource)
	// SpringApplicationContext.getBean( "mydataSource" );
	// Connection connection = null;
	// try {
	// connection = ds.getConnection();
	// try {
	// Statement stmt = connection.createStatement();
	// try {
	// stmt.execute( "TRUNCATE SCHEMA PUBLIC.PUBLIC RESTART IDENTITY AND COMMIT NO
	// CHECK" );
	// connection.commit();
	// } finally {
	// stmt.close();
	// }
	// } catch ( SQLException e ) {
	// connection.rollback();
	// throw new Exception( e );
	// }
	// } catch ( SQLException e ) {
	// throw new Exception( e );
	// } finally {
	// if ( connection != null ) {
	// connection.close();
	// }
	// }
	// }

}
