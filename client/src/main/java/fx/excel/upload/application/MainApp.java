package fx.excel.upload.application;

import java.net.URL;

import fx.excel.upload.util.ResourceUtil;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainApp extends Application {

	private final URL html = ResourceUtil.getResourceNoException("view/main", "html");

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		final WebView view = new WebView();
		final WebEngine engine = view.getEngine();

		VBox layout = new VBox();
		layout.getChildren().addAll(view);

		Scene scene = new Scene(layout, 1060, 655);
		stage.setScene(scene);

		engine.load(html.toExternalForm());
		stage.show();
	}
}
