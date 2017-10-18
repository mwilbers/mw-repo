package de.mw.mwdata.core.ofdb.daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import de.mw.mwdata.core.ofdb.query.DefaultOfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.InvalidQueryConfigurationException;
import de.mw.mwdata.core.ofdb.query.OfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.ValueType;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.ordb.query.OperatorEnum;

public class QueryBuilderTest {

	private static final Logger	LOGGER	= LoggerFactory.getLogger( QueryBuilderTest.class );

	@Test
	public void testEmptyFailedQueryBuilder() {

		try {
			OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
			builder.buildSQL();
			Assert.fail();
		} catch ( InvalidQueryConfigurationException e ) {
			// OK
			LOGGER.debug( "Test ok: exception of missing select-table thrown." );
		}
	}

	@Test
	public void testSimpleQuery() {

		OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
		builder.selectTable( TestConstants.TABLENAME_TABDEF, "TA" );
		builder.fromTable( TestConstants.TABLENAME_TABDEF, "TA" );
		String sql = builder.buildSQL();
		String expectedSql = "select TA from FX_TabDef_K as TA where 1=1 ";
		assertEqualsSqlIgnoringWhitespaces( sql, expectedSql );

	}

	private void assertEqualsSqlIgnoringWhitespaces( final String actualSql, final String expectedSql ) {
		Assert.assertEquals( actualSql.replaceAll( "\\s", "" ), expectedSql.replaceAll( "\\s", "" ) );
	}

	@Test
	public void testQueryWithJoin() {

		OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
		builder.selectTable( "FromTable", "FT" );
		builder.selectAlias( "FT", "ColAlias" );
		builder.fromTable( "FromTable", "FT" );

		// add joined table
		builder.joinTable( "JoinTable", "JT" );
		builder.whereJoin( "FT", "FTCol", "JT", "JTCol" );

		builder.andWhereRestriction( "FT", "ColAlias", OperatorEnum.Eq, "x", ValueType.NUMBER );

		builder.orderBy( "FromTable", "FTCol", "asc" );

		String sql = builder.buildSQL();
		String expectedSql = "select FT, FT.ColAlias from FromTable as FT, JoinTable as JT where 1=1 and FT.FTCol = JT.JTCol and FT.ColAlias = x order by FromTable.FTCol asc ";
		assertEqualsSqlIgnoringWhitespaces( sql, expectedSql );

	}

}
