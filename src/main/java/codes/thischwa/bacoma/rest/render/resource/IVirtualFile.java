package codes.thischwa.bacoma.rest.render.resource;

import java.io.File;

import codes.thischwa.bacoma.rest.model.pojo.site.Level;

/**
 * Interface for all file-based resources.
 */
public interface IVirtualFile {

	/**
	 * Constructs the {@link File} for export depending on the {@link CKResourceFileType} (detected by the implemented class).
	 * 
	 * @return File for export.
	 */
	public File getExportFile();

	/**
	 * Constructs the src-tag for export. 
	 * 
	 * @param level {@link Level} is needed to construct relative links.
	 * @return Src-tag for export.
	 */
	public String getTagSrcForExport(final Level level);	
	
	/**
	 * Constructs the src-tag for the preview.
	 * 
	 * @return Source attribute for the preview.
	 */
	public String getTagSrcForPreview();
	
	/**
	 * Getter for the base file.
	 */
	public File getBaseFile();

	/**
	 * Constructs the main file from the src-attribute of an a-tag or an img-tag. Examples:
	 * <ul>
	 * <li>/site/[pmcms.site.dir.resources]/test.zip</li>
	 * <li>/site/[pmcms.site.dir.resources.layout]/test.zip</li>
	 * </ul>
	 * 
	 * @param src the src-attribute of an a-tag or an img-tag
	 * @throws IllegalArgumentException if the src-attribute can't be analyzed.
	 */
	public void consructFromTagFromView(final String src) throws IllegalArgumentException;
	
	/**
	 * @return true for galleries and images, false for files which are used inside the wysiwyg-editor (= content, no layout). 
	 */
	boolean isForLayout();
}
