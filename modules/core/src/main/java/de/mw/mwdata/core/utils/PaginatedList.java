/**
 *
 */
package de.mw.mwdata.core.utils;

import java.util.List;

import de.mw.mwdata.core.daos.PagingModel;

/**
 * List with additional Paging-informations vor viewing a big list of
 * objects.<br>
 * Example:<br>
 *
 * <pre>
 * 1 &lt; 7 8 <i>9</i> 10 11 &gt; 365
 * </pre>
 *
 * in custom steps. Variables in example: numSteps = 5, count = 365,
 * indexCurrentStep = 9<br>
 * FIXME: class still needed for UI ? paging replaced by angularjs-component
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Nov, 2010
 *
 */
public class PaginatedList<T> {

	/**
	 * the list dependant of the step-size
	 */
	private List<T> items;

	/**
	 * the number of all items in database
	 */
	private long count;

	/**
	 * number of pages for all datasets in database
	 */
	private long numPages;

	// /**
	// * the size of one step
	// */
	// private static int stepsize;

	/**
	 * the number of the current shown steps, not all steps
	 */
	private int numSteps;

	/**
	 * the index of the current step in the whole itemlist of the database. 1-based.
	 */
	private long indexCurrentStep;

	/**
	 * the number of items per page
	 */
	private int pageSize;

	public static final int DEFAULT_STEPSIZE = 30;
	public static final int DEFAULT_NUMSTEPS = 10;

	public PaginatedList(final List<T> items) {
		init(items, items.size());
		setIndexCurrentStep(1);
	}

	// public PaginatedList(final List<T> items, final int indexCurrentStep) {
	// init(items, items.size());
	// setIndexCurrentStep(indexCurrentStep);
	// }

	public PaginatedList(final List<T> items, final long count, final PagingModel pagingModel) {
		init(items, count);
		setIndexCurrentStep(pagingModel.getPageIndex());
		this.pageSize = pagingModel.getPageSize();
	}

	private void init(final List<T> items, final long count) {

		this.items = items;
		this.count = count;
		this.numPages = (count / DEFAULT_STEPSIZE);
		long rest = (count % DEFAULT_STEPSIZE);
		if (rest != 0 /* && isPaging() */) {
			this.numPages += 1;
		}

		if (count > DEFAULT_NUMSTEPS * DEFAULT_STEPSIZE) {
			this.numSteps = DEFAULT_NUMSTEPS;
		} else if (count < DEFAULT_STEPSIZE) {
			this.numSteps = 0;
		} else {
			this.numSteps = (int) (count / DEFAULT_STEPSIZE);

			if (rest != 0) {
				this.numSteps += 1;
			}
			this.numSteps = this.numSteps - 2;// link 1 and link 'count' are always shown
		}

	}

	public void setIndexCurrentStep(final int indexCurrentStep) {
		if (indexCurrentStep > this.numPages) {
			this.indexCurrentStep = this.numPages;
		} else {
			this.indexCurrentStep = indexCurrentStep;
		}
	}

	public long getIndexCurrentStep() {
		return this.indexCurrentStep;
	}

	public List<T> getItems() {
		return this.items;
	}

	public long getCount() {
		return this.count;
	}

	public long getNumPages() {
		return this.numPages;
	}

	// public void setStepsize(int stepsize) {
	// this.stepsize = stepsize;
	// }
	//
	// public int getStepsize() {
	// return stepsize;
	// }

	public long getStepsSmaller() {
		if (this.indexCurrentStep <= 2) {
			return 0; // step 1 is always shown
		}

		long steps = 0;
		if (this.numSteps < this.DEFAULT_NUMSTEPS) {
			steps = this.indexCurrentStep - 1;
		} else {
			steps = this.numSteps / 2;
		}

		if (this.indexCurrentStep - steps <= 1) {
			steps = this.indexCurrentStep - 2; // -1: step 1 always shown
		}
		return steps;
	}

	public long getStepsGreater() {

		if (this.indexCurrentStep >= this.numPages - 1) {
			return 0;
		}

		long steps = 0;
		if (this.numSteps < this.DEFAULT_NUMSTEPS) {
			steps = (this.numPages - this.indexCurrentStep - 1);
		} else {
			steps = this.numSteps / 2;
		}

		if (this.indexCurrentStep + steps >= this.numPages) {
			steps = this.numPages - this.indexCurrentStep - 1; // -1: step 1 always shown
		}
		return steps;
	}

	public boolean isArrowSmaller() {
		long steps = this.numSteps / 2;
		if (this.indexCurrentStep - steps > 2 && this.numPages > DEFAULT_NUMSTEPS) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isArrowGreater() {
		long steps = this.numSteps / 2;
		if (this.indexCurrentStep + steps < this.numPages - 1 && this.numPages > DEFAULT_NUMSTEPS) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return true, if not all datasets can be shown on one page, else false
	 */
	public boolean isPaging() {
		return (this.count > DEFAULT_STEPSIZE);
	}

	/**
	 * Property special for JSTL-access
	 *
	 * @return
	 */
	public long getDefaultStepSize() {
		return DEFAULT_STEPSIZE;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (!isPaging()) {
			return buf.toString();
		}

		buf.append("1 ");
		if (isArrowSmaller()) {
			buf.append("< ");
		}
		long steps = getStepsSmaller();
		if (steps > 0) {
			for (long i = steps; i > 0; i--) {
				buf.append(this.indexCurrentStep - i + " ");
			}
		}

		if (this.indexCurrentStep > 1 && this.indexCurrentStep < this.numPages) {
			buf.append(this.indexCurrentStep + " ");
		}

		steps = getStepsGreater();
		if (steps > 0) {
			for (long i = this.indexCurrentStep + 1; i <= this.indexCurrentStep + steps; i++) {
				buf.append(i + " ");
			}
		}
		if (isArrowGreater()) {
			buf.append("> ");
		}

		if (this.numPages > 1) {
			buf.append(this.numPages + " ");
		}

		return buf.toString();
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public int getNumSteps() {
		return this.numSteps;
	}

}
