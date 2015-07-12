package codes.thischwa.bacoma.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.thischwa.bacoma.rest.exception.AbstractBacomaException;
import codes.thischwa.bacoma.rest.exception.IsNotARenderableException;
import codes.thischwa.bacoma.rest.exception.PersitException;
import codes.thischwa.bacoma.rest.exception.ResourceNotFoundException;
import codes.thischwa.bacoma.rest.exception.SiteNotLoadedException;

/**
 * Global exception handling.
 */
@ControllerAdvice
public class ExceptionHandlingController {

	@ResponseBody
	@ExceptionHandler(SiteNotLoadedException.class)
	public ResponseEntity<Response> handleSiteNotLoaded() {
		return Response.buildNoSiteNotLoaded();
	}

	@ResponseBody
	@ExceptionHandler({IsNotARenderableException.class, PersitException.class, ResourceNotFoundException.class})
	public ResponseEntity<Response> handleResourceNotFound(AbstractBacomaException ex) {
		return Response.build(ex);
	}
}
