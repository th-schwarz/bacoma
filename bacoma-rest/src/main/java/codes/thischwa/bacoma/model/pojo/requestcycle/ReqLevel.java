package codes.thischwa.bacoma.model.pojo.requestcycle;

public class ReqLevel extends GenericRequestObject {

	private String title;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isValid() {
		return getName() != null && getParent() != null;
	}
}
