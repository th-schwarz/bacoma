package codes.thischwa.bacoma.rest.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import codes.thischwa.bacoma.rest.exception.AbstractBacomaException;
import codes.thischwa.bacoma.rest.exception.PersitException;
import codes.thischwa.bacoma.rest.exception.ResourceNotFoundException;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;

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
	
	public static Response ok() {
		return new Response(STATUS.OK, null, null);
	}
	
	public static Response ok(Object response) {
		return new Response(STATUS.OK, null, response);
	}
	
	public static Response error(String message) {
		return new Response(STATUS.ERROR, message, null);
	}
	
	public static ResponseEntity<Response> buildNoSiteNotLoaded() {
		Response resp = error("No site loaded!");
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
			return ResponseEntity.ok(error(msg));
		} else if(e instanceof PersitException) {
			PersitException pe = (PersitException) e;
			StringBuilder msg = new StringBuilder(String.format("%s couldn't be persit!", site.getUrl()));
			if(pe.getCause() != null)
				msg.append(String.format(" Because of: %s", pe.getCause().getMessage()));
			return ResponseEntity.ok(error(msg.toString()));
		} else {
			throw new RuntimeException("Unknown BacomaException"); 
		}
	}
} 
