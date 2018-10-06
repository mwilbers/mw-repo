/**
 *
 */
package de.mw.mwdata.ofdb.domain.impl;

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
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since July, 2010
 *
 *        FIXME: equals, hashCode implementieren
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class),
		@TypeDef(name = "fxDatentyp", typeClass = de.mw.mwdata.ofdb.domain.enums.TypeDATENTYP.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_TabSpEig_K" /* , schema = Constants.DB_SCHEMA */, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "TABDEFID", "SPALTE" }),
		@UniqueConstraint(columnNames = { "TABDEFID", "REIHENFOLGE" }) })
@JsonPropertyOrder({ "id", "name", "tabDef", "tabDefId" })
public class TabSpeig extends AbstractMWEntity implements ITabSpeig {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static String SEQUENCE_KEY = "FX_TabSpEig_K:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TABDEFID", referencedColumnName = "DSID", nullable = false, updatable = false, insertable = false)
	private TabDef tabDef;

	@Column(name = "TABDEFID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long tabDefId;

	@Column(name = "REIHENFOLGE", updatable = true, nullable = false, unique = false)
	private Long reihenfolge;

	@Column(name = "SPALTE", updatable = false, nullable = false, unique = false)
	private String spalte;

	@Column(name = "DBDATENTYP", updatable = false, nullable = false)
	@Enumerated(EnumType.STRING)
	@Type(type = "fxDatentyp")
	private DBTYPE dbDatentyp;

	@Column(name = "PS", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean primSchluessel;

	@Column(name = "EINDEUTIG")
	private Long eindeutig;

	@Column(name = "EINGABENOTWENDIG", updatable = true, columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean eingabeNotwendig;

	@Column(name = "BEARBERLAUBT", columnDefinition = "NUMBER(1) default -1")
	@Type(type = "fxboolean")
	private Boolean bearbErlaubt;

	@Column(name = "SYSTEMWERT", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean systemWert;

	@Column(name = "DEFAULTWERT", updatable = false)
	private String defaultWert;

	@Column(name = "SPALTENKOPF", updatable = true, nullable = false, unique = false)
	private String spaltenkopf;

	@Basic
	private String minimum;

	@Basic
	private String maximum;

	@Column(name = "SPALTENTYP", updatable = false)
	private String spaltenTyp;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.toString();
	}

	@Override
	public void setName(final String name) {
		// this.name = name;
	}

	public void setTabDef(final ITabDef tabDef) {
		this.tabDef = (TabDef) tabDef;
		this.setTabDefId(tabDef.getId());
	}

	public ITabDef getTabDef() {
		return this.tabDef;
	}

	public String getSpalte() {
		return this.spalte;
	}

	public void setSpalte(final String spalte) {
		this.spalte = spalte;
	}

	public Boolean getPrimSchluessel() {
		return this.primSchluessel;
	}

	public void setPrimSchluessel(final Boolean primSchluessel) {
		this.primSchluessel = primSchluessel;
	}

	public Long getEindeutig() {
		return this.eindeutig;
	}

	public void setEindeutig(final Long eindeutig) {
		this.eindeutig = eindeutig;
	}

	public Boolean getEingabeNotwendig() {
		return this.eingabeNotwendig;
	}

	public void setEingabeNotwendig(final Boolean eingabeNotwendig) {
		this.eingabeNotwendig = eingabeNotwendig;
	}

	public Boolean getBearbErlaubt() {
		return this.bearbErlaubt;
	}

	public void setBearbErlaubt(final Boolean bearbErlaubt) {
		this.bearbErlaubt = bearbErlaubt;
	}

	public Boolean getSystemWert() {
		return this.systemWert;
	}

	public void setSystemWert(final Boolean systemWert) {
		this.systemWert = systemWert;
	}

	public String getDefaultWert() {
		return this.defaultWert;
	}

	public void setDefaultWert(final String defaultWert) {
		this.defaultWert = defaultWert;
	}

	public String getMinimum() {
		return this.minimum;
	}

	public void setMinimum(final String minimum) {
		this.minimum = minimum;
	}

	public String getMaximum() {
		return this.maximum;
	}

	public void setMaximum(final String maximum) {
		this.maximum = maximum;
	}

	public String getSpaltenTyp() {
		return this.spaltenTyp;
	}

	public void setSpaltenTyp(final String spaltenTyp) {
		this.spaltenTyp = spaltenTyp;
	}

	public void setDbDatentyp(final DBTYPE dbDatentyp) {
		this.dbDatentyp = dbDatentyp;
	}

	public DBTYPE getDbDatentyp() {
		return this.dbDatentyp;
	}

	public void setReihenfolge(final Long reihenfolge) {
		this.reihenfolge = reihenfolge;
	}

	public Long getReihenfolge() {
		return this.reihenfolge;
	}

	public boolean isEnum() {
		return this.getDbDatentyp().equals(DBTYPE.ENUM);
	}

	public boolean isEindeutig() {
		return (this.getEindeutig() != null);
	}

	@Override
	public String toString() {
		// return this.getSpalte();
		if (null != this.tabDef) {
			return "TabSpeig [" + this.tabDef.getName() + ":" + this.getSpalte() + "]";
		} else {
			return StringUtils.EMPTY;
		}
	}

	public void setSpaltenkopf(final String spaltenkopf) {
		this.spaltenkopf = spaltenkopf;
	}

	public String getSpaltenkopf() {
		return this.spaltenkopf;
	}

	public void setTabDefId(final Long tabDefId) {
		this.tabDefId = tabDefId;
	}

	public Long getTabDefId() {
		return this.tabDefId;
	}

	@Override
	public boolean equals(final Object obj) {

		if (null == obj) {
			return false;
		}
		if (!(obj instanceof TabSpeig)) {
			return false;
		}

		ITabSpeig otherEntity = (ITabSpeig) obj;
		Validate.notNull(this.getTabDef());

		if (null == this.getSpalte() && null != otherEntity.getSpalte()) {
			return false;
		}
		if (null == otherEntity.getTabDef()) {
			return false;
		}
		if (this.getTabDef().equals(otherEntity.getTabDef())) {
			return false;
		}

		if (this.getSpalte().equalsIgnoreCase(otherEntity.getSpalte())) {
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
