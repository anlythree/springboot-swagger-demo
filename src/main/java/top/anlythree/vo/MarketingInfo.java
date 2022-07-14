package top.anlythree.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingInfo {

        /**
         * 活动名称（专属服务时为serviceFunction的值）
         */
        private String activityName;

        /**
         * 图片url
         */
        private String picUrl;

        /**
         * 模板位置选择
         */
        private Integer moduleLocation;

        /**
         * 业务类型:
         * 1：H5页面扩展
         * 4：客户端原生功能
         */
        private Integer busiType;

        /**
         * 跳转URL地址
         */
        private String actionUrl;

        /**
         * 一级手厅内获取插码地址
         */
        private String codeUrl;

        /**
         * 是否省份开发页面
         * (业务类型H5、H5+传参类):
         * 0：否
         * 1：是
         */
        private Integer isProvincePage;

        /**
         * icon编码, 用于发布方式为4，关联mng_icon_info表
         */
        private String iconCode;

        /**
         * MNG_ICON_INFO表中的数据（专属服务时为labelPageUrl的值）
         */
        private String iconsUrl;

        /**
         * 是否配置标签
         * 0:否，1：是
         */
        private Integer isLabel;

        /**
         * 标签样式:
         * 1:小红点
         * 2:文字角标
         * 3：图片角标
         */
        private Integer labelStyle;

        /**
         * 显示机制：
         * 0：一直显示
         * 1：点击一次消失
         */
        private Integer cornerShowLaw;

        /**
         * 角标文案
         */
        private String cornerTitle;

        /**
         * 角标图片URL
         */
        private String cornerPicUrl;

        /**
         * 【3号模板】
         * 功能id
         */
        private String functionId;

        /**
         * 【5号模板】
         * 未登录是否展示
         * 0:否，1：是
         */
        private Integer isLoginShow;


}
