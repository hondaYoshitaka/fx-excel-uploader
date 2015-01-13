package fx.excel.upload.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import fx.excel.upload.model.ExcelListModel;
import fx.excel.upload.scene.control.ExcelListItemView;
import fx.excel.upload.scene.control.SpreadSheetView;
import fx.excel.upload.scene.control.SpreadSheetView.SpreadSheetProperty;
import fx.excel.upload.service.ExcelService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

public class ExcelViewerContorller implements Initializable {
	
	@FXML
	public ListView<ExcelListModel> excelListView;
	
	@FXML
	public SpreadSheetView excelDetail;
	
	private ObservableList<ExcelListModel> excelListModelList;
	
	private ExcelService excelService = new ExcelService();
	
	@Override
	public void initialize(URL paramURL, ResourceBundle paramResourceBundle) {
		List<Map<String, Object>> excelList = null;
		try {
			excelList = excelService.findAllExcelFileName();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		excelListModelList = FXCollections.observableArrayList();
		for (Map<String, Object> map : excelList) {
			excelListModelList.add(createExcelListModel(map));
		}
		this.excelListView.setItems(excelListModelList);
		this.excelListView.setCellFactory(new Callback<ListView<ExcelListModel>, ListCell<ExcelListModel>>() {
			
			@Override
			public ListCell<ExcelListModel> call(ListView<ExcelListModel> arg0) {
				return new ExcelListItemView();
			}
		});
		
		FocusModel<ExcelListModel> focusModel = excelListView.getFocusModel();
		focusModel.focusedItemProperty().addListener(new ChangeListener<ExcelListModel>() {
			
			@Override
			public void changed(ObservableValue<? extends ExcelListModel> observableValue, ExcelListModel oldValue,
					ExcelListModel value) {
				if (value == null || value.excelFileId == null) {
					return;
				}
				List<List<String>> excelDetailData = null;
				try {
					excelDetailData = excelService.findByExcelFileId(value.excelFileId);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				int maxSize = 0;
				for (List<String> cels : excelDetailData) {
					if (maxSize < cels.size()) {
						maxSize = cels.size();
					}
				}
				excelDetail.createColumns(maxSize);
				
				ObservableList<SpreadSheetProperty> detail = FXCollections.observableArrayList();
				for (int rowNum = 0; rowNum < excelDetailData.size(); rowNum++) {
					List<String> rowData = excelDetailData.get(rowNum);
					
					detail.add(new SpreadSheetProperty(rowNum, rowData));
				}
				excelDetail.setItems(detail);
			}
		});
	}
	
	@FXML
	public void handleFileChoose(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Excelファイル", "*.xls", "*.xlsx"));
		
		File excel = fileChooser.showOpenDialog(null);
		if (excel == null) {
			return;
		}
		try {
			excelService.insert(excel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		handleListRefresh(event);
	}
	
	@FXML
	public void handleListRefresh(ActionEvent event) {
		List<Map<String, Object>> excelList = null;
		try {
			excelList = excelService.findAllExcelFileName();
		} catch (Exception e) {
			throw new RuntimeException("");
		}
		excelListModelList.clear();
		
		for (Map<String, Object> map : excelList) {
			excelListModelList.add(createExcelListModel(map));
		}
	}
	
	/**
	 * mapから Modelエンティティを作成します。
	 *
	 * @param map
	 * @return
	 */
	private static ExcelListModel createExcelListModel(Map<String, Object> map) {
		ExcelListModel model = new ExcelListModel();
		
		model.fileName = (String) map.get("fileName");
		model.excelFileId = (String) map.get("excelFileId");
		
		return model;
	}
}
