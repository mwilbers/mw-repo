package de.mw.mwdata.ui;

import org.springframework.util.CollectionUtils;

import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.to.OfdbField;

/**
 * Class that builds full qualified ui access path to properties of
 * {@link EntityTO}
 * 
 * @author mwilbers
 *
 */
public class EntityTOPathBuilder {

	private final String entityPathName = "item";

	public EntityTOPathBuilder() {
	}

	public String getPropertyPath(final OfdbField ofProp) {
		StringBuilder b = new StringBuilder();

		if (!CollectionUtils.isEmpty(ofProp.getListOfValues())
				&& (ofProp.getDbtype().equals(DBTYPE.STRING) || ofProp.getDbtype().equals(DBTYPE.LONGINTEGER))) {
			b.append("map['");
			b.append(ofProp.getItemKey());
			b.append("'].value");
		} else {
			b.append(this.entityPathName);
			b.append(".");
			b.append(ofProp.getPropName());
		}

		return b.toString();
	}

}
