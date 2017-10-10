package de.mw.mwdata.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * Class for managing the Users. DB-Table 'BENUTZERRECHTE'
 *
 * @version 1.0
 * @author mwilbers
 * @since May, 2012
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "BenutzerRechte", uniqueConstraints = @UniqueConstraint(columnNames = { "BENUTZERID", "BEREICHSID" }))
// @FilterDef(name = "BereichNotImported")
// @Filter(name = "BereichNotImported", condition = "importId is null")
public class BenutzerRecht extends AbstractMWEntity {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 5754297219514455982L;

	private static final String	SEQUENCE_KEY		= "Benutzerrechte:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID", updatable = false, nullable = false)
	private Long				id;

	// @Column(name = "BENUTZERNAME", unique = true, updatable = true, nullable = false)
	@Transient
	private String				name;

	@Column(name = "BENUTZERID", updatable = true, nullable = false, unique = false)
	private Integer				benutzerId;

	@Column(name = "BEREICHSID", updatable = true, nullable = false, unique = false)
	private Integer				bereichsId;

	@Column(name = "BEARBEITEN", updatable = true, columnDefinition = "NUMBER(1) default -1")
	@Type(type = "fxboolean")
	private Boolean				bearbeiten;

	@Column(name = "HINZUFUEGEN", updatable = true, columnDefinition = "NUMBER(1) default -1")
	@Type(type = "fxboolean")
	private Boolean				hinzufuegen;

	@Column(name = "LOESCHEN", updatable = true, columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean				loeschen;

	@ManyToOne
	@JoinColumn(name = "BENUTZERID", insertable = false, updatable = false)
	private Benutzer			benutzer;

	@ManyToOne
	@JoinColumn(name = "BEREICHSID", insertable = false, updatable = false)
	private BenutzerBereich		bereich;

	// @OneToMany(targetEntity = BenutzerBereich.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	// @JoinColumn(name = "BEREICHSID")
	// private List<BenutzerBereich> bereiche;

	@Override
	public String getSequenceKey() {
		return this.SEQUENCE_KEY;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId( final Long id ) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName( final String name ) {
		this.name = name;
	}

	public void setBenutzerId( final Integer benutzerId ) {
		this.benutzerId = benutzerId;
	}

	public Integer getBenutzerId() {
		return this.benutzerId;
	}

	public void setBereichsId( final Integer bereichsId ) {
		this.bereichsId = bereichsId;
	}

	public Integer getBereichsId() {
		return this.bereichsId;
	}

	public void setBearbeiten( final Boolean bearbeiten ) {
		this.bearbeiten = bearbeiten;
	}

	public Boolean getBearbeiten() {
		return this.bearbeiten;
	}

	public void setHinzufuegen( final Boolean hinzufuegen ) {
		this.hinzufuegen = hinzufuegen;
	}

	public Boolean getHinzufuegen() {
		return this.hinzufuegen;
	}

	public void setLoeschen( final Boolean loeschen ) {
		this.loeschen = loeschen;
	}

	public Boolean getLoeschen() {
		return this.loeschen;
	}

	public void setBenutzer( final Benutzer benutzer ) {
		this.benutzer = benutzer;
	}

	public Benutzer getBenutzer() {
		return this.benutzer;
	}

	public void setBereich( final BenutzerBereich bereich ) {
		this.bereich = bereich;
	}

	public BenutzerBereich getBereich() {
		return this.bereich;
	}

	// public void setBereiche( final List<BenutzerBereich> bereiche ) {
	// this.bereiche = bereiche;
	// }
	//
	// public List<BenutzerBereich> getBereiche() {
	// return this.bereiche;
	// }

}
