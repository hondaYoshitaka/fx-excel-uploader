package fx.excel.upload.scene.control;

import javafx.scene.control.ListCell;
import fx.excel.upload.model.ExcelListModel;

public class ExcelListItemView extends ListCell<ExcelListModel> {

	/**
	 * modelに変更があった時の処理
	 */
	@Override
	protected void updateItem(ExcelListModel model, boolean empty) {
		super.updateItem(model, empty);

		// nullで更新しないと以前のテキストが残ってしまう
		this.setText(empty ? null : model.fileName);
	}
}
