package codes.thischwa.bacoma.exception;

import codes.thischwa.bacoma.model.pojo.site.Site;

public class AbstractBacomaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Site site;
	
	public AbstractBacomaException(Site site) {
		super();
		this.site = site;
	}
	
	public AbstractBacomaException(Site site, Throwable cause) {
		super(cause);
		this.site = site;
	}

	public Site getSite() {
		return site;
	}

}
