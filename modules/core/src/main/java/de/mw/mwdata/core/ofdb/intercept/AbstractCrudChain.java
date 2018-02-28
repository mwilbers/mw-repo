package de.mw.mwdata.core.ofdb.intercept;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidCheckException;

public abstract class AbstractCrudChain implements CrudChain {

	protected CrudChain nextChainItem;

	public void setNextChainItem(final CrudChain item) {
		this.nextChainItem = item;
	}

	public abstract void doChainActionsBeforeCheck(final AbstractMWEntity entity, final CRUD crud);

	public abstract void doChainCheck(final AbstractMWEntity entity, final CRUD crud) throws OfdbInvalidCheckException;

}
