package codes.thischwa.bacoma.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import codes.thischwa.bacoma.model.pojo.site.Site;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PersitException extends AbstractBacomaException {

	private static final long serialVersionUID = 1L;
	
	public PersitException(Site site) {
		super(site);
	}

	public PersitException(Site site, Throwable cause) {
		super(site, cause);
	}
}
