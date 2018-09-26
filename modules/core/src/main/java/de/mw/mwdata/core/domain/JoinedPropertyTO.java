package de.mw.mwdata.core.domain;

/**
 * For all ofdb generated data queries additional column result metadata by
 * joined tables can be joined to the query metadata by defining HQL specific
 * association entity name, property name and value of dataset in this TO
 * object.<br>
 * E.g.<br>
 * <code>SELECT tabDef, bereich.name FROM TabDef tabDef => JoinedPropertyTO = {bereich, name, [value]}</code>
 * 
 * FIXME: rename class to CoverField ???
 * 
 * @author WilbersM
 *
 */
public class JoinedPropertyTO {

	private final String entityName;
	private final String propName;
	private String value;

	/**
	 * Defines the index of the position in the query result array where to find an
	 * additional column based value. Starts with 1 for first additional value
	 */
	private int resultArrayIndex;

	public JoinedPropertyTO(final String entityName, final String propName, final int resultArrayIndex) {
		this.entityName = entityName;
		this.propName = propName;
		this.resultArrayIndex = resultArrayIndex;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getPropName() {
		return propName;
	}

	public int getResultArrayIndex() {
		return this.resultArrayIndex;
	}

}
