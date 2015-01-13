package fx.excel.upload.action.api;

import static java.nio.charset.StandardCharsets.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import net.arnx.jsonic.JSON;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

import fx.excel.upload.form.ExcelForm;

public class ExcelAction {
	
	@ActionForm
	@Resource(name = "excelForm")
	public ExcelForm form;
	
	@Resource
	public String uploadedFileRootDir;
	
	@Execute(validator = false, input = "/")
	public String list() {
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
	
	@Execute(validator = false, input = "/")
	public String upload() {
		FormFile formFile = form.uploadFile;
		
		File rootDirecotory = new File(uploadedFileRootDir);
		try {
			FileUtils.writeByteArrayToFile(new File(rootDirecotory, formFile.getFileName()), formFile.getFileData());
			
		} catch (IOException e) {
			throw new RuntimeException("ファイルの書き込みに失敗しました。", e);
		}
		BeanMap fileNameMap = new BeanMap();
		fileNameMap.put("fileName", formFile.getFileName());
		
		ResponseUtil.write(JSON.encode(fileNameMap), "application/json");
		
		return null;
	}
	
	@Execute(validator = false, input = "/")
	public String show() throws IOException, InvalidFormatException {
		File rootDirecotory = new File(uploadedFileRootDir);
		File xls = new File(rootDirecotory, form.fileBaseName + "." + form.fileExtention);
		
		if (!xls.exists()) {
			throw new RuntimeException("対象のExcelファイルが見つかりませんでした。" + xls.getAbsolutePath());
		}
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(xls);
			
			// 最初の1シートのみ読む
			Sheet sheet = workbook.getSheetAt(0);
			sheet.setForceFormulaRecalculation(true);
			
			List<List<String>> resultList = CollectionsUtil.newArrayList();
			for (Row row : sheet) {
				List<String> rowList = CollectionsUtil.newArrayList();
				for (Cell cell : row) {
					rowList.add(getCellValue(cell));
				}
				resultList.add(rowList);
			}
			ResponseUtil.write(JSON.encode(resultList), "application/json");
			
		} finally {
			IOUtils.closeQuietly(workbook);
		}
		return null;
	}
	
	private static String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return Boolean.toString(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case Cell.CELL_TYPE_NUMERIC:
				return Double.toString(cell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
		}
		return "";
	}
}
