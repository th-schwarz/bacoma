package codes.thischwa.bacoma.rest.exception;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;

public class AbstractBacomaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Site site;
	
	public AbstractBacomaException(Site site) {
		super();
		this.site = site;
	}
	
	public Site getSite() {
		return site;
	}

}
