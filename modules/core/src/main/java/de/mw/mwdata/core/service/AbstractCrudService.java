package de.mw.mwdata.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.daos.ICrudDao;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidCheckException;
import de.mw.mwdata.core.ofdb.exception.OfdbRuntimeException;
import de.mw.mwdata.core.ofdb.intercept.CrudChain;
import de.mw.mwdata.core.ofdb.intercept.ICrudInterceptable;

// FIXME: rename class to CrudService
public class AbstractCrudService<T> implements ICrudService<T>, ICrudInterceptable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCrudService.class);

	// ...
	// crudchain verschieben
	// neue id-felder und foreign keys testen
	// spring-xmls anpassen
	// crudtest

	@Autowired
	private ICrudDao<T> crudDao;

	private CrudChain crudChain;

	// private ApplicationFactory applicationFactory;

	/**
	 * ordered list of interceptors. Have to be registered first
	 */
	private List<CrudChain> crudChainItems = new ArrayList<CrudChain>();

	/**
	 * unordered list of additional interceptors. Can be used for registering
	 * further interceptors from consumer applications
	 */
	private List<CrudChain> additionalCrudChainItems = new ArrayList<CrudChain>();

	protected ICrudDao<T> getCrudDao() {
		return this.crudDao;
	}

	public void init() {
		buildChain();
	}

	public void setCrudInterceptors(final List<CrudChain> crudChainItems) {
		this.crudChainItems = crudChainItems;
	}

	// public void setApplicationFactory( final ApplicationFactory
	// applicationFactory ) {
	// this.applicationFactory = applicationFactory;
	// }

	public void buildChain() {

		if (CollectionUtils.isEmpty(this.crudChainItems)) {
			return;
		}

		int size = this.crudChainItems.size();
		this.crudChain = this.crudChainItems.get(0);
		for (int i = 0; i < size - 1; i++) {
			this.crudChainItems.get(i).setNextChainItem(this.crudChainItems.get(i + 1));
		}

		size = this.additionalCrudChainItems.size();
		for (int i = 0; i < size - 1; i++) {
			// we copy additional interceptors to main interceptor list
			this.crudChainItems.get(i).setNextChainItem(this.additionalCrudChainItems.get(i + 1));
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T insert(final T entity) {

		// FIXME: all check-methods have to be called in insert-context. implement !

		try {
			// FIXME: if needed: if ( this.applicationFactory.getState().equals(
			// ApplicationState.RUNNING ) ) {
			// FIXME: do equalize arguments IFxPersistable and AbstractMWEntity here and in
			// update method
			doActionsBeforeCheck((AbstractMWEntity) entity, CRUD.INSERT);
			doCheck((AbstractMWEntity) entity, CRUD.INSERT);

			// doActionsBeforeDml( entity );
			// }

		} catch (OfdbInvalidCheckException e) {
			LOGGER.error("Invalid ofdb check" + e.getLocalizedMessage());
			throw new OfdbRuntimeException("Invalid ofdb check", e);
		}

		this.getCrudDao().insert(entity);

		// doActionsAfterDml( entity );

		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T update(final T entity) {

		// FIXME: all check-methods have to be called in update-context. implement !

		try {

			// FIXME: if needed:
			// if ( this.applicationFactory.getState().equals( ApplicationState.RUNNING ) )
			// {

			doActionsBeforeCheck((AbstractMWEntity) entity, CRUD.UPDATE);
			doCheck((AbstractMWEntity) entity, CRUD.UPDATE);

			// doActionsBeforeDml( entity );
			// }

		} catch (OfdbInvalidCheckException e) {
			LOGGER.error("Invalid ofdb check" + e.getLocalizedMessage(), e);

			throw new OfdbRuntimeException("Invalid ofdb check", e);
		}

		this.getCrudDao().update(entity);

		// doActionsAfterDml( entity );

		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final T entity) {

		// FIXME: all check-methods have to be called in delete-context. implement !

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
	public T findByName(final Class<T> clazz, final String name) {
		return this.findByName(clazz, name);
	}

	@Override
	public void registerCrudInterceptor(final CrudChain crudChainItem) {
		this.additionalCrudChainItems.add(crudChainItem);

	}

	@Override
	public void doActionsBeforeCheck(final AbstractMWEntity entity, final CRUD crud) {
		this.crudChain.doChainActionsBeforeCheck(entity, crud);
	}

	@Override
	public void doCheck(final AbstractMWEntity entity, final CRUD crud) throws OfdbInvalidCheckException {
		this.crudChain.doChainCheck(entity, crud);
	}

}
