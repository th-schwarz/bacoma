package codes.thischwa.bacoma.rest.render;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.model.pojo.site.Template;
import codes.thischwa.bacoma.rest.render.context.ContextObjectManager;
import codes.thischwa.bacoma.rest.service.SiteManager;

@Service
public class VelocityRenderer {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private SiteManager siteManager;
	
	/**
	 * The *main* render method. It renders a string with respect of possible context objects.
	 * 
	 * @param writer
	 *            Contains the rendered string. It has to be flushed and closed by the caller!
	 * @param stringToRender
	 *            A string to render.
	 * @param contextObjects
	 *            Contains the context objects. It could be null or empty too.
	 */
	private void renderString(Writer writer, final String stringToRender, final Map<String, Object> contextObjects) {
		if (StringUtils.isBlank(stringToRender))
			return;

		VelocityContext ctx = new VelocityContext(contextObjects);
		try {
			siteManager.getVelocityEngine().evaluate(ctx, writer, "StaticRenderfield", stringToRender);
		} catch (Exception e) {
			throw new RenderException("While string rendering: " + e.getMessage(), e);
		}
	}
	

	public void render(StringBuilderWriter writer, IRenderable renderable, ViewMode viewMode) {
		render(writer, renderable, viewMode, null);
	}
	
	/**
	 * Renders an {@link IRenderable} with respect of the {@link ViewMode} and possible context objects into <code>writer</code>.
	 * 
	 * @param writer
	 *            Contains the rendered string. It has to be flushed and closed by the caller!
	 * @param renderable
	 *            The {@link IRenderable} to render.
	 * @param viewMode
	 *            The {@link ViewMode} to respect.
	 * @param additionalContextObjects
	 *            Contains the context objects. It could be null or empty.
	 */
	public void render(Writer writer, final IRenderable renderable, final ViewMode viewMode,
			final Map<String, Object> additionalContextObjects) {
		logger.debug("Try to render: {}", renderable);
		Site site = BoInfo.getSite(renderable);
		Map<String, Object> contextObjects = ContextObjectManager.get(renderable, viewMode, applicationContext);
		if (logger.isDebugEnabled()) {
			logger.debug("context objects:");
			for (String objName : contextObjects.keySet()) {
				logger.debug(" - Object class: " + objName + " - " + contextObjects.get(objName).getClass());
			}
		}
		if (additionalContextObjects != null && !additionalContextObjects.isEmpty())
			contextObjects.putAll(additionalContextObjects);
		try {
			String templateContent = ((Template) siteManager.getObject(renderable.getTemplateID())).getText();
			StringWriter contentWriter = new StringWriter();
			renderString(contentWriter, templateContent, contextObjects);

			if(site.getLayoutTemplate() != null && StringUtils.isNoneBlank(site.getLayoutTemplate().getText())) {
				String layoutContent = site.getLayoutTemplate().getText();
				contentWriter.flush();
				contextObjects.put("content", contentWriter.toString());
				renderString(writer, layoutContent, contextObjects);
			} else
				writer.write(contentWriter.toString());
		} catch (IOException e) {
			throw new RenderException(renderable.getTemplateType(), renderable.getId(), e);
		}
	}

	/**
	 * Renders a string with respect of possible context objects.
	 * 
	 * @param stringToRender
	 *            A string to render.
	 * @param contextObjects
	 *            Context object. It can be null or empty too.
	 * @return The rendered string.
	 */
	public String renderString(final String stringToRender, final Map<String, Object> contextObjects) {
		if (StringUtils.isBlank(stringToRender))
			return "";

		StringWriter stringWriter = new StringWriter();
		renderString(stringWriter, stringToRender, contextObjects);
		stringWriter.flush();
		IOUtils.closeQuietly(stringWriter);
		return stringWriter.toString();
	}
}
