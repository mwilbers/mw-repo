package de.mw.mwdata.app.admin.client.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import de.mw.mwdata.rest.ofdb.control.AbstractOfdbUserConfigController;

/**
 * Controller for serving all user specific and systemwide properties and
 * constants. <br>
 * 
 */
@RequestMapping("/admin/userConfig/**")
public class UserConfigController extends AbstractOfdbUserConfigController {

}
