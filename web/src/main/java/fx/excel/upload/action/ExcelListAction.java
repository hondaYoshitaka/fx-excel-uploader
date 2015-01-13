package fx.excel.upload.action;

import static java.nio.charset.StandardCharsets.*;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import net.arnx.jsonic.JSON;

import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

public class ExcelListAction {
	
	@Resource
	protected String uploadedFileRootDir;
	
	@Execute(validator = false, input = "/")
	public String findAllExcels() {
		File rootDirecotory = new File(uploadedFileRootDir);
		
		if (!rootDirecotory.exists()) {
			ResponseUtil.write("[]", "application/json", "UTF-8");
			return null;
		}
		List<String> fileNameList = CollectionsUtil.newArrayList();
		for (File file : rootDirecotory.listFiles()) {
			fileNameList.add(file.getName());
		}
		ResponseUtil.write(JSON.encode(fileNameList), "application/json", UTF_8.name());
		
		return null;
	}
}