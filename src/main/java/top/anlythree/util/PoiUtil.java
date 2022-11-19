package top.anlythree.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.anlythree.anno.ExcelColumn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 基于poi的excel工具类
 *
 * @author anlythree
 * @time 2022/3/33:50 下午
 */
public class PoiUtil {


    // startOf 导入

    public static <T> List<T> defaultImportReturnObj(String filePath, String[] keys, Class<T> t) throws Exception {
        List<T> tList = new ArrayList<>();
        List<Map<String, Object>> maps = defaultImport(filePath, keys);
        for (Map<String, Object> map : maps) {
            T tConvert = JSONObject.parseObject(JSONObject.toJSONString(map), t);
            tList.add(tConvert);
        }
        return tList;
    }

    /**
     * 导入excel
     *
     * @param filePath 文件物理地址
     * @param keys     字段名称数组 如  ["id", "name", ... ]
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> defaultImport(String filePath, String[] keys) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        if (keys == null) {
            throw new Exception("keys不能为空!");
        }
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            throw new Exception("文件格式有误!");
        }
        //读
        FileInputStream fis = null;
        Workbook workbook = null;
        try {
            fis = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            }

            // 获取第一个工作表信息
            Sheet sheet = workbook.getSheetAt(0);
            //获得数据的总行数
            int totalRowNum = sheet.getLastRowNum();

            // 获得表头
            Integer startNum = 0;
            if(filePath.contains("_原始记录表_")){
                startNum = 2;
            }
            // todo-anlythree 从哪一行开始读取表头信息
            Row rowHead = sheet.getRow(startNum);
            // 获得表头总列数9
            int cols = rowHead.getPhysicalNumberOfCells();

            if (keys.length != cols) {
                System.out.println("！！！！！！！！！！！！！！！！！！！！"+filePath+"第"+rowHead.getRowNum()+"行未扫描！！！！！！！！！！！！！！！！！！！！！");
                throw new Exception("传入的key数组长度与表头长度不一致!");
            }
            Row row = null;
            Cell cell = null;
            Object value = null;
            // 遍历所有行
            for (int i = 1; i <= totalRowNum; i++) {
                // 清空数据，避免遍历时读取上一次遍历数据
                row = null;
                cell = null;
                value = null;
                map = new HashMap<String, Object>();

                row = sheet.getRow(i);
                if (null == row) continue;    // 若该行第一列为空，则默认认为该行就是空行
                // 遍历该行所有列
                for (short j = 0; j < cols; j++) {
                    cell = row.getCell(j);
                    if (null == cell) continue;    // 为空时，下一列
                    // 根据poi返回的类型，做相应的get处理
                    if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                        value = cell.getStringCellValue();
                    } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                        value = cell.getNumericCellValue();

                        // 由于日期类型格式也被认为是数值型，此处判断是否是日期的格式，若时，则读取为日期类型
                        if (cell.getCellStyle().getDataFormat() > 0) {
                            value = cell.getDateCellValue();
                        }
                    } else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                        value = cell.getBooleanCellValue();
                    } else if (Cell.CELL_TYPE_BLANK == cell.getCellType()) {
                        value = cell.getDateCellValue();
                    } else {
                        // todo-anlythree 之后放开
//                        throw new Exception("At row: %s, col: %s, can not discriminate type!");
                    }

                    map.put(keys[j], value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            throw new Exception("导入表格出错!", e);
        } finally {
            //关闭流
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    // endOf 导入

    // startOf 导出

    /**
     * 导出最普通的 excel表格至poi对象
     *
     * @param sheetName sheet名
     * @param list      数据
     */
    public static <T> HSSFWorkbook defaultExport(String sheetName, List<T> list) throws Exception {
        Map<String, String[]> titlesAndFieldNames = getTitlesAndFieldNames(list.get(0).getClass());
        return defaultExport(sheetName, list, titlesAndFieldNames.get("titles"), titlesAndFieldNames.get("fieldNames"));
    }

    public static <T> HSSFWorkbook defaultExport(HSSFWorkbook wb,String sheetName, List<T> list) throws Exception {
        Map<String, String[]> titlesAndFieldNames = getTitlesAndFieldNames(list.get(0).getClass());
        return defaultExport1(wb,sheetName, list, titlesAndFieldNames.get("titles"), titlesAndFieldNames.get("fieldNames"));
    }

    /**
     * 导出最普通的 excel表格至流
     *
     * @param sheetName    sheet名
     * @param list         数据
     * @param outputStream 导出的流
     */
    public static <T> OutputStream defaultExport(String sheetName, List<T> list, OutputStream outputStream) throws Exception {
        Map<String, String[]> titlesAndFieldNames = getTitlesAndFieldNames(list.get(0).getClass());
        HSSFWorkbook workbook = defaultExport(sheetName, list, titlesAndFieldNames.get("titles"), titlesAndFieldNames.get("fieldNames"));
        workbook.write(outputStream);
        return outputStream;
    }

    /**
     * 导出最普通的 excel表格至文件
     *
     * @param fileNamePath 导出文件名称
     * @param sheetName    sheet名
     * @param list         数据
     */
    public static <T> File defaultExport(String sheetName, List<T> list, String fileNamePath) throws Exception {
        Map<String, String[]> titlesAndFieldNames = getTitlesAndFieldNames(list.get(0).getClass());
        return defaultExport(sheetName, list, titlesAndFieldNames.get("titles"), titlesAndFieldNames.get("fieldNames"), fileNamePath);
    }


//    /**
//     * 设置单元格格式
//     * @param workbook
//     * @param sheetName
//     * @param startRow
//     * @param endRow
//     * @param startCell
//     * @param endCell
//     * @throws Exception
//     */
//    public static void setStyleForCells(XSSFWorkbook workbook, String sheetName, int startRow, int endRow, int startCell, int endCell,
//                                        StyleOption styleOption) throws Exception{
//        Sheet sheet = getSheet(workbook, sheetName);
//        XSSFCellStyle cellStyle;
//        for (int i = startRow; i <= endRow; i++) {
//            Row row = sheet.getRow(i);
//            for (int j = startCell; j <= endCell; j++) {
//                cellStyle = workbook.createCellStyle();
//                //设置格式
//                XSSFFont font = workbook.createFont();
//                if(null != styleOption.getBold()){
//                    font.setBold(styleOption.getBold());
//                }
//                if(StringUtils.isEmpty(styleOption.getFontName())){
//                    font.setFontName("宋体");
//                }else {
//                    font.setFontName(styleOption.getFontName());
//                }
//                font.setFontHeight(styleOption.getFontSize());
//                cellStyle.setFont(font);
//                if(Objects.equals(Boolean.TRUE,styleOption.getFrame())){
//                    cellStyle.setBorderBottom(BorderStyle.THIN);
//                    cellStyle.setBorderLeft(BorderStyle.THIN);
//                    cellStyle.setBorderTop(BorderStyle.THIN);
//                    cellStyle.setBorderRight(BorderStyle.THIN);
//                }
//                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//                if(Objects.equals(Boolean.TRUE,styleOption.getCenter())){
//                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
//                }
//                Cell cell = row.getCell(j);
//                cell.setCellStyle(cellStyle);
//            }
//            row.setHeight(styleOption.getHigh());
//        }
//        //  设置列宽
//        for (int j = startCell; j <= endCell; j++) {
//            if(Objects.equals(Boolean.TRUE,styleOption.getWidthAutoSize())){
//                sheet.autoSizeColumn(j,true);
//                // 获取该列最长字符串长度
//                Integer maxLengthForCell = getMaxLengthForCell(sheet, j, startRow, endRow);
//                sheet.setColumnWidth(j,maxLengthForCell*2*256+10);
//            }else if(null != styleOption.getWide()){
//                sheet.setColumnWidth(j,styleOption.getWide());
//            }
//        }
//    }

    /**
     * 获取这一列最长字符长度
     * @param sheet
     * @param cell
     * @param startRow
     * @param endRow
     * @return
     */
    private static Integer getMaxLengthForCell(Sheet sheet, int cell, int startRow, int endRow){
        Integer maxLength = 0;
        for (int i = startRow; i <=endRow; i++) {
            if(sheet.getRow(i).getCell(cell) != null && sheet.getRow(i).getCell(cell).getStringCellValue() != null){
                int length = sheet.getRow(i).getCell(cell).getStringCellValue().getBytes().length;
                if(length > maxLength){
                    maxLength = length;
                }
            }
        }
        return maxLength;
    }

    /**
     * 设置单元格格式
     * @param workbook
     * @param sheetName
     * @param startRow
     * @param endRow
     * @param startCell
     * @param endCell
     * @param cellStyle
     * @throws Exception
     */
    public static void setStyleForCells(HSSFWorkbook workbook, String sheetName, int startRow, int endRow, int startCell, int endCell, CellStyle cellStyle) throws Exception{
        Sheet sheet = getSheet(workbook, sheetName);
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            for (int j = startCell; j <= endCell; j++) {
                Cell cell = row.getCell(j);
                cell.setCellStyle(cellStyle);
            }
        }
    }



    /**
     * 导出最普通的 excel表格至poi对象
     *
     * @param sheetName  sheet名
     * @param list       数据
     * @param titles     第一行表头标题 数组
     * @param fieldNames 字段名称 数组
     */
    public static <T> HSSFWorkbook defaultExport(String sheetName, List<T> list, String[] titles, String[] fieldNames) throws Exception {
        return defaultExport(new HSSFWorkbook(),sheetName, list, titles, fieldNames);
    }

    /**
     * 导出最普通的 excel表格至poi对象
     *
     * @param sheetName  sheet名
     * @param list       数据
     * @param titles     第一行表头标题 数组
     * @param fieldNames 字段名称 数组
     */
    public static <T> HSSFWorkbook defaultExport1(HSSFWorkbook wb,String sheetName, List<T> list, String[] titles, String[] fieldNames) throws Exception {
        return defaultExport(wb,sheetName, list, titles, fieldNames);
    }

    /**
     * 导出最普通的 excel表格至poi对象
     *
     * @param sheetName  sheet名
     * @param list       数据
     * @param titles     第一行表头标题 数组
     * @param fieldNames 字段名称 数组
     */


    /**
     * 导出最普通的 excel表格至poi对象
     *
     * @param sheetName  sheet名
     * @param list       数据
     * @param titles     第一行表头标题 数组
     * @param fieldNames 字段名称 数组
     */
    public static <T> HSSFWorkbook defaultExport(HSSFWorkbook wb,String sheetName, List<T> list, String[] titles, String[] fieldNames) throws Exception {
        HSSFFont font = wb.createFont();
        HSSFCellStyle cellStyle = wb.createCellStyle();
        font.setFontName("宋体");
        cellStyle.setFont(font);
        HSSFSheet sheet;
        if (sheetName == null) {
            sheetName = "DefaultSheet";
        }
        sheet = wb.createSheet(sheetName);
        //表头
        HSSFRow topRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            fillCellWithValue(topRow.createCell(i), titles[i]);
        }
        String methodNameWithGet = "";
        String methodNameWithIs = "";
        Method method = null;
        T t = null;
        Object ret = null;
        // 遍历生成数据行，通过反射获取字段的get方法
        for (int i = 0; i < list.size(); i++) {
            t = list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            Class<? extends Object> clazz = t.getClass();
            for (int j = 0; j < fieldNames.length; j++) {
                methodNameWithGet = "get" + capitalize(fieldNames[j]);
                methodNameWithIs = "is" + capitalize(fieldNames[j]);
                try {
                    //先用get前缀试，如果获取不到试一下is前缀
                    method = clazz.getDeclaredMethod(methodNameWithGet);
                    if (method == null) {
                        method = clazz.getDeclaredMethod(methodNameWithIs);
                    }

                } catch (NoSuchMethodException e) {    //  不存在该方法，查看父类是否存在。此处只支持一级父类，若想支持更多，建议使用while循环
                    if (null != clazz.getSuperclass()) {
                        method = clazz.getSuperclass().getDeclaredMethod(methodNameWithGet);
                        if (method == null) {
                            method = clazz.getDeclaredMethod(methodNameWithIs);
                        }
                    }
                }
                if (null == method) {
                    throw new Exception(clazz.getName() + " don't have menthod --> " + methodNameWithGet + " or " + methodNameWithIs);
                }
                ret = method.invoke(t);
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                // 对象中没有值就不赋值当前单元格
                if (null == ret) {
                    continue;
                }
                fillCellWithValue(cell, ret + "");
            }
        }
        //自定义表格宽度
        for (int i = 0; i < fieldNames.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return wb;
    }


    /**
     * 导出最普通的 excel表格至流
     *
     * @param sheetName    sheet名
     * @param list         数据
     * @param titles       第一行表头标题 数组
     * @param fieldNames   字段名称 数组
     * @param outputStream 导出的流
     */
    public static <T> OutputStream defaultExport(String sheetName, List<T> list, String[] titles, String[] fieldNames, OutputStream outputStream) throws Exception {
        HSSFWorkbook workbook = defaultExport(sheetName, list, titles, fieldNames);
        workbook.write(outputStream);
        return outputStream;
    }

    /**
     * 导出最普通的 excel表格至文件
     *
     * @param fileNamePath 导出文件名称
     * @param sheetName    sheet名
     * @param list         数据
     * @param titles       第一行表头标题 数组
     * @param fieldNames   字段名称 数组
     */
    public static <T> File defaultExport(String sheetName, List<T> list, String[] titles, String[] fieldNames, String fileNamePath) throws Exception {

        File file = null;

        OutputStream os = null;
        file = new File(fileNamePath);
        try {
            os = new FileOutputStream(file);
            defaultExport(sheetName, list, titles, fieldNames, os);
        } finally {
            if (null != os) {
                os.flush();
                os.close();
            }
        }
        return file;
    }

    // endOf 导出

    /**
     * 把workbook对象保存到文件中
     *
     * @param workbook
     * @param fileNamePath
     * @throws Exception
     */
    public static void exportWorkBookToFile(HSSFWorkbook workbook, String fileNamePath) throws Exception {
        OutputStream os = null;
        File file = new File(fileNamePath);
        try {
            os = new FileOutputStream(file);
            workbook.write(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                os.flush();
                os.close();
            }
        }
    }

    /**
     * 填用value值填充cell小格子
     *
     * @param cell  格子
     * @param value 待填数据
     */
    public static void fillCellWithValue(HSSFWorkbook workbook, String sheetName, int row, int cell, String value) throws Exception {
        HSSFCell cell1 = getCell(workbook, sheetName, row, cell);
        fillCellWithValue(cell1, value);
    }


    /**
     * 填用value值填充cell小格子
     *
     * @param cell  格子
     * @param value 待填数据
     */
    private static void fillCellWithValue(HSSFCell cell, String value) {
        cell.setCellValue(new HSSFRichTextString(value));
    }

    /**
     * 拼接
     *
     * @param str
     * @return
     */
    private static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final char firstChar = str.charAt(0);
        final char newChar = Character.toTitleCase(firstChar);
        if (firstChar == newChar) {
            // already capitalized
            return str;
        }

        char[] newChars = new char[strLen];
        newChars[0] = newChar;
        str.getChars(1, strLen, newChars, 1);
        return String.valueOf(newChars);
    }


    /**
     * 向已有的Excel表格中 插入一行数据，合并单元格
     *
     * @param filePath  文件路径
     * @param theRow    行数
     * @param sheetName sheet名字
     * @param note      文本
     * @param rowHeight 行高
     * @throws Exception
     */
    public static void insertRowsAndMerge(String filePath, int theRow, String sheetName, String note, int cellTotal, Short rowHeight) throws Exception {
        //得到POI对象
        HSSFWorkbook workbook = getWorkBook(filePath);
        //得到sheet
        HSSFSheet sheet = workbook.getSheet(sheetName);
        HSSFRow row = createRow(sheet, theRow, rowHeight, null);
        createCell(row, note);
        //合并单元格
        mergeCells(sheet, theRow, theRow, 0, cellTotal);
        //保存文件
        updateFile(filePath, workbook);
    }

    /**
     * 向已有的Excel表格中 插入一行数据
     *
     * @param theRow    从第几行插入（从0开始）
     * @param sheetName sheet名字
     * @param note      文本
     * @param rowHeight 行高
     * @throws Exception
     */
    public static void insertRows(HSSFWorkbook workbook, int theRow, String sheetName, String note, Short rowHeight, Integer totalCellNum) throws Exception {
        //得到sheet
        HSSFSheet sheet = workbook.getSheet(sheetName);
        HSSFRow row = createRow(sheet, theRow, rowHeight, totalCellNum);
        createCell(row, note);
    }


    /**
     * 找到需要插入的行数，并新建一个POI的row对象
     *
     * @param sheet    sheet对象
     * @param rowIndex 行数
     * @return
     */
    private static HSSFRow createRow(HSSFSheet sheet, Integer rowIndex, Short height, Integer totalCellNum) {
        HSSFRow row = null;
        if (sheet.getRow(rowIndex) != null) {
            int lastRowNo = sheet.getLastRowNum();
            sheet.shiftRows(rowIndex, lastRowNo, 1);
        }
        row = sheet.createRow(rowIndex);
        for (int i = 1; i < totalCellNum; i++) {
            row.createCell(i);
        }
        if (height != null) {
            row.setHeight(height);
        }
        return row;
    }


    /**
     * 补一行
     *
     * @param row  行数
     * @param note 文本
     * @return
     */
    private static HSSFCell createCell(HSSFRow row, String note) {
        HSSFCell cell = row.createCell(0);
        fillCellWithValue(cell, note);
        return cell;
    }

    /**
     * 合并单元格
     *
     * @param sheet     所在的sheet
     * @param startRow  开始行
     * @param endRow    结束行
     * @param startCell 开始列
     * @param endCell   结束列
     */
    public static void mergeCells(Sheet sheet, int startRow, int endRow, int startCell, int endCell) {
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCell, endCell));
    }

    /**
     * 合并单元格
     *
     * @param workbook  workbook
     * @param sheetName sheet名称
     * @param startRow  开始行
     * @param endRow    结束行
     * @param startCell 开始列
     * @param endCell   结束列
     */
    public static void mergeCells(HSSFWorkbook workbook, String sheetName, int startRow, int endRow, int startCell, int endCell) throws Exception {
        getSheet(workbook, sheetName).addMergedRegion(new CellRangeAddress(startRow, endRow, startCell, endCell));
    }

    /**
     * 通过文件路径，获取workBook POI对象
     *
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static HSSFWorkbook getWorkBook(String filePath) throws Exception {
        HSSFWorkbook workbook;
        FileInputStream fis = null;
        File file = new File(filePath);
        try {
            if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
                throw new Exception("文件格式有误!");
            }
            if (file != null) {
                fis = new FileInputStream(file);
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new Exception("文件不存在！");
            }
        } catch (Exception e) {
            throw new Exception("插入数据失败!", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return workbook;
    }

    /**
     * 通过文件路径和sheet名字 获取Sheet对象
     *
     * @param filePath  文件路径
     * @param sheetName sheet名称
     * @return
     * @throws Exception
     */
    public static Sheet getSheet(String filePath, String sheetName) throws Exception {
        HSSFWorkbook workbook = getWorkBook(filePath);
        Sheet sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    /**
     * 通过workbook和sheet名字 获取Sheet对象
     *
     * @param workbook  workbook
     * @param sheetName sheet名称
     * @return
     * @throws Exception
     */
    public static Sheet getSheet(HSSFWorkbook workbook, String sheetName) throws Exception {
        Sheet sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    /**
     * 通过workbook和sheet名字和坐标获取cell对象
     *
     * @param workbook  workbook
     * @param sheetName sheet名称
     * @param sheetName 第几行（从0开始）
     * @param sheetName 第几列（从0开始）
     * @return
     * @throws Exception
     */
    public static HSSFCell getCell(HSSFWorkbook workbook, String sheetName, int row, int cell) throws Exception {
        HSSFSheet sheet = workbook.getSheet(sheetName);
        HSSFRow row1 = sheet.getRow(row);
        HSSFCell cell1 = row1.getCell(cell);
        return cell1;
    }

    /**
     * 通过workbook和sheet名字和坐标获取row对象
     *
     * @param workbook  workbook
     * @param sheetName sheet名称
     * @param sheetName 第几行（从0开始）
     * @return
     * @throws Exception
     */
    public static HSSFRow getCell(HSSFWorkbook workbook, String sheetName, int row) throws Exception {
        HSSFSheet sheet = workbook.getSheet(sheetName);
        HSSFRow row1 = sheet.getRow(row);
        return row1;
    }

    /**
     * 保存workbook到文件，保存文件
     *
     * @param filePath 保存文件路径
     * @param workbook poi对象
     * @throws Exception
     */
    public static void updateFile(String filePath, Workbook workbook) throws Exception {
        //保存
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("成功插入数据！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<AnnoField> getAnnoFieldListByObgList(Class<? extends Object> classO, Class<? extends Annotation> annoClass) {
        List<AnnoField> annoFieldList = new ArrayList();
        for (Field fd : classO.getDeclaredFields()) {
            if (fd.isAnnotationPresent(annoClass)) {
                Annotation d = fd.getAnnotation(annoClass);
                annoFieldList.add(new AnnoField(fd, d));
            }
        }
        return annoFieldList;
    }

    public static Map<String, String[]> getTitlesAndFieldNames(Class<?> c) {
        HashMap<String, String[]> titlesAndFieldNamesMap = Maps.newHashMap();
        List<PoiUtil.AnnoField> annoFieldListByObgList = PoiUtil.getAnnoFieldListByObgList(c, ExcelColumn.class);
        titlesAndFieldNamesMap.put("fieldNames", annoFieldListByObgList.stream().map(f -> f.getField().getName()).toArray(String[]::new));
        titlesAndFieldNamesMap.put("titles", annoFieldListByObgList.stream().map(f -> {
            ExcelColumn annotation = (ExcelColumn) f.getAnnotation();
            return annotation.title();
        }).toArray(String[]::new));
        return titlesAndFieldNamesMap;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnnoField {

        private Field field;

        private Annotation annotation;
    }
}
