package fx.excel.upload.form;

import org.apache.struts.upload.FormFile;
import org.seasar.struts.annotation.Required;

public class ExcelUploadForm {

	@Required
	public FormFile uploadFile;

}
