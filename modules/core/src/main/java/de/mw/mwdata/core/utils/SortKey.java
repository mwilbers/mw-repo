package de.mw.mwdata.core.utils;

public class SortKey {

	public enum SORTDIRECTION {
		ASC("asc"), //
		DESC("desc");

		private String name;

		private SORTDIRECTION(final String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	private String			sortColumn;
	private SORTDIRECTION	sortDirection;

	public SortKey(final String col, final String ascOrDesc) {
		this.sortDirection = SORTDIRECTION.valueOf( ascOrDesc );
		this.sortColumn = col;
	}

	public String getSortColumn() {
		return this.sortColumn;
	}

	public SORTDIRECTION getSortDirection() {
		return this.sortDirection;
	}

}
