package fx.excel.upload.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {

	private static int ascii_alpha_start = 65;

	private static int alpha_amount = 26;

	public static String createCelNumber(int index) {
		byte[] nums = null;

		if (index < 0) {
			throw new IllegalArgumentException();
		}

		for (int i = index;;) {
			int divide = (i / alpha_amount), mod = (i % alpha_amount);

			byte asciiValue = new Integer(ascii_alpha_start + mod).byteValue();
			nums = ArrayUtils.add(nums, asciiValue);

			if (i < alpha_amount) {
				break;
			}
			i = (divide - 1);
		}
		ArrayUtils.reverse(nums);
		return new String(nums, StandardCharsets.US_ASCII);
	}

	/**
	 * セルの値を文字列として返す
	 *
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
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
	public static String getStringFormulaValue(Cell cell) {
		try {
			Workbook book = cell.getSheet().getWorkbook();

			CreationHelper helper = book.getCreationHelper();
			FormulaEvaluator evaluator = helper.createFormulaEvaluator();
			Cell evalCell = evaluator.evaluateInCell(cell);

			return getCellValue(evalCell);
		} catch (Exception e) {
			System.err.println(cell.getCellFormula());
			e.printStackTrace();
			return "#UNSUPPORT";
		}
	}
}
