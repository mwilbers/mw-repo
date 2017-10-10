/**
 *
 */
package de.mw.mwdata.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import de.mw.mwdata.core.Constants;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "BenutzerBereicheDef" /* , schema = Constants.DB_SCHEMA */, uniqueConstraints = @UniqueConstraint(columnNames = {
		"name" }))
@FilterDef(name = "BereichNotImported")
@Filter(name = "BereichNotImported", condition = "importId is null")
public class BenutzerBereich extends AbstractMWEntity {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -7210768017173573098L;

	private final static String	SEQUENCE_KEY		= "BenutzerBereicheDef:BereichsID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "BEREICHSID", updatable = false, nullable = false)
	private Long				id;

	@Column(name = "NAME", unique = true, updatable = true, nullable = false)
	private String				name;

	// @Column(name = "NAMEGB", updatable = true, nullable = false)
	// private String nameGb;

	// @Column(name = "ABRECHNEN", columnDefinition = "NUMBER(1) default 0")
	// @Type(type = "fxboolean")
	// private Boolean abrechnen;

	@Column(name = "BEARBEITEN", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean				bearbeiten;

	// @Column(name = "BERECHNEN", columnDefinition = "NUMBER(1) default 0")
	// @Type(type = "fxboolean")
	// private Boolean berechnen;

	// @Column(name = "EXPORTIEREN", columnDefinition = "NUMBER(1) default 0")
	// @Type(type = "fxboolean")
	// private Boolean exportieren;

	@Column(name = "HINZUFUEGEN", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean				hinzufuegen;

	// @Column(name = "IMPORTIEREN", columnDefinition = "NUMBER(1) default 0")
	// @Type(type = "fxboolean")
	// private Boolean importieren;

	@Column(name = "LESEN", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean				lesen;

	@Column(name = "LOESCHEN", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean				loeschen;

	// @Column(name = "STORNIEREN", columnDefinition = "NUMBER(1) default 0")
	// @Type(type = "fxboolean")
	// private Boolean stornieren;

	// @Column(name = "VERGEBBAR", columnDefinition = "NUMBER(1) default 0")
	// @Type(type = "fxboolean")
	// private Boolean vergebbar;

	@Column(name = "BESCHREIBUNG", updatable = true, nullable = true)
	private String				beschreibung;

	@Column(name = Constants.SYS_COL_IMPORT_ID, updatable = true, nullable = true)
	private Long				importId;

	// private BenutzerRecht benutzerRecht;

	// @OneToMany(mappedBy = "bereich")
	// private List<BenutzerRecht> benutzerRechte;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.kh.fx.domain.AbstractMWEntity#getSequenceKey()
	 */
	@Override
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.kh.fx.domain.AbstractMWEntity#getId()
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.kh.fx.domain.AbstractMWEntity#setId(java.lang.Long)
	 */
	@Override
	public void setId( final Long id ) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.kh.fx.domain.AbstractMWEntity#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.kh.fx.domain.AbstractMWEntity#setName(java.lang.String)
	 */
	@Override
	public void setName( final String name ) {
		this.name = name;
	}

	// public void setAbrechnen( final Boolean abrechnen ) {
	// this.abrechnen = abrechnen;
	// }
	//
	// public Boolean getAbrechnen() {
	// return this.abrechnen;
	// }

	public void setBearbeiten( final Boolean bearbeiten ) {
		this.bearbeiten = bearbeiten;
	}

	public Boolean getBearbeiten() {
		return this.bearbeiten;
	}

	// public void setBerechnen( final Boolean berechnen ) {
	// this.berechnen = berechnen;
	// }
	//
	// public Boolean getBerechnen() {
	// return this.berechnen;
	// }
	//
	// public void setExportieren( final Boolean exportieren ) {
	// this.exportieren = exportieren;
	// }
	//
	// public Boolean getExportieren() {
	// return this.exportieren;
	// }

	public void setHinzufuegen( final Boolean hinzufuegen ) {
		this.hinzufuegen = hinzufuegen;
	}

	public Boolean getHinzufuegen() {
		return this.hinzufuegen;
	}

	// public void setImportieren( final Boolean importieren ) {
	// this.importieren = importieren;
	// }
	//
	// public Boolean getImportieren() {
	// return this.importieren;
	// }

	public void setLesen( final Boolean lesen ) {
		this.lesen = lesen;
	}

	public Boolean getLesen() {
		return this.lesen;
	}

	public void setLoeschen( final Boolean loeschen ) {
		this.loeschen = loeschen;
	}

	public Boolean getLoeschen() {
		return this.loeschen;
	}

	// public void setStornieren( final Boolean stornieren ) {
	// this.stornieren = stornieren;
	// }
	//
	// public Boolean getStornieren() {
	// return this.stornieren;
	// }
	//
	// public void setVergebbar( final Boolean vergebbar ) {
	// this.vergebbar = vergebbar;
	// }
	//
	// public Boolean getVergebbar() {
	// return this.vergebbar;
	// }

	public void setBeschreibung( final String beschreibung ) {
		this.beschreibung = beschreibung;
	}

	public String getBeschreibung() {
		return this.beschreibung;
	}

	// public void setUnterschrift1( final Boolean unterschrift1 ) {
	// this.unterschrift1 = unterschrift1;
	// }
	//
	// public Boolean getUnterschrift1() {
	// return this.unterschrift1;
	// }
	//
	// public void setUnterschrift2( final Boolean unterschrift2 ) {
	// this.unterschrift2 = unterschrift2;
	// }
	//
	// public Boolean getUnterschrift2() {
	// return this.unterschrift2;
	// }
	//
	// public void setUnterschrift3( final Boolean unterschrift3 ) {
	// this.unterschrift3 = unterschrift3;
	// }
	//
	// public Boolean getUnterschrift3() {
	// return this.unterschrift3;
	// }
	//
	// public void setAlleDs( final Boolean alleDs ) {
	// this.alleDs = alleDs;
	// }
	//
	// public Boolean getAlleDs() {
	// return this.alleDs;
	// }
	//
	// public void setVerschluesseln( final Boolean verschluesseln ) {
	// this.verschluesseln = verschluesseln;
	// }
	//
	// public Boolean getVerschluesseln() {
	// return this.verschluesseln;
	// }
	//
	// public void setNameGb( final String nameGb ) {
	// this.nameGb = nameGb;
	// }
	//
	// public String getNameGb() {
	// return this.nameGb;
	// }

	// public void setAdminAutoZuweis( final Boolean adminAutoZuweis ) {
	// this.adminAutoZuweis = adminAutoZuweis;
	// }
	//
	// public Boolean getAdminAutoZuweis() {
	// return this.adminAutoZuweis;
	// }

	@Override
	public boolean equals( final Object obj ) {
		// return super.equals( obj );

		if ( null == obj ) {
			return false;
		}

		BenutzerBereich oBereich = (BenutzerBereich) obj;

		if ( null == this.getId() && null == oBereich.getId() ) {
			throw new IllegalArgumentException( "Bereich.Id is null" );
		}

		if ( null == this.getId() || null == oBereich.getId() ) {
			return false;
		}

		return (this.getId() == oBereich.getId());

	}

	// public void setBenutzerRecht( final BenutzerRecht benutzerRecht ) {
	// this.benutzerRecht = benutzerRecht;
	// }
	//
	// public BenutzerRecht getBenutzerRecht() {
	// return benutzerRecht;
	// }

	// public void setBenutzerRechte( final List<BenutzerRecht> benutzerRechte ) {
	// this.benutzerRechte = benutzerRechte;
	// }
	//
	// public List<BenutzerRecht> getBenutzerRechte() {
	// return this.benutzerRechte;
	// }

	public void setImportId( final Long importId ) {
		this.importId = importId;
	}

	public Long getImportId() {
		return this.importId;
	}

}
