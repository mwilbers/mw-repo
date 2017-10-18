/**
 * 
 */
package de.mw.common.utils;

import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author MAW
 * 
 */
public class FxDateUtilsTest {

	// private Date date;
	//
	// @Before
	// public void setUp() {
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(2010, 0, 31,0,0,0); // month 0-based
	// this.date = calendar.getTime();
	//
	// }

	// @After
	// public void tearDown() {
	//
	// }

	@Test
	public void testDate() {

		Date date = FxDateUtils.date( 2010, 6, 1 );
		Assert.assertEquals( 2010, FxDateUtils.getYear( date ) );
		Assert.assertEquals( 6, FxDateUtils.getMonth( date ) );
		Assert.assertEquals( 1, FxDateUtils.getDayOfMonth( date ) );

	}

	@Test
	public void testDateWithMonthJanuary() {

		Date date = FxDateUtils.date( 2010, 1, 1 );
		Assert.assertEquals( 2010, FxDateUtils.getYear( date ) );
		Assert.assertEquals( 1, FxDateUtils.getMonth( date ) );
		Assert.assertEquals( 1, FxDateUtils.getDayOfMonth( date ) );

	}

	@Test
	public void testDateWithMonthDecember() {

		Date date = FxDateUtils.date( 2010, 12, 1 );
		Assert.assertEquals( 2010, FxDateUtils.getYear( date ) );
		Assert.assertEquals( 12, FxDateUtils.getMonth( date ) );
		Assert.assertEquals( 1, FxDateUtils.getDayOfMonth( date ) );

	}

	@Test
	public void testIllegalMonth() {

		try {
			FxDateUtils.date( 2010, 0, 1 ); // month 0
			Assert.fail();
		} catch ( Exception e ) {
			// exception ok here
		}

		// Assert.assertEquals(2010, FxDateUtils.getYear(date));
		// Assert.assertEquals(1, FxDateUtils.getMonth(date));
		// Assert.assertEquals(1, FxDateUtils.getDayOfMonth(date));

	}

}
