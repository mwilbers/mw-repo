/**
 *
 */
package de.mw.mwdata.core.db;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.Sequence;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Nov, 2010
 *
 */
public class FxSequenceGenerator implements IdentifierGenerator {

	/*
	 * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.
	 * SessionImplementor, java.lang.Object)
	 */
	@Override
	public Serializable generate(final SessionImplementor session, final Object object) throws HibernateException {

		if (!(object instanceof IEntity)) {
			throw new HibernateException("Wrong Sequence configuration.");
		}

		IEntity entity = (IEntity) object;

		Session sess = session.getFactory().getCurrentSession();
		Long nextId = null;
		Sequence sequence = null;

		if (entity.isInDB()) {
			return entity.getId();
		}

		if (object instanceof Sequence) {

			String sql = " select max(s.id) from Sequence s ";
			Query query = sess.createQuery(sql);
			List<?> list = query.list();

			if (null == list.get(0)) {
				// if empty table
				return 1L;
			}

			// return next free id (incremented by 1)
			return (Long) list.get(0) + 1;

		} else {
			// if not of type sequence
			sequence = this.loadSequenceByKey(entity.getSequenceKey(), sess);
			if (null == sequence) {
				throw new IllegalStateException("No sequence defined for entity " + entity.getClass().getName()
						+ " and sequence-key " + entity.getSequenceKey());
			}

			nextId = sequence.getNaechsteNr();
			sequence.increment();
			sess.update(sequence);

			return nextId;

		}

	}

	private Sequence loadSequenceByKey(final String key, final Session sess) {

		String sql = " select s from Sequence s where s.name = :key ";
		Query query = sess.createQuery(sql);
		query.setString("key", key);

		@SuppressWarnings("rawtypes")
		List list = query.list();

		if (list.isEmpty()) {
			return null;
		}

		Sequence sequence = (Sequence) list.get(0);
		return sequence;
	}

}
