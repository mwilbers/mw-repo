/**
 *
 */
package de.mw.mwdata.core.domain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import de.mw.mwdata.core.db.AbstractImmutableUserType;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Apr, 2011
 *
 */
public class FxEnumType<E extends Enum<E>> extends AbstractImmutableUserType implements IFxEnumType<E> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3252141179680973508L;
	private Class<E> clazz = null;

	protected FxEnumType(final Class<E> c) {
		this.clazz = c;
	}

	private static final int[] SQL_TYPES = { Types.VARCHAR };

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	public Class returnedClass() {
		return this.clazz;
	}

	@Override
	public Object nullSafeGet(final ResultSet resultSet, final String[] names, final SessionImplementor imp,
			final Object owner) throws HibernateException, SQLException {
		String name = resultSet.getString(names[0]);
		E result = null;
		if (resultSet.wasNull()) {
			result = null; // Enum.valueOf( this.clazz, "NULL" ); // TODO: case still needed ?
		} else {
			result = Enum.valueOf(this.clazz, name);
		}
		return result;
	}

	@Override
	public void nullSafeSet(final PreparedStatement preparedStatement, final Object value, final int index,
			final SessionImplementor imp) throws HibernateException, SQLException {
		IFxEnum en = (IFxEnum) value;

		if (null == value || en.isEmpty()) {
			preparedStatement.setNull(index, Types.VARCHAR);
		} else {
			preparedStatement.setString(index, ((Enum) value).name());
		}
	}

}
