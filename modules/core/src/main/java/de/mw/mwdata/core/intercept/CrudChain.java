package de.mw.mwdata.core.intercept;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.domain.AbstractMWEntity;

public interface CrudChain {

	public void setNextChainItem(final CrudChain item);

	public CrudChain getNextChainItem();

	public void doChainActionsBeforeCheck(final AbstractMWEntity entity, final CRUD crud);

	public void doChainCheck(final AbstractMWEntity entity, final CRUD crud) throws InvalidChainCheckException;

}
