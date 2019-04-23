/**
 *
 */
package de.mw.mwdata.ofdb.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import de.mw.mwdata.core.daos.ICrudDao;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryBuilder;
import de.mw.mwdata.core.query.SimpleQueryBuilder;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.impl.AnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;
import de.mw.mwdata.ofdb.exception.OfdbMissingObjectException;
import de.mw.mwdata.ofdb.impl.ConfigOfdb;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;

/**
 * Ofdb-Dao-Layer that encapsulates all ofdb-relevant Daos and DB-operations.
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
public class OfdbDao extends HibernateDaoSupport implements IOfdbDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfdbDao.class);

	// FIXME: remove all ofdbDaos here, use then direclty in ofdbService per
	// injection. use ofdbDao only as
	// hibernate-persister

	@Autowired
	private ICrudDao crudDao;

	@Autowired
	private OfdbMapper ofdbMapper;

	@Override
	public OfdbEntityMapping initializeMapping(final Class<? extends AbstractMWEntity> type, final String tableName) {
		LOGGER.debug(
				"Loading Cache for Dao : " + tableName + " ....................................." + type.toString());
		return this.ofdbMapper.init(type, tableName);
	}

	/**
	 * Method returns the field-value of the entity from the given TabSpeig
	 *
	 * @return the value as object, returns null, if there is no value,
	 *
	 */
	@Override
	public Object getEntityValue(final AbstractMWEntity entity, final int propPersistenceIndex) {
		return this.ofdbMapper.getPropertyValue(entity, propPersistenceIndex);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ITabSpeig> findTabSpeigByTable(final String table) {

		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b.selectEntity(ConfigOfdb.T_TABPROPS, "tSpeig").fromEntity(ConfigOfdb.T_TABPROPS, "tSpeig")
				.joinTable(ConfigOfdb.T_TABDEF, "tDef").whereJoin("tSpeig", "tabDefId", "tDef", "id")
				.andWhereRestriction("tDef", "name", OperatorEnum.Eq, table, ValueType.STRING)
				.orderBy("tSpeig", "reihenfolge", "ASC").buildSQL();
		List<IEntity[]> tabProps = this.crudDao.executeSql(sql);

		List<ITabSpeig> props = new ArrayList<ITabSpeig>();
		for (int i = 0; i < tabProps.size(); i++) {

			IEntity[] entityArray = tabProps.get(i);
			props.add((TabSpeig) entityArray[0]);
		}

		return props;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IAnsichtSpalte> findAnsichtSpaltenByAnsicht(final long ansichtId) {

		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b.selectEntity(ConfigOfdb.T_VIEWPROPS, "aSpalten").fromEntity(ConfigOfdb.T_VIEWPROPS, "aSpalten")
				.andWhereRestriction("aSpalten", "ansichtDefId", OperatorEnum.Eq, new Long(ansichtId).toString(),
						ValueType.NUMBER)
				.orderBy("aSpalten", "indexGrid", "asc").buildSQL();
		List<IEntity[]> results = this.crudDao.executeSql(sql);

		List<IAnsichtSpalte> list = new ArrayList<IAnsichtSpalte>(results.size());
		for (int i = 0; i < results.size(); i++) {
			IEntity[] entityArray = results.get(i);
			IAnsichtSpalte aSpalte = (IAnsichtSpalte) entityArray[0];
			list.add(aSpalte);

		}
		return list;

	}

	// @Override
	@Override
	public TabDef findTableDefByFullClassName(final String fullClassName) {

		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b.selectEntity(ConfigOfdb.T_TABDEF, "t").fromEntity(ConfigOfdb.T_TABDEF, "t")
				.andWhereRestriction("t", "fullClassName", OperatorEnum.Eq, fullClassName, ValueType.STRING).buildSQL();
		List<IEntity[]> result = this.crudDao.executeSql(sql);
		if (null == result) {
			String message = MessageFormat.format("TabDef by fullClassName {0} is not found. ", fullClassName);
			throw new OfdbMissingObjectException(message);
		}
		
		... Eintrag BenutzerAnsicht noch nicht in FX_TabDef

		IEntity[] entityArray = result.get(0);
		return (TabDef) entityArray[0];
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId(final long ansichtId) {

		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b.selectEntity(ConfigOfdb.T_VIEWORDERBY, "aOrder").fromEntity(ConfigOfdb.T_VIEWORDERBY, "aOrder")
				.joinTable(ConfigOfdb.T_VIEWTAB, "aTab").whereJoin("aOrder", "ansichtTabId", "aTab", "id")
				.andWhereRestriction("aTab", "ansichtDefId", OperatorEnum.Eq, new Long(ansichtId).toString(),
						ValueType.NUMBER)
				.orderBy("aOrder", "reihenfolge", "asc").buildSQL();
		List<IEntity[]> results = this.crudDao.executeSql(sql);

		List<AnsichtOrderBy> orders = new ArrayList<AnsichtOrderBy>();

		for (int i = 0; i < results.size(); i++) {
			IEntity[] entityArray = results.get(i);
			orders.add((AnsichtOrderBy) entityArray[0]);
		}

		return orders;
	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IAnsichtTab> findAnsichtTabAnsichtId(final long ansichtId) {

		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b
				.selectEntity("AnsichtTab", "aTab").fromEntity("AnsichtTab", "aTab").andWhereRestriction("aTab",
						"ansichtDefId", OperatorEnum.Eq, new Long(ansichtId).toString(), ValueType.NUMBER)
				.orderBy("aTab", "reihenfolge", "asc").buildSQL();
		List<IEntity[]> results = this.crudDao.executeSql(sql);

		List<IAnsichtTab> aTabs = new ArrayList<IAnsichtTab>();
		for (int i = 0; i < results.size(); i++) {
			IEntity[] entityArray = results.get(i);
			aTabs.add((AnsichtTab) entityArray[0]);
		}

		return aTabs;

	}

	@Override
	public Object setEntityValue(final AbstractMWEntity entity, final Object value, final ITabSpeig tabSpeig,
			final OfdbPropMapper propMapper) {

		Object result = null;
		// if enum, than switch from string to enum-value
		Class<? extends AbstractMWEntity> entityClassType = null;
		@SuppressWarnings("rawtypes")
		String fullClassName = tabSpeig.getTabDef().getFullClassName();

		try {
			entityClassType = ClassNameUtils.getClassType(fullClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (tabSpeig.getDbDatentyp() == DBTYPE.ENUM) {
			Class<Enum> c = this.ofdbMapper.getEnumType(propMapper.getPropertyName(), entityClassType);
			result = Enum.valueOf(c, value.toString()); // FIXME: toString replaced by getDescription() ?
		} else {
			result = value;
		}

		this.ofdbMapper.setPropertyValue(entity, propMapper.getPropertyIndex(), result, entityClassType);
		return result;

	}

	// @Override
	@Override
	public List<AnsichtDef> loadViewsForRegistration(final String nameBenutzerBereich) {

		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b.selectEntity(ConfigOfdb.T_VIEWDEF, "viewDef").fromEntity(ConfigOfdb.T_VIEWDEF, "viewDef")
				.joinEntity("bereich", "bBereich")
				.andWhereRestriction("bBereich", "name", OperatorEnum.Eq, nameBenutzerBereich, ValueType.STRING)
				.andWhereRestriction("viewDef", "urlPath", OperatorEnum.IsNotNull, "null", ValueType.STRING)
				.orderBy("viewDef", "reihenfolge", "asc").buildSQL();

		List<IEntity[]> results = this.crudDao.executeSql(sql);

		List<AnsichtDef> views = new ArrayList<AnsichtDef>();
		if (CollectionUtils.isEmpty(results)) {
			return views;
		}

		for (int i = 0; i < results.size(); i++) {
			IEntity[] entityArray = results.get(i);
			views.add((AnsichtDef) entityArray[0]);
		}

		return views;
	}

	@Override
	public List<Object> getEnumValues(final Class<? extends AbstractMWEntity> entityClassType,
			final String propertyName) {
		return this.ofdbMapper.getEnumValues(entityClassType, propertyName);
	}
}
