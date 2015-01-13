package fx.excel.upload.entity.names;

import fx.excel.upload.entity.ExcelFile;
import javax.annotation.Generated;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ExcelFile}のプロパティ名の集合です。
 * 
 */
@Generated("GSP")
public class ExcelFileNames {

    /**
     * excelFileIdのプロパティ名を返します。
     * 
     * @return excelFileIdのプロパティ名
     */
    public static PropertyName<Long> excelFileId() {
        return new PropertyName<Long>("excelFileId");
    }

    /**
     * filePathのプロパティ名を返します。
     * 
     * @return filePathのプロパティ名
     */
    public static PropertyName<String> filePath() {
        return new PropertyName<String>("filePath");
    }

    /**
     * fileDataのプロパティ名を返します。
     * 
     * @return fileDataのプロパティ名
     */
    public static PropertyName<String> fileData() {
        return new PropertyName<String>("fileData");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ExcelFileNames extends PropertyName<ExcelFile> {

        /**
         * インスタンスを構築します。
         */
        public _ExcelFileNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ExcelFileNames(final String name) {
            super(name);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param parent
         *            親
         * @param name
         *            名前
         */
        public _ExcelFileNames(final PropertyName<?> parent, final String name) {
            super(parent, name);
        }

        /**
         * excelFileIdのプロパティ名を返します。
         *
         * @return excelFileIdのプロパティ名
         */
        public PropertyName<Long> excelFileId() {
            return new PropertyName<Long>(this, "excelFileId");
        }

        /**
         * filePathのプロパティ名を返します。
         *
         * @return filePathのプロパティ名
         */
        public PropertyName<String> filePath() {
            return new PropertyName<String>(this, "filePath");
        }

        /**
         * fileDataのプロパティ名を返します。
         *
         * @return fileDataのプロパティ名
         */
        public PropertyName<String> fileData() {
            return new PropertyName<String>(this, "fileData");
        }
    }
}
