package de.mw.mwdata.core.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.daos.ICrudDao;
import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.intercept.CrudChain;
import de.mw.mwdata.core.intercept.ICrudInterceptable;
import de.mw.mwdata.core.intercept.InvalidChainCheckException;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.core.utils.Utils;

public class CrudService<T> implements ICrudService<T>, ICrudInterceptable {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrudService.class);

	@Autowired
	private ICrudDao<T> crudDao;

	private CrudChain crudChain;

	protected ICrudDao<T> getCrudDao() {
		return this.crudDao;
	}

	public void setCrudInterceptors(final List<CrudChain> argCrudChainItems) {

		int size = argCrudChainItems.size();
		if (size > 0) {
			this.crudChain = argCrudChainItems.get(0);

			CrudChain lastItem = this.crudChain;
			for (int i = 0; i < size - 1; i++) {
				lastItem.setNextChainItem(argCrudChainItems.get(i + 1));
				lastItem = argCrudChainItems.get(i + 1);
			}
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T insert(final T entity) {

		// TODO: all check-methods have to be called in insert-context. implement !

		try {
			// FIXME: do equalize arguments IFxPersistable and AbstractMWEntity here and in
			// update method
			doActionsBeforeCheck((AbstractMWEntity) entity, CRUD.INSERT);
			doCheck((AbstractMWEntity) entity, CRUD.INSERT);

			// doActionsBeforeDml( entity );
			// }

		} catch (InvalidChainCheckException e) {
			LOGGER.error("Invalid ofdb check" + e.getLocalizedMessage());
			throw e;
		}

		this.getCrudDao().insert(entity);

		// doActionsAfterDml( entity );

		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T update(final T entity) {

		// TODO: all check-methods have to be called in update-context. implement !

		try {
			doActionsBeforeCheck((AbstractMWEntity) entity, CRUD.UPDATE);
			doCheck((AbstractMWEntity) entity, CRUD.UPDATE);

			// doActionsBeforeDml( entity );
			// }

		} catch (InvalidChainCheckException e) {
			LOGGER.error("Invalid ofdb check" + e.getLocalizedMessage(), e);
			throw e;
		}

		this.getCrudDao().update(entity);

		// doActionsAfterDml( entity );

		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final T entity) {

		// TODO: all check-methods have to be called in delete-context. implement !

		// doActionsBeforeCheck( entity );
		// this.crudInterceptorChain.doChainActionsBeforeCheck( (AbstractMWEntity)
		// entity );

		// doCheck( entity, CRUD.DELETE );
		//
		// doActionsBeforeDml( entity );

		this.getCrudDao().delete(entity);

		// doActionsAfterDml( entity );

	}

	@Override
	public T findById(final Class<T> clazz, final Long id) {

		return this.crudDao.findById(clazz, id);
	}

	@Override
	public List<T> findAll(final Class<T> clazz, final Map<String, String>... sortColumns) {
		return this.crudDao.findAll(clazz, sortColumns);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T findByName(final Class<T> clazz, final String name) {
		return this.crudDao.findByName(clazz, name);
	}

	@Override
	public void registerCrudInterceptor(final CrudChain crudChainItem, final int insertAtPosition) {
		// this.additionalCrudChainItems.add(crudChainItem);

		// FIXME: write unit-test for testing correct ordering of chain item variants
		// ...
		// 1. write unit-test for crudChain
		// 2. continue test SequenceTest.java and check evaluation of crudChain.
		// Therefor continue DefaultCrudInterceptor in core with presetDefaultValues
		//
		CrudChain item = null;
		if (insertAtPosition <= -1) {
			item = getChainItemAtPosition(Integer.MAX_VALUE);
			if (null == item) {
				this.crudChain = crudChainItem;
				return;
			}
			item.setNextChainItem(crudChainItem);
			return;

		} else if (insertAtPosition == 0) {
			if (null == this.crudChain) {
				this.crudChain = crudChainItem;
			} else {
				CrudChain oldItem = this.crudChain;
				this.crudChain = crudChainItem;
				this.crudChain.setNextChainItem(oldItem);
			}
			return;

		} else {
			item = getChainItemAtPosition(insertAtPosition - 1);
			CrudChain nextItem = item.getNextChainItem();
			item.setNextChainItem(crudChainItem);
			crudChainItem.setNextChainItem(nextItem);
		}

	}

	/**
	 * 
	 * @param argPos
	 *            0-based. can be null. Than last item will be returned.
	 * @return the chain item at given position. Can be null.
	 */
	private CrudChain getChainItemAtPosition(final Integer argPos) {
		if (null == this.crudChain) {
			return null;
		}
		if (argPos.intValue() == 0) {
			return this.crudChain;
		}

		CrudChain nextItem = this.crudChain;
		CrudChain lastItem = null;
		int pos = 0;
		while (null != nextItem) {
			lastItem = nextItem;
			if (argPos.intValue() == pos) {
				break;
			}

			nextItem = nextItem.getNextChainItem();
			if (null == nextItem && argPos.intValue() >= pos) { // if next empty but called position still not reached
				break;
			}
			pos++;

		}

		return lastItem;
	}

	@Override
	public void doActionsBeforeCheck(final AbstractMWEntity entity, final CRUD crud) {
		if (null != this.crudChain) {
			this.crudChain.doChainActionsBeforeCheck(entity, crud);
		}
	}

	@Override
	public void doCheck(final AbstractMWEntity entity, final CRUD crud) throws InvalidChainCheckException {
		if (null != this.crudChain) {
			this.crudChain.doChainCheck(entity, crud);
		}
	}

	@Override
	public QueryResult executeSql(String sql) {
		List<IEntity[]> entityList = this.crudDao.executeSql(sql);
		return QueryResult.createMetaLessQueryResult(entityList);
	}

	@Override
	public QueryResult executeSql(String sql, final List<OfdbField> list) {
		List<IEntity[]> entityList = this.crudDao.executeSql(sql);

		// FIXME: why conversion to objects ?
		List<IEntity[]> objectArray = Utils.toObjectArray(entityList);
		return new QueryResult(list, objectArray);
	}

	@Override
	public QueryResult executeSqlPaginated(String sql, final List<OfdbField> list, PagingModel pagingModel) {
		List<IEntity[]> entityList = this.crudDao.executeSqlPaginated(sql, pagingModel);
		List<IEntity[]> objectArray = Utils.toObjectArray(entityList);
		return new QueryResult(list, objectArray);
	}

	@Override
	public long executeCountSql(String sqlCount) {
		return this.crudDao.executeCountSql(sqlCount);
	}

}
