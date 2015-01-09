package fx.excel.upload.action;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.arnx.jsonic.JSON;

import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

public class ExcelListAction {
	
	@Resource
	protected ServletContext application;
	
	@Execute(validator = false, input = "/")
	public String findAllExcels() {
		String rootPath = application.getRealPath("../../../target/datahome");
		File rootDirecotory = new File(rootPath);
		
		String json;
		if (rootDirecotory.exists()) {
			List<String> fileNameList = CollectionsUtil.newArrayList();
			for (File file : rootDirecotory.listFiles()) {
				fileNameList.add(file.getName());
			}
			json = JSON.encode(fileNameList);
		} else {
			json = null;
		}
		ResponseUtil.write(json, "application/json", "UTF-8");
		
		return null;
	}
}
