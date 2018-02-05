package codes.thischwa.bacoma.model.pojo.requestcycle;

import com.fasterxml.jackson.annotation.JsonProperty;

import codes.thischwa.bacoma.model.pojo.site.SiteResourceType;
import codes.thischwa.bacoma.model.pojo.site.TemplateType;

public class ReqTemplate extends GenericRequestSiteResource {

	private TemplateType templateType;

	public ReqTemplate() {
		setResourceType(SiteResourceType.TEMPLATE);
	}
	
	public TemplateType getTemplateType() {
		return templateType;
	}
	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}
	@JsonProperty("templateType")
	public void setTemplateType(String templateType) {
		this.templateType = Enum.valueOf(TemplateType.class, templateType);
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && templateType != null;
	}
}
