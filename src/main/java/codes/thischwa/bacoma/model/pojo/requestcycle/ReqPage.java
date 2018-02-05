package codes.thischwa.bacoma.model.pojo.requestcycle;

import java.util.UUID;

public class ReqPage extends GenericRequestObject {

	private String title;

	private UUID template;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UUID getTemplate() {
		return template;
	}

	public void setTemplate(UUID template) {
		this.template = template;
	}

	public boolean isValid() {
		return getName() != null && template != null && getParent() != null;
	}
}
