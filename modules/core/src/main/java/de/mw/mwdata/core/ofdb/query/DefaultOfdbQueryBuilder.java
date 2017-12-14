package de.mw.mwdata.core.ofdb.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.LocalizedMessages;
import de.mw.mwdata.ordb.query.OperatorEnum;

public class DefaultOfdbQueryBuilder implements OfdbQueryBuilder {

	// private OfdbQueryModel queryModel;

	private boolean					count;

	private String					mainTable;
	private String					mainTableAlias;
	private Map<String, String>		joinTables				= new HashMap<String, String>();

	private Map<String, String>		joinEntities			= new HashMap<String, String>();

	private Map<String, String>		selectTableAliasItems	= new HashMap<String, String>();

	private Map<String, String>		selectColumnAliasItems	= new HashMap<String, String>();

	private List<JoinRestriction>	joinRestrictions		= new ArrayList<JoinRestriction>();

	private List<WhereRestriction>	whereRestrictions		= new ArrayList<WhereRestriction>();

	private List<OrderSet>			orderSet				= new ArrayList<OrderSet>();

	private class TableColumnPair {

		private String	tableName;
		private String	columnName;

		private TableColumnPair(final String tableName, final String columnName) {
			this.tableName = tableName;
			this.columnName = columnName;
		}

		private String getTableName() {
			return this.tableName;
		}

		private String getColumnName() {
			return this.columnName;
		}

	}

	private class OrderSet {

		private TableColumnPair	tableColumnPair;
		private String			orderDirection;

		private OrderSet(final TableColumnPair tableColumnPair, final String orderDirection) {
			this.tableColumnPair = tableColumnPair;
			this.orderDirection = orderDirection;
		}

	}

	private class WhereRestriction {

		private TableColumnPair	tableColumnPair;
		// private String operator;
		private OperatorEnum	operator;
		private Object			value;
		private ValueType		valueType;

		private WhereRestriction(final TableColumnPair tableColumnPair, final OperatorEnum operator, final Object value,
				final ValueType valueType) {
			this.tableColumnPair = tableColumnPair;
			this.operator = operator;
			this.value = value;
			this.valueType = valueType;
		}

		private boolean isString() {
			return ValueType.STRING.equals( this.valueType );
		}

	}

	private class JoinRestriction {

		private TableColumnPair	pair1;
		private TableColumnPair	pair2;

		private JoinRestriction(final TableColumnPair pair1, final TableColumnPair pair2) {
			this.pair1 = pair1;
			this.pair2 = pair2;
		}

		private TableColumnPair getWherePair1() {
			return this.pair1;
		}

		private TableColumnPair getWherePair2() {
			return this.pair2;
		}

	}

	// @Override
	@Override
	public OfdbQueryBuilder setCount( final boolean count ) {
		this.count = count;
		return this;
	}

	@Override
	public String buildSQL() throws InvalidQueryConfigurationException {

		StringBuffer sbFrom = new StringBuffer();
		StringBuilder sbSelect = new StringBuilder();
		StringBuilder sbWhere = new StringBuilder();
		StringBuilder sbOrder = new StringBuilder();
		sbWhere.append( " where 1=1 " );

		// build select-part
		sbSelect.append( "select " );

		if ( this.count ) {
			sbSelect.append( " count(*) " );
		} else {

			if ( CollectionUtils.isEmpty( this.selectTableAliasItems )
					&& CollectionUtils.isEmpty( this.selectColumnAliasItems ) ) {
				String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB,
						"InvalidQueryMissingTableAndAlias" );
				throw new InvalidQueryConfigurationException( msg );
			}

			for ( Map.Entry<String, String> entry : this.selectTableAliasItems.entrySet() ) {
				sbSelect.append( " " ).append( entry.getValue() ).append( "," );
			}
			for ( Map.Entry<String, String> entry : this.selectColumnAliasItems.entrySet() ) {
				sbSelect.append( " " ).append( entry.getKey() ).append( "." ).append( entry.getValue() ).append( "," );
			}

			// remove last ',' in select-list
			if ( sbSelect.lastIndexOf( "," ) + 1 == sbSelect.length() ) {
				sbSelect = new StringBuilder( sbSelect.substring( 0, sbSelect.length() - 1 ) );
			}

		}

		// build from-part

		if ( StringUtils.isBlank( this.mainTable ) && StringUtils.isBlank( this.mainTableAlias ) ) {
			String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "InvalidQueryMissingFromTable" );
			throw new InvalidQueryConfigurationException( msg );
		}

		sbFrom.append( " from " ).append( this.mainTable ).append( " as " ).append( this.mainTableAlias );

		for ( Map.Entry<String, String> joinEntry : this.joinEntities.entrySet() ) {

			// add joins
			sbFrom.append( " inner join " );
			sbFrom.append( this.mainTableAlias ).append( "." ).append( joinEntry.getKey() ).append( " as " )
					.append( joinEntry.getValue() );

		}

		for ( Map.Entry<String, String> joinEntry : this.joinTables.entrySet() ) {

			// add joins
			sbFrom.append( ", " );
			sbFrom.append( joinEntry.getKey() ).append( " as " ).append( joinEntry.getValue() );

		}

		// add join-restrictions
		for ( JoinRestriction where : this.joinRestrictions ) {

			sbWhere.append( " and " ).append( where.getWherePair1().getTableName() ).append( "." )
					.append( where.getWherePair1().getColumnName() );
			sbWhere.append( " = " );
			sbWhere.append( where.getWherePair2().getTableName() ).append( "." )
					.append( where.getWherePair2().getColumnName() );

		}

		// add where-restrictions
		for ( WhereRestriction whereRes : this.whereRestrictions ) {
			TableColumnPair pair = whereRes.tableColumnPair;
			sbWhere.append( " and " ).append( pair.tableName ).append( "." ).append( pair.columnName ).append( " " );

			switch ( whereRes.operator ) {
				case Eq:
				case NotEq: {
					if ( ValueType.BOOLEAN.equals( whereRes.valueType ) ) {
						Boolean bVal = (Boolean) whereRes.value;

						if ( whereRes.operator.equals( OperatorEnum.Eq ) ) {
							sbWhere.append( " = " );
						} else {
							sbWhere.append( " != " );
						}
						if ( bVal ) {
							// sbWhere.append( " is true " );
							sbWhere.append( "'" ).append( Integer.valueOf( Constants.SYS_VAL_TRUE ) ).append( "'" );
						} else {
							// sbWhere.append( " is not true " );
							sbWhere.append( "'" ).append( Integer.valueOf( Constants.SYS_VAL_FALSE ) ).append( "'" );
						}

					} else {

						if ( whereRes.operator.equals( OperatorEnum.Eq ) ) {
							sbWhere.append( " = " );
						} else {
							sbWhere.append( " != " );
						}
						if ( whereRes.isString() ) {
							sbWhere.append( "'" ).append( whereRes.value ).append( "'" );
						} else {
							sbWhere.append( whereRes.value );
						}

					}
					break;
				}
				case IsNotNull: {
					sbWhere.append( " is not null " );
					break;
				}
				default: {
					throw new IllegalStateException( "Missing operator enum for DefaultOfdbQueryBuilder." );
				}
			}
			// .append( whereRes.operator );

			// if ( whereRes.isString() ) {
			// sbWhere.append( "'" ).append( whereRes.value ).append( "'" );
			// } else {
			// sbWhere.append( whereRes.value );
			// }

		}

		// add order-items
		if ( !this.orderSet.isEmpty() ) {
			sbOrder.append( " order by " );
		}
		for ( OrderSet order : this.orderSet ) {
			sbOrder.append( order.tableColumnPair.tableName ).append( "." );
			sbOrder.append( order.tableColumnPair.columnName ).append( " " );
			sbOrder.append( order.orderDirection ).append( "," );
		}

		// remove last comma
		if ( !this.orderSet.isEmpty() ) {
			sbOrder = new StringBuilder( sbOrder.substring( 0, sbOrder.length() - 1 ) );
		}

		// concatenate all parts
		sbSelect.append( sbFrom ).append( sbWhere.toString() ).append( sbOrder.toString() );

		this.reset();
		return sbSelect.toString();

	}

	// @Override
	@Override
	public OfdbQueryBuilder selectTable( final String tableName, final String tableAlias ) {
		// this.mainTable = tableName;
		this.selectTableAliasItems.put( tableName, tableAlias );
		return this;
	}

	// @Override
	@Override
	public OfdbQueryBuilder joinTable( final String tableName, final String tableAlias ) {
		this.joinTables.put( tableName, tableAlias );
		return this;
	}

	// @Override
	@Override
	public OfdbQueryBuilder whereJoin( final String join1Table, final String join1Column, final String join2Table,
			final String join2Column ) {
		this.joinRestrictions.add( new JoinRestriction( new TableColumnPair( join1Table, join1Column ),
				new TableColumnPair( join2Table, join2Column ) ) );
		return this;
	}

	@Override
	public OfdbQueryBuilder fromTable( final String tableName, final String tableAlias ) {
		this.mainTable = tableName;
		this.mainTableAlias = tableAlias;
		return this;
	}

	// @Override
	@Override
	public OfdbQueryBuilder selectAlias( final String tableAlias, final String columnAlias ) {
		this.selectColumnAliasItems.put( tableAlias, columnAlias );
		return this;
	}

	@Override
	public OfdbQueryBuilder andWhereRestriction( final String tableAlias, final String columnAlias,
			final OperatorEnum operator, final Object value, final ValueType valueType ) {

		TableColumnPair pair = new TableColumnPair( tableAlias, columnAlias );
		WhereRestriction whereRes = new WhereRestriction( pair, operator, value, valueType );
		this.whereRestrictions.add( whereRes );

		return this;
	}

	// @Override
	@Override
	public OfdbQueryBuilder orderBy( final String tableAlias, final String columnAlias, final String orderDirection ) {

		this.orderSet.add( new OrderSet( new TableColumnPair( tableAlias, columnAlias ), orderDirection ) );
		return this;
	}

	@Override
	public void reset() {
		this.count = false;
		this.mainTable = null;
		this.mainTableAlias = null;
		this.joinRestrictions.clear();
		this.joinTables.clear();
		this.selectColumnAliasItems.clear();
		this.selectTableAliasItems.clear();
		this.orderSet.clear();
		this.whereRestrictions.clear();

	}

	@Override
	public OfdbQueryBuilder joinEntity( final String entityName, final String entityAlias ) {
		this.joinEntities.put( entityName, entityAlias );
		return this;
	}
}
