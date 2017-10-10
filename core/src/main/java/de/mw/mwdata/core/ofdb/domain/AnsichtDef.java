package de.mw.mwdata.core.ofdb.domain;

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
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;

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
public class AnsichtDef extends AbstractMWEntity implements IAnsichtDef {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;

	private final static String	SEQUENCE_KEY		= "FX_AnsichtDef_K:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long				id;

	@Column(name = "ANSICHT", updatable = false, nullable = false, unique = true)
	private String				name;

	// @OneToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "ANSICHT", referencedColumnName = "TABELLE", nullable = false, insertable = false, updatable =
	// false)
	// private TabDef tabDef;

	// @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// @JoinColumn(name = "ANSICHT", referencedColumnName = "ANSICHT")
	// private List<AnsichtSpalten> ansichtSpalten;
	//
	//
	// public List<AnsichtSpalten> getAnsichtSpalten() {
	// return this.ansichtSpalten;
	// }
	//
	// public void setAnsichtSpalten(List<AnsichtSpalten> ansichtSpalten) {
	// this.ansichtSpalten = ansichtSpalten;
	// }

	// @Column(name = "BEREICH")
	// @ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "BEREICH", referencedColumnName = "name", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BEREICHSID", referencedColumnName = "BEREICHSID", nullable = false)
	private BenutzerBereich		bereich;

	@Column(name = "BEZEICHNUNG")
	private String				bezeichnung;

	// @Column(name = "EXPORTIEREN", columnDefinition = "NUMERIC(11) default 0")
	// @Type(type = "fxboolean")
	// private Boolean exportieren;

	@Column(name = "HINZUFUEGEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean				hinzufuegen;

	// @Column(name = "KOPIEREN", columnDefinition = "NUMBER(11) default 0")
	// @Type(type = "fxboolean")
	// private Boolean kopieren;

	@Column(name = "BEARBEITEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean				bearbeiten;

	@Column(name = "ENTFERNEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean				entfernen;

	// @Column(name = "ENTFERNENMEHRFACH", columnDefinition = "NUMBER(11) default 0")
	// @Type(type = "fxboolean")
	// private Boolean entfernenMehrfach;

	// @Column(name = "IMPORTIEREN", columnDefinition = "NUMBER(11) default 0")
	// @Type(type = "fxboolean")
	// private Boolean importieren;

	// @Column(name = "STORNIEREN", columnDefinition = "NUMBER(11) default 0")
	// @Type(type = "fxboolean")
	// private Boolean stornieren;

	@Column(name = "LESEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean				lesen;

	@Column(name = "FILTER", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean				filter;

	@Column(name = "SORTIEREN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean				sortieren;

	@Column(name = "URLPATH", nullable = true)
	private String				urlPath;

	@Column(name = "APPCONTEXTPATH", nullable = true)
	private String				appContextPath;

	@Column(name = "REIHENFOLGE", updatable = true, nullable = true, unique = true)
	private Integer				reihenfolge;

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

	public BenutzerBereich getBereich() {
		return this.bereich;
	}

	public void setBereich( final BenutzerBereich bereich ) {
		this.bereich = bereich;
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}

	public void setBezeichnung( final String bezeichnung ) {
		this.bezeichnung = bezeichnung;
	}

	// public Boolean getExportieren() {
	// return this.exportieren;
	// }
	//
	// public void setExportieren( final Boolean exportieren ) {
	// this.exportieren = exportieren;
	// }

	public Boolean getHinzufuegen() {
		return this.hinzufuegen;
	}

	public void setHinzufuegen( final Boolean hinzufuegen ) {
		this.hinzufuegen = hinzufuegen;
	}

	// public Boolean getKopieren() {
	// return this.kopieren;
	// }
	//
	// public void setKopieren( final Boolean kopieren ) {
	// this.kopieren = kopieren;
	// }

	public Boolean getEntfernen() {
		return this.entfernen;
	}

	public void setEntfernen( final Boolean entfernen ) {
		this.entfernen = entfernen;
	}

	// public Boolean getEntfernenMehrfach() {
	// return this.entfernenMehrfach;
	// }
	//
	// public void setEntfernenMehrfach( final Boolean entfernenMehrfach ) {
	// this.entfernenMehrfach = entfernenMehrfach;
	// }
	//
	// public Boolean getImportieren() {
	// return this.importieren;
	// }
	//
	// public void setImportieren( final Boolean importieren ) {
	// this.importieren = importieren;
	// }
	//
	// public Boolean getStornieren() {
	// return this.stornieren;
	// }
	//
	// public void setStornieren( final Boolean stornieren ) {
	// this.stornieren = stornieren;
	// }

	public Boolean getLesen() {
		return this.lesen;
	}

	public void setLesen( final Boolean lesen ) {
		this.lesen = lesen;
	}

	public Boolean getFilter() {
		return this.filter;
	}

	public void setFilter( final Boolean filter ) {
		this.filter = filter;
	}

	public Boolean getSortieren() {
		return this.sortieren;
	}

	public void setSortieren( final Boolean sortieren ) {
		this.sortieren = sortieren;
	}

	public void setBearbeiten( final Boolean bearbeiten ) {
		this.bearbeiten = bearbeiten;
	}

	public Boolean getBearbeiten() {
		return this.bearbeiten;
	}

	// public void setTabDef(TabDef tabDef) {
	// this.tabDef = tabDef;
	// }
	//
	// public TabDef getTabDef() {
	// return tabDef;
	// }

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtDef#setUrlPath(java.lang.String)
	 */
	public void setUrlPath( final String urlPath ) {
		this.urlPath = urlPath;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtDef#getUrlPath()
	 */
	public String getUrlPath() {
		return this.urlPath;
	}

	@Override
	public String toString() {
		return "AnsichtDef [name = '" + this.name + "']";
	}

	public void setReihenfolge( final Integer reihenfolge ) {
		this.reihenfolge = reihenfolge;
	}

	public Integer getReihenfolge() {
		return this.reihenfolge;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtDef#setAppContextPath(java.lang.String)
	 */
	public void setAppContextPath( final String appContextPath ) {
		this.appContextPath = appContextPath;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtDef#getAppContextPath()
	 */
	public String getAppContextPath() {
		return this.appContextPath;
	}

	@Override
	public boolean equals( final Object arg0 ) {

		// compare names

		if ( null == arg0 ) {
			return false;
		}
		AnsichtDef orgAnsicht = (AnsichtDef) arg0;

		if ( null == this.getName() && null != orgAnsicht.getName() ) {
			return false;
		}

		return this.getName().equals( orgAnsicht.getName() );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.getName() == null) ? 0 : this.getName().hashCode());
		return result;
	}

}
