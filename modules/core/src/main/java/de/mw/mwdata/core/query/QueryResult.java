package de.mw.mwdata.core.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.to.OfdbField;

public class QueryResult {

	private List<OfdbField> queryMetaData = new ArrayList<>();
	private List<IEntity[]> rows;
	private long countWithoutPaging;

	public QueryResult(final List<OfdbField> queryMetaData, final List<IEntity[]> rows, final long countWithoutPaging) {
		this.rows = rows;
		this.queryMetaData = queryMetaData;
		this.countWithoutPaging = countWithoutPaging;
	}

	/**
	 * Constructor with count = rows.size
	 * 
	 * @param queryMetaData
	 * @param rows
	 */
	public QueryResult(final List<OfdbField> queryMetaData, final List<IEntity[]> rows) {
		this.rows = rows;
		this.queryMetaData = queryMetaData;
		this.countWithoutPaging = rows.size();
	}

	private QueryResult(final List<IEntity[]> rows) {
		this.rows = rows;
		this.countWithoutPaging = rows.size();
	}

	public static QueryResult createMetaLessQueryResult(final List<IEntity[]> rows) {
		return new QueryResult(rows);
	}

	public boolean isEmpty() {
		return CollectionUtils.isEmpty(this.rows);
	}

	public List<IEntity[]> getRows() {
		return this.rows;
	}

	public IEntity getEntityByRowIndex(final int rowIndex) {

		// NOTE: read Object[] works, but not read simple Object from array Object[] !
		Object[] o = this.getRows().get(rowIndex);
		return (IEntity) o[0];
	}

	public String getAliasByRowAndColumnIndex(final int rowIndex, final int colIndex) {
		Object[] o = this.getRows().get(rowIndex);
		return (String) o[colIndex];
	}

	public long size() {
		if (this.isEmpty()) {
			return 0;
		}

		return this.getRows().size();
	}

	public long columnSize() {
		if (this.isEmpty()) {
			return 0;
		}
		return this.getRows().get(0).length;
	}

	public long getCountWithoutPaging() {
		return this.countWithoutPaging;
	}

	public void setCountWithoutPaging(final long count) {
		this.countWithoutPaging = count;
	}

	public boolean hasMetaData() {
		return !CollectionUtils.isEmpty(this.queryMetaData);
	}

	public List<OfdbField> getMetaData() {
		return this.queryMetaData;
	}

}
