package de.mw.mwdata.core.intercept;

import de.mw.mwdata.core.domain.AbstractMWEntity;

public interface ICrudInterceptor {

	public void presetDefaultValues(final AbstractMWEntity entity);

}
