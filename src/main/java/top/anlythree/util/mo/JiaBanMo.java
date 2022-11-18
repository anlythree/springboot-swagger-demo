package top.anlythree.util.mo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anlythree.anno.ExcelColumn;

/**
 * @DATE: 2022/11/18
 * @USER: anlythree
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiaBanMo {

    @ExcelColumn(title = "姓名",fieldNames = "name")
    private String name;

    @ExcelColumn(title = "账号",fieldNames = "zhanghao")
    private String zhanghao;

    @ExcelColumn(title = "联系方式",fieldNames = "phone")
    private String phone;

    @ExcelColumn(title = "本周工作内容",fieldNames = "benzhougongzuoneirong")
    private String benzhougongzuoneirong;

    @ExcelColumn(title = "正常工时（人天）",fieldNames = "zhengchanggongshi")
    private String zhengchanggongshi;

    @ExcelColumn(title = "工作日加班工时（人天）",fieldNames = "gongzuorijiabangongshi")
    private String gongzuorijiabangongshi;

    @ExcelColumn(title = "备注",fieldNames = "beizhu")
    private String beizhu;

}
