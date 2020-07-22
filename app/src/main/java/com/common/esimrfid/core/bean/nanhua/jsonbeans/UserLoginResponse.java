package com.common.esimrfid.core.bean.nanhua.jsonbeans;

//登录
public class UserLoginResponse {
    /**
     * userinfo : {"accesses":[],"corpDbName":"db_corp","corpDbUrl":"172.16.78.129","corpid":"tenantid0001","create_date":1573120126000,"id":"caddb5d8014311eab97300163e086c26","menus":[{"create_date":1539139471000,"id":100,"menu_description":"系统管理","menu_icon":"fa fa-home","menu_name":"首页","menu_seq":10,"menu_status":0,"menu_type":"web","menu_url":"/dashboard","update_date":1552985229000,"menu_superid":101},{"create_date":1539139471000,"id":101,"menu_description":"资产相关父级菜单","menu_icon":"fa fa-building","menu_name":"资产管理","menu_seq":20,"menu_status":0,"menu_type":"web","menu_url":"/asset","update_date":1552985229000},{"create_date":1539139471000,"id":103,"menu_description":"申请或审批 领用、调拨、报修、报废、借用","menu_icon":"fa fa-legal","menu_name":"审批","menu_seq":40,"menu_status":0,"menu_type":"web","menu_url":"/approval","update_date":1552985229000},{"create_date":1539139471000,"id":104,"menu_description":"系统相关父级菜单","menu_icon":"fa fa-cogs","menu_name":"系统管理","menu_seq":50,"menu_status":0,"menu_type":"web","menu_url":"/sysmge","update_date":1552985229000},{"create_date":1539139471000,"id":105,"menu_description":"管理仓库入库、出库等，暂不实现","menu_name":"库存管理","menu_seq":60,"menu_status":1,"menu_type":"web","menu_url":"/store","update_date":1552113591000},{"id":106,"menu_description":"任务父级菜单","menu_icon":"fa fa-tasks","menu_name":"我的任务","menu_seq":31,"menu_status":0,"menu_type":"web","menu_url":"/task","update_date":1553058855000},{"id":107,"menu_description":"提交申请","menu_icon":"fa fa-legal","menu_name":"申请","menu_seq":39,"menu_status":0,"menu_type":"web","menu_url":"/apply","update_date":1552985229000},{"create_date":1539139471000,"id":201,"menu_description":"资产的入库、导入导出、添加删除修改等等","menu_icon":"fa fa-television","menu_name":"资产","menu_seq":70,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/info","update_date":1552985229000},{"create_date":1539139471000,"id":203,"menu_description":"管理员操作、不需要审批，直接创建单据及操作的资产列表","menu_icon":"fa fa-hand-grab-o","menu_name":"资产领用","menu_seq":90,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/requisition","update_date":1552985229000},{"create_date":1539139471000,"id":204,"menu_description":"管理员操作、不需要审批，直接创建单据及操作的资产列表","menu_icon":"fa fa-exchange","menu_name":"资产调拨","menu_seq":100,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/allocation","update_date":1552985229000},{"create_date":1539139471000,"id":205,"menu_description":"管理员操作、不需要审批，直接创建单据及操作的资产列表","menu_icon":"fa fa-gavel","menu_name":"资产维修","menu_seq":110,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/maintain","update_date":1552985229000},{"create_date":1539139471000,"id":206,"menu_description":"管理员操作、不需要审批，直接创建单据及操作的资产列表","menu_icon":"fa fa-remove","menu_name":"资产报废","menu_seq":120,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/discard","update_date":1552985229000},{"create_date":1539139471000,"id":207,"menu_description":"管理员操作、不需要审批，提供盘点单的下载","menu_icon":"fa fa-tag","menu_name":"资产盘点","menu_seq":130,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/inventory","update_date":1552985229000},{"create_date":1539139471000,"id":208,"menu_description":"报表相关父级菜单","menu_icon":"fa fa-line-chart","menu_name":"报表","menu_seq":140,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/report","update_date":1552985229000},{"create_date":1539139471000,"id":209,"menu_description":"资产设置相关父级菜单","menu_icon":"fa fa-cogs","menu_name":"设置","menu_seq":150,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/commset","update_date":1552985229000},{"create_date":1539139471000,"id":210,"menu_description":"资产退库","menu_icon":"fa fa-hand-o-left","menu_name":"领用归还","menu_seq":91,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/warehouse","update_date":1553058855000},{"create_date":1539139471000,"id":211,"menu_description":"资产操作日志","menu_icon":"fa fa-calendar-plus-o","menu_name":"资产日志","menu_seq":139,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/optrecord","update_date":1553168207000},{"create_date":1539139471000,"id":241,"menu_description":"企业的组织结构、多级企业部门","menu_icon":"fa fa-bank","menu_name":"组织结构","menu_seq":180,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/organization","update_date":1552985229000},{"create_date":1539139471000,"id":242,"menu_description":"企业及部门员工管理，添加修改删除等操作","menu_icon":"fa fa-user","menu_name":"员工管理","menu_seq":190,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/employee","update_date":1552985229000},{"create_date":1539139471000,"id":243,"menu_description":"系统默认角色、部门相关角色及自定义角色分配权限","menu_icon":"fa fa-user-secret","menu_name":"角色管理","menu_seq":200,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/role","update_date":1552985229000},{"create_date":1539139471000,"id":244,"menu_description":"使用系统的相关用户管理、分配角色等","menu_icon":"fa fa-users","menu_name":"用户管理","menu_seq":210,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/user","update_date":1552985229000},{"create_date":1570848096000,"id":245,"menu_description":"流程管理","menu_icon":"fa fa-flag","menu_name":"流程管理","menu_seq":0,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/process","update_date":1570848099000},{"create_date":1539139471000,"id":260,"menu_description":"盘点任务,可以查看及提交我的任务完成","menu_icon":"fa fa-tag","menu_name":"盘点任务","menu_seq":280,"menu_status":0,"menu_superid":106,"menu_type":"web","menu_url":"/task/inventory","update_date":1552985229000},{"create_date":1539139471000,"id":301,"menu_description":"多条件、多过滤字段定制生成报表","menu_icon":"fa fa-magnet","menu_name":"资产清单","menu_seq":220,"menu_status":0,"menu_superid":208,"menu_type":"web","menu_url":"/asset/report/info","update_date":1552985229000},{"create_date":1539139471000,"id":302,"menu_description":"根据计算折旧、生成报表","menu_icon":"fa fa-magic","menu_name":"折旧清单","menu_seq":230,"menu_status":0,"menu_superid":208,"menu_type":"web","menu_url":"/asset/report/depreciation","update_date":1552985229000},{"create_date":1539139471000,"id":310,"menu_description":"资产的类型增删改","menu_icon":"fa fa-list","menu_name":"资产类型","menu_seq":240,"menu_status":0,"menu_superid":209,"menu_type":"web","menu_url":"/asset/commset/category","update_date":1552985229000},{"create_date":1539139471000,"id":311,"menu_description":"位置的增删改","menu_icon":"fa fa-map-marker","menu_name":"位置","menu_seq":250,"menu_status":0,"menu_superid":209,"menu_type":"web","menu_url":"/asset/commset/location","update_date":1552985229000},{"create_date":1539139471000,"id":312,"menu_description":"资产部分编码的规则设置","menu_icon":"fa fa-newspaper-o","menu_name":"编码规则","menu_seq":260,"menu_status":1,"menu_superid":209,"menu_type":"web","menu_url":"/asset/commset/coderule","update_date":1552985229000},{"create_date":1539139471000,"id":313,"menu_description":"仓库的增删改等设置","menu_icon":"fa fa-university","menu_name":"仓库","menu_seq":270,"menu_status":0,"menu_superid":209,"menu_type":"web","menu_url":"/asset/commset/store","update_date":1552985229000}],"roles":[{"create_date":1551867934000,"id":"sys_supermanagerid","menu_ids":[100,201,203,204,205,206,207,301,302,310,311,312,313,210,211,241,242,243,244,245,105,260,null,101,101,208,101,209,104,106,103,107],"role_code":100,"role_name":"超级管理员","role_remark":"超级管理员（注册的管理员，不可删除）","role_status":0,"tenant_id":"default","update_date":1570848171000}],"tenant_id":"tenantid0001","update_date":1573120126000,"userOrganizations":[{"create_date":1573120126000,"id":"d66a8def01e511eab4c100163e086c26","role_id":"sys_supermanagerid","update_date":1573120126000,"user_id":"caddb5d8014311eab97300163e086c26"}],"user_mobile":"17521722212","user_name":"admin","user_password":"670b14728ad9902aecba32e22fa4f6bd","user_real_name":"超级管理员","user_status":"0"}
     * token : 47e93ea0-02d1-42f7-b719-411b44c5fa73
     */

    private Userinfo userinfo;
    private String token;

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class Userinfo {
        /**
         * accesses : []
         * corpDbName : db_corp
         * corpDbUrl : 172.16.78.129
         * corpid : tenantid0001
         * create_date : 1573120126000
         * id : caddb5d8014311eab97300163e086c26
         * menus : [{"create_date":1539139471000,"id":100,"menu_description":"系统管理","menu_icon":"fa fa-home","menu_name":"首页","menu_seq":10,"menu_status":0,"menu_type":"web","menu_url":"/dashboard","update_date":1552985229000},{"create_date":1539139471000,"id":101,"menu_description":"资产相关父级菜单","menu_icon":"fa fa-building","menu_name":"资产管理","menu_seq":20,"menu_status":0,"menu_type":"web","menu_url":"/asset","update_date":1552985229000},{"create_date":1539139471000,"id":103,"menu_description":"申请或审批 领用、调拨、报修、报废、借用","menu_icon":"fa fa-legal","menu_name":"审批","menu_seq":40,"menu_status":0,"menu_type":"web","menu_url":"/approval","update_date":1552985229000},{"create_date":1539139471000,"id":104,"menu_description":"系统相关父级菜单","menu_icon":"fa fa-cogs","menu_name":"系统管理","menu_seq":50,"menu_status":0,"menu_type":"web","menu_url":"/sysmge","update_date":1552985229000},{"create_date":1539139471000,"id":105,"menu_description":"管理仓库入库、出库等，暂不实现","menu_name":"库存管理","menu_seq":60,"menu_status":1,"menu_type":"web","menu_url":"/store","update_date":1552113591000},{"id":106,"menu_description":"任务父级菜单","menu_icon":"fa fa-tasks","menu_name":"我的任务","menu_seq":31,"menu_status":0,"menu_type":"web","menu_url":"/task","update_date":1553058855000},{"id":107,"menu_description":"提交申请","menu_icon":"fa fa-legal","menu_name":"申请","menu_seq":39,"menu_status":0,"menu_type":"web","menu_url":"/apply","update_date":1552985229000},{"create_date":1539139471000,"id":201,"menu_description":"资产的入库、导入导出、添加删除修改等等","menu_icon":"fa fa-television","menu_name":"资产","menu_seq":70,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/info","update_date":1552985229000},{"create_date":1539139471000,"id":203,"menu_description":"管理员操作、不需要审批，直接创建单据及操作的资产列表","menu_icon":"fa fa-hand-grab-o","menu_name":"资产领用","menu_seq":90,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/requisition","update_date":1552985229000},{"create_date":1539139471000,"id":204,"menu_description":"管理员操作、不需要审批，直接创建单据及操作的资产列表","menu_icon":"fa fa-exchange","menu_name":"资产调拨","menu_seq":100,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/allocation","update_date":1552985229000},{"create_date":1539139471000,"id":205,"menu_description":"管理员操作、不需要审批，直接创建单据及操作的资产列表","menu_icon":"fa fa-gavel","menu_name":"资产维修","menu_seq":110,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/maintain","update_date":1552985229000},{"create_date":1539139471000,"id":206,"menu_description":"管理员操作、不需要审批，直接创建单据及操作的资产列表","menu_icon":"fa fa-remove","menu_name":"资产报废","menu_seq":120,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/discard","update_date":1552985229000},{"create_date":1539139471000,"id":207,"menu_description":"管理员操作、不需要审批，提供盘点单的下载","menu_icon":"fa fa-tag","menu_name":"资产盘点","menu_seq":130,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/inventory","update_date":1552985229000},{"create_date":1539139471000,"id":208,"menu_description":"报表相关父级菜单","menu_icon":"fa fa-line-chart","menu_name":"报表","menu_seq":140,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/report","update_date":1552985229000},{"create_date":1539139471000,"id":209,"menu_description":"资产设置相关父级菜单","menu_icon":"fa fa-cogs","menu_name":"设置","menu_seq":150,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/commset","update_date":1552985229000},{"create_date":1539139471000,"id":210,"menu_description":"资产退库","menu_icon":"fa fa-hand-o-left","menu_name":"领用归还","menu_seq":91,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/warehouse","update_date":1553058855000},{"create_date":1539139471000,"id":211,"menu_description":"资产操作日志","menu_icon":"fa fa-calendar-plus-o","menu_name":"资产日志","menu_seq":139,"menu_status":0,"menu_superid":101,"menu_type":"web","menu_url":"/asset/optrecord","update_date":1553168207000},{"create_date":1539139471000,"id":241,"menu_description":"企业的组织结构、多级企业部门","menu_icon":"fa fa-bank","menu_name":"组织结构","menu_seq":180,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/organization","update_date":1552985229000},{"create_date":1539139471000,"id":242,"menu_description":"企业及部门员工管理，添加修改删除等操作","menu_icon":"fa fa-user","menu_name":"员工管理","menu_seq":190,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/employee","update_date":1552985229000},{"create_date":1539139471000,"id":243,"menu_description":"系统默认角色、部门相关角色及自定义角色分配权限","menu_icon":"fa fa-user-secret","menu_name":"角色管理","menu_seq":200,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/role","update_date":1552985229000},{"create_date":1539139471000,"id":244,"menu_description":"使用系统的相关用户管理、分配角色等","menu_icon":"fa fa-users","menu_name":"用户管理","menu_seq":210,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/user","update_date":1552985229000},{"create_date":1570848096000,"id":245,"menu_description":"流程管理","menu_icon":"fa fa-flag","menu_name":"流程管理","menu_seq":0,"menu_status":0,"menu_superid":104,"menu_type":"web","menu_url":"/sysmge/process","update_date":1570848099000},{"create_date":1539139471000,"id":260,"menu_description":"盘点任务,可以查看及提交我的任务完成","menu_icon":"fa fa-tag","menu_name":"盘点任务","menu_seq":280,"menu_status":0,"menu_superid":106,"menu_type":"web","menu_url":"/task/inventory","update_date":1552985229000},{"create_date":1539139471000,"id":301,"menu_description":"多条件、多过滤字段定制生成报表","menu_icon":"fa fa-magnet","menu_name":"资产清单","menu_seq":220,"menu_status":0,"menu_superid":208,"menu_type":"web","menu_url":"/asset/report/info","update_date":1552985229000},{"create_date":1539139471000,"id":302,"menu_description":"根据计算折旧、生成报表","menu_icon":"fa fa-magic","menu_name":"折旧清单","menu_seq":230,"menu_status":0,"menu_superid":208,"menu_type":"web","menu_url":"/asset/report/depreciation","update_date":1552985229000},{"create_date":1539139471000,"id":310,"menu_description":"资产的类型增删改","menu_icon":"fa fa-list","menu_name":"资产类型","menu_seq":240,"menu_status":0,"menu_superid":209,"menu_type":"web","menu_url":"/asset/commset/category","update_date":1552985229000},{"create_date":1539139471000,"id":311,"menu_description":"位置的增删改","menu_icon":"fa fa-map-marker","menu_name":"位置","menu_seq":250,"menu_status":0,"menu_superid":209,"menu_type":"web","menu_url":"/asset/commset/location","update_date":1552985229000},{"create_date":1539139471000,"id":312,"menu_description":"资产部分编码的规则设置","menu_icon":"fa fa-newspaper-o","menu_name":"编码规则","menu_seq":260,"menu_status":1,"menu_superid":209,"menu_type":"web","menu_url":"/asset/commset/coderule","update_date":1552985229000},{"create_date":1539139471000,"id":313,"menu_description":"仓库的增删改等设置","menu_icon":"fa fa-university","menu_name":"仓库","menu_seq":270,"menu_status":0,"menu_superid":209,"menu_type":"web","menu_url":"/asset/commset/store","update_date":1552985229000}]
         * roles : [{"create_date":1551867934000,"id":"sys_supermanagerid","menu_ids":[100,201,203,204,205,206,207,301,302,310,311,312,313,210,211,241,242,243,244,245,105,260,null,101,101,208,101,209,104,106,103,107],"role_code":100,"role_name":"超级管理员","role_remark":"超级管理员（注册的管理员，不可删除）","role_status":0,"tenant_id":"default","update_date":1570848171000}]
         * tenant_id : tenantid0001
         * update_date : 1573120126000
         * userOrganizations : [{"create_date":1573120126000,"id":"d66a8def01e511eab4c100163e086c26","role_id":"sys_supermanagerid","update_date":1573120126000,"user_id":"caddb5d8014311eab97300163e086c26"}]
         * user_mobile : 17521722212
         * user_name : admin
         * user_password : 670b14728ad9902aecba32e22fa4f6bd
         * user_real_name : 超级管理员
         * user_status : 0
         */
        private String id;
        private String user_real_name;
        private String user_name;
        private String corpName;
        private String corpid;

        public String getCorpName() {
            return corpName;
        }

        public void setCorpName(String corpName) {
            this.corpName = corpName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_real_name() {
            return user_real_name;
        }

        public void setUser_real_name(String user_real_name) {
            this.user_real_name = user_real_name;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getCorpid() {
            return corpid;
        }

        public void setCorpid(String corpid) {
            this.corpid = corpid;
        }
    }
}
