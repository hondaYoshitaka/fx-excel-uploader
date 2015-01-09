package fx.excel.upload.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.arnx.jsonic.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ExcelListController implements Initializable {
	
	@FXML
	public ListView<String> excelListView;
	
	@FXML
	public Label excelDetail;
	
	/** excelファイル一覧 */
	public ObservableList<String> fileNames;
	
	private static final String GET_EXCEL_LIST_URL = "http://localhost:8080/fx-excel-upload/excel";
	
	@Override
	public void initialize(URL paramURL, ResourceBundle paramResourceBundle) {
		List<String> excelList = null;
		
		try {
			excelList = getRemoteExcelList();
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
				excelDetail.setText(value);
				
				// TODO サーバから詳細を取得する
				
				// TODO 詳細に表示する
			}
		});
	}
	
	@FXML
	public void handleListRefresh(ActionEvent event) {
		List<String> excelList = null;
		try {
			excelList = getRemoteExcelList();
		} catch (Exception e) {
			throw new RuntimeException("");
		}
		fileNames.clear();
		for (String name : excelList) {
			fileNames.add(name);
		}
	}
	
	private List<String> getRemoteExcelList() throws IOException {
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpGet httpget = new HttpGet(GET_EXCEL_LIST_URL);
		HttpEntity entity = null;
		
		HttpResponse response = client.execute(httpget);
		entity = response.getEntity();
		InputStream is = entity.getContent();
		
		if (is == null) {
			return new ArrayList<String>();
		}
		return JSON.decode(is);
	}
}
