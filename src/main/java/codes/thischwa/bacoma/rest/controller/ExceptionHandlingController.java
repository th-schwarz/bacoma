package codes.thischwa.bacoma.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.thischwa.bacoma.rest.exception.ResourceNotFoundException;
import codes.thischwa.bacoma.rest.exception.SiteNotLoadedException;

@ControllerAdvice
public class ExceptionHandlingController {

	@ResponseBody
	@ExceptionHandler(SiteNotLoadedException.class)
	public ResponseEntity<Response> handleSiteNotLoaded() {
		return Response.buildNoSiteNotLoaded();
	}

	@ResponseBody
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Response> handleResourceNotFound(ResourceNotFoundException ex) {
		return Response.build(ex);
	}
}
