package de.mw.mwdata.ui;

/**
 * Represents the html specific type attribute of common form input fields
 * @author mwilbers
 *
 */
public enum Type {

	INPUT("text"), //
	RADIO("radio"), //
	CHECK("checkbox");

	private String	description;

	private Type(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

}
