package codes.thischwa.bacoma.rest.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import codes.thischwa.bacoma.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.exception.AbstractBacomaException;
import codes.thischwa.bacoma.rest.exception.PersitException;
import codes.thischwa.bacoma.rest.exception.ResourceNotFoundException;

public class Response {
	
	public enum STATUS {
		OK, ERROR;
	}
	
	private STATUS status;
	
	private String message;

	private Object object;

	
	private Response(STATUS status, String message, Object object) {
		this.status = status;
		this.message = message;
		this.object = object;
	}

	public STATUS getStatus() {
		return status;
	}
	
	@JsonInclude(Include.NON_EMPTY)
	public String getMessage() {
		return message;
	}

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("response")
	public Object getObject() {
		return object;
	}
	
	public static ResponseEntity<Response> ok() {
		return ResponseEntity.ok(new Response(STATUS.OK, null, null));
	}
	
	public static ResponseEntity<Response> ok(Object response) {
		return ResponseEntity.ok(new Response(STATUS.OK, null, response));
	}
	
	public static ResponseEntity<Response> error(String message) {
		return ResponseEntity.ok(new Response(STATUS.ERROR, message, null));
	}
	
	public static ResponseEntity<Response> error(String message, HttpStatus status) {
		return new ResponseEntity<Response>(new Response(STATUS.ERROR, message, null), status);		
	}
	
	public static ResponseEntity<Response> buildNoSiteNotLoaded() {
		Response resp = new Response(STATUS.ERROR,"No site loaded!", null);
		return ResponseEntity.ok().header("Content-Type", MediaType.APPLICATION_JSON_VALUE).body(resp);
	}
	
	public static ResponseEntity<Response> build(AbstractBacomaException e) {
		Site site = e.getSite();
		if(e instanceof ResourceNotFoundException) {
			ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException)e;
			String resourceName = resourceNotFoundException.getName();
			UUID id = resourceNotFoundException.getId(); 
			String msg;
			if(id != null)
				msg = String.format("%s: resource with the id [%s] not found!", site.getUrl(), id.toString());
			else 
				msg = String.format("%s: resource with the name [%s] not found!", site.getUrl(), resourceName);
			return error(msg);
		} else if(e instanceof PersitException) {
			PersitException pe = (PersitException) e;
			StringBuilder msg = new StringBuilder(String.format("%s couldn't be persit!", site.getUrl()));
			if(pe.getCause() != null)
				msg.append(String.format(" Because of: %s", pe.getCause().getMessage()));
			return error(msg.toString());
		} else {
			throw new RuntimeException("Unknown BacomaException"); 
		}
	}
} 
