package fx.excel.upload.scene.control;

import fx.excel.upload.model.ExcelListModel;

import javafx.scene.control.ListCell;

public class ExcelListItemView extends ListCell<ExcelListModel> {
	
	@Override
	protected void updateItem(ExcelListModel model, boolean empty) {
		super.updateItem(model, empty);
		
		if (!empty) {
			setText(model.fileName);
		}
	}
}
