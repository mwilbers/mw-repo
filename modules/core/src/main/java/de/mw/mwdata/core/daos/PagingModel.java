package de.mw.mwdata.core.daos;

public class PagingModel {

	private int pageSize;
	private int pageIndex;
	private long count;

	public PagingModel(final int pageSize, final int pageIndex) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}

	public static PagingModel createDefaultModel() {
		return new PagingModel(100, 1);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCount() {
		return this.count;
	}

	public int getStartIndex() {
		return (getPageIndex() - 1) * getPageSize();
	}

}
