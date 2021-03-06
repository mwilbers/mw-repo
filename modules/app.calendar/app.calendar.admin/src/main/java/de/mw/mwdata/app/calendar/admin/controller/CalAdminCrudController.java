package de.mw.mwdata.app.calendar.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.rest.ofdb.control.AbstractOfdbCrudController;

@RequestMapping("/caladmin/**")
public class CalAdminCrudController<E extends AbstractMWEntity> extends AbstractOfdbCrudController<E> {

}
