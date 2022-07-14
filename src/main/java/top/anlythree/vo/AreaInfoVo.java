package top.anlythree.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class AreaInfoVo {

    /**
     * 区域类型（2022-07-07）
     */
    public static final String ICON_AREA_TYPE = "1";
    public static final String BANNER_AREA_TYPE = "2";
    public static final String TAB_AREA_TYPE = "3";
    public static final String SELECT_NUM_AREA_TYPE = "4";
    public static final String BEAUTY_NUM_AREA_TYPE = "5";
    public static final String CUSTOM_AREA_TYPE = "6";

    /**
     * tab区域区分标识
     */
    public static final String TAB_AREA_TAG1 = "1";
    public static final String TAB_AREA_TAG2 = "2";

    /**
     * 区域id
     */
    private String areaId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域类型
     */
    private String areaType;

    /**
     * 区域区分标识
     */
    private String areaTypeTag;

    /**
     * 区域背景图url
     */
    private String areaBackgroundUrl;

    /**
     * 是否在app显示 0：否 1：是
     */
    private String isAppShow;

    /**
     * 区域名称是否显示
     * 0：显示 1：不显示
     */
    private String fontShow;

    /**
     * 显示位置
     * 1.居左;2.居中
     */
    private String showLocation;

    /**
     * 区域背景图片是否显示
     * 0：显示 1：不显示
     */
    private String areaBkgPicShow;

    /**
     * 位置选择
     */
    private String locationChoose;

    /**
     * Module列表
     */
    private List<ModuleInfo> moduleInfoList;

    /**
     * tab列表
     */
    private List<TabInfo> tabInfoList;

}
