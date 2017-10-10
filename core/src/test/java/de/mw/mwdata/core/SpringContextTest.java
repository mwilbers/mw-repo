package de.mw.mwdata.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;
import de.mw.mwdata.core.ofdb.AbstractOfdbInitializationTest;

@ContextConfiguration(locations = { "classpath:/appContext-ofdb.xml", "classpath:/appContext-ofdb-test.xml",
		"classpath:/appContext-common-test-db.xml" })
@TransactionConfiguration
public class SpringContextTest extends AbstractOfdbInitializationTest {

	private static final Logger	LOGGER	= LoggerFactory.getLogger( SpringContextTest.class );

	@Test(enabled = true)
	public void testLoadContext() {
		LOGGER.debug( "loaded context successful" );

	}

}
