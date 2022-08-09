package top.anlythree.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldVO {
    /**
     * 属性名称
     */
    private String fieldName;

    /**
     * 属性类型
     */
    private Class<?> fieldType;

    /**
     * 属性注释
     */
    private String describe;

    /**
     * 是否为列表
     */
    private Boolean isList;

    /**
     * 叶子属性列表
     */
    private List<FieldVO> leafField;

    public FieldVO(String fieldName, Class<?> fieldType, String describe) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.describe = describe;
        this.isList = false;
    }
}
