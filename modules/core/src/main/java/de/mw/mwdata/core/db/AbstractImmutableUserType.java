package de.mw.mwdata.core.db;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Base-class for immutable hibernate {@link UserType}s.
 *
 * @author Tobias Bosch (OPITZ CONSULTING GmbH)
 */
public abstract class AbstractImmutableUserType implements UserType, Serializable {
 
    private static final long serialVersionUID = 1077491373209480525L;
 
    public boolean equals(final Object o1, final Object o2) throws HibernateException {
        if (o1 == o2) {
            return true;
        }
        if ((o1 == null) || (o2 == null)) {
            return false;
        }
         return o1.equals(o2);
    }
 
    public boolean isMutable() {
        return false;
    }
 
    public Object deepCopy(final Object o) throws HibernateException {
         return o;
    }
 
    public Object assemble(final Serializable o, final Object owner)
           throws HibernateException {
        return o;
    }
 
    public Serializable disassemble(final Object o) throws HibernateException {
        return (Serializable) o;
    }
 
    public Object replace(final Object original, final Object target, final Object owner)
            throws HibernateException {
        return original;
    }
 
    public int hashCode(final Object o) throws HibernateException {
        return o.hashCode();
    }
}
