package fx.excel.upload.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

public class ExcelUploadController implements Initializable {
	
	private static final String POST_EXCEL_URL = "http://localhost:8080/fx-excel-upload/excel";
	
	@Override
	public void initialize(URL paramURL, ResourceBundle paramResourceBundle) {
		
	}
	
	@FXML
	public void handleFileChoose(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.xls", "*.xlsx");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File excel = fileChooser.showOpenDialog(null);
		if (excel == null) {
			return;
		}
		try {
			fileUpload(excel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void fileUpload(File excel) throws IOException {
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpPost httpPost = new HttpPost(POST_EXCEL_URL);
		
		MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
		mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		mBuilder.setCharset(StandardCharsets.UTF_8);
		mBuilder.addBinaryBody("uploadFile", excel, ContentType.create("application/excel"), excel.getName());
		httpPost.setEntity(mBuilder.build());
		
		client.execute(httpPost);
	}
}
