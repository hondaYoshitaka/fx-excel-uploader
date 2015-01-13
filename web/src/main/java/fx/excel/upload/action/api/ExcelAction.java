package fx.excel.upload.action.api;

import static org.apache.struts.action.ActionMessages.*;
import static org.seasar.framework.util.LongConversionUtil.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import net.arnx.jsonic.JSON;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.StringConversionUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

import fx.excel.upload.entity.ExcelFile;
import fx.excel.upload.form.api.ExcelForm;
import fx.excel.upload.logic.ExcelLogic;
import fx.excel.upload.service.ExcelFileService;

public class ExcelAction {
	
	@ActionForm
	@Resource(name = "api_excelForm")
	public ExcelForm form;
	
	@Resource
	protected ExcelFileService excelFileService;
	
	@Resource
	protected ExcelLogic excelLogic;
	
	@Resource
	public String uploadedFileRootDir;
	
	@Execute(validator = false)
	public String list() {
		List<ExcelFile> excelFileList = excelFileService.findAllOrderById();
		
		List<BeanMap> fileMapList = CollectionsUtil.newArrayList();
		for (ExcelFile entity : excelFileList) {
			BeanMap fileNameMap = new BeanMap();
			fileNameMap.put("excelFileId", StringConversionUtil.toString(entity.excelFileId));
			fileNameMap.put("fileName", FilenameUtils.getName(entity.filePath));
			
			fileMapList.add(fileNameMap);
		}
		ResponseUtil.write(JSON.encode(fileMapList), "application/json");
		
		return null;
	}
	
	@Execute(validate = "validateBeforeShow", input = "/")
	public String show() throws IOException, InvalidFormatException {
		ExcelFile excelFile = excelFileService.findById(toLong(form.excelFileId));
		
		ResponseUtil.write(excelFile.fileData, "application/json");
		
		return null;
	}
	
	@Execute(validator = false)
	public String upload() throws InvalidFormatException, IOException {
		FormFile formFile = form.uploadFile;
		
		File rootDirecotory = new File(uploadedFileRootDir);
		File uploadFile = new File(rootDirecotory, formFile.getFileName());
		try {
			FileUtils.writeByteArrayToFile(uploadFile, formFile.getFileData());
			
		} catch (IOException e) {
			throw new RuntimeException("ファイルの書き込みに失敗しました。", e);
		}
		ExcelFile entity = new ExcelFile();
		entity.filePath = uploadFile.getAbsolutePath();
		entity.fileData = excelLogic.fetchFileData(uploadFile, 0);
		excelFileService.insert(entity);
		
		BeanMap fileNameMap = new BeanMap();
		fileNameMap.put("excelFileId", StringConversionUtil.toString(entity.excelFileId));
		fileNameMap.put("fileName", FilenameUtils.getName(entity.filePath));
		
		ResponseUtil.write(JSON.encode(fileNameMap), "application/json");
		
		return null;
	}
	
	public ActionMessages validateBeforeShow() {
		ActionMessages errors = new ActionMessages();
		
		ExcelFile excelFile = excelFileService.findById(toLong(form.excelFileId));
		if (excelFile == null) {
			errors.add(GLOBAL_MESSAGE, new ActionMessage("対象のExcelファイルが存在しません。", false));
		}
		return errors;
	}
}
