package fx.excel.upload.logic;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.arnx.jsonic.JSON;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
	
	/**
	 * セルの値を文字列として返す
	 *
	 * @param cell
	 * @return
	 */
	private static String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				// TODO 結合セルの場合どう扱うか？
				return "";
			case Cell.CELL_TYPE_BOOLEAN:
				return Boolean.toString(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_NUMERIC:
				return Double.toString(cell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return getStringFormulaValue(cell);
			case Cell.CELL_TYPE_ERROR:
				return "#N/A";
		}
		return "";
	}
	
	/**
	 * 数式を再計算し、文字列として返す
	 *
	 * <p>
	 * ◇使用可能な関数について
	 * </p>
	 * <ul>
	 * <li>http://poi.apache.org/spreadsheet/eval-devguide.html</li>
	 * <li>{@link org.apache.poi.ss.formula.eval.FunctionEval}</li>
	 * </ul>
	 *
	 * @param cell
	 * @return
	 */
	private static String getStringFormulaValue(Cell cell) {
		try {
			Workbook book = cell.getSheet().getWorkbook();
			
			CreationHelper helper = book.getCreationHelper();
			FormulaEvaluator evaluator = helper.createFormulaEvaluator();
			Cell evalCell = evaluator.evaluateInCell(cell);
			
			return getCellValue(evalCell);
		} catch (NotImplementedException e) {
			return "#UNSUPPORT";
		}
	}
}
