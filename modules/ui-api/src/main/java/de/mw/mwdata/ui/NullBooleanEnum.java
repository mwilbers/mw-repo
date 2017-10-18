package de.mw.mwdata.ui;

public enum NullBooleanEnum {

	TRUE("true"), //
	FALSE("false"), //
	NULL("null");
	
	private String description;
	
	private NullBooleanEnum(final String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
