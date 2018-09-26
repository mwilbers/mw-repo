package de.mw.mwdata.ofdb.query.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.query.MetaDataGenerator;

public abstract class AbstractMetaDataGenerator implements MetaDataGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMetaDataGenerator.class);

	protected String calculateMaxLength(final ITabSpeig tabSpeig) {

		if (null == tabSpeig.getMaximum()) {

			int result = 0;
			switch (tabSpeig.getDbDatentyp()) {
			case BOOLEAN:
				result = 1;
			case DATE:
				result = 10;
			case DATETIME:
				result = 10;
			case LONGINTEGER:
				result = 9;
			case STRING:
				result = 255;
			default: {
				LOGGER.error("No maxlength defined for DBType: " + tabSpeig.getDbDatentyp().getDescription()
						+ ", tabSpeig: " + tabSpeig.getSpalte());
			}
			}

			return String.valueOf(result);
		} else {
			return tabSpeig.getMaximum();
		}

	}

	protected String calculateMinLength(final ITabSpeig tabSpeig) {

		if (null == tabSpeig.getMinimum()) {

			int result = 0;
			switch (tabSpeig.getDbDatentyp()) {
			case BOOLEAN:
				result = 1;
			case DATE:
				result = 10;
			case DATETIME:
				result = 10;
			case LONGINTEGER:
				result = 1;
			case STRING:
				result = 1;
			default: {
				LOGGER.error("No minlength defined for DBType: " + tabSpeig.getDbDatentyp().getDescription()
						+ ", tabSpeig: " + tabSpeig.getSpalte());
			}
			}

			return String.valueOf(result);
		} else {
			return tabSpeig.getMinimum();
		}

	}

}
