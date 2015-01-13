package fx.excel.upload.service;

import static fx.excel.upload.entity.names.ExcelFileNames.*;
import static org.seasar.extension.jdbc.operation.Operations.*;

import java.util.List;

import javax.annotation.Generated;

import fx.excel.upload.entity.ExcelFile;

/**
 * {@link ExcelFile}のサービスクラスです。
 *
 */
@Generated(value = { "S2JDBC-Gen 2.4.48", "org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactoryImpl" }, date = "2015/01/13 15:47:07")
public class ExcelFileService extends AbstractService<ExcelFile> {

	/**
	 * 識別子でエンティティを検索します。
	 *
	 * @param excelFileId
	 *            識別子
	 * @return エンティティ
	 */
	public ExcelFile findById(Long excelFileId) {
		return select()
				.id(excelFileId)
				.getSingleResult();
	}

	/**
	 * 識別子の昇順ですべてのエンティティを検索します。
	 *
	 * @return エンティティのリスト
	 */
	public List<ExcelFile> findAllOrderById() {
		return select()
				.orderBy(asc(excelFileId()))
				.getResultList();
	}

	/**
	 * ファイルパスを元にエンティティを検索します。
	 *
	 * @param filePath
	 * @return
	 */
	public ExcelFile findByFileName(String filePath) {
		return select()
				.where(eq(filePath(), filePath))
				.getSingleResult();
	}
}