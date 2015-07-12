package codes.thischwa.bacoma.rest.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class IsNotARenderableException extends AbstractBacomaException {

	private static final long serialVersionUID = 1L;
	
	private UUID id;

	public IsNotARenderableException(Site site, UUID id) {
		super(site);
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

}
