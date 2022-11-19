package top.anlythree.util.mo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anlythree.anno.ExcelColumn;

/**
 * @DATE: 2022/11/19
 * @USER: anlythree
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZhuomoJiaBan {

    @ExcelColumn(title = "姓名",fieldNames = "name")
    private String name;

    @ExcelColumn(title = "账号",fieldNames = "zhanghao")
    private String zhanghao;

    @ExcelColumn(title = "联系方式",fieldNames = "phone")
    private String phone;

    @ExcelColumn(title = "加班日期",fieldNames = "jiabanriqi")
    private String jiabanriqi;

    @ExcelColumn(title = "计划加班时长（小时）",fieldNames = "jiabanshichang")
    private String jiabanshichang;

    @ExcelColumn(title = "计划加班工时（人天）",fieldNames = "jihuajiabangongshi")
    private String jihuajiabangongshi;

    @ExcelColumn(title = "加班内容",fieldNames = "jiabanneirong")
    private String jiabanneirong;

    @ExcelColumn(title = "备注",fieldNames = "beizhu")
    private String beizhu;

    private String[] columNameList = {"name","zhanghao","phone","jiabanriqi","jiabanshichang","jihuajiabangongshi","jiabanneirong","beizhu"};
}
