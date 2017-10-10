/**
 *
 */
package de.mw.mwdata.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test-class for the fx-PaginatedList
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Nov, 2010
 *
 */
public class PaginatedListTest {

	protected static Logger	log	= LoggerFactory.getLogger( PaginatedListTest.class );

	private List<String>	items;

	private void initItems( final int nItems ) {
		this.items = new ArrayList<String>();
		for ( int i = 0; i < nItems; i++ ) {
			this.items.add( new Integer( i ).toString() );
		}
	}

	@Test
	public void testInitEmptyList() {
		initItems( 0 ); // Paging: no paging

		PaginatedList<String> list = new PaginatedList<String>( this.items );

		log.debug( "Paging testInitEmptyList: " + list.toString() );
		Assert.assertEquals( this.items.size(), list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertFalse( list.isPaging(), "Paging not deactivated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertFalse( list.isArrowGreater(), "ArrowGreater not deactivated" );

	}

	@Test
	public void testInitListWithOneItem() {
		initItems( 1 ); // Paging: no paging

		PaginatedList<String> list = new PaginatedList<String>( this.items );

		log.debug( "Paging testInitListWithOneItem: " + list.toString() );
		Assert.assertEquals( this.items.size(), list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertFalse( list.isPaging(), "Paging not deactivated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertFalse( list.isArrowGreater(), "ArrowGreater not deactivated" );

		Assert.assertEquals( 0, list.getStepsGreater(), "Wrong number of greater steps" );
		Assert.assertEquals( 0, list.getStepsSmaller(), "Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith31Item() {
		initItems( 31 ); // Paging: 1 2

		PaginatedList<String> list = new PaginatedList<String>( this.items );

		log.debug( "Paging testInitListWith31Item: " + list.toString() );
		Assert.assertEquals( this.items.size(), list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertFalse( list.isArrowGreater(), "ArrowGreater not deactivated" );

		Assert.assertEquals( 0, list.getStepsGreater(), "Wrong number of greater steps" );
		Assert.assertEquals( 0, list.getStepsSmaller(), "Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith31ItemAndCount1000() {
		initItems( 30 ); // Paging: 1 2 3 4 5 6 > 34

		PaginatedList<String> list = new PaginatedList<String>( this.items, 1000, 1 );

		log.debug( "Paging testInitListWith31ItemAndCount1000: " + list.toString() );
		Assert.assertEquals( 1000, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertTrue( list.isArrowGreater(), "ArrowGreater not activated" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsGreater(),
				"Wrong number of greater steps" );
		Assert.assertEquals( 0, list.getStepsSmaller(), "Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith31ItemAndCount1000AndIndex10() {
		initItems( 30 ); // Paging: 1 < 5 6 7 8 9 10 11 12 13 14 15 > 34

		PaginatedList<String> list = new PaginatedList<String>( this.items, 1000, 10 );

		log.debug( "Paging testInitListWith31ItemAndCount1000AndIndex10: " + list.toString() );
		Assert.assertEquals( 1000, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertTrue( list.isArrowSmaller(), "ArrowSmaller not activated" );
		Assert.assertTrue( list.isArrowGreater(), "ArrowGreater not activated" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsGreater(),
				"Wrong number of greater steps" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsSmaller(),
				"Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith31ItemAndCount1000AndIndex32() {
		initItems( 30 ); // Paging: 1 < 27 28 29 30 31 32 33 34

		PaginatedList<String> list = new PaginatedList<String>( this.items, 1000, 32 );

		log.debug( "Paging testInitListWith31ItemAndCount1000AndIndex32: " + list.toString() );
		Assert.assertEquals( 1000, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertTrue( list.isArrowSmaller(), "ArrowSmaller not activated" );
		Assert.assertFalse( list.isArrowGreater(), "ArrowGreater not deactivated" );
		Assert.assertEquals( 1, list.getStepsGreater(), "Wrong number of greater steps" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsSmaller(),
				"Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith30ItemAndCount8277AndIndex2() {
		initItems( 30 ); // Paging: 1 2 3 4 5 6 7 > 276

		PaginatedList<String> list = new PaginatedList<String>( this.items, 8277, 2 );

		log.debug( "Paging testInitListWith30ItemAndCount8277AndIndex2: " + list.toString() );
		Assert.assertEquals( 8277, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertTrue( list.isArrowGreater(), "ArrowGreater not activated" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsGreater(),
				"Wrong number of greater steps" );
		Assert.assertEquals( 0, list.getStepsSmaller(), "Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith30ItemAndCount8277AndIndex4() {
		initItems( 30 ); // Paging: 1 2 3 4 5 6 7 8 9 > 276

		PaginatedList<String> list = new PaginatedList<String>( this.items, 8277, 4 );

		log.debug( "Paging testInitListWith30ItemAndCount8277AndIndex4: " + list.toString() );
		Assert.assertEquals( 8277, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertTrue( list.isArrowGreater(), "ArrowGreater not activated" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsGreater(),
				"Wrong number of greater steps" );
		Assert.assertEquals( 2, list.getStepsSmaller(), "Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith30ItemAndCount8277AndIndex7() {
		initItems( 30 ); // Paging: 1 2 3 4 5 6 7 8 9 10 11 12 > 276

		PaginatedList<String> list = new PaginatedList<String>( this.items, 8277, 7 );

		log.debug( "Paging testInitListWith30ItemAndCount8277AndIndex7: " + list.toString() );
		Assert.assertEquals( 8277, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertTrue( list.isArrowGreater(), "ArrowGreater not activated" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsGreater(),
				"Wrong number of greater steps" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsSmaller(),
				"Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith30ItemAndCount8277AndIndex271() {
		initItems( 30 ); // Paging: 1 < 266 267 268 269 270 271 272 273 274 275 276

		PaginatedList<String> list = new PaginatedList<String>( this.items, 8277, 271 );

		log.debug( "Paging testInitListWith30ItemAndCount8277AndIndex271: " + list.toString() );
		Assert.assertEquals( 8277, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertTrue( list.isArrowSmaller(), "ArrowSmaller not activated" );
		Assert.assertFalse( list.isArrowGreater(), "ArrowGreater not deactivated" );
		Assert.assertEquals( 4, list.getStepsGreater(), "Wrong number of greater steps" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsSmaller(),
				"Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith30ItemAndCount8277AndIndex270() {
		initItems( 30 ); // Paging: 1 < 265 266 267 268 269 270 271 272 273 274 275 276

		PaginatedList<String> list = new PaginatedList<String>( this.items, 8277, 270 );

		log.debug( "Paging testInitListWith30ItemAndCount8277AndIndex270: " + list.toString() );
		Assert.assertEquals( 8277, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertTrue( list.isArrowSmaller(), "ArrowSmaller not activated" );
		Assert.assertFalse( list.isArrowGreater(), "ArrowGreater not deactivated" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsGreater(),
				"Wrong number of greater steps" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsSmaller(),
				"Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith30ItemAndCount8277AndIndex6() {
		initItems( 30 ); // Paging: 1 2 3 4 5 6 7 8 9 10 11 > 276

		PaginatedList<String> list = new PaginatedList<String>( this.items, 8277, 6 );

		log.debug( "Paging testInitListWith30ItemAndCount8277AndIndex6: " + list.toString() );
		Assert.assertEquals( 8277, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertTrue( list.isArrowGreater(), "ArrowGreater not activated" );
		Assert.assertEquals( PaginatedList.DEFAULT_NUMSTEPS / 2, list.getStepsGreater(),
				"Wrong number of greater steps" );
		Assert.assertEquals( 4, list.getStepsSmaller(), "Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith30ItemAndCount127AndIndex1() {
		initItems( 30 ); // Paging: 1 2 3 4 5 6 7 8 9 10 11 > 276

		PaginatedList<String> list = new PaginatedList<String>( this.items, 127, 1 );

		log.debug( "Paging testInitListWith30ItemAndCount127AndIndex1: " + list.toString() );
		Assert.assertEquals( 127, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertFalse( list.isArrowGreater(), "ArrowGreater not activated" );
		Assert.assertEquals( list.getStepsGreater(), 3, "Wrong number of greater steps" );
		Assert.assertEquals( 0, list.getStepsSmaller(), "Wrong number of smaller steps" );

	}

	@Test
	public void testInitListWith30ItemAndCount127AndIndex4() {
		initItems( 30 ); // Paging: 1 2 3 4 5 6 7 8 9 10 11 > 276

		PaginatedList<String> list = new PaginatedList<String>( this.items, 127, 4 );

		log.debug( "Paging testInitListWith30ItemAndCount127AndIndex4: " + list.toString() );
		Assert.assertEquals( 127, list.getCount(), "Wrong number of paginated list-items" );
		Assert.assertTrue( list.isPaging(), "Paging not activated" );
		Assert.assertFalse( list.isArrowSmaller(), "ArrowSmaller not deactivated" );
		Assert.assertFalse( list.isArrowGreater(), "ArrowGreater not activated" );
		Assert.assertEquals( list.getStepsGreater(), 0, "Wrong number of greater steps" );
		Assert.assertEquals( list.getStepsSmaller(), 2, "Wrong number of smaller steps" );

	}

	// TODO: activate test and find bug here !

	// @Test
	// public void testInitListWith30ItemAndCount137AndIndex1() {
	// initItems( 30 ); // Paging expected: 1 2 3 4 5, actual: 1 2 5
	//
	// PaginatedList list = new PaginatedList( this.items, 137, 1 );
	//
	// log.debug( "Paging testInitListWith30ItemAndCount137AndIndex1: " + list.toString() );
	// Assert.assertEquals( "Wrong number of paginated list-items", 137, list.getCount() );
	// Assert.assertTrue( "Paging not activated", list.isPaging() );
	// Assert.assertFalse( "ArrowSmaller not deactivated", list.isArrowSmaller() );
	// Assert.assertFalse( "ArrowGreater activated", list.isArrowGreater() );
	// Assert.assertEquals( "Wrong number of greater steps", PaginatedList.DEFAULT_NUMSTEPS / 2,
	// list.getStepsGreater() );
	// Assert.assertEquals( "Wrong number of smaller steps", 4, list.getStepsSmaller() );
	//
	// }

}
