package de.mw.mwdata.core.intercept;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.domain.AbstractMWEntity;

public abstract class AbstractCrudChain implements CrudChain {

	protected CrudChain nextChainItem;

	@Override
	public void setNextChainItem(final CrudChain item) {
		this.nextChainItem = item;
	}

	@Override
	public CrudChain getNextChainItem() {
		return this.nextChainItem;
	}

	public abstract void doChainActionsBeforeCheck(final AbstractMWEntity entity, final CRUD crud);

	public abstract void doChainCheck(final AbstractMWEntity entity, final CRUD crud) throws InvalidChainCheckException;

}
