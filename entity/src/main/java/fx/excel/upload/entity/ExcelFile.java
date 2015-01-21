package fx.excel.upload.entity;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 
 *
 */
@Generated("GSP")
@Entity
@Table(name = "excel_file")
public class ExcelFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /** EXCELファイルID */
    @Id
    @GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
    @Column(name = "EXCEL_FILE_ID", precision = 19, nullable = false, unique = true)
    public Long excelFileId;

    /** ファイルパス */
    @Column(name = "FILE_PATH", length = 255, nullable = false, unique = false)
    public String filePath;

    /** ファイルデータ */
    @Lob
    @Column(name = "FILE_DATA", length = 16777215, nullable = false, unique = false)
    public String fileData;
}
