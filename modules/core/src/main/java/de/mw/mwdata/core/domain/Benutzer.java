package de.mw.mwdata.core.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * Class for managing the Users.
 *
 * @version 1.0
 * @author mwilbers
 * @since May, 2012
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "BenutzerDef", uniqueConstraints = @UniqueConstraint(columnNames = { "BENUTZERNAME" }))
// @FilterDef(name = "BereichNotImported")
// @Filter(name = "BereichNotImported", condition = "importId is null")
public class Benutzer extends AbstractMWEntity {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -3289426633012773081L;

	private static final String	SEQUENCE_KEY		= "Benutzerdef:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID", insertable = false, updatable = false, nullable = false)
	private Long				id;

	@Column(name = "BENUTZERNAME", unique = true, updatable = true, nullable = false)
	private String				name;

	@Column(name = "PASSWORT", unique = false, updatable = true, nullable = false)
	private String				passwort;

	@Column(name = "DEAKTIVIERT", columnDefinition = "NUMBER(1) default 0", updatable = false, nullable = false)
	@Type(type = "fxboolean")
	private boolean				deaktiviert;

	@Column(name = "OBJEKTROLLENID", updatable = true, nullable = true)
	private Integer				objektRollenId;

	// @ManyToOne(targetEntity = BenutzerRecht.class, fetch = FetchType.LAZY)
	// @JoinColumn(name = "DSID", referencedColumnName = "BENUTZERID", insertable = false, updatable = false)
	// private BenutzerRecht benutzerRecht;

	@OneToMany(mappedBy = "benutzer")
	private List<BenutzerRecht>	benutzerRechte;

	// @Column(name = "NAMEGB", updatable = true, nullable = false)
	// private String nameGb;

	// Member <--> ManyToOne <--> MemberRole <--> OneToMany <--> Role

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

	public void setPasswort( final String passwort ) {
		this.passwort = passwort;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setDeaktiviert( final boolean deaktiviert ) {
		this.deaktiviert = deaktiviert;
	}

	public boolean isDeaktiviert() {
		return this.deaktiviert;
	}

	public void setObjektRollenId( final int objektRollenId ) {
		this.objektRollenId = objektRollenId;
	}

	public int getObjektRollenId() {
		return this.objektRollenId;
	}

	// public void setBenutzerRecht( final BenutzerRecht benutzerRecht ) {
	// this.benutzerRecht = benutzerRecht;
	// }
	//
	// public BenutzerRecht getBenutzerRecht() {
	// return this.benutzerRecht;
	// }

	public void setBenutzerRechte( final List<BenutzerRecht> benutzerRechte ) {
		this.benutzerRechte = benutzerRechte;
	}

	public List<BenutzerRecht> getBenutzerRechte() {
		return this.benutzerRechte;
	}

}
