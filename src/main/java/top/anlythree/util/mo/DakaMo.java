package top.anlythree.util.mo;

import lombok.Data;
import top.anlythree.anno.ExcelColumn;

/**
 * @DATE: 2022/11/18
 * @USER: anlythree
 */
@Data
public class DakaMo {
    /**
     * 姓名
     */
    @ExcelColumn(title = "姓名",fieldNames = "name")
    private String name;
    /**
     * 考勤组
     */
    @ExcelColumn(title = "考勤组",fieldNames = "kaoqinGroup")
    private String kaoqinGroup;

    /**
     * 部门
     */
    @ExcelColumn(title = "部门",fieldNames = "bumen")
    private String bumen;
    /**
     * 工号
     */
    @ExcelColumn(title = "工号",fieldNames = "gonghao")
    private String gonghao;
    /**
     * 职位
     */
    @ExcelColumn(title = "职位",fieldNames = "zhiwei")
    private String zhiwei;

    /**
     * userid
     */
    @ExcelColumn(title = "UserId",fieldNames = "userId")
    private String userId;
    /**
     * 考勤日期
     */
    @ExcelColumn(title = "考勤日期",fieldNames = "kaoqinDate")
    private String kaoqinDate;
    /**
     * 考勤时间
     */
    @ExcelColumn(title = "考勤时间",fieldNames = "kaoqinTime")
    private String kaoqinTime;
    /**
     * 打卡时间
     */
    @ExcelColumn(title = "打卡时间",fieldNames = "dakaTime")
    private String dakaTime;
    /**
     * 打卡结果
     */
    @ExcelColumn(title = "打卡结果",fieldNames = "dakajieguo")
    private String dakajieguo;
    /**
     * 打卡地址
     */
    @ExcelColumn(title = "打卡地址",fieldNames = "dakadizhi")
    private String dakadizhi;

    /**
     * 打卡备注
     */
    @ExcelColumn(title = "打卡备注",fieldNames = "dakabeizhu")
    private String dakabeizhu;
    /**
     * 异常打卡原因
     */
    @ExcelColumn(title = "异常打卡原因",fieldNames = "yichangdakayuanyin")
    private String yichangdakayuanyin;


    /**
     * 打卡图片1
     */
    @ExcelColumn(title = "打卡图片1",fieldNames = "dakatupian1")
    private String dakatupian1;

    /**
     *打卡图片2
     */
    @ExcelColumn(title = "打卡图片2",fieldNames = "dakatupian2")
    private String dakatupian2;
    /**
     *打卡设备
     */
    @ExcelColumn(title = "打卡设备",fieldNames = "dakashebei")
    private String dakashebei;
    /**
     *管理员修改备注
     */
    @ExcelColumn(title = "管理员修改备注",fieldNames = "guanliyuanxiugaibeizhu")
    private String guanliyuanxiugaibeizhu;
    /**
     *管理员修改备注图片1
     */
    @ExcelColumn(title = "管理员修改备注图片1",fieldNames = "guanliyuanxiugaibeizhutupian1")
    private String guanliyuanxiugaibeizhutupian1;
    /**
     *管理员修改备注图片2
     */
    @ExcelColumn(title = "管理员修改备注图片2",fieldNames = "guanliyuanxiugaibeizhutupian2")
    private String guanliyuanxiugaibeizhutupian2;
    /**
     *管理员修改备注图片3
     */
    @ExcelColumn(title = "管理员修改备注图片3",fieldNames = "guanliyuanxiugaibeizhutupian3")
    private String guanliyuanxiugaibeizhutupian3;

    private String[] columNameList = new String[]{"name","kaoqinGroup","bumen","gonghao","userId","zhiwei","kaoqinDate","kaoqinTime","dakaTime",
            "dakajieguo","dakadizhi","dakabeizhu","yichangdakayuanyin","dakatupian1","dakatupian2","dakashebei",
            "guanliyuanxiugaibeizhu","guanliyuanxiugaibeizhutupian1","guanliyuanxiugaibeizhutupian2","guanliyuanxiugaibeizhutupian3"};
}
