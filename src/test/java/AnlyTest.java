import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import top.anlythree.util.KaoqinUtil;
import top.anlythree.util.PoiUtil;
import top.anlythree.util.mo.DakaMo;
import top.anlythree.util.mo.KaoqinMo;

import java.util.List;
import java.util.Map;

/**
 * @DATE: 2022/11/18
 * @USER: anlythree
 */
public class AnlyTest {

    @Test
    public void test() throws Exception{

        List<Map<String, Object>> maps =
                PoiUtil.defaultImport("/Users/anlythree/Documents/leadeon/考勤统计/打卡数据（21年12月——22年11月）/5月——11月原始数据/中国移动一级手厅_原始记录表_20220501-20220531.xlsx",
                        new DakaMo().getColumNameList());
        System.out.println(maps.get(0));
    }

    @Test
    public void test1() throws Exception{
        List<DakaMo> wangliList = KaoqinUtil.getAllDakaDataByYearAndMonthAndName("2022", "06", "王力");
        for (DakaMo dakaMo : wangliList) {
            System.out.println(dakaMo);
        }

    }

    @Test
    public void test2() throws Exception{
        HSSFWorkbook workBook = PoiUtil.getWorkBook("/Users/anlythree/Documents/wangli-kaoqin.xlsx");
        KaoqinUtil.createDefaultDataByYearAndMonthAndName(workBook,"2022", "06", "王力");
        KaoqinUtil.createDefaultDataByYearAndMonthAndName(workBook,"2022", "07", "王力");
        KaoqinUtil.createDefaultDataByYearAndMonthAndName(workBook,"2022", "08", "王力");
        KaoqinUtil.createDefaultDataByYearAndMonthAndName(workBook,"2022", "09", "王力");
        KaoqinUtil.createDefaultDataByYearAndMonthAndName(workBook,"2022", "10", "王力");
        KaoqinUtil.createDefaultDataByYearAndMonthAndName(workBook,"2022", "11", "王力");
        PoiUtil.updateFile("/Users/anlythree/Documents/wangli-kaoqin.xlsx",workBook);
    }

}
