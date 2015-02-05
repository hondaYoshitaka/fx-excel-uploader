package fx.excel.upload.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;

import fx.excel.upload.model.ExcelListModel;
import fx.excel.upload.scene.control.ExcelListItemView;
import fx.excel.upload.scene.control.SpreadSheetView;
import fx.excel.upload.scene.control.SpreadSheetView.SpreadSheetRowData;
import fx.excel.upload.service.ExcelService;

public class ExcelViewerController implements Initializable {

	@FXML
	public Button fileListRefreshBtn;

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

		MultipleSelectionModel<ExcelListModel> listSelectionModel = this.excelListView.getSelectionModel();
		listSelectionModel.selectedItemProperty().addListener(new ChangeListener<ExcelListModel>() {

			@Override
			public void changed(ObservableValue<? extends ExcelListModel> arg0, ExcelListModel arg1, ExcelListModel arg2) {
				if (arg2 == null || arg2.excelFileId == null) {
					return;
				}
				List<List<Map<String, Object>>> excelDetailData = null;
				try {
					excelDetailData = excelService.findByExcelFileId(arg2.excelFileId);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				int maxSize = 0;
				for (List<Map<String, Object>> cels : excelDetailData) {
					if (maxSize >= cels.size()) {
						continue;
					}
					maxSize = cels.size();
				}
				String extension = FilenameUtils.getExtension(arg2.fileName);
				excelDetail.init(extension);
				excelDetail.settingColumns(maxSize);

				ObservableList<SpreadSheetRowData> rowDataItems = FXCollections.observableArrayList();
				for (int rowNum = 0; rowNum < excelDetailData.size(); rowNum++) {
					List<Map<String, Object>> rowData = excelDetailData.get(rowNum);
					Row excelRow = excelDetail.createRow(rowNum, rowData);

					rowDataItems.add(new SpreadSheetRowData(rowNum, excelRow));
				}
				excelDetail.setItems(rowDataItems);
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
			throw new RuntimeException(e);
		}
		excelListModelList.clear();

		for (Map<String, Object> map : excelList) {
			excelListModelList.add(createExcelListModel(map));
		}
		event.consume();
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
