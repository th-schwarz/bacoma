package codes.thischwa.bacoma.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.thischwa.bacoma.rest.model.pojo._Dummy;
import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Controller
@RequestMapping("/check")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${msg2}")
	private String msg2;
	
	@Autowired
	private ContextUtility contextUtility;
	
	@Autowired
	private FileSystemUtil fileSystemUtil; 
	
	@Value("#{siteManager.getSiteConfig().get('site.export.folder')}")
	private String exportFolder;

	@RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody _Dummy check(@RequestParam(required = false, defaultValue = "no message found") String msg) {
		logger.debug("Method check: {}", msg);
		_Dummy d = new _Dummy(msg);
		d.setMsg2(String.format("msg2: %s, count: %d", msg2, contextUtility.getCount()));
		contextUtility.inc();
		logger.info("data-dir: {}, exportFolder: {}", fileSystemUtil.getDataDir().getAbsoluteFile(), exportFolder);
		return d;
	}
}
