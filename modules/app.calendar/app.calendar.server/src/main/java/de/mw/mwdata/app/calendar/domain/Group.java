/**
 *
 */
package de.mw.mwdata.app.calendar.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import de.mw.mwdata.core.domain.AbstractMWEntity;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "CAL_GRUPPE" /* , schema = Constants.DB_SCHEMA */, uniqueConstraints = @UniqueConstraint(columnNames = {
		"GRUPPENAME", "ORTID" }))
public class Group extends AbstractMWEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2477693255659630342L;
	private final static String SEQUENCE_KEY = "CAL_GRUPPE:GRUPPEID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "GRUPPEID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "GRUPPENAME", unique = false, updatable = true, nullable = false)
	private String name;

	@Column(name = "ORTID", insertable = true, updatable = true, nullable = false)
	private Long locationId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORTID", referencedColumnName = "ORTID", insertable = false, updatable = false, nullable = false)
	private Location location;

	@Column(name = "KATEGORIEID", insertable = true, updatable = true, nullable = false)
	private Long categoryId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "KATEGORIEID", referencedColumnName = "KATEGORIEID", insertable = false, updatable = false, nullable = false)
	private Category category;

	@Column(name = "KALENDARID", unique = true, insertable = true, updatable = true, nullable = false)
	private Long calendarId;

	@Override
	public String getSequenceKey() {
		// FIXME: try to externalize definition of sequence key
		return SEQUENCE_KEY;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
	}

}
