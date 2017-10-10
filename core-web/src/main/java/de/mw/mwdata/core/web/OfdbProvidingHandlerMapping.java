package de.mw.mwdata.core.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.ofdb.domain.AnsichtDef;
import de.mw.mwdata.core.ofdb.service.IOfdbService;

public class OfdbProvidingHandlerMapping<T extends AbstractMWEntity> extends RequestMappingHandlerMapping {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfdbProvidingHandlerMapping.class);

	private String handler;

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	@Autowired
	private IOfdbService ofdbService;

	@Override
	protected void detectHandlerMethods(final Object handler) {

		this.handler = (String) handler;
		super.detectHandlerMethods(handler);
	}

	// @Override
	// protected boolean isHandler( final Class<?> beanType ) {
	// // TODO Auto-generated method stub
	// return super.isHandler( beanType );
	// }

	@Override
	protected RequestMappingInfo getMappingForMethod(final Method method, final Class<?> handlerType) {

		RequestMappingInfo info = null;
		RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		if (methodAnnotation != null) {
			RequestCondition<?> methodCondition = getCustomMethodCondition(method);
			info = createOfdbRequestMappingInfo(methodAnnotation, methodCondition);
			RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			if (typeAnnotation != null) {
				RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
				info = createOfdbRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
			}
		}
		return info;

	}

	private RequestMappingInfo createOfdbRequestMappingInfo(final RequestMapping annotation,
			final RequestCondition<?> customCondition) {

		String[] methodPatterns = resolveEmbeddedValuesInPatterns(annotation.value());

		// ... here do following:
		// 1. remove all generic controllers and and generic controller
		// configuration to applicationcontext
		// 2. inject applicationFActory to ofdbProvidingHandlerMapping for
		// loading all entites mapped to classbased requestmapping /admin
		// 3. load these entites here
		// 4. build method patterns
		// 5. go on, fixing, and cleanup for just one GenericEntityController
		List<AnsichtDef> views = this.ofdbService.loadViewsForRegistration("admin"); // FIXME:
																						// admin
																						// externalisieren
																						// !
		String[] resultPaths = new String[methodPatterns.length * views.size()];

		// Object bean = this.getWebApplicationContext().getBean( this.handler
		// );
		// if ( bean instanceof GenericEntityController ) {

		// GenericEntityController<T> genericController =
		// (GenericEntityController<T>) bean;
		// AnnotationUtils.findAnnotation( bean.getClass(), RequestMapping.class
		// )

		String[] classUrlPathes = getClassBasedRequestMapping(this.handler);

		// for all method-mappings (e.g. list, save, edit)
		for (int i = 0; i < methodPatterns.length; i++) {

			String pattern = methodPatterns[i];

			// e.g.: "**/list.htm"
			if (!pattern.startsWith("**")) {
				continue;
			}

			// for all pathes in class-based requestmapping
			for (int j = 0; j < classUrlPathes.length; j++) {

				int k = 0;
				for (AnsichtDef view : views) {

					String newPattern = buildMethodPattern(pattern, classUrlPathes[j], view.getUrlPath());
					if (j > 0) {
						LOGGER.warn("Overriding method-mapping for " + methodPatterns[i] + " with class-based pattern "
								+ newPattern);
					}
					resultPaths[j * views.size() + k] = newPattern;
					k++;
				}

			}

		}

		return new RequestMappingInfo(
				new PatternsRequestCondition(resultPaths, getUrlPathHelper(), getPathMatcher(), true, true,
						new ArrayList<String>()),
				new RequestMethodsRequestCondition(annotation.method()),
				new ParamsRequestCondition(annotation.params()), new HeadersRequestCondition(annotation.headers()),
				new ConsumesRequestCondition(annotation.consumes(), annotation.headers()), new ProducesRequestCondition(
						annotation.produces(), annotation.headers(), getContentNegotiationManager()),
				customCondition);

	}

	private String[] getClassBasedRequestMapping(final String handlerClassName) {
		String[] classUrlPathes = null;
		// if ( this.handler.contains( "tabDefController" ) ) {

		// FIXME: extract method getClassMapping
		ApplicationContext context = this.getApplicationContext();
		// Class<?> handlerType = context.getType( "tabDefController" );
		RequestMapping classMapping = context.findAnnotationOnBean(handlerClassName, RequestMapping.class);
		classUrlPathes = classMapping.value();
		return classUrlPathes;
	}

	private String buildMethodPattern(final String methodPattern, final String servletPath,
			final String contextSubPath) {

		// ... add /admin before result
		// .. write new url builder using MwUrl.java or UrlManager.java

		String cleanedMethodPattern = cleanPath(methodPattern);
		String cleanedServletPath = cleanPath(servletPath);
		String cleanedSubPath = cleanPath(contextSubPath);

		return cleanedServletPath + "/" + cleanedSubPath + "/" + cleanedMethodPattern;

		// String newPattern = null;
		// String method = methodPattern.substring( 2 ); // from "**/list.htm"
		// to "/list.htm"
		// // e.g.: /admin/tabDef
		// if ( servletPath.endsWith( contextSubPath ) ) { // if "/admin/tabDef"
		// // if ( classUrlPath.startsWith( "/" ) ) {
		// // /list.htm -> classPattern /admin/tabDef + /list.htm
		// newPattern = method;
		// // } else {
		// // /admin/tabDef/list.htm
		// // newPattern = "/" + classUrlPath + suffix;
		// // }
		// } else {
		// // e.g.: "/tabDef/list.htm"
		// newPattern = "/" + contextSubPath + method;
		//
		// }
		//
		// return newPattern;

	}

	private String cleanPath(String pathPattern) {

		String resultPattern = pathPattern;

		// remove '**'
		if (resultPattern.startsWith("*")) {
			resultPattern = StringUtils.trimLeadingCharacter(resultPattern, '*');
		}
		if (resultPattern.endsWith("*")) {
			resultPattern = StringUtils.trimTrailingCharacter(resultPattern, '*');
		}

		// remove '/'
		if (resultPattern.startsWith("/")) {
			resultPattern = StringUtils.trimLeadingCharacter(resultPattern, '/');
		}
		if (resultPattern.endsWith("/")) {
			resultPattern = StringUtils.trimTrailingCharacter(resultPattern, '/');
		}

		return resultPattern;
	}

}
