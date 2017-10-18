package de.mw.mwdata.app.admin.web.control;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.ResultActions;
import org.springframework.test.web.server.ResultMatcher;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.mw.mwdata.core.web.navigation.CrudNavigationManager;
import de.mw.mwdata.core.web.navigation.NavigationManager;
import de.mw.mwdata.core.web.navigation.NavigationState;
import de.mw.mwdata.core.web.util.SessionUtils;

//@RunWith(SpringJUnit4ClassRunner.class)
// "classpath:/appContext-ofdb.xml", "classpath:/appContext-ofdb-test.xml",
// "classpath:/appContext-common-test-db.xml"
@WebAppConfiguration
@ContextConfiguration(locations = {  //
		"classpath:appContext-admin-web.xml",  "classpath:appContext-core-web.xml", 
		"file:src/test/resources/appContext-ofdbMock.xml", "file:src/main/webapp/WEB-INF/admin-servlet.xml" })
public class WebIntegrationTest extends AbstractTestNGSpringContextTests {
	
//	Fehler: scope session is not active for the current thread (scopedTarget.tabDefController)
//	Tip: http://stackoverflow.com/questions/27227233/better-understanding-of-error-scope-session-is-not-active-for-the-current-th
//		1.Idee: tabDefcontroller ist nicht im webcontextd
//		2.Idee: HTTP-Session ist im WebIntegrationTest nicht gestartet worden
	
	@Autowired
	private MockHttpSession session;
	
	@Autowired
	private Filter adminUserFilter;
	
	protected void startSession() {
		this.session = new MockHttpSession();
	}
	
	protected void endSession() {
		this.session.clearAttributes();
		this.session = null;
	}
	
	@Autowired
	private WebApplicationContext	webApplicationContext;
	
	private MockMvc					mockMvc;
	
//	@Autowired
////	@Qualifier(value="scopedTarget.navigationManager")
//	private NavigationManager navigationManager;
//	
	@BeforeClass
	public void setUp() {
		// http://docs.spring.io/spring/docs/current/spring-framework-reference/html/integration-testing.html
		this.mockMvc = MockMvcBuilders.webApplicationContextSetup( webApplicationContext ).addFilter(adminUserFilter, "*.htm").build();
		Assert.assertTrue( true );
		
	}
	
	@Test(enabled = false)
	public void testContextLoaded() throws Exception {

		NavigationState state = SessionUtils.getNavigationState(this.session);
		
//		... problem: session navigationManager wird mehrmals instanziiert
//		Idee: vom WebIntegrationTest nicht per injection, sondern über webAppContext oder über httpsession darauf zugreifen
		
		ResultActions result = this.mockMvc.perform( get( "/admin/tabDef/list.htm" ).param( "menue", "42" ) ).andExpect(
				(ResultMatcher) status().isOk() );

		CrudNavigationManager navManager = (CrudNavigationManager) this.webApplicationContext.getBean("navigationManager");
		
		Assert.assertEquals(state.getPageIndex(), 1);
		Assert.assertEquals(state.getUrlPath(), "tabDef");
		
		
		// .andExpect((ResultMatcher) jsonPath("$.name").value("Lee"));
		
	}
}
