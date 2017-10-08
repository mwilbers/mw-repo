package de.mw.common.testframework;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;


@ContextConfiguration(locations = { "classpath:/appContext-common-test-db.xml" })
public class CommonSpringContextTest extends AbstractTestNGSpringContextTests {

	
	private static final Logger	LOGGER	= LoggerFactory.getLogger(CommonSpringContextTest.class);

//	@Autowired
//	private SimpleJdbcTemplate simpleJdbcTemplate;
//	
	private JdbcTemplate jdbcTemplate;
	
	
//	@Test(enabled = true)	// NOTE: spring context can not be loaded in common-testframework
	public void testLoadContext() {
		LOGGER.debug( "loaded context successful" );
//		Assert.assertNotNull(this.simpleJdbcTemplate);
//		...
//		1. diese klasse muss als TEsts den context hochfahren
//		2. dann ofdbinterceptor so verdrahten, dass nach abschluss des context ladens die chain beladen wird
		
		
	}

	

}
