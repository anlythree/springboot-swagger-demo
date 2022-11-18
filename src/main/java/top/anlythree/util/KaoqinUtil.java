package top.anlythree.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import top.anlythree.util.mo.DakaMo;
import top.anlythree.util.mo.KaoqinMo;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @DATE: 2022/11/18
 * @USER: anlythree
 */
public class KaoqinUtil {

    private static String dakaBaseLocation = "/Users/anlythree/Documents/leadeon/考勤统计/打卡数据（21年12月——22年11月）/5月——11月原始数据/";

    private static String jiabanBaseLocation = "/Users/anlythree/Documents/leadeon/考勤统计/上传研发云周报（二季度至今）";

    public static Map<String,String> xingqiMap = new HashMap<>(8);

    static {
        xingqiMap.put("MONDAY","一");
        xingqiMap.put("TUESDAY","二");
        xingqiMap.put("WEDNESDAY","三");
        xingqiMap.put("THURSDAY","四");
        xingqiMap.put("FRIDAY","五");
        xingqiMap.put("SATURDAY","六");
        xingqiMap.put("SUNDAY","日");
    }

    /**
     * 不考虑加班导出！！！
     * @param yearNum
     * @param monthNum
     * @param name
     * @return
     * @throws Exception
     */
    public static void createDefaultDataByYearAndMonthAndName(HSSFWorkbook workBook,String yearNum, String monthNum, String name) throws Exception{
        List<DakaMo> allDakaDataByYearAndMonthAndName = getAllDakaDataByYearAndMonthAndName(yearNum, monthNum, name);
        System.out.println("转换成考勤数据开始");
        List<KaoqinMo> kaoqinMos = convertDakaToKaoqin(allDakaDataByYearAndMonthAndName);
        System.out.println("转换成考勤数据结束");
        System.out.println("保存操作开始");
        PoiUtil.defaultExport(workBook,"22-" + monthNum, kaoqinMos);
        System.out.println("保存操作结束");
    }

    public static List<KaoqinMo> convertDakaToKaoqin(List<DakaMo> dakaMoList){
        List<String> riqi = new ArrayList<>();
        List<KaoqinMo> kaoqinMoList = new ArrayList<>();
        for (int i = 0; i < dakaMoList.size(); i++) {
            DakaMo dakaMo = dakaMoList.get(i);
            KaoqinMo kaoqinMo = new KaoqinMo(dakaMo);
            if(!riqi.contains(kaoqinMo.getKaoqinDate())){
                kaoqinMoList.add(kaoqinMo);
                riqi.add(kaoqinMo.getKaoqinDate());
            }
            if(i < dakaMoList.size()-1){
                DakaMo dakaMoNext = dakaMoList.get(i + 1);
                LocalDateTime localDateTime = TimeUtil.stringToTime(dakaMo.getDakaTime());
                LocalDateTime localDateTimeNext = TimeUtil.stringToTime(dakaMoNext.getDakaTime());
                int dayNum = localDateTimeNext.getDayOfMonth() - localDateTime.getDayOfMonth();
                for (int o = 1;o< dayNum;o++){
                    KaoqinMo kaoqinMoi = new KaoqinMo(dakaMo, o);
                    if(!riqi.contains(kaoqinMoi.getKaoqinDate())){
                        kaoqinMoList.add(kaoqinMoi);
                        riqi.add(kaoqinMo.getKaoqinDate());
                    }
                }
            }
        }
        return kaoqinMoList;
    }

    public static List<DakaMo> getAllDakaDataByYearAndMonthAndName(String yearNum, String monthNum, String name) throws Exception{
        System.out.println("获取所有【"+name+"在"+yearNum+monthNum+"】的打卡信息中……");
        List<DakaMo> allData = new ArrayList<>();
        List<String> fileByYearAndMonth = getDakaFileByYearAndMonth(dakaBaseLocation, yearNum, monthNum);
        for (String filePath : fileByYearAndMonth) {
            List<DakaMo> dakaMoList = PoiUtil.defaultImportReturnObj(filePath, new DakaMo().getColumNameList(), DakaMo.class);
            for (DakaMo dakaMo : dakaMoList) {
                if(dakaMo.getName().contains(name)){
                    allData.add(dakaMo);
                }
            }
        }
        System.out.println("获取所有【"+name+"在"+yearNum+monthNum+"】的打卡信息结束");
        return allData;
    }

    /**
     *  根据文件夹位置，年月获取符合条件的excel文件路径
     * @param yearNum
     * @param monthNum
     * @return
     */
    public static List<String> getDakaFileByYearAndMonth(String dirPath, String yearNum, String monthNum){
        List<String> filePathList = new ArrayList<>();
        HashMap<String, String> fileMap = new HashMap<>();
        File file = new File(dirPath);
        getFile(file,fileMap);
        for (String filePath : fileMap.keySet()) {
            String fileName = fileMap.get(filePath);
            if(fileName.contains(yearNum+monthNum)){
                filePathList.add(filePath);
            }
        }
        return filePathList;
    }

    public static List<DakaMo> getAllJiabanDataByYearAndMonthAndName(String yearNum, String monthNum, String name) throws Exception{
        System.out.println("获取所有【"+name+"在"+yearNum+monthNum+"】的加班信息中……");
        List<DakaMo> allData = new ArrayList<>();
        List<String> fileByYearAndMonth = getDakaFileByYearAndMonth(jiabanBaseLocation, yearNum, monthNum);
        for (String filePath : fileByYearAndMonth) {
            List<DakaMo> dakaMoList = PoiUtil.defaultImportReturnObj(filePath, new DakaMo().getColumNameList(), DakaMo.class);
            for (DakaMo dakaMo : dakaMoList) {
                if(dakaMo.getName().contains(name)){
                    allData.add(dakaMo);
                }
            }
        }
        System.out.println("获取所有【"+name+"在"+yearNum+monthNum+"】的加班信息中……");
        return allData;
    }

    /**
     *  根据文件夹位置，年月获取符合条件的excel文件路径
     * @param yearNum
     * @param monthNum
     * @return
     */
    public static List<String> getJiaBanFileByYearAndMonth(String dirPath, String yearNum, String monthNum){
        List<String> filePathList = new ArrayList<>();
        HashMap<String, String> fileMap = new HashMap<>();
        File file = new File(dirPath);
        getFile(file,fileMap);
        for (String filePath : fileMap.keySet()) {
            String fileName = fileMap.get(filePath);
            if(fileName.contains(yearNum+monthNum)){
                filePathList.add(filePath);
            }
        }
        return filePathList;
    }





    public static void getFile (File file, HashMap<String,String> map){
        for(File f : Objects.requireNonNull(file.listFiles())){
            if(f.isDirectory()){
                getFile( f, map);
            } else {
                map.put(f.getAbsolutePath(),f.getName());
            }
        }
    }

}
