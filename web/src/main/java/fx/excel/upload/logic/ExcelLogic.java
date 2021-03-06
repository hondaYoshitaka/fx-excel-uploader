package fx.excel.upload.logic;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.arnx.jsonic.JSON;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class ExcelLogic {
	
	public String fetchFileData(File file, int sheetAt) throws InvalidFormatException, IOException {
		Workbook workbook = null;
		
		try {
			workbook = WorkbookFactory.create(file);
			
			Sheet sheet = workbook.getSheetAt(sheetAt);
			sheet.setForceFormulaRecalculation(true);
			
			List<List<String>> resultList = CollectionsUtil.newArrayList();
			for (Row row : sheet) {
				List<String> rowList = CollectionsUtil.newArrayList();
				for (Cell cell : row) {
					rowList.add(getCellValue(cell));
				}
				resultList.add(rowList);
			}
			return JSON.encode(resultList);
			
		} finally {
			IOUtils.closeQuietly(workbook);
		}
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
