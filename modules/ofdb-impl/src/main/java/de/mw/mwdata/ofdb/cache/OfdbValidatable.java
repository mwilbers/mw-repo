package de.mw.mwdata.ofdb.cache;

import java.util.List;

import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

public interface OfdbValidatable {

	// /**
	// *
	// */
	// public ValidationResult isAnsichtTabListValid( final AnsichtDef ansicht,
	// final List<AnsichtTab> ansichtTabList );

	/**
	 * Checks if all properties of the given AnsichtDef-object are valid for the
	 * OFDB-Configuration
	 *
	 * @param ansichtDef
	 * @param set
	 */
	public ViewConfigValidationResultSet isAnsichtValid(final IAnsichtDef ansichtDef);

	/**
	 * Checks if the given tableDef is valid for the OFDB-Configuration
	 *
	 * @param tabDef
	 * @param set
	 */
	public ViewConfigValidationResultSet isTableValid(final ITabDef tabDef);

	/**
	 * Checks if the given tabSpeig is valid for the OFDB-configuration
	 *
	 * @param tabSpeig
	 * @param set
	 */
	public ViewConfigValidationResultSet isTabSpeigValid(final ITabSpeig tabSpeig);

	/**
	 * Checks if all ansichtTab-objects of the given ansichtDef are valid for the
	 * OFDB-configuration
	 *
	 * @param ansichtDef
	 * @param ansichtTabList
	 * @param set
	 */
	public ViewConfigValidationResultSet isAnsichtTabListValid(final IAnsichtDef ansichtDef,
			final List<IAnsichtTab> ansichtTabList);

	/**
	 * Checks if the AnsichtSpalte is valid for the OFDB-configuration
	 *
	 * @param spalte
	 * @param viewHandle
	 */
	public ViewConfigValidationResultSet isAnsichtSpalteValid(final IAnsichtSpalte spalte,
			final ViewConfigHandle viewHandle);

	public ViewConfigValidationResultSet isViewOrderByValid(final IAnsichtOrderBy ansichtOrderBy,
			final ViewConfigHandle viewHandle);

	// public boolean isColumnUnique( final AbstractMWEntity entity, final String
	// columnName );

}
