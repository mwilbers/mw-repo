package de.mw.mwdata.core.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.mw.common.utils.FxDateUtils;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.daos.ICrudDao;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.Sequence;
import de.mw.mwdata.core.mocks.CoreMockFactory;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.test.data.TestConstants;

@ContextConfiguration(locations = { "classpath:/appContext-core.xml", "classpath:/appContext-common-test-db.xml" })
public class SequenceTest extends AbstractTransactionalTestNGSpringContextTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceTest.class);

	@Autowired
	@Qualifier(value = "crudDao")
	protected ICrudDao<Sequence> ofdbSequenceDao;

	private List<Sequence> sequenceCache = new ArrayList<Sequence>();
	private Stack<IEntity> entityStack = new Stack<IEntity>();

	@Autowired
	private ICrudDao crudDao;

	@Autowired
	private ICrudService<BenutzerBereich> crudService;

	private ICrudDao getCrudDao() {
		return this.crudDao;
	}

	@BeforeClass
	public void setUp() {

		LOGGER.info("####################################################");
		LOGGER.info("########setUp " + this.getClass().getName() + "#####");
		LOGGER.info("####################################################");

		Sequence sequence = CoreMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_SYSSEQUENZ, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);

		sequence = CoreMockFactory.createSequenceMock(TestConstants.SEQUENCEKEY_BENUTZERBEREICH, 1l, 0l);
		this.ofdbSequenceDao.insert(sequence);
		this.sequenceCache.add(sequence);

	}

	@Test
	public void testSequenceIncrementationAndInterceptor() {

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

		BenutzerBereich bereich = CoreMockFactory.createBenutzerBereichMock("Testbereich");

		// test state before insert
		Assert.assertNull(bereich.getAngelegtAm(), "AngelegtAm is not null.");
		Assert.assertNull(bereich.getAngelegtVon(), "AngelegtVon is not null.");

		// first get next expected TabDef.ID
		String sql = " SELECT LetzteBelegteNr, Inkrement  FROM " + TestConstants.TABLENAME_SYSSEQUENZ
				+ "  WHERE name = '" + bereich.getSequenceKey() + "' ";
		List<Map<String, Object>> items = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> map = items.get(0);
		Long letzteBelegteNr = (Long) (map.get("LetzteBelegteNr"));
		Long inkrement = (Long) (map.get("Inkrement"));
		Sequence sequence3 = new Sequence();
		sequence3.setLetzteBelegteNr(letzteBelegteNr.longValue());
		sequence3.setInkrement(inkrement.longValue());

		// do Test
		this.crudService.insert(bereich);

		// here we have to query the hibernate-session-factory for objects. This way the
		// factory does in implicit flush
		// to db before we use the simpleJdbcTemplate for querying new saved objects
		// (e.g. sequence-values)
		List<BenutzerBereich> allBereiche = this.getCrudDao().findAll(BenutzerBereich.class);
		Assert.assertTrue(allBereiche.size() == 1, "Wrong number of BenutzerBereich objects was saved.");

		// Test 1: check if inteceptor- and field-defaults were set
		Assert.assertNotNull(bereich.getAngelegtVon(), "AngelegtVon is null.");
		Assert.assertNotNull(bereich.getAngelegtAm(), "AngelegtAm is null.");
		Assert.assertNotNull(bereich.getId(), "Id not correctly set.");
		Assert.assertEquals(bereich.getAngelegtVon(), Constants.SYS_USER_DEFAULT);
		Assert.assertEquals(0, FxDateUtils.compare(new Date(), bereich.getAngelegtAm(), false),
				"AngelegtAm not correctly set.");

		// Test 2: check if next id was set for tabDef
		Assert.assertEquals(sequence3.getNaechsteNr(), bereich.getId(), "Wrong Sequence-ID was set");

		// Test 3: check incrementation of next number in TabDef-Sequence-Generator
		items = this.jdbcTemplate.queryForList(sql);
		map = items.get(0);
		Long neueLetzteBelegteNr = (Long) (map.get("LetzteBelegteNr"));
		Assert.assertNotSame(letzteBelegteNr.longValue(), neueLetzteBelegteNr.longValue(),
				"Sequence-number not incremented after insert-operation.");

		this.getCrudDao().delete(bereich);
		List<BenutzerBereich> bereiche = this.getCrudDao().findAll(BenutzerBereich.class);
		Assert.assertEquals(bereiche.size(), 0);

	}

	@AfterClass
	public void tearDownAfterClass() {
		for (Sequence sequence : this.sequenceCache) {
			this.ofdbSequenceDao.delete(sequence);
		}

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
			this.getCrudDao().delete(entity);
		}

	}

}
