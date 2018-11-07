package de.mw.mwdata.ofdb.query;

import java.util.List;

import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.query.QueryValue;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

public interface IOfdbQueryModelService {

	/**
	 * Creates sql from given {@link OfdbQueryModel }
	 * 
	 * @param queryModel
	 * @param viewHandle
	 * @param setCount
	 * @return
	 */
	public String buildSQL(final OfdbQueryModel queryModel, final ViewConfigHandle viewHandle, final boolean setCount);

	/**
	 * Converts a common java object with given ofdb informations in
	 * {@link ITabSpeig} to sql based query value
	 * 
	 * @param value
	 * @param whereTabSpeig
	 * @return
	 */
	public QueryValue convertValueToQueryValue(final Object value, final ITabSpeig whereTabSpeig);

	/**
	 * Executes the given {@link OfdbQueryModel} and returns a model based
	 * {@link QueryResult} with additional metadata informations
	 * 
	 * @param queryModel
	 * @param viewHandle
	 * @param sortKeys
	 * @param pagingModel
	 * @return
	 */
	public QueryResult executeQueryModel(final OfdbQueryModel queryModel, final ViewConfigHandle viewHandle,
			final List<SortKey> sortKeys, final PagingModel pagingModel);

	/**
	 * Executes the given {@link OfdbQueryModel} and returns a model based
	 * {@link QueryResult} with additional metadata informations. The result is
	 * filtered by the specific values preset in the given argument
	 * {@link AbstractMWEntity}
	 * 
	 * @param queryModel
	 * @param viewHandle
	 * @param sortKeys
	 * @param pagingModel
	 * @param entityTO
	 * @return
	 */
	public QueryResult executeFilteredQueryModel(final OfdbQueryModel queryModel, final ViewConfigHandle viewHandle,
			List<SortKey> sortKeys, final PagingModel pagingModel, final EntityTO<? extends AbstractMWEntity> entityTO);

}
