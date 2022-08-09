package top.anlythree.util;

import com.alibaba.fastjson.JSON;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javadoc.ClassDocImpl;
import com.sun.tools.javadoc.Main;
import org.assertj.core.util.Maps;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.util.StringUtils;
import top.anlythree.vo.AreaInfoVo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Description:
 *
 * @author jack
 * @date 2021/7/13 5:10 下午
 */
public class DocUtil {

    /**
     * 会自动注入
     */
    private static RootDoc rootDoc;

    /**
     * 会自动调用这个方法
     *
     * @param root root
     * @return true
     */
    public static boolean start(RootDoc root) {
        rootDoc = root;
        return true;
    }

    /**
     * 通过class对象获取字段列表（无备注信息）
     *
     * @param clazz 目标类
     * @return
     */
    public static List<FieldVO> getFieldListByClass(Class<?> clazz) {
        List<FieldVO> fieldLeafList = new ArrayList<>();
        // 获取备注map
        Map<String, String> fieldCommentMapByClassDoc = getFieldCommentMapByClassDoc(clazz);
        // 如果是可获取的对象类型
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            FieldVO fieldVO;
            // 叶子属性Class
            Class<?> leafClazz = field.getType();
            if(isSimpleType(leafClazz)){
                // 基础属性
                fieldVO = new FieldVO(field.getName(),
                        field.getType(),
                        fieldCommentMapByClassDoc.get(field.getName()));
            }else if(leafClazz.isArray() || Collection.class.isAssignableFrom(leafClazz)){
                // 数组或列表类型
                leafClazz = leafClazz.getComponentType() == null ? getNorClassByClass(leafClazz):leafClazz.getComponentType();
                fieldVO = new FieldVO(field.getName(),
                        field.getType(),
                        fieldCommentMapByClassDoc.get(field.getName()),
                        true,
                        getFieldListByClass(leafClazz));
            }else {
                // 对象类型
                fieldVO = new FieldVO(field.getName(),
                        field.getType(),
                        fieldCommentMapByClassDoc.get(field.getName()),
                        false,
                        getFieldListByClass(leafClazz));
            }
            fieldLeafList.add(fieldVO);
        }
        return fieldLeafList;
    }

    /**
     * 判断是否为普通类型（基础数据类型或其包装类）
     * @param clazz
     * @return
     */
    public static boolean isSimpleType(Class<?> clazz){
        return isCommonDataType(clazz) || isWrapClass(clazz);
    }

    /**
     * 判断是否是基础数据类型，即 int,double,long等类似格式
     */
    public static boolean isCommonDataType(Class<?> clazz){
        return clazz.isPrimitive();
    }

    /**
     * 判断是否是基础数据类型的包装类型
     *
     * @param clazz
     * @return
     */
    public static boolean isWrapClass(Class<?> clazz) {
        try {
            return clazz == String.class || ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取List范性
     * @param clazz
     * @return
     */
    public static  Class<?> getNorClassByClass(Class<?> clazz) {
        return (Class<?>) (((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    /**
     * 获取文档列表
     *
     * @param clazz 目标类的class对象
     * @return 文档注释
     */
    public static Map<String, String> getFieldCommentMapByClassDoc(Class<?> clazz) {
        HashMap<String, String> fieldCommentMap = new HashMap<>();
        ClassDoc classDoc = getClassDocByClass(clazz);
        // 获取属性名称和注释
        if (classDoc == null) {
            System.out.println("获取不到classDoc对象");
            return fieldCommentMap;
        }

        for (FieldDoc field : classDoc.fields(false)) {
            fieldCommentMap.put(field.name(), field.commentText());
        }
        return fieldCommentMap;
    }

    /**
     * 根据class获取classDoc
     *
     * @param clazz
     * @return
     */
    public static ClassDoc getClassDocByClass(Class<?> clazz) {
        Main.execute(new String[]{"-doclet", DocUtil.class.getName(), "-docletpath",
                DocUtil.class.getResource("/").getPath(), "-encoding", "utf-8", getFilePathByClass(clazz, null)});
        ClassDoc[] classes = rootDoc.classes();
        if (classes == null || classes.length == 0) {
            return null;
        }
        for (ClassDoc aClass : classes) {
            if (Objects.equals(clazz.getName(), classes[0].toString())) {
                return aClass;
            }
        }
        return null;
    }

    /**
     * 根据class获取java文件绝对路径
     *
     * @param path  项目内部地址
     * @param clazz
     * @return
     */
    private static String getFilePathByClass(Class<?> clazz, String path) {
        if (StringUtils.isEmpty(path)) {
            path = "/src/main/java/";
        }
        String objPath = Objects.requireNonNull(clazz.getResource("/")).getPath();
        return objPath.substring(0, objPath.indexOf("/target")) + path + StringUtils.replace(clazz.getName(), ".", "/") + ".java";
    }


    public static void main(String[] args) {
        List<FieldVO> fieldListByClass = DocUtil.getFieldListByClass(AreaInfoVo.class);
        System.out.println(JSON.toJSONString(fieldListByClass));
    }


}
