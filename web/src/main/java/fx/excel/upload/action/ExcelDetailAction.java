package fx.excel.upload.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import net.arnx.jsonic.JSON;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

import fx.excel.upload.form.ExcelDetailForm;

public class ExcelDetailAction {
	
	@ActionForm
	@Resource(name = "excelDetailForm")
	public ExcelDetailForm form;
	
	@Resource
	public String uploadedFileRootDir;
	
	@Execute(validator = false, input = "/")
	public String show() throws IOException, InvalidFormatException {
		File rootDirecotory = new File(uploadedFileRootDir);
		File xls = new File(rootDirecotory, form.fileBaseName + "." + form.fileExtention);
		
		if (!xls.exists()) {
			throw new RuntimeException("対象のExcelファイルが見つかりませんでした。"+xls.getAbsolutePath());
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
	
	public static String getCellValue(Cell cell) {
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
