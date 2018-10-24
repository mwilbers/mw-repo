/**
 *
 */
package de.mw.mwdata.ofdb.domain.impl;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IMenue;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Feb, 2012
 */
@TypeDefs({ @TypeDef(name = "enumMenuetyp", typeClass = de.mw.mwdata.ofdb.domain.enums.TypeMENUETYP.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_Menues_K" /* , schema = Constants.DB_SCHEMA */, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "MENUEID" }) })
@JsonPropertyOrder({ "id", "name", "bereich", "bereichsId", "ansichtDef", "ansichtDefId" })
public class Menue extends AbstractMWEntity implements IMenue {

	/**
	 *
	 */
	private static final long serialVersionUID = -3719655087290529909L;

	private final static String SEQUENCE_KEY = "FX_Menues_K:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long id;

	@Column(name = "MENUEID", updatable = false, nullable = false, unique = false)
	private Long menuId;

	@Column(name = "MENUE", updatable = false, nullable = false, unique = true)
	private String name;

	@Column(name = "UNTERMENUEVON", nullable = true)
	private String untermenueVon;

	@Column(name = "HAUPTMENUEID", insertable = true, updatable = true, nullable = true, unique = false)
	private Long hauptMenueId;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "HAUPTMENUEID", referencedColumnName = "DSID", insertable = false, updatable = false, nullable = true)
	private Menue hauptMenue;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "HAUPTMENUEID", referencedColumnName = "DSID", insertable = false, updatable = false)
	private List<Menue> unterMenues;

	@Column(name = "ANZEIGENAME", nullable = false)
	private String anzeigeName;

	@Column(name = "TYP", updatable = true, nullable = false)
	@Enumerated(EnumType.STRING)
	@Type(type = "enumMenuetyp")
	private MENUETYP typ;

	@Column(name = "EBENE", nullable = false)
	private Integer ebene;

	@Column(name = "BEREICHSID", insertable = true, updatable = true, nullable = true, unique = false)
	private Long bereichsId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BEREICHSID", referencedColumnName = "BEREICHSID", insertable = false, updatable = false, nullable = true)
	private BenutzerBereich bereich;

	@Column(name = "ANSICHTDEFID", insertable = true, updatable = true, nullable = true, unique = false)
	private Long ansichtDefId;
	//
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "ANSICHTDEFID", referencedColumnName = "DSID", updatable = false, insertable = false, nullable = true)
	private AnsichtDef ansichtDef;

	@Column(name = "LIZENZ", nullable = true)
	private String lizenz;

	@Column(name = "GRUPPE", nullable = true)
	private String gruppe;

	@Column(name = "AKTIV", updatable = true, columnDefinition = "NUMBER(1) default -1")
	@Type(type = "fxboolean")
	private Boolean aktiv;

	@Override
	public String getAnzeigeName() {
		return this.anzeigeName;
	}

	public void setAnzeigeName(final String anzeigeName) {
		this.anzeigeName = anzeigeName;
	}

	public List<Menue> getUnterMenues() {
		return this.unterMenues;
	}

	public void setUnterMenues(final List<Menue> unterMenues) {
		this.unterMenues = unterMenues;
	}

	@Override
	public MENUETYP getTyp() {
		return this.typ;
	}

	public void setTyp(final MENUETYP typ) {
		this.typ = typ;
	}

	public Integer getEbene() {
		return this.ebene;
	}

	public void setEbene(final Integer ebene) {
		this.ebene = ebene;
	}

	public String getUntermenueVon() {
		return this.untermenueVon;
	}

	public void setUntermenueVon(final String untermenueVon) {
		this.untermenueVon = untermenueVon;
	}

	public BenutzerBereich getBereich() {
		return this.bereich;
	}

	public void setBereich(final BenutzerBereich bereich) {
		this.bereich = bereich;
		this.bereichsId = bereich.getId();
	}

	public String getLizenz() {
		return this.lizenz;
	}

	public void setLizenz(final String lizenz) {
		this.lizenz = lizenz;
	}

	public String getGruppe() {
		return this.gruppe;
	}

	public void setGruppe(final String gruppe) {
		this.gruppe = gruppe;
	}

	@Override
	public String getSequenceKey() {
		return this.SEQUENCE_KEY;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public void setMenuId(final Long menuId) {
		this.menuId = menuId;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setAktiv(final Boolean aktiv) {
		this.aktiv = aktiv;
	}

	public Boolean getAktiv() {
		return this.aktiv;
	}

	public void setAnsichtDef(final AnsichtDef ansichtDef) {
		this.ansichtDef = ansichtDef;
		this.ansichtDefId = ansichtDef.getId();
	}

	public IAnsichtDef getAnsichtDef() {
		return this.ansichtDef;
	}

	public void setHauptMenueId(final Long hauptMenueId) {
		this.hauptMenueId = hauptMenueId;
	}

	public Long getHauptMenueId() {
		return this.hauptMenueId;
	}

	public Menue getHauptMenue() {
		return hauptMenue;
	}

	public void setHauptMenue(Menue hauptMenue) {
		this.hauptMenue = hauptMenue;
		this.hauptMenueId = hauptMenue.getId();
	}

	// TODO: implement equals, hashCode here and in other domain-classes

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Menue)) {
			return false;
		}
		final Menue menue = (Menue) other;
		if (!menue.getId().equals(this.getId())) {
			return false;
		}
		if (!menue.getMenuId().equals(this.getMenuId())) {
			return false;
		}
		if (!menue.getName().equals(this.getName())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = this.getName().hashCode();
		result = (int) (29 * result + this.getMenuId());
		return result;
	}

}
