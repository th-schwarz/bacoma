package codes.thischwa.bacoma.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends AbstractBacomaException {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	public ResourceNotFoundException(Site site) {
		super(site);
	}

	public ResourceNotFoundException(Site site, String name) {
		this(site);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
