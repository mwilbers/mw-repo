package de.mw.mwdata.core.intercept;

import java.util.List;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.domain.AbstractMWEntity;

/**
 * Special interface for registering crud interceptors and sending crud events.
 * <br>
 * FIXME: interface still needed ?
 *
 * @author mwilbers
 *
 */
public interface ICrudInterceptable {

	/**
	 * Used for set directly crud chain interceptor items. <br>
	 * The order of given items is important ! The items will be evaluated in given
	 * order.
	 * 
	 * @param crudChainItems
	 */
	public void setCrudInterceptors(final List<CrudChain> crudChainItems);

	/**
	 * Used for set indirectly crud chain interceptor items, e.g. from consumer
	 * applications
	 * 
	 * @param crudInterceptor
	 * @param insertAtPosition
	 *            specified position (0-based) in crud chain pattern where to place
	 *            this interceptor. if -1 than will put at and of chain
	 */
	public void registerCrudInterceptor(final CrudChain crudInterceptor, final int insertAtPosition);

	public void doActionsBeforeCheck(final AbstractMWEntity entity, final CRUD crud);

	/**
	 *
	 * @param entity
	 * @param crud
	 * @throws InvalidChainCheckException
	 *             if any ofdb relevant check was failed
	 */
	public void doCheck(final AbstractMWEntity entity, final CRUD crud) throws InvalidChainCheckException;

}
