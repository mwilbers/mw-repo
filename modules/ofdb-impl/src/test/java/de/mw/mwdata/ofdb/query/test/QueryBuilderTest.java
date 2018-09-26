package de.mw.mwdata.ofdb.query.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.query.InvalidQueryConfigurationException;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryBuilder;
import de.mw.mwdata.core.query.SimpleQueryBuilder;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.test.data.TestConstants;

public class QueryBuilderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryBuilderTest.class);

	@Test
	public void testEmptyFailedQueryBuilder() {

		try {
			QueryBuilder builder = new SimpleQueryBuilder();
			builder.buildSQL();
			Assert.fail();
		} catch (InvalidQueryConfigurationException e) {
			// OK
			LOGGER.debug("Test ok: exception of missing select-table thrown.");
		}
	}

	@Test
	public void testSimpleQuery() {

		QueryBuilder builder = new SimpleQueryBuilder();
		builder.selectEntity(TestConstants.TABLENAME_TABDEF, "TA");
		builder.fromEntity(TestConstants.TABLENAME_TABDEF, "TA");
		String sql = builder.buildSQL();
		String expectedSql = "select TA from FX_TabDef_K as TA where 1=1 ";
		assertEqualsSqlIgnoringWhitespaces(sql, expectedSql);

	}

	private void assertEqualsSqlIgnoringWhitespaces(final String actualSql, final String expectedSql) {
		Assert.assertEquals(actualSql.replaceAll("\\s", ""), expectedSql.replaceAll("\\s", ""));
	}

	@Test
	public void testQueryWithJoin() {

		QueryBuilder builder = new SimpleQueryBuilder();
		builder.selectEntity("FromTable", "FT");
		builder.selectAlias("FT", "ColAlias");
		builder.fromEntity("FromTable", "FT");

		// add joined table
		builder.joinTable("JoinTable", "JT");
		builder.whereJoin("FT", "FTCol", "JT", "JTCol");

		builder.andWhereRestriction("FT", "ColAlias", OperatorEnum.Eq, "x", ValueType.NUMBER);

		builder.orderBy("FromTable", "FTCol", "asc");

		String sql = builder.buildSQL();
		String expectedSql = "select FT, FT.ColAlias from FromTable as FT, JoinTable as JT where 1=1 and FT.FTCol = JT.JTCol and FT.ColAlias = x order by FromTable.FTCol asc ";
		assertEqualsSqlIgnoringWhitespaces(sql, expectedSql);

	}

}
