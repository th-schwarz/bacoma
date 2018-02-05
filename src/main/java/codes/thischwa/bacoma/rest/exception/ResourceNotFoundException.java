package codes.thischwa.bacoma.rest.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import codes.thischwa.bacoma.model.pojo.site.Site;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends AbstractBacomaException {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private UUID id;
	
	public ResourceNotFoundException(Site site) {
		super(site);
	}

	public ResourceNotFoundException(Site site, String name) {
		this(site);
		this.name = name;
	}

	public ResourceNotFoundException(Site site, UUID id) {
		this(site);
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getId() {
		return id;
	}
}
