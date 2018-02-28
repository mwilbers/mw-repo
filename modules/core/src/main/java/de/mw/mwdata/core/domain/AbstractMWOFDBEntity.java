/**
 *
 */
package de.mw.mwdata.core.domain;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * Abstract Entity-Base-class for OFDB-Definition-Tables
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Oct, 2010
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@MappedSuperclass
@Deprecated
public abstract class AbstractMWOFDBEntity extends AbstractMWEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -4279273124550594310L;

	// private static final Logger LOGGER = Logger.getLogger(
	// AbstractMWOFDBEntity.class );

	// main fields ...

	// @Column(name = Config.SYS_COL_ANGELEGT_AM, updatable = false, nullable =
	// false)
	// private Date angelegtAm;
	//
	// @Column(name = Config.SYS_COL_ANGELEGT_VON, updatable = false, nullable =
	// false)
	// private String angelegtVon;

	// @Column(name = Constants.SYS_COL_OFDB, updatable = false, nullable = false)
	// private String ofdb;
	//
	// @Column(name = Constants.SYS_COL_SYSTEM, columnDefinition = "NUMBER(1)
	// default -1", updatable = false, nullable =
	// false)
	// @Type(type = "fxboolean")
	// private Boolean system;

	// @Column(name = Config.SYS_COL_TRANSAKTIONS_ID, updatable = true, nullable =
	// false)
	// private Long transaktionsId;

	public AbstractMWOFDBEntity() {
	}

	// abstract methods

	// @Override
	// public abstract String getSequenceKey();
	//
	// @Override
	// public abstract Long getId();
	//
	// @Override
	// public abstract void setId( final Long id );
	//
	// @Override
	// public abstract String getName();
	//
	// @Override
	// public abstract void setName( final String name );

	// implementations ...

	// @Override
	// public boolean isInDB() {
	// // here use angelegtAm instead of getId because Id is already set in
	// // SequenceGenerator
	// // before FxInterceptor
	// return (this.getAngelegtAm() != null);
	// }

	// /**
	// * @param ofdb
	// * the ofdb to set
	// */
	// public void setOfdb( final String ofdb ) {
	// this.ofdb = ofdb;
	// }
	//
	// /**
	// * @return the ofdb
	// */
	// public String getOfdb() {
	// return this.ofdb;
	// }
	//
	// /**
	// * @param system
	// * the system to set
	// */
	// public void setSystem( final Boolean system ) {
	// this.system = system;
	// }
	//
	// /**
	// * @return the system
	// */
	// public Boolean isSystem() {
	// return this.system;
	// }
	//
	// public Boolean getSystem() {
	// return isSystem();
	// }

	// public void setTransaktionsId( Long transaktionsId ) {
	// this.transaktionsId = transaktionsId;
	// }
	//
	// public Long getTransaktionsId() {
	// return transaktionsId;
	// }

}
