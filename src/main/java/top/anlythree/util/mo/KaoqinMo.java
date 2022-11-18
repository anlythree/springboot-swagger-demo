package top.anlythree.util.mo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import top.anlythree.anno.ExcelColumn;
import top.anlythree.util.KaoqinUtil;
import top.anlythree.util.TimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * @DATE: 2022/11/18
 * @USER: anlythree
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaoqinMo {

    Random random=new Random();

    @ExcelColumn(title = "姓名",fieldNames = "name")
    private String name;

    @ExcelColumn(title = "考勤组",fieldNames = "kaoqinGroup")
    private String kaoqinGroup;

    @ExcelColumn(title = "部门",fieldNames = "bumen")
    private String bumen;

    @ExcelColumn(title = "职位",fieldNames = "zhiwei")
    private String zhiwei;

    @ExcelColumn(title = "考勤日期",fieldNames = "kaoqinDate")
    private String kaoqinDate;

    @ExcelColumn(title = "上班打卡时间",fieldNames = "shangbandakashijian")
    private String shangbandakashijian;

    @ExcelColumn(title = "下班打卡时间",fieldNames = "xiabandakashijian")
    private String xiabandakashijian;

    @ExcelColumn(title = "打卡地址",fieldNames = "dakadizhi")
    private String dakadizhi;

    public KaoqinMo(DakaMo dakaMo) {
        BeanUtils.copyProperties(dakaMo,this);
        LocalDateTime localDateTime = TimeUtil.stringToTime(dakaMo.getDakaTime().trim());
        LocalDate localDate = TimeUtil.timeToDate(localDateTime);
        LocalDateTime localDateTime1 = TimeUtil.dateToTime(localDate);
        LocalDateTime shangban = localDateTime1.plusHours(8).plusMinutes(21 + random.nextInt(30));
        LocalDateTime xiaban = localDateTime1.plusHours(18).plusMinutes(13 + random.nextInt(30));
        setShangbandakashijian(TimeUtil.timeToString(shangban));
        setXiabandakashijian(TimeUtil.timeToString(xiaban));
        String dakaLocation = TimeUtil.timeInterval(TimeUtil.stringToTime("2022-08-10 00:00:00"),localDateTime).isNegative() ? "永威·时代中心":"西子联合控股";
        setDakadizhi(dakaLocation);
        setZhiwei("研发工程师");
        setKaoqinGroup("一级手厅APP");
    }

    public KaoqinMo(DakaMo dakaMo,Integer dayNum) {
        BeanUtils.copyProperties(dakaMo,this);
        LocalDateTime localDateTime = TimeUtil.stringToTime(dakaMo.getKaoqinTime().trim());
        LocalDate localDate = TimeUtil.timeToDate(localDateTime);
        setZhiwei("研发工程师");
        setKaoqinGroup("一级手厅APP");
        // 延后天数
        LocalDate localDate1 = localDate.plusDays(dayNum);
        String dayOfWeek = TimeUtil.getDayOfWeek(localDate1);

        setKaoqinDate(TimeUtil.dateToString(localDate1) +" 星期"+ KaoqinUtil.xingqiMap.get(dayOfWeek));
        setShangbandakashijian(null);
        setXiabandakashijian(null);
        setDakadizhi(null);
    }
}
