package top.anlythree.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleInfo {

    /**
     * 模板ID
     */
    private String moduleId;

    /**
     * 模板名称
     */
    private String moduleName;

    /**
     * 模版位置
     */
    private String moduleLocation;

    /**
     * 模版位置图片长度
     */
    private String picLength;

    /**
     * 模版位置图片宽度
     */
    private String picWidth;

    /**
     * 模版位置图片大小
     */
    private String picSize;

    /**
     * 活动列表
     */
    private List<MarketingInfo> marketingInfoList;

}
