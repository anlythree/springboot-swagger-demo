package top.anlythree.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @DATE: 2022/11/18
 * @USER: anlythree
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
     String title() default "";

     String fieldNames() default "";
}
