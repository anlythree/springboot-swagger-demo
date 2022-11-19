package top.anlythree.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @DATE: 2022/11/18
 * @USER: anlythree
 */
public class TimeUtil {

    /**
     * 通用日期格式
     */
    private final static DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 通用时间格式
     */
    private final static DateTimeFormatter FMT_DATE_TIME2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static DateTimeFormatter FMT_DATE_TIME = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");

    private final static DateTimeFormatter FMT_DATE_TIME4 = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");

    private final static DateTimeFormatter FMT_DATE_TIME3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final static DateTimeFormatter FMT_DATE2 = DateTimeFormatter.ofPattern("yy-MM-dd");

    /**
     * 时间(日期)格式转成String类型
     * @param localDate
     * @return
     */
    public static String dateToString(LocalDate localDate){
        return localDate.format(FMT_DATE2);
    }

    /**
     * 时间(时间)格式转成String类型
     * @param localDateTime
     * @return
     */
    public static String timeToString(LocalDateTime localDateTime){
        return localDateTime.format(FMT_DATE_TIME);
    }

    /**
     * String格式转成LocalDate类型
     * @param localDate
     * @return
     */
    public static LocalDate stringToDate (String localDate){
        return LocalDate.parse(localDate, FMT_DATE);
    }

    /**
     * String格式转成LocalDateTime类型
     * @param localDateTime
     * @return
     */
    public static LocalDateTime stringToTime (String localDateTime){
        try {
            return LocalDateTime.parse(localDateTime,FMT_DATE_TIME);
        }catch (DateTimeParseException e){
            try {
                return LocalDateTime.parse(localDateTime,FMT_DATE_TIME2);
            }catch (DateTimeParseException e1){
                try {
                    return LocalDateTime.parse(localDateTime,FMT_DATE_TIME3);
                }catch (DateTimeParseException e2){
                    return LocalDateTime.parse(localDateTime,FMT_DATE_TIME4);
                }
            }
        }
    }

    /**
     * 时分格式转成LocalDateTime类型
     * @param localDateTime
     * @return
     */
    public static LocalDateTime onlyTimeStrToTime(String localDateTime){
        String dateStr = TimeUtil.dateToString(LocalDate.now());
        return LocalDateTime.parse(dateStr+" "+localDateTime+":00",FMT_DATE_TIME);
    }

    /**
     * LocalDate格式转成LocalDateTime格式，时间区域取值 00：00：00
     * @param localDate
     * @return
     */
    public static LocalDateTime dateToTime(LocalDate localDate){
        return stringToTime(dateToString(localDate)+" 00:00:00");
    }

    /**
     * LocalDateTime格式转成LocalDate格式，时间格式直接去掉
     * @param localDateTime
     * @return
     */
    public static LocalDate timeToDate(LocalDateTime localDateTime){
        return localDateTime.toLocalDate();
    }

    /**
     *  根据时间格式分离出日期和时间，（时间只到分钟，不到秒）
     * @param dateTime
     * @return
     */
    public static String[] getDateAndTimeByTime(LocalDateTime dateTime){
        String time = timeToString(dateTime);
        String[] s = time.split(" ");
        s[1] = s[1].substring(0,s[1].lastIndexOf(":"));
        return s;
    }

    /**
     * 返回当前时间到传入时间之间的时间间隔
     * 返回值的isNegative() 返回当前时间是否早于开始时间
     *
     * @param startTime
     * @return
     */
    public static Duration timeInterval(LocalDateTime startTime) {
        return timeInterval(startTime,LocalDateTime.now());
    }

    /**
     * 返回当前时间到传入时间之间的时间间隔
     * 返回值的isNegative() 返回结束时间是否早于开始时间
     *
     * @param startTime
     * @return
     */
    public static Duration timeInterval(LocalDateTime startTime,LocalDateTime endTime) {
        return Duration.between(startTime,endTime);
    }

    /**
     * LocalDateTime转时间戳
     * @param localDateTime
     * @return
     */
    public static Long timeToStamp(LocalDateTime localDateTime){
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 时间戳转LocalDateTime
     * @param timeStamp
     * @return
     */
    public static LocalDateTime stampToTime(long timeStamp){
        return LocalDateTime.ofEpochSecond(timeStamp /1000,0, ZoneOffset.ofHours(8));
    }

    /**
     * 获取星期，它的值为指定日期在当周中的星期
     * @param date 指定日期
     * @return 星期
     */
    public static String getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().toString();
    }
}
