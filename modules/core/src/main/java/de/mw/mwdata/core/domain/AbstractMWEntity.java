package de.mw.mwdata.core.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.mw.mwdata.core.Constants;

/**
 * Abstract class for managing fx-wide domain-objects. An fx-specific object has to implement the db-fields
 *
 * <pre>
 * <b><blockquote>
 * angelegtAm, angelegtVon, feld01-06, importId and transaktionsId
 * </blockquote></b>
 * </pre>
 *
 * The Ids are managed with a db-specific sequence. The key of the sequence has to be implemented by the implementing
 * class.<br>
 * FIXME: There should be an abstract base class in ofdb-api for defining getId() and getName() and implementing <br>
 * hashCode() and equals() because ofdb-api says "our entities are defined by unique natural key"
 *
 * @author Markus Wilbers
 * @since MWData 0.1
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@MappedSuperclass
public abstract class AbstractMWEntity implements Serializable, IEntity {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;

	private static final Logger	LOGGER				= LoggerFactory.getLogger( AbstractMWEntity.class );

	// main fields ...

	/**
	 * FIXME: do not use java.util.Date because of problems in comparing with java.sql.Timestamp.
	 *
	 * @see http://stackoverflow.com/questions/6803823/java-util-dates-behavior
	 */
	@Column(name = Constants.SYS_COL_ANGELEGT_AM, updatable = false, nullable = false)
	private Date				angelegtAm;

	@Column(name = Constants.SYS_COL_ANGELEGT_VON, updatable = false, nullable = false)
	private String				angelegtVon;

	@Column(name = Constants.SYS_COL_OFDB, updatable = false, nullable = false)
	private String				ofdb;

	@Column(name = Constants.SYS_COL_SYSTEM, columnDefinition = "NUMBER(1) default -1", updatable = false, nullable = false)
	@Type(type = "fxboolean")
	private Boolean				system;

	public AbstractMWEntity() {
	}

	// abstract methods

	public abstract String getSequenceKey();

	public abstract Long getId();

	public abstract void setId( final Long id );

	public abstract String getName();

	public abstract void setName( final String name );

	@Override
	public String toString() {
		return getName();
	}

	// implementations ...

	public boolean isInDB() {
		// NOTE: SequenceGenerator is called before Interceptor
		return (this.getId() != null);
	}

	public final void setAngelegtAm( final Date angelegtAm ) {
		this.angelegtAm = angelegtAm;
	}

	public final Date getAngelegtAm() {
		return this.angelegtAm;
	}

	public final void setAngelegtVon( final String angelegtVon ) {
		this.angelegtVon = angelegtVon;
	}

	public final String getAngelegtVon() {
		return this.angelegtVon;
	}

	public void setOfdb( final String ofdb ) {
		this.ofdb = ofdb;
	}

	public String getOfdb() {
		return this.ofdb;
	}

	public void setSystem( final Boolean system ) {
		this.system = system;
	}

	public Boolean isSystem() {
		return this.system;
	}

	public Boolean getSystem() {
		return isSystem();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( null == obj ) {
			return false;
		}

		AbstractMWEntity otherEntity = (AbstractMWEntity) obj;

		if ( null == this.getName() && null != otherEntity.getName() ) {
			return false;
		}
		if ( this.getName().equalsIgnoreCase( otherEntity.getName() ) ) {
			return true;
		}

		return false;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.getName() == null) ? 0 : this.getName().hashCode());
		return result;
	}

}
