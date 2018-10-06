package de.mw.mwdata.ofdb.domain.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;

/**
 * Class for managing entries in the table FX_AnsichtDef_K
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since July, 2010
 *
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_AnsichtDef_K" /* , schema = Constants.DB_SCHEMA */)
@JsonPropertyOrder({ "id", "name", "bereich", "bereichsId" })
public class AnsichtDef extends AbstractMWEntity implements IAnsichtDef {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static String SEQUENCE_KEY = "FX_AnsichtDef_K:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long id;

	@Column(name = "ANSICHT", updatable = false, nullable = false, unique = true)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BEREICHSID", referencedColumnName = "BEREICHSID", insertable = false, updatable = false, nullable = false)
	private BenutzerBereich bereich;

	@Column(name = "BEREICHSID", insertable = true, updatable = true, nullable = false)
	private Long bereichsId;

	@Column(name = "BEZEICHNUNG")
	private String bezeichnung;

	@Column(name = "HINZUFUEGEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean hinzufuegen;

	@Column(name = "BEARBEITEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean bearbeiten;

	@Column(name = "ENTFERNEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean entfernen;

	@Column(name = "LESEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean lesen;

	@Column(name = "FILTER", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean filter;

	@Column(name = "SORTIEREN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean sortieren;

	@Column(name = "URLPATH", nullable = true)
	private String urlPath;

	@Column(name = "REIHENFOLGE", updatable = true, nullable = true, unique = true)
	private Integer reihenfolge;

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

	public BenutzerBereich getBereich() {
		return this.bereich;
	}

	public void setBereich(final BenutzerBereich bereich) {
		this.bereich = bereich;
		this.bereichsId = bereich.getId();
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}

	public void setBezeichnung(final String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public Boolean getHinzufuegen() {
		return this.hinzufuegen;
	}

	public void setHinzufuegen(final Boolean hinzufuegen) {
		this.hinzufuegen = hinzufuegen;
	}

	public Boolean getEntfernen() {
		return this.entfernen;
	}

	public void setEntfernen(final Boolean entfernen) {
		this.entfernen = entfernen;
	}

	public Boolean getLesen() {
		return this.lesen;
	}

	public void setLesen(final Boolean lesen) {
		this.lesen = lesen;
	}

	public Boolean getFilter() {
		return this.filter;
	}

	public void setFilter(final Boolean filter) {
		this.filter = filter;
	}

	public Boolean getSortieren() {
		return this.sortieren;
	}

	public void setSortieren(final Boolean sortieren) {
		this.sortieren = sortieren;
	}

	public void setBearbeiten(final Boolean bearbeiten) {
		this.bearbeiten = bearbeiten;
	}

	public Boolean getBearbeiten() {
		return this.bearbeiten;
	}

	@Override
	public void setUrlPath(final String urlPath) {
		this.urlPath = urlPath;
	}

	@Override
	public String getUrlPath() {
		return this.urlPath;
	}

	@Override
	public String toString() {
		return "AnsichtDef [name = '" + this.name + "']";
	}

	public void setReihenfolge(final Integer reihenfolge) {
		this.reihenfolge = reihenfolge;
	}

	public Integer getReihenfolge() {
		return this.reihenfolge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnsichtDef other = (AnsichtDef) obj;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name)) {
			return false;
		}

		return true;
	}

	public Long getBereichsId() {
		return bereichsId;
	}

	public void setBereichsId(Long bereichsId) {
		this.bereichsId = bereichsId;
	}

}
