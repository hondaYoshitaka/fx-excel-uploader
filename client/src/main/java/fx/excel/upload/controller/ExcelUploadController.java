package fx.excel.upload.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import fx.excel.upload.service.ExcelService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ExcelUploadController implements Initializable {
	
	private ExcelService excelService = new ExcelService();
	
	@Override
	public void initialize(URL paramURL, ResourceBundle paramResourceBundle) {
		
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
	}
}
