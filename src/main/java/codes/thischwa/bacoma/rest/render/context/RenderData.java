package codes.thischwa.bacoma.rest.render.context;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.render.resource.IVirtualFile;


/**
 * Data collector for rendering.
 */
@Component
public class RenderData {
	
	private Set<File> files;
	
	public RenderData() {
		files = Collections.synchronizedSet(new HashSet<File>());
	}
	
	public void clear() {
		files.clear();
	}
	
	public void addFile(final IVirtualFile vf) {
		addFile(vf.getBaseFile());
	}
	
	public void addFile(final File file) {
		if(!file.isDirectory())
			files.add(file.getAbsoluteFile());
	}
	
	public Set<File> getFilesToCopy() {
		return files;
	}
}
