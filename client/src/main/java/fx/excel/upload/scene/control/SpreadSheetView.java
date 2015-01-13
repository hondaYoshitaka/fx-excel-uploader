package fx.excel.upload.scene.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fx.excel.upload.scene.control.SpreadSheetView.SpreadSheetProperty;
import fx.excel.upload.util.ExcelUtil;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class SpreadSheetView extends TableView<SpreadSheetProperty> {
	
	/**
	 * ヘッダー行とカラム設定を作成します。
	 */
	public void createColumns(int columnSize) {
		ObservableList<TableColumn<SpreadSheetProperty, ?>> columns = this.getColumns();
		
		if (columns.size() != 0) {
			columns.clear();
		}
		List<SpreadSheetColumn> newColumns = new ArrayList<SpreadSheetColumn>();
		
		// 行のヘッダー列 の設定をする
		SpreadSheetColumn rowHeader = new SpreadSheetColumn("rowHeader");
		rowHeader.setText("");
		rowHeader.setId("excel-row-header");
		rowHeader.setCellValueFactory(new PropertyValueFactory<SpreadSheetProperty, String>("rowHeader"));
		newColumns.add(rowHeader);
		
		// 行のデータ列 の設定をする
		for (int index = 0; index < columnSize; index++) {
			String propertyKey = "" + index;
			
			SpreadSheetColumn column = new SpreadSheetColumn(propertyKey);
			column.setText(ExcelUtil.createCelNumber(index));
			
			newColumns.add(column);
		}
		columns.addAll(newColumns);
	}
	
	/**
	 * <スプレッドシート:カラムを表すクラス>
	 *
	 * @author honda
	 */
	public static class SpreadSheetColumn extends TableColumn<SpreadSheetProperty, String> {
		
		public SpreadSheetColumn(String columnIndex) {
			super(columnIndex);
			
			// 動的に値を取得するコールバックをセットする
			this.setCellValueFactory(new Callback<CellDataFeatures<SpreadSheetProperty, String>, ObservableValue<String>>() {
				
				public ObservableValue<String> call(CellDataFeatures<SpreadSheetProperty, String> dataFeatures) {
					CelProperty celProperty = dataFeatures.getValue().getCelProperty(columnIndex);
					
					return celProperty.getCelData();
				}
			});
		}
	}
	
	/**
	 * <スプレッドシート:1行のデータを表すクラス>
	 *
	 * @author honda
	 */
	public static class SpreadSheetProperty {
		
		public IntegerProperty rowHeader;
		
		public ObjectProperty<Map<String, CelProperty>> cels;
		
		public SpreadSheetProperty(int rowNum, List<String> rowDataList) {
			rowHeader = new SimpleIntegerProperty(rowNum + 1);
			
			cels = new SimpleObjectProperty<Map<String, CelProperty>>();
			if (rowDataList != null) {
				Map<String, CelProperty> map = new HashMap<String, CelProperty>();
				for (int index = 0; index < rowDataList.size(); index++) {
					String celData = rowDataList.get(index);
					map.put("" + index, new CelProperty(celData));
				}
				this.cels.set(map);
			}
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
		public CelProperty getCelProperty(String columnNumber) {
			if (cels.get() == null) {
				cels.set(new HashMap<String, CelProperty>());
			}
			// カラム番号にデータがない場合は、空文字とする
			if (!cels.get().containsKey(columnNumber)) {
				cels.get().put(columnNumber, new CelProperty(""));
			}
			return cels.get().get(columnNumber);
		}
	}
	
	/**
	 * <スプレッドシート:1セルのデータを表すクラス>
	 *
	 * @author honda
	 */
	public static class CelProperty {
		private StringProperty celData = new SimpleStringProperty();
		
		public CelProperty(String data) {
			this.celData.set(data);
		}
		
		public StringProperty getCelData() {
			return celData;
		}
	}
}
