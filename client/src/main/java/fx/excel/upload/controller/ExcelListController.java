package fx.excel.upload.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.ListView;

public class ExcelListController implements Initializable {
	
	@FXML
	public ListView<String> excelListView;
	
	@FXML
	public SpreadSheetView excelDetail;
	
	/** excelファイル一覧 */
	public ObservableList<String> fileNames;
	
	private ExcelService excelService = new ExcelService();
	
	@Override
	public void initialize(URL paramURL, ResourceBundle paramResourceBundle) {
		List<String> excelList = null;
		
		try {
			excelList = excelService.findAllExcelFileName();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		fileNames = FXCollections.observableArrayList();
		for (String name : excelList) {
			fileNames.add(name);
		}
		this.excelListView.setItems(fileNames);
		
		FocusModel<String> focusModel = excelListView.getFocusModel();
		focusModel.focusedItemProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String value) {
				List<List<String>> remoteExcelDetail = null;
				
				try {
					remoteExcelDetail = excelService.findExcelByFileName(value);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				int maxSize = 0;
				for (List<String> rows : remoteExcelDetail) {
					if (maxSize < rows.size()) {
						maxSize = rows.size();
					}
				}
				excelDetail.createColumns(maxSize);
				
				ObservableList<SpreadSheetProperty> detail = FXCollections.observableArrayList();
				for (int rowNum = 0; rowNum < remoteExcelDetail.size(); rowNum++) {
					List<String> rowData = remoteExcelDetail.get(rowNum);
					
					detail.add(new SpreadSheetProperty(rowNum, rowData));
				}
				excelDetail.setItems(detail);
			}
		});
	}
	
	@FXML
	public void handleListRefresh(ActionEvent event) {
		List<String> excelList = null;
		try {
			excelList = excelService.findAllExcelFileName();
		} catch (Exception e) {
			throw new RuntimeException("");
		}
		fileNames.clear();
		for (String name : excelList) {
			fileNames.add(name);
		}
	}
}
