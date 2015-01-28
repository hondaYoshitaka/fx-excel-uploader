package fx.excel.upload.scene.control;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fx.excel.upload.scene.control.SpreadSheetView.SpreadSheetRowData;
import fx.excel.upload.util.ExcelUtil;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

public class SpreadSheetView extends TableView<SpreadSheetRowData> {
	
	/** 数式計算のための擬似的なExcelシート */
	private Sheet sheet;
	
	@SuppressWarnings("resource")
	public void init(String extension) {
		ObservableList<TableColumn<SpreadSheetRowData, ?>> columns = this.getColumns();
		if (!columns.isEmpty()) {
			columns.clear();
		}
		Workbook workbook = null;
		switch (extension) {
			case "xls":
				workbook = new HSSFWorkbook();
				break;
			case "xlsx":
				workbook = new XSSFWorkbook();
				break;
			default:
				throw new RuntimeException("Illegal extension: " + extension);
		}
		sheet = workbook.createSheet();
		sheet.setForceFormulaRecalculation(true);
	}
	
	/**
	 * ヘッダー行とカラム設定を作成します。
	 *
	 * @param columnSize
	 */
	public void settingColumns(int columnSize) {
		// 行ヘッダーの列 を作成する
		TableColumn<SpreadSheetRowData, String> rowHeader = new TableColumn<SpreadSheetRowData, String>("rowHeader");
		rowHeader.setId("excel-row-header");
		rowHeader.setText("");
		rowHeader.setCellValueFactory(new PropertyValueFactory<SpreadSheetRowData, String>("rowHeader"));
		
		this.getColumns().add(rowHeader);
		
		// 行データの列 を作成する
		for (int index = 0; index < columnSize; index++) {
			final String propertyKey = "" + index;
			
			TableColumn<SpreadSheetRowData, String> column = new TableColumn<SpreadSheetRowData, String>(propertyKey);
			column.setText(ExcelUtil.createCelNumber(index));
			column.setCellValueFactory(new Callback<CellDataFeatures<SpreadSheetRowData, String>, ObservableValue<String>>() {
				
				public ObservableValue<String> call(CellDataFeatures<SpreadSheetRowData, String> dataFeatures) {
					CellProperty celProperty = dataFeatures.getValue().cellProperty(propertyKey);
					
					return celProperty.getCelData();
				}
			});
			// 編集可能なセルを作成する
			column.setCellFactory(new Callback<TableColumn<SpreadSheetRowData, String>, TableCell<SpreadSheetRowData, String>>() {
				
				@Override
				public TableCell<SpreadSheetRowData, String> call(TableColumn<SpreadSheetRowData, String> arg0) {
					return new TextFieldTableCell<SpreadSheetRowData, String>(new DefaultStringConverter());
				}
			});
			this.getColumns().add(column);
		}
	}
	
	/**
	 * SpreadSheetView用の行データを作成します。
	 *
	 * @param rowNumber
	 * @param rowData
	 * @return
	 */
	public Row createRow(int rowNumber, List<String> rowData) {
		Row row = sheet.createRow(rowNumber);
		
		for (int cellIndex = 0; cellIndex < rowData.size(); cellIndex++) {
			String data = rowData.get(cellIndex);
			
			Cell cell = row.createCell(cellIndex);
			cell.setCellValue(data);
		}
		return row;
	}
	
	/**
	 * <スプレッドシート:1行のデータを表すクラス>
	 *
	 * @author honda
	 */
	public static class SpreadSheetRowData {
		
		public IntegerProperty rowHeader;
		
		public Row row;
		
		public SpreadSheetRowData(int rowNum, Row row) {
			this.rowHeader = new SimpleIntegerProperty(rowNum + 1);
			this.row = row;
		}
		
		/**
		 * 行番号を取得します。
		 *
		 * <pre>
		 * 命名規則: 行のキー(rowHeader) + Property
		 * </pre>
		 *
		 * @return
		 */
		public IntegerProperty rowHeaderProperty() {
			return this.rowHeader;
		}
		
		/**
		 * 該当のカラム番号のデータを取得します
		 *
		 * @param columnNumber
		 * @return
		 */
		public CellProperty cellProperty(String columnNumber) {
			Cell cellData = row.getCell(Integer.parseInt(columnNumber));
			
			if (cellData == null || cellData.getStringCellValue() == null) {
				return new CellProperty("");
			}
			return new CellProperty(cellData.getStringCellValue());
		}
	}
	
	/**
	 * <スプレッドシート:1セルのデータを表すクラス>
	 *
	 * @author honda
	 */
	public static class CellProperty {
		private StringProperty celData = new SimpleStringProperty();
		
		public CellProperty(String data) {
			this.celData.set(data);
		}
		
		public StringProperty getCelData() {
			return celData;
		}
	}
}
