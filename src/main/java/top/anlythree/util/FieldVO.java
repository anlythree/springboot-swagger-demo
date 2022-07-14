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
    private String fieldType;

    /**
     * 属性注释
     */
    private String describe;

    /**
     * 该属性对应的类的Doc信息
     */
    private List<FieldVO> fieldVOList;

    public FieldVO(String fieldName, String fieldType, String describe) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.describe = describe;
    }
}
