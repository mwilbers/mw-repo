package de.mw.mwdata.app.admin.web.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.Capture;
import org.easymock.EasyMock;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.mocks.DomainMockFactory;
import de.mw.mwdata.core.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.core.ofdb.cache.ViewConfigFactory;
import de.mw.mwdata.core.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.core.ofdb.cache.ViewConfigValidationResultSet;
import de.mw.mwdata.core.ofdb.cache.ViewConfiguration;
import de.mw.mwdata.core.ofdb.domain.AnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig.DBTYPE;
import de.mw.mwdata.core.ofdb.domain.TabDef;
import de.mw.mwdata.core.ofdb.service.IPagingOfdbService;
import de.mw.mwdata.core.ofdb.validate.OfdbValidatable;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.web.navigation.NavigationManager;

public class MockFactory {
	
	private static ViewConfigHandle	viewHandle;
	
	static {
		
		viewHandle = createViewHandleMock();
		
	}
	
	public IPagingOfdbService createOfdbServiceMock() {
		
		IPagingOfdbService ofdbServiceMock = EasyMock.createNiceMock( IPagingOfdbService.class );
		EasyMock.expect( ofdbServiceMock.findAnsichtByUrlPath( EasyMock.eq( "tabDef" ) ) )
				.andReturn( viewHandle.getViewDef() ).anyTimes();
		// viewHandle.getMainAnsichtTab().getTabDef();
		List<IEntity[]> entities = new ArrayList<IEntity[]>();
		IEntity[] e = new IEntity[1];
		e[0] = viewHandle.getMainAnsichtTab().getTabDef();
		entities.add( e );
		PaginatedList<IEntity[]> list = new PaginatedList<IEntity[]>( entities );
		EasyMock.expect(
				ofdbServiceMock.loadViewPaginated( EasyMock.eq( "FX_TabDef_K" ), EasyMock.eq( 1 ),
						EasyMock.isA( List.class ) ) ).andReturn( list ).anyTimes();
		
		EasyMock.replay( ofdbServiceMock );
		
		return ofdbServiceMock;
		
	}
	
	public OfdbValidatable createOfdbValidatorMock() {
		
		OfdbValidatable ofdbValidatorMock = EasyMock.createNiceMock( OfdbValidatable.class );
		
		ViewConfigValidationResultSet resultSet = new ViewConfigValidationResultSet();
		EasyMock.expect( ofdbValidatorMock.isTableValid( EasyMock.isA( ITabDef.class ) ) ).andReturn( resultSet )
				.anyTimes();
		EasyMock.expect( ofdbValidatorMock.isAnsichtValid( EasyMock.isA( IAnsichtDef.class ) ) ).andReturn( resultSet )
				.anyTimes();
		
		EasyMock.replay( ofdbValidatorMock );
		
		return ofdbValidatorMock;
		
	}
	
	public ViewConfigFactory createViewConfigFactory() {
		
		ViewConfigFactory viewConfigFactoryMock = EasyMock.createNiceMock( ViewConfigFactory.class );
		
		EasyMock.replay( viewConfigFactoryMock );
		
		return viewConfigFactoryMock;
		
	}
	
	private static ViewConfigHandle createViewHandleMock() {
		
		BenutzerBereich bereich = new BenutzerBereich();
		ITabDef tabDef = DomainMockFactory.createTabDefMock( TestConstants.TABLENAME_TABDEF, bereich, true );
		tabDef.setFullClassName( TabDef.class.getName() );
		ITabSpeig tableProp = DomainMockFactory.createTabSpeigMock( (TabDef) tabDef, "tabDefId", 0, DBTYPE.LONGINTEGER );
		IAnsichtDef viewDef = DomainMockFactory.createAnsichtDefMock( TestConstants.TABLENAME_TABDEF, null, true );
		viewDef.setUrlPath( "tabDef" );
		IAnsichtTab viewTable = DomainMockFactory.createAnsichtTabMock( (AnsichtDef) viewDef, (TabDef) tabDef, 0, "x",
				null, null, null, true );
		IAnsichtSpalte viewProp = DomainMockFactory
				.createAnsichtSpalteMock( (AnsichtDef) viewDef, tableProp, viewTable );
		
		ViewConfiguration.Builder builder = new ViewConfiguration.Builder( viewDef );
		builder.addTableDef( tabDef );
		List<ITabSpeig> tableProps = new ArrayList<ITabSpeig>();
		tableProps.add( tableProp );
		builder.addTableProps( tabDef, tableProps );
		builder.addViewTab( viewTable );
		Map<String, IAnsichtSpalte> viewColumns = new HashMap<String, IAnsichtSpalte>();
		viewColumns.put( viewProp.getSpalteAKey().toUpperCase(), viewProp );
		builder.setViewColumns( viewColumns );
		return builder.buildHandle();
	}
	
	public OfdbCacheManager createOfdbCacheManager() {
		
		List<ViewConfigHandle> viewConfigs = new ArrayList<ViewConfigHandle>();
		// ViewConfigHandle viewHandleMock = viewHandle;
		viewConfigs.add( viewHandle );
		
		OfdbCacheManager ofdbCacheManagerMock = EasyMock.createNiceMock( OfdbCacheManager.class );
		EasyMock.expect( ofdbCacheManagerMock.getRegisteredViewConfigs() ).andReturn( viewConfigs ).anyTimes();
		EasyMock.expect( ofdbCacheManagerMock.getViewConfig( "FX_TabDef_K" ) ).andReturn( viewHandle ).anyTimes();
		
		EasyMock.replay( ofdbCacheManagerMock );
		
		return ofdbCacheManagerMock;
		
	}
	
	@SuppressWarnings("unchecked")
	public ICrudService<AbstractMWEntity> createCrudServiceMock() {
		
		ICrudService<AbstractMWEntity> crudServiceMock = EasyMock.createNiceMock( ICrudService.class );
		
		List<AbstractMWEntity> tabDefs = new ArrayList<AbstractMWEntity>();
		
		Capture<Class<AbstractMWEntity>> classCapture = new Capture<Class<AbstractMWEntity>>();
		EasyMock.expect(
				crudServiceMock.findAll( EasyMock.capture( classCapture ), EasyMock.<Map<String, String>> anyObject() ) )
				.andReturn( tabDefs );
		
		EasyMock.replay( crudServiceMock );
		
		return crudServiceMock;
		
	}
	
	public NavigationManager createNavigationManagerMock() {
		
		NavigationManager navigationManagerMock = EasyMock.createNiceMock( NavigationManager.class );
		
		// List<AbstractMWEntity> tabDefs = new ArrayList<AbstractMWEntity>();
		
		// Capture<Class<AbstractMWEntity>> classCapture = new
		// Capture<Class<AbstractMWEntity>>();
		// EasyMock.expect( crudServiceMock.findAll( EasyMock.capture(
		// classCapture ), EasyMock.<Map<String, String>> anyObject() ) )
		// .andReturn( tabDefs );
		
		EasyMock.replay( navigationManagerMock );
		
		return navigationManagerMock;
		
	}
	
}
