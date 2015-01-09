package fx.excel.upload.application;

import static fx.excel.upload.util.ResourceUtil.*;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

	private URL header = getResourceNoException("fxml/header","xml");

	private URL excelUpload = getResourceNoException("fxml/excel-upload","xml");

	private URL excelList = getResourceNoException("fxml/excel-list","xml");

	private URL css = getResourceNoException("css/fx-client","css");

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		VBox layout = new VBox();
		layout.setId("container");

		VBox headerPane = FXMLLoader.load(header);
		VBox uploaderPane = FXMLLoader.load(excelUpload);
		VBox listPane = FXMLLoader.load(excelList);

		layout.getChildren().addAll(headerPane, uploaderPane, listPane);

		Scene scene = new Scene(layout);
		scene.getStylesheets().add(css.toExternalForm());

		stage.setScene(scene);
		stage.setTitle("JavaFx Excelファイルアップロード");

		stage.show();
	}
}
