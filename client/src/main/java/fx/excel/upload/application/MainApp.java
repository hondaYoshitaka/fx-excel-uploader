package fx.excel.upload.application;

import static fx.excel.upload.util.ResourceUtil.*;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private URL excelViewer = getResourceNoException("fxml/excel-viewer", "xml");
	
	private URL css = getResourceNoException("css/fx-client", "css");
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		VBox continerView = FXMLLoader.load(excelViewer);
		
		Scene scene = new Scene(continerView);
		scene.getStylesheets().add(css.toExternalForm());
		
		stage.setScene(scene);
		stage.setTitle("JavaFx Excelファイルアップロード");
		
		stage.show();
	}
}
