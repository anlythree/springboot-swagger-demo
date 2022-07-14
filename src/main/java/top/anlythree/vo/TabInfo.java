package top.anlythree.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TabInfo{

    /**
     * tabId
     */
    private String tabId;

    /**
     * tab名称
     */
    private String tabName;

    /**
     * 是否配置标签
     */
    private String isLabel;

    /**
     * 资费编码
     */
    private String chargesNo;

    /**
     * 区域id（冗余）
     */
    private String areaId;

    /**
     * tab 标签页icon URL
     */
    private String tabLabelPageUrl;

    /**
     * 模版列表
     */
    private List<ModuleInfo> moduleInfoList;
}
