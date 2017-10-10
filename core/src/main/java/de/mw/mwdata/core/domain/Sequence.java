/**
 *
 */
package de.mw.mwdata.core.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import de.mw.mwdata.core.Constants;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Oct, 2010
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "SysSequenz" /* , schema = Constants.DB_SCHEMA */)
public class Sequence extends AbstractMWEntity {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;

	public final static String	SEQUENCE_KEY		= "SysSequenz:SequenzID";

	private final static String	SEQUENCENAME		= Constants.DB_SCHEMA + ".SysSequenz";

	@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SEQUENCE")
	// @TableGenerator(name = "SEQ_SEQUENCE", table = "KD_RRE_PROD.SysSequenz", pkColumnName = "NAME", valueColumnName =
	// "LETZTEBELEGTENR", pkColumnValue = SEQUENCE_KEY, allocationSize = 1)
	// @SequenceGenerator(name = "SEQ_SEQUENCE", sequenceName = SEQUENCENAME)
	@GenericGenerator(name = "SEQ_SEQUENCE", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	// , strategy = "de.mw.mwdata.core.db.FxSequenceGenerator"
	@Column(name = "SEQUENZID", updatable = false, nullable = false)
	private Long				id;

	@Column(name = "NAME", updatable = true, nullable = false)
	private String				name;

	@Column(name = "LETZTEBELEGTENR", updatable = true, nullable = false)
	private Long				letzteBelegteNr;

	@Column(name = "INKREMENT", updatable = true, nullable = false)
	private Long				inkrement;

	// @Column(name = "BLOCKGROESSE", updatable = true, nullable = false)
	// private Long blockGroesse;

	@Column(name = "GESPERRTVONLOGINID", updatable = true, nullable = true)
	private Long				gesperrtVonLoginId;

	@Column(name = "SYSTEMDS", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean				systemDs;

	@Column(name = "ZULETZTBEARBEITETVON", updatable = true, nullable = true)
	private Long				zuletztBearbeitetVon;

	@Column(name = "ZULETZTGEAENDERTAM", updatable = true, nullable = true)
	private Date				zuletztGeaendertAm;

	@Column(name = "BESCHREIBUNG", updatable = true, nullable = true)
	private String				beschreibung;

	public Long getLetzteBelegteNr() {
		return this.letzteBelegteNr;
	}

	public void setLetzteBelegteNr( final Long letzteBelegteNr ) {
		this.letzteBelegteNr = letzteBelegteNr;
	}

	public Long getInkrement() {
		return this.inkrement;
	}

	public void setInkrement( final Long inkrement ) {
		this.inkrement = inkrement;
	}

	// public Long getBlockGroesse() {
	// return this.blockGroesse;
	// }
	//
	// public void setBlockGroesse( final Long blockGroesse ) {
	// this.blockGroesse = blockGroesse;
	// }

	public Long getGesperrtVonLoginId() {
		return this.gesperrtVonLoginId;
	}

	public void setGesperrtVonLoginId( final Long gesperrtVonLoginId ) {
		this.gesperrtVonLoginId = gesperrtVonLoginId;
	}

	public Boolean isSystemDs() {
		return this.systemDs;
	}

	public void setSystemDs( final Boolean systemDs ) {
		this.systemDs = systemDs;
	}

	public Long getZuletztBearbeitetVon() {
		return this.zuletztBearbeitetVon;
	}

	public void setZuletztBearbeitetVon( final Long zuletztBearbeitetVon ) {
		this.zuletztBearbeitetVon = zuletztBearbeitetVon;
	}

	public Date getZuletztGeaendertAm() {
		return this.zuletztGeaendertAm;
	}

	public void setZuletztGeaendertAm( final Date zuletztGeaendertAm ) {
		this.zuletztGeaendertAm = zuletztGeaendertAm;
	}

	public String getBeschreibung() {
		return this.beschreibung;
	}

	public void setBeschreibung( final String beschreibung ) {
		this.beschreibung = beschreibung;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	@Override
	public void setId( final Long id ) {
		this.id = id;
	}

	@Override
	public void setName( final String name ) {
		this.name = name;
	}

	/**
	 * @return the next free id incremented with columnvalue INKREMENT
	 */
	public Long getNaechsteNr() {
		return getLetzteBelegteNr() + getInkrement();
	}

	public void increment() {
		this.letzteBelegteNr = this.letzteBelegteNr + this.inkrement;
	}

}
