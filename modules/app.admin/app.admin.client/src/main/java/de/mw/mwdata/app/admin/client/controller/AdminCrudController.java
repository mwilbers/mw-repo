package de.mw.mwdata.app.admin.client.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.rest.ofdb.control.AbstractOfdbBasedCrudController;

@RequestMapping("/admin/**")
public class AdminCrudController<E extends AbstractMWEntity> extends AbstractOfdbBasedCrudController<E> {

}
