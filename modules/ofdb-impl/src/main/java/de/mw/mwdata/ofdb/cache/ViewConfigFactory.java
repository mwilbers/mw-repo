package de.mw.mwdata.ofdb.cache;

public interface ViewConfigFactory {

	/**
	 * 
	 * @param viewName
	 * @return
	 */
	public ViewConfigHandle createViewConfiguration(final String viewName);

	// /**
	// * Creates a Metadata object for the given table based column property
	// * {@link ITabSpeig} and the view based view column property
	// * {@link IAnsichtSpalte}. If {@link IAnsichtSpalte} is not given metadata is
	// * created only by table property informations
	// *
	// * @param viewHandle
	// * the view config handle that holds the view ofdb context
	// * information
	// * @param tabSpeig
	// * the table property object
	// * @param ansichtSpalte
	// * the view property object
	// * @param highestResultIndex
	// * highest index for additional result column positions. FIXME:
	// * should be removed by design
	// * @return
	// */
	// public OfdbField createColumnMetaData(final ViewConfigHandle viewHandle,
	// final ITabSpeig tabSpeig,
	// final IAnsichtSpalte ansichtSpalte, final int highestResultIndex);

}
