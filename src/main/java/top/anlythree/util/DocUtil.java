package top.anlythree.util;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;
import org.junit.Test;
import org.springframework.util.StringUtils;
import top.anlythree.vo.AreaInfoVo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
     * 获取文档列表
     *
     * @param clazz 目标类的class对象
     * @return 文档注释
     */
    public static List<FieldVO> execute(Class<?> clazz) {
        ClassDoc classDoc = getClassDoc(clazz);
        // 获取属性名称和注释
        if(classDoc == null){
            System.out.println("获取不到classDoc对象");
            return Collections.emptyList();
        }
        FieldDoc[] fields = classDoc.fields(false);

        List<FieldVO> fieldVOList = new ArrayList<>(fields.length);

        for (FieldDoc field : fields) {
            fieldVOList.add(new FieldVO(field.name(), field.type().typeName(), field.commentText()));
        }
        return fieldVOList;
    }

    /**
     * 根据class获取java文件绝对路径
     * @param path 项目内部地址
     * @param clazz
     * @return
     */
    private static String getFilePathByClass(Class<?> clazz,String path){
        if(StringUtils.isEmpty(path)){
            path = "/src/main/java/";
        }
        String objPath = Objects.requireNonNull(clazz.getResource("/")).getPath();
        String classPath = StringUtils.replace(clazz.getName(), ".", "/");
        return objPath.substring(0,objPath.indexOf("/target"))+path+classPath+".java";
    }

    @Test
    public void test1(){
        System.out.println(getFilePathByClass(AreaInfoVo.class,null));
    }

    /**
     * 根据class获取classDoc
     * @param clazz
     * @return
     */
    public static ClassDoc getClassDoc(Class<?> clazz){
        Main.execute(new String[]{"-doclet", DocUtil.class.getName(), "-docletpath",
                DocUtil.class.getResource("/").getPath(), "-encoding", "utf-8", getFilePathByClass(clazz,null)});

        ClassDoc[] classes = rootDoc.classes();

        if (classes == null || classes.length == 0) {
            return null;
        }
        return classes[0];
    }

    public static void main(String[] args) {
        List<FieldVO> execute = DocUtil.execute(AreaInfoVo.class);
        System.out.println(execute);
    }


}
