package fx.excel.upload.action;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import net.arnx.jsonic.JSON;

import org.apache.commons.io.FileUtils;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

import fx.excel.upload.form.ExcelUploadForm;

public class ExcelUploadAction {

	@ActionForm
	@Resource(name = "excelUploadForm")
	public ExcelUploadForm form;

	@Execute(validator = true, input = "/")
	public String uploadTemporary() {
		FormFile formFile = form.uploadFile;

		try {
			// FIXME write file
			FileUtils.writeByteArrayToFile(new File(""), formFile.getFileData());

		} catch (IOException e) {
			throw new RuntimeException("ファイルの書き込みに失敗しました。", e);
		}
		BeanMap fileNameMap = new BeanMap();
		fileNameMap.put("fileName", formFile.getFileName());

		ResponseUtil.write(JSON.encode(fileNameMap), "application/json");

		return null;
	}
}
