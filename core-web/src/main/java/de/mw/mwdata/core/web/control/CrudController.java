package de.mw.mwdata.core.web.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.ofdb.GenericController;

public interface CrudController<E extends AbstractMWEntity> extends GenericController<E> {

	public ModelAndView filter( final HttpServletRequest request, final HttpServletResponse response,
			@ModelAttribute("filterSet") final EntityTO<E> filterSet, final BindingResult result );

	public ModelAndView list( final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "pi", required = false) final Integer pi,
			@RequestParam(value = "menue", required = false) final String menue);

	public ModelAndView sort( final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "col", required = false) final String col,
			@RequestParam(value = "asc", required = false) final String asc );

	public ModelAndView edit( final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "id", required = false) final Long id,
			@RequestParam(value = "menue", required = false) final String menue,
			@ModelAttribute("filterSet") final EntityTO<E> filterSet );

}
