package de.mw.mwdata.core.intercept;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.domain.AbstractMWEntity;

/**
 * Chain pattern interface for handling consumer for crud based data
 * manipulation sequentially.
 * 
 * @author WilbersM
 *
 */
public interface CrudChain {

	/**
	 * Defines the next consumer for working on the current crud operation
	 * 
	 * @param item
	 */
	public void setNextChainItem(final CrudChain item);

	/**
	 * 
	 * @return the next consumer for the current crud operation
	 */
	public CrudChain getNextChainItem();

	/**
	 * Event based operation interceptor method for doing actions just before save
	 * event
	 * 
	 * @param entity
	 * @param crud
	 */
	public void doChainActionsBeforeCheck(final AbstractMWEntity entity, final CRUD crud);

	/**
	 * Event based operation interceptor method for doing some checks just before
	 * save event
	 * 
	 * @param entity
	 * @param crud
	 * @throws InvalidChainCheckException
	 */
	public void doChainCheck(final AbstractMWEntity entity, final CRUD crud) throws InvalidChainCheckException;

}
