/**
 *
 */
package de.mw.mwdata.core.ofdb.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import de.mw.mwdata.core.domain.AbstractMWEntity;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since July, 2010
 *
 *        FIXME: equals, hashCode implementieren
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class),
		@TypeDef(name = "fxDatentyp", typeClass = de.mw.mwdata.core.ofdb.domain.enums.TypeDATENTYP.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_TabSpEig_K" /* , schema = Constants.DB_SCHEMA */)
public class TabSpeig extends AbstractMWEntity implements ITabSpeig {

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.domain.AbstractMWOFDBEntity#getId()
	 */

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;

	private final static String	SEQUENCE_KEY		= "FX_TabSpEig_K:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long				id;

	// @Transient
	// @Column(name = "TABELLE", insertable = true, updatable = true, nullable = false, unique = true)
	// private String name;

	// @Column(name = "TABELLE", insertable = false, updatable = false, nullable = false, unique = true)
	// @Column(name = "TABELLE", insertable = true, updatable = true, nullable = false, unique = true)
	// private String tabelle;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TABDEFID", referencedColumnName = "DSID", nullable = false, updatable = false, insertable = false)
	private TabDef				tabDef;

	@Column(name = "TABDEFID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long				tabDefId;

	@Column(name = "REIHENFOLGE", updatable = true, nullable = false, unique = false)
	private Long				reihenfolge;

	@Column(name = "SPALTE", updatable = false, nullable = false, unique = false)
	private String				spalte;

	// @OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "TABELLE", referencedColumnName = "ANSICHT", nullable = false, insertable = false, updatable =
	// false)
	// private AnsichtSpalten ansichtSpalten;

	@Column(name = "DBDATENTYP", updatable = false, nullable = false)
	@Enumerated(EnumType.STRING)
	@Type(type = "fxDatentyp")
	private DBTYPE				dbDatentyp;

	@Column(name = "PS", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	// @Column(name = "PS", columnDefinition="NUMBER(11) default 0")
	private Boolean				primSchluessel;

	@Column(name = "EINDEUTIG")
	private Long				eindeutig;

	@Column(name = "EINGABENOTWENDIG", updatable = true, columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	// @Column(name = "EINGABENOTWENDIG", columnDefinition="NUMBER(1) default 0")
	private Boolean				eingabeNotwendig;

	@Column(name = "BEARBERLAUBT", columnDefinition = "NUMBER(1) default -1")
	@Type(type = "fxboolean")
	// @Column(name = "BEARBERLAUBT", columnDefinition="NUMBER(1) default -1")
	private Boolean				bearbErlaubt;

	@Column(name = "SYSTEMWERT", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	// @Column(name = "SYSTEMWERT", columnDefinition="NUMBER(1) default 0")
	private Boolean				systemWert;

	@Column(name = "DEFAULTWERT", updatable = false)
	private String				defaultWert;

	@Column(name = "SPALTENKOPF", updatable = true, nullable = false, unique = false)
	private String				spaltenkopf;

	@Basic
	private String				minimum;

	@Basic
	private String				maximum;

	@Column(name = "SPALTENTYP", updatable = false)
	private String				spaltenTyp;

	@Override
	public Long getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.domain.AbstractMWOFDBEntity#getSequenceKey()
	 */
	@Override
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.domain.AbstractMWOFDBEntity#setId(java.lang.Long)
	 */
	@Override
	public void setId( final Long id ) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.domain.AbstractMWOFDBEntity#getName()
	 */
	@Override
	public String getName() {
		return this.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.domain.AbstractMWOFDBEntity#setName(java.lang.String)
	 */
	@Override
	public void setName( final String name ) {
		// this.name = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#setTabDef(de.mw.mwdata.core.ofdb.domain.TabDef)
	 */
	public void setTabDef( final ITabDef tabDef ) {
		this.tabDef = (TabDef) tabDef;
		this.setTabDefId( tabDef.getId() );
		// this.setName( tabDef.getName() );
		// this.tabelle = tabDef.getName();
		// this.name = tabDef.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#getTabDef()
	 */
	public ITabDef getTabDef() {
		return this.tabDef;
	}

	// public String getTabelle() {
	// return this.tabelle;
	// }
	//
	// public void setTabelle( final String tabelle ) {
	// this.tabelle = tabelle;
	// }

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#getSpalte()
	 */
	public String getSpalte() {
		return this.spalte;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#setSpalte(java.lang.String)
	 */
	public void setSpalte( final String spalte ) {
		this.spalte = spalte;
	}

	public Boolean getPrimSchluessel() {
		return this.primSchluessel;
	}

	public void setPrimSchluessel( final Boolean primSchluessel ) {
		this.primSchluessel = primSchluessel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#getEindeutig()
	 */
	public Long getEindeutig() {
		return this.eindeutig;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#setEindeutig(java.lang.Long)
	 */
	public void setEindeutig( final Long eindeutig ) {
		this.eindeutig = eindeutig;
	}

	public Boolean getEingabeNotwendig() {
		return this.eingabeNotwendig;
	}

	public void setEingabeNotwendig( final Boolean eingabeNotwendig ) {
		this.eingabeNotwendig = eingabeNotwendig;
	}

	public Boolean getBearbErlaubt() {
		return this.bearbErlaubt;
	}

	public void setBearbErlaubt( final Boolean bearbErlaubt ) {
		this.bearbErlaubt = bearbErlaubt;
	}

	public Boolean getSystemWert() {
		return this.systemWert;
	}

	public void setSystemWert( final Boolean systemWert ) {
		this.systemWert = systemWert;
	}

	public String getDefaultWert() {
		return this.defaultWert;
	}

	public void setDefaultWert( final String defaultWert ) {
		this.defaultWert = defaultWert;
	}

	public String getMinimum() {
		return this.minimum;
	}

	public void setMinimum( final String minimum ) {
		this.minimum = minimum;
	}

	public String getMaximum() {
		return this.maximum;
	}

	public void setMaximum( final String maximum ) {
		this.maximum = maximum;
	}

	public String getSpaltenTyp() {
		return this.spaltenTyp;
	}

	public void setSpaltenTyp( final String spaltenTyp ) {
		this.spaltenTyp = spaltenTyp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#setDbDatentyp(de.mw.mwdata.core.ofdb.domain.TabSpeig.DBTYPE)
	 */
	public void setDbDatentyp( final DBTYPE dbDatentyp ) {
		this.dbDatentyp = dbDatentyp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#getDbDatentyp()
	 */
	public DBTYPE getDbDatentyp() {
		return this.dbDatentyp;
	}

	public void setReihenfolge( final Long reihenfolge ) {
		this.reihenfolge = reihenfolge;
	}

	public Long getReihenfolge() {
		return this.reihenfolge;
	}

	public boolean isEnum() {
		return this.getDbDatentyp().equals( ITabSpeig.DBTYPE.ENUM );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.ITabSpeig#isEindeutig()
	 */
	public boolean isEindeutig() {
		return (this.getEindeutig() != null);
	}

	@Override
	public String toString() {
		// return this.getSpalte();
		if ( null != this.tabDef ) {
			return "TabSpeig [" + this.tabDef.getName() + ":" + this.getSpalte() + "]";
		} else {
			return StringUtils.EMPTY;
		}
	}

	public void setSpaltenkopf( final String spaltenkopf ) {
		this.spaltenkopf = spaltenkopf;
	}

	public String getSpaltenkopf() {
		return this.spaltenkopf;
	}

	public void setTabDefId( final Long tabDefId ) {
		this.tabDefId = tabDefId;
	}

	public Long getTabDefId() {
		return this.tabDefId;
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( null == obj ) {
			return false;
		}
		if ( !(obj instanceof TabSpeig) ) {
			return false;
		}

		ITabSpeig otherEntity = (ITabSpeig) obj;
		Validate.notNull( this.getTabDef() );

		if ( null == this.getSpalte() && null != otherEntity.getSpalte() ) {
			return false;
		}
		if ( null == otherEntity.getTabDef() ) {
			return false;
		}
		if ( this.getTabDef().equals( otherEntity.getTabDef() ) ) {
			return false;
		}

		if ( this.getSpalte().equalsIgnoreCase( otherEntity.getSpalte() ) ) {
			return true;
		}

		return false;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.getSpalte() == null) ? 0 : this.getSpalte().hashCode());
		result = (prime * result) + ((this.getTabDef() == null) ? 0 : this.getTabDef().hashCode());
		return result;
	}

}
