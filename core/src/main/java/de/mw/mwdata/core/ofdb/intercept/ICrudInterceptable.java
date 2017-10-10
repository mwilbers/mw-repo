package de.mw.mwdata.core.ofdb.intercept;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.ofdb.def.CRUD;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidCheckException;

/**
 * Special interface for registering crud interceptors and sending crud events. <br>
 * FIXME: interface still needed ?
 *
 * @author mwilbers
 *
 */
public interface ICrudInterceptable {

	public void registerCrudInterceptor( final CrudChain crudInterceptor );

	public void doActionsBeforeCheck( final AbstractMWEntity entity, final CRUD crud );

	/**
	 *
	 * @param entity
	 * @param crud
	 * @throws OfdbInvalidCheckException
	 *             if any ofdb relevant check was failed
	 */
	public void doCheck( final AbstractMWEntity entity, final CRUD crud ) throws OfdbInvalidCheckException;

}
