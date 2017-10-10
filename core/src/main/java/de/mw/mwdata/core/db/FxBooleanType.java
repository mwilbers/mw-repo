/**
 *
 */
package de.mw.mwdata.core.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * FX-specific class for handling java.lang.Boolean-values to oracle-number-values (true: -1, false: 0). The values
 * 'null' and '0' are converted to Boolean.FALSE, all other values are converted to Boolean.TRUE.
 *
 * @author Wilbers, Markus
 * @since Sep 2010
 * @version 1.0
 *
 */
public class FxBooleanType extends AbstractImmutableUserType {

	private static final long	serialVersionUID	= 1L;
	private static final int[]	SQL_TYPES			= { Types.NUMERIC };

	@Override
	public Object nullSafeGet( final ResultSet rs, final String[] names, final SessionImplementor imp,
			final Object owner ) throws HibernateException, SQLException {

		// String val = names[0];
		String val = rs.getString( names[0] );
		return defBoolean( val );
		// if(null == val || "0".equals(val)) {
		// return Boolean.FALSE;
		// } else {
		// return Boolean.TRUE;
		// }

	}

	public static boolean defBoolean( final String value ) {
		if ( null == value || "0".equals( value ) ) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	@Override
	public void nullSafeSet( final PreparedStatement st, final Object value, final int index,
			final SessionImplementor imp ) throws HibernateException, SQLException {

		if ( value == null ) {
			st.setInt( index, 0 );
		} else {
			if ( Boolean.FALSE.equals( value ) ) {
				st.setInt( index, 0 );
			} else {
				st.setInt( index, -1 );
			}
		}

	}

	@Override
	public Class returnedClass() {
		return Boolean.class;
	}

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

}
