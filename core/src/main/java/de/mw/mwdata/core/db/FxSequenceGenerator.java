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
// @Component
public class FxSequenceGenerator implements IdentifierGenerator {

	/*
	 * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.SessionImplementor, java.lang.Object)
	 */
	// @Override
	@Override
	public Serializable generate( final SessionImplementor session, final Object object ) throws HibernateException {

		if ( !(object instanceof IEntity) ) {
			throw new HibernateException( "Wrong Sequence configuration." );
		}

		IEntity entity = (IEntity) object;

		Session sess = session.getFactory().getCurrentSession();
		Long nextId = null;
		Sequence sequence = null;

		if ( entity.isInDB() ) {
			return entity.getId(); // return null;
		}

		if ( object instanceof Sequence ) {

			// sequence = this.loadSequenceByKey( entity.getName(), sess );
			// if ( null != sequence ) {
			// throw new DuplicateKeyException( "Invalid sequencekey for insert. Sequence-key already persisted: "
			// + entity.getSequenceKey() );
			// }

			String sql = " select max(s.id) from Sequence s ";
			Query query = sess.createQuery( sql );
			// query.setString( "key", key );
			List<?> list = query.list();

			if ( null == list.get( 0 ) ) {
				// if empty table
				return 1L;
			}

			// return next free id (incremented by 1)
			return (Long) list.get( 0 ) + 1;

		} else {
			// if not of type sequence
			sequence = this.loadSequenceByKey( entity.getSequenceKey(), sess );

			// sequence = this.loadSequenceByKey( entity.getSequenceKey(), sess );
			// sequence = this.loadSequenceByKey( entity.getSequenceKey(), sess );
			if ( null == sequence ) {

				throw new IllegalStateException( "No sequence defined for entity " + entity.getClass().getName()
						+ " and sequence-key " + entity.getSequenceKey() );

			}

			// Sequence sequence = (Sequence) list.get( 0 );
			nextId = sequence.getNaechsteNr();

			sequence.increment();
			sess.update( sequence );

			return nextId;

		}

		// key = entity.getSequenceKey();

		// } else if(object instanceof AbstractMWOFDBEntity) {
		// AbstractMWOFDBEntity ofdbEntity = (AbstractMWOFDBEntity) object;
		//
		// if(ofdbEntity.isInDB()) return null;
		// key = ofdbEntity.getSequenceKey();

	}

	private Sequence loadSequenceByKey( final String key, final Session sess ) {

		String sql = " select s from Sequence s where s.name = :key ";
		Query query = sess.createQuery( sql );
		query.setString( "key", key );
		List list = query.list();

		Long nextId = null;
		if ( list.isEmpty() ) {

			return null;

		}

		Sequence sequence = (Sequence) list.get( 0 );
		return sequence;
	}

}
